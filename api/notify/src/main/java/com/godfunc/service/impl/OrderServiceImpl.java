package com.godfunc.service.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.godfunc.entity.*;
import com.godfunc.enums.OrderStatusEnum;
import com.godfunc.enums.OrderStatusLogReasonEnum;
import com.godfunc.mapper.OrderMapper;
import com.godfunc.model.NotifyOrderInfo;
import com.godfunc.service.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;


/**
 * @author Godfunc
 * @email godfunc@outlook.com
 */
@Service
@RequiredArgsConstructor
public class OrderServiceImpl extends ServiceImpl<OrderMapper, Order> implements OrderService {

    private final OrderLogService orderLogService;

    @Override
    public Order getByOrderNo(String orderNo) {
        return getOne(Wrappers.<Order>lambdaQuery().eq(Order::getOrderNo, orderNo));
    }

    @Override
    public Order getByTradeNo(String tradeNo) {
        return getOne(Wrappers.<Order>lambdaQuery().eq(Order::getTradeNo, tradeNo));
    }

    @Override
    public boolean updatePaid(Long id, Long merchantId, int currentStatus, NotifyOrderInfo notifyOrderInfo) {
        boolean flag = lambdaUpdate().set(Order::getTradeNo, notifyOrderInfo.getTradeNo())
                .set(Order::getRealAmount, notifyOrderInfo.getRealAmount())
                .set(Order::getStatus, OrderStatusEnum.PAID.getValue())
                .set(Order::getNotifyTime, LocalDateTime.now())
                .eq(Order::getId, id)
                .eq(Order::getStatus, currentStatus)
                .update();
        orderLogService.save(new OrderLog(id, merchantId, currentStatus, OrderStatusEnum.PAID.getValue(), OrderStatusLogReasonEnum.ORDER_NOTIFY.getValue(), flag));
        return flag;

    }


}
