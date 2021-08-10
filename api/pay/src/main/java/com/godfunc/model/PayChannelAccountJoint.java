package com.godfunc.model;

import com.godfunc.entity.PayChannel;
import com.godfunc.entity.PayChannelAccount;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class PayChannelAccountJoint {

    private PayChannel payChannel;

    private PayChannelAccount payChannelAccount;
}
