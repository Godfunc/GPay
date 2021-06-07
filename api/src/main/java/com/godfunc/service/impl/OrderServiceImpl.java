package com.godfunc.service.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.godfunc.entity.MerchantOrderProfit;
import com.godfunc.entity.Order;
import com.godfunc.entity.OrderDetail;
import com.godfunc.entity.PlatformOrderProfit;
import com.godfunc.mapper.OrderMapper;
import com.godfunc.model.MerchantAgentProfit;
import com.godfunc.service.MerchantOrderProfitService;
import com.godfunc.service.OrderDetailService;
import com.godfunc.service.OrderService;
import com.godfunc.service.PlatformOrderProfitService;
import lombok.RequiredArgsConstructor;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


/**
 * @author Godfunc
 * @email godfunc@outlook.com
 */
@Service
@RequiredArgsConstructor
public class OrderServiceImpl extends ServiceImpl<OrderMapper, Order> implements OrderService {

    private final OrderDetailService orderDetailService;
    private final MerchantOrderProfitService merchantOrderProfitService;
    private final PlatformOrderProfitService platformOrderProfitService;

    @Override
    public boolean checkExist(String outTradeNo) {
        return count(Wrappers.<Order>lambdaQuery().eq(Order::getOutTradeNo, outTradeNo)) > 0;
    }

    @Override
    @Transactional
    public boolean create(Order order, OrderDetail detail, MerchantAgentProfit merchantAgentProfit, PlatformOrderProfit platformOrderProfit) {
        boolean orderFlag = save(order);
        boolean orderDetailFlag = orderDetailService.save(detail);
        boolean merchantProfitFlag = merchantOrderProfitService.save(merchantAgentProfit.getMerchantProfit());
        List<MerchantOrderProfit> agentProfitList = merchantAgentProfit.getAgentProfitList();
        if (CollectionUtils.isNotEmpty(agentProfitList)) {
            agentProfitList.parallelStream().forEach(merchantOrderProfitService::save);
        }
        boolean platformProfitFlag = platformOrderProfitService.save(platformOrderProfit);
        return orderFlag && orderDetailFlag && merchantProfitFlag && platformProfitFlag;
    }
}
