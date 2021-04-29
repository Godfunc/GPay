package com.godfunc.entity;

import com.baomidou.mybatisplus.annotation.*;

import java.io.Serializable;
import java.time.LocalDateTime;
import lombok.Data;

/**
 * 商户信息
 * @TableName merchant
 */
@TableName("merchant")
@Data
public class Merchant implements Serializable {
    /**
     * 
     */
    @TableId
    private Long id;

    /**
     * 当前商户对应的用户
     */
    private Long userId;

    /**
     * 商户名
     */
    private String name;

    /**
     * 商户code
     */
    private String code;

    /**
     * 商户类型 1商户 2代理
     */
    private Integer type;

    /**
     * 平台私钥
     */
    private String platPrivateKey;

    /**
     * 平台公钥
     */
    private String platPublicKey;

    /**
     * 商户公钥
     */
    private String publicKey;

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