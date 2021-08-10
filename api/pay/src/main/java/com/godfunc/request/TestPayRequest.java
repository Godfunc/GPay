package com.godfunc.request;

import lombok.Data;

@Data
public class TestPayRequest {

    private String outTradeNo;

    private String amount;

    private String notifyUrl;

    private String merchantId;

    private String sign;
}
