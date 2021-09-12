package com.godfunc.modules.sys.enums;

/**
 * 菜单类型
 *
 * @author godfunc
 * @email godfunc@outlook.com
 * @date 2020/7/31
 */
public enum MenuStatusEnum {

    /**
     * 0 停用
     */
    DISABLE(0),
    /**
     * 1 启用
     */
    ENABLE(1);

    private final int value;

    MenuStatusEnum(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }
}
