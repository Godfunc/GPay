package com.godfunc.modules.merchant.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Data
@ApiModel("商户风控")
public class MerchantRiskDTO implements Serializable {

    @ApiModelProperty("id")
    private Long id;

    @ApiModelProperty("单笔最小限额")
    private String oneAmountMin;

    @ApiModelProperty("单笔最大")
    private String oneAmountMax;

    @ApiModelProperty("交易开始时间")
    private LocalTime dayStartTime;

    @ApiModelProperty("交易结束时间")
    private LocalTime dayEndTime;

    @ApiModelProperty("状态")
    private Integer status;

    @ApiModelProperty("创建时间")
    private LocalDateTime createTime;
}
