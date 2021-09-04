package com.godfunc.pay.advice;

import com.godfunc.dto.PayInfoDTO;
import com.godfunc.entity.Order;

import javax.servlet.http.HttpServletRequest;

public interface PayUrlRequestAdvice {

    /**
     * 请求支付前执行该方法
     *
     * @param order
     * @param request
     */
    void beforeRequest(Order order, HttpServletRequest request);

    /**
     * 请求支付成功，并且更新数据库成功后会执行该方法
     *
     * @param payInfo
     */
    void afterRequest(PayInfoDTO payInfo);
}
