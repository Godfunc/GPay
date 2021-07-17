package com.godfunc.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.godfunc.entity.Order;
import com.godfunc.entity.OrderDetail;
import com.godfunc.entity.PlatformOrderProfit;
import com.godfunc.model.MerchantAgentProfit;
import com.godfunc.model.NotifyOrderInfo;

/**
 * @author Godfunc
 * @email godfunc@outlook.com
 */
public interface OrderService extends IService<Order> {
    boolean checkExist(String outTradeNo, String merchantCode);

    boolean create(Order order, OrderDetail detail, MerchantAgentProfit merchantAgentProfit, PlatformOrderProfit platformOrderProfit);

    Order getByOrderNo(String orderNo);

    boolean updatePayInfo(Order order);

    Order getByTradeNo(String tradeNo);

    boolean updatePaid(Long id, int currentStatus, NotifyOrderInfo notifyOrderInfo);
}
