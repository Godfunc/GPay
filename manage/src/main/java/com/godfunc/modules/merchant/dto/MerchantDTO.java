package com.godfunc.modules.merchant.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@ApiModel("商户信息")
public class MerchantDTO implements Serializable {

    @ApiModelProperty("id")
    private Long id;

    @ApiModelProperty("商户对应的用户id")
    private Long userId;

    @ApiModelProperty("商户名")
    private String name;

    @ApiModelProperty("商户号")
    private String code;

    @ApiModelProperty("商户类型")
    private Integer type;

    @ApiModelProperty("平台公钥")
    private String platPublicKey;

    @ApiModelProperty("商户公钥")
    private String publicKey;

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
