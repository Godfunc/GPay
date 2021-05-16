package com.godfunc.modules.merchant.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@ApiModel("商户渠道费率信息")
public class MerchantChannelRateDTO implements Serializable {

    @ApiModelProperty("id")
    private Long id;

    @ApiModelProperty("商户号")
    private String merchantCode;

    @ApiModelProperty("商户名称")
    private String merchantName;

    @ApiModelProperty("渠道关联id")
    private Long categoryChannelId;

    @ApiModelProperty("渠道主类编号")
    private String payCategoryCode;

    @ApiModelProperty("渠道子类编号")
    private String payChannelCode;

    @ApiModelProperty("渠道主类名")
    private String payCategoryName;

    @ApiModelProperty("渠道子类名")
    private String payChannelName;

    @ApiModelProperty("费率")
    private String rate;

    @ApiModelProperty("创建时间")
    private LocalDateTime createTime;

}
