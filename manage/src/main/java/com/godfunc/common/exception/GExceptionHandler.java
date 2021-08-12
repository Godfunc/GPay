package com.godfunc.common.exception;

import com.godfunc.result.ApiCodeMsg;
import com.godfunc.result.R;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;


/**
 * 异常处理器
 *
 * @author godfunc
 * @email godfunc@outlook.com
 */
@Slf4j
@RestControllerAdvice
public class GExceptionHandler {

    @ExceptionHandler(AccessDeniedException.class)
    public R handleException(AccessDeniedException e) {
        log.error(e.getMessage(), e);
        return R.fail(ApiCodeMsg.NOPERMISSION);
    }
}
