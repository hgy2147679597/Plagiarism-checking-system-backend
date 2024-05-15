package com.sztu.check;

import com.alibaba.fastjson.JSONObject;
import com.sztu.check.service.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * @BelongsProject: Plagiarism-checking-system-backend
 * @BelongsPackage: com.sztu.check
 * @Author: wentianrui
 * @CreateTime: 2023-09-24  21:51
 * @Description: test userService
 * @Version: 1.0
 */
@SpringBootTest
public class TestUserService {
    @Autowired
    private UserService userService;
    @Test
    public void test_getToken(){
        JSONObject token = userService.getToken("84c3dfbd04ac914e75ad3c04b606489e");
        System.out.println(token);

    }
}
