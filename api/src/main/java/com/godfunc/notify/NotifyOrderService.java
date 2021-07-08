package com.godfunc.notify;

import com.godfunc.constant.ApiConstant;
import com.godfunc.entity.Order;
import com.godfunc.entity.OrderDetail;
import com.godfunc.enums.OrderStatusEnum;
import com.godfunc.model.NotifyOrderInfo;
import com.godfunc.service.OrderDetailService;
import com.godfunc.service.OrderService;
import com.godfunc.util.Assert;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class NotifyOrderService {

    private final ApplicationContext applicationContext;
    private final OrderService orderService;
    private final OrderDetailService orderDetailService;

    public String notifyOrder(String logical, HttpServletRequest request) {
        NotifyOrderHandler handler = (NotifyOrderHandler) applicationContext.getBean(ApiConstant.NOTIFY_SERVICE_PREFIX + logical);
        Assert.isNull(handler, "非法操作");
        Map<String, Object> params = handler.argumentResolve(request);
        if (MapUtils.isEmpty(params)) {
            log.error("通知参数为空 {}", params);
            return handler.failResult();
        }
        NotifyOrderInfo notifyOrderInfo = handler.resolveInfo(params);
        Order order = getOrder(notifyOrderInfo);
        if (order == null) {
            log.info("订单不存在 {}", notifyOrderInfo);
            return handler.failResult();
        }
        OrderDetail detail = orderDetailService.getByOrderId(order.getId());
        if (detail == null) {
            log.info("订单不存在 {}", notifyOrderInfo);
            return handler.failResult();
        }
        order.setDetail(detail);
        if (!handler.signCheck(params, detail.getPayChannelAccountKeyInfo())) {
            log.info("签名验证未通过 params={}, keyInfo={}", params, detail.getPayChannelAccountKeyInfo());
            return handler.failResult();
        }
        // 已支付
        if (order.getStatus() == OrderStatusEnum.FINISH.getValue()
                || order.getStatus() == OrderStatusEnum.PAID.getValue()) {
            return handler.successResult();
        }

        if (order.getStatus() != OrderStatusEnum.SCAN.getValue()) {
            log.info("订单状态不正确，非已扫码状态 status={}", order.getStatus());
            return handler.failResult();
        }

        // 比较 amount
        if (notifyOrderInfo.getAmount() != null && notifyOrderInfo.getAmount().equals(order.getAmount())) {
            log.info("订单金额不匹配，本台金额 {}, 通知金额 {}", order.getAmount(), notifyOrderInfo.getAmount());
            return handler.failResult();
        }
        // 查询接口进行查询
        if (!handler.queryOrder(order)) {
            return handler.failResult();
        }

        // 风控金额 TODO
        // 更新订单信息
        if (orderService.updatePaid(order.getId(), notifyOrderInfo)) {
            return handler.successResult();
        } else {
            log.error("订单更新为已支付失败 {}", order.getId());
            return handler.failResult();
        }
    }

    private Order getOrder(NotifyOrderInfo notifyOrderInfo) {
        if (StringUtils.isNotBlank(notifyOrderInfo.getOrderNo())) {
            Order order = orderService.getByOrderNo(notifyOrderInfo.getOrderNo());
            if (order == null && StringUtils.isNotBlank(notifyOrderInfo.getTradeNo())) {
                return orderService.getByTradeNo(notifyOrderInfo.getTradeNo());
            }
        }
        return null;
    }
}
