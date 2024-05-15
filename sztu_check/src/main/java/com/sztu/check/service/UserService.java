package com.sztu.check.service;/*
作者:江江江
时间:2023/9/20
*/

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.sztu.check.domain.User;
import com.sztu.check.enums.RoleEnum;
import com.sztu.check.exception.CustomException;
import com.sztu.check.utils.CommonFieldsUtils;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.util.StringUtils;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestTemplate;

/**
 * @description: userService
 * @author: wentianrui
 * @date: 16:22
 **/
@Service
@Log4j2
public class UserService {
    private final static String BASE_URL = "https://auth.sztu.edu.cn/idp/oauth2";
    private final static String CLIENT_ID = "check_system_id";
    private final static String CLIENT_SECRET = "cd1ec07d340c44cda0228a946d762b47";
    private final static String GRANT_TYPE = "authorization_code";
    private final static String ERR_CODE = "errcode";
    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private AdminService adminService;

    public String checkTokenInfo(JSONObject tokenInfo){
        if(tokenInfo.containsKey(ERR_CODE)){
            return tokenInfo.get("msg").toString();
        }
        return null;
    }
    public boolean checkStateAndCode(String code, String state) {
        return StringUtils.isEmpty(code) || StringUtils.isEmpty(state);
    }

    public JSONObject getToken(String code) {
        String path = BASE_URL + "/getToken";
        HttpHeaders httpHeaders = new HttpHeaders();
        MultiValueMap<String, String> param = new LinkedMultiValueMap<>();
        httpHeaders.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        param.add("client_id", CLIENT_ID);
        param.add("grant_type", GRANT_TYPE);
        param.add("code", code);
        param.add("client_secret", CLIENT_SECRET);
        HttpEntity<MultiValueMap<String, String>> requestParams = new HttpEntity<>(param, httpHeaders);
        ResponseEntity<JSONObject> responseEntity;
        try {
            responseEntity = restTemplate.exchange(path, HttpMethod.POST, requestParams, JSONObject.class);
        } catch (ResourceAccessException e) {
            log.error("请求超时", e.getMessage());
            throw new CustomException("请求超时!");
        }
        if (responseEntity.getStatusCode() != HttpStatus.OK) {
            // Handle non-200 HTTP status codes (e.g., error responses)
            log.info("[UserService.getToken] get response fail");
            throw new CustomException("[UserService.getToken] get response fail");
        }
        log.info("[UserService.getToken] get response success ,jsonObject: " + responseEntity.getBody());
        return responseEntity.getBody();
    }

    public JSONObject getUserInfo(String accessToken) {
        log.info("[UserService.getUserInfo] access_token为: " + accessToken);
        String reqPath = BASE_URL + "/getUserInfo?access_token=" + accessToken +
                "&client_id=" + CLIENT_ID;
        JSONObject object = restTemplate.getForObject(reqPath, JSONObject.class);
        log.info("[UserService.getUserInfo] get UserInfo success ,userInfo: " + object);
        return object;
    }

    public User insertUser(JSONObject userInfo) {
        String uid = (String) userInfo.get("uid");
        String username = (String) userInfo.get("displayName");
        User user = new User();
        user.setUsername(uid);
        CommonFieldsUtils.setCrtAndUpdFields(user);
        user.setRealName(username);
        user.setRole(RoleEnum.user);
        user.setStatus(1);
        user.setIsDeleted("0");
        adminService.save(user);
        return user;
    }

    public User findUserOne(Object uid) {
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("username", uid.toString());
        User user = adminService.getOne(queryWrapper);
        log.info("[UserService.findUserOne] get user success ,user: " + user);
        return user;
    }

    public User addUser(JSONObject userInfo) {
        User tempUser = findUserOne(userInfo.get("uid"));
        if (tempUser == null) {
            return insertUser(userInfo);
        }
        return tempUser;
    }

    public boolean checkUserStatus(Object uid) {
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("ssouid", uid);
        User user = adminService.getOne(queryWrapper);
        return user.getStatus() == 1;
    }

    public JSONObject deleteToken(String accessToken) {
        log.info("[UserService.getUserInfo] access_token为: " + accessToken);
        String reqPath = BASE_URL + "/revokeToken?access_token=" + accessToken;
        JSONObject object = restTemplate.getForObject(reqPath, JSONObject.class);
        log.info("[UserService.getUserInfo] revokeToken success ,result: " + object);
        return object;
    }
}
