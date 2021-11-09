package com.godfunc.modules.merchant.excel;

import com.alibaba.excel.annotation.ExcelProperty;
import com.godfunc.entity.Order;
import com.godfunc.util.AmountUtil;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
public class OrderExcel implements Serializable {

    public OrderExcel(Order order) {
        this.merchantCode = order.getMerchantCode();
        this.merchantName = order.getMerchantName();
        this.outTradeNo = order.getOutTradeNo();
        this.orderNo = order.getOrderNo();
        this.channelAccountCode = order.getChannelAccountCode();
        this.amount = AmountUtil.convertCent2Dollar(order.getAmount());
        this.realAmount = AmountUtil.convertCent2Dollar(order.getRealAmount());
        this.createTime = order.getCreateTime();
        this.payTime = order.getPayTime();
        this.payStr = order.getPayStr();
        this.notifyTime = order.getNotifyTime();
        this.status = order.resolverStatus(order.getStatus());
    }

    /**
     * 商户号
     */
    @ExcelProperty("商户号")
    private String merchantCode;
    /**
     * 商户名
     */
    @ExcelProperty("商户名")
    private String merchantName;
    /**
     * 商户单号
     */
    @ExcelProperty("商户单号")
    private String outTradeNo;
    /**
     * 平台单号
     */
    @ExcelProperty("平台单号")
    private String orderNo;

    /**
     * 渠道账号商户号
     */
    @ExcelProperty("渠道账号商户号")
    private String channelAccountCode;
    /**
     * 订单金额
     */
    @ExcelProperty("订单金额")
    private String amount;
    /**
     * 实际支付金额
     */
    @ExcelProperty("实际支付金额")
    private String realAmount;

    /**
     * 订单创建时间
     */
    @ExcelProperty("订单创建时间")
    private LocalDateTime createTime;
    /**
     * 订单支付时间
     */
    @ExcelProperty("订单支付时间")
    private LocalDateTime payTime;

    /**
     * 支付链接
     */
    @ExcelProperty("支付链接")
    private String payStr;
    /**
     * 回调时间
     */
    @ExcelProperty("回调时间")
    private LocalDateTime notifyTime;
    /**
     * 订单状态 1.已下单 2.已扫码 3.已支付 4.已回调
     */
    @ExcelProperty("订单状态")
    private String status;
}