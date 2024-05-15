package com.sztu.check.service;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sztu.check.common.BaseResp;
import com.sztu.check.domain.File;
import com.sztu.check.domain.User;
import com.sztu.check.exception.CustomException;
import com.sztu.check.mapper.FileMapper;
import com.sztu.check.utils.CommonFieldsUtils;
import com.sztu.check.utils.FileUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.multipart.MultipartResolver;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;

import javax.annotation.Resource;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletInputStream;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;
import java.io.*;
import java.text.SimpleDateFormat;
import java.util.*;


/**
 * @author lujiawei
 * @version 1.0
 * @date 2022/01/04/ 20:30
 */
@Slf4j
@Service
public class FileService extends ServiceImpl<FileMapper, File> {

    @Value("${spring.web.resources.static-locations}")
    String filePath;

    static String DELETE_NAME = "d_";

    @Resource
    FileMapper fileMapper;

    /**
     * 保存上传文件
     * @param request
     */
    @Transactional
    public Integer save(String fileName, HttpServletRequest request) throws IOException{
        log.info("开始上传文件：{}", fileName);
        List<String> suffixList = Arrays.asList("docx", "doc", "dotx");
        Date date = new Date();
        SimpleDateFormat format = new SimpleDateFormat("yyyyMMddhhmmssS");
        String suffix = FileUtils.getExtensionName(fileName);
        if (!suffixList.contains(suffix.toLowerCase())) {
            throw new CustomException("文件类型不对，请上传Word文件!");
        }
        String nowStr = format.format(date);
        String path = nowStr + "." + suffix;

        File file = new File();
        file.setFileName(fileName);
        file.setFileUrl(path);
        CommonFieldsUtils.setCrtAndUpdFields(file);
        fileMapper.insert(file);
        java.io.File folder=new java.io.File(filePath);
        if(!folder.exists()){
            boolean mkdirs = folder.mkdirs();
            if(!mkdirs){
                log.error("创建目录失败");
            }
        }
        FileOutputStream fileOutputStream = new FileOutputStream(new java.io.File(filePath + path));
        ServletInputStream inputStream = request.getInputStream();
        inputStream.transferTo(fileOutputStream);
        log.info("文件上传成功:{}", fileName);

        return file.getId();
    }
    /**
     * @description: 根据文件url获取文件
     * @author: wentianrui
     * @date:  16:52
     * @param: [fileUrl]
     * @return: com.sztu.check.domain.File
     **/
    public File getByFileUrl(String fileUrl){
        LambdaQueryWrapper<File> lambdaQueryWrapper=new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(!StringUtils.isEmpty(fileUrl),File::getFileUrl,fileUrl);
        lambdaQueryWrapper.eq(File::getIsDeleted,0);
        return this.getOne(lambdaQueryWrapper);
    }
    /**
     * 先返回文件二进制
     * @param fileId
     * @param request
     * @param response
     */
    public void download(Integer fileId, HttpServletRequest request, HttpServletResponse response) {
        File file = fileMapper.selectById(fileId);
        try {

            FileInputStream inputStream = new FileInputStream(new java.io.File(filePath + file.getFileUrl()));

            response.setContentType("application/octet-stream");
            response.setHeader("Content-Disposition","attachment;filename=" + file.getFileName());
            response.setHeader("Content-Length", String.valueOf(new java.io.File(filePath + file.getFileUrl()).length()));
            ServletOutputStream outputStream = response.getOutputStream();
            byte[] bytes = new byte[1024];
            int length;
            while ((length = inputStream.read(bytes)) > 0) {
                outputStream.write(bytes,0, length);
            }
            outputStream.close();
            inputStream.close();
        } catch (Exception e) {
            throw new CustomException(e.getMessage());
        }
    }

    /**
     * 删除服务器文件
     * @param fileId
     */
    public void deleteFile(Integer fileId) {
        File file = fileMapper.selectById(fileId);
        if (ObjectUtils.isEmpty(file)) {
            throw new CustomException("找不到文件信息!");
        }
        java.io.File rFile = new java.io.File(filePath + file.getFileUrl());
        String newFileUrl = filePath + DELETE_NAME + file.getFileUrl();
        rFile.renameTo(new java.io.File(newFileUrl));

        file.setFileUrl(DELETE_NAME + file.getFileUrl());
        file.setIsDeleted("1");
        CommonFieldsUtils.setUpdFields(file);
        fileMapper.updateById(file);

        log.info("文件：{} 删除成功", file.getFileUrl());
    }
}
