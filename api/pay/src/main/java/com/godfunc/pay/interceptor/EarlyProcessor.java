package com.godfunc.pay.interceptor;

import com.godfunc.param.PayOrderParam;

import javax.servlet.http.HttpServletRequest;

/**
 * 下单的早期处理（验证参数，修改参数）
 */
public interface EarlyProcessor {

    boolean support(PayOrderParam param);

    boolean check(HttpServletRequest request, PayOrderParam param);
}
