package com.godfunc.modules.merchant.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

@Data
@ApiModel("渠道主类简单信息")
public class PayCategorySimpleDTO implements Serializable {

    @ApiModelProperty("id")
    private Long id;

    @ApiModelProperty("名称")
    private String name;
}
