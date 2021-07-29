package com.godfunc.modules.merchant.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.godfunc.entity.OrderDetail;

public interface OrderDetailService extends IService<OrderDetail> {

    OrderDetail getByOrderId(Long orderId);
}
