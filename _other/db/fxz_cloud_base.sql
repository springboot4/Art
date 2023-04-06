
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
INSERT INTO `sys_app` (`id`, `name`, `code`, `sort`, `icon`, `create_time`, `create_by`, `update_time`, `update_by`) VALUES (2, '商城应用', 'mall', 999, 'money-collect', '2022-09-12 00:17:28', 'fxz', '2023-02-22 14:29:06', 'fxz');
COMMIT;

-- ----------------------------
-- Table structure for sys_dept
-- ----------------------------
DROP TABLE IF EXISTS `sys_dept`;
CREATE TABLE `sys_dept` (
  `dept_id` bigint NOT NULL AUTO_INCREMENT COMMENT '部门ID',
  `parent_id` bigint NOT NULL COMMENT '上级部门ID',
  `dept_name` varchar(100) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NOT NULL COMMENT '部门名称',
  `order_num` double(20,0) DEFAULT NULL COMMENT '排序',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime DEFAULT NULL COMMENT '修改时间',
  `create_by` varchar(100) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci DEFAULT NULL COMMENT '创建人',
  `update_by` varchar(100) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci DEFAULT NULL COMMENT '更新人',
  PRIMARY KEY (`dept_id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=12 DEFAULT CHARSET=utf8mb3 COMMENT='部门表';

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
INSERT INTO `sys_dept` (`dept_id`, `parent_id`, `dept_name`, `order_num`, `create_time`, `update_time`, `create_by`, `update_by`) VALUES (10, 2, '软件工程', 3, '2023-02-22 14:27:48', '2023-02-22 14:27:48', 'fxz', 'fxz');
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
INSERT INTO `sys_dict` (`id`, `type`, `description`, `remark`, `system_flag`, `del_flag`, `create_time`, `create_by`, `update_by`, `update_time`) VALUES (1510864072018067457, 'ddd', 'dd', 'dd', '1', '1', '2022-04-04 14:17:20', 'fxz', 'fxz', '2022-04-04 14:17:20');
INSERT INTO `sys_dict` (`id`, `type`, `description`, `remark`, `system_flag`, `del_flag`, `create_time`, `create_by`, `update_by`, `update_time`) VALUES (1510865432566308866, 'dict_type', '字典类型', '字典类型', '1', '0', '2022-04-04 14:22:44', 'fxz', 'fxz', '2022-04-04 14:22:44');
INSERT INTO `sys_dict` (`id`, `type`, `description`, `remark`, `system_flag`, `del_flag`, `create_time`, `create_by`, `update_by`, `update_time`) VALUES (1510869546876649474, '1', '1', '1', '0', '1', '2022-04-04 14:39:05', 'fxz', 'fxz', '2022-04-04 14:39:05');
INSERT INTO `sys_dict` (`id`, `type`, `description`, `remark`, `system_flag`, `del_flag`, `create_time`, `create_by`, `update_by`, `update_time`) VALUES (1510870304607027202, '1231', '13213', '313', '0', '1', '2022-04-04 14:42:06', 'fxz', 'fxz', '2022-04-04 14:42:16');
INSERT INTO `sys_dict` (`id`, `type`, `description`, `remark`, `system_flag`, `del_flag`, `create_time`, `create_by`, `update_by`, `update_time`) VALUES (1510873495704817666, 'dasda', 'dsada', 'dasda', '0', '1', '2022-04-04 14:54:47', 'fxz', 'fxz', '2022-04-04 15:05:11');
INSERT INTO `sys_dict` (`id`, `type`, `description`, `remark`, `system_flag`, `del_flag`, `create_time`, `create_by`, `update_by`, `update_time`) VALUES (1510894313813856257, '1', '1', '1', '0', '1', '2022-04-04 16:17:30', 'fxz', 'fxz', '2022-04-04 16:17:30');
INSERT INTO `sys_dict` (`id`, `type`, `description`, `remark`, `system_flag`, `del_flag`, `create_time`, `create_by`, `update_by`, `update_time`) VALUES (1510894343949930498, '2', '2', '2', '0', '1', '2022-04-04 16:17:37', 'fxz', 'fxz', '2022-04-04 16:17:37');
INSERT INTO `sys_dict` (`id`, `type`, `description`, `remark`, `system_flag`, `del_flag`, `create_time`, `create_by`, `update_by`, `update_time`) VALUES (1510894360676814850, '3', '3', '3', '0', '1', '2022-04-04 16:17:41', 'fxz', 'fxz', '2022-04-04 16:17:41');
INSERT INTO `sys_dict` (`id`, `type`, `description`, `remark`, `system_flag`, `del_flag`, `create_time`, `create_by`, `update_by`, `update_time`) VALUES (1510894381933547521, '4', '4', '4', '0', '1', '2022-04-04 16:17:46', 'fxz', 'fxz', '2022-04-04 16:17:46');
INSERT INTO `sys_dict` (`id`, `type`, `description`, `remark`, `system_flag`, `del_flag`, `create_time`, `create_by`, `update_by`, `update_time`) VALUES (1510894403840397314, '5', '5', '5', '0', '1', '2022-04-04 16:17:52', 'fxz', 'fxz', '2022-04-04 16:17:52');
INSERT INTO `sys_dict` (`id`, `type`, `description`, `remark`, `system_flag`, `del_flag`, `create_time`, `create_by`, `update_by`, `update_time`) VALUES (1510894422765096962, '6', '6', '6', '0', '1', '2022-04-04 16:17:56', 'fxz', 'fxz', '2022-04-04 16:17:56');
INSERT INTO `sys_dict` (`id`, `type`, `description`, `remark`, `system_flag`, `del_flag`, `create_time`, `create_by`, `update_by`, `update_time`) VALUES (1510894443795337218, '7', '7', '7', '0', '1', '2022-04-04 16:18:01', 'fxz', 'fxz', '2022-04-04 16:18:01');
INSERT INTO `sys_dict` (`id`, `type`, `description`, `remark`, `system_flag`, `del_flag`, `create_time`, `create_by`, `update_by`, `update_time`) VALUES (1510894461734375425, '8', '8', '8', '0', '1', '2022-04-04 16:18:05', 'fxz', 'fxz', '2022-04-04 16:18:05');
INSERT INTO `sys_dict` (`id`, `type`, `description`, `remark`, `system_flag`, `del_flag`, `create_time`, `create_by`, `update_by`, `update_time`) VALUES (1510894481871228929, '9', '9', '9', '0', '1', '2022-04-04 16:18:10', 'fxz', 'fxz', '2022-04-04 16:18:10');
INSERT INTO `sys_dict` (`id`, `type`, `description`, `remark`, `system_flag`, `del_flag`, `create_time`, `create_by`, `update_by`, `update_time`) VALUES (1517894240063803393, 'data_permission_type', '数据权限', '数据权限', '1', '0', '2022-04-23 23:52:43', 'fxz', 'fxz', '2022-04-23 23:52:43');
INSERT INTO `sys_dict` (`id`, `type`, `description`, `remark`, `system_flag`, `del_flag`, `create_time`, `create_by`, `update_by`, `update_time`) VALUES (1568577687467589633, 'sex_type', '性别', NULL, '1', '0', '2022-09-10 20:30:38', 'fxz', 'fxz', '2022-09-10 20:30:38');
INSERT INTO `sys_dict` (`id`, `type`, `description`, `remark`, `system_flag`, `del_flag`, `create_time`, `create_by`, `update_by`, `update_time`) VALUES (1619978601734406146, '1', '1', '12', '0', '1', '2023-01-30 16:39:31', 'fxz', 'fxz', '2023-01-30 16:39:37');
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
INSERT INTO `sys_menu` (`id`, `parent_id`, `title`, `name`, `perms`, `type`, `component`, `path`, `redirect`, `icon`, `keep_alive`, `order_num`, `create_time`, `update_time`, `create_by`, `update_by`, `hidden`, `application`) VALUES (100027, 0, '商品管理', 'product', 'product', '0', 'RouteView', '/product', NULL, 'gift', 1, 3, '2022-05-05 20:14:55', '2022-09-12 16:12:22', 'fxz', 'fxz', '0', 2);
INSERT INTO `sys_menu` (`id`, `parent_id`, `title`, `name`, `perms`, `type`, `component`, `path`, `redirect`, `icon`, `keep_alive`, `order_num`, `create_time`, `update_time`, `create_by`, `update_by`, `hidden`, `application`) VALUES (100028, 100027, '品牌管理', 'brandList', 'product:brand', '0', 'mall/product/brand/BrandList', '/product/brand', NULL, 'taobao', 1, 0, '2022-05-05 20:15:54', '2022-05-05 20:15:54', 'fxz', 'fxz', '0', 2);
INSERT INTO `sys_menu` (`id`, `parent_id`, `title`, `name`, `perms`, `type`, `component`, `path`, `redirect`, `icon`, `keep_alive`, `order_num`, `create_time`, `update_time`, `create_by`, `update_by`, `hidden`, `application`) VALUES (100029, 100027, '分类管理', 'categoryList', 'product:category', '0', 'mall/product/category/CategoryList', '/product/category', NULL, 'layout', 1, 1, '2022-05-05 20:16:57', '2022-05-05 20:16:57', 'fxz', 'fxz', '0', 2);
INSERT INTO `sys_menu` (`id`, `parent_id`, `title`, `name`, `perms`, `type`, `component`, `path`, `redirect`, `icon`, `keep_alive`, `order_num`, `create_time`, `update_time`, `create_by`, `update_by`, `hidden`, `application`) VALUES (100030, 100027, '商品上架', 'goodInfo', 'product:good', '0', 'mall/product/goods/Info', '/goods/info', NULL, 'shop', 0, -1, '2022-05-06 16:49:42', '2022-08-11 13:51:33', 'fxz', 'fxz', '0', 2);
INSERT INTO `sys_menu` (`id`, `parent_id`, `title`, `name`, `perms`, `type`, `component`, `path`, `redirect`, `icon`, `keep_alive`, `order_num`, `create_time`, `update_time`, `create_by`, `update_by`, `hidden`, `application`) VALUES (100031, 100027, '商品列表', 'goodsList', 'product:goods', '0', 'mall/product/goods/index.vue', '/product/goods', NULL, 'database', 1, -2, '2022-05-09 09:19:51', '2022-05-09 09:19:51', 'fxz', 'fxz', '0', 2);
INSERT INTO `sys_menu` (`id`, `parent_id`, `title`, `name`, `perms`, `type`, `component`, `path`, `redirect`, `icon`, `keep_alive`, `order_num`, `create_time`, `update_time`, `create_by`, `update_by`, `hidden`, `application`) VALUES (100032, 100036, '订单列表', 'OrderList', 'order:list', '0', 'mall/orders/order/OrderList', '/order/list', NULL, 'shopping-cart', 1, 4, '2022-05-18 17:54:51', '2022-08-11 21:08:53', 'fxz', 'fxz', '0', 2);
INSERT INTO `sys_menu` (`id`, `parent_id`, `title`, `name`, `perms`, `type`, `component`, `path`, `redirect`, `icon`, `keep_alive`, `order_num`, `create_time`, `update_time`, `create_by`, `update_by`, `hidden`, `application`) VALUES (100033, 100035, '会员列表', 'MemberList', 'user:member', '0', 'mall/user/member/MemberList', '/user/member', NULL, 'smile', 1, 5, '2022-05-18 20:25:28', '2022-08-11 21:02:26', 'fxz', 'fxz', '0', 2);
INSERT INTO `sys_menu` (`id`, `parent_id`, `title`, `name`, `perms`, `type`, `component`, `path`, `redirect`, `icon`, `keep_alive`, `order_num`, `create_time`, `update_time`, `create_by`, `update_by`, `hidden`, `application`) VALUES (100034, 100046, '令牌管理', 'tokenList', 'sys:token', '0', 'system/token/index.vue', '/permissions/token', NULL, 'safety-certificate', 1, 5, '2022-06-26 21:18:21', '2022-09-12 16:21:54', 'fxz', 'fxz', '0', 1);
INSERT INTO `sys_menu` (`id`, `parent_id`, `title`, `name`, `perms`, `type`, `component`, `path`, `redirect`, `icon`, `keep_alive`, `order_num`, `create_time`, `update_time`, `create_by`, `update_by`, `hidden`, `application`) VALUES (100035, 0, '会员管理', 'member', 'user', '0', 'RouteView', '/user', NULL, 'user', 1, 4, '2022-08-11 21:01:52', '2022-09-12 16:12:43', 'fxz', 'fxz', '0', 2);
INSERT INTO `sys_menu` (`id`, `parent_id`, `title`, `name`, `perms`, `type`, `component`, `path`, `redirect`, `icon`, `keep_alive`, `order_num`, `create_time`, `update_time`, `create_by`, `update_by`, `hidden`, `application`) VALUES (100036, 0, '订单管理', 'order', 'order', '0', 'RouteView', '/order', NULL, 'shop', 1, 4, '2022-08-11 21:07:41', '2022-09-12 16:21:15', 'fxz', 'fxz', '0', 2);
INSERT INTO `sys_menu` (`id`, `parent_id`, `title`, `name`, `perms`, `type`, `component`, `path`, `redirect`, `icon`, `keep_alive`, `order_num`, `create_time`, `update_time`, `create_by`, `update_by`, `hidden`, `application`) VALUES (100037, 0, '营销管理', 'promotion', 'promotion', '0', 'RouteView', '/promotion', NULL, 'shopping-cart', 1, 3, '2022-08-12 10:33:22', '2022-09-12 16:12:33', 'fxz', 'fxz', '0', 2);
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
INSERT INTO `sys_oauth_client_details` (`client_id`, `resource_ids`, `client_secret`, `scope`, `authorized_grant_types`, `web_server_redirect_uri`, `authorities`, `access_token_validity`, `refresh_token_validity`, `additional_information`, `autoapprove`, `create_time`, `update_time`, `create_by`, `update_by`) VALUES ('daemon', NULL, 'daemon', 'server', 'password,refresh_token', NULL, NULL, NULL, NULL, NULL, 'true', NULL, NULL, NULL, NULL);
INSERT INTO `sys_oauth_client_details` (`client_id`, `resource_ids`, `client_secret`, `scope`, `authorized_grant_types`, `web_server_redirect_uri`, `authorities`, `access_token_validity`, `refresh_token_validity`, `additional_information`, `autoapprove`, `create_time`, `update_time`, `create_by`, `update_by`) VALUES ('fxz', NULL, '123456', 'server', 'refresh_token,authorization_code,captcha,password,sms_code', 'https://fxz.life', NULL, NULL, NULL, NULL, 'true', NULL, NULL, NULL, NULL);
INSERT INTO `sys_oauth_client_details` (`client_id`, `resource_ids`, `client_secret`, `scope`, `authorized_grant_types`, `web_server_redirect_uri`, `authorities`, `access_token_validity`, `refresh_token_validity`, `additional_information`, `autoapprove`, `create_time`, `update_time`, `create_by`, `update_by`) VALUES ('gen', NULL, 'gen', 'server', 'password,refresh_token', NULL, NULL, NULL, NULL, NULL, 'true', NULL, NULL, NULL, NULL);
INSERT INTO `sys_oauth_client_details` (`client_id`, `resource_ids`, `client_secret`, `scope`, `authorized_grant_types`, `web_server_redirect_uri`, `authorities`, `access_token_validity`, `refresh_token_validity`, `additional_information`, `autoapprove`, `create_time`, `update_time`, `create_by`, `update_by`) VALUES ('mall-app', NULL, '123456', 'server', 'refresh_token,authorization_code,password,sms_code', 'http://localhost:80/sso/login', NULL, 43200, 2592001, NULL, 'true', NULL, NULL, NULL, NULL);
INSERT INTO `sys_oauth_client_details` (`client_id`, `resource_ids`, `client_secret`, `scope`, `authorized_grant_types`, `web_server_redirect_uri`, `authorities`, `access_token_validity`, `refresh_token_validity`, `additional_information`, `autoapprove`, `create_time`, `update_time`, `create_by`, `update_by`) VALUES ('pig', NULL, 'pig', 'server', 'password,app,refresh_token,authorization_code,client_credentials', 'http://localhost:4040/sso1/login,http://localhost:4041/sso1/login', NULL, NULL, NULL, NULL, 'true', NULL, NULL, NULL, NULL);
INSERT INTO `sys_oauth_client_details` (`client_id`, `resource_ids`, `client_secret`, `scope`, `authorized_grant_types`, `web_server_redirect_uri`, `authorities`, `access_token_validity`, `refresh_token_validity`, `additional_information`, `autoapprove`, `create_time`, `update_time`, `create_by`, `update_by`) VALUES ('swagger', NULL, '123456', 'test', 'password,captcha', NULL, NULL, NULL, NULL, NULL, 'true', NULL, NULL, NULL, NULL);
INSERT INTO `sys_oauth_client_details` (`client_id`, `resource_ids`, `client_secret`, `scope`, `authorized_grant_types`, `web_server_redirect_uri`, `authorities`, `access_token_validity`, `refresh_token_validity`, `additional_information`, `autoapprove`, `create_time`, `update_time`, `create_by`, `update_by`) VALUES ('test', NULL, 'test', 'server', 'password,app,refresh_token,captcha', NULL, NULL, NULL, NULL, NULL, 'true', NULL, NULL, NULL, NULL);
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
) ENGINE=InnoDB AUTO_INCREMENT=1643892795338948611 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;


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
INSERT INTO `sys_post` (`post_id`, `post_code`, `post_name`, `post_sort`, `del_flag`, `description`, `create_time`, `create_by`, `update_time`, `update_by`) VALUES (1, 'user', '员工', 2, '1', '打工人', '2022-03-19 10:05:15', 'admin', '2022-03-19 10:42:28', 'admin');
INSERT INTO `sys_post` (`post_id`, `post_code`, `post_name`, `post_sort`, `del_flag`, `description`, `create_time`, `create_by`, `update_time`, `update_by`) VALUES (2, 'cto', 'cto', 0, '1', 'cto666', '2022-03-19 10:06:20', 'admin', '2022-03-19 10:06:20', 'admin');
INSERT INTO `sys_post` (`post_id`, `post_code`, `post_name`, `post_sort`, `del_flag`, `description`, `create_time`, `create_by`, `update_time`, `update_by`) VALUES (3, 'boss', '董事长', -1, '1', '大boss', '2022-03-19 10:06:35', 'admin', '2022-03-19 10:42:44', 'admin');
INSERT INTO `sys_post` (`post_id`, `post_code`, `post_name`, `post_sort`, `del_flag`, `description`, `create_time`, `create_by`, `update_time`, `update_by`) VALUES (1509530573596016641, '22s', '222s', 11, '1', '22s', '2022-03-31 21:58:29', 'admin', '2022-03-31 21:58:29', 'admin');
INSERT INTO `sys_post` (`post_id`, `post_code`, `post_name`, `post_sort`, `del_flag`, `description`, `create_time`, `create_by`, `update_time`, `update_by`) VALUES (1509534285496426497, '22sas', '222ssss', 1122, '1', '22s', '2022-03-31 22:13:14', 'admin', '2022-03-31 22:13:14', 'admin');
INSERT INTO `sys_post` (`post_id`, `post_code`, `post_name`, `post_sort`, `del_flag`, `description`, `create_time`, `create_by`, `update_time`, `update_by`) VALUES (1509534945046536193, '22sass', '222sssp', 1122, '1', '22ss', '2022-03-31 22:15:51', 'admin', '2022-03-31 22:15:51', 'admin');
INSERT INTO `sys_post` (`post_id`, `post_code`, `post_name`, `post_sort`, `del_flag`, `description`, `create_time`, `create_by`, `update_time`, `update_by`) VALUES (1509535899309416450, 'aasw', 'ds', 11, '1', 'dd', '2022-03-31 22:19:39', 'admin', '2022-03-31 22:19:39', 'admin');
INSERT INTO `sys_post` (`post_id`, `post_code`, `post_name`, `post_sort`, `del_flag`, `description`, `create_time`, `create_by`, `update_time`, `update_by`) VALUES (1509536193485316097, 'aasws', 'dss', 11, '1', 'dds', '2022-03-31 22:20:49', 'admin', '2022-03-31 22:20:49', 'admin');
INSERT INTO `sys_post` (`post_id`, `post_code`, `post_name`, `post_sort`, `del_flag`, `description`, `create_time`, `create_by`, `update_time`, `update_by`) VALUES (1509536337576435713, 'aasws2', 'dss1', 11, '1', 'dds', '2022-03-31 22:21:23', 'admin', '2022-03-31 22:21:23', 'admin');
INSERT INTO `sys_post` (`post_id`, `post_code`, `post_name`, `post_sort`, `del_flag`, `description`, `create_time`, `create_by`, `update_time`, `update_by`) VALUES (1509536560256229377, 'aasws2s', 'dss1s', 11, '1', 'dds', '2022-03-31 22:22:16', 'admin', '2022-03-31 22:22:16', 'admin');
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
INSERT INTO `sys_role` (`role_id`, `role_name`, `remark`, `create_time`, `update_time`, `create_by`, `update_by`, `data_scope`, `data_scope_dept_ids`, `code`, `tenant_id`) VALUES (1, 'admin', '超级管理员', '2022-02-28 16:23:11', '2023-02-24 11:55:16', NULL, 'fxz', 1, NULL, 'super_admin', 0);
INSERT INTO `sys_role` (`role_id`, `role_name`, `remark`, `create_time`, `update_time`, `create_by`, `update_by`, `data_scope`, `data_scope_dept_ids`, `code`, `tenant_id`) VALUES (12, '租户管理员', '系统生成租户管理员角色', '2022-10-02 19:13:55', '2022-10-03 12:25:10', 'fxz', 'fxz', 1, NULL, 'tenant_admin', 1576530912007237634);
INSERT INTO `sys_role` (`role_id`, `role_name`, `remark`, `create_time`, `update_time`, `create_by`, `update_by`, `data_scope`, `data_scope_dept_ids`, `code`, `tenant_id`) VALUES (13, '游客', '游客', '2022-02-28 16:23:11', '2023-01-31 14:16:58', 'fxz', 'fxz', 5, NULL, 'super_admin', 0);
INSERT INTO `sys_role` (`role_id`, `role_name`, `remark`, `create_time`, `update_time`, `create_by`, `update_by`, `data_scope`, `data_scope_dept_ids`, `code`, `tenant_id`) VALUES (1628283829821120513, '教师', '教师', '2022-02-28 16:23:11', '2023-02-24 12:09:35', 'fxz', 'fxz', 1, NULL, NULL, 0);
INSERT INTO `sys_role` (`role_id`, `role_name`, `remark`, `create_time`, `update_time`, `create_by`, `update_by`, `data_scope`, `data_scope_dept_ids`, `code`, `tenant_id`) VALUES (1628283956224860162, '学生', '学生', '2022-02-28 16:23:11', '2023-02-26 13:27:42', 'fxz', 'fxz', 1, NULL, NULL, 0);
INSERT INTO `sys_role` (`role_id`, `role_name`, `remark`, `create_time`, `update_time`, `create_by`, `update_by`, `data_scope`, `data_scope_dept_ids`, `code`, `tenant_id`) VALUES (1630127122991091713, '租户管理员', '系统生成租户管理员角色', '2023-02-27 16:46:07', '2023-02-27 16:46:07', 'fxz', 'fxz', 1, NULL, 'tenant_admin', 1630127122500358146);
INSERT INTO `sys_role` (`role_id`, `role_name`, `remark`, `create_time`, `update_time`, `create_by`, `update_by`, `data_scope`, `data_scope_dept_ids`, `code`, `tenant_id`) VALUES (1630131045481324546, '租户管理员', '系统生成租户管理员角色', '2023-02-27 17:01:42', '2023-02-27 17:01:42', 'fxz', 'fxz', 1, NULL, 'tenant_admin', 1630131045384855554);
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
INSERT INTO `sys_role_menu` (`ROLE_ID`, `MENU_ID`) VALUES (12, 12);
INSERT INTO `sys_role_menu` (`ROLE_ID`, `MENU_ID`) VALUES (12, 4);
INSERT INTO `sys_role_menu` (`ROLE_ID`, `MENU_ID`) VALUES (12, 5);
INSERT INTO `sys_role_menu` (`ROLE_ID`, `MENU_ID`) VALUES (12, 6);
INSERT INTO `sys_role_menu` (`ROLE_ID`, `MENU_ID`) VALUES (12, 7);
INSERT INTO `sys_role_menu` (`ROLE_ID`, `MENU_ID`) VALUES (12, 21);
INSERT INTO `sys_role_menu` (`ROLE_ID`, `MENU_ID`) VALUES (12, 29);
INSERT INTO `sys_role_menu` (`ROLE_ID`, `MENU_ID`) VALUES (12, 100006);
INSERT INTO `sys_role_menu` (`ROLE_ID`, `MENU_ID`) VALUES (12, 100009);
INSERT INTO `sys_role_menu` (`ROLE_ID`, `MENU_ID`) VALUES (12, 100010);
INSERT INTO `sys_role_menu` (`ROLE_ID`, `MENU_ID`) VALUES (12, 100011);
INSERT INTO `sys_role_menu` (`ROLE_ID`, `MENU_ID`) VALUES (12, 100007);
INSERT INTO `sys_role_menu` (`ROLE_ID`, `MENU_ID`) VALUES (12, 100008);
INSERT INTO `sys_role_menu` (`ROLE_ID`, `MENU_ID`) VALUES (12, 100022);
INSERT INTO `sys_role_menu` (`ROLE_ID`, `MENU_ID`) VALUES (12, 100019);
INSERT INTO `sys_role_menu` (`ROLE_ID`, `MENU_ID`) VALUES (12, 100024);
INSERT INTO `sys_role_menu` (`ROLE_ID`, `MENU_ID`) VALUES (12, 100025);
INSERT INTO `sys_role_menu` (`ROLE_ID`, `MENU_ID`) VALUES (12, 100026);
INSERT INTO `sys_role_menu` (`ROLE_ID`, `MENU_ID`) VALUES (12, 100034);
INSERT INTO `sys_role_menu` (`ROLE_ID`, `MENU_ID`) VALUES (12, 100040);
INSERT INTO `sys_role_menu` (`ROLE_ID`, `MENU_ID`) VALUES (12, 100043);
INSERT INTO `sys_role_menu` (`ROLE_ID`, `MENU_ID`) VALUES (12, 100021);
INSERT INTO `sys_role_menu` (`ROLE_ID`, `MENU_ID`) VALUES (12, 20);
INSERT INTO `sys_role_menu` (`ROLE_ID`, `MENU_ID`) VALUES (13, 12);
INSERT INTO `sys_role_menu` (`ROLE_ID`, `MENU_ID`) VALUES (13, 4);
INSERT INTO `sys_role_menu` (`ROLE_ID`, `MENU_ID`) VALUES (13, 5);
INSERT INTO `sys_role_menu` (`ROLE_ID`, `MENU_ID`) VALUES (13, 6);
INSERT INTO `sys_role_menu` (`ROLE_ID`, `MENU_ID`) VALUES (13, 7);
INSERT INTO `sys_role_menu` (`ROLE_ID`, `MENU_ID`) VALUES (13, 8);
INSERT INTO `sys_role_menu` (`ROLE_ID`, `MENU_ID`) VALUES (13, 11);
INSERT INTO `sys_role_menu` (`ROLE_ID`, `MENU_ID`) VALUES (13, 10);
INSERT INTO `sys_role_menu` (`ROLE_ID`, `MENU_ID`) VALUES (13, 9);
INSERT INTO `sys_role_menu` (`ROLE_ID`, `MENU_ID`) VALUES (13, 100047);
INSERT INTO `sys_role_menu` (`ROLE_ID`, `MENU_ID`) VALUES (13, 100048);
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
INSERT INTO `sys_role_menu` (`ROLE_ID`, `MENU_ID`) VALUES (1628283829821120513, 12);
INSERT INTO `sys_role_menu` (`ROLE_ID`, `MENU_ID`) VALUES (1628283829821120513, 1628281206988611586);
INSERT INTO `sys_role_menu` (`ROLE_ID`, `MENU_ID`) VALUES (1628283829821120513, 1628282988909305858);
INSERT INTO `sys_role_menu` (`ROLE_ID`, `MENU_ID`) VALUES (1628283829821120513, 1628283417441345538);
INSERT INTO `sys_role_menu` (`ROLE_ID`, `MENU_ID`) VALUES (1628283829821120513, 4);
INSERT INTO `sys_role_menu` (`ROLE_ID`, `MENU_ID`) VALUES (1628283829821120513, 5);
INSERT INTO `sys_role_menu` (`ROLE_ID`, `MENU_ID`) VALUES (1628283829821120513, 6);
INSERT INTO `sys_role_menu` (`ROLE_ID`, `MENU_ID`) VALUES (1628283829821120513, 7);
INSERT INTO `sys_role_menu` (`ROLE_ID`, `MENU_ID`) VALUES (1628283829821120513, 8);
INSERT INTO `sys_role_menu` (`ROLE_ID`, `MENU_ID`) VALUES (1628283829821120513, 9);
INSERT INTO `sys_role_menu` (`ROLE_ID`, `MENU_ID`) VALUES (1628283829821120513, 10);
INSERT INTO `sys_role_menu` (`ROLE_ID`, `MENU_ID`) VALUES (1628283829821120513, 11);
INSERT INTO `sys_role_menu` (`ROLE_ID`, `MENU_ID`) VALUES (1628283829821120513, 100006);
INSERT INTO `sys_role_menu` (`ROLE_ID`, `MENU_ID`) VALUES (1628283829821120513, 100009);
INSERT INTO `sys_role_menu` (`ROLE_ID`, `MENU_ID`) VALUES (1628283829821120513, 1628656999216459777);
INSERT INTO `sys_role_menu` (`ROLE_ID`, `MENU_ID`) VALUES (1628283829821120513, 1628317343117275137);
INSERT INTO `sys_role_menu` (`ROLE_ID`, `MENU_ID`) VALUES (1628283829821120513, 1628317471911768065);
INSERT INTO `sys_role_menu` (`ROLE_ID`, `MENU_ID`) VALUES (1628283829821120513, 1628317584814043138);
INSERT INTO `sys_role_menu` (`ROLE_ID`, `MENU_ID`) VALUES (1628283829821120513, 1628317214134038530);
INSERT INTO `sys_role_menu` (`ROLE_ID`, `MENU_ID`) VALUES (1628283829821120513, 100026);
INSERT INTO `sys_role_menu` (`ROLE_ID`, `MENU_ID`) VALUES (1628283829821120513, 100024);
INSERT INTO `sys_role_menu` (`ROLE_ID`, `MENU_ID`) VALUES (1628283829821120513, 100046);
INSERT INTO `sys_role_menu` (`ROLE_ID`, `MENU_ID`) VALUES (1628283829821120513, 1628966690909663233);
INSERT INTO `sys_role_menu` (`ROLE_ID`, `MENU_ID`) VALUES (1628283829821120513, 100010);
INSERT INTO `sys_role_menu` (`ROLE_ID`, `MENU_ID`) VALUES (1628283956224860162, 12);
INSERT INTO `sys_role_menu` (`ROLE_ID`, `MENU_ID`) VALUES (1628283956224860162, 1628281206988611586);
INSERT INTO `sys_role_menu` (`ROLE_ID`, `MENU_ID`) VALUES (1628283956224860162, 1628282988909305858);
INSERT INTO `sys_role_menu` (`ROLE_ID`, `MENU_ID`) VALUES (1628283956224860162, 1628283417441345538);
INSERT INTO `sys_role_menu` (`ROLE_ID`, `MENU_ID`) VALUES (1628283956224860162, 4);
INSERT INTO `sys_role_menu` (`ROLE_ID`, `MENU_ID`) VALUES (1628283956224860162, 6);
INSERT INTO `sys_role_menu` (`ROLE_ID`, `MENU_ID`) VALUES (1628283956224860162, 5);
INSERT INTO `sys_role_menu` (`ROLE_ID`, `MENU_ID`) VALUES (1628283956224860162, 7);
INSERT INTO `sys_role_menu` (`ROLE_ID`, `MENU_ID`) VALUES (1628283956224860162, 8);
INSERT INTO `sys_role_menu` (`ROLE_ID`, `MENU_ID`) VALUES (1628283956224860162, 9);
INSERT INTO `sys_role_menu` (`ROLE_ID`, `MENU_ID`) VALUES (1628283956224860162, 10);
INSERT INTO `sys_role_menu` (`ROLE_ID`, `MENU_ID`) VALUES (1628283956224860162, 11);
INSERT INTO `sys_role_menu` (`ROLE_ID`, `MENU_ID`) VALUES (1628283956224860162, 100006);
INSERT INTO `sys_role_menu` (`ROLE_ID`, `MENU_ID`) VALUES (1628283956224860162, 100009);
INSERT INTO `sys_role_menu` (`ROLE_ID`, `MENU_ID`) VALUES (1628283956224860162, 1628656999216459777);
INSERT INTO `sys_role_menu` (`ROLE_ID`, `MENU_ID`) VALUES (1628283956224860162, 1628728529633726466);
INSERT INTO `sys_role_menu` (`ROLE_ID`, `MENU_ID`) VALUES (1628283956224860162, 1628317214134038530);
INSERT INTO `sys_role_menu` (`ROLE_ID`, `MENU_ID`) VALUES (1628283956224860162, 100046);
INSERT INTO `sys_role_menu` (`ROLE_ID`, `MENU_ID`) VALUES (1628283956224860162, 100026);
INSERT INTO `sys_role_menu` (`ROLE_ID`, `MENU_ID`) VALUES (1628283956224860162, 100024);
INSERT INTO `sys_role_menu` (`ROLE_ID`, `MENU_ID`) VALUES (1628283956224860162, 1628966690909663233);
INSERT INTO `sys_role_menu` (`ROLE_ID`, `MENU_ID`) VALUES (1628283956224860162, 100010);
INSERT INTO `sys_role_menu` (`ROLE_ID`, `MENU_ID`) VALUES (1630127122991091713, 12);
INSERT INTO `sys_role_menu` (`ROLE_ID`, `MENU_ID`) VALUES (1630127122991091713, 4);
INSERT INTO `sys_role_menu` (`ROLE_ID`, `MENU_ID`) VALUES (1630127122991091713, 5);
INSERT INTO `sys_role_menu` (`ROLE_ID`, `MENU_ID`) VALUES (1630127122991091713, 6);
INSERT INTO `sys_role_menu` (`ROLE_ID`, `MENU_ID`) VALUES (1630127122991091713, 7);
INSERT INTO `sys_role_menu` (`ROLE_ID`, `MENU_ID`) VALUES (1630127122991091713, 21);
INSERT INTO `sys_role_menu` (`ROLE_ID`, `MENU_ID`) VALUES (1630127122991091713, 29);
INSERT INTO `sys_role_menu` (`ROLE_ID`, `MENU_ID`) VALUES (1630127122991091713, 100006);
INSERT INTO `sys_role_menu` (`ROLE_ID`, `MENU_ID`) VALUES (1630127122991091713, 100009);
INSERT INTO `sys_role_menu` (`ROLE_ID`, `MENU_ID`) VALUES (1630127122991091713, 100010);
INSERT INTO `sys_role_menu` (`ROLE_ID`, `MENU_ID`) VALUES (1630127122991091713, 100011);
INSERT INTO `sys_role_menu` (`ROLE_ID`, `MENU_ID`) VALUES (1630127122991091713, 100007);
INSERT INTO `sys_role_menu` (`ROLE_ID`, `MENU_ID`) VALUES (1630127122991091713, 100008);
INSERT INTO `sys_role_menu` (`ROLE_ID`, `MENU_ID`) VALUES (1630127122991091713, 100022);
INSERT INTO `sys_role_menu` (`ROLE_ID`, `MENU_ID`) VALUES (1630127122991091713, 100019);
INSERT INTO `sys_role_menu` (`ROLE_ID`, `MENU_ID`) VALUES (1630127122991091713, 100024);
INSERT INTO `sys_role_menu` (`ROLE_ID`, `MENU_ID`) VALUES (1630127122991091713, 100025);
INSERT INTO `sys_role_menu` (`ROLE_ID`, `MENU_ID`) VALUES (1630127122991091713, 100026);
INSERT INTO `sys_role_menu` (`ROLE_ID`, `MENU_ID`) VALUES (1630127122991091713, 100034);
INSERT INTO `sys_role_menu` (`ROLE_ID`, `MENU_ID`) VALUES (1630127122991091713, 100040);
INSERT INTO `sys_role_menu` (`ROLE_ID`, `MENU_ID`) VALUES (1630127122991091713, 100043);
INSERT INTO `sys_role_menu` (`ROLE_ID`, `MENU_ID`) VALUES (1630127122991091713, 100021);
INSERT INTO `sys_role_menu` (`ROLE_ID`, `MENU_ID`) VALUES (1630127122991091713, 20);
INSERT INTO `sys_role_menu` (`ROLE_ID`, `MENU_ID`) VALUES (1630131045481324546, 12);
INSERT INTO `sys_role_menu` (`ROLE_ID`, `MENU_ID`) VALUES (1630131045481324546, 4);
INSERT INTO `sys_role_menu` (`ROLE_ID`, `MENU_ID`) VALUES (1630131045481324546, 5);
INSERT INTO `sys_role_menu` (`ROLE_ID`, `MENU_ID`) VALUES (1630131045481324546, 6);
INSERT INTO `sys_role_menu` (`ROLE_ID`, `MENU_ID`) VALUES (1630131045481324546, 7);
INSERT INTO `sys_role_menu` (`ROLE_ID`, `MENU_ID`) VALUES (1630131045481324546, 21);
INSERT INTO `sys_role_menu` (`ROLE_ID`, `MENU_ID`) VALUES (1630131045481324546, 29);
INSERT INTO `sys_role_menu` (`ROLE_ID`, `MENU_ID`) VALUES (1630131045481324546, 100006);
INSERT INTO `sys_role_menu` (`ROLE_ID`, `MENU_ID`) VALUES (1630131045481324546, 100009);
INSERT INTO `sys_role_menu` (`ROLE_ID`, `MENU_ID`) VALUES (1630131045481324546, 100010);
INSERT INTO `sys_role_menu` (`ROLE_ID`, `MENU_ID`) VALUES (1630131045481324546, 100011);
INSERT INTO `sys_role_menu` (`ROLE_ID`, `MENU_ID`) VALUES (1630131045481324546, 100007);
INSERT INTO `sys_role_menu` (`ROLE_ID`, `MENU_ID`) VALUES (1630131045481324546, 100008);
INSERT INTO `sys_role_menu` (`ROLE_ID`, `MENU_ID`) VALUES (1630131045481324546, 100022);
INSERT INTO `sys_role_menu` (`ROLE_ID`, `MENU_ID`) VALUES (1630131045481324546, 100019);
INSERT INTO `sys_role_menu` (`ROLE_ID`, `MENU_ID`) VALUES (1630131045481324546, 100024);
INSERT INTO `sys_role_menu` (`ROLE_ID`, `MENU_ID`) VALUES (1630131045481324546, 100025);
INSERT INTO `sys_role_menu` (`ROLE_ID`, `MENU_ID`) VALUES (1630131045481324546, 100026);
INSERT INTO `sys_role_menu` (`ROLE_ID`, `MENU_ID`) VALUES (1630131045481324546, 100034);
INSERT INTO `sys_role_menu` (`ROLE_ID`, `MENU_ID`) VALUES (1630131045481324546, 100040);
INSERT INTO `sys_role_menu` (`ROLE_ID`, `MENU_ID`) VALUES (1630131045481324546, 100043);
INSERT INTO `sys_role_menu` (`ROLE_ID`, `MENU_ID`) VALUES (1630131045481324546, 100021);
INSERT INTO `sys_role_menu` (`ROLE_ID`, `MENU_ID`) VALUES (1630131045481324546, 20);
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
INSERT INTO `sys_route_conf` (`id`, `name`, `route_id`, `predicates`, `filters`, `uri`, `sort_order`, `metadata`, `create_by`, `update_by`, `create_time`, `update_time`, `del_flag`) VALUES (1568803192783544321, 'system模块', 'fxz-server-system', '[{\"args\": {\"_genkey_0\": \"/system/**\"}, \"name\": \"Path\"}]', '[{\"args\": {\"key-resolver\": \"#{@remoteAddrKeyResolver}\", \"redis-rate-limiter.burstCapacity\": \"100\", \"redis-rate-limiter.replenishRate\": \"100\"}, \"name\": \"RequestRateLimiter\"}]', 'lb://fxz-server-system', 0, NULL, 'fxz', 'fxz', '2022-09-11 11:26:43', '2022-09-11 11:33:06', '1');
INSERT INTO `sys_route_conf` (`id`, `name`, `route_id`, `predicates`, `filters`, `uri`, `sort_order`, `metadata`, `create_by`, `update_by`, `create_time`, `update_time`, `del_flag`) VALUES (1568803192796127233, 'auth模块', 'fxz-auth', '[{\"args\": {\"_genkey_0\": \"/auth/**\"}, \"name\": \"Path\"}]', NULL, 'lb://fxz-auth', 1, NULL, 'fxz', 'fxz', '2022-09-11 11:26:43', '2022-09-11 11:33:06', '1');
INSERT INTO `sys_route_conf` (`id`, `name`, `route_id`, `predicates`, `filters`, `uri`, `sort_order`, `metadata`, `create_by`, `update_by`, `create_time`, `update_time`, `del_flag`) VALUES (1568803192804515842, '代码生成器模块', 'fxz-generate', '[{\"args\": {\"_genkey_0\": \"/generate/**\"}, \"name\": \"Path\"}]', NULL, 'lb://fxz-generate', 2, NULL, 'fxz', 'fxz', '2022-09-11 11:26:43', '2022-09-11 11:33:06', '1');
INSERT INTO `sys_route_conf` (`id`, `name`, `route_id`, `predicates`, `filters`, `uri`, `sort_order`, `metadata`, `create_by`, `update_by`, `create_time`, `update_time`, `del_flag`) VALUES (1568803192808710146, '实验模块', 'fxz-z-demos', '[{\"args\": {\"_genkey_0\": \"/demos/**\"}, \"name\": \"Path\"}]', NULL, 'lb://fxz-demos', 3, NULL, 'fxz', 'fxz', '2022-09-11 11:26:43', '2022-09-11 11:33:06', '1');
INSERT INTO `sys_route_conf` (`id`, `name`, `route_id`, `predicates`, `filters`, `uri`, `sort_order`, `metadata`, `create_by`, `update_by`, `create_time`, `update_time`, `del_flag`) VALUES (1568803192808710147, '定时任务模块', 'fxz-job', '[{\"args\": {\"_genkey_0\": \"/schedule/**\"}, \"name\": \"Path\"}]', NULL, 'lb://fxz-job', 3, NULL, 'fxz', 'fxz', '2022-09-11 11:26:43', '2022-09-11 11:33:06', '1');
INSERT INTO `sys_route_conf` (`id`, `name`, `route_id`, `predicates`, `filters`, `uri`, `sort_order`, `metadata`, `create_by`, `update_by`, `create_time`, `update_time`, `del_flag`) VALUES (1568803192817098753, '会员模块', 'fxz-mall-user', '[{\"args\": {\"_genkey_0\": \"/user/**\"}, \"name\": \"Path\"}]', NULL, 'lb://fxz-mall-user', 100, NULL, 'fxz', 'fxz', '2022-09-11 11:26:43', '2022-09-11 11:33:06', '1');
INSERT INTO `sys_route_conf` (`id`, `name`, `route_id`, `predicates`, `filters`, `uri`, `sort_order`, `metadata`, `create_by`, `update_by`, `create_time`, `update_time`, `del_flag`) VALUES (1568803192821293057, '订单模块', 'fxz-mall-order', '[{\"args\": {\"_genkey_0\": \"/order/**\"}, \"name\": \"Path\"}]', NULL, 'lb://fxz-mall-order', 100, NULL, 'fxz', 'fxz', '2022-09-11 11:26:43', '2022-09-11 11:33:06', '1');
INSERT INTO `sys_route_conf` (`id`, `name`, `route_id`, `predicates`, `filters`, `uri`, `sort_order`, `metadata`, `create_by`, `update_by`, `create_time`, `update_time`, `del_flag`) VALUES (1568803192821293058, '促销模块', 'fxz-mall-promotion', '[{\"args\": {\"_genkey_0\": \"/promotion/**\"}, \"name\": \"Path\"}]', NULL, 'lb://fxz-mall-promotion', 100, NULL, 'fxz', 'fxz', '2022-09-11 11:26:43', '2022-09-11 11:33:06', '1');
INSERT INTO `sys_route_conf` (`id`, `name`, `route_id`, `predicates`, `filters`, `uri`, `sort_order`, `metadata`, `create_by`, `update_by`, `create_time`, `update_time`, `del_flag`) VALUES (1568803192825487362, '商品模块', 'fxz-mall-product', '[{\"args\": {\"_genkey_0\": \"/product/**\"}, \"name\": \"Path\"}]', NULL, 'lb://fxz-mall-product', 100, NULL, 'fxz', 'fxz', '2022-09-11 11:26:43', '2022-09-11 11:33:06', '1');
INSERT INTO `sys_route_conf` (`id`, `name`, `route_id`, `predicates`, `filters`, `uri`, `sort_order`, `metadata`, `create_by`, `update_by`, `create_time`, `update_time`, `del_flag`) VALUES (1568803192829681665, 'es检索模块', 'fxz-mall-search', '[{\"args\": {\"_genkey_0\": \"/search/**\"}, \"name\": \"Path\"}]', NULL, 'lb://fxz-mall-search', 100, NULL, 'fxz', 'fxz', '2022-09-11 11:26:43', '2022-09-11 11:33:06', '1');
INSERT INTO `sys_route_conf` (`id`, `name`, `route_id`, `predicates`, `filters`, `uri`, `sort_order`, `metadata`, `create_by`, `update_by`, `create_time`, `update_time`, `del_flag`) VALUES (1568804800481239042, 'system模块', 'fxz-server-system', '[{\"args\": {\"_genkey_0\": \"/system/**\"}, \"name\": \"Path\"}]', '[{\"args\": {\"key-resolver\": \"#{@remoteAddrKeyResolver}\", \"redis-rate-limiter.burstCapacity\": \"1\", \"redis-rate-limiter.replenishRate\": \"1\"}, \"name\": \"RequestRateLimiter\"}]', 'lb://fxz-server-system', 0, NULL, 'fxz', 'fxz', '2022-09-11 11:33:06', '2022-09-11 14:23:16', '1');
INSERT INTO `sys_route_conf` (`id`, `name`, `route_id`, `predicates`, `filters`, `uri`, `sort_order`, `metadata`, `create_by`, `update_by`, `create_time`, `update_time`, `del_flag`) VALUES (1568804800493821953, 'auth模块', 'fxz-auth', '[{\"args\": {\"_genkey_0\": \"/auth/**\"}, \"name\": \"Path\"}]', NULL, 'lb://fxz-auth', 1, NULL, 'fxz', 'fxz', '2022-09-11 11:33:06', '2022-09-11 14:23:16', '1');
INSERT INTO `sys_route_conf` (`id`, `name`, `route_id`, `predicates`, `filters`, `uri`, `sort_order`, `metadata`, `create_by`, `update_by`, `create_time`, `update_time`, `del_flag`) VALUES (1568804800498016257, '代码生成器模块', 'fxz-generate', '[{\"args\": {\"_genkey_0\": \"/generate/**\"}, \"name\": \"Path\"}]', NULL, 'lb://fxz-generate', 2, NULL, 'fxz', 'fxz', '2022-09-11 11:33:06', '2022-09-11 14:23:16', '1');
INSERT INTO `sys_route_conf` (`id`, `name`, `route_id`, `predicates`, `filters`, `uri`, `sort_order`, `metadata`, `create_by`, `update_by`, `create_time`, `update_time`, `del_flag`) VALUES (1568804800502210561, '实验模块', 'fxz-z-demos', '[{\"args\": {\"_genkey_0\": \"/demos/**\"}, \"name\": \"Path\"}]', NULL, 'lb://fxz-demos', 3, NULL, 'fxz', 'fxz', '2022-09-11 11:33:06', '2022-09-11 14:23:16', '1');
INSERT INTO `sys_route_conf` (`id`, `name`, `route_id`, `predicates`, `filters`, `uri`, `sort_order`, `metadata`, `create_by`, `update_by`, `create_time`, `update_time`, `del_flag`) VALUES (1568804800506404866, '定时任务模块', 'fxz-job', '[{\"args\": {\"_genkey_0\": \"/schedule/**\"}, \"name\": \"Path\"}]', NULL, 'lb://fxz-job', 3, NULL, 'fxz', 'fxz', '2022-09-11 11:33:06', '2022-09-11 14:23:16', '1');
INSERT INTO `sys_route_conf` (`id`, `name`, `route_id`, `predicates`, `filters`, `uri`, `sort_order`, `metadata`, `create_by`, `update_by`, `create_time`, `update_time`, `del_flag`) VALUES (1568804800506404867, 'es检索模块', 'fxz-mall-search', '[{\"args\": {\"_genkey_0\": \"/search/**\"}, \"name\": \"Path\"}]', NULL, 'lb://fxz-mall-search', 100, NULL, 'fxz', 'fxz', '2022-09-11 11:33:06', '2022-09-11 14:23:16', '1');
INSERT INTO `sys_route_conf` (`id`, `name`, `route_id`, `predicates`, `filters`, `uri`, `sort_order`, `metadata`, `create_by`, `update_by`, `create_time`, `update_time`, `del_flag`) VALUES (1568804800506404868, '会员模块', 'fxz-mall-user', '[{\"args\": {\"_genkey_0\": \"/user/**\"}, \"name\": \"Path\"}]', NULL, 'lb://fxz-mall-user', 100, NULL, 'fxz', 'fxz', '2022-09-11 11:33:06', '2022-09-11 14:23:16', '1');
INSERT INTO `sys_route_conf` (`id`, `name`, `route_id`, `predicates`, `filters`, `uri`, `sort_order`, `metadata`, `create_by`, `update_by`, `create_time`, `update_time`, `del_flag`) VALUES (1568804800510599170, '订单模块', 'fxz-mall-order', '[{\"args\": {\"_genkey_0\": \"/order/**\"}, \"name\": \"Path\"}]', NULL, 'lb://fxz-mall-order', 100, NULL, 'fxz', 'fxz', '2022-09-11 11:33:06', '2022-09-11 14:23:16', '1');
INSERT INTO `sys_route_conf` (`id`, `name`, `route_id`, `predicates`, `filters`, `uri`, `sort_order`, `metadata`, `create_by`, `update_by`, `create_time`, `update_time`, `del_flag`) VALUES (1568804800510599171, '促销模块', 'fxz-mall-promotion', '[{\"args\": {\"_genkey_0\": \"/promotion/**\"}, \"name\": \"Path\"}]', NULL, 'lb://fxz-mall-promotion', 100, NULL, 'fxz', 'fxz', '2022-09-11 11:33:06', '2022-09-11 14:23:16', '1');
INSERT INTO `sys_route_conf` (`id`, `name`, `route_id`, `predicates`, `filters`, `uri`, `sort_order`, `metadata`, `create_by`, `update_by`, `create_time`, `update_time`, `del_flag`) VALUES (1568804800514793473, '商品模块', 'fxz-mall-product', '[{\"args\": {\"_genkey_0\": \"/product/**\"}, \"name\": \"Path\"}]', NULL, 'lb://fxz-mall-product', 100, NULL, 'fxz', 'fxz', '2022-09-11 11:33:06', '2022-09-11 14:23:16', '1');
INSERT INTO `sys_route_conf` (`id`, `name`, `route_id`, `predicates`, `filters`, `uri`, `sort_order`, `metadata`, `create_by`, `update_by`, `create_time`, `update_time`, `del_flag`) VALUES (1568847626078339074, 'system模块', 'fxz-server-system', '[{\"args\": {\"_genkey_0\": \"/system/**\"}, \"name\": \"Path\"}]', '[{\"args\": {\"key-resolver\": \"#{@remoteAddrKeyResolver}\", \"redis-rate-limiter.burstCapacity\": \"100\", \"redis-rate-limiter.replenishRate\": \"100\"}, \"name\": \"RequestRateLimiter\"}]', 'lb://fxz-server-system', 0, NULL, 'fxz', 'fxz', '2022-09-11 14:23:16', '2023-02-22 16:31:59', '1');
INSERT INTO `sys_route_conf` (`id`, `name`, `route_id`, `predicates`, `filters`, `uri`, `sort_order`, `metadata`, `create_by`, `update_by`, `create_time`, `update_time`, `del_flag`) VALUES (1568847626145447938, 'auth模块', 'fxz-auth', '[{\"args\": {\"_genkey_0\": \"/auth/**\"}, \"name\": \"Path\"}]', NULL, 'lb://fxz-auth', 1, NULL, 'fxz', 'fxz', '2022-09-11 14:23:17', '2023-02-22 16:31:59', '1');
INSERT INTO `sys_route_conf` (`id`, `name`, `route_id`, `predicates`, `filters`, `uri`, `sort_order`, `metadata`, `create_by`, `update_by`, `create_time`, `update_time`, `del_flag`) VALUES (1568847626149642241, '代码生成器模块', 'fxz-generate', '[{\"args\": {\"_genkey_0\": \"/generate/**\"}, \"name\": \"Path\"}]', NULL, 'lb://fxz-generate', 2, NULL, 'fxz', 'fxz', '2022-09-11 14:23:17', '2023-02-22 16:31:59', '1');
INSERT INTO `sys_route_conf` (`id`, `name`, `route_id`, `predicates`, `filters`, `uri`, `sort_order`, `metadata`, `create_by`, `update_by`, `create_time`, `update_time`, `del_flag`) VALUES (1568847626162225153, '实验模块', 'fxz-z-demos', '[{\"args\": {\"_genkey_0\": \"/demos/**\"}, \"name\": \"Path\"}]', NULL, 'lb://fxz-demos', 3, NULL, 'fxz', 'fxz', '2022-09-11 14:23:17', '2023-02-22 16:31:59', '1');
INSERT INTO `sys_route_conf` (`id`, `name`, `route_id`, `predicates`, `filters`, `uri`, `sort_order`, `metadata`, `create_by`, `update_by`, `create_time`, `update_time`, `del_flag`) VALUES (1568847626162225154, '定时任务模块', 'fxz-job', '[{\"args\": {\"_genkey_0\": \"/schedule/**\"}, \"name\": \"Path\"}]', NULL, 'lb://fxz-job', 3, NULL, 'fxz', 'fxz', '2022-09-11 14:23:17', '2023-02-22 16:31:59', '1');
INSERT INTO `sys_route_conf` (`id`, `name`, `route_id`, `predicates`, `filters`, `uri`, `sort_order`, `metadata`, `create_by`, `update_by`, `create_time`, `update_time`, `del_flag`) VALUES (1568847626162225155, '商品模块', 'fxz-mall-product', '[{\"args\": {\"_genkey_0\": \"/product/**\"}, \"name\": \"Path\"}]', NULL, 'lb://fxz-mall-product', 100, NULL, 'fxz', 'fxz', '2022-09-11 14:23:17', '2023-02-22 16:31:59', '1');
INSERT INTO `sys_route_conf` (`id`, `name`, `route_id`, `predicates`, `filters`, `uri`, `sort_order`, `metadata`, `create_by`, `update_by`, `create_time`, `update_time`, `del_flag`) VALUES (1568847626166419457, '订单模块', 'fxz-mall-order', '[{\"args\": {\"_genkey_0\": \"/order/**\"}, \"name\": \"Path\"}]', NULL, 'lb://fxz-mall-order', 100, NULL, 'fxz', 'fxz', '2022-09-11 14:23:17', '2023-02-22 16:31:59', '1');
INSERT INTO `sys_route_conf` (`id`, `name`, `route_id`, `predicates`, `filters`, `uri`, `sort_order`, `metadata`, `create_by`, `update_by`, `create_time`, `update_time`, `del_flag`) VALUES (1568847626166419458, 'es检索模块', 'fxz-mall-search', '[{\"args\": {\"_genkey_0\": \"/search/**\"}, \"name\": \"Path\"}]', NULL, 'lb://fxz-mall-search', 100, NULL, 'fxz', 'fxz', '2022-09-11 14:23:17', '2023-02-22 16:31:59', '1');
INSERT INTO `sys_route_conf` (`id`, `name`, `route_id`, `predicates`, `filters`, `uri`, `sort_order`, `metadata`, `create_by`, `update_by`, `create_time`, `update_time`, `del_flag`) VALUES (1568847626174808065, '促销模块', 'fxz-mall-promotion', '[{\"args\": {\"_genkey_0\": \"/promotion/**\"}, \"name\": \"Path\"}]', NULL, 'lb://fxz-mall-promotion', 100, NULL, 'fxz', 'fxz', '2022-09-11 14:23:17', '2023-02-22 16:31:59', '1');
INSERT INTO `sys_route_conf` (`id`, `name`, `route_id`, `predicates`, `filters`, `uri`, `sort_order`, `metadata`, `create_by`, `update_by`, `create_time`, `update_time`, `del_flag`) VALUES (1568847626174808066, '会员模块', 'fxz-mall-user', '[{\"args\": {\"_genkey_0\": \"/user/**\"}, \"name\": \"Path\"}]', NULL, 'lb://fxz-mall-user', 100, NULL, 'fxz', 'fxz', '2022-09-11 14:23:17', '2023-02-22 16:31:59', '1');
INSERT INTO `sys_route_conf` (`id`, `name`, `route_id`, `predicates`, `filters`, `uri`, `sort_order`, `metadata`, `create_by`, `update_by`, `create_time`, `update_time`, `del_flag`) VALUES (1628311641445638145, 'system模块', 'fxz-server-system', '[{\"args\": {\"_genkey_0\": \"/system/**\"}, \"name\": \"Path\"}]', '[{\"args\": {\"key-resolver\": \"#{@remoteAddrKeyResolver}\", \"redis-rate-limiter.burstCapacity\": \"100\", \"redis-rate-limiter.replenishRate\": \"100\"}, \"name\": \"RequestRateLimiter\"}]', 'lb://fxz-server-system', 0, NULL, 'fxz', 'fxz', '2023-02-22 16:32:03', '2023-02-22 16:34:52', '1');
INSERT INTO `sys_route_conf` (`id`, `name`, `route_id`, `predicates`, `filters`, `uri`, `sort_order`, `metadata`, `create_by`, `update_by`, `create_time`, `update_time`, `del_flag`) VALUES (1628311641684713474, 'auth模块', 'fxz-auth', '[{\"args\": {\"_genkey_0\": \"/auth/**\"}, \"name\": \"Path\"}]', NULL, 'lb://fxz-auth', 1, NULL, 'fxz', 'fxz', '2023-02-22 16:32:03', '2023-02-22 16:34:52', '1');
INSERT INTO `sys_route_conf` (`id`, `name`, `route_id`, `predicates`, `filters`, `uri`, `sort_order`, `metadata`, `create_by`, `update_by`, `create_time`, `update_time`, `del_flag`) VALUES (1628311642007674881, 'edu模块', 'art-edu', '[{\"args\": {\"_genkey_0\": \"/edu/**\"}, \"name\": \"Path\"}]', NULL, 'lb://art-edu', 1, NULL, 'fxz', 'fxz', '2023-02-22 16:32:03', '2023-02-22 16:34:52', '1');
INSERT INTO `sys_route_conf` (`id`, `name`, `route_id`, `predicates`, `filters`, `uri`, `sort_order`, `metadata`, `create_by`, `update_by`, `create_time`, `update_time`, `del_flag`) VALUES (1628311642510991362, '代码生成器模块', 'fxz-generate', '[{\"args\": {\"_genkey_0\": \"/generate/**\"}, \"name\": \"Path\"}]', NULL, 'lb://fxz-generate', 2, NULL, 'fxz', 'fxz', '2023-02-22 16:32:03', '2023-02-22 16:34:52', '1');
INSERT INTO `sys_route_conf` (`id`, `name`, `route_id`, `predicates`, `filters`, `uri`, `sort_order`, `metadata`, `create_by`, `update_by`, `create_time`, `update_time`, `del_flag`) VALUES (1628311643173691394, '实验模块', 'fxz-z-demos', '[{\"args\": {\"_genkey_0\": \"/demos/**\"}, \"name\": \"Path\"}]', NULL, 'lb://fxz-demos', 3, NULL, 'fxz', 'fxz', '2023-02-22 16:32:03', '2023-02-22 16:34:52', '1');
INSERT INTO `sys_route_conf` (`id`, `name`, `route_id`, `predicates`, `filters`, `uri`, `sort_order`, `metadata`, `create_by`, `update_by`, `create_time`, `update_time`, `del_flag`) VALUES (1628311643614093314, '定时任务模块', 'fxz-job', '[{\"args\": {\"_genkey_0\": \"/schedule/**\"}, \"name\": \"Path\"}]', NULL, 'lb://fxz-job', 3, NULL, 'fxz', 'fxz', '2023-02-22 16:32:03', '2023-02-22 16:34:52', '1');
INSERT INTO `sys_route_conf` (`id`, `name`, `route_id`, `predicates`, `filters`, `uri`, `sort_order`, `metadata`, `create_by`, `update_by`, `create_time`, `update_time`, `del_flag`) VALUES (1628311643874140162, '商品模块', 'fxz-mall-product', '[{\"args\": {\"_genkey_0\": \"/product/**\"}, \"name\": \"Path\"}]', NULL, 'lb://fxz-mall-product', 100, NULL, 'fxz', 'fxz', '2023-02-22 16:32:03', '2023-02-22 16:34:52', '1');
INSERT INTO `sys_route_conf` (`id`, `name`, `route_id`, `predicates`, `filters`, `uri`, `sort_order`, `metadata`, `create_by`, `update_by`, `create_time`, `update_time`, `del_flag`) VALUES (1628311644276793345, '订单模块', 'fxz-mall-order', '[{\"args\": {\"_genkey_0\": \"/order/**\"}, \"name\": \"Path\"}]', NULL, 'lb://fxz-mall-order', 100, NULL, 'fxz', 'fxz', '2023-02-22 16:32:03', '2023-02-22 16:34:52', '1');
INSERT INTO `sys_route_conf` (`id`, `name`, `route_id`, `predicates`, `filters`, `uri`, `sort_order`, `metadata`, `create_by`, `update_by`, `create_time`, `update_time`, `del_flag`) VALUES (1628311644641697794, 'es检索模块', 'fxz-mall-search', '[{\"args\": {\"_genkey_0\": \"/search/**\"}, \"name\": \"Path\"}]', NULL, 'lb://fxz-mall-search', 100, NULL, 'fxz', 'fxz', '2023-02-22 16:32:03', '2023-02-22 16:34:52', '1');
INSERT INTO `sys_route_conf` (`id`, `name`, `route_id`, `predicates`, `filters`, `uri`, `sort_order`, `metadata`, `create_by`, `update_by`, `create_time`, `update_time`, `del_flag`) VALUES (1628311645308592129, '促销模块', 'fxz-mall-promotion', '[{\"args\": {\"_genkey_0\": \"/promotion/**\"}, \"name\": \"Path\"}]', NULL, 'lb://fxz-mall-promotion', 100, NULL, 'fxz', 'fxz', '2023-02-22 16:32:03', '2023-02-22 16:34:52', '1');
INSERT INTO `sys_route_conf` (`id`, `name`, `route_id`, `predicates`, `filters`, `uri`, `sort_order`, `metadata`, `create_by`, `update_by`, `create_time`, `update_time`, `del_flag`) VALUES (1628311645707051009, '会员模块', 'fxz-mall-user', '[{\"args\": {\"_genkey_0\": \"/user/**\"}, \"name\": \"Path\"}]', NULL, 'lb://fxz-mall-user', 100, NULL, 'fxz', 'fxz', '2023-02-22 16:32:04', '2023-02-22 16:34:52', '1');
INSERT INTO `sys_route_conf` (`id`, `name`, `route_id`, `predicates`, `filters`, `uri`, `sort_order`, `metadata`, `create_by`, `update_by`, `create_time`, `update_time`, `del_flag`) VALUES (1628312365936156673, 'system模块', 'fxz-server-system', '[{\"args\": {\"_genkey_0\": \"/system/**\"}, \"name\": \"Path\"}]', '[{\"args\": {\"key-resolver\": \"#{@remoteAddrKeyResolver}\", \"redis-rate-limiter.burstCapacity\": \"100\", \"redis-rate-limiter.replenishRate\": \"100\"}, \"name\": \"RequestRateLimiter\"}]', 'lb://fxz-server-system', 0, NULL, 'fxz', 'fxz', '2023-02-22 16:34:55', '2023-02-22 16:38:31', '1');
INSERT INTO `sys_route_conf` (`id`, `name`, `route_id`, `predicates`, `filters`, `uri`, `sort_order`, `metadata`, `create_by`, `update_by`, `create_time`, `update_time`, `del_flag`) VALUES (1628312366212980737, 'auth模块', 'fxz-auth', '[{\"args\": {\"_genkey_0\": \"/auth/**\"}, \"name\": \"Path\"}]', NULL, 'lb://fxz-auth', 1, NULL, 'fxz', 'fxz', '2023-02-22 16:34:55', '2023-02-22 16:38:31', '1');
INSERT INTO `sys_route_conf` (`id`, `name`, `route_id`, `predicates`, `filters`, `uri`, `sort_order`, `metadata`, `create_by`, `update_by`, `create_time`, `update_time`, `del_flag`) VALUES (1628312366452056066, 'edu模块', 'art-edu', '[{\"args\": {\"_genkey_0\": \"/edu/**\"}, \"name\": \"Path\"}]', NULL, 'lb://art-edu', 1, NULL, 'fxz', 'fxz', '2023-02-22 16:34:55', '2023-02-22 16:38:31', '1');
INSERT INTO `sys_route_conf` (`id`, `name`, `route_id`, `predicates`, `filters`, `uri`, `sort_order`, `metadata`, `create_by`, `update_by`, `create_time`, `update_time`, `del_flag`) VALUES (1628312366707908610, '代码生成器模块', 'fxz-generate', '[{\"args\": {\"_genkey_0\": \"/generate/**\"}, \"name\": \"Path\"}]', NULL, 'lb://fxz-generate', 2, NULL, 'fxz', 'fxz', '2023-02-22 16:34:55', '2023-02-22 16:38:31', '1');
INSERT INTO `sys_route_conf` (`id`, `name`, `route_id`, `predicates`, `filters`, `uri`, `sort_order`, `metadata`, `create_by`, `update_by`, `create_time`, `update_time`, `del_flag`) VALUES (1628312366959566849, '实验模块', 'fxz-z-demos', '[{\"args\": {\"_genkey_0\": \"/demos/**\"}, \"name\": \"Path\"}]', NULL, 'lb://fxz-demos', 3, NULL, 'fxz', 'fxz', '2023-02-22 16:34:56', '2023-02-22 16:38:31', '1');
INSERT INTO `sys_route_conf` (`id`, `name`, `route_id`, `predicates`, `filters`, `uri`, `sort_order`, `metadata`, `create_by`, `update_by`, `create_time`, `update_time`, `del_flag`) VALUES (1628312367387385858, '定时任务模块', 'fxz-job', '[{\"args\": {\"_genkey_0\": \"/schedule/**\"}, \"name\": \"Path\"}]', NULL, 'lb://fxz-job', 3, NULL, 'fxz', 'fxz', '2023-02-22 16:34:56', '2023-02-22 16:38:31', '1');
INSERT INTO `sys_route_conf` (`id`, `name`, `route_id`, `predicates`, `filters`, `uri`, `sort_order`, `metadata`, `create_by`, `update_by`, `create_time`, `update_time`, `del_flag`) VALUES (1628312367622266881, '商品模块', 'fxz-mall-product', '[{\"args\": {\"_genkey_0\": \"/product/**\"}, \"name\": \"Path\"}]', NULL, 'lb://fxz-mall-product', 100, NULL, 'fxz', 'fxz', '2023-02-22 16:34:56', '2023-02-22 16:38:31', '1');
INSERT INTO `sys_route_conf` (`id`, `name`, `route_id`, `predicates`, `filters`, `uri`, `sort_order`, `metadata`, `create_by`, `update_by`, `create_time`, `update_time`, `del_flag`) VALUES (1628312367836176385, '订单模块', 'fxz-mall-order', '[{\"args\": {\"_genkey_0\": \"/order/**\"}, \"name\": \"Path\"}]', NULL, 'lb://fxz-mall-order', 100, NULL, 'fxz', 'fxz', '2023-02-22 16:34:56', '2023-02-22 16:38:31', '1');
INSERT INTO `sys_route_conf` (`id`, `name`, `route_id`, `predicates`, `filters`, `uri`, `sort_order`, `metadata`, `create_by`, `update_by`, `create_time`, `update_time`, `del_flag`) VALUES (1628312368083640321, 'es检索模块', 'fxz-mall-search', '[{\"args\": {\"_genkey_0\": \"/search/**\"}, \"name\": \"Path\"}]', NULL, 'lb://fxz-mall-search', 100, NULL, 'fxz', 'fxz', '2023-02-22 16:34:56', '2023-02-22 16:38:31', '1');
INSERT INTO `sys_route_conf` (`id`, `name`, `route_id`, `predicates`, `filters`, `uri`, `sort_order`, `metadata`, `create_by`, `update_by`, `create_time`, `update_time`, `del_flag`) VALUES (1628312368343687169, '促销模块', 'fxz-mall-promotion', '[{\"args\": {\"_genkey_0\": \"/promotion/**\"}, \"name\": \"Path\"}]', NULL, 'lb://fxz-mall-promotion', 100, NULL, 'fxz', 'fxz', '2023-02-22 16:34:56', '2023-02-22 16:38:31', '1');
INSERT INTO `sys_route_conf` (`id`, `name`, `route_id`, `predicates`, `filters`, `uri`, `sort_order`, `metadata`, `create_by`, `update_by`, `create_time`, `update_time`, `del_flag`) VALUES (1628312368565985281, '会员模块', 'fxz-mall-user', '[{\"args\": {\"_genkey_0\": \"/user/**\"}, \"name\": \"Path\"}]', NULL, 'lb://fxz-mall-user', 100, NULL, 'fxz', 'fxz', '2023-02-22 16:34:56', '2023-02-22 16:38:31', '1');
INSERT INTO `sys_route_conf` (`id`, `name`, `route_id`, `predicates`, `filters`, `uri`, `sort_order`, `metadata`, `create_by`, `update_by`, `create_time`, `update_time`, `del_flag`) VALUES (1628313283010027521, 'system模块', 'fxz-server-system', '[{\"args\": {\"_genkey_0\": \"/system/**\"}, \"name\": \"Path\"}]', '[{\"args\": {\"key-resolver\": \"#{@remoteAddrKeyResolver}\", \"redis-rate-limiter.burstCapacity\": \"100\", \"redis-rate-limiter.replenishRate\": \"100\"}, \"name\": \"RequestRateLimiter\"}]', 'lb://fxz-server-system', 0, NULL, 'fxz', 'fxz', '2023-02-22 16:38:34', '2023-02-22 16:46:24', '1');
INSERT INTO `sys_route_conf` (`id`, `name`, `route_id`, `predicates`, `filters`, `uri`, `sort_order`, `metadata`, `create_by`, `update_by`, `create_time`, `update_time`, `del_flag`) VALUES (1628313283010027522, 'system模块', 'fxz-server-system', '[{\"args\": {\"_genkey_0\": \"/system/**\"}, \"name\": \"Path\"}]', '[{\"args\": {\"key-resolver\": \"#{@remoteAddrKeyResolver}\", \"redis-rate-limiter.burstCapacity\": \"100\", \"redis-rate-limiter.replenishRate\": \"100\"}, \"name\": \"RequestRateLimiter\"}]', 'lb://fxz-server-system', 0, NULL, 'fxz', 'fxz', '2023-02-22 16:38:34', '2023-02-22 16:48:07', '1');
INSERT INTO `sys_route_conf` (`id`, `name`, `route_id`, `predicates`, `filters`, `uri`, `sort_order`, `metadata`, `create_by`, `update_by`, `create_time`, `update_time`, `del_flag`) VALUES (1628313283454623746, 'auth模块', 'fxz-auth', '[{\"args\": {\"_genkey_0\": \"/auth/**\"}, \"name\": \"Path\"}]', NULL, 'lb://fxz-auth', 1, NULL, 'fxz', 'fxz', '2023-02-22 16:38:34', '2023-02-22 16:46:27', '1');
INSERT INTO `sys_route_conf` (`id`, `name`, `route_id`, `predicates`, `filters`, `uri`, `sort_order`, `metadata`, `create_by`, `update_by`, `create_time`, `update_time`, `del_flag`) VALUES (1628313283454623747, 'auth模块', 'fxz-auth', '[{\"args\": {\"_genkey_0\": \"/auth/**\"}, \"name\": \"Path\"}]', NULL, 'lb://fxz-auth', 1, NULL, 'fxz', 'fxz', '2023-02-22 16:38:34', '2023-02-22 16:48:07', '1');
INSERT INTO `sys_route_conf` (`id`, `name`, `route_id`, `predicates`, `filters`, `uri`, `sort_order`, `metadata`, `create_by`, `update_by`, `create_time`, `update_time`, `del_flag`) VALUES (1628313283798556674, 'edu模块', 'art-edu', '[{\"args\": {\"_genkey_0\": \"/edu/**\"}, \"name\": \"Path\"}]', NULL, 'lb://art-edu', 1, NULL, 'fxz', 'fxz', '2023-02-22 16:38:34', '2023-02-22 16:46:30', '1');
INSERT INTO `sys_route_conf` (`id`, `name`, `route_id`, `predicates`, `filters`, `uri`, `sort_order`, `metadata`, `create_by`, `update_by`, `create_time`, `update_time`, `del_flag`) VALUES (1628313283802750977, 'edu模块', 'art-edu', '[{\"args\": {\"_genkey_0\": \"/edu/**\"}, \"name\": \"Path\"}]', NULL, 'lb://art-edu', 1, NULL, 'fxz', 'fxz', '2023-02-22 16:38:34', '2023-02-22 16:48:07', '1');
INSERT INTO `sys_route_conf` (`id`, `name`, `route_id`, `predicates`, `filters`, `uri`, `sort_order`, `metadata`, `create_by`, `update_by`, `create_time`, `update_time`, `del_flag`) VALUES (1628313284197015554, '代码生成器模块', 'fxz-generate', '[{\"args\": {\"_genkey_0\": \"/generate/**\"}, \"name\": \"Path\"}]', NULL, 'lb://fxz-generate', 2, NULL, 'fxz', 'fxz', '2023-02-22 16:38:34', '2023-02-22 16:46:34', '1');
INSERT INTO `sys_route_conf` (`id`, `name`, `route_id`, `predicates`, `filters`, `uri`, `sort_order`, `metadata`, `create_by`, `update_by`, `create_time`, `update_time`, `del_flag`) VALUES (1628313284222181377, '代码生成器模块', 'fxz-generate', '[{\"args\": {\"_genkey_0\": \"/generate/**\"}, \"name\": \"Path\"}]', NULL, 'lb://fxz-generate', 2, NULL, 'fxz', 'fxz', '2023-02-22 16:38:34', '2023-02-22 16:48:07', '1');
INSERT INTO `sys_route_conf` (`id`, `name`, `route_id`, `predicates`, `filters`, `uri`, `sort_order`, `metadata`, `create_by`, `update_by`, `create_time`, `update_time`, `del_flag`) VALUES (1628313284503199745, '实验模块', 'fxz-z-demos', '[{\"args\": {\"_genkey_0\": \"/demos/**\"}, \"name\": \"Path\"}]', NULL, 'lb://fxz-demos', 3, NULL, 'fxz', 'fxz', '2023-02-22 16:38:34', '2023-02-22 16:46:36', '1');
INSERT INTO `sys_route_conf` (`id`, `name`, `route_id`, `predicates`, `filters`, `uri`, `sort_order`, `metadata`, `create_by`, `update_by`, `create_time`, `update_time`, `del_flag`) VALUES (1628313284503199746, '实验模块', 'fxz-z-demos', '[{\"args\": {\"_genkey_0\": \"/demos/**\"}, \"name\": \"Path\"}]', NULL, 'lb://fxz-demos', 3, NULL, 'fxz', 'fxz', '2023-02-22 16:38:34', '2023-02-22 16:48:07', '1');
INSERT INTO `sys_route_conf` (`id`, `name`, `route_id`, `predicates`, `filters`, `uri`, `sort_order`, `metadata`, `create_by`, `update_by`, `create_time`, `update_time`, `del_flag`) VALUES (1628313284796801025, '定时任务模块', 'fxz-job', '[{\"args\": {\"_genkey_0\": \"/schedule/**\"}, \"name\": \"Path\"}]', NULL, 'lb://fxz-job', 3, NULL, 'fxz', 'fxz', '2023-02-22 16:38:34', '2023-02-22 16:46:40', '1');
INSERT INTO `sys_route_conf` (`id`, `name`, `route_id`, `predicates`, `filters`, `uri`, `sort_order`, `metadata`, `create_by`, `update_by`, `create_time`, `update_time`, `del_flag`) VALUES (1628313284796801026, '定时任务模块', 'fxz-job', '[{\"args\": {\"_genkey_0\": \"/schedule/**\"}, \"name\": \"Path\"}]', NULL, 'lb://fxz-job', 3, NULL, 'fxz', 'fxz', '2023-02-22 16:38:34', '2023-02-22 16:48:07', '1');
INSERT INTO `sys_route_conf` (`id`, `name`, `route_id`, `predicates`, `filters`, `uri`, `sort_order`, `metadata`, `create_by`, `update_by`, `create_time`, `update_time`, `del_flag`) VALUES (1628313285048459265, '商品模块', 'fxz-mall-product', '[{\"args\": {\"_genkey_0\": \"/product/**\"}, \"name\": \"Path\"}]', NULL, 'lb://fxz-mall-product', 100, NULL, 'fxz', 'fxz', '2023-02-22 16:38:34', '2023-02-22 16:46:43', '1');
INSERT INTO `sys_route_conf` (`id`, `name`, `route_id`, `predicates`, `filters`, `uri`, `sort_order`, `metadata`, `create_by`, `update_by`, `create_time`, `update_time`, `del_flag`) VALUES (1628313285128151042, '商品模块', 'fxz-mall-product', '[{\"args\": {\"_genkey_0\": \"/product/**\"}, \"name\": \"Path\"}]', NULL, 'lb://fxz-mall-product', 100, NULL, 'fxz', 'fxz', '2023-02-22 16:38:34', '2023-02-22 16:48:07', '1');
INSERT INTO `sys_route_conf` (`id`, `name`, `route_id`, `predicates`, `filters`, `uri`, `sort_order`, `metadata`, `create_by`, `update_by`, `create_time`, `update_time`, `del_flag`) VALUES (1628313285342060546, '订单模块', 'fxz-mall-order', '[{\"args\": {\"_genkey_0\": \"/order/**\"}, \"name\": \"Path\"}]', NULL, 'lb://fxz-mall-order', 100, NULL, 'fxz', 'fxz', '2023-02-22 16:38:34', '2023-02-22 16:46:45', '1');
INSERT INTO `sys_route_conf` (`id`, `name`, `route_id`, `predicates`, `filters`, `uri`, `sort_order`, `metadata`, `create_by`, `update_by`, `create_time`, `update_time`, `del_flag`) VALUES (1628313285421752321, '订单模块', 'fxz-mall-order', '[{\"args\": {\"_genkey_0\": \"/order/**\"}, \"name\": \"Path\"}]', NULL, 'lb://fxz-mall-order', 100, NULL, 'fxz', 'fxz', '2023-02-22 16:38:34', '2023-02-22 16:48:07', '1');
INSERT INTO `sys_route_conf` (`id`, `name`, `route_id`, `predicates`, `filters`, `uri`, `sort_order`, `metadata`, `create_by`, `update_by`, `create_time`, `update_time`, `del_flag`) VALUES (1628313285614690305, 'es检索模块', 'fxz-mall-search', '[{\"args\": {\"_genkey_0\": \"/search/**\"}, \"name\": \"Path\"}]', NULL, 'lb://fxz-mall-search', 100, NULL, 'fxz', 'fxz', '2023-02-22 16:38:35', '2023-02-22 16:46:49', '1');
INSERT INTO `sys_route_conf` (`id`, `name`, `route_id`, `predicates`, `filters`, `uri`, `sort_order`, `metadata`, `create_by`, `update_by`, `create_time`, `update_time`, `del_flag`) VALUES (1628313285761490946, 'es检索模块', 'fxz-mall-search', '[{\"args\": {\"_genkey_0\": \"/search/**\"}, \"name\": \"Path\"}]', NULL, 'lb://fxz-mall-search', 100, NULL, 'fxz', 'fxz', '2023-02-22 16:38:35', '2023-02-22 16:48:07', '1');
INSERT INTO `sys_route_conf` (`id`, `name`, `route_id`, `predicates`, `filters`, `uri`, `sort_order`, `metadata`, `create_by`, `update_by`, `create_time`, `update_time`, `del_flag`) VALUES (1628313285845377025, '促销模块', 'fxz-mall-promotion', '[{\"args\": {\"_genkey_0\": \"/promotion/**\"}, \"name\": \"Path\"}]', NULL, 'lb://fxz-mall-promotion', 100, NULL, 'fxz', 'fxz', '2023-02-22 16:38:35', '2023-02-22 16:46:53', '1');
INSERT INTO `sys_route_conf` (`id`, `name`, `route_id`, `predicates`, `filters`, `uri`, `sort_order`, `metadata`, `create_by`, `update_by`, `create_time`, `update_time`, `del_flag`) VALUES (1628313286134784001, '促销模块', 'fxz-mall-promotion', '[{\"args\": {\"_genkey_0\": \"/promotion/**\"}, \"name\": \"Path\"}]', NULL, 'lb://fxz-mall-promotion', 100, NULL, 'fxz', 'fxz', '2023-02-22 16:38:35', '2023-02-22 16:48:07', '1');
INSERT INTO `sys_route_conf` (`id`, `name`, `route_id`, `predicates`, `filters`, `uri`, `sort_order`, `metadata`, `create_by`, `update_by`, `create_time`, `update_time`, `del_flag`) VALUES (1628313286227058690, '会员模块', 'fxz-mall-user', '[{\"args\": {\"_genkey_0\": \"/user/**\"}, \"name\": \"Path\"}]', NULL, 'lb://fxz-mall-user', 100, NULL, 'fxz', 'fxz', '2023-02-22 16:38:35', '2023-02-22 16:46:59', '1');
INSERT INTO `sys_route_conf` (`id`, `name`, `route_id`, `predicates`, `filters`, `uri`, `sort_order`, `metadata`, `create_by`, `update_by`, `create_time`, `update_time`, `del_flag`) VALUES (1628313286461939714, '会员模块', 'fxz-mall-user', '[{\"args\": {\"_genkey_0\": \"/user/**\"}, \"name\": \"Path\"}]', NULL, 'lb://fxz-mall-user', 100, NULL, 'fxz', 'fxz', '2023-02-22 16:38:35', '2023-02-22 16:48:07', '1');
INSERT INTO `sys_route_conf` (`id`, `name`, `route_id`, `predicates`, `filters`, `uri`, `sort_order`, `metadata`, `create_by`, `update_by`, `create_time`, `update_time`, `del_flag`) VALUES (1628315700145483778, 'system模块', 'fxz-server-system', '[{\"args\": {\"_genkey_0\": \"/system/**\"}, \"name\": \"Path\"}]', '[{\"args\": {\"key-resolver\": \"#{@remoteAddrKeyResolver}\", \"redis-rate-limiter.burstCapacity\": \"100\", \"redis-rate-limiter.replenishRate\": \"100\"}, \"name\": \"RequestRateLimiter\"}]', 'lb://fxz-server-system', 0, NULL, 'fxz', 'fxz', '2023-02-22 16:48:10', '2023-02-22 16:48:10', '0');
INSERT INTO `sys_route_conf` (`id`, `name`, `route_id`, `predicates`, `filters`, `uri`, `sort_order`, `metadata`, `create_by`, `update_by`, `create_time`, `update_time`, `del_flag`) VALUES (1628315700384559106, 'auth模块', 'fxz-auth', '[{\"args\": {\"_genkey_0\": \"/auth/**\"}, \"name\": \"Path\"}]', NULL, 'lb://fxz-auth', 1, NULL, 'fxz', 'fxz', '2023-02-22 16:48:10', '2023-02-22 16:48:10', '0');
INSERT INTO `sys_route_conf` (`id`, `name`, `route_id`, `predicates`, `filters`, `uri`, `sort_order`, `metadata`, `create_by`, `update_by`, `create_time`, `update_time`, `del_flag`) VALUES (1628315700694937601, 'edu模块', 'art-edu', '[{\"args\": {\"_genkey_0\": \"/edu/**\"}, \"name\": \"Path\"}]', NULL, 'lb://art-edu', 1, NULL, 'fxz', 'fxz', '2023-02-22 16:48:10', '2023-02-22 16:48:10', '0');
INSERT INTO `sys_route_conf` (`id`, `name`, `route_id`, `predicates`, `filters`, `uri`, `sort_order`, `metadata`, `create_by`, `update_by`, `create_time`, `update_time`, `del_flag`) VALUES (1628315701189865474, '代码生成器模块', 'fxz-generate', '[{\"args\": {\"_genkey_0\": \"/generate/**\"}, \"name\": \"Path\"}]', NULL, 'lb://fxz-generate', 2, NULL, 'fxz', 'fxz', '2023-02-22 16:48:10', '2023-02-22 16:48:10', '0');
INSERT INTO `sys_route_conf` (`id`, `name`, `route_id`, `predicates`, `filters`, `uri`, `sort_order`, `metadata`, `create_by`, `update_by`, `create_time`, `update_time`, `del_flag`) VALUES (1628315701487661058, '实验模块', 'fxz-z-demos', '[{\"args\": {\"_genkey_0\": \"/demos/**\"}, \"name\": \"Path\"}]', NULL, 'lb://fxz-demos', 3, NULL, 'fxz', 'fxz', '2023-02-22 16:48:11', '2023-02-22 16:48:11', '0');
INSERT INTO `sys_route_conf` (`id`, `name`, `route_id`, `predicates`, `filters`, `uri`, `sort_order`, `metadata`, `create_by`, `update_by`, `create_time`, `update_time`, `del_flag`) VALUES (1628315701823205378, '定时任务模块', 'fxz-job', '[{\"args\": {\"_genkey_0\": \"/schedule/**\"}, \"name\": \"Path\"}]', NULL, 'lb://fxz-job', 3, NULL, 'fxz', 'fxz', '2023-02-22 16:48:11', '2023-02-22 16:48:11', '0');
INSERT INTO `sys_route_conf` (`id`, `name`, `route_id`, `predicates`, `filters`, `uri`, `sort_order`, `metadata`, `create_by`, `update_by`, `create_time`, `update_time`, `del_flag`) VALUES (1628315702112612354, '商品模块', 'fxz-mall-product', '[{\"args\": {\"_genkey_0\": \"/product/**\"}, \"name\": \"Path\"}]', NULL, 'lb://fxz-mall-product', 100, NULL, 'fxz', 'fxz', '2023-02-22 16:48:11', '2023-02-22 16:48:11', '0');
INSERT INTO `sys_route_conf` (`id`, `name`, `route_id`, `predicates`, `filters`, `uri`, `sort_order`, `metadata`, `create_by`, `update_by`, `create_time`, `update_time`, `del_flag`) VALUES (1628315702439768066, '订单模块', 'fxz-mall-order', '[{\"args\": {\"_genkey_0\": \"/order/**\"}, \"name\": \"Path\"}]', NULL, 'lb://fxz-mall-order', 100, NULL, 'fxz', 'fxz', '2023-02-22 16:48:11', '2023-02-22 16:48:11', '0');
INSERT INTO `sys_route_conf` (`id`, `name`, `route_id`, `predicates`, `filters`, `uri`, `sort_order`, `metadata`, `create_by`, `update_by`, `create_time`, `update_time`, `del_flag`) VALUES (1628315703068913666, 'es检索模块', 'fxz-mall-search', '[{\"args\": {\"_genkey_0\": \"/search/**\"}, \"name\": \"Path\"}]', NULL, 'lb://fxz-mall-search', 100, NULL, 'fxz', 'fxz', '2023-02-22 16:48:11', '2023-02-22 16:48:11', '0');
INSERT INTO `sys_route_conf` (`id`, `name`, `route_id`, `predicates`, `filters`, `uri`, `sort_order`, `metadata`, `create_by`, `update_by`, `create_time`, `update_time`, `del_flag`) VALUES (1628315703387680770, '促销模块', 'fxz-mall-promotion', '[{\"args\": {\"_genkey_0\": \"/promotion/**\"}, \"name\": \"Path\"}]', NULL, 'lb://fxz-mall-promotion', 100, NULL, 'fxz', 'fxz', '2023-02-22 16:48:11', '2023-02-22 16:48:11', '0');
INSERT INTO `sys_route_conf` (`id`, `name`, `route_id`, `predicates`, `filters`, `uri`, `sort_order`, `metadata`, `create_by`, `update_by`, `create_time`, `update_time`, `del_flag`) VALUES (1628315703802916866, '会员模块', 'fxz-mall-user', '[{\"args\": {\"_genkey_0\": \"/user/**\"}, \"name\": \"Path\"}]', NULL, 'lb://fxz-mall-user', 100, NULL, 'fxz', 'fxz', '2023-02-22 16:48:11', '2023-02-22 16:48:11', '0');
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
) ENGINE=InnoDB AUTO_INCREMENT=1630131045384855555 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='租户表';

-- ----------------------------
-- Records of sys_tenant
-- ----------------------------
BEGIN;
INSERT INTO `sys_tenant` (`id`, `name`, `tenant_admin_id`, `tenant_admin_name`, `tenant_admin_mobile`, `status`, `package_id`, `expire_time`, `account_count`, `create_by`, `create_time`, `update_by`, `update_time`, `del_flag`) VALUES (0, 'fxzcloud', 14, 'fxz', '19812341234', 0, 0, '3000-03-30 00:00:00', 100, '1', '2022-02-22 00:56:14', 'fxz', '2023-04-04 17:36:37', b'0');
INSERT INTO `sys_tenant` (`id`, `name`, `tenant_admin_id`, `tenant_admin_name`, `tenant_admin_mobile`, `status`, `package_id`, `expire_time`, `account_count`, `create_by`, `create_time`, `update_by`, `update_time`, `del_flag`) VALUES (1576530912007237634, '测试租户', 5, '老六', '16601231234', 0, 1620302578562916353, '2023-07-29 14:13:27', 100, 'fxz', '2022-10-02 19:13:55', 'fxz', '2023-02-03 13:19:12', b'1');
INSERT INTO `sys_tenant` (`id`, `name`, `tenant_admin_id`, `tenant_admin_name`, `tenant_admin_mobile`, `status`, `package_id`, `expire_time`, `account_count`, `create_by`, `create_time`, `update_by`, `update_time`, `del_flag`) VALUES (1630127122500358146, '13812345678', 14, '13812345678', '13812345678', 0, 1576530575020077058, '2023-02-28 00:00:00', 11, 'fxz', '2023-02-27 16:46:07', 'fxz', '2023-04-04 17:35:59', b'1');
INSERT INTO `sys_tenant` (`id`, `name`, `tenant_admin_id`, `tenant_admin_name`, `tenant_admin_mobile`, `status`, `package_id`, `expire_time`, `account_count`, `create_by`, `create_time`, `update_by`, `update_time`, `del_flag`) VALUES (1630131045384855554, '1', 14, '1', '15912345678', 0, 1576530575020077058, '2023-02-28 00:00:00', 111, 'fxz', '2023-02-27 17:01:42', 'fxz', '2023-04-04 17:35:56', b'1');
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
  `menu_ids` varchar(2048) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '关联的菜单编号',
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
INSERT INTO `sys_tenant_package` (`id`, `name`, `status`, `remark`, `menu_ids`, `create_by`, `create_time`, `update_by`, `update_time`, `del_flag`) VALUES (1620302578562916353, '吊毛套餐', 0, '专为贫穷设计', '5,6,7,21,29,100006,100009,100010,100011,100007,100008,100022,100019,100024,100025,100026,100034,100040,100043,100021', 'fxz', '2022-10-02 19:12:34', 'fxz', '2023-02-03 13:19:17', b'1');
INSERT INTO `sys_tenant_package` (`id`, `name`, `status`, `remark`, `menu_ids`, `create_by`, `create_time`, `update_by`, `update_time`, `del_flag`) VALUES (1620355238989148162, 's', 0, '1', '12,4,5,6,7,21,29,100006,100009,100010,100011,100007,100008,100022,100019,100024,100025,100026,100034,100040,100043,100021,20', 'fxz', '2022-10-02 19:12:34', 'fxz', '2023-01-31 17:55:08', b'1');
INSERT INTO `sys_tenant_package` (`id`, `name`, `status`, `remark`, `menu_ids`, `create_by`, `create_time`, `update_by`, `update_time`, `del_flag`) VALUES (1620355905602465793, '11', 1, '1', '12,4,5,6,7,21,29,100006,100009,100010,100011,100007,100008,100022,100019,100024,100025,100026,100034,100040,100043,100021,20', 'fxz', '2023-01-31 17:38:47', 'fxz', '2023-01-31 17:55:11', b'1');
INSERT INTO `sys_tenant_package` (`id`, `name`, `status`, `remark`, `menu_ids`, `create_by`, `create_time`, `update_by`, `update_time`, `del_flag`) VALUES (1620356519480799233, '111aaaaa', 0, '1', '12,4,5,6,7,21,29,100006,100009,100010,100011,100007,100008,100022,100019,100024,100025,100026,100034,100040,100043,100021,20', 'fxz', '2022-10-02 19:12:34', 'fxz', '2023-01-31 17:55:18', b'1');
INSERT INTO `sys_tenant_package` (`id`, `name`, `status`, `remark`, `menu_ids`, `create_by`, `create_time`, `update_by`, `update_time`, `del_flag`) VALUES (1620356834594664450, '666', 1, '', '12,4,5,6,7,21,29,100006,100009,100010,100011,100007,100008,100022,100019,100024,100025,100026,100034,100040,100043,100021,20', 'fxz', '2023-01-31 17:42:29', 'fxz', '2023-01-31 17:55:20', b'1');
INSERT INTO `sys_tenant_package` (`id`, `name`, `status`, `remark`, `menu_ids`, `create_by`, `create_time`, `update_by`, `update_time`, `del_flag`) VALUES (1620378325738639361, '爱心社AS', 0, 'SAS', '12', 'fxz', '2023-01-31 19:07:53', 'fxz', '2023-01-31 19:06:05', b'1');
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
) ENGINE=InnoDB AUTO_INCREMENT=15 DEFAULT CHARSET=utf8mb3 COMMENT='用户表';

-- ----------------------------
-- Records of sys_user
-- ----------------------------
BEGIN;
INSERT INTO `sys_user` (`user_id`, `username`, `password`, `dept_id`, `email`, `mobile`, `status`, `create_time`, `update_time`, `last_login_time`, `ssex`, `avatar`, `description`, `create_by`, `update_by`, `tenant_id`) VALUES (1, 'fxz', '{bcrypt}$2a$10$yMqLTL9t9TeRGp5fT7vENuxZaruaN.a/VlOpRGF7jBnKPAim6Xey.', 10, '2****@qq.com', '198****2431', '1', '2022-04-05 20:39:22', '2023-02-22 19:31:16', '2022-04-05 15:57:00', '0', '', 'Art的作者,FxzClud的核心成员。', NULL, 'fxz', 0);
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

SET FOREIGN_KEY_CHECKS = 1;
