package com.godfunc.service.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.godfunc.entity.MerchantOrderProfit;
import com.godfunc.entity.Order;
import com.godfunc.entity.OrderLog;
import com.godfunc.entity.PlatformOrderProfit;
import com.godfunc.enums.OrderStatusEnum;
import com.godfunc.enums.OrderStatusLogReasonEnum;
import com.godfunc.mapper.OrderMapper;
import com.godfunc.model.MerchantAgentProfit;
import com.godfunc.model.ProfitJoint;
import com.godfunc.producer.OrderExpireQueue;
import com.godfunc.queue.model.OrderExpire;
import com.godfunc.service.*;
import lombok.RequiredArgsConstructor;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.time.LocalDateTime;
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
    private final OrderExpireQueue orderExpireQueue;
    private final OrderLogService orderLogService;

    @Override
    public boolean checkExist(String outTradeNo, String merchantCode) {
        return count(Wrappers.<Order>lambdaQuery().eq(Order::getMerchantCode, merchantCode).eq(Order::getOutTradeNo, outTradeNo)) > 0;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean create(Order order, ProfitJoint profitJoint) {
        boolean orderFlag = save(order);
        boolean orderDetailFlag = orderDetailService.save(order.getDetail());
        MerchantAgentProfit merchantAgentProfit = profitJoint.getMerchantAgentProfit();
        PlatformOrderProfit platformOrderProfit = profitJoint.getPlatformOrderProfit();
        boolean merchantProfitFlag = merchantOrderProfitService.save(merchantAgentProfit.getMerchantProfit());
        List<MerchantOrderProfit> agentProfitList = merchantAgentProfit.getAgentProfitList();
        if (CollectionUtils.isNotEmpty(agentProfitList)) {
            agentProfitList.parallelStream().forEach(merchantOrderProfitService::save);
        }
        boolean platformProfitFlag = platformOrderProfitService.save(platformOrderProfit);

        intoExpireQueue(order);
        orderLogService.save(new OrderLog(order.getId(), order.getMerchantId(), 0, order.getStatus(), OrderStatusLogReasonEnum.MERCHANT_CREATE.getValue(), orderFlag));
        return orderFlag && orderDetailFlag && merchantProfitFlag && platformProfitFlag;
    }

    private void intoExpireQueue(Order order) {
        OrderExpire orderExpire = new OrderExpire();
        orderExpire.setId(order.getId());
        orderExpire.setMerchantId(order.getMerchantId());
        orderExpire.setAmount(order.getAmount());
        orderExpire.setStatus(order.getStatus());
        orderExpire.setDelayTime(Duration.between(LocalDateTime.now(), order.getDetail().getOrderExpiredTime()).toMillis());
        orderExpire.setPayChannelId(order.getDetail().getPayChannelId());
        orderExpire.setPayChannelAccountId(order.getDetail().getPayChannelAccountId());
        orderExpireQueue.push(orderExpire);
    }

    @Override
    public Order getByOrderNo(String orderNo) {
        return getOne(Wrappers.<Order>lambdaQuery().eq(Order::getOrderNo, orderNo));
    }

    @Override
    public boolean updatePayInfo(Order order) {
        boolean orderFlag = lambdaUpdate().set(Order::getTradeNo, order.getTradeNo())
                .set(Order::getPayStr, order.getPayStr())
                .set(Order::getPayTime, LocalDateTime.now())
                .set(Order::getStatus, OrderStatusEnum.SCAN.getValue())
                .eq(Order::getStatus, OrderStatusEnum.CREATED.getValue())
                .eq(Order::getId, order.getId()).update();
        boolean detailFlag = orderDetailService.updateClientInfo(order.getDetail());
        orderLogService.save(new OrderLog(order.getId(), order.getMerchantId(), OrderStatusEnum.CREATED.getValue(), OrderStatusEnum.SCAN.getValue(), OrderStatusLogReasonEnum.MERCHANT_SCAN.getValue(), orderFlag));
        return orderFlag;
    }


}
