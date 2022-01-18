/*
 Navicat Premium Data Transfer

 Source Server         : localhost3306
 Source Server Type    : MySQL
 Source Server Version : 80000
 Source Host           : localhost:3306
 Source Schema         : fxz_cloud_base

 Target Server Type    : MySQL
 Target Server Version : 80000
 File Encoding         : 65001

 Date: 18/01/2022 19:45:59
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for t_dept
-- ----------------------------
DROP TABLE IF EXISTS `t_dept`;
CREATE TABLE `t_dept`  (
  `DEPT_ID` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '部门ID',
  `PARENT_ID` bigint(20) NOT NULL COMMENT '上级部门ID',
  `DEPT_NAME` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '部门名称',
  `ORDER_NUM` double(20, 0) NULL DEFAULT NULL COMMENT '排序',
  `CREATE_TIME` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  `MODIFY_TIME` datetime(0) NULL DEFAULT NULL COMMENT '修改时间',
  PRIMARY KEY (`DEPT_ID`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '部门表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of t_dept
-- ----------------------------
INSERT INTO `t_dept` VALUES (1, 0, '开发部', 1, '2018-01-04 15:42:26', '2019-01-05 21:08:27');

-- ----------------------------
-- Table structure for t_menu
-- ----------------------------
DROP TABLE IF EXISTS `t_menu`;
CREATE TABLE `t_menu`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '菜单/按钮ID',
  `parent_id` bigint(20) NOT NULL COMMENT '上级菜单ID',
  `name` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '菜单/按钮名称',
  `perms` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '权限标识(多个用逗号分隔，如：user:list,user:create)',
  `type` char(2) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '类型 0菜单 1按钮',
  `component` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '对应路由组件component',
  `path` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '对应路由path',
  `icon` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '图标',
  `keep_alive` tinyint(4) NULL DEFAULT NULL COMMENT '是否缓存 0:否 1:是',
  `order_num` double(20, 0) NULL DEFAULT NULL COMMENT '排序',
  `create_time` datetime(0) NOT NULL COMMENT '创建时间',
  `modify_time` datetime(0) NULL DEFAULT NULL COMMENT '修改时间',
  `create_by` bigint(20) NULL DEFAULT NULL COMMENT '创建人',
  `update_by` bigint(20) NULL DEFAULT NULL COMMENT '更新人',
  `hidden` char(10) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '是否隐藏(1 隐藏 0 不隐藏)',
  `title` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT 'title',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '菜单表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of t_menu
-- ----------------------------
INSERT INTO `t_menu` VALUES (1, 0, 'dashboard', 'dashboard', '0', 'RouteView', '/dashboard', '', NULL, 1, '2017-12-27 16:39:07', '2019-07-20 16:19:04', NULL, NULL, NULL, '仪表盘');
INSERT INTO `t_menu` VALUES (2, 1, 'Analysis', 'dashboard', '0', 'dashboard/Analysis', '/dashboard/analysis/:pageNo([1-9]\\d*)?', '', NULL, 1, '2017-12-27 16:47:13', '2019-01-22 06:45:55', NULL, NULL, NULL, '分析页');
INSERT INTO `t_menu` VALUES (3, 1, 'Workplace', 'dashboard', '0', 'dashboard/Workplace', '/dashboard/workplace', NULL, NULL, 1, '2017-12-27 17:02:58', NULL, NULL, NULL, NULL, '工作台');
INSERT INTO `t_menu` VALUES (4, 0, 'account', 'user', '0', 'RouteView', '/account', NULL, NULL, 2, '2017-12-27 17:04:07', NULL, NULL, NULL, NULL, '个人页');
INSERT INTO `t_menu` VALUES (5, 4, 'center', 'user', '0', 'account/center', '/account/center', NULL, NULL, 2, '2017-12-27 17:04:58', NULL, NULL, NULL, NULL, '个人中心');
INSERT INTO `t_menu` VALUES (6, 4, 'settings', 'user', '0', 'account/settings/Index', '/account/settings', NULL, NULL, 2, '2022-01-18 14:44:25', NULL, NULL, NULL, NULL, '个人设置');
INSERT INTO `t_menu` VALUES (7, 6, 'BaseSettings', 'user', '0', 'account/settings/BaseSetting', '/account/settings/base', NULL, NULL, 2, '2022-01-18 14:48:57', NULL, NULL, NULL, NULL, '基本设置');
INSERT INTO `t_menu` VALUES (8, 6, 'SecuritySettings', 'user', '0', 'account/settings/Security', '/account/settings/security', NULL, NULL, 2, '2022-01-18 14:49:39', NULL, NULL, NULL, NULL, '安全设置');
INSERT INTO `t_menu` VALUES (9, 6, 'CustomSettings', 'user', '0', 'account/settings/Custom', '/account/settings/custom', NULL, NULL, 2, '2022-01-18 14:50:17', NULL, NULL, NULL, NULL, '个性化设置');
INSERT INTO `t_menu` VALUES (10, 6, 'BindingSettings', 'user', '0', 'account/settings/Binding', '/account/settings/binding', NULL, NULL, 2, '2022-01-18 14:50:58', NULL, NULL, NULL, NULL, '账户绑定');
INSERT INTO `t_menu` VALUES (11, 6, 'NotificationSettings', 'user', '0', 'account/settings/Notification', '/account/settings/notification', NULL, NULL, 2, '2022-01-18 14:51:31', NULL, NULL, NULL, NULL, '新消息通知');
INSERT INTO `t_menu` VALUES (12, 0, 'welcome', 'welcome', '0', 'index/welcome', '/welcome', NULL, NULL, 0, '2022-01-18 16:22:11', NULL, NULL, NULL, '1', '欢迎页');
INSERT INTO `t_menu` VALUES (99998, 0, '403', NULL, '0', '/exception/403', '/403', NULL, 1, NULL, '2022-01-18 17:49:39', NULL, NULL, NULL, '1', '403');
INSERT INTO `t_menu` VALUES (99999, 0, '404', NULL, '0', '404', '/404', NULL, 1, 999999, '2022-01-18 17:39:09', NULL, NULL, NULL, '1', '404');

-- ----------------------------
-- Table structure for t_role
-- ----------------------------
DROP TABLE IF EXISTS `t_role`;
CREATE TABLE `t_role`  (
  `ROLE_ID` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '角色ID',
  `ROLE_NAME` varchar(10) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '角色名称',
  `REMARK` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '角色描述',
  `CREATE_TIME` datetime(0) NOT NULL COMMENT '创建时间',
  `MODIFY_TIME` datetime(0) NULL DEFAULT NULL COMMENT '修改时间',
  PRIMARY KEY (`ROLE_ID`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '角色表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of t_role
-- ----------------------------
INSERT INTO `t_role` VALUES (1, '管理员', '管理员', '2019-08-08 16:23:11', '2019-08-09 14:38:59');

-- ----------------------------
-- Table structure for t_role_menu
-- ----------------------------
DROP TABLE IF EXISTS `t_role_menu`;
CREATE TABLE `t_role_menu`  (
  `ROLE_ID` bigint(20) NOT NULL,
  `MENU_ID` bigint(20) NOT NULL
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '角色菜单关联表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of t_role_menu
-- ----------------------------
INSERT INTO `t_role_menu` VALUES (1, 1);
INSERT INTO `t_role_menu` VALUES (1, 2);
INSERT INTO `t_role_menu` VALUES (1, 3);
INSERT INTO `t_role_menu` VALUES (1, 4);
INSERT INTO `t_role_menu` VALUES (1, 5);
INSERT INTO `t_role_menu` VALUES (1, 6);
INSERT INTO `t_role_menu` VALUES (1, 7);
INSERT INTO `t_role_menu` VALUES (1, 12);

-- ----------------------------
-- Table structure for t_user
-- ----------------------------
DROP TABLE IF EXISTS `t_user`;
CREATE TABLE `t_user`  (
  `USER_ID` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '用户ID',
  `USERNAME` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '用户名',
  `PASSWORD` varchar(128) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '密码',
  `DEPT_ID` bigint(20) NULL DEFAULT NULL COMMENT '部门ID',
  `EMAIL` varchar(128) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '邮箱',
  `MOBILE` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '联系电话',
  `STATUS` char(1) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '状态 0锁定 1有效',
  `CREATE_TIME` datetime(0) NOT NULL COMMENT '创建时间',
  `MODIFY_TIME` datetime(0) NULL DEFAULT NULL COMMENT '修改时间',
  `LAST_LOGIN_TIME` datetime(0) NULL DEFAULT NULL COMMENT '最近访问时间',
  `SSEX` char(1) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '性别 0男 1女 2保密',
  `AVATAR` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '头像',
  `DESCRIPTION` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '描述',
  PRIMARY KEY (`USER_ID`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '用户表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of t_user
-- ----------------------------
INSERT INTO `t_user` VALUES (1, 'fxz', '$2a$10$gzhiUb1ldc1Rf3lka4k/WOoFKKGPepHSzJxzcPSN5/65SzkMdc.SK', 1, 'fxzbiz@gmail.com', '19888888888', '1', '2019-06-14 20:39:22', '2019-07-19 10:18:36', '2019-08-02 15:57:00', '0', 'https://preview.pro.antdv.com/avatar2.jpg', '我是帅比作者。');
INSERT INTO `t_user` VALUES (3, 'fxzz', '$2a$10$gzhiUb1ldc1Rf3lka4k/WOoFKKGPepHSzJxzcPSN5/65SzkMdc.SK', NULL, 'fxzbiz@gmail.com', '19888888880', '1', '2021-11-28 20:43:47', '2021-12-19 21:20:29', NULL, '1', 'https://preview.pro.antdv.com/avatar2.jpg', NULL);

-- ----------------------------
-- Table structure for t_user_role
-- ----------------------------
DROP TABLE IF EXISTS `t_user_role`;
CREATE TABLE `t_user_role`  (
  `USER_ID` bigint(20) NOT NULL COMMENT '用户ID',
  `ROLE_ID` bigint(20) NOT NULL COMMENT '角色ID'
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '用户角色关联表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of t_user_role
-- ----------------------------
INSERT INTO `t_user_role` VALUES (1, 1);
INSERT INTO `t_user_role` VALUES (3, 1);

-- ----------------------------
-- Table structure for zipkin_annotations
-- ----------------------------
DROP TABLE IF EXISTS `zipkin_annotations`;
CREATE TABLE `zipkin_annotations`  (
  `trace_id_high` bigint(20) NOT NULL DEFAULT 0 COMMENT 'If non zero, this means the trace uses 128 bit traceIds instead of 64 bit',
  `trace_id` bigint(20) NOT NULL COMMENT 'coincides with zipkin_spans.trace_id',
  `span_id` bigint(20) NOT NULL COMMENT 'coincides with zipkin_spans.id',
  `a_key` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT 'BinaryAnnotation.key or Annotation.value if type == -1',
  `a_value` blob NULL COMMENT 'BinaryAnnotation.value(), which must be smaller than 64KB',
  `a_type` int(11) NOT NULL COMMENT 'BinaryAnnotation.type() or -1 if Annotation',
  `a_timestamp` bigint(20) NULL DEFAULT NULL COMMENT 'Used to implement TTL; Annotation.timestamp or zipkin_spans.timestamp',
  `endpoint_ipv4` int(11) NULL DEFAULT NULL COMMENT 'Null when Binary/Annotation.endpoint is null',
  `endpoint_ipv6` binary(16) NULL DEFAULT NULL COMMENT 'Null when Binary/Annotation.endpoint is null, or no IPv6 address',
  `endpoint_port` smallint(6) NULL DEFAULT NULL COMMENT 'Null when Binary/Annotation.endpoint is null',
  `endpoint_service_name` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT 'Null when Binary/Annotation.endpoint is null',
  UNIQUE INDEX `trace_id_high`(`trace_id_high`, `trace_id`, `span_id`, `a_key`, `a_timestamp`) USING BTREE COMMENT 'Ignore insert on duplicate',
  INDEX `trace_id_high_2`(`trace_id_high`, `trace_id`, `span_id`) USING BTREE COMMENT 'for joining with zipkin_spans',
  INDEX `trace_id_high_3`(`trace_id_high`, `trace_id`) USING BTREE COMMENT 'for getTraces/ByIds',
  INDEX `endpoint_service_name`(`endpoint_service_name`) USING BTREE COMMENT 'for getTraces and getServiceNames',
  INDEX `a_type`(`a_type`) USING BTREE COMMENT 'for getTraces and autocomplete values',
  INDEX `a_key`(`a_key`) USING BTREE COMMENT 'for getTraces and autocomplete values',
  INDEX `trace_id`(`trace_id`, `span_id`, `a_key`) USING BTREE COMMENT 'for dependencies job'
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Compressed;

-- ----------------------------
-- Records of zipkin_annotations
-- ----------------------------
INSERT INTO `zipkin_annotations` VALUES (0, -2566181379648875080, -2566181379648875080, 'lc', '', 6, 1638169865802000, -1062715903, NULL, NULL, 'fxz-server-system');
INSERT INTO `zipkin_annotations` VALUES (0, -7881802119374865842, -7881802119374865842, 'lc', '', 6, 1638169867805000, -1062715903, NULL, NULL, 'fxz-server-system-test');
INSERT INTO `zipkin_annotations` VALUES (0, -6145841007590615249, -6145841007590615249, 'sr', NULL, -1, 1638169875010503, -1062715903, NULL, NULL, 'fxz-server-system-test');
INSERT INTO `zipkin_annotations` VALUES (0, -6145841007590615249, -6145841007590615249, 'ss', NULL, -1, 1638169875015540, -1062715903, NULL, NULL, 'fxz-server-system-test');
INSERT INTO `zipkin_annotations` VALUES (0, -6145841007590615249, -6145841007590615249, 'ca', 0x01, 0, 1638169875010503, -1062721535, NULL, -5304, '');
INSERT INTO `zipkin_annotations` VALUES (0, -6145841007590615249, -6145841007590615249, 'http.method', 0x474554, 6, 1638169875010503, -1062715903, NULL, NULL, 'fxz-server-system-test');
INSERT INTO `zipkin_annotations` VALUES (0, -6145841007590615249, -6145841007590615249, 'http.path', 0x2F6163747561746F72, 6, 1638169875010503, -1062715903, NULL, NULL, 'fxz-server-system-test');
INSERT INTO `zipkin_annotations` VALUES (0, -8025310126578137703, -8025310126578137703, 'sr', NULL, -1, 1638169875020435, -1062715903, NULL, NULL, 'fxz-server-system');
INSERT INTO `zipkin_annotations` VALUES (0, -8025310126578137703, -8025310126578137703, 'ss', NULL, -1, 1638169875026622, -1062715903, NULL, NULL, 'fxz-server-system');
INSERT INTO `zipkin_annotations` VALUES (0, -8025310126578137703, -8025310126578137703, 'ca', 0x01, 0, 1638169875020435, -1062721535, NULL, -5302, '');
INSERT INTO `zipkin_annotations` VALUES (0, -8025310126578137703, -8025310126578137703, 'http.method', 0x474554, 6, 1638169875020435, -1062715903, NULL, NULL, 'fxz-server-system');
INSERT INTO `zipkin_annotations` VALUES (0, -8025310126578137703, -8025310126578137703, 'http.path', 0x2F6163747561746F72, 6, 1638169875020435, -1062715903, NULL, NULL, 'fxz-server-system');
INSERT INTO `zipkin_annotations` VALUES (0, -7606622751437803094, -7606622751437803094, 'sr', NULL, -1, 1638169892123160, -1062715903, NULL, NULL, 'fxz-server-system');
INSERT INTO `zipkin_annotations` VALUES (0, -7606622751437803094, -7606622751437803094, 'ss', NULL, -1, 1638169892126424, -1062715903, NULL, NULL, 'fxz-server-system');
INSERT INTO `zipkin_annotations` VALUES (0, -7606622751437803094, -7606622751437803094, 'ca', 0x01, 0, 1638169892123160, -1062721535, NULL, -5301, '');
INSERT INTO `zipkin_annotations` VALUES (0, -7606622751437803094, -7606622751437803094, 'http.method', 0x474554, 6, 1638169892123160, -1062715903, NULL, NULL, 'fxz-server-system');
INSERT INTO `zipkin_annotations` VALUES (0, -7606622751437803094, -7606622751437803094, 'http.path', 0x2F6163747561746F72, 6, 1638169892123160, -1062715903, NULL, NULL, 'fxz-server-system');
INSERT INTO `zipkin_annotations` VALUES (0, 4592102201703596623, 4592102201703596623, 'sr', NULL, -1, 1638169894232100, -1062715903, NULL, NULL, 'fxz-server-system-test');
INSERT INTO `zipkin_annotations` VALUES (0, 4592102201703596623, 4592102201703596623, 'ss', NULL, -1, 1638169894234427, -1062715903, NULL, NULL, 'fxz-server-system-test');
INSERT INTO `zipkin_annotations` VALUES (0, 4592102201703596623, 4592102201703596623, 'ca', 0x01, 0, 1638169894232100, -1062721535, NULL, -5303, '');
INSERT INTO `zipkin_annotations` VALUES (0, 4592102201703596623, 4592102201703596623, 'http.method', 0x474554, 6, 1638169894232100, -1062715903, NULL, NULL, 'fxz-server-system-test');
INSERT INTO `zipkin_annotations` VALUES (0, 4592102201703596623, 4592102201703596623, 'http.path', 0x2F6163747561746F72, 6, 1638169894232100, -1062715903, NULL, NULL, 'fxz-server-system-test');
INSERT INTO `zipkin_annotations` VALUES (0, -5328535706363524248, -6951861956747260916, 'cs', NULL, -1, 1638169938987665, -1062715903, NULL, NULL, 'fxz-server-system-test');
INSERT INTO `zipkin_annotations` VALUES (0, -5328535706363524248, -6951861956747260916, 'cr', NULL, -1, 1638169939049919, -1062715903, NULL, NULL, 'fxz-server-system-test');
INSERT INTO `zipkin_annotations` VALUES (0, -5328535706363524248, -6951861956747260916, 'http.method', 0x474554, 6, 1638169938987665, -1062715903, NULL, NULL, 'fxz-server-system-test');
INSERT INTO `zipkin_annotations` VALUES (0, -5328535706363524248, -6951861956747260916, 'http.path', 0x2F617574682F75736572, 6, 1638169938987665, -1062715903, NULL, NULL, 'fxz-server-system-test');
INSERT INTO `zipkin_annotations` VALUES (0, -5328535706363524248, 3225804995639601053, 'cs', NULL, -1, 1638169939525241, -1062715903, NULL, NULL, 'fxz-server-system');
INSERT INTO `zipkin_annotations` VALUES (0, -5328535706363524248, 3225804995639601053, 'cr', NULL, -1, 1638169939573407, -1062715903, NULL, NULL, 'fxz-server-system');
INSERT INTO `zipkin_annotations` VALUES (0, -5328535706363524248, 3225804995639601053, 'http.method', 0x474554, 6, 1638169939525241, -1062715903, NULL, NULL, 'fxz-server-system');
INSERT INTO `zipkin_annotations` VALUES (0, -5328535706363524248, 3225804995639601053, 'http.path', 0x2F617574682F75736572, 6, 1638169939525241, -1062715903, NULL, NULL, 'fxz-server-system');
INSERT INTO `zipkin_annotations` VALUES (0, -5328535706363524248, -8520234267970190352, 'sr', NULL, -1, 1638169939521100, -1062715903, NULL, NULL, 'fxz-server-system');
INSERT INTO `zipkin_annotations` VALUES (0, -5328535706363524248, -8520234267970190352, 'ss', NULL, -1, 1638169939594973, -1062715903, NULL, NULL, 'fxz-server-system');
INSERT INTO `zipkin_annotations` VALUES (0, -5328535706363524248, -8520234267970190352, 'ca', 0x01, 0, 1638169939521100, 2130706433, NULL, -5197, '');
INSERT INTO `zipkin_annotations` VALUES (0, -5328535706363524248, -8520234267970190352, 'http.method', 0x474554, 6, 1638169939521100, -1062715903, NULL, NULL, 'fxz-server-system');
INSERT INTO `zipkin_annotations` VALUES (0, -5328535706363524248, -8520234267970190352, 'http.path', 0x2F68656C6C6F, 6, 1638169939521100, -1062715903, NULL, NULL, 'fxz-server-system');
INSERT INTO `zipkin_annotations` VALUES (0, -5328535706363524248, -8520234267970190352, 'mvc.controller.class', 0x54657374436F6E74726F6C6C6572, 6, 1638169939521100, -1062715903, NULL, NULL, 'fxz-server-system');
INSERT INTO `zipkin_annotations` VALUES (0, -5328535706363524248, -8520234267970190352, 'mvc.controller.method', 0x68656C6C6F, 6, 1638169939521100, -1062715903, NULL, NULL, 'fxz-server-system');
INSERT INTO `zipkin_annotations` VALUES (0, -5328535706363524248, -8520234267970190352, 'cs', NULL, -1, 1638169939517799, -1062715903, NULL, NULL, 'fxz-server-system-test');
INSERT INTO `zipkin_annotations` VALUES (0, -5328535706363524248, -8520234267970190352, 'cr', NULL, -1, 1638169939598483, -1062715903, NULL, NULL, 'fxz-server-system-test');
INSERT INTO `zipkin_annotations` VALUES (0, -5328535706363524248, -8520234267970190352, 'http.method', 0x474554, 6, 1638169939517799, -1062715903, NULL, NULL, 'fxz-server-system-test');
INSERT INTO `zipkin_annotations` VALUES (0, -5328535706363524248, -8520234267970190352, 'http.path', 0x2F68656C6C6F, 6, 1638169939517799, -1062715903, NULL, NULL, 'fxz-server-system-test');
INSERT INTO `zipkin_annotations` VALUES (0, -5328535706363524248, -5328535706363524248, 'sr', NULL, -1, 1638169938984125, -1062715903, NULL, NULL, 'fxz-server-system-test');
INSERT INTO `zipkin_annotations` VALUES (0, -5328535706363524248, -5328535706363524248, 'ss', NULL, -1, 1638169939611242, -1062715903, NULL, NULL, 'fxz-server-system-test');
INSERT INTO `zipkin_annotations` VALUES (0, -5328535706363524248, -5328535706363524248, 'ca', 0x01, 0, 1638169938984125, NULL, 0x00000000000000000000000000000001, NULL, '');
INSERT INTO `zipkin_annotations` VALUES (0, -5328535706363524248, -5328535706363524248, 'http.method', 0x474554, 6, 1638169938984125, -1062715903, NULL, NULL, 'fxz-server-system-test');
INSERT INTO `zipkin_annotations` VALUES (0, -5328535706363524248, -5328535706363524248, 'http.path', 0x2F68656C6C6F, 6, 1638169938984125, -1062715903, NULL, NULL, 'fxz-server-system-test');
INSERT INTO `zipkin_annotations` VALUES (0, -5328535706363524248, -5328535706363524248, 'mvc.controller.class', 0x54657374436F6E74726F6C6C6572, 6, 1638169938984125, -1062715903, NULL, NULL, 'fxz-server-system-test');
INSERT INTO `zipkin_annotations` VALUES (0, -5328535706363524248, -5328535706363524248, 'mvc.controller.method', 0x68656C6C6F, 6, 1638169938984125, -1062715903, NULL, NULL, 'fxz-server-system-test');
INSERT INTO `zipkin_annotations` VALUES (0, 3021340501933078410, 3021340501933078410, 'lc', '', 6, 1638171406675000, -1062715903, NULL, NULL, 'fxz-server-system');
INSERT INTO `zipkin_annotations` VALUES (0, -832826882313434463, -832826882313434463, 'lc', '', 6, 1638171416873000, -1062715903, NULL, NULL, 'fxz-server-system-test');
INSERT INTO `zipkin_annotations` VALUES (0, 3831019749791502824, 3831019749791502824, 'sr', NULL, -1, 1638171433055254, -1062715903, NULL, NULL, 'fxz-server-system');
INSERT INTO `zipkin_annotations` VALUES (0, 3831019749791502824, 3831019749791502824, 'ss', NULL, -1, 1638171433063336, -1062715903, NULL, NULL, 'fxz-server-system');
INSERT INTO `zipkin_annotations` VALUES (0, 3831019749791502824, 3831019749791502824, 'ca', 0x01, 0, 1638171433055254, -1062721535, NULL, -3732, '');
INSERT INTO `zipkin_annotations` VALUES (0, 3831019749791502824, 3831019749791502824, 'http.method', 0x474554, 6, 1638171433055254, -1062715903, NULL, NULL, 'fxz-server-system');
INSERT INTO `zipkin_annotations` VALUES (0, 3831019749791502824, 3831019749791502824, 'http.path', 0x2F6163747561746F72, 6, 1638171433055254, -1062715903, NULL, NULL, 'fxz-server-system');
INSERT INTO `zipkin_annotations` VALUES (0, 6953175459323808214, 6953175459323808214, 'sr', NULL, -1, 1638171443324234, -1062715903, NULL, NULL, 'fxz-server-system-test');
INSERT INTO `zipkin_annotations` VALUES (0, 6953175459323808214, 6953175459323808214, 'ss', NULL, -1, 1638171443331556, -1062715903, NULL, NULL, 'fxz-server-system-test');
INSERT INTO `zipkin_annotations` VALUES (0, 6953175459323808214, 6953175459323808214, 'ca', 0x01, 0, 1638171443324234, -1062721535, NULL, -3716, '');
INSERT INTO `zipkin_annotations` VALUES (0, 6953175459323808214, 6953175459323808214, 'http.method', 0x474554, 6, 1638171443324234, -1062715903, NULL, NULL, 'fxz-server-system-test');
INSERT INTO `zipkin_annotations` VALUES (0, 6953175459323808214, 6953175459323808214, 'http.path', 0x2F6163747561746F72, 6, 1638171443324234, -1062715903, NULL, NULL, 'fxz-server-system-test');
INSERT INTO `zipkin_annotations` VALUES (0, 3592737260128299163, 8028521047333011932, 'cs', NULL, -1, 1638171534842575, -1062715903, NULL, NULL, 'fxz-server-system-test');
INSERT INTO `zipkin_annotations` VALUES (0, 3592737260128299163, 8028521047333011932, 'cr', NULL, -1, 1638171534937773, -1062715903, NULL, NULL, 'fxz-server-system-test');
INSERT INTO `zipkin_annotations` VALUES (0, 3592737260128299163, 8028521047333011932, 'http.method', 0x474554, 6, 1638171534842575, -1062715903, NULL, NULL, 'fxz-server-system-test');
INSERT INTO `zipkin_annotations` VALUES (0, 3592737260128299163, 8028521047333011932, 'http.path', 0x2F617574682F75736572, 6, 1638171534842575, -1062715903, NULL, NULL, 'fxz-server-system-test');
INSERT INTO `zipkin_annotations` VALUES (0, 3592737260128299163, 1198070489411063301, 'cs', NULL, -1, 1638171535296297, -1062715903, NULL, NULL, 'fxz-server-system-test');
INSERT INTO `zipkin_annotations` VALUES (0, 3592737260128299163, 1198070489411063301, 'cr', NULL, -1, 1638171535351360, -1062715903, NULL, NULL, 'fxz-server-system-test');
INSERT INTO `zipkin_annotations` VALUES (0, 3592737260128299163, 1198070489411063301, 'http.method', 0x474554, 6, 1638171535296297, -1062715903, NULL, NULL, 'fxz-server-system-test');
INSERT INTO `zipkin_annotations` VALUES (0, 3592737260128299163, 1198070489411063301, 'http.path', 0x2F68656C6C6F, 6, 1638171535296297, -1062715903, NULL, NULL, 'fxz-server-system-test');
INSERT INTO `zipkin_annotations` VALUES (0, 3592737260128299163, 3592737260128299163, 'sr', NULL, -1, 1638171534839111, -1062715903, NULL, NULL, 'fxz-server-system-test');
INSERT INTO `zipkin_annotations` VALUES (0, 3592737260128299163, 3592737260128299163, 'ss', NULL, -1, 1638171535361900, -1062715903, NULL, NULL, 'fxz-server-system-test');
INSERT INTO `zipkin_annotations` VALUES (0, 3592737260128299163, 3592737260128299163, 'ca', 0x01, 0, 1638171534839111, NULL, 0x00000000000000000000000000000001, NULL, '');
INSERT INTO `zipkin_annotations` VALUES (0, 3592737260128299163, 3592737260128299163, 'http.method', 0x474554, 6, 1638171534839111, -1062715903, NULL, NULL, 'fxz-server-system-test');
INSERT INTO `zipkin_annotations` VALUES (0, 3592737260128299163, 3592737260128299163, 'http.path', 0x2F68656C6C6F, 6, 1638171534839111, -1062715903, NULL, NULL, 'fxz-server-system-test');
INSERT INTO `zipkin_annotations` VALUES (0, 3592737260128299163, 3592737260128299163, 'mvc.controller.class', 0x54657374436F6E74726F6C6C6572, 6, 1638171534839111, -1062715903, NULL, NULL, 'fxz-server-system-test');
INSERT INTO `zipkin_annotations` VALUES (0, 3592737260128299163, 3592737260128299163, 'mvc.controller.method', 0x68656C6C6F, 6, 1638171534839111, -1062715903, NULL, NULL, 'fxz-server-system-test');
INSERT INTO `zipkin_annotations` VALUES (0, 3592737260128299163, -2874851072035824594, 'cs', NULL, -1, 1638171535303270, -1062715903, NULL, NULL, 'fxz-server-system');
INSERT INTO `zipkin_annotations` VALUES (0, 3592737260128299163, -2874851072035824594, 'cr', NULL, -1, 1638171535326023, -1062715903, NULL, NULL, 'fxz-server-system');
INSERT INTO `zipkin_annotations` VALUES (0, 3592737260128299163, -2874851072035824594, 'http.method', 0x474554, 6, 1638171535303270, -1062715903, NULL, NULL, 'fxz-server-system');
INSERT INTO `zipkin_annotations` VALUES (0, 3592737260128299163, -2874851072035824594, 'http.path', 0x2F617574682F75736572, 6, 1638171535303270, -1062715903, NULL, NULL, 'fxz-server-system');
INSERT INTO `zipkin_annotations` VALUES (0, 3592737260128299163, 1198070489411063301, 'sr', NULL, -1, 1638171535299183, -1062715903, NULL, NULL, 'fxz-server-system');
INSERT INTO `zipkin_annotations` VALUES (0, 3592737260128299163, 1198070489411063301, 'ss', NULL, -1, 1638171535349071, -1062715903, NULL, NULL, 'fxz-server-system');
INSERT INTO `zipkin_annotations` VALUES (0, 3592737260128299163, 1198070489411063301, 'ca', 0x01, 0, 1638171535299183, 2130706433, NULL, -3679, '');
INSERT INTO `zipkin_annotations` VALUES (0, 3592737260128299163, 1198070489411063301, 'http.method', 0x474554, 6, 1638171535299183, -1062715903, NULL, NULL, 'fxz-server-system');
INSERT INTO `zipkin_annotations` VALUES (0, 3592737260128299163, 1198070489411063301, 'http.path', 0x2F68656C6C6F, 6, 1638171535299183, -1062715903, NULL, NULL, 'fxz-server-system');
INSERT INTO `zipkin_annotations` VALUES (0, 3592737260128299163, 1198070489411063301, 'mvc.controller.class', 0x54657374436F6E74726F6C6C6572, 6, 1638171535299183, -1062715903, NULL, NULL, 'fxz-server-system');
INSERT INTO `zipkin_annotations` VALUES (0, 3592737260128299163, 1198070489411063301, 'mvc.controller.method', 0x68656C6C6F, 6, 1638171535299183, -1062715903, NULL, NULL, 'fxz-server-system');
INSERT INTO `zipkin_annotations` VALUES (0, -5656661044141199813, -6092636913825039442, 'cs', NULL, -1, 1638171883694734, -1062715903, NULL, NULL, 'fxz-server-system-test');
INSERT INTO `zipkin_annotations` VALUES (0, -5656661044141199813, -6092636913825039442, 'cr', NULL, -1, 1638171883707687, -1062715903, NULL, NULL, 'fxz-server-system-test');
INSERT INTO `zipkin_annotations` VALUES (0, -5656661044141199813, -6092636913825039442, 'http.method', 0x474554, 6, 1638171883694734, -1062715903, NULL, NULL, 'fxz-server-system-test');
INSERT INTO `zipkin_annotations` VALUES (0, -5656661044141199813, -6092636913825039442, 'http.path', 0x2F617574682F75736572, 6, 1638171883694734, -1062715903, NULL, NULL, 'fxz-server-system-test');
INSERT INTO `zipkin_annotations` VALUES (0, -5656661044141199813, 1200834296849922203, 'cs', NULL, -1, 1638171883710609, -1062715903, NULL, NULL, 'fxz-server-system-test');
INSERT INTO `zipkin_annotations` VALUES (0, -5656661044141199813, 1200834296849922203, 'cr', NULL, -1, 1638171883730378, -1062715903, NULL, NULL, 'fxz-server-system-test');
INSERT INTO `zipkin_annotations` VALUES (0, -5656661044141199813, 1200834296849922203, 'http.method', 0x474554, 6, 1638171883710609, -1062715903, NULL, NULL, 'fxz-server-system-test');
INSERT INTO `zipkin_annotations` VALUES (0, -5656661044141199813, 1200834296849922203, 'http.path', 0x2F68656C6C6F, 6, 1638171883710609, -1062715903, NULL, NULL, 'fxz-server-system-test');
INSERT INTO `zipkin_annotations` VALUES (0, -5656661044141199813, -5656661044141199813, 'sr', NULL, -1, 1638171883694087, -1062715903, NULL, NULL, 'fxz-server-system-test');
INSERT INTO `zipkin_annotations` VALUES (0, -5656661044141199813, -5656661044141199813, 'ss', NULL, -1, 1638171883731729, -1062715903, NULL, NULL, 'fxz-server-system-test');
INSERT INTO `zipkin_annotations` VALUES (0, -5656661044141199813, -5656661044141199813, 'ca', 0x01, 0, 1638171883694087, NULL, 0x00000000000000000000000000000001, NULL, '');
INSERT INTO `zipkin_annotations` VALUES (0, -5656661044141199813, -5656661044141199813, 'http.method', 0x474554, 6, 1638171883694087, -1062715903, NULL, NULL, 'fxz-server-system-test');
INSERT INTO `zipkin_annotations` VALUES (0, -5656661044141199813, -5656661044141199813, 'http.path', 0x2F68656C6C6F, 6, 1638171883694087, -1062715903, NULL, NULL, 'fxz-server-system-test');
INSERT INTO `zipkin_annotations` VALUES (0, -5656661044141199813, -5656661044141199813, 'mvc.controller.class', 0x54657374436F6E74726F6C6C6572, 6, 1638171883694087, -1062715903, NULL, NULL, 'fxz-server-system-test');
INSERT INTO `zipkin_annotations` VALUES (0, -5656661044141199813, -5656661044141199813, 'mvc.controller.method', 0x68656C6C6F, 6, 1638171883694087, -1062715903, NULL, NULL, 'fxz-server-system-test');
INSERT INTO `zipkin_annotations` VALUES (0, -5656661044141199813, -8489855318435819779, 'cs', NULL, -1, 1638171883712031, -1062715903, NULL, NULL, 'fxz-server-system');
INSERT INTO `zipkin_annotations` VALUES (0, -5656661044141199813, -8489855318435819779, 'cr', NULL, -1, 1638171883725761, -1062715903, NULL, NULL, 'fxz-server-system');
INSERT INTO `zipkin_annotations` VALUES (0, -5656661044141199813, -8489855318435819779, 'http.method', 0x474554, 6, 1638171883712031, -1062715903, NULL, NULL, 'fxz-server-system');
INSERT INTO `zipkin_annotations` VALUES (0, -5656661044141199813, -8489855318435819779, 'http.path', 0x2F617574682F75736572, 6, 1638171883712031, -1062715903, NULL, NULL, 'fxz-server-system');
INSERT INTO `zipkin_annotations` VALUES (0, -5656661044141199813, 1200834296849922203, 'sr', NULL, -1, 1638171883712085, -1062715903, NULL, NULL, 'fxz-server-system');
INSERT INTO `zipkin_annotations` VALUES (0, -5656661044141199813, 1200834296849922203, 'ss', NULL, -1, 1638171883730681, -1062715903, NULL, NULL, 'fxz-server-system');
INSERT INTO `zipkin_annotations` VALUES (0, -5656661044141199813, 1200834296849922203, 'ca', 0x01, 0, 1638171883712085, 2130706433, NULL, -3481, '');
INSERT INTO `zipkin_annotations` VALUES (0, -5656661044141199813, 1200834296849922203, 'http.method', 0x474554, 6, 1638171883712085, -1062715903, NULL, NULL, 'fxz-server-system');
INSERT INTO `zipkin_annotations` VALUES (0, -5656661044141199813, 1200834296849922203, 'http.path', 0x2F68656C6C6F, 6, 1638171883712085, -1062715903, NULL, NULL, 'fxz-server-system');
INSERT INTO `zipkin_annotations` VALUES (0, -5656661044141199813, 1200834296849922203, 'mvc.controller.class', 0x54657374436F6E74726F6C6C6572, 6, 1638171883712085, -1062715903, NULL, NULL, 'fxz-server-system');
INSERT INTO `zipkin_annotations` VALUES (0, -5656661044141199813, 1200834296849922203, 'mvc.controller.method', 0x68656C6C6F, 6, 1638171883712085, -1062715903, NULL, NULL, 'fxz-server-system');
INSERT INTO `zipkin_annotations` VALUES (0, 8582406937137518271, 8109846340536009304, 'cs', NULL, -1, 1638171885829877, -1062715903, NULL, NULL, 'fxz-server-system');
INSERT INTO `zipkin_annotations` VALUES (0, 8582406937137518271, 8109846340536009304, 'cr', NULL, -1, 1638171885841633, -1062715903, NULL, NULL, 'fxz-server-system');
INSERT INTO `zipkin_annotations` VALUES (0, 8582406937137518271, 8109846340536009304, 'http.method', 0x474554, 6, 1638171885829877, -1062715903, NULL, NULL, 'fxz-server-system');
INSERT INTO `zipkin_annotations` VALUES (0, 8582406937137518271, 8109846340536009304, 'http.path', 0x2F617574682F75736572, 6, 1638171885829877, -1062715903, NULL, NULL, 'fxz-server-system');
INSERT INTO `zipkin_annotations` VALUES (0, 8582406937137518271, 8582406937137518271, 'sr', NULL, -1, 1638171885828194, -1062715903, NULL, NULL, 'fxz-server-system');
INSERT INTO `zipkin_annotations` VALUES (0, 8582406937137518271, 8582406937137518271, 'ss', NULL, -1, 1638171886199373, -1062715903, NULL, NULL, 'fxz-server-system');
INSERT INTO `zipkin_annotations` VALUES (0, 8582406937137518271, 8582406937137518271, 'ca', 0x01, 0, 1638171885828194, NULL, 0x00000000000000000000000000000001, NULL, '');
INSERT INTO `zipkin_annotations` VALUES (0, 8582406937137518271, 8582406937137518271, 'http.method', 0x505554, 6, 1638171885828194, -1062715903, NULL, NULL, 'fxz-server-system');
INSERT INTO `zipkin_annotations` VALUES (0, 8582406937137518271, 8582406937137518271, 'http.path', 0x2F75736572, 6, 1638171885828194, -1062715903, NULL, NULL, 'fxz-server-system');
INSERT INTO `zipkin_annotations` VALUES (0, 8582406937137518271, 8582406937137518271, 'mvc.controller.class', 0x55736572436F6E74726F6C6C6572, 6, 1638171885828194, -1062715903, NULL, NULL, 'fxz-server-system');
INSERT INTO `zipkin_annotations` VALUES (0, 8582406937137518271, 8582406937137518271, 'mvc.controller.method', 0x75706461746555736572, 6, 1638171885828194, -1062715903, NULL, NULL, 'fxz-server-system');
INSERT INTO `zipkin_annotations` VALUES (0, -7001130729477240016, -7001130729477240016, 'lc', '', 6, 1638172016343000, -1062715903, NULL, NULL, 'fxz-server-system');
INSERT INTO `zipkin_annotations` VALUES (0, 5953620445964840203, 5953620445964840203, 'sr', NULL, -1, 1638172023984811, -1062715903, NULL, NULL, 'fxz-server-system');
INSERT INTO `zipkin_annotations` VALUES (0, 5953620445964840203, 5953620445964840203, 'ss', NULL, -1, 1638172023991105, -1062715903, NULL, NULL, 'fxz-server-system');
INSERT INTO `zipkin_annotations` VALUES (0, 5953620445964840203, 5953620445964840203, 'ca', 0x01, 0, 1638172023984811, -1062721535, NULL, -3318, '');
INSERT INTO `zipkin_annotations` VALUES (0, 5953620445964840203, 5953620445964840203, 'http.method', 0x474554, 6, 1638172023984811, -1062715903, NULL, NULL, 'fxz-server-system');
INSERT INTO `zipkin_annotations` VALUES (0, 5953620445964840203, 5953620445964840203, 'http.path', 0x2F6163747561746F72, 6, 1638172023984811, -1062715903, NULL, NULL, 'fxz-server-system');
INSERT INTO `zipkin_annotations` VALUES (0, 5555067615528282343, 5555067615528282343, 'lc', '', 6, 1638172027446000, -1062715903, NULL, NULL, 'fxz-server-system-test');
INSERT INTO `zipkin_annotations` VALUES (0, -6387757123946159492, -895604104681823013, 'cs', NULL, -1, 1638172033329855, -1062715903, NULL, NULL, 'fxz-server-system');
INSERT INTO `zipkin_annotations` VALUES (0, -6387757123946159492, -895604104681823013, 'cr', NULL, -1, 1638172033352121, -1062715903, NULL, NULL, 'fxz-server-system');
INSERT INTO `zipkin_annotations` VALUES (0, -6387757123946159492, -895604104681823013, 'http.method', 0x474554, 6, 1638172033329855, -1062715903, NULL, NULL, 'fxz-server-system');
INSERT INTO `zipkin_annotations` VALUES (0, -6387757123946159492, -895604104681823013, 'http.path', 0x2F617574682F75736572, 6, 1638172033329855, -1062715903, NULL, NULL, 'fxz-server-system');
INSERT INTO `zipkin_annotations` VALUES (0, -6387757123946159492, -6387757123946159492, 'sr', NULL, -1, 1638172033317109, -1062715903, NULL, NULL, 'fxz-server-system');
INSERT INTO `zipkin_annotations` VALUES (0, -6387757123946159492, -6387757123946159492, 'ss', NULL, -1, 1638172033668958, -1062715903, NULL, NULL, 'fxz-server-system');
INSERT INTO `zipkin_annotations` VALUES (0, -6387757123946159492, -6387757123946159492, 'ca', 0x01, 0, 1638172033317109, NULL, 0x00000000000000000000000000000001, NULL, '');
INSERT INTO `zipkin_annotations` VALUES (0, -6387757123946159492, -6387757123946159492, 'http.method', 0x505554, 6, 1638172033317109, -1062715903, NULL, NULL, 'fxz-server-system');
INSERT INTO `zipkin_annotations` VALUES (0, -6387757123946159492, -6387757123946159492, 'http.path', 0x2F75736572, 6, 1638172033317109, -1062715903, NULL, NULL, 'fxz-server-system');
INSERT INTO `zipkin_annotations` VALUES (0, -6387757123946159492, -6387757123946159492, 'mvc.controller.class', 0x55736572436F6E74726F6C6C6572, 6, 1638172033317109, -1062715903, NULL, NULL, 'fxz-server-system');
INSERT INTO `zipkin_annotations` VALUES (0, -6387757123946159492, -6387757123946159492, 'mvc.controller.method', 0x75706461746555736572, 6, 1638172033317109, -1062715903, NULL, NULL, 'fxz-server-system');
INSERT INTO `zipkin_annotations` VALUES (0, -5778378588885136564, -6751089193175309411, 'cs', NULL, -1, 1638172034664994, -1062715903, NULL, NULL, 'fxz-server-system');
INSERT INTO `zipkin_annotations` VALUES (0, -5778378588885136564, -6751089193175309411, 'cr', NULL, -1, 1638172034677582, -1062715903, NULL, NULL, 'fxz-server-system');
INSERT INTO `zipkin_annotations` VALUES (0, -5778378588885136564, -6751089193175309411, 'http.method', 0x474554, 6, 1638172034664994, -1062715903, NULL, NULL, 'fxz-server-system');
INSERT INTO `zipkin_annotations` VALUES (0, -5778378588885136564, -6751089193175309411, 'http.path', 0x2F617574682F75736572, 6, 1638172034664994, -1062715903, NULL, NULL, 'fxz-server-system');
INSERT INTO `zipkin_annotations` VALUES (0, -5778378588885136564, -5778378588885136564, 'sr', NULL, -1, 1638172034664102, -1062715903, NULL, NULL, 'fxz-server-system');
INSERT INTO `zipkin_annotations` VALUES (0, -5778378588885136564, -5778378588885136564, 'ss', NULL, -1, 1638172034693000, -1062715903, NULL, NULL, 'fxz-server-system');
INSERT INTO `zipkin_annotations` VALUES (0, -5778378588885136564, -5778378588885136564, 'ca', 0x01, 0, 1638172034664102, NULL, 0x00000000000000000000000000000001, NULL, '');
INSERT INTO `zipkin_annotations` VALUES (0, -5778378588885136564, -5778378588885136564, 'http.method', 0x505554, 6, 1638172034664102, -1062715903, NULL, NULL, 'fxz-server-system');
INSERT INTO `zipkin_annotations` VALUES (0, -5778378588885136564, -5778378588885136564, 'http.path', 0x2F75736572, 6, 1638172034664102, -1062715903, NULL, NULL, 'fxz-server-system');
INSERT INTO `zipkin_annotations` VALUES (0, -5778378588885136564, -5778378588885136564, 'mvc.controller.class', 0x55736572436F6E74726F6C6C6572, 6, 1638172034664102, -1062715903, NULL, NULL, 'fxz-server-system');
INSERT INTO `zipkin_annotations` VALUES (0, -5778378588885136564, -5778378588885136564, 'mvc.controller.method', 0x75706461746555736572, 6, 1638172034664102, -1062715903, NULL, NULL, 'fxz-server-system');
INSERT INTO `zipkin_annotations` VALUES (0, -4279369022548599338, 4917283278434034164, 'cs', NULL, -1, 1638172037192059, -1062715903, NULL, NULL, 'fxz-server-system');
INSERT INTO `zipkin_annotations` VALUES (0, -4279369022548599338, 4917283278434034164, 'cr', NULL, -1, 1638172037200850, -1062715903, NULL, NULL, 'fxz-server-system');
INSERT INTO `zipkin_annotations` VALUES (0, -4279369022548599338, 4917283278434034164, 'http.method', 0x474554, 6, 1638172037192059, -1062715903, NULL, NULL, 'fxz-server-system');
INSERT INTO `zipkin_annotations` VALUES (0, -4279369022548599338, 4917283278434034164, 'http.path', 0x2F617574682F75736572, 6, 1638172037192059, -1062715903, NULL, NULL, 'fxz-server-system');
INSERT INTO `zipkin_annotations` VALUES (0, -4279369022548599338, 4872798174086814728, 'sr', NULL, -1, 1638172037191112, -1062715903, NULL, NULL, 'fxz-server-system');
INSERT INTO `zipkin_annotations` VALUES (0, -4279369022548599338, 4872798174086814728, 'ss', NULL, -1, 1638172037208005, -1062715903, NULL, NULL, 'fxz-server-system');
INSERT INTO `zipkin_annotations` VALUES (0, -4279369022548599338, 4872798174086814728, 'ca', 0x01, 0, 1638172037191112, 2130706433, NULL, -3264, '');
INSERT INTO `zipkin_annotations` VALUES (0, -4279369022548599338, 4872798174086814728, 'http.method', 0x474554, 6, 1638172037191112, -1062715903, NULL, NULL, 'fxz-server-system');
INSERT INTO `zipkin_annotations` VALUES (0, -4279369022548599338, 4872798174086814728, 'http.path', 0x2F68656C6C6F, 6, 1638172037191112, -1062715903, NULL, NULL, 'fxz-server-system');
INSERT INTO `zipkin_annotations` VALUES (0, -4279369022548599338, 4872798174086814728, 'mvc.controller.class', 0x54657374436F6E74726F6C6C6572, 6, 1638172037191112, -1062715903, NULL, NULL, 'fxz-server-system');
INSERT INTO `zipkin_annotations` VALUES (0, -4279369022548599338, 4872798174086814728, 'mvc.controller.method', 0x68656C6C6F, 6, 1638172037191112, -1062715903, NULL, NULL, 'fxz-server-system');
INSERT INTO `zipkin_annotations` VALUES (0, -4279369022548599338, 4623739235853618020, 'cs', NULL, -1, 1638172036801136, -1062715903, NULL, NULL, 'fxz-server-system-test');
INSERT INTO `zipkin_annotations` VALUES (0, -4279369022548599338, 4623739235853618020, 'cr', NULL, -1, 1638172036824833, -1062715903, NULL, NULL, 'fxz-server-system-test');
INSERT INTO `zipkin_annotations` VALUES (0, -4279369022548599338, 4623739235853618020, 'http.method', 0x474554, 6, 1638172036801136, -1062715903, NULL, NULL, 'fxz-server-system-test');
INSERT INTO `zipkin_annotations` VALUES (0, -4279369022548599338, 4623739235853618020, 'http.path', 0x2F617574682F75736572, 6, 1638172036801136, -1062715903, NULL, NULL, 'fxz-server-system-test');
INSERT INTO `zipkin_annotations` VALUES (0, -4279369022548599338, 4872798174086814728, 'cs', NULL, -1, 1638172037190102, -1062715903, NULL, NULL, 'fxz-server-system-test');
INSERT INTO `zipkin_annotations` VALUES (0, -4279369022548599338, 4872798174086814728, 'cr', NULL, -1, 1638172037212833, -1062715903, NULL, NULL, 'fxz-server-system-test');
INSERT INTO `zipkin_annotations` VALUES (0, -4279369022548599338, 4872798174086814728, 'http.method', 0x474554, 6, 1638172037190102, -1062715903, NULL, NULL, 'fxz-server-system-test');
INSERT INTO `zipkin_annotations` VALUES (0, -4279369022548599338, 4872798174086814728, 'http.path', 0x2F68656C6C6F, 6, 1638172037190102, -1062715903, NULL, NULL, 'fxz-server-system-test');
INSERT INTO `zipkin_annotations` VALUES (0, -4279369022548599338, -4279369022548599338, 'sr', NULL, -1, 1638172036775310, -1062715903, NULL, NULL, 'fxz-server-system-test');
INSERT INTO `zipkin_annotations` VALUES (0, -4279369022548599338, -4279369022548599338, 'ss', NULL, -1, 1638172037241653, -1062715903, NULL, NULL, 'fxz-server-system-test');
INSERT INTO `zipkin_annotations` VALUES (0, -4279369022548599338, -4279369022548599338, 'ca', 0x01, 0, 1638172036775310, NULL, 0x00000000000000000000000000000001, NULL, '');
INSERT INTO `zipkin_annotations` VALUES (0, -4279369022548599338, -4279369022548599338, 'http.method', 0x474554, 6, 1638172036775310, -1062715903, NULL, NULL, 'fxz-server-system-test');
INSERT INTO `zipkin_annotations` VALUES (0, -4279369022548599338, -4279369022548599338, 'http.path', 0x2F68656C6C6F, 6, 1638172036775310, -1062715903, NULL, NULL, 'fxz-server-system-test');
INSERT INTO `zipkin_annotations` VALUES (0, -4279369022548599338, -4279369022548599338, 'mvc.controller.class', 0x54657374436F6E74726F6C6C6572, 6, 1638172036775310, -1062715903, NULL, NULL, 'fxz-server-system-test');
INSERT INTO `zipkin_annotations` VALUES (0, -4279369022548599338, -4279369022548599338, 'mvc.controller.method', 0x68656C6C6F, 6, 1638172036775310, -1062715903, NULL, NULL, 'fxz-server-system-test');
INSERT INTO `zipkin_annotations` VALUES (0, 2161201663844554158, 2161201663844554158, 'sr', NULL, -1, 1638172042857098, -1062715903, NULL, NULL, 'fxz-server-system');
INSERT INTO `zipkin_annotations` VALUES (0, 2161201663844554158, 2161201663844554158, 'ss', NULL, -1, 1638172042859704, -1062715903, NULL, NULL, 'fxz-server-system');
INSERT INTO `zipkin_annotations` VALUES (0, 2161201663844554158, 2161201663844554158, 'ca', 0x01, 0, 1638172042857098, -1062721535, NULL, -3317, '');
INSERT INTO `zipkin_annotations` VALUES (0, 2161201663844554158, 2161201663844554158, 'http.method', 0x474554, 6, 1638172042857098, -1062715903, NULL, NULL, 'fxz-server-system');
INSERT INTO `zipkin_annotations` VALUES (0, 2161201663844554158, 2161201663844554158, 'http.path', 0x2F6163747561746F72, 6, 1638172042857098, -1062715903, NULL, NULL, 'fxz-server-system');
INSERT INTO `zipkin_annotations` VALUES (0, 9063703747363425883, 9063703747363425883, 'sr', NULL, -1, 1638172043861134, -1062715903, NULL, NULL, 'fxz-server-system-test');
INSERT INTO `zipkin_annotations` VALUES (0, 9063703747363425883, 9063703747363425883, 'ss', NULL, -1, 1638172043864526, -1062715903, NULL, NULL, 'fxz-server-system-test');
INSERT INTO `zipkin_annotations` VALUES (0, 9063703747363425883, 9063703747363425883, 'ca', 0x01, 0, 1638172043861134, -1062721535, NULL, -3257, '');
INSERT INTO `zipkin_annotations` VALUES (0, 9063703747363425883, 9063703747363425883, 'http.method', 0x474554, 6, 1638172043861134, -1062715903, NULL, NULL, 'fxz-server-system-test');
INSERT INTO `zipkin_annotations` VALUES (0, 9063703747363425883, 9063703747363425883, 'http.path', 0x2F6163747561746F72, 6, 1638172043861134, -1062715903, NULL, NULL, 'fxz-server-system-test');
INSERT INTO `zipkin_annotations` VALUES (0, 261522166580646438, 261522166580646438, 'sr', NULL, -1, 1638172053902101, -1062715903, NULL, NULL, 'fxz-server-system-test');
INSERT INTO `zipkin_annotations` VALUES (0, 261522166580646438, 261522166580646438, 'ss', NULL, -1, 1638172053904298, -1062715903, NULL, NULL, 'fxz-server-system-test');
INSERT INTO `zipkin_annotations` VALUES (0, 261522166580646438, 261522166580646438, 'ca', 0x01, 0, 1638172053902101, -1062721535, NULL, -3258, '');
INSERT INTO `zipkin_annotations` VALUES (0, 261522166580646438, 261522166580646438, 'http.method', 0x474554, 6, 1638172053902101, -1062715903, NULL, NULL, 'fxz-server-system-test');
INSERT INTO `zipkin_annotations` VALUES (0, 261522166580646438, 261522166580646438, 'http.path', 0x2F6163747561746F72, 6, 1638172053902101, -1062715903, NULL, NULL, 'fxz-server-system-test');
INSERT INTO `zipkin_annotations` VALUES (0, -5378421523613414783, 6868718754103691883, 'cs', NULL, -1, 1638172593497465, -1062715903, NULL, NULL, 'fxz-server-system');
INSERT INTO `zipkin_annotations` VALUES (0, -5378421523613414783, 6868718754103691883, 'cr', NULL, -1, 1638172593507775, -1062715903, NULL, NULL, 'fxz-server-system');
INSERT INTO `zipkin_annotations` VALUES (0, -5378421523613414783, 6868718754103691883, 'http.method', 0x474554, 6, 1638172593497465, -1062715903, NULL, NULL, 'fxz-server-system');
INSERT INTO `zipkin_annotations` VALUES (0, -5378421523613414783, 6868718754103691883, 'http.path', 0x2F617574682F75736572, 6, 1638172593497465, -1062715903, NULL, NULL, 'fxz-server-system');
INSERT INTO `zipkin_annotations` VALUES (0, -5378421523613414783, -5378421523613414783, 'sr', NULL, -1, 1638172593497064, -1062715903, NULL, NULL, 'fxz-server-system');
INSERT INTO `zipkin_annotations` VALUES (0, -5378421523613414783, -5378421523613414783, 'ss', NULL, -1, 1638172593525793, -1062715903, NULL, NULL, 'fxz-server-system');
INSERT INTO `zipkin_annotations` VALUES (0, -5378421523613414783, -5378421523613414783, 'ca', 0x01, 0, 1638172593497064, NULL, 0x00000000000000000000000000000001, NULL, '');
INSERT INTO `zipkin_annotations` VALUES (0, -5378421523613414783, -5378421523613414783, 'http.method', 0x505554, 6, 1638172593497064, -1062715903, NULL, NULL, 'fxz-server-system');
INSERT INTO `zipkin_annotations` VALUES (0, -5378421523613414783, -5378421523613414783, 'http.path', 0x2F75736572, 6, 1638172593497064, -1062715903, NULL, NULL, 'fxz-server-system');
INSERT INTO `zipkin_annotations` VALUES (0, -5378421523613414783, -5378421523613414783, 'mvc.controller.class', 0x55736572436F6E74726F6C6C6572, 6, 1638172593497064, -1062715903, NULL, NULL, 'fxz-server-system');
INSERT INTO `zipkin_annotations` VALUES (0, -5378421523613414783, -5378421523613414783, 'mvc.controller.method', 0x75706461746555736572, 6, 1638172593497064, -1062715903, NULL, NULL, 'fxz-server-system');
INSERT INTO `zipkin_annotations` VALUES (0, 3969593134840307654, 8375295979555635969, 'cs', NULL, -1, 1638172595384801, -1062715903, NULL, NULL, 'fxz-server-system');
INSERT INTO `zipkin_annotations` VALUES (0, 3969593134840307654, 8375295979555635969, 'cr', NULL, -1, 1638172595396921, -1062715903, NULL, NULL, 'fxz-server-system');
INSERT INTO `zipkin_annotations` VALUES (0, 3969593134840307654, 8375295979555635969, 'http.method', 0x474554, 6, 1638172595384801, -1062715903, NULL, NULL, 'fxz-server-system');
INSERT INTO `zipkin_annotations` VALUES (0, 3969593134840307654, 8375295979555635969, 'http.path', 0x2F617574682F75736572, 6, 1638172595384801, -1062715903, NULL, NULL, 'fxz-server-system');
INSERT INTO `zipkin_annotations` VALUES (0, 3969593134840307654, 3969593134840307654, 'sr', NULL, -1, 1638172595384081, -1062715903, NULL, NULL, 'fxz-server-system');
INSERT INTO `zipkin_annotations` VALUES (0, 3969593134840307654, 3969593134840307654, 'ss', NULL, -1, 1638172595464071, -1062715903, NULL, NULL, 'fxz-server-system');
INSERT INTO `zipkin_annotations` VALUES (0, 3969593134840307654, 3969593134840307654, 'ca', 0x01, 0, 1638172595384081, NULL, 0x00000000000000000000000000000001, NULL, '');
INSERT INTO `zipkin_annotations` VALUES (0, 3969593134840307654, 3969593134840307654, 'http.method', 0x474554, 6, 1638172595384081, -1062715903, NULL, NULL, 'fxz-server-system');
INSERT INTO `zipkin_annotations` VALUES (0, 3969593134840307654, 3969593134840307654, 'http.path', 0x2F75736572, 6, 1638172595384081, -1062715903, NULL, NULL, 'fxz-server-system');
INSERT INTO `zipkin_annotations` VALUES (0, 3969593134840307654, 3969593134840307654, 'mvc.controller.class', 0x55736572436F6E74726F6C6C6572, 6, 1638172595384081, -1062715903, NULL, NULL, 'fxz-server-system');
INSERT INTO `zipkin_annotations` VALUES (0, 3969593134840307654, 3969593134840307654, 'mvc.controller.method', 0x757365724C697374, 6, 1638172595384081, -1062715903, NULL, NULL, 'fxz-server-system');
INSERT INTO `zipkin_annotations` VALUES (0, -2442269197606816279, -2442269197606816279, 'lc', '', 6, 1638172780931000, -1062715903, NULL, NULL, 'fxz-server-system');
INSERT INTO `zipkin_annotations` VALUES (0, 1636879556027148046, 1636879556027148046, 'lc', '', 6, 1638172783690000, -1062715903, NULL, NULL, 'fxz-server-system-test');
INSERT INTO `zipkin_annotations` VALUES (0, 5267234743012679838, 5267234743012679838, 'sr', NULL, -1, 1638172807175193, -1062715903, NULL, NULL, 'fxz-server-system');
INSERT INTO `zipkin_annotations` VALUES (0, 5267234743012679838, 5267234743012679838, 'ss', NULL, -1, 1638172807183522, -1062715903, NULL, NULL, 'fxz-server-system');
INSERT INTO `zipkin_annotations` VALUES (0, 5267234743012679838, 5267234743012679838, 'ca', 0x01, 0, 1638172807175193, -1062721535, NULL, -2434, '');
INSERT INTO `zipkin_annotations` VALUES (0, 5267234743012679838, 5267234743012679838, 'http.method', 0x474554, 6, 1638172807175193, -1062715903, NULL, NULL, 'fxz-server-system');
INSERT INTO `zipkin_annotations` VALUES (0, 5267234743012679838, 5267234743012679838, 'http.path', 0x2F6163747561746F72, 6, 1638172807175193, -1062715903, NULL, NULL, 'fxz-server-system');
INSERT INTO `zipkin_annotations` VALUES (0, -5216487376956510447, -5216487376956510447, 'sr', NULL, -1, 1638172809976512, -1062715903, NULL, NULL, 'fxz-server-system-test');
INSERT INTO `zipkin_annotations` VALUES (0, -5216487376956510447, -5216487376956510447, 'ss', NULL, -1, 1638172809982302, -1062715903, NULL, NULL, 'fxz-server-system-test');
INSERT INTO `zipkin_annotations` VALUES (0, -5216487376956510447, -5216487376956510447, 'ca', 0x01, 0, 1638172809976512, -1062721535, NULL, -2431, '');
INSERT INTO `zipkin_annotations` VALUES (0, -5216487376956510447, -5216487376956510447, 'http.method', 0x474554, 6, 1638172809976512, -1062715903, NULL, NULL, 'fxz-server-system-test');
INSERT INTO `zipkin_annotations` VALUES (0, -5216487376956510447, -5216487376956510447, 'http.path', 0x2F6163747561746F72, 6, 1638172809976512, -1062715903, NULL, NULL, 'fxz-server-system-test');
INSERT INTO `zipkin_annotations` VALUES (0, 6121297254844789231, 6121297254844789231, 'sr', NULL, -1, 1638179258730900, -1062715903, NULL, NULL, 'fxz-server-system');
INSERT INTO `zipkin_annotations` VALUES (0, 6121297254844789231, 6121297254844789231, 'ss', NULL, -1, 1638179258736126, -1062715903, NULL, NULL, 'fxz-server-system');
INSERT INTO `zipkin_annotations` VALUES (0, 6121297254844789231, 6121297254844789231, 'ca', 0x01, 0, 1638179258730900, -1062721535, NULL, -7316, '');
INSERT INTO `zipkin_annotations` VALUES (0, 6121297254844789231, 6121297254844789231, 'http.method', 0x474554, 6, 1638179258730900, -1062715903, NULL, NULL, 'fxz-server-system');
INSERT INTO `zipkin_annotations` VALUES (0, 6121297254844789231, 6121297254844789231, 'http.path', 0x2F6163747561746F72, 6, 1638179258730900, -1062715903, NULL, NULL, 'fxz-server-system');
INSERT INTO `zipkin_annotations` VALUES (0, -4991943118875554767, -4991943118875554767, 'sr', NULL, -1, 1638179258834153, -1062715903, NULL, NULL, 'fxz-server-system-test');
INSERT INTO `zipkin_annotations` VALUES (0, -4991943118875554767, -4991943118875554767, 'ss', NULL, -1, 1638179258838428, -1062715903, NULL, NULL, 'fxz-server-system-test');
INSERT INTO `zipkin_annotations` VALUES (0, -4991943118875554767, -4991943118875554767, 'ca', 0x01, 0, 1638179258834153, -1062721535, NULL, -7309, '');
INSERT INTO `zipkin_annotations` VALUES (0, -4991943118875554767, -4991943118875554767, 'http.method', 0x474554, 6, 1638179258834153, -1062715903, NULL, NULL, 'fxz-server-system-test');
INSERT INTO `zipkin_annotations` VALUES (0, -4991943118875554767, -4991943118875554767, 'http.path', 0x2F6163747561746F72, 6, 1638179258834153, -1062715903, NULL, NULL, 'fxz-server-system-test');

-- ----------------------------
-- Table structure for zipkin_dependencies
-- ----------------------------
DROP TABLE IF EXISTS `zipkin_dependencies`;
CREATE TABLE `zipkin_dependencies`  (
  `day` date NOT NULL,
  `parent` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `child` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `call_count` bigint(20) NULL DEFAULT NULL,
  `error_count` bigint(20) NULL DEFAULT NULL,
  PRIMARY KEY (`day`, `parent`, `child`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Compressed;

-- ----------------------------
-- Records of zipkin_dependencies
-- ----------------------------

-- ----------------------------
-- Table structure for zipkin_spans
-- ----------------------------
DROP TABLE IF EXISTS `zipkin_spans`;
CREATE TABLE `zipkin_spans`  (
  `trace_id_high` bigint(20) NOT NULL DEFAULT 0 COMMENT 'If non zero, this means the trace uses 128 bit traceIds instead of 64 bit',
  `trace_id` bigint(20) NOT NULL,
  `id` bigint(20) NOT NULL,
  `name` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `remote_service_name` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `parent_id` bigint(20) NULL DEFAULT NULL,
  `debug` bit(1) NULL DEFAULT NULL,
  `start_ts` bigint(20) NULL DEFAULT NULL COMMENT 'Span.timestamp(): epoch micros used for endTs query and to implement TTL',
  `duration` bigint(20) NULL DEFAULT NULL COMMENT 'Span.duration(): micros used for minDuration and maxDuration query',
  PRIMARY KEY (`trace_id_high`, `trace_id`, `id`) USING BTREE,
  INDEX `trace_id_high`(`trace_id_high`, `trace_id`) USING BTREE COMMENT 'for getTracesByIds',
  INDEX `name`(`name`) USING BTREE COMMENT 'for getTraces and getSpanNames',
  INDEX `remote_service_name`(`remote_service_name`) USING BTREE COMMENT 'for getTraces and getRemoteServiceNames',
  INDEX `start_ts`(`start_ts`) USING BTREE COMMENT 'for getTraces ordering and range'
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Compressed;

-- ----------------------------
-- Records of zipkin_spans
-- ----------------------------
INSERT INTO `zipkin_spans` VALUES (0, -8025310126578137703, -8025310126578137703, 'get', '', NULL, NULL, 1638169875020435, 6187);
INSERT INTO `zipkin_spans` VALUES (0, -7881802119374865842, -7881802119374865842, 'async', '', NULL, NULL, 1638169867805000, 697619);
INSERT INTO `zipkin_spans` VALUES (0, -7606622751437803094, -7606622751437803094, 'get', '', NULL, NULL, 1638169892123160, 3264);
INSERT INTO `zipkin_spans` VALUES (0, -7001130729477240016, -7001130729477240016, 'async', '', NULL, NULL, 1638172016343000, 691219);
INSERT INTO `zipkin_spans` VALUES (0, -6387757123946159492, -6387757123946159492, 'put /user', '', NULL, NULL, 1638172033317109, 351849);
INSERT INTO `zipkin_spans` VALUES (0, -6387757123946159492, -895604104681823013, 'get', '', -6387757123946159492, NULL, 1638172033329855, 22266);
INSERT INTO `zipkin_spans` VALUES (0, -6145841007590615249, -6145841007590615249, 'get', '', NULL, NULL, 1638169875010503, 5037);
INSERT INTO `zipkin_spans` VALUES (0, -5778378588885136564, -6751089193175309411, 'get', '', -5778378588885136564, NULL, 1638172034664994, 12588);
INSERT INTO `zipkin_spans` VALUES (0, -5778378588885136564, -5778378588885136564, 'put /user', '', NULL, NULL, 1638172034664102, 28898);
INSERT INTO `zipkin_spans` VALUES (0, -5656661044141199813, -8489855318435819779, 'get', '', 1200834296849922203, NULL, 1638171883712031, 13730);
INSERT INTO `zipkin_spans` VALUES (0, -5656661044141199813, -6092636913825039442, 'get', '', -5656661044141199813, NULL, 1638171883694734, 12953);
INSERT INTO `zipkin_spans` VALUES (0, -5656661044141199813, -5656661044141199813, 'get /hello', '', NULL, NULL, 1638171883694087, 37642);
INSERT INTO `zipkin_spans` VALUES (0, -5656661044141199813, 1200834296849922203, 'get /hello', '', -5656661044141199813, NULL, 1638171883710609, 19769);
INSERT INTO `zipkin_spans` VALUES (0, -5378421523613414783, -5378421523613414783, 'put /user', '', NULL, NULL, 1638172593497064, 28729);
INSERT INTO `zipkin_spans` VALUES (0, -5378421523613414783, 6868718754103691883, 'get', '', -5378421523613414783, NULL, 1638172593497465, 10310);
INSERT INTO `zipkin_spans` VALUES (0, -5328535706363524248, -8520234267970190352, 'get', '', -5328535706363524248, NULL, 1638169939517799, 80684);
INSERT INTO `zipkin_spans` VALUES (0, -5328535706363524248, -6951861956747260916, 'get', '', -5328535706363524248, NULL, 1638169938987665, 62254);
INSERT INTO `zipkin_spans` VALUES (0, -5328535706363524248, -5328535706363524248, 'get /hello', '', NULL, NULL, 1638169938984125, 627117);
INSERT INTO `zipkin_spans` VALUES (0, -5328535706363524248, 3225804995639601053, 'get', '', -8520234267970190352, NULL, 1638169939525241, 48166);
INSERT INTO `zipkin_spans` VALUES (0, -5216487376956510447, -5216487376956510447, 'get', '', NULL, NULL, 1638172809976512, 5790);
INSERT INTO `zipkin_spans` VALUES (0, -4991943118875554767, -4991943118875554767, 'get', '', NULL, NULL, 1638179258834153, 4275);
INSERT INTO `zipkin_spans` VALUES (0, -4279369022548599338, -4279369022548599338, 'get /hello', '', NULL, NULL, 1638172036775310, 466343);
INSERT INTO `zipkin_spans` VALUES (0, -4279369022548599338, 4623739235853618020, 'get', '', -4279369022548599338, NULL, 1638172036801136, 23697);
INSERT INTO `zipkin_spans` VALUES (0, -4279369022548599338, 4872798174086814728, 'get', '', -4279369022548599338, NULL, 1638172037190102, 22731);
INSERT INTO `zipkin_spans` VALUES (0, -4279369022548599338, 4917283278434034164, 'get', '', 4872798174086814728, NULL, 1638172037192059, 8791);
INSERT INTO `zipkin_spans` VALUES (0, -2566181379648875080, -2566181379648875080, 'async', '', NULL, NULL, 1638169865802000, 848862);
INSERT INTO `zipkin_spans` VALUES (0, -2442269197606816279, -2442269197606816279, 'async', '', NULL, NULL, 1638172780931000, 974437);
INSERT INTO `zipkin_spans` VALUES (0, -832826882313434463, -832826882313434463, 'async', '', NULL, NULL, 1638171416873000, 702409);
INSERT INTO `zipkin_spans` VALUES (0, 261522166580646438, 261522166580646438, 'get', '', NULL, NULL, 1638172053902101, 2197);
INSERT INTO `zipkin_spans` VALUES (0, 1636879556027148046, 1636879556027148046, 'async', '', NULL, NULL, 1638172783690000, 731652);
INSERT INTO `zipkin_spans` VALUES (0, 2161201663844554158, 2161201663844554158, 'get', '', NULL, NULL, 1638172042857098, 2606);
INSERT INTO `zipkin_spans` VALUES (0, 3021340501933078410, 3021340501933078410, 'async', '', NULL, NULL, 1638171406675000, 1077855);
INSERT INTO `zipkin_spans` VALUES (0, 3592737260128299163, -2874851072035824594, 'get', '', 1198070489411063301, NULL, 1638171535303270, 22753);
INSERT INTO `zipkin_spans` VALUES (0, 3592737260128299163, 1198070489411063301, 'get /hello', '', 3592737260128299163, NULL, 1638171535296297, 55063);
INSERT INTO `zipkin_spans` VALUES (0, 3592737260128299163, 3592737260128299163, 'get /hello', '', NULL, NULL, 1638171534839111, 522789);
INSERT INTO `zipkin_spans` VALUES (0, 3592737260128299163, 8028521047333011932, 'get', '', 3592737260128299163, NULL, 1638171534842575, 95198);
INSERT INTO `zipkin_spans` VALUES (0, 3831019749791502824, 3831019749791502824, 'get', '', NULL, NULL, 1638171433055254, 8082);
INSERT INTO `zipkin_spans` VALUES (0, 3969593134840307654, 3969593134840307654, 'get /user', '', NULL, NULL, 1638172595384081, 79990);
INSERT INTO `zipkin_spans` VALUES (0, 3969593134840307654, 8375295979555635969, 'get', '', 3969593134840307654, NULL, 1638172595384801, 12120);
INSERT INTO `zipkin_spans` VALUES (0, 4592102201703596623, 4592102201703596623, 'get', '', NULL, NULL, 1638169894232100, 2327);
INSERT INTO `zipkin_spans` VALUES (0, 5267234743012679838, 5267234743012679838, 'get', '', NULL, NULL, 1638172807175193, 8329);
INSERT INTO `zipkin_spans` VALUES (0, 5555067615528282343, 5555067615528282343, 'async', '', NULL, NULL, 1638172027446000, 730540);
INSERT INTO `zipkin_spans` VALUES (0, 5953620445964840203, 5953620445964840203, 'get', '', NULL, NULL, 1638172023984811, 6294);
INSERT INTO `zipkin_spans` VALUES (0, 6121297254844789231, 6121297254844789231, 'get', '', NULL, NULL, 1638179258730900, 5226);
INSERT INTO `zipkin_spans` VALUES (0, 6953175459323808214, 6953175459323808214, 'get', '', NULL, NULL, 1638171443324234, 7322);
INSERT INTO `zipkin_spans` VALUES (0, 8582406937137518271, 8109846340536009304, 'get', '', 8582406937137518271, NULL, 1638171885829877, 11756);
INSERT INTO `zipkin_spans` VALUES (0, 8582406937137518271, 8582406937137518271, 'put /user', '', NULL, NULL, 1638171885828194, 371179);
INSERT INTO `zipkin_spans` VALUES (0, 9063703747363425883, 9063703747363425883, 'get', '', NULL, NULL, 1638172043861134, 3392);

SET FOREIGN_KEY_CHECKS = 1;
