package com.godfunc.pay;

import com.godfunc.constant.ApiConstant;
import com.godfunc.entity.Order;
import com.godfunc.entity.OrderDetail;
import com.godfunc.service.OrderDetailService;
import com.godfunc.service.OrderService;
import com.godfunc.util.Assert;
import lombok.AllArgsConstructor;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author godfunc
 * @email godfunc@outlook.com
 * @date 2021/6/15
 */
@Service
@AllArgsConstructor
public class PayOrderService {

    private final OrderService orderService;
    private final OrderDetailService orderDetailService;
    private final ApplicationContext applicationContext;

    public void goPay(String orderNo, HttpServletRequest request, HttpServletResponse response) {
        Order order = orderService.getByOrderNo(orderNo);
        Assert.isNull(order, "订单不存在");
        OrderDetail detail = orderDetailService.getByOrderId(order.getId());
        Assert.isNull(detail, "订单信息不全，请重新下单");
        order.setDetail(detail);
        PayService payService = (PayService) applicationContext.getBean(ApiConstant.PAY_SERVICE_PREFIX + detail.getLogicalTag());
        Assert.isNull(payService, "不支持的支付类型");

        payService.pay(order,  request, response);
    }
}
