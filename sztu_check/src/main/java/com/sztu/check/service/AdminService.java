package com.sztu.check.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sztu.check.domain.User;
import com.sztu.check.enums.RoleEnum;
import com.sztu.check.mapper.UserMapper;
import com.sztu.check.utils.CommonFieldsUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.util.List;
/**
 * @description: adminService
 * @author: wentianrui
 * @date:  16:15
 **/
@Service
public class AdminService extends ServiceImpl<UserMapper, User> {
    @Resource
    private UserMapper userMapper;
    @Autowired
    private RedisTemplate<String,String> redisTemplate;
    private final static String USER_LIST_NAME ="userList";
    public int insertWithUserNamePassword(String username,String password) {
        User user = new User();
        user.setUsername(username);
        user.setPassword(password);
        user.setRole(RoleEnum.admin);
        user.setStatus(1);
        user.setIsDeleted("0");
        CommonFieldsUtils.setCrtAndUpdFields(user);
        this.save(user);
        return user.getId();
    }


    public void getAdminListAndSave(){
        LambdaQueryWrapper<User>lambdaQueryWrapper=new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(User::getRole,"admin");
        List<User> adminList = this.list(lambdaQueryWrapper);
        String adminListString = JSON.toJSONString(adminList);
        redisTemplate.opsForValue().set(USER_LIST_NAME,adminListString);
    }
    public boolean checkAdminsExist(String username) {
        QueryWrapper<User>queryWrapper=new QueryWrapper<>();
        queryWrapper.eq("username",username);
        User user = userMapper.selectOne(queryWrapper);
        return user !=null;
    }
    /**
     * @description: 管理员分页查询
     * @author: wentianrui
     * @date:  10:33
     * @param: [pageOffset, pageSize]
     * @return: com.baomidou.mybatisplus.extension.plugins.pagination.Page<com.sztu.check.domain.Application>
     **/
    public Page<User> findAllByPage(Integer pageOffset, Integer pageSize, User user) {
        Page<User> page = new Page<>(pageOffset, pageSize);
        LambdaQueryWrapper<User> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.select(
                User::getId,
                User::getUsername,
                User::getPassword
        );
        lambdaQueryWrapper.like(!StringUtils.isEmpty(user.getUsername()), User::getUsername, user.getUsername());
        lambdaQueryWrapper.eq(!StringUtils.isEmpty(user.getRole()), User::getRole, user.getRole());
        lambdaQueryWrapper.orderByAsc(User::getId);
        return userMapper.selectPage(page, lambdaQueryWrapper);
    }
    /**
     * @description: 检查用户名密码是否为空
     * @author: wentianrui
     * @date:  23:02
     * @param: [username, password]
     * @return: boolean
     **/
    public boolean checkIsNull(String username,String password) {
        return username == null || password == null || "".equals(username) || "".equals(password);
    }
    //
    /**
     * @description: 检查用户名密码是否正确
     * @author: wentianrui
     * @date:  23:00
     * @param: [username, password, adminList]
     * @return: com.sztu.check.domain.Admin
     **/
    public User checkNameAndPwd(String username, String password, List<User> userList) {
        for (User e: userList) {
            if (e.getUsername().equals(username) && e.getPassword().equals(password)) {
                return e;
            }
        }
        return null;
    }


    /**
     * @description: Get admin collection from redis
     * @author: wentianrui
     * @date:  23:00
     * @param: []
     * @return: java.util.List<com.sztu.check.domain.Admin>
     **/
    public List<User> getUserListByRedis() {
        String adminListString = redisTemplate.opsForValue().get("userList");
        return JSONArray.parseArray(adminListString, User.class);
    }



}
