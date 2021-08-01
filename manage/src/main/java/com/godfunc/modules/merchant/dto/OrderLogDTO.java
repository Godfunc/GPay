package com.godfunc.modules.merchant.dto;


import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
public class OrderLogDTO implements Serializable {

    private Integer oldStatus;

    private Integer newStatus;

    private String reason;
    /**
     * 1成功 2失败
     */
    private Integer result;

    private Date createTime;
}
