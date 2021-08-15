package com.godfunc.create.impl;

import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.godfunc.create.CreateOrderService;
import com.godfunc.dto.PayOrderDTO;
import com.godfunc.entity.*;
import com.godfunc.enums.*;
import com.godfunc.exception.GException;
import com.godfunc.model.MerchantAgentProfit;
import com.godfunc.model.PayChannelAccountJoint;
import com.godfunc.model.ProfitJoint;
import com.godfunc.param.PayOrderParam;
import com.godfunc.pay.PayOrderService;
import com.godfunc.pay.interceptor.EarlyProcessorComposite;
import com.godfunc.result.ApiCode;
import com.godfunc.result.ApiMsg;
import com.godfunc.service.*;
import com.godfunc.util.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * @author Godfunc
 * @email godfunc@outlook.com
 */
@Slf4j
@Service
@RefreshScope
@RequiredArgsConstructor
public class CreateOrderServiceImpl implements CreateOrderService {

    private final EarlyProcessorComposite earlyProcessorComposite;
    private final MerchantService merchantService;
    private final OrderService orderService;
    private final MerchantRiskService merchantRiskService;
    private final PayCategoryService payCategoryService;
    private final PayChannelService payChannelService;
    private final PayChannelAccountService payChannelAccountService;
    private final PlatformOrderProfitService platformOrderProfitService;
    private final MerchantOrderProfitService merchantOrderProfitService;
    private final PayOrderService payOrderService;
    private final ConfigService configService;
    @Value("${goPayUrl}")
    private String goPayUrl;

    @Override
    public PayOrderDTO create(PayOrderParam param, HttpServletRequest request) {
        return create(true, param, request);
    }

    @Override
    public void create(PayOrderParam param, HttpServletRequest request, HttpServletResponse response) {
        try {
            PayOrderDTO payOrderDTO = create(false, param, request);
            payOrderService.goPay(payOrderDTO.getTradNo(), request, response);
        } catch (GException e) {
            Map<String, Object> errorMap = new HashMap<>(2);
            errorMap.put("code", e.getCode());
            errorMap.put("msg", e.getMsg());
            errorPage(errorMap, response);
        } catch (Exception e) {
            Map<String, Object> errorMap = new HashMap<>(2);
            errorMap.put("code", ApiCode.FAIL);
            errorMap.put("msg", ApiMsg.SYSTEM_BUSY);
            errorPage(errorMap, response);
        }
    }

    private void errorPage(Map<String, Object> errorMap, HttpServletResponse response) {
        // TODO 看 thymeleaf 怎么做的占位符替换
        // TODO 缓存一下这个页面数据
        Config config = configService.getByName(ConfigNameEnum.ORDER_ERROR_PAGE.getValue());
        Assert.isTrue(config == null || StringUtils.isBlank(config.getValue()), "订单错误页未配置，请联系管理员！");

        response.setContentType(MediaType.TEXT_HTML_VALUE);
        response.setCharacterEncoding(CharsetEnum.UTF8.getValue());
        try {
            response.getWriter().write(HtmlPlaceholderUtils.replacePlaceholder(errorMap, config.getValue()));
        } catch (IOException e) {
            log.error("response write异常", e);
        }
    }

    /**
     * 下单方法
     *
     * @param isSign  是否签名
     * @param param   请求参数
     * @param request 请求体
     * @return 返回支付对象
     */
    public PayOrderDTO create(boolean isSign, PayOrderParam param, HttpServletRequest request) {
        // 外部风控
        invokeEarlyProcessor(param, request);

        Merchant merchant = merchantService.getByCode(param.getMerchantCode());
        Assert.isNull(merchant, "商户不存在");

        // 验证签名
        signCheck(param, merchant.getPublicKey());

        // 检查商户状态 检查商户代理的状态
        List<Merchant> agentList = merchantCheck(merchant);

        // 检查订单是否已存在
        orderExistCheck(param.getOutTradeNo(), merchant.getCode());

        // 设置订单基本信息
        Order order = newOrder(param, merchant);

        // 商户风控
        merchantRisk(merchant, order);

        // 找渠道
        PayCategory payCategory = getCategory(param.getType());

        // 渠道风控（粗略的限额，后面请求支付时进行详细限额） 选择渠道和渠道账号
        PayChannelAccountJoint payChannelAccountJoint = getChannelAccounts(payCategory.getId(), order);

        // 订单详细信息
        OrderDetail detail = newDetailAndSetOrderChannel(param, payCategory, payChannelAccountJoint, merchant, order);

        // 设置订单过期时间
        setOrderExpireTime(detail);

        // 计算收益
        ProfitJoint profitJoint = calcProfit(merchant, agentList, order);

        // 创建订单以及详细
        saveOrder(order, profitJoint);

        // 返回
        return result(isSign, order, merchant.getPlatPrivateKey(), request);
    }


    /**
     * 进行外部风控
     *
     * @param param   请求参数
     * @param request 请求头
     */
    private void invokeEarlyProcessor(PayOrderParam param, HttpServletRequest request) {
        if (earlyProcessorComposite.support(param)) {
            if (!earlyProcessorComposite.check(request, param)) {
                throw new GException("系统繁忙，请稍后再试");
            }
        }
    }

    /**
     * 签名验证
     *
     * @param param     请求参数
     * @param publicKey 商户公钥
     */
    private void signCheck(PayOrderParam param, String publicKey) {
        Assert.isTrue(!SignUtils.rsa2Check(param, publicKey, param.getSign()), "签名验证未通过，请检查签名是否正确");
    }

    /**
     * 检查商户和商户代理的状态
     *
     * @param merchant 商户
     * @return 商户代理的集合
     */
    private List<Merchant> merchantCheck(Merchant merchant) {
        Assert.isTrue(merchant.getStatus() != MerchantStatusEnum.ENABLE.getValue(), "商户被禁用");
        Assert.isTrue(merchant.getType() != MerchantTypeEnum.MERCHANT.getValue(), "当前商户类型不支持创建订单");
        List<Merchant> agentList = new ArrayList<>();
        Assert.isTrue(!merchantService.checkAgent(merchant, agentList), "当前商户上游代理已被禁用");
        return agentList;
    }

    /**
     * 检查商户单号是否重复
     *
     * @param outTradeNo   商户单号
     * @param merchantCode 商户号
     */
    private void orderExistCheck(String outTradeNo, String merchantCode) {
        Assert.isTrue(orderService.checkExist(outTradeNo, merchantCode), "订单号已存在");
    }

    /**
     * 创建订单对象
     *
     * @param param    请求参数
     * @param merchant 商户信息
     * @return 订单对象
     */
    public Order newOrder(PayOrderParam param, Merchant merchant) {
        Long centAmount = AmountUtil.convertDollar2Cent(param.getAmount());
        return new Order()
                .setId(IdWorker.getId())
                .setOutTradeNo(param.getOutTradeNo())
                .setOrderNo(IdWorker.getIdStr())
                .setMerchantId(merchant.getId())
                .setMerchantCode(merchant.getCode())
                .setMerchantName(merchant.getName())
                .setClientCreateTime(param.getTime())
                .setPayType(param.getType())
                .setAmount(centAmount)
                .setRealAmount(centAmount)
                .setStatus(OrderStatusEnum.CREATED.getValue())
                .setNotifyUrl(param.getNotifyUrl());
    }

    private void merchantRisk(Merchant merchant, Order order) {
        if (!merchantRiskService.riskMerchant(merchant.getId(), order)) {
            throw new GException("订单创建失败，请检查订单参数是否正确");
        }
    }

    /**
     * 根据请求参数type查询渠道主类
     *
     * @param type 支付渠道类型
     * @return 渠道主类
     */
    private PayCategory getCategory(String type) {
        PayCategory payCategory = payCategoryService.getByCode(type);
        Assert.isNull(payCategory, "没有对应的支付渠道");
        Assert.isTrue(payCategory.getStatus() != PayCategoryStatusEnum.ENABLE.getValue(), "没有可用的支付渠道");
        return payCategory;
    }

    /**
     * 根据渠道主类选取未被风控的渠道子类(一个)和渠道账号(一个)，会根据权重选取渠道子类和账号
     *
     * @param payCategoryId 渠道主类id
     * @param order         订单(用来进行风控)
     * @return 渠道子类和渠道账号
     */
    private PayChannelAccountJoint getChannelAccounts(Long payCategoryId, Order order) {
        List<PayChannel> payChannelList = payChannelService.getEnableByRisk(payCategoryId, order);
        if (CollectionUtils.isEmpty(payChannelList)) {
            throw new GException("没有可用的支付渠道");
        }
        PayChannelAccountJoint payChannelAccountJoint = payChannelAccountService.getEnableByWeight(payChannelList, order);
        Assert.isNull(payChannelAccountJoint.getPayChannelAccount(), "没有可用的渠道账号");
        return payChannelAccountJoint;
    }

    /**
     * 生成订单详细信息对象
     *
     * @param param                  请求参数
     * @param payCategory            渠道主类
     * @param payChannelAccountJoint 渠道子类和渠道账号
     * @param merchant               商户
     * @param order                  订单
     * @return 订单详细信息
     */
    private OrderDetail newDetailAndSetOrderChannel(PayOrderParam param, PayCategory payCategory, PayChannelAccountJoint payChannelAccountJoint, Merchant merchant, Order order) {
        PayChannel payChannel = payChannelAccountJoint.getPayChannel();
        PayChannelAccount payChannelAccount = payChannelAccountJoint.getPayChannelAccount();
        OrderDetail detail = new OrderDetail()
                .setOrderId(order.getId())
                .setPayCategoryId(payCategory.getId())
                .setMerchantId(merchant.getId())
                .setMerchantCode(merchant.getCode())
                .setMerchantName(merchant.getName())
                .setClientIp(param.getClientIp())
                .setPlatPrivateKey(merchant.getPlatPrivateKey())
                .setGoodName(param.getGoodName())
                .setPayChannelId(payChannel.getId())
                .setPayChannelAccountId(payChannelAccount.getId())
                .setPayChannelAccountCode(payChannelAccount.getAccountCode())
                .setPayChannelAccountKeyInfo(payChannelAccount.getKeyInfo())
                .setChannelCreateUrl(payChannel.getCreateUrl())
                .setChannelQueryUrl(payChannel.getQueryUrl())
                .setChannelNotifyUrl(payChannel.getNotifyUrl())
                .setChannelPayTypeInfo(payChannel.getPayTypeInfo())
                .setChannelCostRate(payChannel.getCostRate())
                .setLogicalTag(payChannel.getLogicalTag());
        order.setDetail(detail);
        order.setChannelAccountCode(payChannelAccount.getAccountCode());
        return detail;
    }

    private void setOrderExpireTime(OrderDetail detail) {
        Config expireConfig = configService.getByName(ConfigNameEnum.ORDER_EXPIRED_TIME.getValue());
        Assert.isNull(expireConfig, "初始化参数未设置，请联系管理员");
        Assert.isBlank(expireConfig.getValue(), "初始化参数value未设置完整，请联系管理员");
        detail.setOrderExpiredTime(LocalDateTime.now().plusSeconds(Long.parseLong(expireConfig.getValue())));
    }

    /**
     * 计算商户(代理)收益和平台收益
     *
     * @param merchant  商户
     * @param agentList 商户代理列表
     * @param order     订单
     * @return 商户(代理)收益和平台收益
     */
    private ProfitJoint calcProfit(Merchant merchant, List<Merchant> agentList, Order order) {
        MerchantAgentProfit merchantAgentProfit = merchantOrderProfitService.calc(merchant, agentList, order);
        PlatformOrderProfit platformOrderProfit = platformOrderProfitService.calc(merchantAgentProfit, order);
        return new ProfitJoint(merchantAgentProfit, platformOrderProfit);
    }

    private void saveOrder(Order order, ProfitJoint profitJoint) {
        boolean flag = false;
        try {
            flag = orderService.create(order, profitJoint);
        } catch (DuplicateKeyException e) {
            throw new GException("单号已存在，请检查您的订单号");
        } catch (Exception e) {
            log.error("创建订单异常", e);
            throw new GException(ApiMsg.SYSTEM_BUSY);
        }
        Assert.isTrue(!flag, "订单创建失败");
    }

    /**
     * 生成返回对象
     *
     * @param isSign         是否签名
     * @param order          订单
     * @param platPrivateKey 平台私钥
     * @param request        请求体
     * @return 返回支付订单对象
     */
    private PayOrderDTO result(boolean isSign, Order order, String platPrivateKey, HttpServletRequest request) {
        PayOrderDTO payOrderDTO = new PayOrderDTO(order.getOutTradeNo(), order.getOrderNo(), HostUtils.getFullHost(request) + goPayUrl + order.getOrderNo(), order.getDetail().getOrderExpiredTime());
        if (isSign) {
            payOrderDTO.setSign(SignUtils.rsa2Sign(payOrderDTO, platPrivateKey));
        }
        return payOrderDTO;
    }
}
