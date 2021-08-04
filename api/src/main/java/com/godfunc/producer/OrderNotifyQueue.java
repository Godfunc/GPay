package com.godfunc.producer;

import com.godfunc.constant.RabbitMQConstant;
import com.godfunc.entity.Order;
import com.godfunc.queue.model.OrderNotify;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;


@Slf4j
@Component
@RequiredArgsConstructor
public class OrderNotifyQueue {

    private final RabbitTemplate rabbitTemplate;

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
        rabbitTemplate.convertAndSend(RabbitMQConstant.MerchantNotifyOrder.MERCHANT_NOTIFY_ORDER_EXCHANGE,
                RabbitMQConstant.MerchantNotifyOrder.MERCHANT_NOTIFY_ORDER_ROUTING_KEY, orderNotify, new CorrelationData(orderNotify.getId().toString()));
    }
}
