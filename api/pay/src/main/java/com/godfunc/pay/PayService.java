package com.godfunc.pay;

import com.godfunc.dto.PayInfoDTO;
import com.godfunc.entity.Order;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author godfunc
 * @email godfunc@outlook.com
 * @date 2021/6/15
 */
public interface PayService {

    void pay(Order order, HttpServletRequest request, HttpServletResponse response);

    PayInfoDTO doPay(Order order);

    /**
     * 设置客户端信息
     *
     * @param order
     * @param request
     */
    void setClientInfo(Order order, HttpServletRequest request);

    // 检查订单并更新未已扫码
    boolean checkOrder(Order order);

    // 检查渠道状态
    boolean checkChannelAndRollbackRisk(Order order);

    /**
     * 处理接口响应 尽量捕获该方法中的异常
     *
     * @param payInfo
     * @param request
     * @param response
     */
    void handleResponse(PayInfoDTO payInfo, HttpServletRequest request, HttpServletResponse response);

}
