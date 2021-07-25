package com.godfunc.producer;

import com.godfunc.constant.QueueConstant;
import com.godfunc.entity.Order;
import com.godfunc.queue.model.OrderNotify;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.spring.core.RocketMQTemplate;
import org.springframework.messaging.support.GenericMessage;
import org.springframework.stereotype.Component;


@Slf4j
@Component
@RequiredArgsConstructor
public class OrderNotifyQueue {

    private final RocketMQTemplate rocketMQTemplate;

    public void push(Order order) {
        OrderNotify orderNotify = new OrderNotify();
        orderNotify.setNotifyUrl(order.getNotifyUrl());
        orderNotify.setAmount(order.getAmount());
        orderNotify.setRealAmount(order.getRealAmount());
        orderNotify.setPlatPrivateKey(order.getDetail().getPlatPrivateKey());
        orderNotify.setId(order.getId());
        orderNotify.setPayType(order.getPayType());
        orderNotify.setTime(0);
        orderNotify.setOutTradeNo(order.getOutTradeNo());
        orderNotify.setOrderNo(order.getOrderNo());
        orderNotify.setStatus(order.getStatus());
        rocketMQTemplate.syncSend(QueueConstant.MERCHANT_NOTIFY_ORDER_TOPIC, new GenericMessage<>(orderNotify), QueueConstant.SYNC_TIME_OUT);
    }
}
