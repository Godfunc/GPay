package com.godfunc.listener;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;

@Slf4j
public class ConfirmListener implements RabbitTemplate.ConfirmCallback {
    @Override
    public void confirm(CorrelationData correlationData, boolean ack, String cause) {
        // TODO 进行具体业务处理
        if (ack) {
            log.info("消息确认成功 message id = {}", correlationData.getId());
        } else {
            log.error("消息确认失败 message id ={}, cause={}", correlationData.getId(), cause);
        }

    }
}
