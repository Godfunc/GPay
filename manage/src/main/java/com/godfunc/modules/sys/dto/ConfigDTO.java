package com.godfunc.modules.sys.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * @author godfunc
 * @email godfunc@outlook.com
 */
@Data
@ApiModel("配置分页")
public class ConfigDTO implements Serializable {

    @ApiModelProperty("配置id")
    private Long id;

    @ApiModelProperty("配置名")
    private String name;

    @ApiModelProperty("配置值")
    private String value;

    @ApiModelProperty("备注")
    private String remark;

    @ApiModelProperty("创建时间")
    private LocalDateTime createTime;

    @ApiModelProperty("更新时间")
    private LocalDateTime updateTime;
}
