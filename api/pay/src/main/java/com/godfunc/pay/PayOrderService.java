package com.godfunc.pay;

import com.godfunc.constant.ApiConstant;
import com.godfunc.entity.Order;
import com.godfunc.entity.OrderDetail;
import com.godfunc.pay.plugin.PluginPayServiceBuilder;
import com.godfunc.plugin.PayPluginExecutor;
import com.godfunc.service.OrderDetailService;
import com.godfunc.service.OrderService;
import com.godfunc.util.Assert;
import lombok.RequiredArgsConstructor;
import org.apache.commons.collections4.CollectionUtils;
import org.pf4j.PluginManager;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * @author godfunc
 * @email godfunc@outlook.com
 * @date 2021/6/15
 */
@Service
@RequiredArgsConstructor
public class PayOrderService {

    private final OrderService orderService;
    private final OrderDetailService orderDetailService;
    private final ApplicationContext applicationContext;

    private final PluginManager pluginManager;

    private final PluginPayServiceBuilder pluginPayServiceBuilder;

    public void goPay(String orderNo, HttpServletRequest request, HttpServletResponse response) {
        Order order = orderService.getByOrderNo(orderNo);
        Assert.isNull(order, "订单不存在");
        OrderDetail detail = orderDetailService.getByOrderId(order.getId());
        Assert.isNull(detail, "订单信息不全，请重新下单");
        order.setDetail(detail);
        // 1. 先从plugins里面去找
        List<PayPluginExecutor> extensions = pluginManager.getExtensions(PayPluginExecutor.class, detail.getLogicalTag() + "Pay-plugin");
        PayService payService = null;
        if (CollectionUtils.isNotEmpty(extensions)) {
            PayPluginExecutor executor = extensions.get(0);
            payService = pluginPayServiceBuilder.build(executor);
        } else {
            // 2. plugins没有就去IoC容器里面去找
            payService = (PayService) applicationContext.getBean(ApiConstant.PAY_SERVICE_PREFIX + detail.getLogicalTag());
        }
        Assert.isNull(payService, "不支持的支付类型");
        payService.pay(order, request, response);
    }
}
