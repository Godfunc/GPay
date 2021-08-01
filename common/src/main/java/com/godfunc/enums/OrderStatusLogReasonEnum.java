package com.godfunc.enums;

public enum OrderStatusLogReasonEnum {

    MERCHANT_CREATE("用户下单"),
    MERCHANT_SCAN("用户扫码支付"),
    ORDER_NOTIFY("上游通知"),
    ORDER_DELAY_EXPIRED("订单过期（延时任务）"),
    UPDATE_PAID("补单"),
    OPER_NOTIFY_MERCHANT("手动通知商户"),
    NOTIFY_MERCHANT("通知商户");

    private final String value;

    OrderStatusLogReasonEnum(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
