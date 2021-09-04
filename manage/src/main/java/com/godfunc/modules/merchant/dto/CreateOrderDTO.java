package com.godfunc.modules.merchant.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@ApiModel("创建订单返回信息")
@AllArgsConstructor
public class CreateOrderDTO {

    @ApiModelProperty("商户订单号")
    private String outTradeNo;

    @ApiModelProperty("平台订单号")
    private String tradNo;

    @ApiModelProperty("支付信息")
    private String payUrl;

    @ApiModelProperty("过期时间")
    private LocalDateTime expiredTime;
}
