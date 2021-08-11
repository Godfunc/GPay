package com.godfunc.notify;

import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.godfunc.constant.ApiConstant;
import com.godfunc.constant.PayLogicalConstant;
import com.godfunc.model.NotifyOrderInfo;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service(ApiConstant.NOTIFY_SERVICE_PREFIX + PayLogicalConstant.NORMAL)
public class NormalNotifyHandler implements NotifyOrderHandler {
    @Override
    public boolean signCheck(Map<String, Object> params, String publicKey) {
        return true;
    }

    @Override
    public NotifyOrderInfo resolveInfo(Map<String, Object> params) {
        return new NotifyOrderInfo(IdWorker.getIdStr(), IdWorker.getIdStr(), 100L, 99L);
    }

    @Override
    public boolean checkSuccess(Map<String, Object> params) {
        return true;
    }
}
