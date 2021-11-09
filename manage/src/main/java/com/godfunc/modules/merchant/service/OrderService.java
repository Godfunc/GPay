package com.godfunc.modules.merchant.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.godfunc.dto.PageDTO;
import com.godfunc.entity.Order;
import com.godfunc.modules.merchant.dto.CreateOrderDTO;
import com.godfunc.modules.merchant.dto.OrderDTO;
import com.godfunc.modules.merchant.param.CreateOrderParam;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.time.LocalDateTime;

public interface OrderService extends IService<Order> {

    PageDTO<OrderDTO> getPage(Integer page, Integer limit, Integer status,
                              String payType, String outTradeNo, String orderNo,
                              LocalDateTime createTime);

    Boolean updatePaid(Long id);

    Boolean notifyMerchant(Long id);

    CreateOrderDTO createOrder(CreateOrderParam param, HttpServletRequest request, HttpServletResponse response);

    void export(LocalDateTime startTime, LocalDateTime endTime, String merchantCode, HttpServletResponse response);
}
