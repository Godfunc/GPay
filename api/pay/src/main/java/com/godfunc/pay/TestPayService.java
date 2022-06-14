package com.godfunc.pay;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.godfunc.cache.ChannelRiskCache;
import com.godfunc.constant.ApiConstant;
import com.godfunc.constant.PayLogicalConstant;
import com.godfunc.dto.PayInfoDTO;
import com.godfunc.entity.Order;
import com.godfunc.exception.GException;
import com.godfunc.lock.OrderPayRequestLock;
import com.godfunc.pay.advice.PayUrlRequestAdvice;
import com.godfunc.pay.advice.PayUrlRequestAdviceFinder;
import com.godfunc.producer.FixChannelRiskQueue;
import com.godfunc.request.TestPayRequest;
import com.godfunc.response.TestPayResponse;
import com.godfunc.service.OrderService;
import com.godfunc.service.PayChannelAccountService;
import com.godfunc.service.PayChannelService;
import com.godfunc.util.AmountUtil;
import com.godfunc.util.SignUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Map;


@Slf4j
@Service(ApiConstant.PAY_SERVICE_PREFIX + PayLogicalConstant.TEST)
public class TestPayService extends DefaultAbstractPay {

    private ObjectMapper objectMapper;

    public TestPayService(RestTemplate restTemplate, PayChannelService payChannelService, PayChannelAccountService payChannelAccountService,
                          ChannelRiskCache channelRiskCache, OrderService orderService, OrderPayRequestLock orderPayRequestLock,
                          PayUrlRequestAdviceFinder payUrlRequestAdviceFinder, FixChannelRiskQueue fixChannelRiskQueue,
                          List<PayUrlRequestAdvice> payUrlRequestAdvicesCacheList, ObjectMapper objectMapper) {
        super(restTemplate, payChannelService, payChannelAccountService, channelRiskCache, orderService, orderPayRequestLock,
                payUrlRequestAdviceFinder, fixChannelRiskQueue, payUrlRequestAdvicesCacheList);
        this.objectMapper = objectMapper;
    }

    @Override
    public PayInfoDTO doPay(Order order) {
        TestPayRequest request = new TestPayRequest();
        request.setAmount(AmountUtil.convertCent2Dollar(order.getAmount()));
        request.setNotifyUrl(order.getNotifyUrl());
        request.setMerchantId(order.getDetail().getPayChannelAccountCode());
        request.setOutTradeNo(order.getOrderNo());
        Map<String, String> keyMap = null;
        try {
            keyMap = objectMapper.readValue(order.getDetail().getPayChannelAccountKeyInfo(), new TypeReference<Map<String, String>>() {
            });
        } catch (JsonProcessingException e) {
            log.error("解析key错误");
            throw new GException("密钥设置错误，请联系管理员");
        }
        request.setSign(SignUtils.rsa2Sign(request, keyMap.get("privateKey")));
        ResponseEntity<TestPayResponse> response = restTemplate.postForEntity(order.getDetail().getChannelCreateUrl(), request, TestPayResponse.class);
        if (response.getBody() != null && 0 == response.getBody().getCode() && StringUtils.isNotBlank(response.getBody().getData().getPayUrl())) {
            return new PayInfoDTO(response.getBody().getData().getTradeNo(), response.getBody().getData().getPayUrl());
        } else {
            throw new GException("请求支付失败 {}", response.getBody() != null ? response.getBody().getMessage() : "");
        }
    }
}
