package com.godfunc.entity;

import com.baomidou.mybatisplus.annotation.*;

import java.io.Serializable;
import java.time.LocalDateTime;
import lombok.Data;

/**
 * 商户代理信息
 * @TableName merchant_agent_info
 */
@TableName(value ="merchant_agent_info")
@Data
public class MerchantAgentInfo implements Serializable {
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
     * 一级代理
     */
    private Long agentOneId;

    /**
     * 二级代理
     */
    private Long agentTwoId;

    /**
     * 三级代理
     */
    private Long agentThreeId;

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