package com.godfunc.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 渠道子类账号
 *
 * @TableName pay_channel_account
 */
@TableName("pay_channel_account")
@Data
public class PayChannelAccount implements Serializable, WeightEntity {
    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
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
     * 渠道子类商户名
     */
    private String name;
    /**
     * 渠道子类账号商户号
     */
    private String accountCode;
    /**
     * 渠道子类商户密钥
     */
    private String keyInfo;
    /**
     * 权重
     */
    private Integer weight;
    /**
     * 状态 0禁用 1启用
     */
    private Integer status;
    /**
     * 风控设置 0不设置，1使用通道的风控，2使用自定义风控
     */
    private Integer riskType;
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