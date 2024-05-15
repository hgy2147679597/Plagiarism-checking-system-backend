package com.sztu.check.annotation;


import com.sztu.check.enums.BusinessTypeEnum;

import java.lang.annotation.*;

/**
 * 自定义操作日志记录注解
 * 
 * @author lujiawei
 */
@Target({ ElementType.PARAMETER, ElementType.METHOD })
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Log
{
    /**
     * 内容
     */
    public String content() default "";

    /**
     * 业务类型
     */
    public BusinessTypeEnum businessType() ;
}
