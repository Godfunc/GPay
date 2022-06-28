package com.godfunc.producer;

import com.godfunc.constant.RabbitMQConstant;
import com.godfunc.queue.model.FixChannelRisk;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.time.LocalDateTime;


@Slf4j
@Component
public class FixChannelRiskQueue {

    private final RabbitTemplate rabbitTemplate;

    private final Long fixChannelFloatMillis;

    public FixChannelRiskQueue(RabbitTemplate rabbitTemplate, @Value("${fixChannelFloatMillis}") Long fixChannelFloatMillis) {
        this.rabbitTemplate = rabbitTemplate;
        this.fixChannelFloatMillis = fixChannelFloatMillis;
    }

    public void push(FixChannelRisk fixChannelRisk, LocalDateTime orderExpiredTime) {
        // 在订单过期之后，才进行金额回滚，fixChannelFloatMillis是一个相对安全的时间范围，一般为正数
        fixChannelRisk.setDelayTime(Duration.between(LocalDateTime.now(), orderExpiredTime).toMillis() + fixChannelFloatMillis);
        rabbitTemplate.convertAndSend(RabbitMQConstant.DelayFixChannelRisk.DELAYED_FIX_CHANNEL_RISK_EXCHANGE,
                RabbitMQConstant.DelayFixChannelRisk.DELAYED_FIX_CHANNEL_RISK_ROUTING_KEY,
                fixChannelRisk,
                message -> {
                    message.getMessageProperties().setDelay(Long.valueOf(fixChannelRisk.getDelayTime()).intValue());
                    return message;
                }, new CorrelationData(fixChannelRisk.getId().toString()));
    }
}
