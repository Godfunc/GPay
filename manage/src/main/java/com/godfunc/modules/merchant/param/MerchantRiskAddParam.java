package com.godfunc.modules.merchant.param;

import com.godfunc.constant.CommonConstant;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.time.LocalTime;


@Data
@ApiModel("新增商户风控")
public class MerchantRiskAddParam {

    @ApiModelProperty("商户号")
    @NotBlank(message = "商户号不能为空")
    private String merchantCode;

    @ApiModelProperty("单笔最大限额")
    @Pattern(regexp = CommonConstant.AMOUNT_PATTEN, message = "单笔最大限额金额格式错误")
    private String oneAmountMax;

    @ApiModelProperty("单笔最小限额")
    @Pattern(regexp = CommonConstant.AMOUNT_PATTEN, message = "单笔最小限额金额格式错误")
    private String oneAmountMin;

    @ApiModelProperty("交易开始时间")
    @NotNull(message = "交易开始时间不能为空")
    private LocalTime dayStartTime;

    @ApiModelProperty("交易结束时间")
    @NotNull(message = "交易结束时间不能为空")
    private LocalTime dayEndTime;

    @ApiModelProperty("状态 0禁用 1启用")
    @NotNull(message = "状态不能为空")
    private Integer status;
}
