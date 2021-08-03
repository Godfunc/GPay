package com.godfunc.schedule.producer;

import com.godfunc.constant.RabbitMQConstant;
import com.godfunc.queue.model.OrderNotify;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class MerchantOrderNotifyDelayQueue {

    private final RabbitTemplate rabbitTemplate;

    private int[] NOTIFY_DELAY_ARRAY = {5, 30, 60, 90, 120};

    public int delay(int time) {
        if (time < 0 || time >= NOTIFY_DELAY_ARRAY.length) {
            return 0;
        } else {
            return NOTIFY_DELAY_ARRAY[time];
        }
    }

    public void delayPush(OrderNotify orderNotify) {
        rabbitTemplate.convertAndSend(RabbitMQConstant.DELAYED_MERCHANT_NOTIFY_EXCHANGE,
                RabbitMQConstant.DELAYED_MERCHANT_NOTIFY_ROUTING_KEY,
                orderNotify, message -> {
                    message.getMessageProperties().setDelay(1000 * delay(orderNotify.getTime()));
                    return message;
                },
                new CorrelationData(orderNotify.getId().toString()));
    }
}
