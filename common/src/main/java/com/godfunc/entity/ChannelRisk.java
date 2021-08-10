package com.godfunc.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.LocalTime;

/**
 * 渠道子类风控
 *
 * @TableName channel_risk
 */
@TableName("channel_risk")
@Data
public class ChannelRisk implements Serializable {
    /**
     *
     */
    @TableId
    private Long id;

    /**
     * 渠道子类id
     */
    private Long channelId;

    /**
     * 渠道子类商户id
     */
    private Long channelAccountId;

    /**
     * 每日最大限额
     */
    @TableField(updateStrategy = FieldStrategy.IGNORED)
    private Long dayAmountMax;

    /**
     * 单笔最大限额
     */
    @TableField(updateStrategy = FieldStrategy.IGNORED)
    private Long oneAmountMax;

    /**
     * 单笔最小限额
     */
    @TableField(updateStrategy = FieldStrategy.IGNORED)
    private Long oneAmountMin;

    /**
     * 指定单笔金额，多个用,分割
     */
    private String oneAmount;

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

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}