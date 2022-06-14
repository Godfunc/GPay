package com.godfunc.pay;

import com.godfunc.cache.ChannelRiskCache;
import com.godfunc.dto.PayInfoDTO;
import com.godfunc.entity.Order;
import com.godfunc.entity.OrderDetail;
import com.godfunc.enums.OrderStatusEnum;
import com.godfunc.exception.GException;
import com.godfunc.lock.OrderPayRequestLock;
import com.godfunc.pay.advice.PayUrlRequestAdvice;
import com.godfunc.pay.advice.PayUrlRequestAdviceFinder;
import com.godfunc.producer.FixChannelRiskQueue;
import com.godfunc.queue.model.FixChannelRisk;
import com.godfunc.result.ApiMsg;
import com.godfunc.service.OrderService;
import com.godfunc.service.PayChannelAccountService;
import com.godfunc.service.PayChannelService;
import com.godfunc.util.Assert;
import com.godfunc.util.IpUtils;
import com.godfunc.util.UserAgentUtils;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

/**
 * @author godfunc
 * @email godfunc@outlook.com
 * @date 2021/6/15
 */
@Slf4j
@RefreshScope
public abstract class DefaultAbstractPay implements PayService {

    protected RestTemplate restTemplate;
    protected PayChannelService payChannelService;
    protected PayChannelAccountService payChannelAccountService;
    protected ChannelRiskCache channelRiskCache;
    protected OrderService orderService;
    private OrderPayRequestLock orderPayRequestLock;
    private PayUrlRequestAdviceFinder payUrlRequestAdviceFinder;
    private FixChannelRiskQueue fixChannelRiskQueue;

    private List<PayUrlRequestAdvice> payUrlRequestAdvicesCacheList;

    public DefaultAbstractPay(RestTemplate restTemplate, PayChannelService payChannelService, PayChannelAccountService payChannelAccountService, ChannelRiskCache channelRiskCache, OrderService orderService, OrderPayRequestLock orderPayRequestLock, PayUrlRequestAdviceFinder payUrlRequestAdviceFinder, FixChannelRiskQueue fixChannelRiskQueue, List<PayUrlRequestAdvice> payUrlRequestAdvicesCacheList) {
        this.restTemplate = restTemplate;
        this.payChannelService = payChannelService;
        this.payChannelAccountService = payChannelAccountService;
        this.channelRiskCache = channelRiskCache;
        this.orderService = orderService;
        this.orderPayRequestLock = orderPayRequestLock;
        this.payUrlRequestAdviceFinder = payUrlRequestAdviceFinder;
        this.fixChannelRiskQueue = fixChannelRiskQueue;
        this.payUrlRequestAdvicesCacheList = payUrlRequestAdvicesCacheList;
    }

    /**
     * 1. 检查修改订单状态为已扫码
     * 2. 检查订单是否过期
     * 3. 锁住当前当前订单的请求
     * 4. 检查渠道子类和账号的每个限额（修改已使用金额）
     * 5. 获取advices
     * 6. 获取客户端信息，存储到订单中
     * 7. 执行advice前置处理
     * 8. 执行支付请求
     * 9. 更新订单支付信息
     * 10. 将渠道子类和账号push到限额延时回滚队列中
     * 11. 支付请求异常，进行渠道子类和账号限额回滚
     * 12. 执行advice的后置处理
     * 13. 响应信息给用户
     * 14. 进行当前订单的锁释放
     *
     * @param order
     * @param request
     * @param response
     */
    @Override
    public void pay(Order order, HttpServletRequest request, HttpServletResponse response) {
        PayInfoDTO payInfo = new PayInfoDTO();
        if (order.getStatus() == OrderStatusEnum.SCAN.getValue() && StringUtils.isNotBlank(order.getPayStr())) {
            payInfo.setPayUrl(order.getPayStr());
            payInfo.setTradeNo(order.getOrderNo());
            handleResponse(payInfo, request, response);
            return;
        }

        OrderDetail detail = order.getDetail();
        Assert.isTrue(!checkOrder(order), "订单已过期");
        try {
            // 锁定当前订单
            Assert.isTrue(orderPayRequestLock.isLock(order.getId()), ApiMsg.SYSTEM_BUSY);
            // 渠道子类和渠道账号加上当前订单金额后，是否超过每日最大限额，如果超过就将增加金额回滚
            Assert.isTrue(!checkChannelAndRollbackRisk(order), "渠道不可用");

            List<PayUrlRequestAdvice> payUrlRequestAdvices = getPayUrlRequestAdvices();
            setClientInfo(order, request);

            payUrlRequestAdviceInvokeBefore(payUrlRequestAdvices, order, request);

            try {
                payInfo = doPay(order);
                Assert.isNull(payInfo, "请求支付失败，请稍后再试");
                Assert.isBlank(payInfo.getPayUrl(), "请求支付失败，请稍后再试");

                order.setTradeNo(payInfo.getTradeNo());
                order.setPayStr(payInfo.getPayUrl());
                // 更新订单支付信息
                // order : tradeNo, payStr, payTime, status
                // detail: uaType, uaString, payClientIp
                Assert.isTrue(!orderService.updatePayInfo(order), "请求支付失败，请检查订单状态");

                // 进入渠道风控金额回滚队列，单独用一个延时队列是为了让订单过期之后，再进行风控金额回滚，尽量减少超额的情况。
                // 但是还是可能出现超额，因为如果上游超时回调了会自动修复过期的订单。
                fixChannelRiskQueue.push(new FixChannelRisk(order.getId(), order.getAmount(),
                                order.getDetail().getPayChannelDayMax() != null ? order.getDetail().getPayChannelId() : null,
                                order.getDetail().getPayChannelAccountDayMax() != null ? order.getDetail().getPayChannelAccountId() : null),
                        order.getDetail().getOrderExpiredTime());
            } catch (Exception e) {
                // 请求失败就把缓存中扣的金额给恢复
                if (order.getDetail().getPayChannelDayMax() != null) {
                    channelRiskCache.divideTodayAmount(detail.getPayChannelId(), order.getAmount());
                }
                if (order.getDetail().getPayChannelAccountDayMax() != null) {
                    channelRiskCache.divideTodayAmount(detail.getPayChannelAccountId(), order.getAmount());
                }
                // 将异常抛出，让外层方法处理
                throw e;
            }

            payUrlRequestAdviceInvokeAfter(payUrlRequestAdvices, payInfo);

            handleResponse(payInfo, request, response);
        } finally {
            // 请求完成后删除锁
            orderPayRequestLock.rmLock(order.getId());
        }
    }

    @Override
    public void setClientInfo(Order order, HttpServletRequest request) {
        OrderDetail detail = order.getDetail();
        detail.setUaType(UserAgentUtils.getUAType(request));
        detail.setUaStr(UserAgentUtils.getUAStr(request));
        detail.setPayClientIp(IpUtils.getIpAddr(request));
    }

    @Override
    public abstract PayInfoDTO doPay(Order order);

    @Override
    public boolean checkOrder(Order order) {
        Assert.isTrue(order.getStatus() != OrderStatusEnum.CREATED.getValue()
                && order.getStatus() != OrderStatusEnum.SCAN.getValue(), "订单已过期");
        Assert.isTrue(isExpired(order.getDetail()), "订单已过期");
        return true;
    }

    private boolean isExpired(OrderDetail detail) {
        return detail.getOrderExpiredTime().isBefore(LocalDateTime.now());
    }

    /**
     * 渠道子类和渠道账号加上当前订单金额后，是否超过每日最大限额，如果超过就将增加金额回滚，
     *
     * @param order
     * @return
     */
    @Override
    public boolean checkChannelAndRollbackRisk(Order order) {
        Long payChannelDayMax = order.getDetail().getPayChannelDayMax();
        if (payChannelDayMax != null && channelRiskCache.addTodayAmount(order.getDetail().getPayChannelId(), order.getAmount()) > payChannelDayMax) {
            log.info("渠道 {} 超过限额 {}", order.getDetail().getPayChannelId(), payChannelDayMax);
            channelRiskCache.divideTodayAmount(order.getDetail().getPayChannelId(), order.getAmount());
            throw new GException("当前渠道今日交易已达到上线");
        }
        try {
            // 渠道账号的每日最大限额处理
            Long payChannelAccountDayMax = order.getDetail().getPayChannelAccountDayMax();
            if (payChannelAccountDayMax != null && channelRiskCache.addTodayAmount(order.getDetail().getPayChannelAccountId(), order.getAmount()) > payChannelAccountDayMax) {
                log.info("账号 {} 超过限额 {}", order.getDetail().getPayChannelAccountId(), payChannelDayMax);
                channelRiskCache.divideTodayAmount(order.getDetail().getPayChannelAccountId(), order.getAmount());
                throw new GException("当前渠道今日交易已达到上线");
            }

            return true;
        } catch (GException e) {
            // 如果子账号超额，将上面渠道子类扣除的金额恢复
            if (order.getDetail().getPayChannelDayMax() != null) {
                channelRiskCache.divideTodayAmount(order.getDetail().getPayChannelId(), order.getAmount());
            }
            throw e;
        }
    }

    @Override
    public void handleResponse(PayInfoDTO payInfo, HttpServletRequest request, HttpServletResponse response) {
        try {
            response.sendRedirect(payInfo.getPayUrl());
        } catch (IOException e) {
            log.error("跳转到支付链接异常", e);
        }
    }

    /**
     * 返回排序后的所有的 PayUrlRequestAdvice
     *
     * @return
     */
    private List<PayUrlRequestAdvice> getPayUrlRequestAdvices() {
        if (payUrlRequestAdvicesCacheList == null) {
            payUrlRequestAdvicesCacheList = payUrlRequestAdviceFinder.findAll();
        }
        return payUrlRequestAdvicesCacheList;
    }

    private void payUrlRequestAdviceInvokeBefore(List<PayUrlRequestAdvice> list, Order order, HttpServletRequest request) {
        if (CollectionUtils.isEmpty(list)) {
            return;
        }
        for (PayUrlRequestAdvice payUrlRequestAdvice : list) {
            payUrlRequestAdvice.beforeRequest(order, request);
        }
    }

    private void payUrlRequestAdviceInvokeAfter(List<PayUrlRequestAdvice> list, PayInfoDTO payInfo) {
        if (CollectionUtils.isEmpty(list)) {
            return;
        }
        for (PayUrlRequestAdvice payUrlRequestAdvice : list) {
            payUrlRequestAdvice.afterRequest(payInfo);
        }
    }
}
