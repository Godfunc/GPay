package com.godfunc.queue.model;

import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
public class OrderExpire implements Serializable {

    private Long id;
    private Long merchantId;
    private Long amount;
    private Integer status;
    private LocalDateTime createTime;
    private Long payChannelId;
    private Long payChannelAccountId;
    private long delayTime;
}
