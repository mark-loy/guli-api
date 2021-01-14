package com.mark.servicebase.handler;

import com.mark.commonutil.entity.Result;
import com.mark.servicebase.exception.CustomException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * spring boot统一异常处理
 * @author 木可
 * @version 1.0
 * @date 2020/12/19 21:13
 */
@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    /**
     * 全局异常处理
     * @param e Exception
     * @return
     */
    @ExceptionHandler(Exception.class)
    public Result exceptionHandler(Exception e) {
        log.error(e.getMessage());
        e.printStackTrace();
        return Result.error().message(e.getMessage());
    }

    /**
     * 自定义异常
     * @param e CustomException
     * @return
     */
    @ExceptionHandler(CustomException.class)
    public Result customExceptionHandler(CustomException e) {
        log.error(e.getCode() + "====" +e.getMsg());
        e.printStackTrace();
        return Result.error().code(e.getCode()).message(e.getMsg());
    }

    /**
     * 特定异常 ==》算术异常
     * @param e ArithmeticException
     * @return
     */
    @ExceptionHandler(ArithmeticException.class)
    public Result arithmeticExceptionHandler(ArithmeticException e){
        log.error(e.getMessage());
        e.printStackTrace();
        return Result.error().message(e.getMessage());
    }


}
