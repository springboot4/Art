/*
 Navicat Premium Data Transfer

 Source Server         : 127.0.0.1
 Source Server Type    : MySQL
 Source Server Version : 80032
 Source Host           : 127.0.0.1:3306
 Source Schema         : art_codegen

 Target Server Type    : MySQL
 Target Server Version : 80032
 File Encoding         : 65001

 Date: 01/07/2023 15:31:19
*/
USE art_codegen;

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for gen_datasource_conf
-- ----------------------------
DROP TABLE IF EXISTS `gen_datasource_conf`;
CREATE TABLE `gen_datasource_conf` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `name` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '数据源名称',
  `url` varchar(512) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT 'jdbc-url',
  `username` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '用户名',
  `password` varchar(256) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '密码',
  `del_flag` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT '0' COMMENT '删除标记',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `create_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '创建人',
  `update_time` datetime DEFAULT NULL COMMENT '修改时间',
  `update_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '更新人',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=1654312475545124867 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='数据源表';

-- ----------------------------
-- Records of gen_datasource_conf
-- ----------------------------
BEGIN;
INSERT INTO `gen_datasource_conf` (`id`, `name`, `url`, `username`, `password`, `del_flag`, `create_time`, `create_by`, `update_time`, `update_by`) VALUES (1509439122488958977, 'art_base', 'jdbc:mysql://art-mysql/art_base?useUnicode=true&characterEncoding=UTF-8&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=GMT%2b8', 'root', '+Kn2gIPAKrCd/XB/QdQjoofFwXlNYknhgHifEgqP98M=', '0', '2022-03-31 15:55:05', 'fxz', '2023-07-01 10:10:54', 'fxz');
COMMIT;

SET FOREIGN_KEY_CHECKS = 1;
