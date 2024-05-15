package com.sztu.check.common;

import com.sztu.check.annotation.Log;
import com.sztu.check.enums.BusinessTypeEnum;
import com.sztu.check.exception.CustomException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import java.sql.SQLIntegrityConstraintViolationException;
/**
 * @BelongsProject: Plagiarism-checking-system-backend
 * @BelongsPackage: com.sztu.check.common
 * @Author: wentianrui
 * @CreateTime: 2023-09-23  16:50
 * @Description: Global Exception Handling
 * @Version: 1.0
 */
@ControllerAdvice()
@ResponseBody
@Slf4j
public class GlobalExceptionHandler {


    @ExceptionHandler(SQLIntegrityConstraintViolationException.class)
    public ResponseEntity<BaseResp<String>> exceptionHandler(SQLIntegrityConstraintViolationException ex) {
        log.error("[GlobalExceptionHandle.exceptionHandler] get exception :",ex);
        return new ResponseEntity<>(new BaseResp<>(500,"数据库相关错误",null), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Log(content = "自定义异常", businessType = BusinessTypeEnum.EXCEPTION)
    @ExceptionHandler(CustomException.class)
    public ResponseEntity<BaseResp<String>> exceptionHandler(CustomException ex) {
        log.error("[GlobalExceptionHandle.exceptionHandler] get exception :",ex);
        return new ResponseEntity<>(new BaseResp<>(500,ex.getMessage(),null),HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Log(content = "全局异常", businessType = BusinessTypeEnum.EXCEPTION)
    @ExceptionHandler(Exception.class)
    public ResponseEntity<BaseResp<String>> exceptionHandler(Exception ex) {
        log.error("[GlobalExceptionHandle.exceptionHandler] get exception :",ex);
        return new ResponseEntity<>(new BaseResp<>(400,ex.getMessage(),null),HttpStatus.INTERNAL_SERVER_ERROR);
    }

}
