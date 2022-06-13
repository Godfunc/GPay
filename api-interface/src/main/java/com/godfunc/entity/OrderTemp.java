package com.godfunc.entity;

import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
public class OrderTemp implements Serializable {

    /**
     *
     */
    private Long id;
    /**
     * 商户id
     */
    private Long merchantId;
    /**
     * 商户号
     */
    private String merchantCode;
    /**
     * 商户名
     */
    private String merchantName;
    /**
     * 商户单号
     */
    private String outTradeNo;
    /**
     * 平台单号
     */
    private String orderNo;
    /**
     * 上游单号
     */
    private String tradeNo;
    /**
     * 渠道账号商户号
     */
    private String channelAccountCode;
    /**
     * 订单金额
     */
    private Long amount;
    /**
     * 实际支付金额
     */
    private Long realAmount;
    /**
     * 客户端创建时间
     */
    private LocalDateTime clientCreateTime;
    /**
     * 订单创建时间
     */
    private LocalDateTime createTime;
    /**
     * 订单支付时间
     */
    private LocalDateTime payTime;
    /**
     * 支付类型信息
     */
    private String payType;
    /**
     * 支付链接
     */
    private String payStr;
    /**
     * 回调时间
     */
    private LocalDateTime notifyTime;
    /**
     * 回调地址
     */
    private String notifyUrl;
    /**
     * 订单状态 1.已下单 2.已扫码 3.已支付 4.已回调
     */
    private Integer status;
    /**
     * 更新时间
     */
    private LocalDateTime updateTime;
    private OrderDetailTemp detail;
}
