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
 * 平台订单收益表
 *
 * @TableName pay_platform_order_profit
 */
@TableName(value = "pay_platform_order_profit")
@Data
@NoArgsConstructor
public class PlatformOrderProfit implements Serializable {

    public PlatformOrderProfit(Long orderId, Long orderAmount, Long merchantId, String merchantCode, Float channelCostRate, Long profitAmount, Long channelCastAmount) {
        this.orderId = orderId;
        this.orderAmount = orderAmount;
        this.merchantId = merchantId;
        this.merchantCode = merchantCode;
        this.channelCostRate = channelCostRate;
        this.profitAmount = profitAmount;
        this.channelCastAmount = channelCastAmount;
    }

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
     * 订单金额
     */
    private Long orderAmount;

    /**
     * 商户id
     */
    private Long merchantId;

    /**
     * 商户号
     */
    private String merchantCode;

    /**
     * 渠道成本费率
     */
    private Float channelCostRate;

    /**
     * 收益
     */
    private Long profitAmount;

    /**
     * 通道成本
     */
    private Long channelCastAmount;

    /**
     * 创建时间
     */
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}