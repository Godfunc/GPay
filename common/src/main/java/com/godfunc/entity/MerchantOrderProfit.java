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
 * 商户订单收益表
 * @TableName pay_merchant_order_profit
 */
@TableName(value ="pay_merchant_order_profit")
@Data
@NoArgsConstructor
public class MerchantOrderProfit implements Serializable {

    public MerchantOrderProfit(Long orderId, Long orderAmount, Long merchantId, String merchantCode, Float merchantChannelRate, Long profitAmount) {
        this.orderId = orderId;
        this.orderAmount = orderAmount;
        this.merchantId = merchantId;
        this.merchantCode = merchantCode;
        this.merchantChannelRate = merchantChannelRate;
        this.profitAmount = profitAmount;
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
     * 商户渠道费率
     */
    private Float merchantChannelRate;

    /**
     * 收益
     */
    private Long profitAmount;

    /**
     * 创建时间
     */
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}