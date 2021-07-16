package com.godfunc.notify;


import com.godfunc.entity.Order;
import com.godfunc.enums.NotifyResultEnum;
import com.godfunc.util.AmountUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;


@Slf4j
@Service
@RequiredArgsConstructor
public class NotifyMerchantService {

    private final RestTemplate restTemplate;

    public boolean notifyMerchant(Order order) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        MultiValueMap<String, Object> params = new LinkedMultiValueMap<>();
        params.add("outTradeNo", order.getOutTradeNo());
        params.add("tradeNo", order.getOrderNo());
        params.add("amount", AmountUtil.convertCent2Dollar(order.getAmount()));
        params.add("realAmount", AmountUtil.convertCent2Dollar(order.getRealAmount()));
        params.add("payType", order.getPayType());
        params.add("status", order.getStatus());
        ResponseEntity<String> response = null;
        try {
            response = restTemplate.postForEntity(order.getNotifyUrl(), new HttpEntity<>(params, headers), String.class);
        } catch (Exception e) {
            log.error("回调商户异常", e);
            return false;
        }
        if (NotifyResultEnum.SUCCESS.getValue().equals(response.getBody())) {
            return true;
        } else {
            log.error("回调商户失败 {}", response.getBody());
            return false;
        }
    }

}
