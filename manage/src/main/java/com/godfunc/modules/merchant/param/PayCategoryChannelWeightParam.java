package com.godfunc.modules.merchant.param;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * @author godfunc
 * @email godfunc@outlook.com
 * @date 2021/7/26
 */

@Data
@ApiModel("渠道权重设置")
public class PayCategoryChannelWeightParam {

    @ApiModelProperty("id")
    @NotNull(message = "请选择要修改的渠道主类")
    private Long id;

    @ApiModelProperty("权重")
    @NotNull(message = "权重不能为空")
    private Integer weight;
}
