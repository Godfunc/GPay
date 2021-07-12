package com.godfunc.pay.advice;

import com.godfunc.dto.PayInfoDto;
import com.godfunc.entity.Order;

import javax.servlet.http.HttpServletRequest;

public interface PayUrlRequestAdvice {

    void beforeRequest(Order order, HttpServletRequest request);

    void afterRequest(PayInfoDto payInfo);
}
