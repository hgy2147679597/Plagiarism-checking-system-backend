package com.sztu.check.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.sztu.check.domain.User;
import org.apache.ibatis.annotations.Mapper;
/**
 * @description: userMapper
 * @author: wentianrui
 * @date:  16:21
 **/
@Mapper
public interface UserMapper extends BaseMapper<User> {

}