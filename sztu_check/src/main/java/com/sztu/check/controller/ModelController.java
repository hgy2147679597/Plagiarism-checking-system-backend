package com.sztu.check.controller;

import com.sztu.check.annotation.Log;
import com.sztu.check.common.BaseResp;
import com.sztu.check.enums.BusinessTypeEnum;
import com.sztu.check.service.ModelService;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @BelongsProject: Plagiarism-checking-system-backend
 * @BelongsPackage: com.sztu.check.controller
 * @Author: wentianrui
 * @CreateTime: 2023-11-12  20:38
 * @Description: 实现模型训练接口
 * @Version: 1.0
 */
@RestController
@RequestMapping("/model")
@Log4j2
public class ModelController {
    @Autowired
    private ModelService modelService;
    @Log(content = "模型训练", businessType = BusinessTypeEnum.MODEL)
    @PostMapping("/train")
    public ResponseEntity<BaseResp<Object>>train(){
        boolean train = modelService.train(40, 0, 40);
        if(train) {
            return new ResponseEntity<>(new BaseResp<>(200), HttpStatus.OK);
        }
        return new ResponseEntity<>(new BaseResp<>(500,"模型训练失败",null), HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
