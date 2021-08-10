package com.godfunc.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.godfunc.entity.Order;
import com.godfunc.queue.model.OrderNotify;

/**
 * @author Godfunc
 * @email godfunc@outlook.com
 */
public interface OrderService extends IService<Order> {
    boolean updateFinish(OrderNotify orderNotify);
}
