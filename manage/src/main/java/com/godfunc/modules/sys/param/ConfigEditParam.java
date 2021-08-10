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
@ApiModel("修改配置")
public class ConfigEditParam {

    @ApiModelProperty("配置id")
    @NotBlank(message = "请选择要修改的数据")
    private Long id;

    @ApiModelProperty("配置名")
    @NotBlank(message = "配置名不能为空")
    private String name;

    @ApiModelProperty("配置值")
    @NotBlank(message = "配置值不能为空")
    private String value;

    @ApiModelProperty("备注")
    private String remark;
}
