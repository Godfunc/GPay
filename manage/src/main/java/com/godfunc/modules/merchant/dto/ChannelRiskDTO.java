package com.godfunc.modules.merchant.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Data
@ApiModel("渠道风控信息")
public class ChannelRiskDTO implements Serializable {

    @ApiModelProperty("id")
    private Long id;

    @ApiModelProperty("渠道子类id")
    private Long channelId;

    @ApiModelProperty("渠道子类编号")
    private String channelCode;

    @ApiModelProperty("渠道账号id")
    private Long channelAccountId;

    @ApiModelProperty("账号商户号")
    private Long accountCode;

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

    @ApiModelProperty("创建人id")
    private Long createId;

    @ApiModelProperty("更新人id")
    private Long updateId;

    @ApiModelProperty("创建时间")
    private LocalDateTime createTime;

    @ApiModelProperty("更新时间")
    private LocalDateTime updateTime;
}
