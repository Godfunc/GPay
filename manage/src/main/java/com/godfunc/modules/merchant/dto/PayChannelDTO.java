package com.godfunc.modules.merchant.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@ApiModel("渠道子类信息")
public class PayChannelDTO implements Serializable {

    @ApiModelProperty("id")
    private Long id;

    @ApiModelProperty("名称")
    private String name;

    @ApiModelProperty("编号")
    private String code;

    @ApiModelProperty("状态")
    private Integer status;

    @ApiModelProperty("下单网关")
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
    private Float costRate;

    @ApiModelProperty("创建人id")
    private Long createId;

    @ApiModelProperty("更新人id")
    private Long updateId;

    @ApiModelProperty("创建时间")
    private LocalDateTime createTime;

    @ApiModelProperty("更新时间")
    private LocalDateTime updateTime;
}
