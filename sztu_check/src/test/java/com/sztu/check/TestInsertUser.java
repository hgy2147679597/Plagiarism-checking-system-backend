package com.sztu.check;/*
作者:江江江
时间:2023/9/22
*/

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.sztu.check.domain.User;
import com.sztu.check.enums.RoleEnum;
import com.sztu.check.mapper.UserMapper;
import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;

@SpringBootTest
@Log4j2
public class TestInsertUser {
    @Autowired
    private UserMapper userMapper;

    @Autowired
    private RedisTemplate<Object,Object> redisTemplate;
    @Test
    public void test1(){
        User user =new User();
        user.setUsername("uidxxxx");
        userMapper.insert(user);
    }

    @Test
    public void test2(){
        QueryWrapper<User>queryWrapper=new QueryWrapper<>();
        queryWrapper.eq("ssouid","ss");
        User user = userMapper.selectOne(queryWrapper);

    }

    @Test
    public void test3(){
        new My().fun1();
    }



    @Test
    public void test5(){
        User user =new User();
        user.setId(44);
        userMapper.updateById(user);
    }

    @Test
    public void test6(){
        System.out.println(RoleEnum.valueOf("admi1n"));
    }
}

class My{
    @Value("${my.name}")
    private String name;
    public void fun1(){
        fun2();
    }
    public void fun2(){
        System.out.println(name);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}