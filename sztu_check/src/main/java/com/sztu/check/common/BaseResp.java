package com.sztu.check.common;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * BaseResp
 * @param <T> data
 * @author elysiaEgo
 */
@Data
@AllArgsConstructor
public class BaseResp<T> {
    private Integer code = 200;
    private String message = "success";
    private T data;

    public BaseResp(T data) {
        this.data = data;
    }
}
