package com.godfunc.enums;

public enum NotifyResultEnum {

    SUCCESS("success"),

    SUCCESS_UPPER("SUCCESS"),

    OK("ok"),

    OK_UPPER("OK"),

    FAIL("fail");

    private final String value;

    NotifyResultEnum(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
