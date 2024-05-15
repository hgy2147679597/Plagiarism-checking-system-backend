package com.sztu.check.common;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CommonResult<T> {
    private Integer code;   //200代表成功，400代表失败，403代表token失效，405代表无权限
    private String message; //提示信息
    private T data;    //返回数据

    private static final Integer CODE_SUCCESS = 200;

    private static final Integer CODE_FAIL = 400;

    private static final Integer CODE_UNAUTHORIZED=405;

    private static final String MSG_SUCCESS = "success";

    private static final String MSG_FAIL = "failed";

    private static final String MSG_UNAUTHORIZED="unauthorized";

    public CommonResult(Integer code){
        this.code=code;
    }
    public CommonResult(Integer code, T data){
        this.code=code;
        this.data=data;
    }
    public CommonResult(Integer code, String msg){
        this.code = code;
        this.message = msg;
    }
    public static<T> CommonResult<T> success(){
        return new CommonResult<T>(CODE_SUCCESS,MSG_SUCCESS);
    }

    public static<T> CommonResult<T> success(T data){
        return new CommonResult<T>(CODE_SUCCESS,MSG_SUCCESS,data);
    }

    public static <T>CommonResult<T> fail(){
        return new CommonResult<T>(CODE_FAIL, MSG_FAIL);
    }
    //自定义code、msg、data
    public static <T>CommonResult<T> success(Integer code, String msg, T data){
        return new CommonResult<T>(code, msg, data);
    }
    public static <T>CommonResult<T> fail(Integer code, String msg, T data){
        return new CommonResult<T>(code, msg, data);
    }

    public static <T>CommonResult<T> fail(String msg){
        return new CommonResult<T>(CODE_FAIL, msg);
    }

    public static <T>CommonResult<T> fail(String msg, T data){
        return new CommonResult<T>(CODE_FAIL, msg,data);
    }

    public static <T>CommonResult<T> unauthorized(){
        return new CommonResult<T>(CODE_UNAUTHORIZED,MSG_UNAUTHORIZED);
    }
}

