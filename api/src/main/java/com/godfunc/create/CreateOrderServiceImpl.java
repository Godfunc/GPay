package com.godfunc.create;

import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.godfunc.dto.PayOrderDTO;
import com.godfunc.entity.*;
import com.godfunc.enums.*;
import com.godfunc.exception.GException;
import com.godfunc.model.MerchantAgentProfit;
import com.godfunc.model.PayChannelAccountJoint;
import com.godfunc.param.PayOrderParam;
import com.godfunc.pay.PayOrderService;
import com.godfunc.queue.OrderExpireQueue;
import com.godfunc.result.ApiCode;
import com.godfunc.result.ApiMsg;
import com.godfunc.service.*;
import com.godfunc.pay.interceptor.EarlyProcessorComposite;
import com.godfunc.util.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.*;


/**
 * @author Godfunc
 * @email godfunc@outlook.com
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class CreateOrderServiceImpl implements CreateOrderService {

    @Value("${gopay}")
    private String goPayUrl;

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
    private final OrderExpireQueue orderExpireQueue;


    @Override
    public PayOrderDTO create(PayOrderParam param, HttpServletRequest request) {
        ValidatorUtils.validate(param);

        // 外部风控
        if (earlyProcessorComposite.support(param)) {
            if (!earlyProcessorComposite.check(request, param)) {
                throw new GException("系统繁忙，请稍后再试");
            }
        }
        Merchant merchant = merchantService.getByCode(param.getMerchantCode());

        Assert.isNull(merchant, "商户不存在");
        // 验证签名
        Assert.isTrue(!SignUtils.rsa2Check(param, merchant.getPublicKey(), param.getSign()), "签名验证未通过，请检查签名是否正确");
        // 检查商户状态
        Assert.isTrue(merchant.getStatus() != MerchantStatusEnum.ENABLE.getValue(), "商户被禁用");
        Assert.isTrue(merchant.getType() != MerchantTypeEnum.MERCHANT.getValue(), "当前商户类型不支持创建订单");

        // 检查商户代理的状态
        List<Merchant> agentList = new ArrayList<>();
        Assert.isTrue(!merchantService.checkAgent(merchant, agentList), "当前商户上游代理已被禁用");

        // 检查订单是否已存在
        Assert.isTrue(orderService.checkExist(param.getOutTradeNo(), merchant.getCode()), "订单号已存在");

        // 设置订单基本信息
        Long centAmount = AmountUtil.convertDollar2Cent(param.getAmount());
        Order order = new Order();
        order.setId(IdWorker.getId());
        order.setOutTradeNo(param.getOutTradeNo());
        order.setOrderNo(IdWorker.getIdStr());
        order.setMerchantId(merchant.getId());
        order.setMerchantCode(merchant.getCode());
        order.setMerchantName(merchant.getName());
        order.setClientCreateTime(param.getTime());
        order.setPayType(param.getType());
        order.setAmount(centAmount);

        // 商户风控
        if (!merchantRiskService.riskMerchant(merchant.getId(), order)) {
            throw new GException("订单创建失败，请检查订单参数是否正确");
        }

        // 找渠道
        PayCategory payCategory = payCategoryService.getByCode(param.getType());
        Assert.isNull(payCategory, "没有对应的支付渠道");
        Assert.isTrue(payCategory.getStatus() != PayCategoryStatusEnum.ENABLE.getValue(), "没有可用的支付渠道");

        // 订单详细信息
        OrderDetail detail = new OrderDetail();
        detail.setOrderId(order.getId());
        detail.setPayCategoryId(payCategory.getId());
        detail.setMerchantId(merchant.getId());
        detail.setMerchantCode(merchant.getCode());
        detail.setMerchantName(merchant.getName());
        detail.setClientIp(param.getClientIp());

        // 渠道风控（粗略的限额，后面请求支付时进行详细限额）
        List<PayChannel> payChannelList = payChannelService.getEnableByRisk(payCategory.getId(), order);
        if (CollectionUtils.isEmpty(payChannelList)) {
            throw new GException("没有可用的支付渠道");
        }

        // 选择渠道和渠道账号
        PayChannelAccountJoint payChannelAccountJoint = payChannelAccountService.getEnableByWeight(payChannelList, order);
        PayChannel payChannel = payChannelAccountJoint.getPayChannel();
        PayChannelAccount payChannelAccount = payChannelAccountJoint.getPayChannelAccount();
        Assert.isNull(payChannelAccount, "没有可用的渠道账号");

        order.setChannelAccountCode(payChannelAccount.getAccountCode());

        detail.setPayChannelId(payChannel.getId());
        detail.setPayCategoryChannelId(payChannel.getCategoryChannelId());
        detail.setPayChannelAccountId(payChannelAccount.getId());
        detail.setPayChannelAccountCode(payChannelAccount.getAccountCode());
        detail.setPayChannelAccountKeyInfo(payChannelAccount.getKeyInfo());
        detail.setChannelCreateUrl(payChannel.getCreateUrl());
        detail.setChannelQueryUrl(payChannel.getQueryUrl());
        detail.setChannelNotifyUrl(payChannel.getNotifyUrl());
        detail.setChannelPayTypeInfo(payChannel.getPayTypeInfo());
        detail.setChannelCostRate(payChannel.getCostRate());
        detail.setLogicalTag(payChannel.getLogicalTag());

        String expiredTime = configService.getByName(ConfigNameEnum.ORDER_EXPIRED_TIME.getValue());
        Assert.isBlank(expiredTime, "初始化参数未设置完整，请联系管理员");
        detail.setOrderExpiredTime(LocalDateTime.now().plusSeconds(Long.parseLong(expiredTime)));

        // 计算收益
        MerchantAgentProfit merchantAgentProfit = merchantOrderProfitService.calc(merchant, agentList, order, detail);
        PlatformOrderProfit platformOrderProfit = platformOrderProfitService.calc(merchantAgentProfit, order, detail);

        // 创建订单以及详细
        boolean flag = false;
        try {
            flag = orderService.create(order, detail, merchantAgentProfit, platformOrderProfit);
        } catch (DuplicateKeyException e) {
            throw new GException("单号已存在，请检查您的订单号");
        }
        Assert.isTrue(!flag, "订单创建失败");

        // 添加到过期队列中
        intoExpireQueue(order);
        
        // 签名返回
        PayOrderDTO payOrderDTO = new PayOrderDTO(order.getOutTradeNo(), order.getOrderNo(), goPayUrl + order.getOrderNo(), LocalDateTime.now().plusMinutes(10));
        payOrderDTO.setSign(SignUtils.rsa2Sign(payOrderDTO, merchant.getPlatPrivateKey()));
        return payOrderDTO;
    }

    @Override
    public void create(PayOrderParam param, HttpServletRequest request, HttpServletResponse response) {
        try {
            PayOrderDTO payOrderDTO = create(param, request);
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
        String html = configService.getByName(ConfigNameEnum.ORDER_ERROR_PAGE.getValue());
        Assert.isBlank(html, "订单错误页未配置，请联系管理员！");

        response.setContentType(MediaType.TEXT_HTML_VALUE);
        response.setCharacterEncoding(CharsetEnum.UTF8.getValue());
        try {
            response.getWriter().write(HtmlPlaceholderUtils.replacePlaceholder(errorMap, html));
        } catch (IOException e) {
            log.error("response write异常", e);
        }
    }

    private void intoExpireQueue(Order order) {
        OrderExpireQueue.OrderExpire orderExpire = new OrderExpireQueue.OrderExpire();
        orderExpire.setId(order.getId());
        orderExpire.setCreateTime(order.getCreateTime());
        orderExpire.setAmount(order.getAmount());
        orderExpire.setExpiredTime(order.getDetail().getOrderExpiredTime());
        orderExpire.setStatus(order.getStatus());
        orderExpireQueue.push(orderExpire);
    }
}
