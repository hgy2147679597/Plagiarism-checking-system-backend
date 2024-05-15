package com.sztu.check.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.sztu.check.domain.Application;
import com.sztu.check.dto.req.ApplicationReqDTO;
import com.sztu.check.dto.resp.ApplicationRespDTO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;


/**
 * @author lujiawei
 * @version 1.0
 * @date 2022/01/04/ 20:30
 */
@Mapper
public interface ApplicationMapper extends BaseMapper<Application> {

    /**
     * 分页查询
     * @param page
     * @param applicationReqDTO
     * @return
     */
    Page<ApplicationRespDTO> selectPageList(Page<ApplicationRespDTO> page, @Param("dto") ApplicationReqDTO applicationReqDTO);
}