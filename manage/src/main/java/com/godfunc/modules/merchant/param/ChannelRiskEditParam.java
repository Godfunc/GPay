package com.godfunc.modules.merchant.param;

import com.godfunc.constant.CommonConstant;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.time.LocalTime;


@Data
@ApiModel("修改渠道风控")
public class ChannelRiskEditParam {

    @ApiModelProperty("渠道风控id")
    @NotNull(message = "请选择要修改的渠道风控数据")
    private Long id;

    @ApiModelProperty("每日最大限额")
    @Pattern(regexp = CommonConstant.AMOUNT_PATTEN, message = "每日最大限额金额格式错误")
    private String dayAmountMax;

    @ApiModelProperty("单笔最大限额")
    @Pattern(regexp = CommonConstant.AMOUNT_PATTEN, message = "单笔最大限额金额格式错误")
    private String oneAmountMax;

    @ApiModelProperty("单笔最小限额")
    @Pattern(regexp = CommonConstant.AMOUNT_PATTEN, message = "每日最小限额金额格式错误")
    private String oneAmountMin;

    @ApiModelProperty("指定单笔金额，多个用,分割")
    private String oneAmount;

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
