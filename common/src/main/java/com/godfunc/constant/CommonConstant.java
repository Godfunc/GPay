package com.godfunc.constant;

/**
 * @author godfunc
 * @email godfunc@outlook.com
 */
public interface CommonConstant {

    /**
     * Date格式化字符串
     */
    String DATE_FORMAT = "yyyy-MM-dd";
    /**
     * DateTime格式化字符串
     */
    String DATETIME_FORMAT = "yyyy-MM-dd HH:mm:ss";
    /**
     * Time格式化字符串
     */
    String TIME_FORMAT = "HH:mm:ss";

    String AMOUNT_PATTEN = "^([1-9]\\d{0,9}|0)(\\.\\d{1,2})?$";
    String RATE_PATTERN = "^0.\\d+$";

    String ONE_AMOUNT_SPLIT = ",";
    String VALIDATE_SPLIT = ",";

    String ORDER_SUCCESS_CACHE_PREFIX = "order:success:cache:prefix:";
}
