package com.godfunc.entity;

import com.baomidou.mybatisplus.annotation.*;

import java.io.Serializable;
import java.time.LocalDateTime;
import lombok.Data;

/**
 * 渠道关联
 * @TableName pay_category_channel
 */
@TableName("pay_category_channel")
@Data
public class PayCategoryChannel implements Serializable {
    /**
     * 
     */
    @TableId
    private Long id;

    /**
     * 渠道主类id
     */
    private Long categoryId;

    /**
     * 渠道子类id
     */
    private Long channelId;

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