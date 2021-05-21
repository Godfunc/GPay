package com.godfunc.modules.merchant.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * @author godfunc
 * @email godfunc@outlook.com
 * @date 2021/5/20
 */
@Data
@ApiModel("渠道费率费率信息")
public class ChannelRateDTO implements Serializable {
    @ApiModelProperty("id")
    private Long id;

    @ApiModelProperty("渠道关联id")
    private Long categoryChannelId;

    @ApiModelProperty("渠道子类编号")
    private String payChannelCode;

    @ApiModelProperty("渠道子类名")
    private String payChannelName;

    @ApiModelProperty("费率")
    private String rate;
}
