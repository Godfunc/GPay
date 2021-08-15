package com.godfunc.pay;

import com.godfunc.cache.ChannelRiskCache;
import com.godfunc.dto.PayInfoDto;
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

    @Autowired
    protected RestTemplate restTemplate;
    @Autowired
    protected PayChannelService payChannelService;
    @Autowired
    protected PayChannelAccountService payChannelAccountService;
    @Autowired
    protected ChannelRiskCache channelRiskCache;
    @Autowired
    protected OrderService orderService;
    @Autowired
    private OrderPayRequestLock orderPayRequestLock;
    @Autowired
    private PayUrlRequestAdviceFinder payUrlRequestAdviceFinder;
    @Autowired
    private FixChannelRiskQueue fixChannelRiskQueue;

    private List<PayUrlRequestAdvice> payUrlRequestAdvicesCacheList;

    @Value("${fixChannelFloatMillis}")
    private Long fixChannelFloatMillis;

    @Override
    public void pay(Order order, HttpServletRequest request, HttpServletResponse response) {
        PayInfoDto payInfo = new PayInfoDto();
        if (order.getStatus() == OrderStatusEnum.SCAN.getValue() && StringUtils.isNotBlank(order.getPayStr())) {
            payInfo.setPayUrl(order.getPayStr());
            payInfo.setTradeNo(order.getOrderNo());
            handleResponse(payInfo, request, response);
            return;
        }

        OrderDetail detail = order.getDetail();
        Assert.isTrue(!checkOrder(order), "订单已过期");
        Assert.isTrue(!checkChannel(order), "渠道不可用");
        try {
            // 锁定当前订单
            Assert.isTrue(orderPayRequestLock.isLock(order.getId()), ApiMsg.SYSTEM_BUSY);

            List<PayUrlRequestAdvice> payUrlRequestAdvices = getPayUrlRequestAdvices();
            setClientInfo(order, request);

            payUrlRequestAdviceInvokeBefore(payUrlRequestAdvices, order, request);

            payInfo = doPay(order);
            order.setTradeNo(payInfo.getTradeNo());
            order.setPayStr(payInfo.getPayUrl());
            // 更新订单支付信息
            // order : tradeNo, payStr, payTime, status
            // detail: uaType, uaString, payClientIp
            Assert.isTrue(!orderService.updatePayInfo(order), "请求支付失败，请检查订单状态");

            payUrlRequestAdviceInvokeAfter(payUrlRequestAdvices, payInfo);

            handleResponse(payInfo, request, response);
        } catch (GException e) {
            // 请求失败就把缓存中扣的金额给恢复
            if (order.getDetail().getPayChannelDayMax() != null) {
                channelRiskCache.divideTodayAmount(detail.getPayChannelId(), order.getAmount());
            }
            if (order.getDetail().getPayChannelAccountDayMax() != null) {
                channelRiskCache.divideTodayAmount(detail.getPayChannelAccountId(), order.getAmount());
            }
            // 将异常抛出，让外层方法处理
            throw e;
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
    public abstract PayInfoDto doPay(Order order);

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

    @Override
    public boolean checkChannel(Order order) {
        Long payChannelDayMax = order.getDetail().getPayChannelDayMax();
        if (payChannelDayMax != null && channelRiskCache.addTodayAmount(order.getDetail().getPayChannelId(), order.getAmount()) > payChannelDayMax) {
            log.info("渠道 {} 超过限额 {}", order.getDetail().getPayChannelId(), payChannelDayMax);
            throw new GException("当前渠道今日交易已达到上线");
        }
        try {
            Long payChannelAccountDayMax = order.getDetail().getPayChannelAccountDayMax();
            if (payChannelAccountDayMax != null && channelRiskCache.addTodayAmount(order.getDetail().getPayChannelAccountId(), order.getAmount()) > payChannelAccountDayMax) {
                log.info("账号 {} 超过限额 {}", order.getDetail().getPayChannelAccountId(), payChannelDayMax);
                throw new GException("当前渠道今日交易已达到上线");
            }
            // 进入渠道风控金额回滚队列
            fixChannelRiskQueue.push(new FixChannelRisk(order.getId(), order.getAmount(),
                    payChannelDayMax != null ? order.getDetail().getPayChannelId() : null,
                    payChannelAccountDayMax != null ? order.getDetail().getPayChannelAccountId() : null,
                    Duration.between(LocalDateTime.now(), order.getDetail().getOrderExpiredTime()).toMillis() + fixChannelFloatMillis));
            return true;
        } catch (GException e) {
            // 将上面渠道扣除的金额恢复
            if (order.getDetail().getPayChannelDayMax() != null) {
                channelRiskCache.divideTodayAmount(order.getDetail().getPayChannelId(), order.getAmount());
            }
            throw e;
        }
    }

    @Override
    public void handleResponse(PayInfoDto payInfo, HttpServletRequest request, HttpServletResponse response) {
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

    private void payUrlRequestAdviceInvokeAfter(List<PayUrlRequestAdvice> list, PayInfoDto payInfo) {
        if (CollectionUtils.isEmpty(list)) {
            return;
        }
        for (PayUrlRequestAdvice payUrlRequestAdvice : list) {
            payUrlRequestAdvice.afterRequest(payInfo);
        }
    }
}
