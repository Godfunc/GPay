package com.godfunc.exception;

import com.godfunc.result.ApiCodeMsg;
import com.godfunc.result.R;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;


/**
 * 异常处理器
 * @author godfunc
 * @email godfunc@outlook.com
 */
@Slf4j
@RestControllerAdvice
public class GExceptionHandler {
    /**
     * 处理自定义异常
     */
    @ExceptionHandler(GException.class)
    public R handleGException(GException e) {
        return R.fail(e.getCode(), e.getMsg());
    }

    @ExceptionHandler(DuplicateKeyException.class)
    public R handleDuplicateKeyException(DuplicateKeyException e) {
        log.error(e.getMessage(), e);
        return R.fail(ApiCodeMsg.DATA_DUPLICATION);
    }

    @ExceptionHandler(Exception.class)
    public R handleException(Exception e) {
        log.error(e.getMessage(), e);
        return R.fail(ApiCodeMsg.OTHER);
    }

}
