package com.godfunc.exception;

import com.godfunc.constant.CommonConstant;
import com.godfunc.result.ApiCode;
import com.godfunc.result.ApiCodeMsg;
import com.godfunc.result.R;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindException;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.stream.Collectors;


/**
 * 异常处理器
 *
 * @author godfunc
 * @email godfunc@outlook.com
 */
@Slf4j
@RestControllerAdvice
public class BaseExceptionHandler {
    /**
     * 处理自定义异常
     */
    @ExceptionHandler(GException.class)
    public R handleException(GException e) {
        return R.fail(e.getCode(), e.getMsg());
    }

    @ExceptionHandler(DuplicateKeyException.class)
    public R handleException(DuplicateKeyException e) {
        log.error(e.getMessage(), e);
        return R.fail(ApiCodeMsg.DATA_DUPLICATION);
    }

    @ExceptionHandler(Exception.class)
    public R handleException(Exception e) {
        log.error(e.getMessage(), e);
        return R.fail(ApiCodeMsg.OTHER);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public R handleException(MethodArgumentNotValidException e) {
        return R.fail(ApiCode.PARAM_ERROR, e.getBindingResult().getFieldErrors().stream().map(ObjectError::getDefaultMessage).collect(Collectors.joining(CommonConstant.VALIDATE_SPLIT)));
    }

    @ExceptionHandler(BindException.class)
    public R handleException(BindException e) {
        return R.fail(ApiCode.PARAM_ERROR, e.getAllErrors().stream().map(ObjectError::getDefaultMessage).collect(Collectors.joining(CommonConstant.VALIDATE_SPLIT)));
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public R handleException(HttpMessageNotReadableException e) {
        return R.fail(ApiCodeMsg.REQUEST_BOY_IS_EMPTY);
    }
}