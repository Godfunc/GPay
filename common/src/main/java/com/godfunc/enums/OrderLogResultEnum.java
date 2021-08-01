package com.godfunc.enums;

public enum OrderLogResultEnum {
    /**
     * 成功 1
     */
    SUCCESS(1),
    /**
     * 失败 2
     */
    FAIL(2);

    private final int value;

    OrderLogResultEnum(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }
}
