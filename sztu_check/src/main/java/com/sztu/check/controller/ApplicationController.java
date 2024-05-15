package com.sztu.check.controller;

import cn.hutool.core.lang.Pair;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.sztu.check.annotation.Log;
import com.sztu.check.common.BasePage;
import com.sztu.check.common.BaseResp;
import com.sztu.check.domain.Application;
import com.sztu.check.domain.ApplicationSimilarity;
import com.sztu.check.domain.File;
import com.sztu.check.dto.req.ApplicationReqDTO;
import com.sztu.check.dto.resp.ApplicationRespDTO;
import com.sztu.check.enums.ApplicationStatusEnum;
import com.sztu.check.enums.BusinessTypeEnum;
import com.sztu.check.service.ApplicationService;
import com.sztu.check.service.FileService;
import com.sztu.check.service.ModelService;
import com.sztu.check.utils.CommonFieldsUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 文件管理
 *
 * @author lujiawei
 */
@RestController
@RequestMapping("/application")
public class ApplicationController {
    @Value("${spring.web.resources.static-locations}")
    String filePath;
    @Autowired
    ApplicationService applicationService;

    @Autowired
    FileService fileService;

    @Autowired
    private ModelService modelService;

    @Log(content = "获取申报信息列表", businessType = BusinessTypeEnum.FILE)
    @GetMapping("/list")
    public BaseResp<BasePage<ApplicationRespDTO>> getList(ApplicationReqDTO reqDTO) {
        BasePage<ApplicationRespDTO> page = new BasePage<>(applicationService.getList(reqDTO));
        return new BaseResp<>(page);
    }

    @Log(content = "新增申报信息", businessType = BusinessTypeEnum.FILE)
    @PostMapping("")
    public BaseResp<String> create(@RequestBody Application application) {
        applicationService.create(application);
        return new BaseResp<>("");
    }

    @Log(content = "更新申报信息", businessType = BusinessTypeEnum.FILE)
    @PostMapping("/{id}")
    public BaseResp<String> updateById(@PathVariable("id") Integer id,
                                       @RequestBody Application aplication) {
        aplication.setId(id);
        CommonFieldsUtils.setUpdFields(aplication);
        applicationService.updateById(aplication);
        return new BaseResp<>("");
    }

    @Log(content = "删除申报信息", businessType = BusinessTypeEnum.FILE)
    @DeleteMapping("/{id}")
    public BaseResp<String> deleteById(@PathVariable("id") Integer id) {
        Application application = applicationService.getById(id);
        application.setIsDeleted("1");

        fileService.deleteFile(application.getFileId());
        CommonFieldsUtils.setUpdFields(application);
        applicationService.updateById(application);

        return new BaseResp<>("");
    }

    @Log(content = "获取申报信息", businessType = BusinessTypeEnum.FILE)
    @GetMapping("/{id}")
    public ResponseEntity<BaseResp<Object>> getById(@PathVariable("id") Integer id) {
        ApplicationRespDTO applicationRespDTO = applicationService.getDetailById(id);
        Application applicationOrigin=new Application();
        applicationOrigin.setId(applicationRespDTO.getId());
        CommonFieldsUtils.setUpdFields(applicationOrigin);
        applicationOrigin.setStatus(1);
        applicationService.updateById(applicationOrigin);
        String testFileUrl = applicationRespDTO.getFileUrl();
        List<Pair<String, Double>> similarity = modelService.start(testFileUrl, 40, 0, 40);
        if(similarity==null){
            applicationOrigin.setStatus(3);
            applicationService.updateById(applicationOrigin);
            return new ResponseEntity<>(new BaseResp<>(400,"相似度为空",null), HttpStatus.BAD_REQUEST);
        }
        List<Pair<String, Double>> topFiveSimilarity = similarity.subList(0, Math.min(10, similarity.size()));
        List<ApplicationSimilarity> list = topFiveSimilarity.stream()
                .filter(item -> {
                    String fileUrl = item.getKey();
                    return !(testFileUrl != null && testFileUrl.equals(fileUrl));
                })
                .map(item -> {
                    ApplicationSimilarity applicationSimilarity = new ApplicationSimilarity();
                    File file = fileService.getByFileUrl(item.getKey());
                    Application application = applicationService.getByFileId(file.getId());
                    BeanUtils.copyProperties(application, applicationSimilarity);
                    applicationSimilarity.setSimilarity(item.getValue());
                    applicationSimilarity.setFileUrl(file.getFileUrl());
                    return applicationSimilarity;
                })
                .collect(Collectors.toList());
        List<ApplicationSimilarity> applicationSimilarities = list.subList(0, Math.min(5, similarity.size()));
        JSONObject jsonObject = new JSONObject();
        applicationOrigin.setStatus(3);
        applicationService.updateById(applicationOrigin);
        jsonObject.put("list", applicationSimilarities);
        jsonObject.put("current", applicationRespDTO);
        return new ResponseEntity<>(new BaseResp<>(jsonObject), HttpStatus.OK);
    }
    @Log(content = "审核文件", businessType = BusinessTypeEnum.FILE)
    @PutMapping("/editStatus")
    public BaseResp<Object> editStatus(@RequestBody Application application){
        Integer status = application.getStatus();
        if(status!= ApplicationStatusEnum.passed.getStatus() && status!=ApplicationStatusEnum.ejected.getStatus()){
            return new BaseResp<>(400,"参数出错",null);
        }
        CommonFieldsUtils.setUpdFields(application);
        Integer id=applicationService.editStatus(application);
        JSONObject jsonObject=new JSONObject();
        jsonObject.put("id",id);
        return new BaseResp<>(jsonObject);
    }
    @Log(content = "获取申报文件下载", businessType = BusinessTypeEnum.FILE)
    @GetMapping("/download/{id}/{fileName:.*}")
    public void download(@PathVariable("id") Integer id,
                         HttpServletRequest request,
                         HttpServletResponse response) {
        Application application = applicationService.getById(id);
        fileService.download(application.getFileId(), request, response);
    }

    @Log(content = "上传文件信息", businessType = BusinessTypeEnum.FILE)
    @PutMapping("/upload/{fileName:.*}")
    public BaseResp<Integer> upload(@PathVariable("fileName") String fileName,
                                    HttpServletRequest request) throws IOException {
        return new BaseResp<>(fileService.save(fileName, request));
    }
}
