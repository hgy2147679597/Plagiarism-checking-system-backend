package com.sztu.check.service;

import com.alibaba.fastjson.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

/**
 * @BelongsProject: Plagiarism-checking-system-backend
 * @BelongsPackage: com.sztu.check.service
 * @Author: wentianrui
 * @CreateTime: 2023-09-23  16:11
 * @Description: jwtService
 * @Version: 1.0
 */
@Service
public class JwtService {
    @Autowired
    private RedisTemplate<String,String> redisTemplate;
    /**
     * @description: 存储token
     * @author: wentianrui
     * @date:  10:39
     * @param: [jsonObject]
     * @return: void
     **/
    public void storageToken(String token) {
        String key = "authorization:" + token;
        redisTemplate.opsForValue().set(key, token, 30, TimeUnit.MINUTES);
    }
    /**
     * @description: Deleting a token in redis
     * @author: wentianrui
     * @date:  22:59
     * @param: [token]
     * @return: void
     **/
    public boolean deleteToken(String token) {
        String key = "authorization:" + token;
        return Boolean.TRUE.equals(redisTemplate.delete(key));
    }
    public Boolean checkToken(String token) {
        String key = "authorization:" + token;
        return redisTemplate.hasKey(key);
    }
}
