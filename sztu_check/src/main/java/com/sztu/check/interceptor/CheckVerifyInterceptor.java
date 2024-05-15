package com.sztu.check.interceptor;

import com.alibaba.fastjson.JSONObject;

import com.baomidou.mybatisplus.core.toolkit.ObjectUtils;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sztu.check.common.BaseResp;
import com.sztu.check.common.JacksonObjectMapper;
import com.sztu.check.domain.User;
import com.sztu.check.domain.OperatingUser;
import com.sztu.check.service.AdminService;
import com.sztu.check.service.JwtService;
import com.sztu.check.utils.CurrentContext;
import com.sztu.check.utils.JwtUtils;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @description: 拦截器
 * @author: wentianrui
 * @date:  11:39
 **/
@Component
@Log4j2
public class CheckVerifyInterceptor implements HandlerInterceptor {
    @Autowired
    private AdminService adminService;
    @Autowired
    private JwtService jwtService;
    private static final String ADMIN="admin";
    private static final String USER="user";
    private static final String APPLICATION="application";


    @Override
    public boolean preHandle(HttpServletRequest request, @NotNull HttpServletResponse response, @NotNull Object o) throws Exception {
        log.info("请求路径为:"+request.getRequestURI());
        String userToken = request.getHeader("Authorization");

        // 兼容Query参数 token
        if (StringUtils.isBlank(userToken)) {
            userToken = request.getParameter("token");
        }
        response.setContentType("application/json;charset=UTF-8");
        JacksonObjectMapper objectMapper = new JacksonObjectMapper();
        if(!jwtService.checkToken(userToken)){
            response.setStatus(HttpServletResponse.SC_FORBIDDEN);
            response.getWriter().write(objectMapper.writeValueAsString(new BaseResp<>(403,"用户未登录",null)));
            return false;
        }
        try {
            verifyUserAndInitContext(userToken, request.getRemoteAddr(), request.getRequestURI());
        }catch (Exception e){
            response.setStatus(HttpServletResponse.SC_FORBIDDEN);
            response.getWriter().write(objectMapper.writeValueAsString(new BaseResp<>(403,e.getMessage(),null)));
            return false;
        }
        String uri=request.getRequestURI();
        OperatingUser currentOperatingUser = CurrentContext.getCurrentOperatingUser();
        if(currentOperatingUser==null){
            response.setStatus(HttpServletResponse.SC_FORBIDDEN);
            response.getWriter().write(objectMapper.writeValueAsString(new BaseResp<>(403,"获取当前操作用户失败",null)));
            return false;
        }
        Integer id = currentOperatingUser.getId();
        User user = adminService.getById(id);
        Integer status = user.getStatus();
        if(status==0){
            response.setStatus(HttpServletResponse.SC_FORBIDDEN);
            response.getWriter().write(objectMapper.writeValueAsString(new BaseResp<>(403,"用户已被禁用",null)));
            return false;
        }
        String role = user.getRole().toString();
        currentOperatingUser.setRole(role);
        CurrentContext.setCurrentOperatingUser(currentOperatingUser);
        if(!ADMIN.equals(role)){
            if(!(uri.contains(USER) || uri.contains(APPLICATION))){
                response.setStatus(HttpServletResponse.SC_FORBIDDEN);
                response.setContentType("application/json;charset=UTF-8");
                response.getWriter().write(objectMapper.writeValueAsString(new BaseResp<>(403,"无权限访问接口",null)));
                return false;
            }
        }
        return true;
    }

    @Override
    public void postHandle(@NotNull HttpServletRequest request, @NotNull HttpServletResponse response, @NotNull Object o, ModelAndView modelAndView) {

    }

    @Override
    public void afterCompletion(@NotNull HttpServletRequest request, @NotNull HttpServletResponse response, @NotNull Object o, Exception e)  {
        CurrentContext.destroy();
    }

    private void verifyUserAndInitContext(String userToken, String remoteAddress,String requestUri) throws Exception {
        if (StringUtils.isBlank(userToken)) {
            throw new RuntimeException("token不可为空");
        }
        OperatingUser currentUser = JSONObject.parseObject(JSONObject.toJSONString(JwtUtils.verifyJwt(userToken)), OperatingUser.class);

        if (ObjectUtils.isNull(currentUser)) {
            throw new RuntimeException("登录已过期,请重新登录!!");
        }
        CurrentContext.setCurrentOperatingUser(createOperatingUserByCurrentUser(currentUser, remoteAddress));
    }


    public OperatingUser createOperatingUserByCurrentUser(OperatingUser currentSysUser, String remoteAddress) {
        OperatingUser operatingUser = new OperatingUser();
        BeanUtils.copyProperties(currentSysUser, operatingUser);
        operatingUser.setHost(remoteAddress);

        return operatingUser;
    }

}
