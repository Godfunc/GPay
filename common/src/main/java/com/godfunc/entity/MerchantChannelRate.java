package com.godfunc.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 商户渠道费率
 *
 * @TableName merchant_channel_rate
 */
@TableName("merchant_channel_rate")
@Data
public class MerchantChannelRate implements Serializable {
    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
    /**
     *
     */
    @TableId
    private Long id;
    /**
     * 商户id
     */
    private Long merchantId;
    /**
     * 商户code
     */
    private String merchantCode;
    /**
     * 渠道主类id
     */
    private Long payCategoryId;
    /**
     * 渠道子类id
     */
    private Long payChannelId;
    /**
     * 费率
     */
    private Float rate;
    /**
     * 创建人id
     */
    @TableField(fill = FieldFill.INSERT)
    private Long createId;
    /**
     * 更新人id
     */
    @TableField(fill = FieldFill.UPDATE)
    private Long updateId;
    /**
     * 创建时间
     */
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;
    /**
     * 更新时间
     */
    @TableField(fill = FieldFill.UPDATE)
    private LocalDateTime updateTime;
}