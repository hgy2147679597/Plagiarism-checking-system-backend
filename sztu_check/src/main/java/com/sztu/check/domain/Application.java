package com.sztu.check.domain;

import java.io.Serializable;
import java.util.Date;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Application
 * @Author elysiaEgo
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Application implements Serializable {
    private static final long serialVersionUID = -1;
    /**
     * 主键
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 标题
     */
    private String title;

    /**
     * 文件id
     */
    private Integer fileId;

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
     * 状态  1=通过，2=不通过，0=审核中
     */
    private Integer status = 0;
    /**
     * 逻辑删除
     */
    @TableField(value = "is_deleted", select = false)
    private String isDeleted="0" ;
}
