/*
 Navicat Premium Data Transfer

 Source Server         : 10.11.155.47docker
 Source Server Type    : MySQL
 Source Server Version : 80100
 Source Host           : 10.11.155.47:3307
 Source Schema         : sztu_check

 Target Server Type    : MySQL
 Target Server Version : 80100
 File Encoding         : 65001

 Date: 16/10/2023 18:55:43
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for application
-- ----------------------------
CREATE TABLE IF NOT EXISTS `application`  (
                                `id` int NOT NULL AUTO_INCREMENT COMMENT '主键',
                                `title` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '标题',
                                `file_id` int NULL DEFAULT NULL COMMENT '文件id',
                                `create_id` int NULL DEFAULT NULL COMMENT '创建人id',
                                `create_user` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '创建人名称',
                                `create_time` datetime NULL DEFAULT CURRENT_TIMESTAMP,
                                `update_id` int NULL DEFAULT NULL COMMENT '更新人id',
                                `update_user` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '更新人名称',
                                `update_time` datetime NULL DEFAULT CURRENT_TIMESTAMP,
                                `status` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '状态',
                                `is_deleted` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '逻辑删除',
                                PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 35 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for file
-- ----------------------------
CREATE TABLE IF NOT EXISTS `file`  (
                         `id` int NOT NULL AUTO_INCREMENT COMMENT '主键',
                         `file_url` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '文件路径',
                         `create_id` int NULL DEFAULT NULL COMMENT '创建人id',
                         `create_user` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '创建人名称',
                         `create_time` datetime NULL DEFAULT CURRENT_TIMESTAMP,
                         `update_id` int NULL DEFAULT NULL COMMENT '更新人id',
                         `update_user` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '更新人名称',
                         `update_time` datetime NULL DEFAULT CURRENT_TIMESTAMP,
                         `is_deleted` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '逻辑删除',
                         `application_id` int NULL DEFAULT NULL COMMENT '申报id',
                         `file_name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '文件名',
                         PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 120 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for log
-- ----------------------------
CREATE TABLE IF NOT EXISTS `log`  (
                        `id` int NOT NULL AUTO_INCREMENT COMMENT '主键',
                        `content` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '内容',
                        `request_url` text CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '请求地址',
                        `request_method` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '请求方法',
                        `request_params` varchar(300) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '请求参数',
                        `create_id` int NULL DEFAULT NULL COMMENT '创建人id',
                        `create_user` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '创建人名称',
                        `create_time` datetime NULL DEFAULT CURRENT_TIMESTAMP,
                        `update_id` int NULL DEFAULT NULL COMMENT '更新人id',
                        `update_user` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '更新人名称',
                        `update_time` datetime NULL DEFAULT CURRENT_TIMESTAMP,
                        `status` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '状态',
                        `is_deleted` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '逻辑删除',
                        `response` varchar(3000) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '响应参数',
                        `business_type` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '业务类型',
                        PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1147 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for user
-- ----------------------------
CREATE TABLE IF NOT EXISTS `user`  (
                         `id` int NOT NULL AUTO_INCREMENT COMMENT '主键',
                         `username` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL DEFAULT '',
                         `password` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL DEFAULT '' COMMENT '密码',
                         `create_id` int NOT NULL DEFAULT -1 COMMENT '创建人id',
                         `create_user` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL DEFAULT '' COMMENT '创建人名称',
                         `create_time` datetime NULL DEFAULT CURRENT_TIMESTAMP,
                         `update_id` int NOT NULL DEFAULT -1 COMMENT '更新人id',
                         `update_user` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL DEFAULT '' COMMENT '更新人名称',
                         `update_time` datetime NULL DEFAULT CURRENT_TIMESTAMP,
                         `status` tinyint NOT NULL DEFAULT 1 COMMENT '状态',
                         `is_deleted` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL DEFAULT '0' COMMENT '逻辑删除',
                         `role` enum('admin','user') CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
                         `real_name` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT '',
                         PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 44 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = DYNAMIC;

SET FOREIGN_KEY_CHECKS = 1;
INSERT INTO user(`id`, `username`, `password`, `role`)
VALUES (1, 'admin', 'P@ssw0rD1', 'admin')
    ON DUPLICATE KEY UPDATE
                         username = VALUES(username), password = VALUES(password), role = VALUES(role);
