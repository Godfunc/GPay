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
    ORDER_ERROR_PAGE("ORDER_ERROR_PAGE");

    private final String value;

    ConfigNameEnum(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
