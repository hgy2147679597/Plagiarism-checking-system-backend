package com.sztu.check.exception;

/**
 * @BelongsProject: Plagiarism-checking-system-backend
 * @BelongsPackage: com.sztu.check.exception
 * @Author: wentianrui
 * @CreateTime: 2023-09-23  16:51
 * @Description: 自定义异常
 * @Version: 1.0
 */
public class CustomException extends RuntimeException {
    public CustomException(String message) {
        super(message);
    }
}
