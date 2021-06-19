package com.godfunc.pay;

import com.godfunc.cache.ChannelRiskCache;
import com.godfunc.dto.PayInfoDto;
import com.godfunc.entity.Order;
import com.godfunc.entity.OrderDetail;
import com.godfunc.enums.OrderStatusEnum;
import com.godfunc.exception.GException;
import com.godfunc.service.PayChannelAccountService;
import com.godfunc.service.PayChannelService;
import com.godfunc.util.Assert;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.time.LocalDateTime;

/**
 * @author godfunc
 * @email godfunc@outlook.com
 * @date 2021/6/15
 */
@Slf4j
@AllArgsConstructor
public abstract class DefaultAbstractPay implements PayService {

    protected final PayChannelService payChannelService;
    protected final PayChannelAccountService payChannelAccountService;
    protected final ChannelRiskCache channelRiskCache;

    @Override
    public void pay(Order order, HttpServletRequest request, HttpServletResponse response) {
        PayInfoDto payInfo = new PayInfoDto();
        if (order.getStatus() == OrderStatusEnum.SCAN.getValue() && StringUtils.isNotBlank(order.getPayStr())) {
            payInfo.setPayUrl(order.getPayStr());
            payInfo.setTradeNo(order.getOrderNo());
            handleResponse(payInfo, request, response);
            return;
        }
        // 设置UA等信息 TODO
        OrderDetail detail = order.getDetail();
        Assert.isTrue(!checkOrder(order), "订单已过期");
        Assert.isTrue(!checkChannel(order), "渠道不可用");
        try {
            payInfo = doPay(order);

            handleResponse(payInfo, request, response);
        } catch (GException e) {
            if (order.getDetail().getPayChannelDayMax() != null) {
                channelRiskCache.divideAmount(detail.getPayChannelId(), order.getAmount());
            }
            if (order.getDetail().getPayChannelAccountDayMax() != null) {
                channelRiskCache.divideAmount(detail.getPayChannelAccountId(), order.getAmount());
            }
        }
    }

    @Override
    public void setClientInfo(Order order, HttpServletRequest request) {
        
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
        Long payChannelAccountDayMax = order.getDetail().getPayChannelAccountDayMax();
        if (payChannelAccountDayMax != null && channelRiskCache.addTodayAmount(order.getDetail().getPayChannelAccountId(), order.getAmount()) > payChannelAccountDayMax) {
            log.info("账号 {} 超过限额 {}", order.getDetail().getPayChannelAccountId(), payChannelDayMax);
            throw new GException("当前渠道今日交易已达到上线");
        }
        return true;
    }

    @Override
    public void handleResponse(PayInfoDto payInfo, HttpServletRequest request, HttpServletResponse response) {

    }
}
