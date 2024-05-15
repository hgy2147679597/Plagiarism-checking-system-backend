package com.sztu.check.enums;

/**
 * @description: Enumeration of administrator tables
 * @author: wentianrui
 * @date:  21:49
 **/
public enum RoleEnum{
        // 管理员
        admin,
        //用户
        user;

        @Override
        public String toString() {
                switch (this) {
                        case admin:
                                return "admin";
                        case user:
                                return "user";
                        default:
                                return "";
                }
        }
}


