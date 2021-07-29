package com.godfunc.modules.merchant.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.godfunc.dto.PageDTO;
import com.godfunc.entity.Order;
import com.godfunc.modules.merchant.dto.OrderDTO;

import java.time.LocalDateTime;

public interface OrderService extends IService<Order> {

    PageDTO<OrderDTO> getPage(Integer page, Integer limit, Integer status,
                              String payType, String tradeNo, String orderNo,
                              LocalDateTime createTime);

    Boolean updatePaid(Long id);

    Boolean notifyMerchant(Long id);
}
