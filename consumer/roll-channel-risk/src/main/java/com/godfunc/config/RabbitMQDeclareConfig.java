package com.godfunc.config;

import com.godfunc.constant.RabbitMQConstant;
import com.godfunc.listener.ConfirmListener;
import com.godfunc.listener.ReturnListener;
import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class RabbitMQDeclareConfig {

    @Bean
    public RabbitTemplate.ConfirmCallback confirmCallback(RabbitTemplate rabbitTemplate) {
        ConfirmListener confirmListener = new ConfirmListener();
        rabbitTemplate.setConfirmCallback(confirmListener);
        return confirmListener;
    }

    @Bean
    public RabbitTemplate.ReturnCallback returnCallback(RabbitTemplate rabbitTemplate) {
        ReturnListener returnListener = new ReturnListener();
        rabbitTemplate.setReturnCallback(returnListener);
        return returnListener;
    }

    @Bean
    public Queue delayedFixChannelRiskQueue() {
        return QueueBuilder.durable(RabbitMQConstant.DelayFixChannelRisk.DELAYED_FIX_CHANNEL_RISK_QUEUE).build();
    }

    @Bean
    public Exchange delayedFixChannelRiskExchange() {
        Map<String, Object> arguments = new HashMap<>();
        arguments.put("x-delayed-type", "direct");
        return new CustomExchange(RabbitMQConstant.DelayFixChannelRisk.DELAYED_FIX_CHANNEL_RISK_EXCHANGE,
                RabbitMQConstant.DELAYED_EXCHANGE_TYPE, true, false, arguments);
    }

    @Bean
    public Binding delayedFixChannelRiskBinding(@Qualifier("delayedFixChannelRiskQueue") Queue delayedFixChannelRiskQueue,
                                                @Qualifier("delayedFixChannelRiskExchange") Exchange delayedFixChannelRiskExchange) {
        return BindingBuilder
                .bind(delayedFixChannelRiskQueue)
                .to(delayedFixChannelRiskExchange)
                .with(RabbitMQConstant.DelayFixChannelRisk.DELAYED_FIX_CHANNEL_RISK_ROUTING_KEY)
                .noargs();
    }
}
