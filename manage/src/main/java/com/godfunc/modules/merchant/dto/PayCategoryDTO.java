package com.godfunc.modules.merchant.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@ApiModel("渠道主类信息")
public class PayCategoryDTO implements Serializable {

    @ApiModelProperty("id")
    private Long id;

    @ApiModelProperty("名称")
    private String name;

    @ApiModelProperty("编号")
    private String code;

    @ApiModelProperty("状态")
    private Integer status;

    @ApiModelProperty("创建人id")
    private Long createId;

    @ApiModelProperty("更新人id")
    private Long updateId;

    @ApiModelProperty("创建时间")
    private LocalDateTime createTime;

    @ApiModelProperty("更新时间")
    private LocalDateTime updateTime;
}
