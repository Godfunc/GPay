package com.godfunc.constant;

/**
 * @author godfunc
 * @email godfunc@outlook.com
 */
public interface ApiConstant {

    String USER_ID = "userId";

    /**
     * token head 的 key
     */
    String TOKEN_HEADER = "Authorization";

    /**
     * token有效时间 单位：秒
     */
    long TOKEN_EXPIRED = 60 * 60 * 24 * 5;

    /**
     * token快过期时间
     */
    long EXPIRED_PLUS = 30;

    /**
     * 锁定订单获取支付链接的锁过期时间 单位分钟
     */
    long ORDER_PAY_REQUEST_LOCK_EXPIRE = 2;

    String PAY_SERVICE_PREFIX = "pay_service_";
    String NOTIFY_SERVICE_PREFIX = "notify_service_";
}
