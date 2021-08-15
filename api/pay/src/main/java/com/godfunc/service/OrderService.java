package com.godfunc.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.godfunc.entity.Order;
import com.godfunc.model.ProfitJoint;

/**
 * @author Godfunc
 * @email godfunc@outlook.com
 */
public interface OrderService extends IService<Order> {
    boolean checkExist(String outTradeNo, String merchantCode);

    boolean create(Order order, ProfitJoint profitJoint);

    Order getByOrderNo(String orderNo);

    boolean updatePayInfo(Order order);
}
