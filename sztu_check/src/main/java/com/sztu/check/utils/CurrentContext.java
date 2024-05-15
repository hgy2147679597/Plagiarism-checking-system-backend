package com.sztu.check.utils;

import com.baomidou.mybatisplus.core.toolkit.ObjectUtils;
import com.sztu.check.domain.OperatingUser;

import java.util.HashMap;
import java.util.Map;

/**
 * @author lujiawei
 * @version 1.0
 * @date 2022/01/04/ 14:50
 */
public class CurrentContext {
    private static final ThreadLocal<Map<String, Object>> THREAD_LOCAL = new ThreadLocal();
    private static final int LOCAL_MAP_SIZE = 8;

    public CurrentContext() {
    }

    private static void verifyThreadLocalMap() {
        Map<String, Object> map = (Map)THREAD_LOCAL.get();
        if (ObjectUtils.isNull(map)) {
            initThreadLocalMap();
        }

    }

    private static void initThreadLocalMap() {
        THREAD_LOCAL.set(new HashMap(8));
    }

    public static void set(String key, Object value) {
        verifyThreadLocalMap();
        ((Map)THREAD_LOCAL.get()).put(key, value);
    }

    public static Object get(String key) {
        verifyThreadLocalMap();
        Object value = ((Map)THREAD_LOCAL.get()).get(key);
        return ObjectUtils.isNull(value) ? null : value;
    }

    public static void setCurrentOperatingUser(OperatingUser operatingUser) {
        set("user", operatingUser);
    }

    public static final OperatingUser getCurrentOperatingUser() {
        return ObjectUtils.isNull(get("user")) ? null : (OperatingUser)get("user");
    }

    public static void destroy() {
        THREAD_LOCAL.remove();
    }
}
