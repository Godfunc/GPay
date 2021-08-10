package com.godfunc.constant;

/**
 * @author godfunc
 * @email godfunc@outlook.com
 */
public interface ApiConstant {

    /**
     * 锁定订单获取支付链接的锁过期时间 单位分钟
     */
    long ORDER_PAY_REQUEST_LOCK_EXPIRE = 2;

    String PAY_SERVICE_PREFIX = "pay_service_";
}
