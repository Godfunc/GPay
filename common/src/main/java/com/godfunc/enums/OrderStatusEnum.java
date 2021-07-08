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
     * 已扫码 2
     */
    SCAN(2),

    /**
     * 已支付 3
     */
    PAID(3),

    /**
     * 已完成 4
     */
    FINISH(4);

    private final int value;

    OrderStatusEnum(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }
}
