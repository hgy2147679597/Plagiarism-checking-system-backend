package com.sztu.check;

import cn.hutool.core.lang.Pair;
import com.sztu.check.service.ModelService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

/**
 * @BelongsProject: Plagiarism-checking-system-backend
 * @BelongsPackage: com.sztu.sztu_check
 * @Author: wentianrui
 * @CreateTime: 2023-09-19  21:56
 * @Description: test start and train
 * @Version: 1.0
 */
@SpringBootTest
public class TestModelService {
    @Autowired
    private ModelService modelService;
    @Test
    public void test_start() throws Exception {
        String testFileUrl = "E:\\查重系统\\Plagiarism-checking-system-backend\\articles\\1997～2002年中文体育核心期刊中的比较学校体育文献综述.docx";
        List<Pair<String,Double>> result = modelService.start(testFileUrl, 40, 0, 40);
    }
    @Test
    public void test_train(){
        modelService.train(40,0,40);
    }
}
