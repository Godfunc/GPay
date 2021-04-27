package com.godfunc.modules.merchant.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@ApiModel("平台密钥")
@NoArgsConstructor
@AllArgsConstructor
public class MerchantKeysDTO implements Serializable {

    @ApiModelProperty("平台公钥")
    private String publicKey;

    @ApiModelProperty("平台私钥")
    private String privateKey;
}
