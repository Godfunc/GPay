package com.godfunc.modules.merchant.enums;

/**
 * @author godfunc
 * @email godfunc@outlook.com
 * @date 2020/7/31
 */
public enum PayCategoryStatusEnum {
    /**
     * 状态停用 0
     */
    DISABLE(0),
    /**
     * 状态可用 1
     */
    ENABLE(1);

    private final int value;

    PayCategoryStatusEnum(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }
}
