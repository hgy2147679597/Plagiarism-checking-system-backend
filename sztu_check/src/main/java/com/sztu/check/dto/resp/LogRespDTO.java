package com.sztu.check.dto.resp;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;

/**
 * @author lujiawei
 * @version 1.0
 * @date 2022/01/04/ 20:30
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class LogRespDTO implements Serializable {
    private static final long serialVersionUID = -1;
    /**
    * 主键
    */
    private Integer id;

    /**
    * 内容
    */
    private String content;

    /**
    * 请求地址
    */
    private String requestUrl;

    /**
    * 请求方法
    */
    private String requestMethod;

    /**
    * 请求参数
    */
    private String requestParams;

    /**
     * 响应参数
     */
    private String response;

    /**
     * 业务类型
     */
    private String businessType;


    /**
    * 创建人名称
    */
    private String createUser;

    /**
    * 创建时间
    */
    private Date createTime;

}