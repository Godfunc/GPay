package com.godfunc.util;


import com.godfunc.constant.CommonConstant;
import com.godfunc.exception.GException;
import com.godfunc.result.ApiCode;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * hibernate-validator校验工具类
 *
 * @author godfunc
 */
public class ValidatorUtils {
    private static Validator validator;

    static {
        validator = Validation.buildDefaultValidatorFactory().getValidator();
    }

    /**
     * 校验对象
     *
     * @param object 待校验对象
     */
    public static void validate(Object object)
            throws GException {
        Set<ConstraintViolation<Object>> constraintViolations = validator.validate(object);
        if(!constraintViolations.isEmpty()) {
            throw new GException(constraintViolations.stream()
                    .map(ConstraintViolation::getMessage)
                    .collect(Collectors.joining(CommonConstant.VALIDATE_SPLIT)), ApiCode.PARAM_ERROR);
        }
    }
}
