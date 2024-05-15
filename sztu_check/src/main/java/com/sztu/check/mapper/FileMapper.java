package com.sztu.check.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.sztu.check.domain.Application;
import com.sztu.check.domain.File;
import org.apache.ibatis.annotations.Mapper;

/**
 * @author lujiawei
 * @version 1.0
 * @date 2022/01/04/ 20:30
 */
@Mapper
public interface FileMapper extends BaseMapper<File> {
}
