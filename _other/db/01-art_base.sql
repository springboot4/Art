
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
INSERT INTO `sys_app` (`id`, `name`, `code`, `sort`, `icon`, `create_time`, `create_by`, `update_time`, `update_by`) VALUES (1, '系统应用', 'system', 1, 'laptop', '2022-09-12 00:17:28', 'fxz', '2022-09-12 14:04:29', 'fxz');
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
INSERT INTO `sys_menu` (`id`, `parent_id`, `title`, `name`, `perms`, `type`, `component`, `path`, `redirect`, `icon`, `keep_alive`, `order_num`, `create_time`, `update_time`, `create_by`, `update_by`, `hidden`, `application`) VALUES (4, 0, '个人页', 'account', 'user', '0', 'RouteView', '/account', '', 'aliwangwang', 1, 2, '2022-01-23 17:04:07', '2022-09-12 16:11:59', NULL, 'fxz', '1', 1);
INSERT INTO `sys_menu` (`id`, `parent_id`, `title`, `name`, `perms`, `type`, `component`, `path`, `redirect`, `icon`, `keep_alive`, `order_num`, `create_time`, `update_time`, `create_by`, `update_by`, `hidden`, `application`) VALUES (5, 4, '个人中心', 'center', 'user', '0', 'account/center', '/account/center', NULL, NULL, 1, 2, '2022-01-23 17:04:58', NULL, NULL, NULL, NULL, 1);
INSERT INTO `sys_menu` (`id`, `parent_id`, `title`, `name`, `perms`, `type`, `component`, `path`, `redirect`, `icon`, `keep_alive`, `order_num`, `create_time`, `update_time`, `create_by`, `update_by`, `hidden`, `application`) VALUES (6, 4, '个人设置', 'settings', 'user', '0', 'account/settings/Index', '/account/settings', NULL, NULL, 1, 2, '2022-01-18 14:44:25', NULL, NULL, NULL, '1', 1);
INSERT INTO `sys_menu` (`id`, `parent_id`, `title`, `name`, `perms`, `type`, `component`, `path`, `redirect`, `icon`, `keep_alive`, `order_num`, `create_time`, `update_time`, `create_by`, `update_by`, `hidden`, `application`) VALUES (7, 6, '基本设置', 'BaseSettings', 'user', '0', 'account/settings/BaseSetting', '/account/settings/base', NULL, NULL, 1, 2, '2022-01-18 14:48:57', NULL, NULL, NULL, '0', 1);
INSERT INTO `sys_menu` (`id`, `parent_id`, `title`, `name`, `perms`, `type`, `component`, `path`, `redirect`, `icon`, `keep_alive`, `order_num`, `create_time`, `update_time`, `create_by`, `update_by`, `hidden`, `application`) VALUES (8, 6, '安全设置', 'SecuritySettings', 'user', '0', 'account/settings/Security', '/account/settings/security', NULL, NULL, 1, 2, '2022-01-18 14:49:39', NULL, NULL, NULL, NULL, 1);
INSERT INTO `sys_menu` (`id`, `parent_id`, `title`, `name`, `perms`, `type`, `component`, `path`, `redirect`, `icon`, `keep_alive`, `order_num`, `create_time`, `update_time`, `create_by`, `update_by`, `hidden`, `application`) VALUES (9, 6, '个性化设置', 'CustomSettings', 'user', '0', 'account/settings/Custom', '/account/settings/custom', NULL, NULL, 1, 2, '2022-01-18 14:50:17', NULL, NULL, NULL, NULL, 1);
INSERT INTO `sys_menu` (`id`, `parent_id`, `title`, `name`, `perms`, `type`, `component`, `path`, `redirect`, `icon`, `keep_alive`, `order_num`, `create_time`, `update_time`, `create_by`, `update_by`, `hidden`, `application`) VALUES (10, 6, '账户绑定', 'BindingSettings', 'user', '0', 'account/settings/Binding', '/account/settings/binding', NULL, NULL, 1, 2, '2022-01-18 14:50:58', NULL, NULL, NULL, NULL, 1);
INSERT INTO `sys_menu` (`id`, `parent_id`, `title`, `name`, `perms`, `type`, `component`, `path`, `redirect`, `icon`, `keep_alive`, `order_num`, `create_time`, `update_time`, `create_by`, `update_by`, `hidden`, `application`) VALUES (11, 6, '新消息通知', 'NotificationSettings', 'user', '0', 'account/settings/Notification', '/account/settings/notification', NULL, NULL, 1, 2, '2022-01-18 14:51:31', NULL, NULL, NULL, NULL, 1);
INSERT INTO `sys_menu` (`id`, `parent_id`, `title`, `name`, `perms`, `type`, `component`, `path`, `redirect`, `icon`, `keep_alive`, `order_num`, `create_time`, `update_time`, `create_by`, `update_by`, `hidden`, `application`) VALUES (12, 0, '首页', 'welcome', 'welcome', '0', 'analysis', '/welcome', '', 'twitter', 1, 0, '2022-01-18 16:22:11', '2022-11-26 20:02:35', NULL, 'fxz', '0', 1);
INSERT INTO `sys_menu` (`id`, `parent_id`, `title`, `name`, `perms`, `type`, `component`, `path`, `redirect`, `icon`, `keep_alive`, `order_num`, `create_time`, `update_time`, `create_by`, `update_by`, `hidden`, `application`) VALUES (20, 0, '权限管理', 'permissions', 'sys', '0', 'RouteView', '/permissions', NULL, 'rocket', 1, 3, '2022-01-23 16:52:53', '2022-09-12 16:17:19', NULL, 'fxz', '0', 1);
INSERT INTO `sys_menu` (`id`, `parent_id`, `title`, `name`, `perms`, `type`, `component`, `path`, `redirect`, `icon`, `keep_alive`, `order_num`, `create_time`, `update_time`, `create_by`, `update_by`, `hidden`, `application`) VALUES (21, 20, '菜单管理', 'menu', 'sys:menu', '0', 'system/menu/menu', '/permissions/menu', NULL, 'idcard', 1, 0, '2022-01-23 16:55:48', '2022-04-04 17:31:22', NULL, 'fxz', '0', 1);
INSERT INTO `sys_menu` (`id`, `parent_id`, `title`, `name`, `perms`, `type`, `component`, `path`, `redirect`, `icon`, `keep_alive`, `order_num`, `create_time`, `update_time`, `create_by`, `update_by`, `hidden`, `application`) VALUES (29, 21, '添加菜单', 'saveMenu', 'sys:menu:save', '1', NULL, NULL, NULL, NULL, 1, 1, '2022-01-23 18:16:31', NULL, NULL, NULL, '1', 1);
INSERT INTO `sys_menu` (`id`, `parent_id`, `title`, `name`, `perms`, `type`, `component`, `path`, `redirect`, `icon`, `keep_alive`, `order_num`, `create_time`, `update_time`, `create_by`, `update_by`, `hidden`, `application`) VALUES (100006, 20, '用户管理', 'user', 'sys:user', '0', 'system/user/userList.vue', '/permissions/user', NULL, 'user', 1, 1, '2022-02-27 09:35:00', '2022-04-05 20:24:14', NULL, 'fxz', '0', 1);
INSERT INTO `sys_menu` (`id`, `parent_id`, `title`, `name`, `perms`, `type`, `component`, `path`, `redirect`, `icon`, `keep_alive`, `order_num`, `create_time`, `update_time`, `create_by`, `update_by`, `hidden`, `application`) VALUES (100007, 20, '角色管理', 'role', 'sys:role', '0', 'system/role/RoleList.vue', '/permissions/role', NULL, 'woman', 1, 2, '2022-02-27 09:43:02', '2022-04-05 20:24:23', NULL, 'fxz', '0', 1);
INSERT INTO `sys_menu` (`id`, `parent_id`, `title`, `name`, `perms`, `type`, `component`, `path`, `redirect`, `icon`, `keep_alive`, `order_num`, `create_time`, `update_time`, `create_by`, `update_by`, `hidden`, `application`) VALUES (100008, 20, '部门管理', 'sys:dept', 'dept', '0', 'system/dept/DeptList.vue', '/permissions/dept', NULL, 'contacts', 1, 3, '2022-02-27 10:15:46', '2022-06-26 21:16:18', NULL, 'fxz', '0', 1);
INSERT INTO `sys_menu` (`id`, `parent_id`, `title`, `name`, `perms`, `type`, `component`, `path`, `redirect`, `icon`, `keep_alive`, `order_num`, `create_time`, `update_time`, `create_by`, `update_by`, `hidden`, `application`) VALUES (100009, 100006, '分页查看用户信息', 'sys:user:view', 'sys:user:view', '1', 'sys:user:view', '/sys/user/list', NULL, NULL, 1, 1, '2022-02-27 13:43:05', NULL, NULL, NULL, '0', 1);
INSERT INTO `sys_menu` (`id`, `parent_id`, `title`, `name`, `perms`, `type`, `component`, `path`, `redirect`, `icon`, `keep_alive`, `order_num`, `create_time`, `update_time`, `create_by`, `update_by`, `hidden`, `application`) VALUES (100010, 100006, '更新用户信息', 'sys:user:update', 'sys:user:update', '1', NULL, 'sys:user:update', NULL, NULL, 1, NULL, '2022-02-27 17:05:44', NULL, NULL, NULL, '0', 1);
INSERT INTO `sys_menu` (`id`, `parent_id`, `title`, `name`, `perms`, `type`, `component`, `path`, `redirect`, `icon`, `keep_alive`, `order_num`, `create_time`, `update_time`, `create_by`, `update_by`, `hidden`, `application`) VALUES (100011, 100006, '新增用户信息', 'sys:user:add', 'sys:user:add', '1', NULL, 'sys:user:add', NULL, NULL, 1, NULL, '2022-02-27 18:07:12', NULL, NULL, NULL, '0', 1);
INSERT INTO `sys_menu` (`id`, `parent_id`, `title`, `name`, `perms`, `type`, `component`, `path`, `redirect`, `icon`, `keep_alive`, `order_num`, `create_time`, `update_time`, `create_by`, `update_by`, `hidden`, `application`) VALUES (100012, 0, '开发工具', 'sysTool', 'sysTool', '0', 'RouteView', '/sysTool', NULL, 'shopping-cart', 1, 999, '2022-03-04 09:14:37', '2022-10-23 14:55:08', NULL, 'fxz', '0', 1);
INSERT INTO `sys_menu` (`id`, `parent_id`, `title`, `name`, `perms`, `type`, `component`, `path`, `redirect`, `icon`, `keep_alive`, `order_num`, `create_time`, `update_time`, `create_by`, `update_by`, `hidden`, `application`) VALUES (100013, 100012, '代码生成器', 'genCode', 'sysTool:genCode', '0', 'system/gen/CodeGenIndex.vue', '/sysTool/genCode', NULL, 'area-chart', 1, 1, '2022-03-04 09:19:37', '2022-04-04 17:33:00', NULL, 'fxz', '0', 1);
INSERT INTO `sys_menu` (`id`, `parent_id`, `title`, `name`, `perms`, `type`, `component`, `path`, `redirect`, `icon`, `keep_alive`, `order_num`, `create_time`, `update_time`, `create_by`, `update_by`, `hidden`, `application`) VALUES (100016, 100012, '数据源管理', 'DatasourceConfList', 'sysTool:datasourceConf', '0', 'system/gen/datasource/DatasourceConfList.vue', '/sysTool/datasourceConf', NULL, 'box-plot', 1, 0, '2022-03-31 12:31:26', '2022-04-04 17:33:10', 'fxz', 'fxz', '0', 1);
INSERT INTO `sys_menu` (`id`, `parent_id`, `title`, `name`, `perms`, `type`, `component`, `path`, `redirect`, `icon`, `keep_alive`, `order_num`, `create_time`, `update_time`, `create_by`, `update_by`, `hidden`, `application`) VALUES (100017, 0, '系统监控', 'monitor', 'sysMonitor', '0', 'RouteView', '/sysMonitor', NULL, 'aliwangwang', 1, 1000, '2022-04-03 17:59:52', '2022-09-12 16:13:15', 'fxz', 'fxz', '0', 1);
INSERT INTO `sys_menu` (`id`, `parent_id`, `title`, `name`, `perms`, `type`, `component`, `path`, `redirect`, `icon`, `keep_alive`, `order_num`, `create_time`, `update_time`, `create_by`, `update_by`, `hidden`, `application`) VALUES (100018, 100017, '定时任务', 'jobList', 'sysMonitor:job', '0', 'system/monitor/JobList.vue', '/sysMonitor/job', NULL, 'loading', 1, 0, '2022-04-03 18:02:04', '2022-04-04 17:33:39', 'fxz', 'fxz', '0', 1);
INSERT INTO `sys_menu` (`id`, `parent_id`, `title`, `name`, `perms`, `type`, `component`, `path`, `redirect`, `icon`, `keep_alive`, `order_num`, `create_time`, `update_time`, `create_by`, `update_by`, `hidden`, `application`) VALUES (100019, 100046, '字典管理', 'dict', 'sys:dict', '0', 'system/dict/DictList.vue', '/permissions/dict', NULL, 'folder-open', 1, 4, '2022-04-04 11:20:33', '2022-09-12 16:20:41', 'fxz', 'fxz', '0', 1);
INSERT INTO `sys_menu` (`id`, `parent_id`, `title`, `name`, `perms`, `type`, `component`, `path`, `redirect`, `icon`, `keep_alive`, `order_num`, `create_time`, `update_time`, `create_by`, `update_by`, `hidden`, `application`) VALUES (100020, 100017, '调度日志', 'JobLogList', 'sysMonitor:jobLog', '0', 'system/monitor/JobLogList.vue', '/sysMonitor/jobLog', NULL, 'hdd', 1, 1, '2022-04-04 15:55:43', '2022-04-04 17:34:06', 'fxz', 'fxz', '0', 1);
INSERT INTO `sys_menu` (`id`, `parent_id`, `title`, `name`, `perms`, `type`, `component`, `path`, `redirect`, `icon`, `keep_alive`, `order_num`, `create_time`, `update_time`, `create_by`, `update_by`, `hidden`, `application`) VALUES (100021, 100046, '文件管理', 'file', 'sys:file', '0', 'system/file/FileList.vue', '/permissions/file', NULL, 'mail', 1, 5, '2022-04-04 22:47:38', '2022-09-12 16:21:04', 'fxz', 'fxz', '0', 1);
INSERT INTO `sys_menu` (`id`, `parent_id`, `title`, `name`, `perms`, `type`, `component`, `path`, `redirect`, `icon`, `keep_alive`, `order_num`, `create_time`, `update_time`, `create_by`, `update_by`, `hidden`, `application`) VALUES (100022, 20, '岗位管理', 'post', 'sys:post', '0', 'system/post/PostList.vue', '/permissions/post', NULL, 'coffee', 1, 4, '2022-04-05 20:17:47', '2022-04-05 20:24:58', 'fxz', 'fxz', '0', 1);
INSERT INTO `sys_menu` (`id`, `parent_id`, `title`, `name`, `perms`, `type`, `component`, `path`, `redirect`, `icon`, `keep_alive`, `order_num`, `create_time`, `update_time`, `create_by`, `update_by`, `hidden`, `application`) VALUES (100023, 100012, '表单设计器', 'FormBuild', 'sysTool:formBuild', '0', 'system/gen/FormBuild.vue', '/sysTool/formBuild', NULL, 'bg-colors', 1, 2, '2022-04-23 23:22:38', '2022-04-23 23:38:03', 'fxz', 'fxz', '0', 1);
INSERT INTO `sys_menu` (`id`, `parent_id`, `title`, `name`, `perms`, `type`, `component`, `path`, `redirect`, `icon`, `keep_alive`, `order_num`, `create_time`, `update_time`, `create_by`, `update_by`, `hidden`, `application`) VALUES (100024, 100046, '审计管理', 'oper', 'sys:oper', '0', 'RouteView', '/permissions/oper', NULL, 'credit-card', 1, 7, '2022-04-24 00:12:34', '2022-09-12 16:21:38', 'fxz', 'fxz', '0', 1);
INSERT INTO `sys_menu` (`id`, `parent_id`, `title`, `name`, `perms`, `type`, `component`, `path`, `redirect`, `icon`, `keep_alive`, `order_num`, `create_time`, `update_time`, `create_by`, `update_by`, `hidden`, `application`) VALUES (100025, 100024, '登录日志', 'OperLogin', 'sys:oper:login', '0', 'system/log/OperLogList.vue', '/sys/oper/login', NULL, 'logout', 1, 0, '2022-04-24 00:15:20', '2022-04-24 00:16:26', 'fxz', 'fxz', '0', 1);
INSERT INTO `sys_menu` (`id`, `parent_id`, `title`, `name`, `perms`, `type`, `component`, `path`, `redirect`, `icon`, `keep_alive`, `order_num`, `create_time`, `update_time`, `create_by`, `update_by`, `hidden`, `application`) VALUES (100026, 100024, '操作日志', 'OperLogList', 'sys:oper:log', '0', 'system/log/OperLogList.vue', '/sys/oper/log', NULL, 'stock', 1, 1, '2022-04-24 00:17:31', '2022-04-24 00:17:31', 'fxz', 'fxz', '0', 1);
INSERT INTO `sys_menu` (`id`, `parent_id`, `title`, `name`, `perms`, `type`, `component`, `path`, `redirect`, `icon`, `keep_alive`, `order_num`, `create_time`, `update_time`, `create_by`, `update_by`, `hidden`, `application`) VALUES (100028, 100027, '品牌管理', 'brandList', 'product:brand', '0', 'mall/product/brand/BrandList', '/product/brand', NULL, 'taobao', 1, 0, '2022-05-05 20:15:54', '2022-05-05 20:15:54', 'fxz', 'fxz', '0', 2);
INSERT INTO `sys_menu` (`id`, `parent_id`, `title`, `name`, `perms`, `type`, `component`, `path`, `redirect`, `icon`, `keep_alive`, `order_num`, `create_time`, `update_time`, `create_by`, `update_by`, `hidden`, `application`) VALUES (100029, 100027, '分类管理', 'categoryList', 'product:category', '0', 'mall/product/category/CategoryList', '/product/category', NULL, 'layout', 1, 1, '2022-05-05 20:16:57', '2022-05-05 20:16:57', 'fxz', 'fxz', '0', 2);
INSERT INTO `sys_menu` (`id`, `parent_id`, `title`, `name`, `perms`, `type`, `component`, `path`, `redirect`, `icon`, `keep_alive`, `order_num`, `create_time`, `update_time`, `create_by`, `update_by`, `hidden`, `application`) VALUES (100030, 100027, '商品上架', 'goodInfo', 'product:good', '0', 'mall/product/goods/Info', '/goods/info', NULL, 'shop', 0, -1, '2022-05-06 16:49:42', '2022-08-11 13:51:33', 'fxz', 'fxz', '0', 2);
INSERT INTO `sys_menu` (`id`, `parent_id`, `title`, `name`, `perms`, `type`, `component`, `path`, `redirect`, `icon`, `keep_alive`, `order_num`, `create_time`, `update_time`, `create_by`, `update_by`, `hidden`, `application`) VALUES (100031, 100027, '商品列表', 'goodsList', 'product:goods', '0', 'mall/product/goods/index.vue', '/product/goods', NULL, 'database', 1, -2, '2022-05-09 09:19:51', '2022-05-09 09:19:51', 'fxz', 'fxz', '0', 2);
INSERT INTO `sys_menu` (`id`, `parent_id`, `title`, `name`, `perms`, `type`, `component`, `path`, `redirect`, `icon`, `keep_alive`, `order_num`, `create_time`, `update_time`, `create_by`, `update_by`, `hidden`, `application`) VALUES (100032, 100036, '订单列表', 'OrderList', 'order:list', '0', 'mall/orders/order/OrderList', '/order/list', NULL, 'shopping-cart', 1, 4, '2022-05-18 17:54:51', '2022-08-11 21:08:53', 'fxz', 'fxz', '0', 2);
INSERT INTO `sys_menu` (`id`, `parent_id`, `title`, `name`, `perms`, `type`, `component`, `path`, `redirect`, `icon`, `keep_alive`, `order_num`, `create_time`, `update_time`, `create_by`, `update_by`, `hidden`, `application`) VALUES (100033, 100035, '会员列表', 'MemberList', 'user:member', '0', 'mall/user/member/MemberList', '/user/member', NULL, 'smile', 1, 5, '2022-05-18 20:25:28', '2022-08-11 21:02:26', 'fxz', 'fxz', '0', 2);
INSERT INTO `sys_menu` (`id`, `parent_id`, `title`, `name`, `perms`, `type`, `component`, `path`, `redirect`, `icon`, `keep_alive`, `order_num`, `create_time`, `update_time`, `create_by`, `update_by`, `hidden`, `application`) VALUES (100034, 100046, '令牌管理', 'tokenList', 'sys:token', '0', 'system/token/index.vue', '/permissions/token', NULL, 'safety-certificate', 1, 5, '2022-06-26 21:18:21', '2022-09-12 16:21:54', 'fxz', 'fxz', '0', 1);
INSERT INTO `sys_menu` (`id`, `parent_id`, `title`, `name`, `perms`, `type`, `component`, `path`, `redirect`, `icon`, `keep_alive`, `order_num`, `create_time`, `update_time`, `create_by`, `update_by`, `hidden`, `application`) VALUES (100038, 100037, '秒杀活动', 'seckillList', 'promotion:seckill', '0', 'mall/promotion/seckill/SeckillList', '/promotion/seckill', NULL, 'pay-circle', 1, 0, '2022-08-12 10:35:44', '2022-08-12 10:58:28', 'fxz', 'fxz', '0', 2);
INSERT INTO `sys_menu` (`id`, `parent_id`, `title`, `name`, `perms`, `type`, `component`, `path`, `redirect`, `icon`, `keep_alive`, `order_num`, `create_time`, `update_time`, `create_by`, `update_by`, `hidden`, `application`) VALUES (100039, 100037, '秒杀商品管理', 'SeckillManage', 'seckill:manage', '0', 'mall/promotion/seckill/SeckillManage', '/promotion/seckill/manage', NULL, 'border-inner', 1, 0, '2022-08-13 10:48:23', '2022-08-13 10:48:23', 'fxz', 'fxz', '1', 2);
INSERT INTO `sys_menu` (`id`, `parent_id`, `title`, `name`, `perms`, `type`, `component`, `path`, `redirect`, `icon`, `keep_alive`, `order_num`, `create_time`, `update_time`, `create_by`, `update_by`, `hidden`, `application`) VALUES (100040, 100046, '动态网关', 'routeIndex', 'sys:route', '0', 'system/route/Index.vue', '/sys/route', NULL, 'bg-colors', 1, 10, '2022-08-20 17:12:24', '2022-09-12 16:22:24', 'fxz', 'fxz', '0', 1);
INSERT INTO `sys_menu` (`id`, `parent_id`, `title`, `name`, `perms`, `type`, `component`, `path`, `redirect`, `icon`, `keep_alive`, `order_num`, `create_time`, `update_time`, `create_by`, `update_by`, `hidden`, `application`) VALUES (100041, 100037, '优惠券列表', 'couponList', 'promotion:coupon:list', '0', 'mall/promotion/coupon/CouponList', '/promotion/coupon/list', NULL, 'sound', 1, 2, '2022-08-29 13:28:28', '2022-08-29 13:28:28', 'fxz', 'fxz', '0', 2);
INSERT INTO `sys_menu` (`id`, `parent_id`, `title`, `name`, `perms`, `type`, `component`, `path`, `redirect`, `icon`, `keep_alive`, `order_num`, `create_time`, `update_time`, `create_by`, `update_by`, `hidden`, `application`) VALUES (100042, 100037, '优惠券活动', 'couponActivityList', 'promotion:coupon:activity', '0', 'mall/promotion/coupon/CouponActivityList', '/promotion/coupon/activity', NULL, 'fire', 1, 3, '2022-08-29 18:21:54', '2022-08-29 18:21:54', 'fxz', 'fxz', '0', 2);
INSERT INTO `sys_menu` (`id`, `parent_id`, `title`, `name`, `perms`, `type`, `component`, `path`, `redirect`, `icon`, `keep_alive`, `order_num`, `create_time`, `update_time`, `create_by`, `update_by`, `hidden`, `application`) VALUES (100043, 100046, '应用管理', 'appList', 'sys:app', '0', 'system/app/AppList.vue', '/sys/app', NULL, 'robot', 1, 6, '2022-09-12 12:55:49', '2022-09-12 16:22:11', 'fxz', 'fxz', '0', 1);
INSERT INTO `sys_menu` (`id`, `parent_id`, `title`, `name`, `perms`, `type`, `component`, `path`, `redirect`, `icon`, `keep_alive`, `order_num`, `create_time`, `update_time`, `create_by`, `update_by`, `hidden`, `application`) VALUES (100045, 100044, '11', '2', '2', '0', '2', '2', '2', '2', 0, 2, '2022-09-12 16:10:37', '2022-09-12 16:10:37', 'fxz', 'fxz', '0', 2);
INSERT INTO `sys_menu` (`id`, `parent_id`, `title`, `name`, `perms`, `type`, `component`, `path`, `redirect`, `icon`, `keep_alive`, `order_num`, `create_time`, `update_time`, `create_by`, `update_by`, `hidden`, `application`) VALUES (100046, 0, '系统管理', 'sys', 'system', '0', 'RouteView', '/sys', NULL, 'desktop', 1, 3, '2022-09-12 16:19:00', '2022-09-12 16:19:50', 'fxz', 'fxz', '0', 1);
INSERT INTO `sys_menu` (`id`, `parent_id`, `title`, `name`, `perms`, `type`, `component`, `path`, `redirect`, `icon`, `keep_alive`, `order_num`, `create_time`, `update_time`, `create_by`, `update_by`, `hidden`, `application`) VALUES (100047, 0, '租户管理', 'tenant', 'sys:tenant', '0', 'RouteView', '/tenant', NULL, 'smile', 1, 2, '2022-10-01 17:01:56', '2022-10-01 17:01:56', 'fxz', 'fxz', '0', 1);
INSERT INTO `sys_menu` (`id`, `parent_id`, `title`, `name`, `perms`, `type`, `component`, `path`, `redirect`, `icon`, `keep_alive`, `order_num`, `create_time`, `update_time`, `create_by`, `update_by`, `hidden`, `application`) VALUES (100048, 100047, '租户列表', 'tenantIndex', 'sys:tenant:index', '0', 'system/tenant/Index', '/tenant/list', NULL, 'folder-open', 1, 0, '2022-10-01 17:03:47', '2022-10-01 17:04:40', 'fxz', 'fxz', '0', 1);
INSERT INTO `sys_menu` (`id`, `parent_id`, `title`, `name`, `perms`, `type`, `component`, `path`, `redirect`, `icon`, `keep_alive`, `order_num`, `create_time`, `update_time`, `create_by`, `update_by`, `hidden`, `application`) VALUES (100049, 100047, '租户套餐', 'TenantPackageIndex', 'sys:tenant:package', '0', 'system/tenant/TenantPackageIndex', '/tenant/package', NULL, 'shopping-cart', 1, 1, '2022-10-01 18:07:44', '2022-10-01 18:07:44', 'fxz', 'fxz', '0', 1);
INSERT INTO `sys_menu` (`id`, `parent_id`, `title`, `name`, `perms`, `type`, `component`, `path`, `redirect`, `icon`, `keep_alive`, `order_num`, `create_time`, `update_time`, `create_by`, `update_by`, `hidden`, `application`) VALUES (100050, 100012, '接口文档', 'doc', 'sys:doc', '0', 'RouteView', 'http://fxz-gateway:8301/doc.html', NULL, 'form', 1, 4, '2022-10-23 14:56:04', '2022-10-23 14:56:04', 'fxz', 'fxz', '0', 1);
INSERT INTO `sys_menu` (`id`, `parent_id`, `title`, `name`, `perms`, `type`, `component`, `path`, `redirect`, `icon`, `keep_alive`, `order_num`, `create_time`, `update_time`, `create_by`, `update_by`, `hidden`, `application`) VALUES (1646327120896270338, 100046, '终端管理', 'client', 'sys:client', '0', 'system/client/ClientList.vue', '/permissions/client', NULL, 'desktop', 1, 5, '2023-04-13 09:39:08', '2023-04-13 10:24:31', 'fxz', 'fxz', '0', 1);
INSERT INTO `sys_menu` (`id`, `parent_id`, `title`, `name`, `perms`, `type`, `component`, `path`, `redirect`, `icon`, `keep_alive`, `order_num`, `create_time`, `update_time`, `create_by`, `update_by`, `hidden`, `application`) VALUES (1646327719289233410, 1646327120896270338, '新增', 'add', 'system:ClientDetails:add', '1', NULL, NULL, NULL, 'border-top', 1, 0, '2023-04-13 09:41:30', '2023-04-13 10:24:31', 'fxz', 'fxz', '0', 1);
INSERT INTO `sys_menu` (`id`, `parent_id`, `title`, `name`, `perms`, `type`, `component`, `path`, `redirect`, `icon`, `keep_alive`, `order_num`, `create_time`, `update_time`, `create_by`, `update_by`, `hidden`, `application`) VALUES (1646327832816459777, 1646327120896270338, '修改', 'update', 'system:ClientDetails:update', '1', NULL, NULL, NULL, 'arrow-right', 1, 1, '2023-04-13 09:41:57', '2023-04-13 10:24:31', 'fxz', 'fxz', '0', 1);
INSERT INTO `sys_menu` (`id`, `parent_id`, `title`, `name`, `perms`, `type`, `component`, `path`, `redirect`, `icon`, `keep_alive`, `order_num`, `create_time`, `update_time`, `create_by`, `update_by`, `hidden`, `application`) VALUES (1646328545541955585, 100043, '删除', 'system:ClientDetails:delete', 'system:ClientDetails:delete', '1', NULL, NULL, NULL, 'border-top', 1, 3, '2023-04-13 09:44:47', '2023-04-13 09:44:47', 'fxz', 'fxz', '0', 1);
INSERT INTO `sys_menu` (`id`, `parent_id`, `title`, `name`, `perms`, `type`, `component`, `path`, `redirect`, `icon`, `keep_alive`, `order_num`, `create_time`, `update_time`, `create_by`, `update_by`, `hidden`, `application`) VALUES (1646328655927648257, 1646327120896270338, '查看', 'system:ClientDetails:view', 'system:ClientDetails:view', '1', NULL, NULL, NULL, 'border-top', 1, 4, '2023-04-13 09:45:14', '2023-04-13 10:24:31', 'fxz', 'fxz', '0', 1);
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
INSERT INTO `sys_oauth_client_details` (`client_id`, `resource_ids`, `client_secret`, `scope`, `authorized_grant_types`, `web_server_redirect_uri`, `authorities`, `access_token_validity`, `refresh_token_validity`, `additional_information`, `autoapprove`, `create_time`, `update_time`, `create_by`, `update_by`) VALUES ('wechat', NULL, '123456', 'server', 'refresh_token,authorization_code,wechat', 'https://fxz.life', NULL, NULL, NULL, '{\n	\"appName\":\"测试应用\"\n}', 'false', NULL, NULL, NULL, NULL);
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
  `oper_param` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '请求参数',
  `status` tinyint(1) DEFAULT NULL COMMENT '操作状态（0正常 1异常）',
  `error_msg` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '异常信息',
  `time` bigint DEFAULT NULL COMMENT '执行时间',
  `create_by` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '创建人',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '更新人',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=1646431825827741698 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

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
) ENGINE=InnoDB AUTO_INCREMENT=1511338847253417986 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='岗位信息表';

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
) ENGINE=InnoDB AUTO_INCREMENT=1630131045481324547 DEFAULT CHARSET=utf8mb3 COMMENT='角色表';

-- ----------------------------
-- Records of sys_role
-- ----------------------------
BEGIN;
INSERT INTO `sys_role` (`role_id`, `role_name`, `remark`, `create_time`, `update_time`, `create_by`, `update_by`, `data_scope`, `data_scope_dept_ids`, `code`, `tenant_id`) VALUES (1, 'admin', '超级管理员', '2022-02-28 16:23:11', '2023-04-13 09:48:59', NULL, 'fxz', 1, NULL, 'super_admin', 0);
INSERT INTO `sys_role` (`role_id`, `role_name`, `remark`, `create_time`, `update_time`, `create_by`, `update_by`, `data_scope`, `data_scope_dept_ids`, `code`, `tenant_id`) VALUES (13, '游客', '游客', '2022-02-28 16:23:11', '2023-04-12 20:25:09', 'fxz', 'fxz', 5, NULL, 'super_admin', 0);
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
INSERT INTO `sys_role_menu` (`ROLE_ID`, `MENU_ID`) VALUES (13, 4);
INSERT INTO `sys_role_menu` (`ROLE_ID`, `MENU_ID`) VALUES (13, 5);
INSERT INTO `sys_role_menu` (`ROLE_ID`, `MENU_ID`) VALUES (13, 6);
INSERT INTO `sys_role_menu` (`ROLE_ID`, `MENU_ID`) VALUES (13, 12);
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
INSERT INTO `sys_route_conf` (`id`, `name`, `route_id`, `predicates`, `filters`, `uri`, `sort_order`, `metadata`, `create_by`, `update_by`, `create_time`, `update_time`, `del_flag`) VALUES (1628315700145483778, 'system模块', 'art-server-system', '[{\"args\": {\"_genkey_0\": \"/system/**\"}, \"name\": \"Path\"}]', '[{\"args\": {\"key-resolver\": \"#{@remoteAddrKeyResolver}\", \"redis-rate-limiter.burstCapacity\": \"100\", \"redis-rate-limiter.replenishRate\": \"100\"}, \"name\": \"RequestRateLimiter\"}]', 'lb://art-server-system', 0, NULL, 'fxz', 'fxz', '2023-02-22 16:48:10', '2023-04-06 20:01:16', '0');
INSERT INTO `sys_route_conf` (`id`, `name`, `route_id`, `predicates`, `filters`, `uri`, `sort_order`, `metadata`, `create_by`, `update_by`, `create_time`, `update_time`, `del_flag`) VALUES (1628315700384559106, 'auth模块', 'art-auth', '[{\"args\": {\"_genkey_0\": \"/auth/**\"}, \"name\": \"Path\"}]', NULL, 'lb://art-auth', 1, NULL, 'fxz', 'fxz', '2023-02-22 16:48:10', '2023-04-06 20:01:20', '0');
INSERT INTO `sys_route_conf` (`id`, `name`, `route_id`, `predicates`, `filters`, `uri`, `sort_order`, `metadata`, `create_by`, `update_by`, `create_time`, `update_time`, `del_flag`) VALUES (1628315701189865474, '代码生成器模块', 'art-generate', '[{\"args\": {\"_genkey_0\": \"/generate/**\"}, \"name\": \"Path\"}]', NULL, 'lb://art-generate', 2, NULL, 'fxz', 'fxz', '2023-02-22 16:48:10', '2023-04-06 20:01:25', '0');
INSERT INTO `sys_route_conf` (`id`, `name`, `route_id`, `predicates`, `filters`, `uri`, `sort_order`, `metadata`, `create_by`, `update_by`, `create_time`, `update_time`, `del_flag`) VALUES (1628315701487661058, '实验模块', 'art-demo', '[{\"args\": {\"_genkey_0\": \"/demos/**\"}, \"name\": \"Path\"}]', NULL, 'lb://art-demo', 3, NULL, 'fxz', 'fxz', '2023-02-22 16:48:11', '2023-04-06 20:01:30', '0');
INSERT INTO `sys_route_conf` (`id`, `name`, `route_id`, `predicates`, `filters`, `uri`, `sort_order`, `metadata`, `create_by`, `update_by`, `create_time`, `update_time`, `del_flag`) VALUES (1628315701823205378, '定时任务模块', 'art-job', '[{\"args\": {\"_genkey_0\": \"/schedule/**\"}, \"name\": \"Path\"}]', NULL, 'lb://art-job', 3, NULL, 'fxz', 'fxz', '2023-02-22 16:48:11', '2023-04-06 20:01:33', '0');
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
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='租户表';

-- ----------------------------
-- Records of sys_tenant
-- ----------------------------
BEGIN;
INSERT INTO `sys_tenant` (`id`, `name`, `tenant_admin_id`, `tenant_admin_name`, `tenant_admin_mobile`, `status`, `package_id`, `expire_time`, `account_count`, `create_by`, `create_time`, `update_by`, `update_time`, `del_flag`) VALUES (0, 'fxzcloud', 14, 'fxz', '19812341234', 0, 0, '3000-03-30 00:00:00', 100, '1', '2022-02-22 00:56:14', 'fxz', '2023-04-12 13:33:56', b'0');
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
) ENGINE=InnoDB AUTO_INCREMENT=1620378325738639362 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='租户套餐表';

-- ----------------------------
-- Records of sys_tenant_package
-- ----------------------------
BEGIN;
INSERT INTO `sys_tenant_package` (`id`, `name`, `status`, `remark`, `menu_ids`, `create_by`, `create_time`, `update_by`, `update_time`, `del_flag`) VALUES (1576530575020077058, '豪华套餐', 0, '豪华租户套餐', '12,4,5,6,7,21,29,100006,100009,100010,100011,100007,100008,100022,100019,100024,100025,100026,100034,100040,100043,100021,20', 'fxz', '2022-10-02 19:12:34', 'fxz', '2022-10-03 12:25:10', b'0');
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
) ENGINE=InnoDB AUTO_INCREMENT=16 DEFAULT CHARSET=utf8mb3 COMMENT='用户表';

-- ----------------------------
-- Records of sys_user
-- ----------------------------
BEGIN;
INSERT INTO `sys_user` (`user_id`, `username`, `password`, `dept_id`, `email`, `mobile`, `status`, `create_time`, `update_time`, `last_login_time`, `ssex`, `avatar`, `description`, `create_by`, `update_by`, `tenant_id`) VALUES (1, 'fxz', '{bcrypt}$2a$10$yMqLTL9t9TeRGp5fT7vENuxZaruaN.a/VlOpRGF7jBnKPAim6Xey.', 10, '2****@qq.com', '198****2431', '1', '2022-04-05 20:39:22', '2023-02-22 19:31:16', '2022-04-05 15:57:00', '0', '', 'Art的作者,FxzClud的核心成员。', NULL, 'fxz', 0);
INSERT INTO `sys_user` (`user_id`, `username`, `password`, `dept_id`, `email`, `mobile`, `status`, `create_time`, `update_time`, `last_login_time`, `ssex`, `avatar`, `description`, `create_by`, `update_by`, `tenant_id`) VALUES (15, 'aaa', '{bcrypt}$2a$10$tAFLsgNt0Ew4O7mpuAUjeeqiG3j.6IwHkzDlzpWNY7TUbRqNe1AJy', NULL, '15912345432@qq.com', '15912345432', '1', '2023-04-12 20:24:10', '2023-04-12 20:24:10', NULL, '0', '', '', 'fxz', 'fxz', 0);
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
INSERT INTO `sys_user_post` (`user_id`, `post_id`) VALUES (15, 1511326356242804737);
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
INSERT INTO `sys_user_role` (`USER_ID`, `ROLE_ID`) VALUES (15, 13);
COMMIT;

SET FOREIGN_KEY_CHECKS = 1;
