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
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
    public boolean updatePaid(Long id, int currentStatus, NotifyOrderInfo notifyOrderInfo) {
        boolean flag = lambdaUpdate().set(Order::getTradeNo, notifyOrderInfo.getTradeNo())
                .set(Order::getRealAmount, notifyOrderInfo.getRealAmount())
                .set(Order::getStatus, currentStatus)
                .set(Order::getNotifyTime, LocalDateTime.now())
                .eq(Order::getId, id)
                .eq(Order::getStatus, OrderStatusEnum.SCAN.getValue())
                .update();
        orderLogService.save(new OrderLog(id, currentStatus, OrderStatusEnum.SCAN.getValue(), OrderStatusLogReasonEnum.ORDER_NOTIFY.getValue(), flag));
        return flag;

    }




}
