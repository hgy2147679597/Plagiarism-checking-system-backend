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
public class LogReqDTO extends PageDTO {


    /**
    * 业务类型
    */
    private String businessType;

}