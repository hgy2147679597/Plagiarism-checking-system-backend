package com.sztu.check.controller;/*
作者:江江江
时间:2023/9/15
*/

import cn.hutool.core.lang.Pair;
import cn.hutool.jwt.JWTUtil;
import com.alibaba.fastjson.JSONObject;
import com.sztu.check.annotation.Log;
import com.sztu.check.common.BaseResp;
import com.sztu.check.domain.User;
import com.sztu.check.dto.req.CodeStateDto;
import com.sztu.check.enums.BusinessTypeEnum;
import com.sztu.check.service.JwtService;
import com.sztu.check.service.UserService;
import com.sztu.check.utils.JwtUtils;
import lombok.extern.log4j.Log4j;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

/**
 * @description:
 * @author: wentianrui
 * @date: 16:26
 * @param:
 * @return:
 **/
@RestController
@Log4j2
public class UserController {
    @Autowired
    private UserService userService;
    @Autowired
    private JwtService jwtService;
    /**
     * @description:
     * @author: wentianrui
     * @date: 16:27
     * @param: [code, state]
     * @return: com.sztu.check.common.BaseResp<java.lang.Object>
     **/
    @Log(content = "用户登录", businessType = BusinessTypeEnum.USER)
    @PostMapping("/user/login")
    public ResponseEntity<BaseResp<Object>> login(@RequestBody CodeStateDto dto) {
        String code = dto.getCode();
        String state = dto.getState();
        if (userService.checkStateAndCode(code, state)) {
            return new ResponseEntity<>(new BaseResp<>(403, "统一认证登录失败,state和code不正确", null),HttpStatus.FORBIDDEN);
        }
        JSONObject tokenInfo = userService.getToken(code);
        String s = userService.checkTokenInfo(tokenInfo);
        if (s != null) {
            return new ResponseEntity<>(new BaseResp<>(403, s, null),HttpStatus.FORBIDDEN);
        }
        String accessToken = (String) tokenInfo.get("access_token");
        String refreshToken = (String) tokenInfo.get("refresh_token");
        JSONObject userInfo = userService.getUserInfo(accessToken);
        String s1 = userService.checkTokenInfo(userInfo);
        if (s1 != null) {
            return new ResponseEntity<>(new BaseResp<>(403, s1, null),HttpStatus.FORBIDDEN);
        }
        JSONObject deleteTokenResult = userService.deleteToken(accessToken);
        String s2 = userService.checkTokenInfo(deleteTokenResult);
        if(s2!=null){
            return new ResponseEntity<>(new BaseResp<>(403,s2,null),HttpStatus.FORBIDDEN);
        }
        User user = userService.addUser(userInfo);
        String token = JwtUtils.createToken(user.getId(), user.getUsername(), null, user.getRole().toString());
        JSONObject jsonObject=new JSONObject();
        jsonObject.put("token",token);
        jsonObject.put("username",user.getRealName());
        jsonObject.put("id",user.getId());
        jwtService.storageToken(token);
        return new ResponseEntity<>(new BaseResp<>(jsonObject),HttpStatus.OK);
    }

    /**
     * @description:
     * @author: wentianrui
     * @date: 16:27
     * @param: [token]
     * @return: com.sztu.check.common.BaseResp<java.lang.Object>
     **/
    @Log(content = "用户登出", businessType = BusinessTypeEnum.USER)
    @PostMapping("/user/logout")
    public ResponseEntity<BaseResp<Object>> logout(HttpServletRequest request) {
        String authorization = request.getHeader("Authorization");
        boolean b = jwtService.deleteToken(authorization);
        if(!b){
            return new ResponseEntity<>(new BaseResp<>(404,"记录不存在",null),HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(new BaseResp<>(null),HttpStatus.OK);
    }
}
