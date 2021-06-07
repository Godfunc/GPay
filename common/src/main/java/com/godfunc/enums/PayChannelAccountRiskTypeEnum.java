package com.godfunc.enums;

/**
 * @author godfunc
 * @email godfunc@outlook.com
 * @date 2020/7/31
 */
public enum PayChannelAccountRiskTypeEnum {
    /**
     * 不设置 0
     */
    NONE(0),

    /**
     * 使用通道的风控 1
     */
    CHANNEL(1),
    /**
     * 使用自定义风控 2
     */
    CUSTOMIZE(2);

    private final int value;

    PayChannelAccountRiskTypeEnum(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }
}
