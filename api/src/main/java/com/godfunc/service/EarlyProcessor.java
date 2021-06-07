package com.godfunc.service;

import com.godfunc.param.PayOrderParam;

import javax.servlet.http.HttpServletRequest;

/**
 * 对下单参数进行处理（验证参数，修改参数）
 */
public interface EarlyProcessor {

    boolean support(PayOrderParam param);

    boolean check(HttpServletRequest request, PayOrderParam param);
}
