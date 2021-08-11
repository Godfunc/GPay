package com.godfunc.queue.model;

import lombok.Data;

import java.io.Serializable;

@Data
public class OrderNotify implements Serializable {
    private Long id;
    private Long merchantId;
    private String platPrivateKey;
    private String notifyUrl;
    private String outTradeNo;
    private String orderNo;
    private Long realAmount;
    private Long amount;
    private int time;
    private String payType;
    private Integer status;
}