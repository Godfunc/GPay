package com.godfunc.constant;

/**
 * @author godfunc
 * @email godfunc@outlook.com
 */
public interface RabbitMQConstant {

    String DELAYED_EXCHANGE_TYPE = "x-delayed-message";


    String DELAYED_ORDER_EXPIRE_EXCHANGE = "delayed-order-expire-exchange";
    String DELAYED_ORDER_EXPIRE_QUEUE = "delayed-order-expire-queue";
    String DELAYED_ORDER_EXPIRE_ROUTING_KEY = "expire";

    String DELAYED_FIX_CHANNEL_RISK_EXCHANGE = "delayed-fix-channel-risk-exchange";
    String DELAYED_FIX_CHANNEL_RISK_QUEUE = "delayed-fix-channel-risk-queue";
    String DELAYED_FIX_CHANNEL_RISK_ROUTING_KEY = "channel-risk";

    String MERCHANT_NOTIFY_ORDER_EXCHANGE = "merchant-notify-order-exchange";
    String MERCHANT_NOTIFY_ORDER_QUEUE = "merchant-notify-order-queue";
    String MERCHANT_NOTIFY_ORDER_ROUTING_KEY = "merchant-notify";

    String DELAYED_MERCHANT_NOTIFY_EXCHANGE = "delayed-merchant-notify-exchange";
    String DELAYED_MERCHANT_NOTIFY_QUEUE = "delayed-merchant-notify-queue";
    String DELAYED_MERCHANT_NOTIFY_ROUTING_KEY = "delayed-notify";

}
