package com.sztu.check.utils;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.toolkit.ObjectUtils;
import com.sztu.check.domain.OperatingUser;
import io.jsonwebtoken.*;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.lang3.time.DateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;

import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * @author lujiawei
 * @version 1.0
 * @date 2022/01/04/ 20:30
 */
@Log4j2
public class JwtUtils {

    public static final String BASE64SECRET = "(af31a87200-895b0d89d291b-38d4d9c9b(*";

    //超时毫秒数（默认30分钟）
    public static final int EXPIRESSECOND = 1000 * 60 * 30;

    public static final String DATAKEY = "(af31a87200-895b0d89d291b-38d4d9c9b(*";

    private static final Logger logger = LoggerFactory.getLogger(JwtUtils.class);

    private static String secret = "(af31a87200-895b0d89d291b-38d4d9c9b(*";

    public static String getSecret() {
        return secret;
    }

    public JwtUtils(String secret) {
        JwtUtils.secret = secret;
    }

    public static String createToken(Object id, String username, Map<String, Object> extMap,String role) {
        return createToken(id, username, secret, extMap,role);
    }

    public static String createToken(Object id, String username, String secret, Map<String, Object> extMap,String role) {
        Map<String, Object> map = new HashMap<>(2);
        map.put("alg", "HS256");
        map.put("typ", "JWT");
        Map<String, Object> claimsMap = null != extMap && extMap.size() > 0 ? new HashMap(extMap) : new HashMap(3);
        claimsMap.put("id", id);
        claimsMap.put("username", username);
        claimsMap.put("role", role);
        Date date = DateUtils.addMinutes(new Date(), 30);
        String token = Jwts.builder().setHeader(map).setClaims(claimsMap).setExpiration(date).signWith(SignatureAlgorithm.HS256, secret.getBytes(StandardCharsets.UTF_8)).compact();
        log.info("[JwtUtils.createToken] get token: "+token);
        return token;
    }

    public static Map<String, Object> verifyJwt(String token) throws Exception {
        return verifyJwt(token, secret);
    }

    public static boolean verifyToken(String token)  {
        Map<String, Object> userInfoMap = verifyJwt(token, secret);
        return true;
    }

    public static Map<String, Object> verifyJwt(String token, String secret) {
        Jws<Claims> claimsJws = Jwts.parser().setSigningKey(secret.getBytes()).parseClaimsJws(token);
        checkExpTime(claimsJws);
        Claims claims = claimsJws.getBody();
        Object id = claims.get("id", Object.class);
        log.info("用户或管理员id为:"+id);
        String username = claims.get("username", String.class);
        String role=claims.get("role",String.class);
        Map<String, Object> resultMap = new HashMap<>(8);
        resultMap.put("id", id);
        resultMap.put("username", username);
        resultMap.put("role",role);
        return resultMap;
    }

    private static void checkExpTime(Jws<Claims> claims) {
        Object expSeconds = ((Claims) claims.getBody()).get("exp");
        if (expSeconds == null) {
            throw new RuntimeException("token失效时间为空，请调整");
        } else {
            long expTimes = Long.parseLong(expSeconds.toString()) - System.currentTimeMillis();
            if (expTimes > EXPIRESSECOND) {
                throw new RuntimeException("token失效时间大于30分钟，请调整");
            }
        }
    }






    public static void main(String[] args) throws Exception {
        Integer openId = 2323;
        String name = "Turn  RoUndº";
        String token = createToken(openId, name, null, null);
        System.out.println(token);
    }



}
