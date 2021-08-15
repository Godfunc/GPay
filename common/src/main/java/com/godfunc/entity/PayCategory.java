package com.godfunc.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 渠道主类
 *
 * @TableName pay_category
 */
@TableName("pay_category")
@Data
public class PayCategory implements Serializable {
    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
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
     * 编号
     */
    private String code;
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