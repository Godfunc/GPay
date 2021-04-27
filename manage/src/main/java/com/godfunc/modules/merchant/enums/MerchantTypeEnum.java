package com.godfunc.modules.merchant.enums;

/**
 * @author godfunc
 * @email godfunc@outlook.com
 * @date 2020/7/31
 */
public enum MerchantTypeEnum {
    /**
     * 商户 1
     */
    MERCHANT(1),
    /**
     * 代理 2
     */
    AGENT(2);

    private final int value;

    MerchantTypeEnum(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }
}
