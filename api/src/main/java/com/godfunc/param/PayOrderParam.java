package com.godfunc.param;

import com.godfunc.constant.CommonConstant;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import java.time.LocalDateTime;

/**
 * @author godfunc
 * @email godfunc@outlook.com
 */
@Data
@ApiModel("下单信息")
public class PayOrderParam {

    @ApiModelProperty("商户号")
    @NotBlank(message = "商户号不能为空")
    private String merchantCode;

    @ApiModelProperty("订单号")
    @NotBlank(message = "订单号不能为空")
    private String outTradeNo;

    @ApiModelProperty("订单金额")
    @NotBlank(message = "订单金额不能为空")
    @Pattern(regexp = CommonConstant.AMOUNT_PATTEN, message = "订单金额格式不正确")
    private String amount;

    @ApiModelProperty("下单客户ip")
    @NotBlank(message = "下单客户ip不能为空")
    private String clientIp;

    @ApiModelProperty("支付类型")
    @NotBlank(message = "支付类型不能为空")
    private String type;

    @ApiModelProperty("商品名称")
    private String goodName;

    @ApiModelProperty("下单时间")
    @NotBlank(message = "下单时间不能为空")
    private LocalDateTime time;

    @ApiModelProperty("回调地址")
    @NotBlank(message = "回调地址不能为空")
    private String notifyUrl;

    @ApiModelProperty("备注")
    private String remark;

    @ApiModelProperty("签名")
    @NotBlank(message = "签名不能为空")
    private String sign;

}
