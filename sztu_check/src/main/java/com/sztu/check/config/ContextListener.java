package com.sztu.check.config;/*
作者:江江江
时间:2023/9/13
*/

import com.sztu.check.service.AdminService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.context.support.WebApplicationContextUtils;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

/**
 * @description: ContextListener
 * @author: wentianrui
 * @date:  16:21
 **/
@WebListener
@Component
@Slf4j
public class ContextListener implements ServletContextListener {

    @Autowired
    private AdminService adminService;
    @Override
    public void contextDestroyed(ServletContextEvent arg0) {
        log.info("web stop!");
    }


    @Override
    public void contextInitialized(ServletContextEvent sce) {
        WebApplicationContextUtils.getRequiredWebApplicationContext(sce.getServletContext())
                .getAutowireCapableBeanFactory().autowireBean(this);
        log.info("web start!");
        adminService.getAdminListAndSave();
    }
}