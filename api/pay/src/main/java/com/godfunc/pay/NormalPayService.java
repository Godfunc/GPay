package com.godfunc.pay;

import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayConfig;
import com.alipay.api.DefaultAlipayClient;
import com.alipay.api.domain.AlipayTradePrecreateModel;
import com.alipay.api.request.AlipayTradePrecreateRequest;
import com.alipay.api.response.AlipayTradePrecreateResponse;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.godfunc.constant.ApiConstant;
import com.godfunc.constant.PayLogicalConstant;
import com.godfunc.dto.PayInfoDto;
import com.godfunc.entity.Order;
import com.godfunc.entity.OrderDetail;
import com.godfunc.exception.GException;
import com.godfunc.util.AmountUtil;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;


@Slf4j
@Service(ApiConstant.PAY_SERVICE_PREFIX + PayLogicalConstant.NORMAL)
@RequiredArgsConstructor
public class NormalPayService extends DefaultAbstractPay {

    private final ObjectMapper objectMapper;

    @Override
    public PayInfoDto doPay(Order order) {
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
            model.setTotalAmount(AmountUtil.convertCent2Dollar(order.getAmount()));
            model.setSubject(detail.getGoodName());
            model.setBody(detail.getGoodName());
            request.setBizModel(model);
            AlipayTradePrecreateResponse response = defaultAlipayClient.certificateExecute(request);
            if ("10000".equals(response.getCode())) {
                return new PayInfoDto(null, response.getQrCode());
            } else {
                throw new GException("上游错误 {}", response.getSubMsg());
            }
        } catch (AlipayApiException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public void handleResponse(PayInfoDto payInfo, HttpServletRequest request, HttpServletResponse response) {
        response.setContentType(MediaType.IMAGE_JPEG_VALUE);
        int width = 300;
        int height = 300;
        //制定图片格式
        String format = "jpeg";
        //内容
        String content = payInfo.getPayUrl();

        //定义二维码的参数
        HashMap map = new HashMap();
        map.put(EncodeHintType.CHARACTER_SET, "utf-8");//编码
        map.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.M);
        map.put(EncodeHintType.MARGIN, 2);

        //生成二维码
        try {
            BitMatrix bitMatrix = new MultiFormatWriter().encode(content, BarcodeFormat.QR_CODE, width, height, map);
            MatrixToImageWriter.writeToStream(bitMatrix, format, response.getOutputStream());

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
