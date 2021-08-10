package com.godfunc.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author godfunc
 * @email godfunc@outlook.com
 * @date 2021/6/16
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class PayInfoDto {

    private String tradeNo;

    private String payUrl;
}
