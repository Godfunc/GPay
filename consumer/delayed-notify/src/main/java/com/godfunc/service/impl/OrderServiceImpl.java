package com.godfunc.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.godfunc.entity.Order;
import com.godfunc.enums.OrderStatusEnum;
import com.godfunc.mapper.OrderMapper;
import com.godfunc.queue.model.OrderNotify;
import com.godfunc.service.OrderService;
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


    @Override
    public boolean updateFinish(OrderNotify orderNotify) {
        return lambdaUpdate().set(Order::getStatus, OrderStatusEnum.FINISH.getValue())
                .set(Order::getNotifyTime, LocalDateTime.now())
                .eq(Order::getStatus, OrderStatusEnum.PAID.getValue())
                .eq(Order::getId, orderNotify.getId()).update();
    }

}
