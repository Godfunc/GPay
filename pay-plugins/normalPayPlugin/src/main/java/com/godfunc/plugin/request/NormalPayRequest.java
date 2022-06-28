package com.godfunc.plugin.request;

import lombok.Data;

@Data
public class NormalPayRequest {
    private String outTradeNo;

    private String amount;

    private String notifyUrl;

    private String merchantId;

    private String sign;
}
