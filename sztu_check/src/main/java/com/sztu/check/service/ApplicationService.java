package com.sztu.check.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sztu.check.domain.File;
import com.sztu.check.domain.OperatingUser;
import com.sztu.check.dto.req.ApplicationReqDTO;
import com.sztu.check.dto.resp.ApplicationRespDTO;
import com.sztu.check.mapper.FileMapper;
import com.sztu.check.utils.CommonFieldsUtils;
import com.sztu.check.utils.CurrentContext;
import com.sztu.check.utils.FileUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

import com.sztu.check.mapper.ApplicationMapper;
import com.sztu.check.domain.Application;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author lujiawei
 */
@Slf4j
@Service
public class ApplicationService extends ServiceImpl<ApplicationMapper,Application>{

    @Value("${spring.web.resources.static-locations}")
    String filePath;

    static String MODEL_NAME = "m_";

    static final String USER = "user";

    @Resource
    private ApplicationMapper applicationMapper;

    @Autowired
    private FileMapper fileMapper;

    /**
     * 分页查询文件列表
     * @param reqDTO
     * @return
     */
    public Page<ApplicationRespDTO> getList(ApplicationReqDTO reqDTO) {
        //TODO:
        OperatingUser currentOperatingUser = CurrentContext.getCurrentOperatingUser();
        if (currentOperatingUser.getRole().equals(USER)) {
            reqDTO.setCreateId(currentOperatingUser.getId());
        }
        Page<ApplicationRespDTO> page = new Page<>(reqDTO.getPageNum(), reqDTO.getPageSize());
        return applicationMapper.selectPageList(page, reqDTO);
    }

    /**
     *
     * @param id
     * @return
     */
    public Application getByFileId(Integer id){
        LambdaQueryWrapper<Application>lambdaQueryWrapper=new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(id != null, Application::getFileId, id);
        return this.getOne(lambdaQueryWrapper);
    }
    /**
     * 获取申报详情
     * @param id
     * @return
     */
    public ApplicationRespDTO getDetailById(Integer id) {
        LambdaQueryWrapper<Application> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Application::getId, id);
        queryWrapper.eq(Application::getIsDeleted, "0");
        Application application = applicationMapper.selectOne(queryWrapper);
        if (ObjectUtils.isEmpty(application)) {
            return null;
        }
        ApplicationRespDTO applicationRespDTO = new ApplicationRespDTO();
        BeanUtils.copyProperties(application, applicationRespDTO);

        File file = fileMapper.selectById(application.getFileId());
        if (!ObjectUtils.isEmpty(file)) {
            applicationRespDTO.setFileUrl(file.getFileUrl());
            applicationRespDTO.setFileName(file.getFileName());
        }
        return applicationRespDTO;
    }

    /**
     * 创建申报
     * @param application
     */
    @Transactional(rollbackFor = Exception.class)
    public void create(Application application) {
        application.setStatus(0);
        CommonFieldsUtils.setCrtAndUpdFields(application);
        applicationMapper.insert(application);
        File file = fileMapper.selectById(application.getFileId());
        if (ObjectUtils.isEmpty(file)) {
            throw new RuntimeException("文件未上传！");
        }
        // 将与创建申报关联的文件名添加前缀，区分识别
        String fileUrl = file.getFileUrl();
        java.io.File newFile = new java.io.File(filePath + fileUrl);
        String newFileUrl = filePath + MODEL_NAME + fileUrl;
        newFile.renameTo(new java.io.File(newFileUrl));

        file.setFileUrl(MODEL_NAME + fileUrl);
        file.setApplicationId(application.getId());
        CommonFieldsUtils.setUpdFields(file);
        fileMapper.updateById(file);
    }

    public Integer editStatus(Application application) {
        LambdaQueryWrapper<Application>queryWrapper= new LambdaQueryWrapper<>();
        queryWrapper.eq(Application::getId,application.getId());
        return applicationMapper.update(application,queryWrapper);
    }
}
