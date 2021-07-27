package com.godfunc.constant;

/**
 * @author godfunc
 * @email godfunc@outlook.com
 */
public interface QueueConstant {

   String EXPIRE_ORDER_TOPIC = "order-expire";
   String EXPIRE_ORDER_GROUP = "order-expire-group";
   String MERCHANT_NOTIFY_ORDER_TOPIC = "order-merchant-notify";
   String MERCHANT_NOTIFY_ORDER_GROUP = "merchant-notify-order-group";
   int SYNC_TIME_OUT = 1000;
}
