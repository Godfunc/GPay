package com.godfunc.modules.merchant.param;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;


@Data
@ApiModel("修改渠道账号")
public class PayChannelAccountEditParam {

    @ApiModelProperty("id")
    @NotNull(message = "请选择要修改的渠道账号")
    private Long id;

    @ApiModelProperty("名称")
    @NotBlank(message = "名称不能为空")
    private String name;

    @ApiModelProperty("渠道子类id")
    @NotNull(message = "请选择渠道子类")
    private Long channelId;

    @ApiModelProperty("账号商户编号")
    @NotBlank(message = "商户编号不能为空")
    private String accountCode;

    @ApiModelProperty("状态")
    @NotNull(message = "状态不能为空")
    private Integer status;

    @ApiModelProperty("密钥信息")
    private String keyInfo;

    @ApiModelProperty("权重")
    @NotNull(message = "请设置权重")
    private Integer weight;

    @ApiModelProperty("风控")
    @NotNull(message = "请选择风控")
    private Integer riskType;
}
