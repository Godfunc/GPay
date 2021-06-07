package com.godfunc.service.impl;

import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.godfunc.dto.PayOrderDTO;
import com.godfunc.entity.*;
import com.godfunc.enums.MerchantStatusEnum;
import com.godfunc.enums.MerchantTypeEnum;
import com.godfunc.enums.PayCategoryStatusEnum;
import com.godfunc.exception.GException;
import com.godfunc.model.MerchantAgentProfit;
import com.godfunc.model.PayChannelAccountJoint;
import com.godfunc.param.PayOrderParam;
import com.godfunc.service.*;
import com.godfunc.util.AmountUtil;
import com.godfunc.util.Assert;
import com.godfunc.util.SignUtils;
import com.godfunc.util.ValidatorUtils;
import lombok.RequiredArgsConstructor;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;


/**
 * @author Godfunc
 * @email godfunc@outlook.com
 */
@Service
@RequiredArgsConstructor
public class PayOrderServiceImpl implements PayOrderService {

    @Value("#{gopay}")
    private final String goPayUrl;

    private final EarlyProcessorComposite earlyProcessorComposite;
    private final MerchantService merchantService;
    private final OrderService orderService;
    private final MerchantRiskService merchantRiskService;
    private final PayCategoryService payCategoryService;
    private final PayChannelService payChannelService;
    private final PayChannelAccountService payChannelAccountService;
    private final PlatformOrderProfitService platformOrderProfitService;
    private final MerchantOrderProfitService merchantOrderProfitService;


    @Override
    public PayOrderDTO create(PayOrderParam param, HttpServletRequest request, HttpServletResponse response) {
        ValidatorUtils.validate(param);

        // 外部风控
        if (earlyProcessorComposite.support(param)) {
            if (!earlyProcessorComposite.check(request, param)) {
                throw new GException("系统繁忙，请稍后再试");
            }
        }
        Merchant merchant = merchantService.getByCode(param.getMerchantCode());

        // 验证签名
        Assert.isTrue(!SignUtils.rsa2Check(param, merchant.getPublicKey(), param.getSign()), "签名验证未通过，请检查签名是否正确");

        // 检查商户状态
        Assert.isNull(merchant, "商户不存在");
        Assert.isTrue(merchant.getStatus() != MerchantStatusEnum.ENABLE.getValue(), "商户被禁用");
        Assert.isTrue(merchant.getType() != MerchantTypeEnum.MERCHANT.getValue(), "当前商户类型不支持创建订单");

        // 检查商户代理的状态
        List<Merchant> agentList = new ArrayList<>();
        Assert.isTrue(!merchantService.checkAgent(merchant, agentList), "当前商户上游代理已被禁用");

        // 检查订单是否已存在
        Assert.isTrue(orderService.checkExist(param.getOutTradeNo()), "订单号已存在");

        // 设置订单基本信息
        Long centAmount = AmountUtil.convertDollar2Cent(param.getAmount());
        Order order = new Order();
        order.setId(IdWorker.getId());
        order.setOutTradeNo(param.getOutTradeNo());
        order.setMerchantId(merchant.getId());
        order.setMerchantCode(merchant.getCode());
        order.setMerchantName(merchant.getName());
        order.setClientCreateTime(param.getTime());
        order.setClientIp(param.getClientIp());
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
        detail.setChannelCreateUrl(payChannel.getCreateUrl());
        detail.setChannelQueryUrl(payChannel.getQueryUrl());
        detail.setChannelNotifyUrl(payChannel.getNotifyUrl());
        detail.setChannelPayTypeInfo(payChannel.getPayTypeInfo());
        detail.setChannelCostRate(payChannel.getCostRate());
        detail.setLogicalTag(payChannel.getLogicalTag());

        // 计算收益
        MerchantAgentProfit merchantAgentProfit = merchantOrderProfitService.calc(merchant, agentList, order, detail);
        PlatformOrderProfit platformOrderProfit = platformOrderProfitService.calc(merchantAgentProfit, order, detail);

        // 创建订单以及详细
        boolean flag = orderService.create(order, detail, merchantAgentProfit, platformOrderProfit);
        Assert.isTrue(!flag, "订单创建失败");

        // 签名返回
        PayOrderDTO payOrderDTO = new PayOrderDTO(order.getOutTradeNo(), order.getTradeNo(), goPayUrl + order.getTradeNo(), LocalDateTime.now().plusMinutes(10));
        payOrderDTO.setSign(SignUtils.rsa2Sign(payOrderDTO, merchant.getPlatPrivateKey()));
        return payOrderDTO;
    }
}
