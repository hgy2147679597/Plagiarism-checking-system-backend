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
public class ApplicationRespDTO implements Serializable {
    private static final long serialVersionUID = -1;
    /**
    * 主键
    */
    private Integer id;

    /**
     * 文件url
     */
    private String fileUrl;

    /**
     * 文件url
     */
    private String fileName;

    /**
     * 文件id
     */
    private Integer fileId;

    /**
    * 标题
    */
    private String title;

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

}