package com.sztu.check.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.sztu.check.domain.File;
import com.sztu.check.domain.Log;
import com.sztu.check.dto.req.LogReqDTO;
import com.sztu.check.dto.resp.LogRespDTO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * @author lujiawei
 * @version 1.0
 * @date 2022/01/04/ 20:30
 */
@Mapper
public interface LogMapper extends BaseMapper<Log> {

    /**
     * 分页查询日志列表
     * @param logReqDTO
     * @return
     */
    List<LogRespDTO> getList(LogReqDTO logReqDTO);
}