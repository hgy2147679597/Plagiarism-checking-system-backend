package com.sztu.check.dto.req;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @BelongsProject: Plagiarism-checking-system-backend
 * @BelongsPackage: com.sztu.check.dto.req
 * @Author: wentianrui
 * @CreateTime: 2023-09-24  23:43
 * @Description: 为了接受参数
 * @Version: 1.0
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CodeStateDto {
    private String code;
    private String state;

}
