package com.godfunc.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 商户余额变更记录
 *
 * @TableName merchant_balance_record
 */
@TableName("merchant_balance_record")
@Data
public class MerchantBalanceRecord implements Serializable {
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
     * 当时金额
     */
    private Long oldAmount;
    /**
     * 变更后金额
     */
    private Long newAmount;
    /**
     * 变更金额
     */
    private Long changeAmount;
    /**
     * 类型 1订单支付 2提现申请 3手动修改
     */
    private Integer type;
    /**
     * 备注
     */
    private String remark;
    /**
     * 关联数据
     */
    private Long linkedId;
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