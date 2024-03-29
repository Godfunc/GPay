package com.godfunc.modules.merchant.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Data
@ApiModel("渠道风控简单信息")
public class ChannelRiskSimpleDTO implements Serializable {

    @ApiModelProperty("id")
    private Long id;

    @ApiModelProperty("每日最大限额")
    private String dayAmountMax;

    @ApiModelProperty("单笔最大限额")
    private String oneAmountMax;

    @ApiModelProperty("单笔最小限额")
    private String oneAmountMin;

    @ApiModelProperty("指定单笔金额，多个用,分割")
    private String oneAmount;

    @ApiModelProperty("交易开始时间")
    private LocalTime dayStartTime;

    @ApiModelProperty("交易结束时间")
    private LocalTime dayEndTime;

    @ApiModelProperty("状态 0禁用 1启用")
    private Integer status;

    @ApiModelProperty("创建时间")
    private LocalDateTime createTime;
}
