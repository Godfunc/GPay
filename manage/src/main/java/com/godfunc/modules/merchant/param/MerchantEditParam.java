package com.godfunc.modules.merchant.param;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;


@Data
@ApiModel("修改商户")
public class MerchantEditParam {

    @ApiModelProperty("商户id")
    @NotNull(message = "请选择要修改的商户")
    private Long id;

    @ApiModelProperty("商户名")
    @NotBlank(message = "商户名不能为空")
    private String name;

    @ApiModelProperty("商户公钥")
    private String publicKey;

    @ApiModelProperty("状态")
    @NotNull(message = "商户状态不能为空")
    private Integer status;
}
