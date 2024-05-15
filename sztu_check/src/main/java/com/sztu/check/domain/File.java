package com.sztu.check.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
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
public class File implements Serializable {
    private static final long serialVersionUID = -1;
    /**
    * 主键
    */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 申报id
     */
    private Integer applicationId;

    /**
    * 文件路径
    */
    private String fileUrl;

    /**
     * 文件名
     */
    private String fileName;


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
    * 逻辑删除
    */
    @TableField(value = "is_deleted",select = false)
    private String isDeleted = "0";
}