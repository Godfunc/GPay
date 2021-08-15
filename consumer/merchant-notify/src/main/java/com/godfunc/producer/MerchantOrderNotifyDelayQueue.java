package com.godfunc.producer;

import com.godfunc.constant.RabbitMQConstant;
import com.godfunc.queue.model.OrderNotify;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.stereotype.Component;

@Component
@RefreshScope
@RequiredArgsConstructor
public class MerchantOrderNotifyDelayQueue {

    private final RabbitTemplate rabbitTemplate;
    @Value("${firstDelayNotifyMillis}")
    private Integer firstDelayNotifyMillis;

    public void delayPush(OrderNotify orderNotify) {
        rabbitTemplate.convertAndSend(RabbitMQConstant.DelayedMerchantNotify.DELAYED_MERCHANT_NOTIFY_EXCHANGE,
                RabbitMQConstant.DelayedMerchantNotify.DELAYED_MERCHANT_NOTIFY_ROUTING_KEY,
                orderNotify, message -> {
                    message.getMessageProperties().setDelay(firstDelayNotifyMillis);
                    return message;
                },
                new CorrelationData(orderNotify.getId().toString()));
    }
}
