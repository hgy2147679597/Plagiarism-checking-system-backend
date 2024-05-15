package com.sztu.check.dto.req;

import com.sztu.check.dto.PageDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;


/**
 * @author lujiawei
 * @version 1.0
 * @date 2022/01/04/ 20:30
 */
@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ApplicationReqDTO extends PageDTO {
    /**
    * 主键
    */
    private Integer id;

    /**
     * 文件id
     */
    private Integer fileId;

    /**
    * 标题
    */
    private String title;

    /**
    * 状态
    */
    private Integer status;

    /**
     * 创建人id
     */
    private Integer createId;

    /**
     * 创建人
     */
    private String createUser;

}