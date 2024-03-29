package com.godfunc.producer;

import com.godfunc.constant.RabbitMQConstant;
import com.godfunc.queue.model.FixChannelRisk;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;


@Slf4j
@Component
@RequiredArgsConstructor
public class FixChannelRiskQueue {

    private final RabbitTemplate rabbitTemplate;

    public void push(FixChannelRisk fixChannelRisk) {
        rabbitTemplate.convertAndSend(RabbitMQConstant.DelayFixChannelRisk.DELAYED_FIX_CHANNEL_RISK_EXCHANGE,
                RabbitMQConstant.DelayFixChannelRisk.DELAYED_FIX_CHANNEL_RISK_ROUTING_KEY,
                fixChannelRisk,
                message -> {
                    message.getMessageProperties().setDelay(Long.valueOf(fixChannelRisk.getDelayTime()).intValue());
                    return message;
                }, new CorrelationData(fixChannelRisk.getId().toString()));
    }
}
