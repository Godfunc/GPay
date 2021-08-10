package com.godfunc.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.godfunc.entity.Order;

/**
 * @author Godfunc
 * @email godfunc@outlook.com
 */
public interface OrderService extends IService<Order> {
    boolean expired(Long id, Integer... status);
}
