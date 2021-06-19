package com.godfunc.enums;

/**
 * @author godfunc
 * @email godfunc@outlook.com
 * @date 2020/7/31
 */
public enum CharsetEnum {

    UTF8("utf-8");

    private final String value;

    CharsetEnum(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
