package com.sztu.check.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 当前线程用户
 * @author lujiawei
 * @version 1.0
 * @date 2022/01/04/ 20:06
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class OperatingUser {

    private Integer id;
    private String username;
    private String host;
    private String phone;
    private String role;
}
