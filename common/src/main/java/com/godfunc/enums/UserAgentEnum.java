package com.godfunc.enums;

import java.util.regex.Pattern;

public enum UserAgentEnum {

    ANDROID(1, Pattern.compile("\\S*(Android|android|Linux|linux)\\S*")),
    IPHONE(2, Pattern.compile("\\S*(iPhone|iphone|Mac|mac|ipad|iPad)\\S*")),
    OTHER(0, null);

    private final int value;
    private final Pattern pattern;

    UserAgentEnum(int value, Pattern pattern) {
        this.value = value;
        this.pattern = pattern;
    }

    public int getValue() {
        return value;
    }

    public Pattern getPattern() {
        return pattern;
    }
}
