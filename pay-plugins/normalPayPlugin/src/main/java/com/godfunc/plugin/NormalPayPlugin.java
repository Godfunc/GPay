package com.godfunc.plugin;

import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayConfig;
import com.alipay.api.DefaultAlipayClient;
import com.alipay.api.domain.AlipayTradePrecreateModel;
import com.alipay.api.request.AlipayTradePrecreateRequest;
import com.alipay.api.response.AlipayTradePrecreateResponse;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.godfunc.dto.PayInfoDTO;
import com.godfunc.entity.Order;
import com.godfunc.entity.OrderDetail;
import org.pf4j.PluginWrapper;

import java.util.Map;

public class NormalPayPlugin extends BasePlugin {
    /**
     * Constructor to be used by plugin manager for plugin instantiation.
     * Your plugins have to provide constructor with this exact signature to
     * be successfully loaded by manager.
     *
     * @param wrapper
     */

    private ObjectMapper objectMapper = new ObjectMapper();

    public NormalPayPlugin(PluginWrapper wrapper) {
        super(wrapper);
    }

    @Override
    public PayInfoDTO doPay(Order order) {
        OrderDetail detail = order.getDetail();
        AlipayConfig alipayConfig = new AlipayConfig();
        alipayConfig.setServerUrl(detail.getChannelCreateUrl());
        alipayConfig.setAppId(detail.getPayChannelAccountCode());
        Map<String, String> keyInfoMap = null;
        try {
            keyInfoMap = objectMapper.readValue(detail.getPayChannelAccountKeyInfo(), new TypeReference<Map<String, String>>() {
            });
        } catch (Exception e) {
            log.error("解析渠道账号密钥信息异常 keyInfo={}, {}", detail.getPayChannelAccountKeyInfo(), e);
        }

        alipayConfig.setPrivateKey(keyInfoMap.get("privateKey"));
        alipayConfig.setAppCertContent(keyInfoMap.get("appCertContent"));
        alipayConfig.setAlipayPublicCertContent(keyInfoMap.get("alipayPublicCertContent"));
        alipayConfig.setRootCertContent(keyInfoMap.get("rootCertContent"));
        try {
            DefaultAlipayClient defaultAlipayClient = new DefaultAlipayClient(alipayConfig);
            AlipayTradePrecreateRequest request = new AlipayTradePrecreateRequest();
            AlipayTradePrecreateModel model = new AlipayTradePrecreateModel();
            model.setOutTradeNo(order.getOrderNo());
            // TODO
            //model.setTotalAmount(AmountUtil.convertCent2Dollar(order.getAmount()));
            model.setSubject(detail.getGoodName());
            model.setBody(detail.getGoodName());
            request.setBizModel(model);
            AlipayTradePrecreateResponse response = defaultAlipayClient.certificateExecute(request);
            if ("10000".equals(response.getCode())) {
                return new PayInfoDTO(null, response.getQrCode());
            } else {
                // TODO
                //throw new GException("上游错误 {}", response.getSubMsg());
            }
        } catch (AlipayApiException e) {
            e.printStackTrace();
        }
        return null;
    }
}
