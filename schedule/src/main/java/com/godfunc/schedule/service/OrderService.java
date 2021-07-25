package com.godfunc.schedule.service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.godfunc.entity.Order;
import com.godfunc.enums.OrderStatusEnum;
import com.godfunc.queue.model.OrderNotify;
import com.godfunc.schedule.mapper.OrderMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Slf4j
@Service
@RequiredArgsConstructor
public class OrderService extends ServiceImpl<OrderMapper, Order> {

    public boolean expired(Long id, Integer status) {
        return lambdaUpdate().set(Order::getStatus, OrderStatusEnum.EXPIRED.getValue())
                .eq(Order::getId, id)
                .eq(Order::getStatus, status).update();
    }

    public boolean updateFinish(OrderNotify orderNotify) {
        return lambdaUpdate().set(Order::getStatus, OrderStatusEnum.FINISH.getValue())
                .set(Order::getNotifyTime, LocalDateTime.now())
                .eq(Order::getStatus, OrderStatusEnum.PAID.getValue())
                .eq(Order::getId, orderNotify.getId()).update();
    }
}
