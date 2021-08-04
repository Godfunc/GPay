package com.godfunc.producer;

import com.godfunc.constant.RabbitMQConstant;
import com.godfunc.queue.model.OrderExpire;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;


@Slf4j
@Component
@RequiredArgsConstructor
public class OrderExpireQueue {

    private final RabbitTemplate rabbitTemplate;

    public void push(OrderExpire orderExpire) {
        rabbitTemplate.convertAndSend(RabbitMQConstant.DelayOrderExpire.DELAYED_ORDER_EXPIRE_EXCHANGE,
                RabbitMQConstant.DelayOrderExpire.DELAYED_ORDER_EXPIRE_ROUTING_KEY,
                orderExpire,
                message -> {
                    message.getMessageProperties().setDelay(Long.valueOf(orderExpire.getDelayTime()).intValue());
                    return message;
                }, new CorrelationData(orderExpire.getId().toString()));
    }
}
