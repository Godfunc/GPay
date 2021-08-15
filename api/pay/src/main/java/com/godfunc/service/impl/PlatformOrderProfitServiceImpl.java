package com.godfunc.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.godfunc.entity.MerchantOrderProfit;
import com.godfunc.entity.Order;
import com.godfunc.entity.OrderDetail;
import com.godfunc.entity.PlatformOrderProfit;
import com.godfunc.mapper.PlatformOrderProfitMapper;
import com.godfunc.model.MerchantAgentProfit;
import com.godfunc.service.PlatformOrderProfitService;
import lombok.RequiredArgsConstructor;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.stereotype.Service;

import java.util.List;


/**
 * @author Godfunc
 * @email godfunc@outlook.com
 */
@Service
@RequiredArgsConstructor
public class PlatformOrderProfitServiceImpl extends ServiceImpl<PlatformOrderProfitMapper, PlatformOrderProfit> implements PlatformOrderProfitService {
    @Override
    public PlatformOrderProfit calc(MerchantAgentProfit merchantAgentProfit, Order order) {
        final Long amount = order.getAmount();
        OrderDetail detail = order.getDetail();
        MerchantOrderProfit merchantProfit = merchantAgentProfit.getMerchantProfit();
        long platProfit = amount - merchantProfit.getProfitAmount();
        List<MerchantOrderProfit> agentProfitList = merchantAgentProfit.getAgentProfitList();
        if (CollectionUtils.isNotEmpty(agentProfitList)) {
            platProfit -= agentProfitList.parallelStream().mapToLong(MerchantOrderProfit::getProfitAmount).sum();
        }
        long channelBase = Double.valueOf(Math.floor(amount * detail.getChannelCostRate())).longValue();
        return new PlatformOrderProfit(order.getId(), amount, merchantProfit.getMerchantId(), merchantProfit.getMerchantCode(), detail.getChannelCostRate(), platProfit - channelBase, channelBase);
    }
}
