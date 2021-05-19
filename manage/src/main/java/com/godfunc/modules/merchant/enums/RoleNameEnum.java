package com.godfunc.modules.merchant.enums;

/**
 * @author godfunc
 * @email godfunc@outlook.com
 * @date 2020/7/31
 */
public enum RoleNameEnum {
    /**
     * 商户 1
     */
    MERCHANT("merchant"),
    /**
     * 代理 2
     */
    AGENT("agent"),

    /**
     * 管理 3
     */
    MANAGE("manage");

    private final String value;

    RoleNameEnum(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
