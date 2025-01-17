package com.sztu.check.aspectj;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.support.spring.PropertyPreFilters;
import com.sztu.check.annotation.Log;
import com.sztu.check.domain.OperatingUser;
import com.sztu.check.utils.CommonFieldsUtils;
import com.sztu.check.utils.CurrentContext;
import com.sztu.check.utils.ServletUtils;
import com.sztu.check.service.LogService;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Collection;
import java.util.Map;

/**
 * 操作日志记录处理
 * 
 * @author lujiawei
 */
@Aspect
@Component
public class LogAspect
{
    private static final Logger log = LoggerFactory.getLogger(LogAspect.class);

    /** 排除敏感属性字段 */
    public static final String[] EXCLUDE_PROPERTIES = { "password", "oldPassword", "newPassword", "confirmPassword" };

    @Autowired
    LogService logService;

    @Pointcut("@annotation(com.sztu.check.annotation.Log)")
    public void logPointCut()
    {
    }

    /**
     * 处理完请求后执行
     *
     * @param joinPoint 切点
     */
    @AfterReturning(pointcut = "@annotation(controllerLog)", returning = "jsonResult")
    public void doAfterReturning(JoinPoint joinPoint, Log controllerLog, Object jsonResult) throws Exception {
        handleLog(joinPoint, controllerLog, null, jsonResult);
    }

    /**
     * 拦截异常操作
     * 
     * @param joinPoint 切点
     * @param e 异常
     */
    @AfterThrowing(value = "@annotation(controllerLog)", throwing = "e")
    public void doAfterThrowing(JoinPoint joinPoint, Log controllerLog, Exception e) throws Exception {
        handleLog(joinPoint, controllerLog, e, null);
    }

    protected void handleLog(final JoinPoint joinPoint, Log controllerLog, final Exception e, Object jsonResult) throws Exception {
        try
        {
            // 获取当前的用户
            OperatingUser currentUser = CurrentContext.getCurrentOperatingUser();

            // *========数据库日志=========*//
            com.sztu.check.domain.Log log = new com.sztu.check.domain.Log();

            log.setRequestUrl(ServletUtils.getRequest().getRequestURI());
            if (currentUser != null)
            {
                log.setCreateUser(currentUser.getUsername());
            }

            if (e != null)
            {
                log.setContent(StringUtils.substring(e.getMessage(), 0, 2000));
            }
            // 设置请求方式
            log.setRequestMethod(ServletUtils.getRequest().getMethod());
            // 处理设置注解上的参数
            getControllerMethodDescription(joinPoint, controllerLog, log, jsonResult);
            // 保存数据库
            CommonFieldsUtils.setCrtAndUpdFields(log);
            logService.save(log);
        }
        catch (Exception exp)
        {
            // 记录本地异常日志
            log.error("==前置通知异常==");
            log.error("异常信息:{}", exp.getMessage());
            throw exp;
        }
    }

    /**
     * 获取注解中对方法的描述信息 用于Controller层注解
     * 
     * @param log 日志
     * @param operLog 操作日志
     * @throws Exception
     */
    public void getControllerMethodDescription(JoinPoint joinPoint, Log log, com.sztu.check.domain.Log operLog, Object jsonResult) throws Exception
    {
        // 设置标题
        operLog.setContent(log.content());
        operLog.setBusinessType(log.businessType().name());
        // 保存request，参数和值
        setRequestValue(joinPoint, operLog);
        // 保存response，参数和值
        operLog.setResponse(StringUtils.substring(JSONObject.toJSONString(jsonResult), 0, 2000));

    }

    /**
     * 获取请求的参数，放到log中
     * 
     * @param log 操作日志
     * @throws Exception 异常
     */
    private void setRequestValue(JoinPoint joinPoint, com.sztu.check.domain.Log log) throws Exception
    {
        Map<String, String[]> map = ServletUtils.getRequest().getParameterMap();
        if (!CollectionUtils.isEmpty(map))
        {
            String params = JSONObject.toJSONString(map, excludePropertyPreFilter());
            log.setRequestParams(StringUtils.substring(params, 0, 300));
        }
        else
        {
            Object args = joinPoint.getArgs();
            if (ObjectUtils.isNotEmpty(args))
            {
                String params = argsArrayToString(joinPoint.getArgs());
                log.setRequestParams(StringUtils.substring(params, 0, 300));
            }
        }
    }

    /**
     * 忽略敏感属性
     */
    public PropertyPreFilters.MySimplePropertyPreFilter excludePropertyPreFilter()
    {
        return new PropertyPreFilters().addFilter().addExcludes(EXCLUDE_PROPERTIES);
    }

    /**
     * 参数拼装
     */
    private String argsArrayToString(Object[] paramsArray)
    {
        String params = "";
        if (paramsArray != null && paramsArray.length > 0)
        {
            for (Object o : paramsArray)
            {
                if (ObjectUtils.isNotEmpty(o) && !isFilterObject(o))
                {
                    try
                    {
                        Object jsonObj = JSONObject.toJSONString(o, excludePropertyPreFilter());
                        params += jsonObj.toString() + " ";
                    }
                    catch (Exception e)
                    {
                    }
                }
            }
        }
        return params.trim();
    }

    /**
     * 判断是否需要过滤的对象。
     * 
     * @param o 对象信息。
     * @return 如果是需要过滤的对象，则返回true；否则返回false。
     */
    @SuppressWarnings("rawtypes")
    public boolean isFilterObject(final Object o)
    {
        Class<?> clazz = o.getClass();
        if (clazz.isArray())
        {
            return clazz.getComponentType().isAssignableFrom(MultipartFile.class);
        }
        else if (Collection.class.isAssignableFrom(clazz))
        {
            Collection collection = (Collection) o;
            for (Object value : collection)
            {
                return value instanceof MultipartFile;
            }
        }
        else if (Map.class.isAssignableFrom(clazz))
        {
            Map map = (Map) o;
            for (Object value : map.entrySet())
            {
                Map.Entry entry = (Map.Entry) value;
                return entry.getValue() instanceof MultipartFile;
            }
        }
        return o instanceof MultipartFile || o instanceof HttpServletRequest || o instanceof HttpServletResponse
                || o instanceof BindingResult;
    }
}
