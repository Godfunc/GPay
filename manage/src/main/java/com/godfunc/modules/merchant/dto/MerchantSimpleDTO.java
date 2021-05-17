package com.godfunc.modules.merchant.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

@Data
@ApiModel("商户简单信息")
public class MerchantSimpleDTO implements Serializable {

    @ApiModelProperty("id")
    private Long id;

    @ApiModelProperty("商户名")
    private String name;

    @ApiModelProperty("商户号")
    private String code;
}
