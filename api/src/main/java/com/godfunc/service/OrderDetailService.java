package com.godfunc.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.godfunc.entity.OrderDetail;

/**
 * @author Godfunc
 * @email godfunc@outlook.com
 */
public interface OrderDetailService extends IService<OrderDetail> {

    OrderDetail getByOrderId(Long orderId);
}
