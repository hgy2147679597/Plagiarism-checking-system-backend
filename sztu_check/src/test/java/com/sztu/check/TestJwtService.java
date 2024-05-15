package com.sztu.check;

import com.sztu.check.service.JwtService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * @BelongsProject: Plagiarism-checking-system-backend
 * @BelongsPackage: com.sztu.check
 * @Author: wentianrui
 * @CreateTime: 2023-09-23  16:34
 * @Description: TestJwtService
 * @Version: 1.0
 */
@SpringBootTest
public class TestJwtService {
    @Autowired
    private JwtService jwtService;
    @Test
    public void test_storge(){
        jwtService.storageToken("1");
    }
    @Test
    public void test_delete(){
        jwtService.deleteToken("1");
    }
    @Test
    public void test_check(){
        System.out.println(jwtService.checkToken("1"));
    }
}
