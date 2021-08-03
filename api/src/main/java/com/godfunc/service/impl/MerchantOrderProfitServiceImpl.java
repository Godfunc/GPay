package com.godfunc.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.godfunc.entity.*;
import com.godfunc.mapper.MerchantOrderProfitMapper;
import com.godfunc.model.MerchantAgentProfit;
import com.godfunc.service.MerchantChannelRateService;
import com.godfunc.service.MerchantOrderProfitService;
import com.godfunc.util.Assert;
import lombok.RequiredArgsConstructor;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;


/**
 * @author Godfunc
 * @email godfunc@outlook.com
 */
@Service
@RequiredArgsConstructor
public class MerchantOrderProfitServiceImpl extends ServiceImpl<MerchantOrderProfitMapper, MerchantOrderProfit> implements MerchantOrderProfitService {

    private final MerchantChannelRateService merchantChannelRateService;

    @Override
    public MerchantAgentProfit calc(Merchant merchant, List<Merchant> agentList, Order order, OrderDetail detail) {
        final Long amount = order.getAmount();
        final Long payCategoryId = detail.getPayCategoryId();
        final Long payChannelId = detail.getPayChannelId();
        MerchantChannelRate merchantChannelRate = merchantChannelRateService.getByMerchant(merchant.getCode(), payCategoryId, payChannelId);
        Assert.isNull(merchantChannelRate, "商户费率未设置，请先设置");
        float merchantProfit = amount * (1 - merchantChannelRate.getRate());
        MerchantAgentProfit merchantAgentProfit = new MerchantAgentProfit();
        MerchantOrderProfit merchantOrderProfit = new MerchantOrderProfit(order.getId(), amount,
                merchant.getId(), merchant.getCode(), merchantChannelRate.getRate(),
                Double.valueOf(Math.floor(merchantProfit)).longValue());
        merchantAgentProfit.setMerchantProfit(merchantOrderProfit);

        if (CollectionUtils.isNotEmpty(agentList)) {
            List<MerchantOrderProfit> agentOrderProfitList = new ArrayList<>(agentList.size());
            MerchantChannelRate nextChannelRate = merchantChannelRate;
            for (int i = 0; i < agentList.size(); i++) {
                Merchant agent = agentList.get(i);
                MerchantChannelRate agentChannelRate = merchantChannelRateService.getByMerchant(agent.getCode(), payCategoryId, payChannelId);
                Assert.isNull(agentChannelRate, "代理费率未设置，请先设置");
                float agentProfit = amount * (nextChannelRate.getRate() - agentChannelRate.getRate());
                agentOrderProfitList.add(new MerchantOrderProfit(order.getId(), amount,
                        agent.getId(), agent.getCode(), agentChannelRate.getRate(), Double.valueOf(Math.floor(agentProfit)).longValue()));
                nextChannelRate = agentChannelRate;
            }
            merchantAgentProfit.setAgentProfitList(agentOrderProfitList);
        }
        return merchantAgentProfit;
    }
}
