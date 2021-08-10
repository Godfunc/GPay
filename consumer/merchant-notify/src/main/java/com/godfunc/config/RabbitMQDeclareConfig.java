package com.godfunc.config;

import com.godfunc.constant.RabbitMQConstant;
import com.godfunc.listener.ConfirmListener;
import com.godfunc.listener.ReturnListener;
import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


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
    public Queue merchantNotifyOrderQueue() {
        return QueueBuilder.durable(RabbitMQConstant.MerchantNotifyOrder.MERCHANT_NOTIFY_ORDER_QUEUE).build();
    }

    @Bean
    public Exchange merchantNotifyOrderExchange() {
        return ExchangeBuilder.directExchange(RabbitMQConstant.MerchantNotifyOrder.MERCHANT_NOTIFY_ORDER_EXCHANGE).build();
    }

    @Bean
    public Binding merchantNotifyOrderBinding(@Qualifier("merchantNotifyOrderQueue") Queue merchantNotifyOrderQueue,
                                              @Qualifier("merchantNotifyOrderExchange") Exchange merchantNotifyOrderExchange) {
        return BindingBuilder
                .bind(merchantNotifyOrderQueue)
                .to(merchantNotifyOrderExchange)
                .with(RabbitMQConstant.MerchantNotifyOrder.MERCHANT_NOTIFY_ORDER_ROUTING_KEY)
                .noargs();
    }
}
