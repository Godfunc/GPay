package com.godfunc.pay;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.godfunc.constant.ApiConstant;
import com.godfunc.constant.PayLogicalConstant;
import com.godfunc.dto.PayInfoDto;
import com.godfunc.entity.Order;
import com.godfunc.exception.GException;
import com.godfunc.request.TestPayRequest;
import com.godfunc.response.TestPayResponse;
import com.godfunc.util.AmountUtil;
import com.godfunc.util.SignUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Map;


@Slf4j
@Service(ApiConstant.PAY_SERVICE_PREFIX + PayLogicalConstant.TEST)
@RequiredArgsConstructor
public class TestPayService extends DefaultAbstractPay {

    private final ObjectMapper objectMapper;

    @Override
    public PayInfoDto doPay(Order order) {
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
            return new PayInfoDto(response.getBody().getData().getTradeNo(), response.getBody().getData().getPayUrl());
        } else {
            throw new GException("请求支付失败 {}", response.getBody() != null ? response.getBody().getMessage() : "");
        }
    }
}
