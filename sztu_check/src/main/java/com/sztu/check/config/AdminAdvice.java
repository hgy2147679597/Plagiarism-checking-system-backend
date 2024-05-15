package com.sztu.check.config;/*
作者:江江江
时间:2023/9/14
*/

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.sztu.check.domain.User;
import com.sztu.check.service.AdminService;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.List;
/**
 * @description: AdminAdvice
 * @author: wentianrui
 * @date:  15:50
 * @param:
 * @return:
 **/
@Aspect
@Component
public class AdminAdvice {
    @Autowired
    private AdminService adminService;

    /**
     * @description: Refresh redis when registering
     * @author: wentianrui
     * @date:  15:06
     * @param: []
     * @return: void
     **/
    @Pointcut("execution (* com.sztu.check.controller.AdminController.register(..))")
    public void register() {}
    @Pointcut("execution(* com.sztu.check.controller.AdminController.edit(..))")
    public void edit(){}
    @Pointcut("execution(* com.sztu.check.controller.AdminController.removeAdminById(..))")
    public void removeAdminById(){}
    @After("register()")
    public void registerAdvice() {
      adminService.getAdminListAndSave();
    }
    @After("edit()")
    public void editAdvice(){
       adminService.getAdminListAndSave();
    }
    @After("removeAdminById()")
    public void removeAdminByIdAdvice(){
       adminService.getAdminListAndSave();
    }

}
