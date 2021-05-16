package com.godfunc.modules.merchant.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

@Data
@ApiModel("渠道关联信息")
public class PayCategoryChannelDTO implements Serializable {

    @ApiModelProperty("id")
    private Long id;

    @ApiModelProperty("渠道主类编号")
    private String payCategoryCode;

    @ApiModelProperty("渠道主类名称")
    private String payCategoryName;

    @ApiModelProperty("渠道主类编号")
    private String payChannelCode;

    @ApiModelProperty("渠道主类名称")
    private String payChannelName;
}
