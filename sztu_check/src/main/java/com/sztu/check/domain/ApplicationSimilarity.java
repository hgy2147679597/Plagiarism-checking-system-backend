package com.sztu.check.domain;

import java.io.Serializable;
import java.util.Date;

import com.sztu.check.enums.ApplicationStatusEnum;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

/**
 * Application
 *
 * @Author elysiaEgo
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ApplicationSimilarity implements Serializable {
    private static final long serialVersionUID = -1;
    private Integer id;
    private String title;
    private Integer createId;
    private Date createTime;
    private Integer status;
    private String fileUrl;
    private Integer fileId;
    private String createUser;
    private Double similarity;
}
