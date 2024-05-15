package com.sztu.check.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.sztu.check.domain.Application;
import com.sztu.check.dto.req.LogReqDTO;
import com.sztu.check.dto.resp.LogRespDTO;
import com.sztu.check.mapper.ApplicationMapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import javax.annotation.Resource;
import com.sztu.check.mapper.LogMapper;
import com.sztu.check.domain.Log;

import java.util.List;

/**
 * @author lujiawei
 */
@Slf4j
@Service
public class LogService extends ServiceImpl<LogMapper, Log> {

    @Resource
    private LogMapper logMapper;

    public Page<Log> getList(LogReqDTO logReqDTO) {
        Page<Log> page = new Page<>(logReqDTO.getPageNum(), logReqDTO.getPageSize());
        LambdaQueryWrapper<Log> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.orderByDesc(Log::getUpdateTime);
        if (StringUtils.isNotEmpty(logReqDTO.getBusinessType())) {
            queryWrapper.eq(Log::getBusinessType, logReqDTO.getBusinessType());
        }
        return logMapper.selectPage(page, queryWrapper);
    }
}
