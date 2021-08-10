package com.godfunc.queue.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class FixChannelRisk implements Serializable {

    private Long id;
    private Long amount;
    private Long payChannelId;
    private Long payChannelAccountId;
    private long delayTime;
}
