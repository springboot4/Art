/*
 Navicat Premium Data Transfer

 Source Server         : 127.0.0.1
 Source Server Type    : MySQL
 Source Server Version : 80032
 Source Host           : 127.0.0.1:3306
 Source Schema         : art_base

 Target Server Type    : MySQL
 Target Server Version : 80032
 File Encoding         : 65001

 Date: 01/07/2023 15:31:05
*/
USE art_base;

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for sys_app
-- ----------------------------
DROP TABLE IF EXISTS `sys_app`;
CREATE TABLE `sys_app` (
  `id` bigint NOT NULL COMMENT '主键id',
  `name` varchar(100) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NOT NULL COMMENT '应用名称',
  `code` varchar(50) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NOT NULL COMMENT '应用编码',
  `sort` int DEFAULT '0' COMMENT '排序',
  `icon` varchar(50) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci DEFAULT NULL COMMENT '图标',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `create_by` varchar(100) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci DEFAULT NULL COMMENT '创建人',
  `update_time` datetime DEFAULT NULL COMMENT '修改时间',
  `update_by` varchar(100) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci DEFAULT NULL COMMENT '修改人',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3 COMMENT='系统应用表';

-- ----------------------------
-- Records of sys_app
-- ----------------------------
BEGIN;
INSERT INTO `sys_app` (`id`, `name`, `code`, `sort`, `icon`, `create_time`, `create_by`, `update_time`, `update_by`) VALUES (1, '系统应用', 'system', 1, 'laptop', '2022-09-12 00:17:28', 'fxz', '2023-05-01 13:22:26', 'fxz');
COMMIT;

-- ----------------------------
-- Table structure for sys_dept
-- ----------------------------
DROP TABLE IF EXISTS `sys_dept`;
CREATE TABLE `sys_dept` (
  `dept_id` bigint NOT NULL COMMENT '部门ID',
  `parent_id` bigint NOT NULL COMMENT '上级部门ID',
  `dept_name` varchar(100) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NOT NULL COMMENT '部门名称',
  `order_num` double(20,0) DEFAULT NULL COMMENT '排序',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime DEFAULT NULL COMMENT '修改时间',
  `create_by` varchar(100) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci DEFAULT NULL COMMENT '创建人',
  `update_by` varchar(100) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci DEFAULT NULL COMMENT '更新人',
  PRIMARY KEY (`dept_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3 COMMENT='部门表';

-- ----------------------------
-- Records of sys_dept
-- ----------------------------
BEGIN;
INSERT INTO `sys_dept` (`dept_id`, `parent_id`, `dept_name`, `order_num`, `create_time`, `update_time`, `create_by`, `update_by`) VALUES (0, -1, '清华大学', 0, '2022-02-28 16:42:49', '2023-02-22 14:15:23', NULL, 'fxz');
INSERT INTO `sys_dept` (`dept_id`, `parent_id`, `dept_name`, `order_num`, `create_time`, `update_time`, `create_by`, `update_by`) VALUES (1, 0, '物电学院', 0, '2022-02-28 16:43:53', '2023-02-22 14:15:57', NULL, 'fxz');
INSERT INTO `sys_dept` (`dept_id`, `parent_id`, `dept_name`, `order_num`, `create_time`, `update_time`, `create_by`, `update_by`) VALUES (2, 0, '计算机学院', -1, '2022-02-28 16:43:10', '2023-02-22 14:15:39', NULL, 'fxz');
INSERT INTO `sys_dept` (`dept_id`, `parent_id`, `dept_name`, `order_num`, `create_time`, `update_time`, `create_by`, `update_by`) VALUES (3, 2, '教研组', 0, '2022-02-28 16:43:27', '2023-02-22 14:16:35', NULL, 'fxz');
INSERT INTO `sys_dept` (`dept_id`, `parent_id`, `dept_name`, `order_num`, `create_time`, `update_time`, `create_by`, `update_by`) VALUES (4, 1, '教务处', 1, '2022-02-28 16:44:29', '2023-02-22 14:17:30', NULL, 'fxz');
INSERT INTO `sys_dept` (`dept_id`, `parent_id`, `dept_name`, `order_num`, `create_time`, `update_time`, `create_by`, `update_by`) VALUES (5, 1, '教研组', 0, '2022-02-28 16:44:41', '2023-02-22 14:17:19', NULL, 'fxz');
INSERT INTO `sys_dept` (`dept_id`, `parent_id`, `dept_name`, `order_num`, `create_time`, `update_time`, `create_by`, `update_by`) VALUES (9, 2, '教务处', 1, '2023-02-22 14:17:02', '2023-02-22 14:17:02', 'fxz', 'fxz');
INSERT INTO `sys_dept` (`dept_id`, `parent_id`, `dept_name`, `order_num`, `create_time`, `update_time`, `create_by`, `update_by`) VALUES (11, 1, '通信工程', 3, '2023-02-22 14:28:08', '2023-02-22 14:28:08', 'fxz', 'fxz');
COMMIT;

-- ----------------------------
-- Table structure for sys_dict
-- ----------------------------
DROP TABLE IF EXISTS `sys_dict`;
CREATE TABLE `sys_dict` (
  `id` bigint NOT NULL,
  `type` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '类型',
  `description` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '描述',
  `remark` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '备注',
  `system_flag` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT '0' COMMENT '是否是系统内置',
  `del_flag` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT '0' COMMENT '删除标记',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `create_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '创建人',
  `update_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '更新人',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE,
  KEY `sys_dict_del_flag` (`del_flag`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='字典表';

-- ----------------------------
-- Records of sys_dict
-- ----------------------------
BEGIN;
INSERT INTO `sys_dict` (`id`, `type`, `description`, `remark`, `system_flag`, `del_flag`, `create_time`, `create_by`, `update_by`, `update_time`) VALUES (1510865432566308866, 'dict_type', '字典类型', '字典类型', '1', '0', '2022-04-04 14:22:44', 'fxz', 'fxz', '2022-04-04 14:22:44');
INSERT INTO `sys_dict` (`id`, `type`, `description`, `remark`, `system_flag`, `del_flag`, `create_time`, `create_by`, `update_by`, `update_time`) VALUES (1517894240063803393, 'data_permission_type', '数据权限', '数据权限', '1', '0', '2022-04-23 23:52:43', 'fxz', 'fxz', '2022-04-23 23:52:43');
INSERT INTO `sys_dict` (`id`, `type`, `description`, `remark`, `system_flag`, `del_flag`, `create_time`, `create_by`, `update_by`, `update_time`) VALUES (1568577687467589633, 'sex_type', '性别', '性别', '1', '0', '2022-09-10 20:30:38', 'fxz', 'fxz', '2022-09-10 20:30:38');
COMMIT;

-- ----------------------------
-- Table structure for sys_dict_item
-- ----------------------------
DROP TABLE IF EXISTS `sys_dict_item`;
CREATE TABLE `sys_dict_item` (
  `id` bigint NOT NULL,
  `dict_id` bigint NOT NULL COMMENT '字典ID',
  `value` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '值',
  `label` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '标签',
  `type` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '字典类型',
  `description` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '描述',
  `sort_order` int NOT NULL DEFAULT '0' COMMENT '排序（升序）',
  `remark` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT ' ' COMMENT '备注',
  `del_flag` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT '0' COMMENT '删除标记',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `create_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '创建人',
  `update_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '修改人',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE,
  KEY `sys_dict_value` (`value`) USING BTREE,
  KEY `sys_dict_label` (`label`) USING BTREE,
  KEY `sys_dict_del_flag` (`del_flag`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='字典项';

-- ----------------------------
-- Records of sys_dict_item
-- ----------------------------
BEGIN;
INSERT INTO `sys_dict_item` (`id`, `dict_id`, `value`, `label`, `type`, `description`, `sort_order`, `remark`, `del_flag`, `create_time`, `create_by`, `update_by`, `update_time`) VALUES (1510865497015984129, 1510865432566308866, '1', '系统类', 'dict_type', '系统类', 1, ' ', '0', '2022-04-04 14:23:00', 'fxz', 'fxz', '2022-04-04 14:23:25');
INSERT INTO `sys_dict_item` (`id`, `dict_id`, `value`, `label`, `type`, `description`, `sort_order`, `remark`, `del_flag`, `create_time`, `create_by`, `update_by`, `update_time`) VALUES (1510865563508285441, 1510865432566308866, '0', '业务类', 'dict_type', '业务类', 0, ' ', '0', '2022-04-04 14:23:16', 'fxz', 'fxz', '2022-04-04 14:23:16');
INSERT INTO `sys_dict_item` (`id`, `dict_id`, `value`, `label`, `type`, `description`, `sort_order`, `remark`, `del_flag`, `create_time`, `create_by`, `update_by`, `update_time`) VALUES (1517894931306074113, 1517894240063803393, '1', '全部数据权限', 'data_permission_type', '全部数据权限', 1, ' ', '0', '2022-04-23 23:55:27', 'fxz', 'fxz', '2022-04-23 23:55:27');
INSERT INTO `sys_dict_item` (`id`, `dict_id`, `value`, `label`, `type`, `description`, `sort_order`, `remark`, `del_flag`, `create_time`, `create_by`, `update_by`, `update_time`) VALUES (1517895362191118337, 1517894240063803393, '2', '指定部门数据权限', 'data_permission_type', '指定部门数据权限', 2, ' ', '0', '2022-04-23 23:57:10', 'fxz', 'fxz', '2022-04-23 23:55:27');
INSERT INTO `sys_dict_item` (`id`, `dict_id`, `value`, `label`, `type`, `description`, `sort_order`, `remark`, `del_flag`, `create_time`, `create_by`, `update_by`, `update_time`) VALUES (1517895581628715009, 1517894240063803393, '3', '部门数据权限', 'data_permission_type', '部门数据权限', 3, ' ', '0', '2022-04-23 23:58:02', 'fxz', 'fxz', '2022-04-23 23:55:27');
INSERT INTO `sys_dict_item` (`id`, `dict_id`, `value`, `label`, `type`, `description`, `sort_order`, `remark`, `del_flag`, `create_time`, `create_by`, `update_by`, `update_time`) VALUES (1517895701451591682, 1517894240063803393, '4', '部门及以下数据权限', 'data_permission_type', '部门及以下数据权限', 4, ' ', '0', '2022-04-23 23:58:31', 'fxz', 'fxz', '2022-04-23 23:55:27');
INSERT INTO `sys_dict_item` (`id`, `dict_id`, `value`, `label`, `type`, `description`, `sort_order`, `remark`, `del_flag`, `create_time`, `create_by`, `update_by`, `update_time`) VALUES (1517895786809872385, 1517894240063803393, '5', '仅本人数据权限', 'data_permission_type', '仅本人数据权限', 5, ' ', '0', '2022-04-23 23:58:51', 'fxz', 'fxz', '2022-04-23 23:55:27');
INSERT INTO `sys_dict_item` (`id`, `dict_id`, `value`, `label`, `type`, `description`, `sort_order`, `remark`, `del_flag`, `create_time`, `create_by`, `update_by`, `update_time`) VALUES (1568577818589921282, 1568577687467589633, '0', '男', 'sex_type', '男性', 0, ' ', '0', '2022-09-10 20:31:09', 'fxz', 'fxz', '2022-09-10 20:31:09');
INSERT INTO `sys_dict_item` (`id`, `dict_id`, `value`, `label`, `type`, `description`, `sort_order`, `remark`, `del_flag`, `create_time`, `create_by`, `update_by`, `update_time`) VALUES (1568577849510330369, 1568577687467589633, '1', '女', 'sex_type', '女性', 0, ' ', '0', '2022-09-10 20:31:17', 'fxz', 'fxz', '2022-09-10 20:31:17');
COMMIT;

-- ----------------------------
-- Table structure for sys_file
-- ----------------------------
DROP TABLE IF EXISTS `sys_file`;
CREATE TABLE `sys_file` (
  `id` bigint NOT NULL,
  `file_name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL,
  `bucket_name` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL,
  `original` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL,
  `type` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL,
  `file_size` bigint DEFAULT NULL COMMENT '文件大小',
  `del_flag` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT '0' COMMENT '0-正常，1-删除',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime DEFAULT NULL COMMENT '修改时间',
  `create_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '创建者',
  `update_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '更新人',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='文件管理表';

-- ----------------------------
-- Records of sys_file
-- ----------------------------
BEGIN;
COMMIT;

-- ----------------------------
-- Table structure for sys_menu
-- ----------------------------
DROP TABLE IF EXISTS `sys_menu`;
CREATE TABLE `sys_menu` (
  `id` bigint NOT NULL COMMENT '菜单/按钮ID',
  `parent_id` bigint NOT NULL COMMENT '上级菜单ID',
  `title` varchar(20) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci DEFAULT NULL COMMENT 'title',
  `name` varchar(50) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NOT NULL COMMENT '菜单/按钮名称',
  `perms` varchar(50) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci DEFAULT NULL COMMENT '权限标识(多个用逗号分隔，如：user:list,user:create)',
  `type` char(2) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NOT NULL COMMENT '类型 0菜单 1按钮',
  `component` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci DEFAULT NULL COMMENT '对应路由组件component',
  `path` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci DEFAULT NULL COMMENT '对应路由path',
  `redirect` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci DEFAULT NULL COMMENT '重定向',
  `icon` varchar(50) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci DEFAULT NULL COMMENT '图标',
  `keep_alive` tinyint DEFAULT '1' COMMENT '是否缓存 0:否 1:是',
  `order_num` double(20,0) DEFAULT NULL COMMENT '排序',
  `create_time` datetime NOT NULL COMMENT '创建时间',
  `update_time` datetime DEFAULT NULL COMMENT '修改时间',
  `create_by` varchar(100) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci DEFAULT NULL COMMENT '创建人',
  `update_by` varchar(100) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci DEFAULT NULL COMMENT '更新人',
  `hidden` char(10) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci DEFAULT '0' COMMENT '是否隐藏(1 隐藏 0 不隐藏)',
  `application` bigint DEFAULT NULL COMMENT '应用id',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3 COMMENT='菜单表';

-- ----------------------------
-- Records of sys_menu
-- ----------------------------
BEGIN;
INSERT INTO `sys_menu` (`id`, `parent_id`, `title`, `name`, `perms`, `type`, `component`, `path`, `redirect`, `icon`, `keep_alive`, `order_num`, `create_time`, `update_time`, `create_by`, `update_by`, `hidden`, `application`) VALUES (4, 0, '个人页', 'account', 'user', '0', 'RouteView', '/account', '', 'ant-design:user-outlined', 1, 2, '2022-01-23 17:04:07', '2023-05-04 18:02:09', NULL, 'fxz', '1', 1);
INSERT INTO `sys_menu` (`id`, `parent_id`, `title`, `name`, `perms`, `type`, `component`, `path`, `redirect`, `icon`, `keep_alive`, `order_num`, `create_time`, `update_time`, `create_by`, `update_by`, `hidden`, `application`) VALUES (5, 4, '个人中心', 'center', 'user', '0', 'account/center', '/account/center', NULL, NULL, 1, 2, '2022-01-23 17:04:58', '2023-05-04 18:02:09', NULL, 'fxz', NULL, 1);
INSERT INTO `sys_menu` (`id`, `parent_id`, `title`, `name`, `perms`, `type`, `component`, `path`, `redirect`, `icon`, `keep_alive`, `order_num`, `create_time`, `update_time`, `create_by`, `update_by`, `hidden`, `application`) VALUES (6, 4, '个人设置', 'AccountSettings', 'user', '0', 'sys/account/setting/index.vue', '/account/settings', NULL, NULL, 1, 2, '2022-01-18 14:44:25', '2023-05-05 18:48:52', NULL, 'fxz', '1', 1);
INSERT INTO `sys_menu` (`id`, `parent_id`, `title`, `name`, `perms`, `type`, `component`, `path`, `redirect`, `icon`, `keep_alive`, `order_num`, `create_time`, `update_time`, `create_by`, `update_by`, `hidden`, `application`) VALUES (7, 6, '基本设置', 'BaseSettings', 'user', '0', 'account/settings/BaseSetting', '/account/settings/base', NULL, NULL, 1, 2, '2022-01-18 14:48:57', '2023-05-05 18:48:52', NULL, 'fxz', '0', 1);
INSERT INTO `sys_menu` (`id`, `parent_id`, `title`, `name`, `perms`, `type`, `component`, `path`, `redirect`, `icon`, `keep_alive`, `order_num`, `create_time`, `update_time`, `create_by`, `update_by`, `hidden`, `application`) VALUES (8, 6, '安全设置', 'SecuritySettings', 'user', '0', 'account/settings/Security', '/account/settings/security', NULL, NULL, 1, 2, '2022-01-18 14:49:39', '2023-05-05 18:48:52', NULL, 'fxz', NULL, 1);
INSERT INTO `sys_menu` (`id`, `parent_id`, `title`, `name`, `perms`, `type`, `component`, `path`, `redirect`, `icon`, `keep_alive`, `order_num`, `create_time`, `update_time`, `create_by`, `update_by`, `hidden`, `application`) VALUES (9, 6, '个性化设置', 'CustomSettings', 'user', '0', 'account/settings/Custom', '/account/settings/custom', NULL, NULL, 1, 2, '2022-01-18 14:50:17', '2023-05-05 18:48:52', NULL, 'fxz', NULL, 1);
INSERT INTO `sys_menu` (`id`, `parent_id`, `title`, `name`, `perms`, `type`, `component`, `path`, `redirect`, `icon`, `keep_alive`, `order_num`, `create_time`, `update_time`, `create_by`, `update_by`, `hidden`, `application`) VALUES (10, 6, '账户绑定', 'BindingSettings', 'user', '0', 'account/settings/Binding', '/account/settings/binding', NULL, NULL, 1, 2, '2022-01-18 14:50:58', '2023-05-05 18:48:52', NULL, 'fxz', NULL, 1);
INSERT INTO `sys_menu` (`id`, `parent_id`, `title`, `name`, `perms`, `type`, `component`, `path`, `redirect`, `icon`, `keep_alive`, `order_num`, `create_time`, `update_time`, `create_by`, `update_by`, `hidden`, `application`) VALUES (11, 6, '新消息通知', 'NotificationSettings', 'user', '0', 'account/settings/Notification', '/account/settings/notification', NULL, NULL, 1, 2, '2022-01-18 14:51:31', '2023-05-05 18:48:52', NULL, 'fxz', NULL, 1);
INSERT INTO `sys_menu` (`id`, `parent_id`, `title`, `name`, `perms`, `type`, `component`, `path`, `redirect`, `icon`, `keep_alive`, `order_num`, `create_time`, `update_time`, `create_by`, `update_by`, `hidden`, `application`) VALUES (12, 0, '首页', 'welcome', 'welcome', '0', 'analysis', '/welcome', '', 'ant-design:aliwangwang-filled', 1, 0, '2022-01-18 16:22:11', '2023-05-06 08:58:36', NULL, 'fxz', '1', 1);
INSERT INTO `sys_menu` (`id`, `parent_id`, `title`, `name`, `perms`, `type`, `component`, `path`, `redirect`, `icon`, `keep_alive`, `order_num`, `create_time`, `update_time`, `create_by`, `update_by`, `hidden`, `application`) VALUES (20, 0, '权限管理', 'permissions', 'sys', '0', 'RouteView', '/permissions', NULL, 'ant-design:folder-open-filled', 1, 3, '2022-01-23 16:52:53', '2023-05-04 17:53:52', NULL, 'fxz', '0', 1);
INSERT INTO `sys_menu` (`id`, `parent_id`, `title`, `name`, `perms`, `type`, `component`, `path`, `redirect`, `icon`, `keep_alive`, `order_num`, `create_time`, `update_time`, `create_by`, `update_by`, `hidden`, `application`) VALUES (21, 20, '菜单管理', 'Menu', 'sys:menu', '0', 'system/menu/menu.vue', '/permissions/menu', NULL, 'ant-design:menu-unfold-outlined', 1, 0, '2022-01-23 16:55:48', '2023-05-04 17:54:08', NULL, 'fxz', '0', 1);
INSERT INTO `sys_menu` (`id`, `parent_id`, `title`, `name`, `perms`, `type`, `component`, `path`, `redirect`, `icon`, `keep_alive`, `order_num`, `create_time`, `update_time`, `create_by`, `update_by`, `hidden`, `application`) VALUES (29, 21, '添加菜单', 'saveMenu', 'sys:menu:save', '1', NULL, NULL, NULL, NULL, 1, 1, '2022-01-23 18:16:31', '2023-05-04 17:54:08', NULL, 'fxz', '1', 1);
INSERT INTO `sys_menu` (`id`, `parent_id`, `title`, `name`, `perms`, `type`, `component`, `path`, `redirect`, `icon`, `keep_alive`, `order_num`, `create_time`, `update_time`, `create_by`, `update_by`, `hidden`, `application`) VALUES (100006, 20, '用户管理', 'UserList', 'sys:user', '0', 'system/user/userList.vue', '/permissions/user', NULL, 'ant-design:user-outlined', 1, 1, '2022-02-27 09:35:00', '2023-05-04 17:54:26', NULL, 'fxz', '0', 1);
INSERT INTO `sys_menu` (`id`, `parent_id`, `title`, `name`, `perms`, `type`, `component`, `path`, `redirect`, `icon`, `keep_alive`, `order_num`, `create_time`, `update_time`, `create_by`, `update_by`, `hidden`, `application`) VALUES (100007, 20, '角色管理', 'RoleList', 'sys:role', '0', 'system/role/RoleList.vue', '/permissions/role', NULL, 'ant-design:android-outlined', 1, 2, '2022-02-27 09:43:02', '2023-05-04 17:54:51', NULL, 'fxz', '0', 1);
INSERT INTO `sys_menu` (`id`, `parent_id`, `title`, `name`, `perms`, `type`, `component`, `path`, `redirect`, `icon`, `keep_alive`, `order_num`, `create_time`, `update_time`, `create_by`, `update_by`, `hidden`, `application`) VALUES (100008, 20, '部门管理', 'DeptList', 'dept', '0', 'system/dept/DeptList.vue', '/permissions/dept', NULL, 'ant-design:apartment-outlined', 1, 3, '2022-02-27 10:15:46', '2023-05-04 17:55:12', NULL, 'fxz', '0', 1);
INSERT INTO `sys_menu` (`id`, `parent_id`, `title`, `name`, `perms`, `type`, `component`, `path`, `redirect`, `icon`, `keep_alive`, `order_num`, `create_time`, `update_time`, `create_by`, `update_by`, `hidden`, `application`) VALUES (100009, 100006, '分页查看用户信息', 'sys:user:view', 'sys:user:view', '1', 'sys:user:view', '/sys/user/list', NULL, NULL, 1, 1, '2022-02-27 13:43:05', '2023-05-04 17:54:26', NULL, 'fxz', '0', 1);
INSERT INTO `sys_menu` (`id`, `parent_id`, `title`, `name`, `perms`, `type`, `component`, `path`, `redirect`, `icon`, `keep_alive`, `order_num`, `create_time`, `update_time`, `create_by`, `update_by`, `hidden`, `application`) VALUES (100010, 100006, '更新用户信息', 'sys:user:update', 'sys:user:update', '1', NULL, 'sys:user:update', NULL, NULL, 1, NULL, '2022-02-27 17:05:44', '2023-05-04 17:54:26', NULL, 'fxz', '0', 1);
INSERT INTO `sys_menu` (`id`, `parent_id`, `title`, `name`, `perms`, `type`, `component`, `path`, `redirect`, `icon`, `keep_alive`, `order_num`, `create_time`, `update_time`, `create_by`, `update_by`, `hidden`, `application`) VALUES (100011, 100006, '新增用户信息', 'sys:user:add', 'sys:user:add', '1', NULL, 'sys:user:add', NULL, NULL, 1, NULL, '2022-02-27 18:07:12', '2023-05-04 17:54:26', NULL, 'fxz', '0', 1);
INSERT INTO `sys_menu` (`id`, `parent_id`, `title`, `name`, `perms`, `type`, `component`, `path`, `redirect`, `icon`, `keep_alive`, `order_num`, `create_time`, `update_time`, `create_by`, `update_by`, `hidden`, `application`) VALUES (100012, 0, '开发工具', 'sysTool', 'sysTool', '0', 'RouteView', '/sysTool', NULL, 'ant-design:tool-filled', 1, 999, '2022-03-04 09:14:37', '2023-05-04 17:59:23', NULL, 'fxz', '0', 1);
INSERT INTO `sys_menu` (`id`, `parent_id`, `title`, `name`, `perms`, `type`, `component`, `path`, `redirect`, `icon`, `keep_alive`, `order_num`, `create_time`, `update_time`, `create_by`, `update_by`, `hidden`, `application`) VALUES (100013, 100012, '代码生成器', 'CodeGenIndex', 'sysTool:genCode', '0', 'system/gen/CodeGenIndex.vue', '/sysTool/genCode', NULL, 'ant-design:check-square-twotone', 1, 1, '2022-03-04 09:19:37', '2023-05-05 10:20:43', NULL, 'fxz', '0', 1);
INSERT INTO `sys_menu` (`id`, `parent_id`, `title`, `name`, `perms`, `type`, `component`, `path`, `redirect`, `icon`, `keep_alive`, `order_num`, `create_time`, `update_time`, `create_by`, `update_by`, `hidden`, `application`) VALUES (100016, 100012, '数据源管理', 'DatasourceConfList', 'sysTool:datasourceConf', '0', 'system/gen/datasource/DatasourceConfList.vue', '/sysTool/datasourceConf', NULL, 'ant-design:ant-cloud-outlined', 1, 0, '2022-03-31 12:31:26', '2023-05-04 17:59:47', 'fxz', 'fxz', '0', 1);
INSERT INTO `sys_menu` (`id`, `parent_id`, `title`, `name`, `perms`, `type`, `component`, `path`, `redirect`, `icon`, `keep_alive`, `order_num`, `create_time`, `update_time`, `create_by`, `update_by`, `hidden`, `application`) VALUES (100018, 100012, '定时任务', 'JobList', 'sys:job', '0', 'system/job/JobList.vue', '/job/index', NULL, 'ant-design:bug-filled', 0, 3, '2022-04-03 18:02:04', '2023-05-05 12:25:23', 'fxz', 'fxz', '0', 1);
INSERT INTO `sys_menu` (`id`, `parent_id`, `title`, `name`, `perms`, `type`, `component`, `path`, `redirect`, `icon`, `keep_alive`, `order_num`, `create_time`, `update_time`, `create_by`, `update_by`, `hidden`, `application`) VALUES (100019, 100046, '字典管理', 'DictList', 'sys:dict', '0', 'system/dict/DictList.vue', '/permissions/dict', NULL, 'ant-design:medicine-box-filled', 1, 4, '2022-04-04 11:20:33', '2023-05-04 17:56:38', 'fxz', 'fxz', '0', 1);
INSERT INTO `sys_menu` (`id`, `parent_id`, `title`, `name`, `perms`, `type`, `component`, `path`, `redirect`, `icon`, `keep_alive`, `order_num`, `create_time`, `update_time`, `create_by`, `update_by`, `hidden`, `application`) VALUES (100020, 100012, '调度日志', 'JobLogList', 'sys:jobLog', '0', 'system/job/JobLogList.vue', '/job/log', NULL, 'ant-design:idcard-filled', 0, 4, '2022-04-04 15:55:43', '2023-05-05 10:34:49', 'fxz', 'fxz', '0', 1);
INSERT INTO `sys_menu` (`id`, `parent_id`, `title`, `name`, `perms`, `type`, `component`, `path`, `redirect`, `icon`, `keep_alive`, `order_num`, `create_time`, `update_time`, `create_by`, `update_by`, `hidden`, `application`) VALUES (100021, 100046, '文件管理', 'FileList', 'sys:file', '0', 'system/file/FileList.vue', '/permissions/file', NULL, 'ant-design:file-pdf-twotone', 1, 5, '2022-04-04 22:47:38', '2023-05-04 17:56:58', 'fxz', 'fxz', '0', 1);
INSERT INTO `sys_menu` (`id`, `parent_id`, `title`, `name`, `perms`, `type`, `component`, `path`, `redirect`, `icon`, `keep_alive`, `order_num`, `create_time`, `update_time`, `create_by`, `update_by`, `hidden`, `application`) VALUES (100022, 20, '岗位管理', 'PostList', 'sys:post', '0', 'system/post/PostList.vue', '/permissions/post', NULL, 'ant-design:bank-filled', 1, 4, '2022-04-05 20:17:47', '2023-05-04 17:55:25', 'fxz', 'fxz', '0', 1);
INSERT INTO `sys_menu` (`id`, `parent_id`, `title`, `name`, `perms`, `type`, `component`, `path`, `redirect`, `icon`, `keep_alive`, `order_num`, `create_time`, `update_time`, `create_by`, `update_by`, `hidden`, `application`) VALUES (100024, 100046, '审计管理', 'oper', 'sys:oper', '0', 'RouteView', '/permissions/oper', NULL, 'ant-design:book-twotone', 1, 7, '2022-04-24 00:12:34', '2023-05-04 17:58:03', 'fxz', 'fxz', '0', 1);
INSERT INTO `sys_menu` (`id`, `parent_id`, `title`, `name`, `perms`, `type`, `component`, `path`, `redirect`, `icon`, `keep_alive`, `order_num`, `create_time`, `update_time`, `create_by`, `update_by`, `hidden`, `application`) VALUES (100025, 100024, '登录日志', 'OperLogin', 'sys:oper:login', '0', 'system/log/OperLogList.vue', '/sys/oper/login', NULL, 'ant-design:bg-colors-outlined', 1, 0, '2022-04-24 00:15:20', '2023-05-04 17:58:26', 'fxz', 'fxz', '0', 1);
INSERT INTO `sys_menu` (`id`, `parent_id`, `title`, `name`, `perms`, `type`, `component`, `path`, `redirect`, `icon`, `keep_alive`, `order_num`, `create_time`, `update_time`, `create_by`, `update_by`, `hidden`, `application`) VALUES (100026, 100024, '操作日志', 'OperLogList', 'sys:oper:log', '0', 'system/log/OperLogList.vue', '/sys/oper/log', NULL, 'ant-design:alert-twotone', 1, 1, '2022-04-24 00:17:31', '2023-05-04 17:58:40', 'fxz', 'fxz', '0', 1);
INSERT INTO `sys_menu` (`id`, `parent_id`, `title`, `name`, `perms`, `type`, `component`, `path`, `redirect`, `icon`, `keep_alive`, `order_num`, `create_time`, `update_time`, `create_by`, `update_by`, `hidden`, `application`) VALUES (100034, 100046, '令牌管理', 'TokenList', 'sys:token', '0', 'system/token/index.vue', '/permissions/token', NULL, 'ant-design:thunderbolt-filled', 0, 5, '2022-06-26 21:18:21', '2023-05-04 17:57:22', 'fxz', 'fxz', '0', 1);
INSERT INTO `sys_menu` (`id`, `parent_id`, `title`, `name`, `perms`, `type`, `component`, `path`, `redirect`, `icon`, `keep_alive`, `order_num`, `create_time`, `update_time`, `create_by`, `update_by`, `hidden`, `application`) VALUES (100040, 100046, '动态网关', 'RouteList', 'sys:route', '0', 'system/route/Index.vue', '/sys/route', NULL, 'ant-design:gateway-outlined', 1, 10, '2022-08-20 17:12:24', '2023-05-04 17:59:11', 'fxz', 'fxz', '0', 1);
INSERT INTO `sys_menu` (`id`, `parent_id`, `title`, `name`, `perms`, `type`, `component`, `path`, `redirect`, `icon`, `keep_alive`, `order_num`, `create_time`, `update_time`, `create_by`, `update_by`, `hidden`, `application`) VALUES (100043, 100046, '应用管理', 'AppList', 'sys:app', '0', 'system/app/AppList.vue', '/sys/app', NULL, 'ant-design:apple-filled', 1, 6, '2022-09-12 12:55:49', '2023-05-04 17:58:56', 'fxz', 'fxz', '0', 1);
INSERT INTO `sys_menu` (`id`, `parent_id`, `title`, `name`, `perms`, `type`, `component`, `path`, `redirect`, `icon`, `keep_alive`, `order_num`, `create_time`, `update_time`, `create_by`, `update_by`, `hidden`, `application`) VALUES (100045, 100044, '11', '2', '2', '0', '2', '2', '2', '2', 0, 2, '2022-09-12 16:10:37', '2022-09-12 16:10:37', 'fxz', 'fxz', '0', 2);
INSERT INTO `sys_menu` (`id`, `parent_id`, `title`, `name`, `perms`, `type`, `component`, `path`, `redirect`, `icon`, `keep_alive`, `order_num`, `create_time`, `update_time`, `create_by`, `update_by`, `hidden`, `application`) VALUES (100046, 0, '系统管理', 'sys', 'system', '0', 'RouteView', '/sys', NULL, 'ant-design:windows-outlined', 1, 3, '2022-09-12 16:19:00', '2023-05-04 17:56:22', 'fxz', 'fxz', '0', 1);
INSERT INTO `sys_menu` (`id`, `parent_id`, `title`, `name`, `perms`, `type`, `component`, `path`, `redirect`, `icon`, `keep_alive`, `order_num`, `create_time`, `update_time`, `create_by`, `update_by`, `hidden`, `application`) VALUES (100047, 0, '租户管理', 'tenant', 'sys:tenant', '0', 'RouteView', '/tenant', NULL, 'ant-design:usergroup-add-outlined', 1, 2, '2022-10-01 17:01:56', '2023-05-04 17:52:45', 'fxz', 'fxz', '0', 1);
INSERT INTO `sys_menu` (`id`, `parent_id`, `title`, `name`, `perms`, `type`, `component`, `path`, `redirect`, `icon`, `keep_alive`, `order_num`, `create_time`, `update_time`, `create_by`, `update_by`, `hidden`, `application`) VALUES (100048, 100047, '租户列表', 'TenantList', 'sys:tenant:index', '0', 'system/tenant/TenantList.vue', '/tenant/list', NULL, 'ant-design:user-add-outlined', 1, 0, '2022-10-01 17:03:47', '2023-05-05 13:08:46', 'fxz', 'fxz', '0', 1);
INSERT INTO `sys_menu` (`id`, `parent_id`, `title`, `name`, `perms`, `type`, `component`, `path`, `redirect`, `icon`, `keep_alive`, `order_num`, `create_time`, `update_time`, `create_by`, `update_by`, `hidden`, `application`) VALUES (100049, 100047, '租户套餐', 'TenantPackageIndex', 'sys:tenant:package', '0', 'system/tenant/TenantPackageIndex.vue', '/tenant/package', NULL, 'ant-design:menu-outlined', 1, 1, '2022-10-01 18:07:44', '2023-05-05 13:07:51', 'fxz', 'fxz', '0', 1);
INSERT INTO `sys_menu` (`id`, `parent_id`, `title`, `name`, `perms`, `type`, `component`, `path`, `redirect`, `icon`, `keep_alive`, `order_num`, `create_time`, `update_time`, `create_by`, `update_by`, `hidden`, `application`) VALUES (100050, 100012, '接口文档', 'doc', 'sys:doc', '0', 'RouteView', 'http://art-gateway:9999/doc.html', '', 'ant-design:file-done-outlined', 1, 999, '2022-10-23 14:56:04', '2023-05-05 10:34:19', 'fxz', 'fxz', '0', 1);
INSERT INTO `sys_menu` (`id`, `parent_id`, `title`, `name`, `perms`, `type`, `component`, `path`, `redirect`, `icon`, `keep_alive`, `order_num`, `create_time`, `update_time`, `create_by`, `update_by`, `hidden`, `application`) VALUES (1646327120896270338, 100046, '终端管理', 'ClientList', 'sys:client', '0', 'system/client/ClientList.vue', '/permissions/client', NULL, 'ant-design:mobile-filled', 1, 5, '2023-04-13 09:39:08', '2023-05-04 17:57:45', 'fxz', 'fxz', '0', 1);
INSERT INTO `sys_menu` (`id`, `parent_id`, `title`, `name`, `perms`, `type`, `component`, `path`, `redirect`, `icon`, `keep_alive`, `order_num`, `create_time`, `update_time`, `create_by`, `update_by`, `hidden`, `application`) VALUES (1646327719289233410, 1646327120896270338, '新增', 'add', 'system:ClientDetails:add', '1', NULL, NULL, NULL, 'border-top', 1, 0, '2023-04-13 09:41:30', '2023-05-04 17:57:45', 'fxz', 'fxz', '0', 1);
INSERT INTO `sys_menu` (`id`, `parent_id`, `title`, `name`, `perms`, `type`, `component`, `path`, `redirect`, `icon`, `keep_alive`, `order_num`, `create_time`, `update_time`, `create_by`, `update_by`, `hidden`, `application`) VALUES (1646327832816459777, 1646327120896270338, '修改', 'update', 'system:ClientDetails:update', '1', NULL, NULL, NULL, 'arrow-right', 1, 1, '2023-04-13 09:41:57', '2023-05-04 17:57:45', 'fxz', 'fxz', '0', 1);
INSERT INTO `sys_menu` (`id`, `parent_id`, `title`, `name`, `perms`, `type`, `component`, `path`, `redirect`, `icon`, `keep_alive`, `order_num`, `create_time`, `update_time`, `create_by`, `update_by`, `hidden`, `application`) VALUES (1646328545541955585, 100043, '删除', 'system:ClientDetails:delete', 'system:ClientDetails:delete', '1', NULL, NULL, NULL, 'border-top', 1, 3, '2023-04-13 09:44:47', '2023-05-04 17:58:56', 'fxz', 'fxz', '0', 1);
INSERT INTO `sys_menu` (`id`, `parent_id`, `title`, `name`, `perms`, `type`, `component`, `path`, `redirect`, `icon`, `keep_alive`, `order_num`, `create_time`, `update_time`, `create_by`, `update_by`, `hidden`, `application`) VALUES (1646328655927648257, 1646327120896270338, '查看', 'system:ClientDetails:view', 'system:ClientDetails:view', '1', NULL, NULL, NULL, 'border-top', 1, 4, '2023-04-13 09:45:14', '2023-05-04 17:57:45', 'fxz', 'fxz', '0', 1);
INSERT INTO `sys_menu` (`id`, `parent_id`, `title`, `name`, `perms`, `type`, `component`, `path`, `redirect`, `icon`, `keep_alive`, `order_num`, `create_time`, `update_time`, `create_by`, `update_by`, `hidden`, `application`) VALUES (1947373944789016578, 0, 'ai工作流', 'workflowIndex', 'ai:workflow', '0', 'ai/workflow/WorkFlowIndex.vue', '/ai/workflow', '', 'ant-design:android-filled', 1, 1, '2025-07-22 03:11:44', '2025-08-04 20:12:53', 'fxz', 'fxz', '1', 1);
INSERT INTO `sys_menu` (`id`, `parent_id`, `title`, `name`, `perms`, `type`, `component`, `path`, `redirect`, `icon`, `keep_alive`, `order_num`, `create_time`, `update_time`, `create_by`, `update_by`, `hidden`, `application`) VALUES (1948743715304505345, 0, '工作室', 'AiAppList', 'ai:workshop', '0', 'ai/app/AiAppList.vue', '/ai/workshop', '', 'ant-design:bank-filled', 1, 0, '2025-07-25 21:54:42', '2025-07-31 01:41:05', 'fxz', 'fxz', '0', 1);
INSERT INTO `sys_menu` (`id`, `parent_id`, `title`, `name`, `perms`, `type`, `component`, `path`, `redirect`, `icon`, `keep_alive`, `order_num`, `create_time`, `update_time`, `create_by`, `update_by`, `hidden`, `application`) VALUES (1948746158314680322, 1948743715304505345, '创建应用', 'ai:aiApp:add', 'ai:aiApp:add', '1', '', '', '', '', 1, 99, '2025-07-25 22:04:25', '2025-07-31 01:41:05', 'fxz', 'fxz', '', 1);
INSERT INTO `sys_menu` (`id`, `parent_id`, `title`, `name`, `perms`, `type`, `component`, `path`, `redirect`, `icon`, `keep_alive`, `order_num`, `create_time`, `update_time`, `create_by`, `update_by`, `hidden`, `application`) VALUES (1948746158314680399, 1948743715304505345, '查看应用', 'ai:aiApp:view', 'ai:aiApp:view', '1', '', '', '', '', 1, 99, '2025-07-25 22:04:25', '2025-07-31 01:41:05', 'fxz', 'fxz', '', 1);
INSERT INTO `sys_menu` (`id`, `parent_id`, `title`, `name`, `perms`, `type`, `component`, `path`, `redirect`, `icon`, `keep_alive`, `order_num`, `create_time`, `update_time`, `create_by`, `update_by`, `hidden`, `application`) VALUES (1948746277923647489, 1948743715304505345, '修改应用', 'ai:aiApp:update', 'ai:aiApp:update', '1', '', '', '', '', 1, 99, '2025-07-25 22:04:53', '2025-07-31 01:41:05', 'fxz', 'fxz', '', 1);
INSERT INTO `sys_menu` (`id`, `parent_id`, `title`, `name`, `perms`, `type`, `component`, `path`, `redirect`, `icon`, `keep_alive`, `order_num`, `create_time`, `update_time`, `create_by`, `update_by`, `hidden`, `application`) VALUES (1948746387512422401, 1948743715304505345, '删除应用', 'ai:aiApp:delete', 'ai:aiApp:delete', '1', '', '', '', '', 1, 99, '2025-07-25 22:05:19', '2025-07-31 01:41:05', 'fxz', 'fxz', '', 1);
INSERT INTO `sys_menu` (`id`, `parent_id`, `title`, `name`, `perms`, `type`, `component`, `path`, `redirect`, `icon`, `keep_alive`, `order_num`, `create_time`, `update_time`, `create_by`, `update_by`, `hidden`, `application`) VALUES (1950611694533042178, 1947373944789016578, '新建工作流', 'ai:aiWorkflows:add', 'ai:aiWorkflows:add', '1', '', '', '', '', 1, 99, '2025-07-31 01:37:23', '2025-08-04 20:12:54', 'fxz', 'fxz', '', 1);
INSERT INTO `sys_menu` (`id`, `parent_id`, `title`, `name`, `perms`, `type`, `component`, `path`, `redirect`, `icon`, `keep_alive`, `order_num`, `create_time`, `update_time`, `create_by`, `update_by`, `hidden`, `application`) VALUES (1950611817623281665, 1947373944789016578, '修改工作流', 'ai:aiWorkflows:update', 'ai:aiWorkflows:update', '1', '', '', '', '', 1, 99, '2025-07-31 01:37:53', '2025-08-04 20:12:54', 'fxz', 'fxz', '', 1);
INSERT INTO `sys_menu` (`id`, `parent_id`, `title`, `name`, `perms`, `type`, `component`, `path`, `redirect`, `icon`, `keep_alive`, `order_num`, `create_time`, `update_time`, `create_by`, `update_by`, `hidden`, `application`) VALUES (1950611896321007617, 1947373944789016578, '删除工作流', 'ai:aiWorkflows:delete', 'ai:aiWorkflows:delete', '1', '', '', '', '', 1, 99, '2025-07-31 01:38:11', '2025-08-04 20:12:54', 'fxz', 'fxz', '', 1);
INSERT INTO `sys_menu` (`id`, `parent_id`, `title`, `name`, `perms`, `type`, `component`, `path`, `redirect`, `icon`, `keep_alive`, `order_num`, `create_time`, `update_time`, `create_by`, `update_by`, `hidden`, `application`) VALUES (1950611982950162434, 1947373944789016578, '查看工作流', 'ai:aiWorkflows:view', 'ai:aiWorkflows:view', '1', '', '', '', '', 1, 99, '2025-07-31 01:38:32', '2025-08-04 20:12:54', 'fxz', 'fxz', '', 1);
INSERT INTO `sys_menu` (`id`, `parent_id`, `title`, `name`, `perms`, `type`, `component`, `path`, `redirect`, `icon`, `keep_alive`, `order_num`, `create_time`, `update_time`, `create_by`, `update_by`, `hidden`, `application`) VALUES (1973309578902540289, 0, '知识库', 'AiDatasetsList', 'ai:dataset', '0', 'ai/dataset/AiDatasetsList.vue', '/ai/dataset', '', 'ant-design:folder-open-filled', 1, 1, '2025-10-01 16:50:41', '2025-10-02 15:02:52', 'fxz', 'fxz', '', 1);
INSERT INTO `sys_menu` (`id`, `parent_id`, `title`, `name`, `perms`, `type`, `component`, `path`, `redirect`, `icon`, `keep_alive`, `order_num`, `create_time`, `update_time`, `create_by`, `update_by`, `hidden`, `application`) VALUES (1973310066154835969, 1973309578902540289, '查看知识库', 'ai:aiDatasets:view', 'ai:aiDatasets:view', '1', '', '', '', '', 1, 99, '2025-10-01 16:52:37', '2025-10-02 15:02:52', 'fxz', 'fxz', '', 1);
INSERT INTO `sys_menu` (`id`, `parent_id`, `title`, `name`, `perms`, `type`, `component`, `path`, `redirect`, `icon`, `keep_alive`, `order_num`, `create_time`, `update_time`, `create_by`, `update_by`, `hidden`, `application`) VALUES (1973310172430110721, 1973309578902540289, '编辑知识库', 'ai:aiDatasets:update', 'ai:aiDatasets:update', '1', '', '', '', '', 1, 99, '2025-10-01 16:53:02', '2025-10-02 15:02:52', 'fxz', 'fxz', '', 1);
INSERT INTO `sys_menu` (`id`, `parent_id`, `title`, `name`, `perms`, `type`, `component`, `path`, `redirect`, `icon`, `keep_alive`, `order_num`, `create_time`, `update_time`, `create_by`, `update_by`, `hidden`, `application`) VALUES (1973310259596136450, 1973309578902540289, '删除知识库', 'ai:aiDatasets:delete', 'ai:aiDatasets:delete', '1', '', '', '', '', 1, 99, '2025-10-01 16:53:23', '2025-10-02 15:02:52', 'fxz', 'fxz', '', 1);
INSERT INTO `sys_menu` (`id`, `parent_id`, `title`, `name`, `perms`, `type`, `component`, `path`, `redirect`, `icon`, `keep_alive`, `order_num`, `create_time`, `update_time`, `create_by`, `update_by`, `hidden`, `application`) VALUES (1973310337702465538, 1973309578902540289, '新增知识库', 'ai:aiDatasets:add', 'ai:aiDatasets:add', '1', '', '', '', '', 1, 99, '2025-10-01 16:53:41', '2025-10-02 15:02:52', 'fxz', 'fxz', '', 1);
INSERT INTO `sys_menu` (`id`, `parent_id`, `title`, `name`, `perms`, `type`, `component`, `path`, `redirect`, `icon`, `keep_alive`, `order_num`, `create_time`, `update_time`, `create_by`, `update_by`, `hidden`, `application`) VALUES (1973413747810484226, 0, 'ai文档列表', 'AiDocumentsList', 'ai:dataset:document', '0', 'ai/document/AiDocumentsList.vue', '/ai/document', '', 'ant-design:align-left-outlined', 1, 10, '2025-10-01 23:44:36', '2025-10-02 15:03:04', 'fxz', 'fxz', '1', 1);
INSERT INTO `sys_menu` (`id`, `parent_id`, `title`, `name`, `perms`, `type`, `component`, `path`, `redirect`, `icon`, `keep_alive`, `order_num`, `create_time`, `update_time`, `create_by`, `update_by`, `hidden`, `application`) VALUES (1973415590561169410, 1973413747810484226, '查看文档列表', 'ai:aiDocuments:view', 'ai:aiDocuments:view', '1', '', '', '', '', 1, 99, '2025-10-01 23:51:56', '2025-10-02 15:03:04', 'fxz', 'fxz', '', 1);
INSERT INTO `sys_menu` (`id`, `parent_id`, `title`, `name`, `perms`, `type`, `component`, `path`, `redirect`, `icon`, `keep_alive`, `order_num`, `create_time`, `update_time`, `create_by`, `update_by`, `hidden`, `application`) VALUES (1973441926402977793, 1973413747810484226, '删除文档', 'ai:aiDocuments:delete', 'ai:aiDocuments:delete', '1', '', '', '', '', 1, 99, '2025-10-02 01:36:35', '2025-10-02 15:03:04', 'fxz', 'fxz', '', 1);
INSERT INTO `sys_menu` (`id`, `parent_id`, `title`, `name`, `perms`, `type`, `component`, `path`, `redirect`, `icon`, `keep_alive`, `order_num`, `create_time`, `update_time`, `create_by`, `update_by`, `hidden`, `application`) VALUES (1973645406585491457, 0, 'ai文档分段列表', 'AiDocumentSegmentList', 'ai:document:segment', '0', 'ai/document/AiDocumentSegmentList.vue', '/ai/document/segment', '', 'ant-design:calculator-outlined', 1, 11, '2025-10-02 15:05:08', '2025-10-02 15:06:04', 'fxz', 'fxz', '1', 1);
INSERT INTO `sys_menu` (`id`, `parent_id`, `title`, `name`, `perms`, `type`, `component`, `path`, `redirect`, `icon`, `keep_alive`, `order_num`, `create_time`, `update_time`, `create_by`, `update_by`, `hidden`, `application`) VALUES (1973683226884759554, 0, 'ai知识图谱', 'GraphIndex.vue', 'ai:document:graph', '0', 'ai/document/graph/GraphIndex.vue', '/ai/document/graph', '', 'ant-design:aliwangwang-outlined', 1, 11, '2025-10-02 17:35:25', '2025-10-02 17:35:25', 'fxz', 'fxz', '1', 1);
INSERT INTO `sys_menu` (`id`, `parent_id`, `title`, `name`, `perms`, `type`, `component`, `path`, `redirect`, `icon`, `keep_alive`, `order_num`, `create_time`, `update_time`, `create_by`, `update_by`, `hidden`, `application`) VALUES (1976278299929530369, 0, 'ai配置', 'aiConfig', 'ai:config', '0', 'RouteView', '/ai/config', '', 'ant-design:ant-cloud-outlined', 1, 5, '2025-10-09 21:27:19', '2025-10-09 21:27:19', 'fxz', 'fxz', '0', 1);
INSERT INTO `sys_menu` (`id`, `parent_id`, `title`, `name`, `perms`, `type`, `component`, `path`, `redirect`, `icon`, `keep_alive`, `order_num`, `create_time`, `update_time`, `create_by`, `update_by`, `hidden`, `application`) VALUES (1976279035769835522, 1976278299929530369, '模型管理', 'AiModelList', 'ai:config:model', '0', 'ai/model/AiModelList.vue', '/ai/config/model', '', 'ant-design:android-outlined', 1, 1, '2025-10-09 21:30:14', '2025-10-09 21:30:14', 'fxz', 'fxz', '0', 1);
INSERT INTO `sys_menu` (`id`, `parent_id`, `title`, `name`, `perms`, `type`, `component`, `path`, `redirect`, `icon`, `keep_alive`, `order_num`, `create_time`, `update_time`, `create_by`, `update_by`, `hidden`, `application`) VALUES (1976279483100745730, 1976278299929530369, '供应商管理', 'AiModelPlatformList', 'ai:config:model-platform', '0', 'ai/model/AiModelPlatformList.vue', '/ai/config/model-platform', '', 'ant-design:apple-filled', 1, 2, '2025-10-09 21:32:01', '2025-10-09 21:32:01', 'fxz', 'fxz', '0', 1);
INSERT INTO `sys_menu` (`id`, `parent_id`, `title`, `name`, `perms`, `type`, `component`, `path`, `redirect`, `icon`, `keep_alive`, `order_num`, `create_time`, `update_time`, `create_by`, `update_by`, `hidden`, `application`) VALUES (1976280323416969218, 1976279035769835522, '新建模型', 'system:aiModel:add', 'system:aiModel:add', '1', '', '', '', '', 1, 99, '2025-10-09 21:35:21', '2025-10-09 21:35:21', 'fxz', 'fxz', '', NULL);
INSERT INTO `sys_menu` (`id`, `parent_id`, `title`, `name`, `perms`, `type`, `component`, `path`, `redirect`, `icon`, `keep_alive`, `order_num`, `create_time`, `update_time`, `create_by`, `update_by`, `hidden`, `application`) VALUES (1976280426068365313, 1976279035769835522, '删除模型', 'system:aiModel:delete', 'system:aiModel:delete', '1', '', '', '', '', 1, 99, '2025-10-09 21:35:46', '2025-10-09 21:35:46', 'fxz', 'fxz', '', NULL);
INSERT INTO `sys_menu` (`id`, `parent_id`, `title`, `name`, `perms`, `type`, `component`, `path`, `redirect`, `icon`, `keep_alive`, `order_num`, `create_time`, `update_time`, `create_by`, `update_by`, `hidden`, `application`) VALUES (1976280499078615042, 1976279035769835522, '更新模型', 'system:aiModel:update', 'system:aiModel:update', '1', '', '', '', '', 1, 99, '2025-10-09 21:36:03', '2025-10-09 21:36:03', 'fxz', 'fxz', '', NULL);
INSERT INTO `sys_menu` (`id`, `parent_id`, `title`, `name`, `perms`, `type`, `component`, `path`, `redirect`, `icon`, `keep_alive`, `order_num`, `create_time`, `update_time`, `create_by`, `update_by`, `hidden`, `application`) VALUES (1976280566099398658, 1976279035769835522, '查看模型', 'system:aiModel:view', 'system:aiModel:view', '1', '', '', '', '', 1, 99, '2025-10-09 21:36:19', '2025-10-09 21:36:19', 'fxz', 'fxz', '', NULL);
INSERT INTO `sys_menu` (`id`, `parent_id`, `title`, `name`, `perms`, `type`, `component`, `path`, `redirect`, `icon`, `keep_alive`, `order_num`, `create_time`, `update_time`, `create_by`, `update_by`, `hidden`, `application`) VALUES (1976280682906570754, 1976279483100745730, '查看模型供应商', 'system:aiModelPlatform:view', 'system:aiModelPlatform:view', '1', '', '', '', '', 1, 99, '2025-10-09 21:36:47', '2025-10-09 21:36:47', 'fxz', 'fxz', '', NULL);
INSERT INTO `sys_menu` (`id`, `parent_id`, `title`, `name`, `perms`, `type`, `component`, `path`, `redirect`, `icon`, `keep_alive`, `order_num`, `create_time`, `update_time`, `create_by`, `update_by`, `hidden`, `application`) VALUES (1976281003280093186, 1976279483100745730, '更新供应商', 'system:aiModelPlatform:update', 'system:aiModelPlatform:update', '1', '', '', '', '', 1, 99, '2025-10-09 21:38:03', '2025-10-09 21:38:03', 'fxz', 'fxz', '', NULL);
INSERT INTO `sys_menu` (`id`, `parent_id`, `title`, `name`, `perms`, `type`, `component`, `path`, `redirect`, `icon`, `keep_alive`, `order_num`, `create_time`, `update_time`, `create_by`, `update_by`, `hidden`, `application`) VALUES (1976286148311175170, 1976279483100745730, '新增模型供应商', 'system:aiModelPlatform:add', 'system:aiModelPlatform:add', '1', '', '', '', '', 1, 99, '2025-10-09 21:58:30', '2025-10-09 21:58:30', 'fxz', 'fxz', '', NULL);

COMMIT;

-- ----------------------------
-- Table structure for sys_oauth_client_details
-- ----------------------------
DROP TABLE IF EXISTS `sys_oauth_client_details`;
CREATE TABLE `sys_oauth_client_details` (
  `client_id` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '客户端ID',
  `resource_ids` varchar(256) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '资源列表',
  `client_secret` varchar(256) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '客户端密钥',
  `scope` varchar(256) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '域',
  `authorized_grant_types` varchar(256) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '认证类型',
  `web_server_redirect_uri` varchar(256) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '重定向地址',
  `authorities` varchar(256) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '角色列表',
  `access_token_validity` int DEFAULT NULL COMMENT 'token 有效期',
  `refresh_token_validity` int DEFAULT NULL COMMENT '刷新令牌有效期',
  `additional_information` varchar(4096) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '令牌扩展字段JSON',
  `autoapprove` varchar(256) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '是否自动放行',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  `create_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '创建人',
  `update_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '更新人',
  PRIMARY KEY (`client_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='终端信息表';

-- ----------------------------
-- Records of sys_oauth_client_details
-- ----------------------------
BEGIN;
INSERT INTO `sys_oauth_client_details` (`client_id`, `resource_ids`, `client_secret`, `scope`, `authorized_grant_types`, `web_server_redirect_uri`, `authorities`, `access_token_validity`, `refresh_token_validity`, `additional_information`, `autoapprove`, `create_time`, `update_time`, `create_by`, `update_by`) VALUES ('app', NULL, 'app', 'server', 'app,refresh_token', NULL, NULL, NULL, NULL, NULL, 'true', NULL, NULL, NULL, NULL);
INSERT INTO `sys_oauth_client_details` (`client_id`, `resource_ids`, `client_secret`, `scope`, `authorized_grant_types`, `web_server_redirect_uri`, `authorities`, `access_token_validity`, `refresh_token_validity`, `additional_information`, `autoapprove`, `create_time`, `update_time`, `create_by`, `update_by`) VALUES ('fxz', NULL, '123456', 'server', 'refresh_token,authorization_code,captcha,password,sms_code', 'https://fxz.life', NULL, 21600, 2592000, NULL, 'true', NULL, '2023-04-13 10:25:58', NULL, 'fxz');
INSERT INTO `sys_oauth_client_details` (`client_id`, `resource_ids`, `client_secret`, `scope`, `authorized_grant_types`, `web_server_redirect_uri`, `authorities`, `access_token_validity`, `refresh_token_validity`, `additional_information`, `autoapprove`, `create_time`, `update_time`, `create_by`, `update_by`) VALUES ('mall-app', NULL, '123456', 'server', 'refresh_token,authorization_code,password,sms_code', 'http://localhost:80/sso/login', NULL, 43200, 2592001, NULL, 'true', NULL, NULL, NULL, NULL);
INSERT INTO `sys_oauth_client_details` (`client_id`, `resource_ids`, `client_secret`, `scope`, `authorized_grant_types`, `web_server_redirect_uri`, `authorities`, `access_token_validity`, `refresh_token_validity`, `additional_information`, `autoapprove`, `create_time`, `update_time`, `create_by`, `update_by`) VALUES ('wechat', NULL, '123456', 'server', 'refresh_token,authorization_code,wechat', 'https://fxz.life', '', 11111, NULL, '{\n	\"appName\":\"测试应用\"\n}', 'false', NULL, '2023-05-01 15:31:23', NULL, 'fxz');
COMMIT;

-- ----------------------------
-- Table structure for sys_oper_log
-- ----------------------------
DROP TABLE IF EXISTS `sys_oper_log`;
CREATE TABLE `sys_oper_log` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键',
  `title` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '标题',
  `business_type` tinyint(1) DEFAULT NULL COMMENT '业务类型',
  `method` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '方法名称',
  `request_method` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '请求方式',
  `oper_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '操作人员',
  `oper_url` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '请求url',
  `oper_ip` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '主机地址',
  `oper_param` longtext CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci COMMENT '请求参数',
  `status` tinyint(1) DEFAULT NULL COMMENT '操作状态（0正常 1异常）',
  `error_msg` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '异常信息',
  `time` bigint DEFAULT NULL COMMENT '执行时间',
  `create_by` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '创建人',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '更新人',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=1675039868060528642 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- ----------------------------
-- Records of sys_oper_log
-- ----------------------------
BEGIN;
COMMIT;

-- ----------------------------
-- Table structure for sys_post
-- ----------------------------
DROP TABLE IF EXISTS `sys_post`;
CREATE TABLE `sys_post` (
  `post_id` bigint NOT NULL AUTO_INCREMENT COMMENT '岗位ID',
  `post_code` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '岗位编码',
  `post_name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '岗位名称',
  `post_sort` int NOT NULL COMMENT '岗位排序',
  `del_flag` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '0' COMMENT '是否删除  -1：已删除  0：正常',
  `description` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '描述',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `create_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT '' COMMENT '创建人',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  `update_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT '' COMMENT '更新人',
  PRIMARY KEY (`post_id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=1654057031002046466 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='岗位信息表';

-- ----------------------------
-- Records of sys_post
-- ----------------------------
BEGIN;
INSERT INTO `sys_post` (`post_id`, `post_code`, `post_name`, `post_sort`, `del_flag`, `description`, `create_time`, `create_by`, `update_time`, `update_by`) VALUES (1511326356242804737, 'cto', 'cto', 0, '0', 'cto', '2022-04-05 20:54:17', 'fxz', '2022-06-28 13:12:48', 'fxz');
INSERT INTO `sys_post` (`post_id`, `post_code`, `post_name`, `post_sort`, `del_flag`, `description`, `create_time`, `create_by`, `update_time`, `update_by`) VALUES (1511338847253417985, 'ceo', 'ceo', 1, '0', 'ceo', '2022-04-05 21:43:55', 'fxz', '2022-04-05 21:43:55', 'fxz');
COMMIT;

-- ----------------------------
-- Table structure for sys_role
-- ----------------------------
DROP TABLE IF EXISTS `sys_role`;
CREATE TABLE `sys_role` (
  `role_id` bigint NOT NULL AUTO_INCREMENT COMMENT '角色ID',
  `role_name` varchar(10) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NOT NULL COMMENT '角色名称',
  `remark` varchar(100) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci DEFAULT NULL COMMENT '角色描述',
  `create_time` datetime NOT NULL COMMENT '创建时间',
  `update_time` datetime DEFAULT NULL COMMENT '修改时间',
  `create_by` varchar(100) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci DEFAULT NULL COMMENT '创建人',
  `update_by` varchar(100) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci DEFAULT NULL COMMENT '更新人',
  `data_scope` tinyint(1) DEFAULT NULL COMMENT '数据权限范围',
  `data_scope_dept_ids` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci DEFAULT NULL COMMENT '数据范围(指定部门数组)',
  `code` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci DEFAULT NULL COMMENT '角色code',
  `tenant_id` bigint DEFAULT NULL COMMENT '租户id',
  PRIMARY KEY (`role_id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=1666742445714272258 DEFAULT CHARSET=utf8mb3 COMMENT='角色表';

-- ----------------------------
-- Records of sys_role
-- ----------------------------
BEGIN;
INSERT INTO `sys_role` (`role_id`, `role_name`, `remark`, `create_time`, `update_time`, `create_by`, `update_by`, `data_scope`, `data_scope_dept_ids`, `code`, `tenant_id`) VALUES (1, 'admin', '超级管理员', '2022-02-28 16:23:11', '2023-05-03 12:54:20', NULL, 'fxz', 1, NULL, 'super_admin', 1);
INSERT INTO `sys_role` (`role_id`, `role_name`, `remark`, `create_time`, `update_time`, `create_by`, `update_by`, `data_scope`, `data_scope_dept_ids`, `code`, `tenant_id`) VALUES (13, '游客', '游客', '2022-02-28 16:23:11', '2023-05-04 15:08:01', 'fxz', 'fxz', 5, '', 'tourist', 1);
COMMIT;

-- ----------------------------
-- Table structure for sys_role_menu
-- ----------------------------
DROP TABLE IF EXISTS `sys_role_menu`;
CREATE TABLE `sys_role_menu` (
  `ROLE_ID` bigint NOT NULL,
  `MENU_ID` bigint NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3 COMMENT='角色菜单关联表';

-- ----------------------------
-- Records of sys_role_menu
-- ----------------------------
BEGIN;
INSERT INTO `sys_role_menu` (`ROLE_ID`, `MENU_ID`) VALUES (1, 2);
INSERT INTO `sys_role_menu` (`ROLE_ID`, `MENU_ID`) VALUES (1, 3);
INSERT INTO `sys_role_menu` (`ROLE_ID`, `MENU_ID`) VALUES (1, 4);
INSERT INTO `sys_role_menu` (`ROLE_ID`, `MENU_ID`) VALUES (1, 5);
INSERT INTO `sys_role_menu` (`ROLE_ID`, `MENU_ID`) VALUES (1, 6);
INSERT INTO `sys_role_menu` (`ROLE_ID`, `MENU_ID`) VALUES (1, 7);
INSERT INTO `sys_role_menu` (`ROLE_ID`, `MENU_ID`) VALUES (1, 12);
INSERT INTO `sys_role_menu` (`ROLE_ID`, `MENU_ID`) VALUES (1, 20);
INSERT INTO `sys_role_menu` (`ROLE_ID`, `MENU_ID`) VALUES (1, 21);
INSERT INTO `sys_role_menu` (`ROLE_ID`, `MENU_ID`) VALUES (1, 29);
INSERT INTO `sys_role_menu` (`ROLE_ID`, `MENU_ID`) VALUES (1, 100006);
INSERT INTO `sys_role_menu` (`ROLE_ID`, `MENU_ID`) VALUES (1, 100009);
INSERT INTO `sys_role_menu` (`ROLE_ID`, `MENU_ID`) VALUES (1, 100010);
INSERT INTO `sys_role_menu` (`ROLE_ID`, `MENU_ID`) VALUES (1, 100011);
INSERT INTO `sys_role_menu` (`ROLE_ID`, `MENU_ID`) VALUES (1, 100008);
INSERT INTO `sys_role_menu` (`ROLE_ID`, `MENU_ID`) VALUES (1, 100007);
INSERT INTO `sys_role_menu` (`ROLE_ID`, `MENU_ID`) VALUES (1, 100012);
INSERT INTO `sys_role_menu` (`ROLE_ID`, `MENU_ID`) VALUES (1, 100013);
INSERT INTO `sys_role_menu` (`ROLE_ID`, `MENU_ID`) VALUES (1, 100014);
INSERT INTO `sys_role_menu` (`ROLE_ID`, `MENU_ID`) VALUES (1, 1);
INSERT INTO `sys_role_menu` (`ROLE_ID`, `MENU_ID`) VALUES (1, 100015);
INSERT INTO `sys_role_menu` (`ROLE_ID`, `MENU_ID`) VALUES (1, 100016);
INSERT INTO `sys_role_menu` (`ROLE_ID`, `MENU_ID`) VALUES (1, 100017);
INSERT INTO `sys_role_menu` (`ROLE_ID`, `MENU_ID`) VALUES (1, 100018);
INSERT INTO `sys_role_menu` (`ROLE_ID`, `MENU_ID`) VALUES (1, 100019);
INSERT INTO `sys_role_menu` (`ROLE_ID`, `MENU_ID`) VALUES (1, 100020);
INSERT INTO `sys_role_menu` (`ROLE_ID`, `MENU_ID`) VALUES (1, 100021);
INSERT INTO `sys_role_menu` (`ROLE_ID`, `MENU_ID`) VALUES (1, 100022);
INSERT INTO `sys_role_menu` (`ROLE_ID`, `MENU_ID`) VALUES (1, 100023);
INSERT INTO `sys_role_menu` (`ROLE_ID`, `MENU_ID`) VALUES (1, 100024);
INSERT INTO `sys_role_menu` (`ROLE_ID`, `MENU_ID`) VALUES (1, 100025);
INSERT INTO `sys_role_menu` (`ROLE_ID`, `MENU_ID`) VALUES (1, 100026);
INSERT INTO `sys_role_menu` (`ROLE_ID`, `MENU_ID`) VALUES (1, 100027);
INSERT INTO `sys_role_menu` (`ROLE_ID`, `MENU_ID`) VALUES (1, 100028);
INSERT INTO `sys_role_menu` (`ROLE_ID`, `MENU_ID`) VALUES (1, 100029);
INSERT INTO `sys_role_menu` (`ROLE_ID`, `MENU_ID`) VALUES (1, 100030);
INSERT INTO `sys_role_menu` (`ROLE_ID`, `MENU_ID`) VALUES (1, 100031);
INSERT INTO `sys_role_menu` (`ROLE_ID`, `MENU_ID`) VALUES (1, 100032);
INSERT INTO `sys_role_menu` (`ROLE_ID`, `MENU_ID`) VALUES (1, 100033);
INSERT INTO `sys_role_menu` (`ROLE_ID`, `MENU_ID`) VALUES (1, 100034);
INSERT INTO `sys_role_menu` (`ROLE_ID`, `MENU_ID`) VALUES (1, 100035);
INSERT INTO `sys_role_menu` (`ROLE_ID`, `MENU_ID`) VALUES (1, 100036);
INSERT INTO `sys_role_menu` (`ROLE_ID`, `MENU_ID`) VALUES (1, 100037);
INSERT INTO `sys_role_menu` (`ROLE_ID`, `MENU_ID`) VALUES (1, 100038);
INSERT INTO `sys_role_menu` (`ROLE_ID`, `MENU_ID`) VALUES (1, 100039);
INSERT INTO `sys_role_menu` (`ROLE_ID`, `MENU_ID`) VALUES (1, 100040);
INSERT INTO `sys_role_menu` (`ROLE_ID`, `MENU_ID`) VALUES (1, 100041);
INSERT INTO `sys_role_menu` (`ROLE_ID`, `MENU_ID`) VALUES (1, 100042);
INSERT INTO `sys_role_menu` (`ROLE_ID`, `MENU_ID`) VALUES (1, 100043);
INSERT INTO `sys_role_menu` (`ROLE_ID`, `MENU_ID`) VALUES (1, 100044);
INSERT INTO `sys_role_menu` (`ROLE_ID`, `MENU_ID`) VALUES (1, 100045);
INSERT INTO `sys_role_menu` (`ROLE_ID`, `MENU_ID`) VALUES (1, 100046);
INSERT INTO `sys_role_menu` (`ROLE_ID`, `MENU_ID`) VALUES (1, 100047);
INSERT INTO `sys_role_menu` (`ROLE_ID`, `MENU_ID`) VALUES (1, 100048);
INSERT INTO `sys_role_menu` (`ROLE_ID`, `MENU_ID`) VALUES (1, 100049);
INSERT INTO `sys_role_menu` (`ROLE_ID`, `MENU_ID`) VALUES (1, 100050);
INSERT INTO `sys_role_menu` (`ROLE_ID`, `MENU_ID`) VALUES (1, 1628281206988611586);
INSERT INTO `sys_role_menu` (`ROLE_ID`, `MENU_ID`) VALUES (1, 1628282988909305858);
INSERT INTO `sys_role_menu` (`ROLE_ID`, `MENU_ID`) VALUES (1, 1628283417441345538);
INSERT INTO `sys_role_menu` (`ROLE_ID`, `MENU_ID`) VALUES (1, 1628317343117275137);
INSERT INTO `sys_role_menu` (`ROLE_ID`, `MENU_ID`) VALUES (1, 1628317471911768065);
INSERT INTO `sys_role_menu` (`ROLE_ID`, `MENU_ID`) VALUES (1, 1628317584814043138);
INSERT INTO `sys_role_menu` (`ROLE_ID`, `MENU_ID`) VALUES (1, 1628317214134038530);
INSERT INTO `sys_role_menu` (`ROLE_ID`, `MENU_ID`) VALUES (1, 1628656999216459777);
INSERT INTO `sys_role_menu` (`ROLE_ID`, `MENU_ID`) VALUES (1, 1628728529633726466);
INSERT INTO `sys_role_menu` (`ROLE_ID`, `MENU_ID`) VALUES (1, 1628966690909663233);
INSERT INTO `sys_role_menu` (`ROLE_ID`, `MENU_ID`) VALUES (1, 1646327120896270338);
INSERT INTO `sys_role_menu` (`ROLE_ID`, `MENU_ID`) VALUES (1, 1646327719289233410);
INSERT INTO `sys_role_menu` (`ROLE_ID`, `MENU_ID`) VALUES (1, 1646327832816459777);
INSERT INTO `sys_role_menu` (`ROLE_ID`, `MENU_ID`) VALUES (1, 1646328655927648257);
INSERT INTO `sys_role_menu` (`ROLE_ID`, `MENU_ID`) VALUES (1, 1646328545541955585);
INSERT INTO `sys_role_menu` (`ROLE_ID`, `MENU_ID`) VALUES (1, 10);
INSERT INTO `sys_role_menu` (`ROLE_ID`, `MENU_ID`) VALUES (1, 1643914857870577666);
INSERT INTO `sys_role_menu` (`ROLE_ID`, `MENU_ID`) VALUES (1, 1643914131685560321);
INSERT INTO `sys_role_menu` (`ROLE_ID`, `MENU_ID`) VALUES (1, 1643915123281940482);
INSERT INTO `sys_role_menu` (`ROLE_ID`, `MENU_ID`) VALUES (1, 1643915724992598018);
INSERT INTO `sys_role_menu` (`ROLE_ID`, `MENU_ID`) VALUES (1, 1643915864348348417);
INSERT INTO `sys_role_menu` (`ROLE_ID`, `MENU_ID`) VALUES (1, 1643916004987555842);
INSERT INTO `sys_role_menu` (`ROLE_ID`, `MENU_ID`) VALUES (1, 1643916153356865538);
INSERT INTO `sys_role_menu` (`ROLE_ID`, `MENU_ID`) VALUES (1, 1643916725887750145);
INSERT INTO `sys_role_menu` (`ROLE_ID`, `MENU_ID`) VALUES (1, 1643918418641735681);
INSERT INTO `sys_role_menu` (`ROLE_ID`, `MENU_ID`) VALUES (1, 1643917900674551810);
INSERT INTO `sys_role_menu` (`ROLE_ID`, `MENU_ID`) VALUES (13, 12);
INSERT INTO `sys_role_menu` (`ROLE_ID`, `MENU_ID`) VALUES (13, 6);
INSERT INTO `sys_role_menu` (`ROLE_ID`, `MENU_ID`) VALUES (13, 7);
INSERT INTO `sys_role_menu` (`ROLE_ID`, `MENU_ID`) VALUES (13, 4);
INSERT INTO `sys_role_menu` (`ROLE_ID`, `MENU_ID`) VALUES (13, 5);
COMMIT;

-- ----------------------------
-- Table structure for sys_route_conf
-- ----------------------------
DROP TABLE IF EXISTS `sys_route_conf`;
CREATE TABLE `sys_route_conf` (
  `id` bigint NOT NULL COMMENT '主键',
  `name` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL,
  `route_id` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL,
  `predicates` json DEFAULT NULL COMMENT '断言',
  `filters` json DEFAULT NULL COMMENT '过滤器',
  `uri` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL,
  `sort_order` int DEFAULT '0' COMMENT '排序',
  `metadata` json DEFAULT NULL COMMENT '路由元信息',
  `create_by` varchar(64) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NOT NULL DEFAULT ' ' COMMENT '创建人',
  `update_by` varchar(64) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NOT NULL DEFAULT ' ' COMMENT '修改人',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
  `del_flag` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT '0',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='路由配置表';

-- ----------------------------
-- Records of sys_route_conf
-- ----------------------------
BEGIN;
INSERT INTO `sys_route_conf` (`id`, `name`, `route_id`, `predicates`, `filters`, `uri`, `sort_order`, `metadata`, `create_by`, `update_by`, `create_time`, `update_time`, `del_flag`) VALUES (1674965850066325505, 'system模块', 'art-server-system', '[{\"args\": {\"_genkey_0\": \"/system/**\"}, \"name\": \"Path\"}]', '[{\"args\": {\"key-resolver\": \"#{@remoteAddrKeyResolver}\", \"redis-rate-limiter.burstCapacity\": \"100\", \"redis-rate-limiter.replenishRate\": \"100\"}, \"name\": \"RequestRateLimiter\"}]', 'lb://art-server-system', 0, NULL, 'fxz', 'fxz', '2023-07-01 10:19:13', '2023-07-01 10:19:13', '0');
INSERT INTO `sys_route_conf` (`id`, `name`, `route_id`, `predicates`, `filters`, `uri`, `sort_order`, `metadata`, `create_by`, `update_by`, `create_time`, `update_time`, `del_flag`) VALUES (1674965850087297025, 'auth模块', 'art-auth', '[{\"args\": {\"_genkey_0\": \"/auth/**\"}, \"name\": \"Path\"}]', NULL, 'lb://art-auth', 1, NULL, 'fxz', 'fxz', '2023-07-01 10:19:13', '2023-07-01 10:19:13', '0');
INSERT INTO `sys_route_conf` (`id`, `name`, `route_id`, `predicates`, `filters`, `uri`, `sort_order`, `metadata`, `create_by`, `update_by`, `create_time`, `update_time`, `del_flag`) VALUES (1674965850099879937, 'edu模块', 'art-edu', '[{\"args\": {\"_genkey_0\": \"/edu/**\"}, \"name\": \"Path\"}]', NULL, 'lb://art-edu', 1, NULL, 'fxz', 'fxz', '2023-07-01 10:19:13', '2023-07-01 10:19:13', '0');
INSERT INTO `sys_route_conf` (`id`, `name`, `route_id`, `predicates`, `filters`, `uri`, `sort_order`, `metadata`, `create_by`, `update_by`, `create_time`, `update_time`, `del_flag`) VALUES (1674965850125045761, '代码生成器模块', 'art-generate', '[{\"args\": {\"_genkey_0\": \"/generate/**\"}, \"name\": \"Path\"}]', NULL, 'lb://art-generate', 2, NULL, 'fxz', 'fxz', '2023-07-01 10:19:13', '2023-07-01 10:19:13', '0');
INSERT INTO `sys_route_conf` (`id`, `name`, `route_id`, `predicates`, `filters`, `uri`, `sort_order`, `metadata`, `create_by`, `update_by`, `create_time`, `update_time`, `del_flag`) VALUES (1674965850150211585, '定时任务模块', 'art-job', '[{\"args\": {\"_genkey_0\": \"/schedule/**\"}, \"name\": \"Path\"}]', NULL, 'lb://art-job', 3, NULL, 'fxz', 'fxz', '2023-07-01 10:19:13', '2023-07-01 10:19:13', '0');
INSERT INTO `sys_route_conf` (`id`, `name`, `route_id`, `predicates`, `filters`, `uri`, `sort_order`, `metadata`, `create_by`, `update_by`, `create_time`, `update_time`, `del_flag`) VALUES (1674965850171183105, 'demo模块', 'art-demo', '[{\"args\": {\"_genkey_0\": \"/demos/**\"}, \"name\": \"Path\"}]', NULL, 'lb://art-demo', 4, NULL, 'fxz', 'fxz', '2023-07-01 10:19:13', '2023-07-01 10:19:13', '0');
INSERT INTO `sys_route_conf` (`id`, `name`, `route_id`, `predicates`, `filters`, `uri`, `sort_order`, `metadata`, `create_by`, `update_by`, `create_time`, `update_time`, `del_flag`) VALUES (1948744693558804481, 'ai模块', 'art-ai', '[{\"args\": {\"_genkey_0\": \"/ai/**\"}, \"name\": \"Path\"}]', '[{\"args\": {\"key-resolver\": \"#{@remoteAddrKeyResolver}\", \"redis-rate-limiter.burstCapacity\": \"100\", \"redis-rate-limiter.replenishRate\": \"100\"}, \"name\": \"RequestRateLimiter\"}]', 'lb://art-ai', 0, NULL, 'fxz', 'fxz', '2025-07-25 21:58:36', '2025-07-25 21:58:36', '0');
COMMIT;

-- ----------------------------
-- Table structure for sys_tenant
-- ----------------------------
DROP TABLE IF EXISTS `sys_tenant`;
CREATE TABLE `sys_tenant` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '租户id',
  `name` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '租户名',
  `tenant_admin_id` bigint DEFAULT NULL COMMENT '当前租户管理员id',
  `tenant_admin_name` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '当前租户管理员姓名',
  `tenant_admin_mobile` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '当前租户管理员手机号',
  `status` tinyint NOT NULL DEFAULT '0' COMMENT '租户状态（0正常 1停用）',
  `package_id` bigint NOT NULL COMMENT '租户套餐id',
  `expire_time` datetime NOT NULL COMMENT '过期时间',
  `account_count` int NOT NULL COMMENT '账号数量',
  `create_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT '' COMMENT '创建者',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT '' COMMENT '更新者',
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `del_flag` bit(1) NOT NULL DEFAULT b'0' COMMENT '0-正常，1-删除',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=1666742445684912130 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='租户表';

-- ----------------------------
-- Records of sys_tenant
-- ----------------------------
BEGIN;
INSERT INTO `sys_tenant` (`id`, `name`, `tenant_admin_id`, `tenant_admin_name`, `tenant_admin_mobile`, `status`, `package_id`, `expire_time`, `account_count`, `create_by`, `create_time`, `update_by`, `update_time`, `del_flag`) VALUES (1, 'art', 41, 'fxz', '19812341234', 0, 1648145956129792001, '3000-03-30 00:00:00', 1000, '1', '2022-02-22 00:56:14', 'fxz', '2023-06-08 17:42:20', b'0');
INSERT INTO `sys_tenant` (`id`, `name`, `tenant_admin_id`, `tenant_admin_name`, `tenant_admin_mobile`, `status`, `package_id`, `expire_time`, `account_count`, `create_by`, `create_time`, `update_by`, `update_time`, `del_flag`) VALUES (1658121488846090241, '清华大学', 41, '张一山', '19812341111', 0, 1648145956129792001, '2023-05-31 22:45:36', 10, 'fxz', '2023-05-15 22:45:44', 'fxz', '2023-06-08 17:42:20', b'0');
INSERT INTO `sys_tenant` (`id`, `name`, `tenant_admin_id`, `tenant_admin_name`, `tenant_admin_mobile`, `status`, `package_id`, `expire_time`, `account_count`, `create_by`, `create_time`, `update_by`, `update_time`, `del_flag`) VALUES (1658121627035824129, '北京大学', 41, '张二山', '19812349999', 0, 1648145956129792001, '2023-06-02 22:46:09', 5, 'fxz', '2023-05-15 22:46:17', 'fxz', '2023-06-08 17:42:20', b'0');
COMMIT;

-- ----------------------------
-- Table structure for sys_tenant_package
-- ----------------------------
DROP TABLE IF EXISTS `sys_tenant_package`;
CREATE TABLE `sys_tenant_package` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '套餐id',
  `name` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '套餐名',
  `status` tinyint NOT NULL DEFAULT '0' COMMENT '套餐状态（0正常 1停用）',
  `remark` varchar(256) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT '' COMMENT '备注',
  `menu_ids` varchar(2048) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '关联的菜单编号',
  `create_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '创建者',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT '' COMMENT '更新者',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `del_flag` bit(1) NOT NULL DEFAULT b'0' COMMENT '0-正常，1-删除',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=1654380073645330434 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='租户套餐表';

-- ----------------------------
-- Records of sys_tenant_package
-- ----------------------------
BEGIN;
INSERT INTO `sys_tenant_package` (`id`, `name`, `status`, `remark`, `menu_ids`, `create_by`, `create_time`, `update_by`, `update_time`, `del_flag`) VALUES (1648145956129792001, '租户套餐', 0, '', '12,4,5,6,7,21,29,100006,100009,100010,100011,100007,100008,100022,100025,100026,20,100024,100046,100021', 'fxz', '2023-04-18 10:06:32', 'fxz', '2023-04-25 11:00:38', b'0');
COMMIT;

-- ----------------------------
-- Table structure for sys_user
-- ----------------------------
DROP TABLE IF EXISTS `sys_user`;
CREATE TABLE `sys_user` (
  `user_id` bigint NOT NULL AUTO_INCREMENT COMMENT '用户ID',
  `username` varchar(50) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NOT NULL COMMENT '用户名',
  `password` varchar(128) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NOT NULL COMMENT '密码',
  `dept_id` bigint DEFAULT NULL COMMENT '部门ID',
  `email` varchar(128) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci DEFAULT NULL COMMENT '邮箱',
  `mobile` varchar(20) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci DEFAULT NULL COMMENT '联系电话',
  `status` char(1) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NOT NULL DEFAULT '1' COMMENT '状态 0锁定 1有效',
  `create_time` datetime NOT NULL COMMENT '创建时间',
  `update_time` datetime DEFAULT NULL COMMENT '修改时间',
  `last_login_time` datetime DEFAULT NULL COMMENT '最近访问时间',
  `ssex` char(1) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci DEFAULT NULL COMMENT '性别 0男 1女 2保密',
  `avatar` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci DEFAULT NULL COMMENT '头像',
  `description` varchar(100) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci DEFAULT NULL COMMENT '描述',
  `create_by` varchar(100) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci DEFAULT NULL COMMENT '创建人',
  `update_by` varchar(100) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci DEFAULT NULL COMMENT '更新人',
  `tenant_id` bigint DEFAULT NULL COMMENT '租户id',
  PRIMARY KEY (`user_id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=42 DEFAULT CHARSET=utf8mb3 COMMENT='用户表';

-- ----------------------------
-- Records of sys_user
-- ----------------------------
BEGIN;
INSERT INTO `sys_user` (`user_id`, `username`, `password`, `dept_id`, `email`, `mobile`, `status`, `create_time`, `update_time`, `last_login_time`, `ssex`, `avatar`, `description`, `create_by`, `update_by`, `tenant_id`) VALUES (1, 'fxz', '{bcrypt}$2a$10$XZOjtFLmR7yE9dRPqLavCechdOZnCG9KZdYg7jjuvGs0Aas4dDWoC', 0, '1****@qq.com', '19806082431', '1', '2022-04-05 20:39:22', '2023-05-24 10:35:34', '2022-04-05 15:57:00', '0', '/system/file/download/art/2023-05-21.6b9670593fb14de98d7abd685e7f9303.png', 'Art的作者,Art社区的核心成员。', NULL, 'fxz', 1);
COMMIT;

-- ----------------------------
-- Table structure for sys_user_post
-- ----------------------------
DROP TABLE IF EXISTS `sys_user_post`;
CREATE TABLE `sys_user_post` (
  `user_id` bigint NOT NULL COMMENT '用户ID',
  `post_id` bigint NOT NULL COMMENT '岗位ID',
  PRIMARY KEY (`user_id`,`post_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='用户与岗位关联表';

-- ----------------------------
-- Records of sys_user_post
-- ----------------------------
BEGIN;
COMMIT;

-- ----------------------------
-- Table structure for sys_user_role
-- ----------------------------
DROP TABLE IF EXISTS `sys_user_role`;
CREATE TABLE `sys_user_role` (
  `USER_ID` bigint NOT NULL COMMENT '用户ID',
  `ROLE_ID` bigint NOT NULL COMMENT '角色ID'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3 COMMENT='用户角色关联表';

-- ----------------------------
-- Records of sys_user_role
-- ----------------------------
BEGIN;
INSERT INTO `sys_user_role` (`USER_ID`, `ROLE_ID`) VALUES (1, 1);
COMMIT;

-- ----------------------------
-- Table structure for sys_users_gitee
-- ----------------------------
DROP TABLE IF EXISTS `sys_users_gitee`;
CREATE TABLE `sys_users_gitee` (
  `users_gitee_id` bigint NOT NULL AUTO_INCREMENT COMMENT '码云Gitee用户表主键，自增',
  `users_id` bigint DEFAULT NULL COMMENT '绑定的用户主键，唯一键：uk__users_gitee__appid__id__users_id',
  `appid` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL COMMENT 'AppID(码云Gitee client_id)，唯一键：uk__users_gitee__appid__id',
  `id` int NOT NULL COMMENT '码云Gitee唯一标识，不为空，唯一键：uk__users_gitee__appid__id',
  `login` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL COMMENT '码云Gitee登录用户名，不为空',
  `name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL COMMENT '码云Gitee用户名，不为空',
  `avatar_url` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL COMMENT '头像',
  `url` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL COMMENT '公开资料URL',
  `html_url` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL COMMENT '空间URL',
  `remark` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL COMMENT '企业备注名',
  `followers_url` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL COMMENT '粉丝URL',
  `following_url` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `gists_url` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `starred_url` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL COMMENT 'star项目URL',
  `subscriptions_url` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL COMMENT '订阅项目URL',
  `organizations_url` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL COMMENT '组织URL',
  `repos_url` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL COMMENT '仓库URL',
  `events_url` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `received_events_url` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL COMMENT '接收事件',
  `type` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL COMMENT '类型',
  `blog` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL COMMENT '博客地址',
  `weibo` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL COMMENT '微博地址',
  `bio` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL COMMENT '自我介绍',
  `public_repos` int DEFAULT NULL COMMENT '公共仓库数',
  `public_gists` int DEFAULT NULL,
  `followers` int DEFAULT NULL COMMENT '粉丝数',
  `following` int DEFAULT NULL COMMENT '关注的人',
  `stared` int DEFAULT NULL COMMENT 'star数',
  `watched` int DEFAULT NULL COMMENT '关注的仓库',
  `created_at` datetime DEFAULT NULL COMMENT '创建时间',
  `updated_at` datetime DEFAULT NULL COMMENT '更新时间',
  `email` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL COMMENT '邮箱',
  `binding_date` datetime DEFAULT NULL COMMENT '绑定时间',
  `access_token` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL COMMENT '授权凭证',
  `refresh_token` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL COMMENT '刷新凭证',
  `expires` datetime DEFAULT NULL COMMENT '过期时间',
  `scope` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL COMMENT '授权范围',
  `company` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL COMMENT '公司',
  `profession` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL COMMENT '职务',
  `wechat` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL COMMENT '微信',
  `qq` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL COMMENT 'QQ',
  `linkedin` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL COMMENT '领英账户',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '更新时间，不为空',
  `create_time` datetime DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '创建时间，未更新时为空',
  `create_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '创建人，不为空',
  `update_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '更新人，未更新时为空',
  `del_flag` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL DEFAULT '0' COMMENT '逻辑删除，0 未删除，1 删除，MySQL 默认值 0，不为 NULL，注解@TableLogic。',
  PRIMARY KEY (`users_gitee_id`) USING BTREE,
  UNIQUE KEY `uk__users_gitee__appid__id` (`appid`,`id`) USING BTREE,
  UNIQUE KEY `uk__users_gitee__appid__id__users_id` (`appid`,`id`,`users_id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=1647799255183495171 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin COMMENT='码云Gitee用户表';

-- ----------------------------
-- Records of sys_users_gitee
-- ----------------------------
BEGIN;
INSERT INTO `sys_users_gitee` (`users_gitee_id`, `users_id`, `appid`, `id`, `login`, `name`, `avatar_url`, `url`, `html_url`, `remark`, `followers_url`, `following_url`, `gists_url`, `starred_url`, `subscriptions_url`, `organizations_url`, `repos_url`, `events_url`, `received_events_url`, `type`, `blog`, `weibo`, `bio`, `public_repos`, `public_gists`, `followers`, `following`, `stared`, `watched`, `created_at`, `updated_at`, `email`, `binding_date`, `access_token`, `refresh_token`, `expires`, `scope`, `company`, `profession`, `wechat`, `qq`, `linkedin`, `update_time`, `create_time`, `create_by`, `update_by`, `del_flag`) VALUES (1647797756575453186, 15, '94026f6491b102051726e28c718ab4706bb3a681939aee222119ef987a7170bf', 11559588, 'sssssssllll', 'sssssssllll', 'https://gitee.com/assets/no_portrait.png', 'https://gitee.com/api/v5/users/sssssssllll', 'https://gitee.com/sssssssllll', '', 'https://gitee.com/api/v5/users/sssssssllll/followers', 'https://gitee.com/api/v5/users/sssssssllll/following_url{/other_user}', 'https://gitee.com/api/v5/users/sssssssllll/gists{/gist_id}', 'https://gitee.com/api/v5/users/sssssssllll/starred{/owner}{/repo}', 'https://gitee.com/api/v5/users/sssssssllll/subscriptions', 'https://gitee.com/api/v5/users/sssssssllll/orgs', 'https://gitee.com/api/v5/users/sssssssllll/repos', 'https://gitee.com/api/v5/users/sssssssllll/events{/privacy}', 'https://gitee.com/api/v5/users/sssssssllll/received_events', 'User', NULL, NULL, NULL, 8, 0, 0, 1, 7, 9, '2022-09-03 13:57:10', '2023-04-17 10:31:47', NULL, '2023-04-18 16:31:43', 'bb500a586d795d350e6e497113d5b082', 'e7fb1a41dd55e284a442133ddd9df572027876439b9f5b80fab118c293acca5d', '2023-04-19 16:32:10', 'user_info projects pull_requests issues notes keys hook groups gists enterprises emails', NULL, NULL, NULL, NULL, NULL, '2023-04-18 16:32:10', '2023-04-18 16:32:00', 'anonymousUser', 'anonymousUser', '0');
INSERT INTO `sys_users_gitee` (`users_gitee_id`, `users_id`, `appid`, `id`, `login`, `name`, `avatar_url`, `url`, `html_url`, `remark`, `followers_url`, `following_url`, `gists_url`, `starred_url`, `subscriptions_url`, `organizations_url`, `repos_url`, `events_url`, `received_events_url`, `type`, `blog`, `weibo`, `bio`, `public_repos`, `public_gists`, `followers`, `following`, `stared`, `watched`, `created_at`, `updated_at`, `email`, `binding_date`, `access_token`, `refresh_token`, `expires`, `scope`, `company`, `profession`, `wechat`, `qq`, `linkedin`, `update_time`, `create_time`, `create_by`, `update_by`, `del_flag`) VALUES (1647799255183495170, 1, '94026f6491b102051726e28c718ab4706bb3a681939aee222119ef987a7170bf', 7493281, 'fxzcloud', 'fxz', 'https://foruda.gitee.com/avatar/1682401752511923504/7493281_fxzcloud_1682401752.png', 'https://gitee.com/api/v5/users/fxzcloud', 'https://gitee.com/fxzcloud', '', 'https://gitee.com/api/v5/users/fxzcloud/followers', 'https://gitee.com/api/v5/users/fxzcloud/following_url{/other_user}', 'https://gitee.com/api/v5/users/fxzcloud/gists{/gist_id}', 'https://gitee.com/api/v5/users/fxzcloud/starred{/owner}{/repo}', 'https://gitee.com/api/v5/users/fxzcloud/subscriptions', 'https://gitee.com/api/v5/users/fxzcloud/orgs', 'https://gitee.com/api/v5/users/fxzcloud/repos', 'https://gitee.com/api/v5/users/fxzcloud/events{/privacy}', 'https://gitee.com/api/v5/users/fxzcloud/received_events', 'User', 'https://fxzcloud.gitee.io/docs/', NULL, '23年应届生，拥有十余年架构经验，有年薪百万的工作欢迎联系我。（校招、进群加我微信）', 84, 0, 59, 101, 215, 220, '2020-04-24 22:01:18', '2023-04-17 11:08:49', 'fxzcloud@gmail.com', '2023-05-24 00:38:18', '8c83d4ba63ced47f6e7c40a78cbbf0ef', '35b27e09d541741b773afac12f6b8e3331286509813de822072c893e70ff6f78', '2023-07-02 11:19:24', 'user_info', NULL, NULL, NULL, NULL, NULL, '2023-07-01 11:19:25', '2023-07-01 11:19:24', 'anonymousUser', 'anonymousUser', '0');
COMMIT;

SET FOREIGN_KEY_CHECKS = 1;
