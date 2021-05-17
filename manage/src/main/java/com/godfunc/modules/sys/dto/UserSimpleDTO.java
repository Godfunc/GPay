package com.godfunc.modules.sys.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * @author godfunc
 */
@Data
@ApiModel("用户简单列表")
public class UserSimpleDTO implements Serializable {

    @ApiModelProperty("用户id")
    private Long id;

    @ApiModelProperty("用户名")
    private String username;
}
