package com.godfunc.queue.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
public class FixChannelRisk implements Serializable {


    public FixChannelRisk(Long id, Long amount, Long payChannelId, Long payChannelAccountId) {
        this.id = id;
        this.amount = amount;
        this.payChannelId = payChannelId;
        this.payChannelAccountId = payChannelAccountId;
    }

    private Long id;
    private Long amount;
    private Long payChannelId;
    private Long payChannelAccountId;
    private long delayTime;
}
