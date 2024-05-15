package com.sztu.check;

import com.sztu.check.domain.Application;
import com.sztu.check.service.ApplicationService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * @BelongsProject: Plagiarism-checking-system-backend
 * @BelongsPackage: com.sztu.check
 * @Author: wentianrui
 * @CreateTime: 2023-11-03  19:59
 * @Version: 1.0
 */
@SpringBootTest
public class ApplicationServiceTest {
    @Autowired
    private ApplicationService applicationService;
    @Test
    void get_by_file_id_is_logic_delete(){
        Application byFileId = applicationService.getByFileId(126);
        if(byFileId==null){
            System.out.println("logic delete");
        }else
        {
            System.out.println(byFileId);
        }
    }
}
