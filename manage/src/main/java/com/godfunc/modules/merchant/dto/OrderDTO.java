package com.godfunc.modules.merchant.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * @author godfunc
 * @email godfunc@outlook.com
 * @date 2021/5/20
 */
@Data
@ApiModel("订单信息")
public class OrderDTO implements Serializable {

    @ApiModelProperty("id")
    private Long id;

    /**
     * 商户号
     */
    @ApiModelProperty("商户号")
    private String merchantCode;

    /**
     * 商户名
     */
    @ApiModelProperty("商户名")
    private String merchantName;

    /**
     * 商户单号
     */
    @ApiModelProperty("商户单号")
    private String outTradeNo;

    /**
     * 平台单号
     */
    private String orderNo;

    /**
     * 上游单号
     */
    @ApiModelProperty("上游单号")
    private String tradeNo;

    /**
     * 渠道账号商户号
     */
    @ApiModelProperty("渠道账号商户号")
    private String channelAccountCode;

    /**
     * 订单金额
     */
    @ApiModelProperty("订单金额")
    private Long amount;

    /**
     * 实际支付金额
     */
    @ApiModelProperty("实际金额")
    private Long realAmount;

    /**
     * 客户端创建时间
     */
    @ApiModelProperty("客户端创建时间")
    private LocalDateTime clientCreateTime;

    /**
     * 订单创建时间
     */
    @ApiModelProperty("订单创建时间")
    private LocalDateTime createTime;

    /**
     * 订单支付时间
     */
    @ApiModelProperty("支付时间")
    private LocalDateTime payTime;

    /**
     * 支付类型信息
     */
    @ApiModelProperty("支付类型")
    private String payType;

    /**
     * 回调时间
     */
    @ApiModelProperty("回调时间")
    private LocalDateTime notifyTime;

    /**
     * 订单状态 1.已下单 2.已扫码 3.已支付 4.已回调
     */
    @ApiModelProperty("订单状态")
    private Integer status;
}
