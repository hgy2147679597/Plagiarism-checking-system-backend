package com.sztu.check.domain;

import java.io.Serializable;
import java.util.Date;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author lujiawei
 * @version 1.0
 * @date 2022/01/04/ 20:30
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Log implements Serializable {
    private static final long serialVersionUID = -1;
    /**
    * 主键
    */
    @TableId(value = "id", type = IdType.AUTO)
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
    * 创建人id
    */
    private Integer createId;

    /**
    * 创建人名称
    */
    private String createUser;

    /**
    * 创建时间
    */
    private Date createTime;

    /**
    * 更新人id
    */
    private Integer updateId;

    /**
    * 更新人名称
    */
    private String updateUser;

    /**
    * 更新时间
    */
    private Date updateTime;

    /**
    * 状态
    */
    private String status = "0";

    /**
    * 逻辑删除
    */
    @TableField(value = "is_deleted",select = false)
    private String isDeleted = "0";
}