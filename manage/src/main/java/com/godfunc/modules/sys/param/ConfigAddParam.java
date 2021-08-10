package com.godfunc.modules.sys.param;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * @author godfunc
 * @email godfunc@outlook.com
 */
@Data
@ApiModel("新增配置")
public class ConfigAddParam {

    @ApiModelProperty("配置名")
    @NotBlank(message = "配置名不能为空")
    private String name;

    @ApiModelProperty("配置值")
    @NotBlank(message = "配置值不能为空")
    private String value;

    @ApiModelProperty("备注")
    private String remark;

}
