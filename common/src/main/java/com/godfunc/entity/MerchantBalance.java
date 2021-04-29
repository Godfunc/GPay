package com.godfunc.entity;

import com.baomidou.mybatisplus.annotation.*;

import java.io.Serializable;
import java.time.LocalDateTime;
import lombok.Data;

/**
 * 商户余额
 * @TableName merchant_balance
 */
@TableName("merchant_balance")
@Data
public class MerchantBalance implements Serializable {
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
     * 余额
     */
    private Long balanceAmount;

    /**
     * 冻结金额
     */
    private Long frozenAmount;

    /**
     * 最后变更时间
     */
    private LocalDateTime lastModifyTime;

    /**
     * 最后变更金额
     */
    private Long lastModifyAmount;

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