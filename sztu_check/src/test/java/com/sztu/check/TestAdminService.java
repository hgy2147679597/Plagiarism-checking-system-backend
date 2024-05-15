package com.sztu.check;/*
作者:江江江
时间:2023/9/14
*/

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.sztu.check.config.AdminAdvice;
import com.sztu.check.domain.User;
import com.sztu.check.service.AdminService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class TestAdminService {
    @Autowired
    private AdminService adminService;
    @Autowired
    private AdminAdvice adminAdvice;
    @Test
    public void test_insert(){
        System.out.println(adminService.insertWithUserNamePassword("文天瑞5", "1234"));
    }


    @Test
    public void test_findAllByPage(){

    }
    @Test
    public void test_delete(){
        adminService.removeById(1);
    }

    @Test
    public void test_adminAdvice(){
        adminAdvice.registerAdvice();
    }
    @Test
    public void get_user_deleted(){
        LambdaQueryWrapper<User>lambdaQueryWrapper=new LambdaQueryWrapper<User>();
        User byId = adminService.getById(46);
        System.out.println(byId);
    }
}
