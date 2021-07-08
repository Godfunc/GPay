package com.godfunc.model;

import lombok.Data;

import java.io.Serializable;

@Data
public class NotifyOrderInfo implements Serializable {

    /**
     * 上游单号
     */
    private String tradeNo;

    /**
     * 平台单号
     */
    private String orderNo;

    /**
     * 订单金额 单位分
     */
    private Long amount;

    /**
     * 实际支付金额 单位分
     */
    private Long realAmount;
}
