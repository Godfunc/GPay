package com.godfunc.entity;

import com.baomidou.mybatisplus.annotation.*;

import java.io.Serializable;
import java.time.LocalDateTime;
import lombok.Data;

/**
 * 商户渠道费率
 *
 * @TableName merchant_channel_rate
 */
@TableName("merchant_channel_rate")
@Data
public class MerchantChannelRate implements Serializable {
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
     * 渠道主类子类关联id
     */
    private Long categoryChannelId;

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

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}