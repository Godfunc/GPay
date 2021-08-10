package com.godfunc.notify;


import com.godfunc.entity.Order;
import com.godfunc.enums.NotifyResultEnum;
import com.godfunc.model.NotifyOrderInfo;
import com.godfunc.util.RequestArgumentResolveUtils;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

public interface NotifyOrderHandler {

    /**
     * 参数解析
     *
     * @return 返回解析之后的参数
     */
    default Map<String, Object> argumentResolve(HttpServletRequest request) {
        return RequestArgumentResolveUtils.getParams(request);
    }

    /**
     * 签名验证
     *
     * @param params    请求参数
     * @param publicKey 密钥
     * @return 结果
     */
    boolean signCheck(Map<String, Object> params, String publicKey);


    /**
     * 解析订单的核心信息
     *
     * @param params 请求参数
     * @return
     */
    NotifyOrderInfo resolveInfo(Map<String, Object> params);

    /**
     * 检查订单是否成功
     *
     * @param params
     * @return
     */
    boolean checkSuccess(Map<String, Object> params);

    /**
     * 回调成功返回
     *
     * @return
     */
    default String successResult() {
        return NotifyResultEnum.SUCCESS.getValue();
    }

    /**
     * 回调失败返回
     *
     * @return
     */
    default String failResult() {
        return NotifyResultEnum.FAIL.getValue();
    }

    /**
     * 查询订单
     *
     * @return
     */
    default boolean queryOrder(Order order) {
        return true;
    }
}
