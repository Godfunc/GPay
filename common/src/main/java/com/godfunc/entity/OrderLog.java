package com.godfunc.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.godfunc.enums.OrderLogResultEnum;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@TableName(value = "pay_order_log")
public class OrderLog implements Serializable {

    public OrderLog(Long orderId, Long merchantId, Integer oldStatus, Integer newStatus, String reason, boolean result) {
        this.merchantId = merchantId;
        this.orderId = orderId;
        this.oldStatus = oldStatus;
        this.newStatus = newStatus;
        this.reason = reason;
        this.result = result ? OrderLogResultEnum.SUCCESS.getValue() : OrderLogResultEnum.FAIL.getValue();
    }

    /**
     *
     */
    @TableId
    private Long id;

    private Long merchantId;

    private Long orderId;

    private Integer oldStatus;

    private Integer newStatus;

    private String reason;

    /**
     * 1成功 2失败
     */
    private Integer result;

    /**
     * 创建时间
     */
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;

}