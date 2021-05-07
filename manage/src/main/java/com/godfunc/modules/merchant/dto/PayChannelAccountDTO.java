package com.godfunc.modules.merchant.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@ApiModel("渠道账号信息")
public class PayChannelAccountDTO implements Serializable {

    @ApiModelProperty("id")
    private Long id;

    @ApiModelProperty("渠道子类id")
    private Long channelId;

    @ApiModelProperty("渠道子类名称")
    private String channelCode;

    @ApiModelProperty("名称")
    private String name;

    @ApiModelProperty("账号商户编号")
    private String accountCode;

    @ApiModelProperty("状态")
    private Integer status;

    @ApiModelProperty("渠道子类商户密钥")
    private String keyInfo;

    @ApiModelProperty("权重")
    private Integer weight;

    @ApiModelProperty("风控设置")
    private Integer riskType;

    @ApiModelProperty("支付类型信息")
    private String payTypeInfo;

    @ApiModelProperty("创建时间")
    private LocalDateTime createTime;

    @ApiModelProperty("更新时间")
    private LocalDateTime updateTime;
}
