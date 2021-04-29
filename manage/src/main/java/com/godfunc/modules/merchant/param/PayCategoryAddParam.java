package com.godfunc.modules.merchant.param;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;


@Data
@ApiModel("新增渠道主类")
public class PayCategoryAddParam {

    @ApiModelProperty("名称")
    @NotBlank(message = "名称不能为空")
    public String name;

    @ApiModelProperty("编号")
    @NotBlank(message = "编号不能为空")
    private String code;

    @ApiModelProperty("状态")
    @NotNull(message = "状态不能为空")
    private Integer status;
}
