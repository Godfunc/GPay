package com.godfunc.modules.merchant.param;

import com.godfunc.constant.CommonConstant;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;


@Data
@ApiModel("新增商户渠道费率")
public class MerchantChannelRateSaveParam {

    @ApiModelProperty("id")
    private Long id;

    @ApiModelProperty("渠道主类id")
    @NotNull(message = "渠道主类不能为空")
    private Long payChannelId;

    @ApiModelProperty("渠道子类id")
    @NotNull(message = "渠道子类不能为空")
    private Long payCategoryId;

    @ApiModelProperty("商户号")
    @NotNull(message = "商户号不能为空")
    private String merchantCode;

    @ApiModelProperty("费率")
    @NotBlank(message = "费率不能为空")
    @Pattern(regexp = CommonConstant.RATE_PATTERN, message = "费率数据格式不正确")
    private String rate;
}
