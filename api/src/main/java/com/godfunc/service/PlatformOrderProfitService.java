package com.godfunc.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.godfunc.entity.Order;
import com.godfunc.entity.OrderDetail;
import com.godfunc.entity.PlatformOrderProfit;
import com.godfunc.model.MerchantAgentProfit;

/**
 * @author Godfunc
 * @email godfunc@outlook.com
 */
public interface PlatformOrderProfitService extends IService<PlatformOrderProfit> {
    PlatformOrderProfit calc(MerchantAgentProfit merchantAgentProfit, Order order, OrderDetail detail);
}
