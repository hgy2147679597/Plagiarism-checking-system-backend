package com.sztu.check.controller;

import com.sztu.check.common.BasePage;
import com.sztu.check.common.BaseResp;
import com.sztu.check.domain.Log;
import com.sztu.check.dto.req.LogReqDTO;
import com.sztu.check.service.LogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


/**
 * 日志管理
 * @author lujiawei
 */
@RestController
@RequestMapping("/log")
public class LogController {

    @Autowired
    LogService logService;

    @GetMapping("/list")
    public BaseResp<BasePage<Log>> getList(LogReqDTO logReqDTO){
        BasePage<Log> page = new BasePage<>(logService.getList(logReqDTO));
        return new BaseResp<>(page);
    }

}
