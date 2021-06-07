package com.godfunc.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;


/**
 * @author godfunc
 * @email godfunc@outlook.com
 */
@Data
@NoArgsConstructor
@ApiModel("订单返回信息")
public class PayOrderDTO {

    public PayOrderDTO(String outTradeNo, String tradNo, String payUrl, LocalDateTime expiredTime) {
        this.outTradeNo = outTradeNo;
        this.tradNo = tradNo;
        this.payUrl = payUrl;
        this.expiredTime = expiredTime;
    }

    @ApiModelProperty("商户订单号")
    private String outTradeNo;

    @ApiModelProperty("平台订单号")
    private String tradNo;

    @ApiModelProperty("支付信息")
    private String payUrl;

    @ApiModelProperty("过期时间")
    private LocalDateTime expiredTime;

    @ApiModelProperty("签名")
    private String sign;
}
