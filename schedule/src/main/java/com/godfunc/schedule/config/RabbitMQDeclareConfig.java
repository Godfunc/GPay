package com.godfunc.schedule.config;

import com.godfunc.constant.RabbitMQConstant;
import org.springframework.amqp.core.*;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class RabbitMQDeclareConfig {

    @Bean
    public Queue delayedOrderExpireQueue() {
        return QueueBuilder.durable(RabbitMQConstant.DELAYED_ORDER_EXPIRE_QUEUE).build();
    }

    @Bean
    public Exchange delayedOrderExpireExchange() {
        Map<String, Object> arguments = new HashMap<>();
        arguments.put("x-delayed-type", "direct");
        return new CustomExchange(RabbitMQConstant.DELAYED_ORDER_EXPIRE_EXCHANGE,
                RabbitMQConstant.DELAYED_EXCHANGE_TYPE, true, false, arguments);
    }

    @Bean
    public Binding delayedOrderExpireBinding(@Qualifier("delayedOrderExpireQueue") Queue delayedOrderExpireQueue,
                                             @Qualifier("delayedOrderExpireExchange") Exchange delayedOrderExpireExchange) {
        return BindingBuilder
                .bind(delayedOrderExpireQueue)
                .to(delayedOrderExpireExchange)
                .with(RabbitMQConstant.DELAYED_ORDER_EXPIRE_ROUTING_KEY)
                .noargs();
    }


    @Bean
    public Queue delayedFixChannelRiskQueue() {
        return QueueBuilder.durable(RabbitMQConstant.DELAYED_FIX_CHANNEL_RISK_QUEUE).build();
    }

    @Bean
    public Exchange delayedFixChannelRiskExchange() {
        Map<String, Object> arguments = new HashMap<>();
        arguments.put("x-delayed-type", "direct");
        return new CustomExchange(RabbitMQConstant.DELAYED_FIX_CHANNEL_RISK_EXCHANGE,
                RabbitMQConstant.DELAYED_EXCHANGE_TYPE, true, false, arguments);
    }

    @Bean
    public Binding delayedFixChannelRiskBinding(@Qualifier("delayedFixChannelRiskQueue") Queue delayedFixChannelRiskQueue,
                                                @Qualifier("delayedFixChannelRiskExchange") Exchange delayedFixChannelRiskExchange) {
        return BindingBuilder
                .bind(delayedFixChannelRiskQueue)
                .to(delayedFixChannelRiskExchange)
                .with(RabbitMQConstant.DELAYED_FIX_CHANNEL_RISK_ROUTING_KEY)
                .noargs();
    }


    @Bean
    public Queue merchantNotifyOrderQueue() {
        return QueueBuilder.durable(RabbitMQConstant.MERCHANT_NOTIFY_ORDER_QUEUE).build();
    }

    @Bean
    public Exchange merchantNotifyOrderExchange() {
        return ExchangeBuilder.directExchange(RabbitMQConstant.MERCHANT_NOTIFY_ORDER_EXCHANGE).build();
    }

    @Bean
    public Binding merchantNotifyOrderBinding(@Qualifier("merchantNotifyOrderQueue") Queue merchantNotifyOrderQueue,
                                              @Qualifier("merchantNotifyOrderExchange") Exchange merchantNotifyOrderExchange) {
        return BindingBuilder
                .bind(merchantNotifyOrderQueue)
                .to(merchantNotifyOrderExchange)
                .with(RabbitMQConstant.MERCHANT_NOTIFY_ORDER_ROUTING_KEY)
                .noargs();
    }

    @Bean
    public Queue delayedMerchantNotifyQueue() {
        return QueueBuilder.durable(RabbitMQConstant.DELAYED_MERCHANT_NOTIFY_QUEUE).build();
    }

    @Bean
    public Exchange delayedMerchantNotifyExchange() {
        Map<String, Object> arguments = new HashMap<>();
        arguments.put("x-delayed-type", "direct");
        return new CustomExchange(RabbitMQConstant.DELAYED_MERCHANT_NOTIFY_EXCHANGE,
                RabbitMQConstant.DELAYED_EXCHANGE_TYPE, true, false, arguments);
    }

    @Bean
    public Binding delayedMerchantNotifyBinding(@Qualifier("delayedMerchantNotifyQueue") Queue delayedMerchantNotifyQueue,
                                                @Qualifier("delayedMerchantNotifyExchange") Exchange delayedMerchantNotifyExchange) {
        return BindingBuilder
                .bind(delayedMerchantNotifyQueue)
                .to(delayedMerchantNotifyExchange)
                .with(RabbitMQConstant.DELAYED_MERCHANT_NOTIFY_ROUTING_KEY)
                .noargs();
    }
}
