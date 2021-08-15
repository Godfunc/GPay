package com.godfunc.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.godfunc.entity.Merchant;
import com.godfunc.entity.MerchantOrderProfit;
import com.godfunc.entity.Order;
import com.godfunc.model.MerchantAgentProfit;

import java.util.List;

/**
 * @author Godfunc
 * @email godfunc@outlook.com
 */
public interface MerchantOrderProfitService extends IService<MerchantOrderProfit> {
    MerchantAgentProfit calc(Merchant merchant, List<Merchant> agentList, Order order);
}
