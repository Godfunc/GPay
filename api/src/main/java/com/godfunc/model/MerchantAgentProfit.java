package com.godfunc.model;

import com.godfunc.entity.MerchantOrderProfit;
import lombok.Data;

import java.util.List;

@Data
public class MerchantAgentProfit {

    private List<MerchantOrderProfit> agentProfitList;

    private MerchantOrderProfit merchantProfit;
}
