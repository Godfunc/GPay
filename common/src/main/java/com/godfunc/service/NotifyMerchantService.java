package com.godfunc.service;


import com.godfunc.enums.NotifyResultEnum;
import com.godfunc.util.AmountUtil;
import com.godfunc.util.SignUtils;
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

    public boolean notifyMerchant(String notifyUrl, String outTradeNo, String orderNo, Long amount,
                                  Long realAmount, String payType, Integer status,
                                  String platPrivateKey) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        MultiValueMap<String, Object> params = new LinkedMultiValueMap<>();
        params.add("outTradeNo", outTradeNo);
        params.add("tradeNo", orderNo);
        params.add("amount", AmountUtil.convertCent2Dollar(amount));
        params.add("realAmount", AmountUtil.convertCent2Dollar(realAmount));
        params.add("payType", payType);
        params.add("status", status);

        try {
            params.add("sign", SignUtils.rsa2Sign(params.toSingleValueMap(), platPrivateKey));
        } catch (Exception e) {
            log.error("签名异常", e);
            return false;
        }

        ResponseEntity<String> response = null;
        try {
            response = restTemplate.postForEntity(notifyUrl, new HttpEntity<>(params, headers), String.class);
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
