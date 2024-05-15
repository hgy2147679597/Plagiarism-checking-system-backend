package com.sztu.check.domain;

import java.io.Serializable;
import java.util.Date;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.sztu.check.enums.RoleEnum;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @description: Administrator entity class
 * @author: wentianrui
 * @date:  12:00
 **/
@Data
@AllArgsConstructor
@NoArgsConstructor
public class User implements Serializable {
    private static final long serialVersionUID = -1;

    /*
    * 角色
    * */

    private RoleEnum role;

    /*
     * 用户id
     * */

    private String realName;

    /**
     * 主键
     */
    @TableId(value = "id",type = IdType.AUTO)
    private Integer id;

    /**
     * 账号
     */
    private String username;

    /**
     * 密码
     */
    private String password;

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
     * 状态 0禁用 1启用
     */
    private Integer status;

    /**
     * 逻辑删除
     */

    @TableField(value = "is_deleted", select = false)
    @TableLogic(value = "0", delval = "1")
    private String isDeleted;
}