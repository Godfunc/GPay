package com.godfunc.service;

import com.godfunc.dto.PayOrderDTO;
import com.godfunc.param.PayOrderParam;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public interface PayOrderService {
    PayOrderDTO create(PayOrderParam param, HttpServletRequest request, HttpServletResponse response);
}
