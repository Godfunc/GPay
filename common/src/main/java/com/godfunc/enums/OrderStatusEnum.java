package com.godfunc.enums;

/**
 * @author godfunc
 * @email godfunc@outlook.com
 * @date 2020/7/31
 */
public enum OrderStatusEnum {
    /**
     * 已下单 1
     */
    CREATED(1),
    /**
     * 已扫码 1
     */
    SCAN(2);

    private final int value;

    OrderStatusEnum(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }
}
