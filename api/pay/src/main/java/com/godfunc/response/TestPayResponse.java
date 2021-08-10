package com.godfunc.response;


import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
public class TestPayResponse {

    private Integer code;
    private String message;
    private DataDTO data;

    @NoArgsConstructor
    @Data
    public static class DataDTO {
        private String tradeNo;
        private String payUrl;
    }
}
