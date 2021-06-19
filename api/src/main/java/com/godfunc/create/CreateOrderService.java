package com.godfunc.create;

import com.godfunc.dto.PayOrderDTO;
import com.godfunc.param.PayOrderParam;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public interface CreateOrderService {
    PayOrderDTO create(PayOrderParam param, HttpServletRequest request);
    void create(PayOrderParam param, HttpServletRequest request, HttpServletResponse response);
}
