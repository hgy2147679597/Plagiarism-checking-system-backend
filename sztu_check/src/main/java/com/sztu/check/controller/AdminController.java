package com.sztu.check.controller;/*
作者:江江江
时间:2023/9/13
*/

import com.alibaba.fastjson.JSONObject;
import com.fasterxml.jackson.databind.JsonNode;
import com.sztu.check.annotation.Log;
import com.sztu.check.common.BasePage;
import com.sztu.check.common.BaseResp;
import com.sztu.check.domain.User;
import com.sztu.check.enums.BusinessTypeEnum;
import com.sztu.check.service.JwtService;
import com.sztu.check.utils.CommonFieldsUtils;
import com.sztu.check.utils.JwtUtils;
import com.sztu.check.service.AdminService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;


/**
 * @description: AdminController
 * @author: wentianrui
 * @date: 21:54
 **/
@RestController
@Slf4j
public class AdminController {


    @Autowired
    private AdminService adminService;

    @Autowired
    private JwtService jwtService;

    /**
     * @description: login router
     * @author: wentianrui
     * @date: 21:56
     * @param: [admin]
     * @return: com.sztu.check.common.BaseResp<java.lang.Object>
     **/
    @Log(content = "管理员登录", businessType = BusinessTypeEnum.ADMIN)
    @PostMapping("/admin/login")
    public ResponseEntity<BaseResp<Object>> login(@RequestBody User user) {
        //检查用户名密码是否null或空串
        if (adminService.checkIsNull(user.getUsername(), user.getPassword())) {
            return new ResponseEntity<>(new BaseResp<>(403, "账户密码为空", null), HttpStatus.FORBIDDEN);
        }
        //从redis查询管理员集合
        List<User> userList = adminService.getUserListByRedis();
        //检查用户名密码正确性
        User userTemp;
        if ((userTemp = adminService.checkNameAndPwd(user.getUsername(), user.getPassword(), userList)) == null) {
            return new ResponseEntity<>(new BaseResp<>(403, "账户密码不正确", null),HttpStatus.FORBIDDEN);
        }
        if (userTemp.getStatus() == 0) {
            return new ResponseEntity<>(new BaseResp<>(403, "账号已被禁止", null),HttpStatus.FORBIDDEN);
        }
        JSONObject jsonObject = new JSONObject();
        String token = JwtUtils.createToken(userTemp.getId(), userTemp.getUsername(), null, userTemp.getRole().toString());
        jsonObject.put("token", token);
        jsonObject.put("admin", userTemp);
        jwtService.storageToken(token);
        return new ResponseEntity<>(new BaseResp<>(200, "登录成功", jsonObject),HttpStatus.OK);
    }


    /**
     * @description: register router
     * @author: wentianrui
     * @date: 21:57
     * @param: [username, password]
     * @return: com.sztu.check.common.BaseResp<java.lang.Object>
     **/
    @Log(content = "管理员注册", businessType = BusinessTypeEnum.ADMIN)
    @PostMapping("/admin/add")
    public ResponseEntity<BaseResp<Object>> register(@RequestBody User user) {
        JSONObject jsonObject = new JSONObject();
        String username = user.getUsername();
        String password = user.getPassword();
        boolean isNull = adminService.checkIsNull(username, password);
        if(isNull){
            return new ResponseEntity<>(new BaseResp<>(400,"参数不正确",null),HttpStatus.BAD_REQUEST);
        }
        if (adminService.checkAdminsExist(username)) {
            return new ResponseEntity<>(new BaseResp<>(400, "账户名已经存在", null),HttpStatus.BAD_REQUEST);
        }
        int id = adminService.insertWithUserNamePassword(username, password);
        jsonObject.put("id", id);
        if (id > 0) {
            return new ResponseEntity<>(new BaseResp<>(jsonObject),HttpStatus.OK);
        }
        return new ResponseEntity<>(new BaseResp<>(400, "注册失败", null),HttpStatus.BAD_REQUEST);
    }

    /**
     * @description:
     * @author: wentianrui
     * @date: 16:24
     * @param: [page, pageSize, user]
     * @return: com.sztu.check.common.BaseResp<com.sztu.check.common.BasePage < com.sztu.check.domain.User>>
     **/
    @Log(content = "查询管理员列表", businessType = BusinessTypeEnum.ADMIN)
    @GetMapping("/admin/list")
    public ResponseEntity<BaseResp<Object>> userList(
            @RequestParam("page") Integer page,
            @RequestParam("pageSize") Integer pageSize,
            User user
    ) {
        if (page == null || page < 0) {
            // 参数page无效，可以根据需要处理错误
            return new ResponseEntity<>(new BaseResp<>(400,"page参数不正确",null), HttpStatus.BAD_REQUEST);
        }
        if (pageSize == null || pageSize < 0) {
            // 参数pageSize无效，可以根据需要处理错误
            return new ResponseEntity<>(new BaseResp<>("pageSize参数不正确"), HttpStatus.BAD_REQUEST);
        }
        BasePage<User> basePage = new BasePage<>(adminService.findAllByPage(page, pageSize, user));
        return new ResponseEntity<>(new BaseResp<>(basePage),HttpStatus.OK);
    }

    /**
     * @description:
     * @author: wentianrui
     * @date: 16:24
     * @param: [id]
     * @return: com.sztu.check.common.BaseResp<java.lang.Object>
     **/
    @Log(content = "删除管理员", businessType = BusinessTypeEnum.ADMIN)
    @DeleteMapping("/admin/delete/{id}")
    public ResponseEntity<BaseResp<Object>> removeAdminById(@PathVariable("id") Integer id) {
        boolean b = adminService.removeById(id);
        JSONObject jsonObject=new JSONObject();
        jsonObject.put("id",id);
        if (!b) {
            return new ResponseEntity<>(new BaseResp<>(400, "删除失败", null),HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(new BaseResp<>(jsonObject),HttpStatus.OK);
    }

    /**
     * @description: 修改管理员密码
     * @author: wentianrui
     * @date: 22:46
     * @param: [admin]
     * @return: com.sztu.check.common.BaseResp<java.lang.Object>
     **/
    @Log(content = "编辑管理员", businessType = BusinessTypeEnum.ADMIN)
    @PostMapping("/admin/edit")
    public ResponseEntity<BaseResp<Object>> edit(@RequestBody User user) {
        Integer id = user.getId();
        if(id==null){
            return new ResponseEntity<>(new BaseResp<>("id参数为空"),HttpStatus.BAD_REQUEST);
        }
        CommonFieldsUtils.setUpdFields(user);
        boolean b = adminService.updateById(user);
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("id", id);
        if (!b) {
            return new ResponseEntity<>(new BaseResp<>(400, "修改用户信息失败,该用户可能已被删除", jsonObject),HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(new BaseResp<>(jsonObject),HttpStatus.OK);
    }

    /**
     * @description: logout
     * @author: wentianrui
     * @date: 22:57
     * @param: [token]
     * @return: com.sztu.check.common.BaseResp<java.lang.Object>
     **/
    @Log(content = "管理员登出", businessType = BusinessTypeEnum.ADMIN)
    @PostMapping("/admin/logout")
    public ResponseEntity<BaseResp<Object>> logout(String token,HttpServletRequest request) {
        String jwtToken = request.getHeader("Authorization");
        jwtService.deleteToken(jwtToken);
        return new ResponseEntity<>(new BaseResp<>(null),HttpStatus.OK);
    }
}