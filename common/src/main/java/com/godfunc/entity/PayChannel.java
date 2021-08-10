package com.godfunc.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 渠道子类
 *
 * @TableName pay_channel
 */
@TableName("pay_channel")
@Data
public class PayChannel implements Serializable, WeightEntity {
    /**
     *
     */
    @TableId
    private Long id;

    /**
     * 名称
     */
    private String name;

    /**
     * 编码
     */
    private String code;

    /**
     * 创建订单地址
     */
    private String createUrl;

    /**
     * 查询订单地址
     */
    private String queryUrl;

    /**
     * 回调地址
     */
    private String notifyUrl;

    /**
     * 支付类型信息
     */
    private String payTypeInfo;

    /**
     * 逻辑标识
     */
    private String logicalTag;

    /**
     * 成本费率
     */
    private Float costRate;

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

    @TableField(exist = false)
    private Integer weight;

    @TableField(exist = false)
    private Long categoryChannelId;
}