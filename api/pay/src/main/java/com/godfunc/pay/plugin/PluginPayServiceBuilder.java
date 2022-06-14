package com.godfunc.pay.plugin;

import com.godfunc.cache.ChannelRiskCache;
import com.godfunc.dto.PayInfoDTO;
import com.godfunc.entity.Order;
import com.godfunc.lock.OrderPayRequestLock;
import com.godfunc.pay.DefaultAbstractPay;
import com.godfunc.pay.PayService;
import com.godfunc.pay.advice.PayUrlRequestAdvice;
import com.godfunc.pay.advice.PayUrlRequestAdviceFinder;
import com.godfunc.plugin.BasePlugin;
import com.godfunc.producer.FixChannelRiskQueue;
import com.godfunc.service.OrderService;
import com.godfunc.service.PayChannelAccountService;
import com.godfunc.service.PayChannelService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class PluginPayServiceBuilder {

    private final RestTemplate restTemplate;
    private final PayChannelService payChannelService;
    private final PayChannelAccountService payChannelAccountService;
    private final ChannelRiskCache channelRiskCache;
    private final OrderService orderService;
    private final OrderPayRequestLock orderPayRequestLock;
    private final PayUrlRequestAdviceFinder payUrlRequestAdviceFinder;
    private final FixChannelRiskQueue fixChannelRiskQueue;
    private final List<PayUrlRequestAdvice> payUrlRequestAdvicesCacheList;

    public PayService build(BasePlugin basePlugin) {
        return new DefaultAbstractPay(restTemplate, payChannelService, payChannelAccountService, channelRiskCache, orderService, orderPayRequestLock, payUrlRequestAdviceFinder, fixChannelRiskQueue, payUrlRequestAdvicesCacheList) {
            @Override
            public PayInfoDTO doPay(Order order) {
                return basePlugin.doPay(order);
            }

            @Override
            public void handleResponse(PayInfoDTO payInfo, HttpServletRequest request, HttpServletResponse response) {
                basePlugin.handleResponse(payInfo, request, response);
            }
        };
    }
}
