package com.godfunc.notify;

import com.godfunc.constant.ApiConstant;
import com.godfunc.constant.PayLogicalConstant;
import com.godfunc.model.NotifyOrderInfo;
import com.godfunc.util.AmountUtil;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service(ApiConstant.NOTIFY_SERVICE_PREFIX + PayLogicalConstant.TEST)
public class TestNotifyHandler implements NotifyOrderHandler {
    @Override
    public boolean signCheck(Map<String, Object> params, String publicKey) {
        return true;
    }

    @Override
    public NotifyOrderInfo resolveInfo(Map<String, Object> params) {
        return new NotifyOrderInfo(params.get("tradeNo").toString(), params.get("outTradeNo").toString(), AmountUtil.convertDollar2Cent(params.get("amount").toString()), AmountUtil.convertDollar2Cent(params.get("amount").toString()));
    }

    @Override
    public boolean checkSuccess(Map<String, Object> params) {
        return "1".equals(params.getOrDefault("status", ""));
    }
}
