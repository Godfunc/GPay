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
public interface OrderService extends IService<Order> {
    boolean checkExist(String outTradeNo);

    boolean create(Order order, OrderDetail detail, MerchantAgentProfit merchantAgentProfit, PlatformOrderProfit platformOrderProfit);

}
