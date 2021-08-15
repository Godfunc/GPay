package com.godfunc.model;

import com.godfunc.entity.PlatformOrderProfit;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ProfitJoint {

    private MerchantAgentProfit merchantAgentProfit;

    private PlatformOrderProfit platformOrderProfit;
}
