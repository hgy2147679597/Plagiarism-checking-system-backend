package com.sztu.check.utils;

import com.baomidou.mybatisplus.core.toolkit.ObjectUtils;
import com.sztu.check.domain.OperatingUser;
import org.apache.commons.lang3.Validate;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.sql.Timestamp;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;


/**
 * @author lujiawei
 * @version 1.0
 * @date 2022/01/04/ 20:30
 */
public class CommonFieldsUtils {

    public CommonFieldsUtils() {
    }

    public static Object setCrtFields(Object handler) {
        OperatingUser currentOperatingUser = CurrentContext.getCurrentOperatingUser();
        return setCrtFields(currentOperatingUser, handler);
    }

    public static Object setUpdFields(Object handler) {
        OperatingUser currentOperatingUser = CurrentContext.getCurrentOperatingUser();
        return setUpdFields(currentOperatingUser, handler);
    }

    public static Object setCrtFields(OperatingUser operatingUser, Object handler) {
        Map<String, Object> fields = new HashMap(10);
        if (ObjectUtils.isNotNull(operatingUser)) {
            fields.put("setCreateId", operatingUser.getId());
            fields.put("setCreateUser", operatingUser.getUsername());
        }

        fields.put("setCreateTime", new Timestamp(System.currentTimeMillis()));
        Map<String, String> mapKeys = new HashMap(10);
        mapKeys.put("setCreateId", "createId");
        mapKeys.put("setCreateUser", "createUser");
        mapKeys.put("setCreateTime", "createTime");
        Method[] methods = handler.getClass().getMethods();
        callMethod(handler, methods, fields, mapKeys);
        return handler;
    }

    public static Object setUpdFields(OperatingUser operatingUser, Object handler) {
        Map<String, Object> fields = new HashMap(10);
        if (ObjectUtils.isNotNull(operatingUser)) {
            fields.put("setUpdateId", operatingUser.getId());
            fields.put("setUpdateUser", operatingUser.getUsername());
        }

        fields.put("setUpdateTime", new Timestamp(System.currentTimeMillis()));
        Map<String, String> mapKeys = new HashMap(10);
        mapKeys.put("setUpdateId", "updateId");
        mapKeys.put("setUpdateUser", "updateUser");
        mapKeys.put("setUpdateTime", "updateTime");
        Method[] methods = handler.getClass().getMethods();
        callMethod(handler, methods, fields, mapKeys);
        return handler;
    }

    public static Object setCrtAndUpdFields(Object handler) {
        setCrtFields(handler);
        setUpdFields(handler);
        return handler;
    }


    public static Object callMethod(Object handler, Method[] methods, Map<String, Object> fields, Map<String, String> mapKeys) {
        Iterator var4 = fields.entrySet().iterator();

        while(var4.hasNext()) {
            Entry<String, Object> entry = (Entry)var4.next();
            if (isExistMethod(methods, (String)entry.getKey())) {
                Method method = null;

                try {
                    String fieldName = (String)mapKeys.get(entry.getKey());
                    method = handler.getClass().getMethod((String)entry.getKey(), getAccessibleField(handler, fieldName).getType());
                } catch (NoSuchMethodException var10) {
                    var10.printStackTrace();
                } catch (NoSuchFieldException var11) {
                    var11.printStackTrace();
                }

                try {
                    method.invoke(handler, entry.getValue());
                } catch (IllegalAccessException var8) {
                    var8.printStackTrace();
                } catch (InvocationTargetException var9) {
                    var9.printStackTrace();
                }
            }
        }

        return handler;
    }

    public static boolean isExistMethod(Method[] methods, String methodName) {
        for(int i = 0; i < methods.length; ++i) {
            if (methods[i].getName().equals(methodName)) {
                return true;
            }
        }

        return false;
    }

    public static Field getAccessibleField(Object obj, String fieldName) throws NoSuchFieldException {
        Validate.notNull(obj, "object can't be null");
        Validate.notEmpty(fieldName, "fieldName can't be blank");
        Class superClass = obj.getClass();

        while(superClass != Object.class) {
            try {
                Field field = superClass.getDeclaredField(fieldName);
                makeAccessible(field);
                return field;
            } catch (NoSuchFieldException var4) {
                superClass = superClass.getSuperclass();
            }
        }

        return null;
    }

    public static void makeAccessible(Field field) {
        if ((!Modifier.isPublic(field.getModifiers()) || !Modifier.isPublic(field.getDeclaringClass().getModifiers()) || Modifier.isFinal(field.getModifiers())) && !field.isAccessible()) {
            field.setAccessible(true);
        }

    }
}

