package com.sztu.check;/*
作者:江江江
时间:2023/9/12
*/

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableAsync
@ServletComponentScan
@MapperScan(basePackages = "com.sztu.check")
public class SztuCheckingApplication {
    public static void main(String[] args) {
        SpringApplication.run(SztuCheckingApplication.class,args);
    }
}
