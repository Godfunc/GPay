package com.godfunc.entity;

import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
public class OrderDetailTemp implements Serializable {

    private static final long serialVersionUID = 1L;
    /**
     *
     */
    private Long id;
    /**
     * 订单id
     */
    private Long orderId;
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
     * 平台私钥
     */
    private String platPrivateKey;
    /**
     * 渠道主类id
     */
    private Long payCategoryId;
    /**
     * 渠道子类id
     */
    private Long payChannelId;
    /**
     * 渠道账号id
     */
    private Long payChannelAccountId;
    /**
     * 渠道账号商户号
     */
    private String payChannelAccountCode;
    /**
     * 渠道账号密钥
     */
    private String payChannelAccountKeyInfo;
    /**
     * 渠道最大限额
     */
    private Long payChannelDayMax;
    /**
     * 账号最大限额
     */
    private Long payChannelAccountDayMax;
    /**
     * 渠道下单地址
     */
    private String channelCreateUrl;
    /**
     * 渠道查询订单地址
     */
    private String channelQueryUrl;
    /**
     * 渠道回调地址
     */
    private String channelNotifyUrl;
    /**
     * 支付类型信息
     */
    private String channelPayTypeInfo;
    /**
     * 费率
     */
    private Float channelCostRate;
    /**
     * 逻辑标识
     */
    private String logicalTag;
    /**
     * 订单过期时间
     */
    private LocalDateTime orderExpiredTime;
    /**
     * 客户端类型
     */
    private Integer uaType;
    /**
     * 客户端ua
     */
    private String uaStr;
    /**
     * 支付客户端ip
     */
    private String payClientIp;
    /**
     * 下单客户ip
     */
    private String clientIp;
    /**
     * 商品信息
     */
    private String goodName;
    /**
     * 创建时间
     */
    private LocalDateTime createTime;
}
