package com.godfunc.enums;

/**
 * @author godfunc
 * @email godfunc@outlook.com
 * @date 2020/7/31
 */
public enum ConfigNameEnum {
    /**
     * 订单错误页
     */
    ORDER_ERROR_PAGE("ORDER_ERROR_PAGE"),
    /**
     * 订单有效时间 单位秒
     */
    ORDER_EXPIRED_TIME("ORDER_EXPIRED_TIME");

    private final String value;

    ConfigNameEnum(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
