package com.godfunc.modules.merchant.param;

import com.godfunc.constant.CommonConstant;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;


@Data
@ApiModel("创建订单")
public class CreateOrderParam {

    @ApiModelProperty("金额")
    @NotNull(message = "订单金额不能为空")
    @Pattern(regexp = CommonConstant.AMOUNT_PATTEN)
    private String amount;

    @ApiModelProperty("商户Code")
    @NotBlank(message = "商户编码不能为空")
    private String merchantCode;

    @ApiModelProperty("下单地址")
    @NotBlank(message = "下单地址不能为空")
    private String createUrl;

    @ApiModelProperty("支付类型")
    @NotBlank(message = "支付类型不能为空")
    private String type;

    @ApiModelProperty("商户私钥")
    @NotBlank(message = "商户私钥不能为空")
    private String privateKey;

    @ApiModelProperty("商品名称")
    @NotBlank(message = "商品名称不能为空")
    private String goodName;

    @ApiModelProperty("回调地址")
    @NotBlank(message = "回调地址不能为空")
    private String notifyUrl;

}
