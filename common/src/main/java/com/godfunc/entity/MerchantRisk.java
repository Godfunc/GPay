package com.godfunc.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.LocalTime;

/**
 * 商户风控
 *
 * @TableName merchant_risk
 */
@TableName("merchant_risk")
@Data
public class MerchantRisk implements Serializable {
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
     * 单笔最小
     */
    @TableField(updateStrategy = FieldStrategy.IGNORED)
    private Long oneAmountMin;
    /**
     * 单笔最大
     */
    @TableField(updateStrategy = FieldStrategy.IGNORED)
    private Long oneAmountMax;
    /**
     * 交易开始时间
     */
    private LocalTime dayStartTime;
    /**
     * 交易结束时间
     */
    private LocalTime dayEndTime;
    /**
     * 状态 0禁用 1启用
     */
    private Integer status;
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
    /**
     * 删除标识 0正常 1删除
     */
    @TableLogic
    private Integer rmTag;
}