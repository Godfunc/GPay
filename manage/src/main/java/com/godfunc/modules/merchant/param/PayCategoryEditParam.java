package com.godfunc.modules.merchant.param;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;


@Data
@ApiModel("修改渠道主类")
public class PayCategoryEditParam {

    @ApiModelProperty("名称")
    @NotBlank(message = "名称不能为空")
    public String name;
    @ApiModelProperty("编号")
    @NotBlank(message = "编号不能为空")
    public String code;
    @ApiModelProperty("id")
    @NotNull(message = "请选择要修改的渠道主类")
    private Long id;
    @ApiModelProperty("状态")
    @NotNull(message = "状态不能为空")
    private Integer status;
}
