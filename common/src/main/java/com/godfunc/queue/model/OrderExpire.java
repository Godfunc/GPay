package com.godfunc.queue.model;

import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
public class OrderExpire implements Serializable {

    private Long id;
    private Long amount;
    private Integer status;
    private LocalDateTime createTime;
    private LocalDateTime expiredTime;
    private Long payChannelId;
    private Long payChannelAccountId;
    private int expireLevel;
}
