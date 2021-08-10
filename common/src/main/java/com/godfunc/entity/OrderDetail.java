package com.godfunc.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 订单详情表
 *
 * @TableName pay_order_detail
 */
@TableName(value = "pay_order_detail")
@Data
@NoArgsConstructor
public class OrderDetail implements Serializable {

    /**
     *
     */
    @TableId
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
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}