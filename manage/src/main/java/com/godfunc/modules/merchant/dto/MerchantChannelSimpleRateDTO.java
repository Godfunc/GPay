package com.godfunc.modules.merchant.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
@ApiModel("商户渠道费率简单信息")
public class MerchantChannelSimpleRateDTO implements Serializable {

    @ApiModelProperty("渠道主类编号")
    private String payCategoryCode;

    @ApiModelProperty("渠道主类名")
    private String payCategoryName;

    @ApiModelProperty("渠道子类列表")
    private List<ChannelRateDTO> channelList;
}
