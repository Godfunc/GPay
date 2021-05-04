package com.godfunc.modules.merchant.param;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;


@Data
@ApiModel("修改渠道子类")
public class PayChannelEditParam {

    @ApiModelProperty("渠道子类id")
    @NotNull(message = "请选择要修改的渠道子类")
    private Long id;

    @ApiModelProperty("名称")
    @NotBlank(message = "名称不能为空")
    public String name;

    @ApiModelProperty("编号")
    @NotBlank(message = "编号不能为空")
    private String code;

    @ApiModelProperty("状态")
    @NotNull(message = "状态不能为空")
    private Integer status;

    @ApiModelProperty("订单创建网关")
    @NotBlank(message = "订单创建网关不能为空")
    private String createUrl;

    @ApiModelProperty("订单查询网关")
    private String queryUrl;

    @ApiModelProperty("通知地址")
    private String notifyUrl;

    @ApiModelProperty("支付类型信息")
    private String payTypeInfo;

    @ApiModelProperty("逻辑标识")
    private String logicalTag;

    @ApiModelProperty("费率")
    @NotBlank(message = "费率不能为空")
    @Pattern(regexp = "^\\d{1,2}\\.\\d{0,6}$", message = "费率设置不正确")
    private String costRate;
}
