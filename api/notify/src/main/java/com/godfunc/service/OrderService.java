package com.godfunc.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.godfunc.entity.Order;
import com.godfunc.model.NotifyOrderInfo;

/**
 * @author Godfunc
 * @email godfunc@outlook.com
 */
public interface OrderService extends IService<Order> {

    Order getByOrderNo(String orderNo);

    Order getByTradeNo(String tradeNo);

    boolean updatePaid(Long id, int currentStatus, NotifyOrderInfo notifyOrderInfo);
}
