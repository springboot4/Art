
SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for sys_dept
-- ----------------------------
DROP TABLE IF EXISTS `sys_dept`;
CREATE TABLE `sys_dept` (
  `dept_id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '部门ID',
  `parent_id` bigint(20) NOT NULL COMMENT '上级部门ID',
  `dept_name` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '部门名称',
  `order_num` double(20,0) DEFAULT NULL COMMENT '排序',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime DEFAULT NULL COMMENT '修改时间',
  `create_by` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '创建人',
  `update_by` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '更新人',
  PRIMARY KEY (`dept_id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=8 DEFAULT CHARSET=utf8 COMMENT='部门表';

-- ----------------------------
-- Records of sys_dept
-- ----------------------------
BEGIN;
INSERT INTO `sys_dept` (`dept_id`, `parent_id`, `dept_name`, `order_num`, `create_time`, `update_time`, `create_by`, `update_by`) VALUES (0, -1, 'fxzcloud', 0, '2022-02-28 16:42:49', '2022-08-11 22:06:16', NULL, 'fxz');
INSERT INTO `sys_dept` (`dept_id`, `parent_id`, `dept_name`, `order_num`, `create_time`, `update_time`, `create_by`, `update_by`) VALUES (1, 0, '研发部', 0, '2022-02-28 16:43:53', '2022-02-28 16:43:55', NULL, NULL);
INSERT INTO `sys_dept` (`dept_id`, `parent_id`, `dept_name`, `order_num`, `create_time`, `update_time`, `create_by`, `update_by`) VALUES (2, 0, '市场部', -1, '2022-02-28 16:43:10', '2022-03-26 20:16:52', NULL, 'fxz');
INSERT INTO `sys_dept` (`dept_id`, `parent_id`, `dept_name`, `order_num`, `create_time`, `update_time`, `create_by`, `update_by`) VALUES (3, 2, '销售部', 0, '2022-02-28 16:43:27', '2022-03-25 19:32:45', NULL, 'fxz');
INSERT INTO `sys_dept` (`dept_id`, `parent_id`, `dept_name`, `order_num`, `create_time`, `update_time`, `create_by`, `update_by`) VALUES (4, 1, '前端组', 1, '2022-02-28 16:44:29', '2022-02-28 16:44:29', NULL, NULL);
INSERT INTO `sys_dept` (`dept_id`, `parent_id`, `dept_name`, `order_num`, `create_time`, `update_time`, `create_by`, `update_by`) VALUES (5, 1, '后端组', 0, '2022-02-28 16:44:41', '2022-03-26 20:26:43', NULL, 'fxz');
COMMIT;

-- ----------------------------
-- Table structure for sys_dict
-- ----------------------------
DROP TABLE IF EXISTS `sys_dict`;
CREATE TABLE `sys_dict` (
  `id` bigint(20) NOT NULL,
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
COMMIT;

-- ----------------------------
-- Table structure for sys_dict_item
-- ----------------------------
DROP TABLE IF EXISTS `sys_dict_item`;
CREATE TABLE `sys_dict_item` (
  `id` bigint(20) NOT NULL,
  `dict_id` bigint(20) NOT NULL COMMENT '字典ID',
  `value` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '值',
  `label` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '标签',
  `type` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '字典类型',
  `description` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '描述',
  `sort_order` int(11) NOT NULL DEFAULT '0' COMMENT '排序（升序）',
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
  `id` bigint(20) NOT NULL,
  `file_name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL,
  `bucket_name` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL,
  `original` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL,
  `type` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL,
  `file_size` bigint(20) DEFAULT NULL COMMENT '文件大小',
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
INSERT INTO `sys_file` (`id`, `file_name`, `bucket_name`, `original`, `type`, `file_size`, `del_flag`, `create_time`, `update_time`, `create_by`, `update_by`) VALUES (1562696776226283521, '1de3526e8d5543e1841968d9ad0f8788.png', 'fxzcloud', '3c7ab88b25934d71a9e20f1a8a369e51.png', 'png', 20151, '0', '2022-08-25 15:02:00', '2022-08-25 15:02:00', 'fxz', 'fxz');
INSERT INTO `sys_file` (`id`, `file_name`, `bucket_name`, `original`, `type`, `file_size`, `del_flag`, `create_time`, `update_time`, `create_by`, `update_by`) VALUES (1562703566783442945, 'ddb56bf4fe7044999c7b1516724d738c.jpg', 'fxzcloud', '631659497692_.pic.jpg', 'jpg', 121716, '0', '2022-08-25 15:28:59', '2022-08-25 15:28:59', 'fxz', 'fxz');
INSERT INTO `sys_file` (`id`, `file_name`, `bucket_name`, `original`, `type`, `file_size`, `del_flag`, `create_time`, `update_time`, `create_by`, `update_by`) VALUES (1562812767195209730, '76fa32f1b8904b0ab3e256ab3a7736f3.svg', 'fxzcloud', 'logo.svg', 'svg', 6834, '0', '2022-08-25 22:42:54', '2022-08-25 22:42:54', 'fxz', 'fxz');
INSERT INTO `sys_file` (`id`, `file_name`, `bucket_name`, `original`, `type`, `file_size`, `del_flag`, `create_time`, `update_time`, `create_by`, `update_by`) VALUES (1563843483030806530, '10ecdc1273c24e37a213616b604de77c.png', 'fxzcloud', '3c7ab88b25934d71a9e20f1a8a369e51.png', 'png', 20151, '0', '2022-08-28 18:58:36', '2022-08-28 18:58:36', 'fxz', 'fxz');
INSERT INTO `sys_file` (`id`, `file_name`, `bucket_name`, `original`, `type`, `file_size`, `del_flag`, `create_time`, `update_time`, `create_by`, `update_by`) VALUES (1564238448072404994, '3d829549ad714c5383d93ef74c73c73f.jpg', 'fxzcloud', '631659497692_.pic.jpg', 'jpg', 121716, '0', '2022-08-29 21:08:03', '2022-08-29 21:08:03', 'fxz', 'fxz');
INSERT INTO `sys_file` (`id`, `file_name`, `bucket_name`, `original`, `type`, `file_size`, `del_flag`, `create_time`, `update_time`, `create_by`, `update_by`) VALUES (1564239003960291329, '698ae344620c4564ad2021e84668d57e.jpg', 'fxzcloud', '631659497692_.pic.jpg', 'jpg', 121716, '0', '2022-08-29 21:10:15', '2022-08-29 21:10:15', 'fxz', 'fxz');
INSERT INTO `sys_file` (`id`, `file_name`, `bucket_name`, `original`, `type`, `file_size`, `del_flag`, `create_time`, `update_time`, `create_by`, `update_by`) VALUES (1564240047347937282, '98c0064a394149f38e0761ec4c88f4bf.jpg', 'fxzcloud', '631659497692_.pic.jpg', 'jpg', 121716, '0', '2022-08-29 21:14:24', '2022-08-29 21:14:24', 'fxz', 'fxz');
INSERT INTO `sys_file` (`id`, `file_name`, `bucket_name`, `original`, `type`, `file_size`, `del_flag`, `create_time`, `update_time`, `create_by`, `update_by`) VALUES (1564242762115395586, 'c0a05fb910bc4d61bb8f45de6ed9b575.jpg', 'fxzcloud', '631659497692_.pic.jpg', 'jpg', 121716, '0', '2022-08-29 21:25:11', '2022-08-29 21:25:11', 'fxz', 'fxz');
INSERT INTO `sys_file` (`id`, `file_name`, `bucket_name`, `original`, `type`, `file_size`, `del_flag`, `create_time`, `update_time`, `create_by`, `update_by`) VALUES (1564437480673660930, '102ff1c9828a4d99ba365eaf866577e1.jpg', 'fxzcloud', '631659497692_.pic.jpg', 'jpg', 121716, '0', '2022-08-30 10:18:56', '2022-08-30 10:18:56', 'fxz', 'fxz');
INSERT INTO `sys_file` (`id`, `file_name`, `bucket_name`, `original`, `type`, `file_size`, `del_flag`, `create_time`, `update_time`, `create_by`, `update_by`) VALUES (1564437703407980546, 'becfd62040b2413ca9126c8b21afe808.jpg', 'fxzcloud', '631659497692_.pic.jpg', 'jpg', 121716, '0', '2022-08-30 10:19:49', '2022-08-30 10:19:49', 'fxz', 'fxz');
INSERT INTO `sys_file` (`id`, `file_name`, `bucket_name`, `original`, `type`, `file_size`, `del_flag`, `create_time`, `update_time`, `create_by`, `update_by`) VALUES (1564437891262468097, '7183f708b6214cdd8a32a954fe4ca9c3.png', 'fxzcloud', '1de3526e8d5543e1841968d9ad0f8788.png', 'png', 20151, '0', '2022-08-30 10:20:34', '2022-08-30 10:20:34', 'fxz', 'fxz');
INSERT INTO `sys_file` (`id`, `file_name`, `bucket_name`, `original`, `type`, `file_size`, `del_flag`, `create_time`, `update_time`, `create_by`, `update_by`) VALUES (1568575004098691074, 'ad8e220e61ac47709446a704debac098.png', 'fxzcloud', '1de3526e8d5543e1841968d9ad0f8788.png', 'png', 20151, '0', '2022-09-10 20:19:58', '2022-09-10 20:19:58', 'fxz', 'fxz');
INSERT INTO `sys_file` (`id`, `file_name`, `bucket_name`, `original`, `type`, `file_size`, `del_flag`, `create_time`, `update_time`, `create_by`, `update_by`) VALUES (1568575161141821442, '6d0a2f8f10d148d29710bece5bbdc057.png', 'fxzcloud', '1de3526e8d5543e1841968d9ad0f8788.png', 'png', 20151, '0', '2022-09-10 20:20:36', '2022-09-10 20:20:36', 'fxz', 'fxz');
INSERT INTO `sys_file` (`id`, `file_name`, `bucket_name`, `original`, `type`, `file_size`, `del_flag`, `create_time`, `update_time`, `create_by`, `update_by`) VALUES (1568575692685967361, '207309e3914441d09723909fe0decaac.png', 'fxzcloud', '1de3526e8d5543e1841968d9ad0f8788.png', 'png', 20151, '0', '2022-09-10 20:22:43', '2022-09-10 20:22:43', 'fxz', 'fxz');
COMMIT;

-- ----------------------------
-- Table structure for sys_menu
-- ----------------------------
DROP TABLE IF EXISTS `sys_menu`;
CREATE TABLE `sys_menu` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '菜单/按钮ID',
  `parent_id` bigint(20) NOT NULL COMMENT '上级菜单ID',
  `title` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT 'title',
  `name` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '菜单/按钮名称',
  `perms` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '权限标识(多个用逗号分隔，如：user:list,user:create)',
  `type` char(2) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '类型 0菜单 1按钮',
  `component` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '对应路由组件component',
  `path` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '对应路由path',
  `redirect` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '重定向',
  `icon` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '图标',
  `keep_alive` tinyint(4) DEFAULT '1' COMMENT '是否缓存 0:否 1:是',
  `order_num` double(20,0) DEFAULT NULL COMMENT '排序',
  `create_time` datetime NOT NULL COMMENT '创建时间',
  `update_time` datetime DEFAULT NULL COMMENT '修改时间',
  `create_by` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '创建人',
  `update_by` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '更新人',
  `hidden` char(10) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT '0' COMMENT '是否隐藏(1 隐藏 0 不隐藏)',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=100043 DEFAULT CHARSET=utf8 COMMENT='菜单表';

-- ----------------------------
-- Records of sys_menu
-- ----------------------------
BEGIN;
INSERT INTO `sys_menu` (`id`, `parent_id`, `title`, `name`, `perms`, `type`, `component`, `path`, `redirect`, `icon`, `keep_alive`, `order_num`, `create_time`, `update_time`, `create_by`, `update_by`, `hidden`) VALUES (4, 0, '个人页', 'account', 'user', '0', 'RouteView', '/account', '', 'aliwangwang', 1, 2, '2022-01-23 17:04:07', '2022-04-04 17:29:00', NULL, 'fxz', '1');
INSERT INTO `sys_menu` (`id`, `parent_id`, `title`, `name`, `perms`, `type`, `component`, `path`, `redirect`, `icon`, `keep_alive`, `order_num`, `create_time`, `update_time`, `create_by`, `update_by`, `hidden`) VALUES (5, 4, '个人中心', 'center', 'user', '0', 'account/center', '/account/center', NULL, NULL, 1, 2, '2022-01-23 17:04:58', NULL, NULL, NULL, NULL);
INSERT INTO `sys_menu` (`id`, `parent_id`, `title`, `name`, `perms`, `type`, `component`, `path`, `redirect`, `icon`, `keep_alive`, `order_num`, `create_time`, `update_time`, `create_by`, `update_by`, `hidden`) VALUES (6, 4, '个人设置', 'settings', 'user', '0', 'account/settings/Index', '/account/settings', NULL, NULL, 1, 2, '2022-01-18 14:44:25', NULL, NULL, NULL, '1');
INSERT INTO `sys_menu` (`id`, `parent_id`, `title`, `name`, `perms`, `type`, `component`, `path`, `redirect`, `icon`, `keep_alive`, `order_num`, `create_time`, `update_time`, `create_by`, `update_by`, `hidden`) VALUES (7, 6, '基本设置', 'BaseSettings', 'user', '0', 'account/settings/BaseSetting', '/account/settings/base', NULL, NULL, 1, 2, '2022-01-18 14:48:57', NULL, NULL, NULL, '0');
INSERT INTO `sys_menu` (`id`, `parent_id`, `title`, `name`, `perms`, `type`, `component`, `path`, `redirect`, `icon`, `keep_alive`, `order_num`, `create_time`, `update_time`, `create_by`, `update_by`, `hidden`) VALUES (8, 6, '安全设置', 'SecuritySettings', 'user', '0', 'account/settings/Security', '/account/settings/security', NULL, NULL, 1, 2, '2022-01-18 14:49:39', NULL, NULL, NULL, NULL);
INSERT INTO `sys_menu` (`id`, `parent_id`, `title`, `name`, `perms`, `type`, `component`, `path`, `redirect`, `icon`, `keep_alive`, `order_num`, `create_time`, `update_time`, `create_by`, `update_by`, `hidden`) VALUES (9, 6, '个性化设置', 'CustomSettings', 'user', '0', 'account/settings/Custom', '/account/settings/custom', NULL, NULL, 1, 2, '2022-01-18 14:50:17', NULL, NULL, NULL, NULL);
INSERT INTO `sys_menu` (`id`, `parent_id`, `title`, `name`, `perms`, `type`, `component`, `path`, `redirect`, `icon`, `keep_alive`, `order_num`, `create_time`, `update_time`, `create_by`, `update_by`, `hidden`) VALUES (10, 6, '账户绑定', 'BindingSettings', 'user', '0', 'account/settings/Binding', '/account/settings/binding', NULL, NULL, 1, 2, '2022-01-18 14:50:58', NULL, NULL, NULL, NULL);
INSERT INTO `sys_menu` (`id`, `parent_id`, `title`, `name`, `perms`, `type`, `component`, `path`, `redirect`, `icon`, `keep_alive`, `order_num`, `create_time`, `update_time`, `create_by`, `update_by`, `hidden`) VALUES (11, 6, '新消息通知', 'NotificationSettings', 'user', '0', 'account/settings/Notification', '/account/settings/notification', NULL, NULL, 1, 2, '2022-01-18 14:51:31', NULL, NULL, NULL, NULL);
INSERT INTO `sys_menu` (`id`, `parent_id`, `title`, `name`, `perms`, `type`, `component`, `path`, `redirect`, `icon`, `keep_alive`, `order_num`, `create_time`, `update_time`, `create_by`, `update_by`, `hidden`) VALUES (12, 0, '首页', 'welcome', 'welcome', '0', 'dashboard/Analysis', '/welcome', '', 'twitter', 1, 0, '2022-01-18 16:22:11', '2022-08-11 21:51:48', NULL, 'fxz', '0');
INSERT INTO `sys_menu` (`id`, `parent_id`, `title`, `name`, `perms`, `type`, `component`, `path`, `redirect`, `icon`, `keep_alive`, `order_num`, `create_time`, `update_time`, `create_by`, `update_by`, `hidden`) VALUES (20, 0, '系统管理', 'permissions', 'sys', '0', 'RouteView', '/permissions', NULL, 'rocket', 1, 3, '2022-01-23 16:52:53', '2022-04-05 16:17:42', NULL, 'fxz', '0');
INSERT INTO `sys_menu` (`id`, `parent_id`, `title`, `name`, `perms`, `type`, `component`, `path`, `redirect`, `icon`, `keep_alive`, `order_num`, `create_time`, `update_time`, `create_by`, `update_by`, `hidden`) VALUES (21, 20, '菜单管理', 'menu', 'sys:menu', '0', 'modules/system/menu/menu', '/permissions/menu', NULL, 'idcard', 1, 0, '2022-01-23 16:55:48', '2022-04-04 17:31:22', NULL, 'fxz', '0');
INSERT INTO `sys_menu` (`id`, `parent_id`, `title`, `name`, `perms`, `type`, `component`, `path`, `redirect`, `icon`, `keep_alive`, `order_num`, `create_time`, `update_time`, `create_by`, `update_by`, `hidden`) VALUES (29, 21, '添加菜单', 'saveMenu', 'sys:menu:save', '1', NULL, NULL, NULL, NULL, 1, 1, '2022-01-23 18:16:31', NULL, NULL, NULL, '1');
INSERT INTO `sys_menu` (`id`, `parent_id`, `title`, `name`, `perms`, `type`, `component`, `path`, `redirect`, `icon`, `keep_alive`, `order_num`, `create_time`, `update_time`, `create_by`, `update_by`, `hidden`) VALUES (100006, 20, '用户管理', 'user', 'sys:user', '0', 'modules/system/user/userList.vue', '/permissions/user', NULL, 'user', 1, 1, '2022-02-27 09:35:00', '2022-04-05 20:24:14', NULL, 'fxz', '0');
INSERT INTO `sys_menu` (`id`, `parent_id`, `title`, `name`, `perms`, `type`, `component`, `path`, `redirect`, `icon`, `keep_alive`, `order_num`, `create_time`, `update_time`, `create_by`, `update_by`, `hidden`) VALUES (100007, 20, '角色管理', 'role', 'sys:role', '0', 'modules/system/role/RoleList.vue', '/permissions/role', NULL, 'woman', 1, 2, '2022-02-27 09:43:02', '2022-04-05 20:24:23', NULL, 'fxz', '0');
INSERT INTO `sys_menu` (`id`, `parent_id`, `title`, `name`, `perms`, `type`, `component`, `path`, `redirect`, `icon`, `keep_alive`, `order_num`, `create_time`, `update_time`, `create_by`, `update_by`, `hidden`) VALUES (100008, 20, '部门管理', 'sys:dept', 'dept', '0', 'modules/system/dept/DeptList.vue', '/permissions/dept', NULL, 'contacts', 1, 3, '2022-02-27 10:15:46', '2022-06-26 21:16:18', NULL, 'fxz', '0');
INSERT INTO `sys_menu` (`id`, `parent_id`, `title`, `name`, `perms`, `type`, `component`, `path`, `redirect`, `icon`, `keep_alive`, `order_num`, `create_time`, `update_time`, `create_by`, `update_by`, `hidden`) VALUES (100009, 100006, '分页查看用户信息', 'sys:user:view', 'sys:user:view', '1', 'sys:user:view', '/sys/user/list', NULL, NULL, 1, 1, '2022-02-27 13:43:05', NULL, NULL, NULL, '0');
INSERT INTO `sys_menu` (`id`, `parent_id`, `title`, `name`, `perms`, `type`, `component`, `path`, `redirect`, `icon`, `keep_alive`, `order_num`, `create_time`, `update_time`, `create_by`, `update_by`, `hidden`) VALUES (100010, 100006, '更新用户信息', 'sys:user:update', 'sys:user:update', '1', NULL, 'sys:user:update', NULL, NULL, 1, NULL, '2022-02-27 17:05:44', NULL, NULL, NULL, '0');
INSERT INTO `sys_menu` (`id`, `parent_id`, `title`, `name`, `perms`, `type`, `component`, `path`, `redirect`, `icon`, `keep_alive`, `order_num`, `create_time`, `update_time`, `create_by`, `update_by`, `hidden`) VALUES (100011, 100006, '新增用户信息', 'sys:user:add', 'sys:user:add', '1', NULL, 'sys:user:add', NULL, NULL, 1, NULL, '2022-02-27 18:07:12', NULL, NULL, NULL, '0');
INSERT INTO `sys_menu` (`id`, `parent_id`, `title`, `name`, `perms`, `type`, `component`, `path`, `redirect`, `icon`, `keep_alive`, `order_num`, `create_time`, `update_time`, `create_by`, `update_by`, `hidden`) VALUES (100012, 0, '系统工具', 'sysTool', 'sysTool', '0', 'RouteView', '/sysTool', NULL, 'shopping-cart', 1, 999, '2022-03-04 09:14:37', '2022-05-18 17:52:55', NULL, 'fxz', '0');
INSERT INTO `sys_menu` (`id`, `parent_id`, `title`, `name`, `perms`, `type`, `component`, `path`, `redirect`, `icon`, `keep_alive`, `order_num`, `create_time`, `update_time`, `create_by`, `update_by`, `hidden`) VALUES (100013, 100012, '代码生成器', 'genCode', 'sysTool:genCode', '0', 'modules/system/gen/CodeGenIndex.vue', '/sysTool/genCode', NULL, 'area-chart', 1, 1, '2022-03-04 09:19:37', '2022-04-04 17:33:00', NULL, 'fxz', '0');
INSERT INTO `sys_menu` (`id`, `parent_id`, `title`, `name`, `perms`, `type`, `component`, `path`, `redirect`, `icon`, `keep_alive`, `order_num`, `create_time`, `update_time`, `create_by`, `update_by`, `hidden`) VALUES (100016, 100012, '数据源管理', 'DatasourceConfList', 'sysTool:datasourceConf', '0', 'modules/system/gen/datasource/DatasourceConfList.vue', '/sysTool/datasourceConf', NULL, 'box-plot', 1, 0, '2022-03-31 12:31:26', '2022-04-04 17:33:10', 'fxz', 'fxz', '0');
INSERT INTO `sys_menu` (`id`, `parent_id`, `title`, `name`, `perms`, `type`, `component`, `path`, `redirect`, `icon`, `keep_alive`, `order_num`, `create_time`, `update_time`, `create_by`, `update_by`, `hidden`) VALUES (100017, 0, '系统监控', 'monitor', 'sysMonitor', '0', 'RouteView', '/sysMonitor', NULL, 'aliwangwang', 1, 1000, '2022-04-03 17:59:52', '2022-05-18 17:53:07', 'fxz', 'fxz', '0');
INSERT INTO `sys_menu` (`id`, `parent_id`, `title`, `name`, `perms`, `type`, `component`, `path`, `redirect`, `icon`, `keep_alive`, `order_num`, `create_time`, `update_time`, `create_by`, `update_by`, `hidden`) VALUES (100018, 100017, '定时任务', 'jobList', 'sysMonitor:job', '0', 'modules/system/monitor/JobList.vue', '/sysMonitor/job', NULL, 'loading', 1, 0, '2022-04-03 18:02:04', '2022-04-04 17:33:39', 'fxz', 'fxz', '0');
INSERT INTO `sys_menu` (`id`, `parent_id`, `title`, `name`, `perms`, `type`, `component`, `path`, `redirect`, `icon`, `keep_alive`, `order_num`, `create_time`, `update_time`, `create_by`, `update_by`, `hidden`) VALUES (100019, 20, '字典管理', 'dict', 'sys:dict', '0', 'modules/system/dict/DictList.vue', '/permissions/dict', NULL, 'folder-open', 1, 4, '2022-04-04 11:20:33', '2022-04-05 20:24:45', 'fxz', 'fxz', '0');
INSERT INTO `sys_menu` (`id`, `parent_id`, `title`, `name`, `perms`, `type`, `component`, `path`, `redirect`, `icon`, `keep_alive`, `order_num`, `create_time`, `update_time`, `create_by`, `update_by`, `hidden`) VALUES (100020, 100017, '调度日志', 'JobLogList', 'sysMonitor:jobLog', '0', 'modules/system/monitor/JobLogList.vue', '/sysMonitor/jobLog', NULL, 'hdd', 1, 1, '2022-04-04 15:55:43', '2022-04-04 17:34:06', 'fxz', 'fxz', '0');
INSERT INTO `sys_menu` (`id`, `parent_id`, `title`, `name`, `perms`, `type`, `component`, `path`, `redirect`, `icon`, `keep_alive`, `order_num`, `create_time`, `update_time`, `create_by`, `update_by`, `hidden`) VALUES (100021, 20, '文件管理', 'file', 'sys:file', '0', 'modules/system/file/FileList.vue', '/permissions/file', NULL, 'mail', 1, 5, '2022-04-04 22:47:38', '2022-04-04 22:49:20', 'fxz', 'fxz', '0');
INSERT INTO `sys_menu` (`id`, `parent_id`, `title`, `name`, `perms`, `type`, `component`, `path`, `redirect`, `icon`, `keep_alive`, `order_num`, `create_time`, `update_time`, `create_by`, `update_by`, `hidden`) VALUES (100022, 20, '岗位管理', 'post', 'sys:post', '0', 'modules/system/post/PostList.vue', '/permissions/post', NULL, 'coffee', 1, 4, '2022-04-05 20:17:47', '2022-04-05 20:24:58', 'fxz', 'fxz', '0');
INSERT INTO `sys_menu` (`id`, `parent_id`, `title`, `name`, `perms`, `type`, `component`, `path`, `redirect`, `icon`, `keep_alive`, `order_num`, `create_time`, `update_time`, `create_by`, `update_by`, `hidden`) VALUES (100023, 100012, '表单设计器', 'FormBuild', 'sysTool:formBuild', '0', 'modules/system/gen/FormBuild.vue', '/sysTool/formBuild', NULL, 'bg-colors', 1, 2, '2022-04-23 23:22:38', '2022-04-23 23:38:03', 'fxz', 'fxz', '0');
INSERT INTO `sys_menu` (`id`, `parent_id`, `title`, `name`, `perms`, `type`, `component`, `path`, `redirect`, `icon`, `keep_alive`, `order_num`, `create_time`, `update_time`, `create_by`, `update_by`, `hidden`) VALUES (100024, 20, '审计管理', 'oper', 'sys:oper', '0', 'RouteView', '/permissions/oper', NULL, 'credit-card', 1, 7, '2022-04-24 00:12:34', '2022-04-24 00:12:34', 'fxz', 'fxz', '0');
INSERT INTO `sys_menu` (`id`, `parent_id`, `title`, `name`, `perms`, `type`, `component`, `path`, `redirect`, `icon`, `keep_alive`, `order_num`, `create_time`, `update_time`, `create_by`, `update_by`, `hidden`) VALUES (100025, 100024, '登录日志', 'OperLogin', 'sys:oper:login', '0', 'modules/system/log/OperLogList.vue', '/sys/oper/login', NULL, 'logout', 1, 0, '2022-04-24 00:15:20', '2022-04-24 00:16:26', 'fxz', 'fxz', '0');
INSERT INTO `sys_menu` (`id`, `parent_id`, `title`, `name`, `perms`, `type`, `component`, `path`, `redirect`, `icon`, `keep_alive`, `order_num`, `create_time`, `update_time`, `create_by`, `update_by`, `hidden`) VALUES (100026, 100024, '操作日志', 'OperLogList', 'sys:oper:log', '0', 'modules/system/log/OperLogList.vue', '/sys/oper/log', NULL, 'stock', 1, 1, '2022-04-24 00:17:31', '2022-04-24 00:17:31', 'fxz', 'fxz', '0');
INSERT INTO `sys_menu` (`id`, `parent_id`, `title`, `name`, `perms`, `type`, `component`, `path`, `redirect`, `icon`, `keep_alive`, `order_num`, `create_time`, `update_time`, `create_by`, `update_by`, `hidden`) VALUES (100027, 0, '商品管理', 'product', 'product', '0', 'RouteView', '/product', NULL, 'gift', 1, 3, '2022-05-05 20:14:55', '2022-05-05 20:14:55', 'fxz', 'fxz', '0');
INSERT INTO `sys_menu` (`id`, `parent_id`, `title`, `name`, `perms`, `type`, `component`, `path`, `redirect`, `icon`, `keep_alive`, `order_num`, `create_time`, `update_time`, `create_by`, `update_by`, `hidden`) VALUES (100028, 100027, '品牌管理', 'brandList', 'product:brand', '0', 'modules/mall/product/brand/BrandList', '/product/brand', NULL, 'taobao', 1, 0, '2022-05-05 20:15:54', '2022-05-05 20:15:54', 'fxz', 'fxz', '0');
INSERT INTO `sys_menu` (`id`, `parent_id`, `title`, `name`, `perms`, `type`, `component`, `path`, `redirect`, `icon`, `keep_alive`, `order_num`, `create_time`, `update_time`, `create_by`, `update_by`, `hidden`) VALUES (100029, 100027, '分类管理', 'categoryList', 'product:category', '0', 'modules/mall/product/category/CategoryList', '/product/category', NULL, 'layout', 1, 1, '2022-05-05 20:16:57', '2022-05-05 20:16:57', 'fxz', 'fxz', '0');
INSERT INTO `sys_menu` (`id`, `parent_id`, `title`, `name`, `perms`, `type`, `component`, `path`, `redirect`, `icon`, `keep_alive`, `order_num`, `create_time`, `update_time`, `create_by`, `update_by`, `hidden`) VALUES (100030, 100027, '商品上架', 'goodInfo', 'product:good', '0', 'modules/mall/product/goods/Info', '/goods/info', NULL, 'shop', 0, -1, '2022-05-06 16:49:42', '2022-08-11 13:51:33', 'fxz', 'fxz', '0');
INSERT INTO `sys_menu` (`id`, `parent_id`, `title`, `name`, `perms`, `type`, `component`, `path`, `redirect`, `icon`, `keep_alive`, `order_num`, `create_time`, `update_time`, `create_by`, `update_by`, `hidden`) VALUES (100031, 100027, '商品列表', 'goodsList', 'product:goods', '0', 'modules/mall/product/goods/index.vue', '/product/goods', NULL, 'database', 1, -2, '2022-05-09 09:19:51', '2022-05-09 09:19:51', 'fxz', 'fxz', '0');
INSERT INTO `sys_menu` (`id`, `parent_id`, `title`, `name`, `perms`, `type`, `component`, `path`, `redirect`, `icon`, `keep_alive`, `order_num`, `create_time`, `update_time`, `create_by`, `update_by`, `hidden`) VALUES (100032, 100036, '订单列表', 'OrderList', 'order:list', '0', 'modules/mall/orders/order/OrderList', '/order/list', NULL, 'shopping-cart', 1, 4, '2022-05-18 17:54:51', '2022-08-11 21:08:53', 'fxz', 'fxz', '0');
INSERT INTO `sys_menu` (`id`, `parent_id`, `title`, `name`, `perms`, `type`, `component`, `path`, `redirect`, `icon`, `keep_alive`, `order_num`, `create_time`, `update_time`, `create_by`, `update_by`, `hidden`) VALUES (100033, 100035, '会员列表', 'MemberList', 'user:member', '0', 'modules/mall/user/member/MemberList', '/user/member', NULL, 'smile', 1, 5, '2022-05-18 20:25:28', '2022-08-11 21:02:26', 'fxz', 'fxz', '0');
INSERT INTO `sys_menu` (`id`, `parent_id`, `title`, `name`, `perms`, `type`, `component`, `path`, `redirect`, `icon`, `keep_alive`, `order_num`, `create_time`, `update_time`, `create_by`, `update_by`, `hidden`) VALUES (100034, 20, '令牌管理', 'tokenList', 'sys:token', '0', 'modules/system/token/index.vue', '/permissions/token', NULL, 'safety-certificate', 1, 5, '2022-06-26 21:18:21', '2022-06-26 21:18:21', 'fxz', 'fxz', '0');
INSERT INTO `sys_menu` (`id`, `parent_id`, `title`, `name`, `perms`, `type`, `component`, `path`, `redirect`, `icon`, `keep_alive`, `order_num`, `create_time`, `update_time`, `create_by`, `update_by`, `hidden`) VALUES (100035, 0, '会员管理', 'member', 'user', '0', 'RouteView', '/user', NULL, 'user', 1, 4, '2022-08-11 21:01:52', '2022-08-11 21:05:18', 'fxz', 'fxz', '0');
INSERT INTO `sys_menu` (`id`, `parent_id`, `title`, `name`, `perms`, `type`, `component`, `path`, `redirect`, `icon`, `keep_alive`, `order_num`, `create_time`, `update_time`, `create_by`, `update_by`, `hidden`) VALUES (100036, 0, '订单管理', 'order', 'order', '0', 'RouteView', '/order', NULL, 'shop', 1, 4, '2022-08-11 21:07:41', '2022-08-30 11:03:44', 'fxz', 'fxz', '0');
INSERT INTO `sys_menu` (`id`, `parent_id`, `title`, `name`, `perms`, `type`, `component`, `path`, `redirect`, `icon`, `keep_alive`, `order_num`, `create_time`, `update_time`, `create_by`, `update_by`, `hidden`) VALUES (100037, 0, '营销管理', 'promotion', 'promotion', '0', 'RouteView', '/promotion', NULL, 'shopping-cart', 1, 3, '2022-08-12 10:33:22', '2022-08-30 11:04:04', 'fxz', 'fxz', '0');
INSERT INTO `sys_menu` (`id`, `parent_id`, `title`, `name`, `perms`, `type`, `component`, `path`, `redirect`, `icon`, `keep_alive`, `order_num`, `create_time`, `update_time`, `create_by`, `update_by`, `hidden`) VALUES (100038, 100037, '秒杀活动', 'seckillList', 'promotion:seckill', '0', 'modules/mall/promotion/seckill/SeckillList', '/promotion/seckill', NULL, 'pay-circle', 1, 0, '2022-08-12 10:35:44', '2022-08-12 10:58:28', 'fxz', 'fxz', '0');
INSERT INTO `sys_menu` (`id`, `parent_id`, `title`, `name`, `perms`, `type`, `component`, `path`, `redirect`, `icon`, `keep_alive`, `order_num`, `create_time`, `update_time`, `create_by`, `update_by`, `hidden`) VALUES (100039, 100037, '秒杀商品管理', 'SeckillManage', 'seckill:manage', '0', 'modules/mall/promotion/seckill/SeckillManage', '/promotion/seckill/manage', NULL, 'border-inner', 1, 0, '2022-08-13 10:48:23', '2022-08-13 10:48:23', 'fxz', 'fxz', '1');
INSERT INTO `sys_menu` (`id`, `parent_id`, `title`, `name`, `perms`, `type`, `component`, `path`, `redirect`, `icon`, `keep_alive`, `order_num`, `create_time`, `update_time`, `create_by`, `update_by`, `hidden`) VALUES (100040, 20, '动态网关', 'routeIndex', 'sys:route', '0', 'modules/system/route/Index.vue', '/sys/route', NULL, 'bg-colors', 1, 10, '2022-08-20 17:12:24', '2022-09-10 20:33:21', 'fxz', 'fxz', '0');
INSERT INTO `sys_menu` (`id`, `parent_id`, `title`, `name`, `perms`, `type`, `component`, `path`, `redirect`, `icon`, `keep_alive`, `order_num`, `create_time`, `update_time`, `create_by`, `update_by`, `hidden`) VALUES (100041, 100037, '优惠券列表', 'couponList', 'promotion:coupon:list', '0', 'modules/mall/promotion/coupon/CouponList', '/promotion/coupon/list', NULL, 'sound', 1, 2, '2022-08-29 13:28:28', '2022-08-29 13:28:28', 'fxz', 'fxz', '0');
INSERT INTO `sys_menu` (`id`, `parent_id`, `title`, `name`, `perms`, `type`, `component`, `path`, `redirect`, `icon`, `keep_alive`, `order_num`, `create_time`, `update_time`, `create_by`, `update_by`, `hidden`) VALUES (100042, 100037, '优惠券活动', 'couponActivityList', 'promotion:coupon:activity', '0', 'modules/mall/promotion/coupon/CouponActivityList', '/promotion/coupon/activity', NULL, 'fire', 1, 3, '2022-08-29 18:21:54', '2022-08-29 18:21:54', 'fxz', 'fxz', '0');
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
  `access_token_validity` int(11) DEFAULT NULL COMMENT 'token 有效期',
  `refresh_token_validity` int(11) DEFAULT NULL COMMENT '刷新令牌有效期',
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
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
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
  `time` bigint(20) DEFAULT NULL COMMENT '执行时间',
  `create_by` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '创建人',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '更新人',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=1568802792915378179 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- ----------------------------
-- Records of sys_oper_log
-- ----------------------------
BEGIN;
INSERT INTO `sys_oper_log` (`id`, `title`, `business_type`, `method`, `request_method`, `oper_name`, `oper_url`, `oper_ip`, `oper_param`, `status`, `error_msg`, `time`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES (1564611148938813442, '用户登录', 4, NULL, 'POST', 'fxz', NULL, '127.0.0.1', 'fxz', 0, NULL, NULL, 'fxz', '2022-08-30 21:49:02', 'anonymousUser', '2022-08-30 21:49:01');
INSERT INTO `sys_oper_log` (`id`, `title`, `business_type`, `method`, `request_method`, `oper_name`, `oper_url`, `oper_ip`, `oper_param`, `status`, `error_msg`, `time`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES (1564638241521201153, '用户登录', 4, NULL, 'POST', 'fxz', NULL, '127.0.0.1', 'fxz', 0, NULL, NULL, 'fxz', '2022-08-30 23:36:41', 'anonymousUser', '2022-08-30 23:36:40');
INSERT INTO `sys_oper_log` (`id`, `title`, `business_type`, `method`, `request_method`, `oper_name`, `oper_url`, `oper_ip`, `oper_param`, `status`, `error_msg`, `time`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES (1564885577941975042, '用户登录', 4, NULL, 'POST', 'fxz', NULL, '127.0.0.1', 'fxz', 0, NULL, NULL, 'fxz', '2022-08-31 15:59:31', 'anonymousUser', '2022-08-31 15:59:30');
INSERT INTO `sys_oper_log` (`id`, `title`, `business_type`, `method`, `request_method`, `oper_name`, `oper_url`, `oper_ip`, `oper_param`, `status`, `error_msg`, `time`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES (1564952860079927298, '用户登录', 4, NULL, 'POST', 'fxz', NULL, '127.0.0.1', 'fxz', 0, NULL, NULL, 'fxz', '2022-08-31 20:26:52', 'anonymousUser', '2022-08-31 20:26:51');
INSERT INTO `sys_oper_log` (`id`, `title`, `business_type`, `method`, `request_method`, `oper_name`, `oper_url`, `oper_ip`, `oper_param`, `status`, `error_msg`, `time`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES (1564959943001092097, '用户登录', 4, NULL, 'POST', 'fxz', NULL, '127.0.0.1', 'fxz', 0, NULL, NULL, 'fxz', '2022-08-31 20:55:01', 'anonymousUser', '2022-08-31 20:55:00');
INSERT INTO `sys_oper_log` (`id`, `title`, `business_type`, `method`, `request_method`, `oper_name`, `oper_url`, `oper_ip`, `oper_param`, `status`, `error_msg`, `time`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES (1565157333943336961, '用户登录', 4, NULL, 'POST', 'fxz', NULL, '127.0.0.1', 'fxz', 0, NULL, NULL, 'fxz', '2022-09-01 09:59:22', 'anonymousUser', '2022-09-01 09:59:22');
INSERT INTO `sys_oper_log` (`id`, `title`, `business_type`, `method`, `request_method`, `oper_name`, `oper_url`, `oper_ip`, `oper_param`, `status`, `error_msg`, `time`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES (1565192757635252226, '用户登录', 4, NULL, 'POST', 'fxz', NULL, '127.0.0.1', 'fxz', 0, NULL, NULL, 'fxz', '2022-09-01 12:20:08', 'anonymousUser', '2022-09-01 12:20:07');
INSERT INTO `sys_oper_log` (`id`, `title`, `business_type`, `method`, `request_method`, `oper_name`, `oper_url`, `oper_ip`, `oper_param`, `status`, `error_msg`, `time`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES (1565192928460865538, '用户登录', 4, NULL, 'POST', 'fxz', NULL, '127.0.0.1', 'fxz', 0, NULL, NULL, 'fxz', '2022-09-01 12:20:49', 'anonymousUser', '2022-09-01 12:20:48');
INSERT INTO `sys_oper_log` (`id`, `title`, `business_type`, `method`, `request_method`, `oper_name`, `oper_url`, `oper_ip`, `oper_param`, `status`, `error_msg`, `time`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES (1565205885425061889, '用户登录', 4, NULL, 'POST', 'fxz', NULL, '127.0.0.1', 'fxz', 0, NULL, NULL, 'fxz', '2022-09-01 13:12:18', 'anonymousUser', '2022-09-01 13:12:17');
INSERT INTO `sys_oper_log` (`id`, `title`, `business_type`, `method`, `request_method`, `oper_name`, `oper_url`, `oper_ip`, `oper_param`, `status`, `error_msg`, `time`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES (1565214631203241985, '用户登录', 4, NULL, 'POST', 'fxz', NULL, '127.0.0.1', 'fxz', 0, NULL, NULL, 'fxz', '2022-09-01 13:47:03', 'anonymousUser', '2022-09-01 13:47:02');
INSERT INTO `sys_oper_log` (`id`, `title`, `business_type`, `method`, `request_method`, `oper_name`, `oper_url`, `oper_ip`, `oper_param`, `status`, `error_msg`, `time`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES (1565216237403561985, '用户登录', 4, NULL, 'POST', 'fxz', NULL, '127.0.0.1', 'fxz', 0, NULL, NULL, 'fxz', '2022-09-01 13:53:26', 'anonymousUser', '2022-09-01 13:53:25');
INSERT INTO `sys_oper_log` (`id`, `title`, `business_type`, `method`, `request_method`, `oper_name`, `oper_url`, `oper_ip`, `oper_param`, `status`, `error_msg`, `time`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES (1565216257985011713, '用户登录', 4, NULL, 'POST', 'fxz', NULL, '127.0.0.1', 'fxz', 0, NULL, NULL, 'fxz', '2022-09-01 13:53:31', 'anonymousUser', '2022-09-01 13:53:30');
INSERT INTO `sys_oper_log` (`id`, `title`, `business_type`, `method`, `request_method`, `oper_name`, `oper_url`, `oper_ip`, `oper_param`, `status`, `error_msg`, `time`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES (1565235770700951553, '用户登录', 4, NULL, 'POST', 'fxz', NULL, '127.0.0.1', 'fxz', 0, NULL, NULL, 'fxz', '2022-09-01 15:11:03', 'anonymousUser', '2022-09-01 15:11:02');
INSERT INTO `sys_oper_log` (`id`, `title`, `business_type`, `method`, `request_method`, `oper_name`, `oper_url`, `oper_ip`, `oper_param`, `status`, `error_msg`, `time`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES (1565260946931019777, '用户登录', 4, NULL, 'POST', 'fxz', NULL, '127.0.0.1', 'fxz', 0, NULL, NULL, 'fxz', '2022-09-01 16:51:06', 'anonymousUser', '2022-09-01 16:51:05');
INSERT INTO `sys_oper_log` (`id`, `title`, `business_type`, `method`, `request_method`, `oper_name`, `oper_url`, `oper_ip`, `oper_param`, `status`, `error_msg`, `time`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES (1565261027251941377, '用户登录', 4, NULL, 'POST', 'fxz', NULL, '127.0.0.1', 'fxz', 0, NULL, NULL, 'fxz', '2022-09-01 16:51:25', 'anonymousUser', '2022-09-01 16:51:24');
INSERT INTO `sys_oper_log` (`id`, `title`, `business_type`, `method`, `request_method`, `oper_name`, `oper_url`, `oper_ip`, `oper_param`, `status`, `error_msg`, `time`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES (1565261163533266945, '用户登录', 4, NULL, 'POST', 'fxz', NULL, '127.0.0.1', 'fxz', 0, NULL, NULL, 'fxz', '2022-09-01 16:51:57', 'anonymousUser', '2022-09-01 16:51:57');
INSERT INTO `sys_oper_log` (`id`, `title`, `business_type`, `method`, `request_method`, `oper_name`, `oper_url`, `oper_ip`, `oper_param`, `status`, `error_msg`, `time`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES (1565280552416538625, '用户登录', 4, NULL, 'POST', 'fxz', NULL, '127.0.0.1', 'fxz', 0, NULL, NULL, 'fxz', '2022-09-01 18:09:00', 'anonymousUser', '2022-09-01 18:08:59');
INSERT INTO `sys_oper_log` (`id`, `title`, `business_type`, `method`, `request_method`, `oper_name`, `oper_url`, `oper_ip`, `oper_param`, `status`, `error_msg`, `time`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES (1565322463567228929, '用户登录', 4, NULL, 'POST', 'fxz', NULL, '127.0.0.1', 'fxz', 0, NULL, NULL, 'fxz', '2022-09-01 20:55:32', 'anonymousUser', '2022-09-01 20:55:31');
INSERT INTO `sys_oper_log` (`id`, `title`, `business_type`, `method`, `request_method`, `oper_name`, `oper_url`, `oper_ip`, `oper_param`, `status`, `error_msg`, `time`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES (1565358192443863041, '用户登录', 4, NULL, 'POST', 'fxz', NULL, '127.0.0.1', 'fxz', 0, NULL, NULL, 'fxz', '2022-09-01 23:17:31', 'anonymousUser', '2022-09-01 23:17:30');
INSERT INTO `sys_oper_log` (`id`, `title`, `business_type`, `method`, `request_method`, `oper_name`, `oper_url`, `oper_ip`, `oper_param`, `status`, `error_msg`, `time`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES (1565358219929137154, '强退', 7, 'com.fxz.system.controller.TokenController.removeToken()', 'DELETE', 'fxz', '/token/e78278fc-18f6-4ad6-9f7b-8b194ecc2f39', '127.0.0.1', '', NULL, NULL, 201, 'fxz', '2022-09-01 23:17:37', 'fxz', '2022-09-01 23:17:37');
INSERT INTO `sys_oper_log` (`id`, `title`, `business_type`, `method`, `request_method`, `oper_name`, `oper_url`, `oper_ip`, `oper_param`, `status`, `error_msg`, `time`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES (1565358228003172353, '强退', 7, 'com.fxz.system.controller.TokenController.removeToken()', 'DELETE', 'fxz', '/token/a7d80b58-8a03-4332-b74f-2ee2e65f78a1', '127.0.0.1', '', NULL, NULL, 213, 'fxz', '2022-09-01 23:17:39', 'fxz', '2022-09-01 23:17:39');
INSERT INTO `sys_oper_log` (`id`, `title`, `business_type`, `method`, `request_method`, `oper_name`, `oper_url`, `oper_ip`, `oper_param`, `status`, `error_msg`, `time`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES (1565367240463560705, '用户登录', 4, NULL, 'POST', 'fxz', NULL, '127.0.0.1', 'fxz', 0, NULL, NULL, 'fxz', '2022-09-01 23:53:28', 'anonymousUser', '2022-09-01 23:53:27');
INSERT INTO `sys_oper_log` (`id`, `title`, `business_type`, `method`, `request_method`, `oper_name`, `oper_url`, `oper_ip`, `oper_param`, `status`, `error_msg`, `time`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES (1565985481257881601, '用户登录', 4, NULL, 'POST', 'fxz', NULL, '127.0.0.1', 'fxz', 0, NULL, NULL, 'fxz', '2022-09-03 16:50:08', 'anonymousUser', '2022-09-03 16:50:07');
INSERT INTO `sys_oper_log` (`id`, `title`, `business_type`, `method`, `request_method`, `oper_name`, `oper_url`, `oper_ip`, `oper_param`, `status`, `error_msg`, `time`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES (1568256706176798722, '用户登录', 4, NULL, 'POST', 'fxz', NULL, '127.0.0.1', 'fxz', 0, NULL, NULL, 'fxz', '2022-09-09 23:15:10', 'anonymousUser', '2022-09-09 23:15:10');
INSERT INTO `sys_oper_log` (`id`, `title`, `business_type`, `method`, `request_method`, `oper_name`, `oper_url`, `oper_ip`, `oper_param`, `status`, `error_msg`, `time`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES (1568376230410637314, '用户登录', 4, NULL, 'POST', 'fxz', NULL, '127.0.0.1', 'fxz', 0, NULL, NULL, 'fxz', '2022-09-10 07:10:07', 'anonymousUser', '2022-09-10 07:10:06');
INSERT INTO `sys_oper_log` (`id`, `title`, `business_type`, `method`, `request_method`, `oper_name`, `oper_url`, `oper_ip`, `oper_param`, `status`, `error_msg`, `time`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES (1568402760385875970, '用户登录', 4, NULL, 'POST', 'fxz', NULL, '127.0.0.1', 'fxz', 0, NULL, NULL, 'fxz', '2022-09-10 08:55:32', 'anonymousUser', '2022-09-10 08:55:32');
INSERT INTO `sys_oper_log` (`id`, `title`, `business_type`, `method`, `request_method`, `oper_name`, `oper_url`, `oper_ip`, `oper_param`, `status`, `error_msg`, `time`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES (1568421751187812353, '用户登录', 4, NULL, 'POST', 'fxz', NULL, '127.0.0.1', 'fxz', 0, NULL, NULL, 'fxz', '2022-09-10 10:11:00', 'anonymousUser', '2022-09-10 10:10:59');
INSERT INTO `sys_oper_log` (`id`, `title`, `business_type`, `method`, `request_method`, `oper_name`, `oper_url`, `oper_ip`, `oper_param`, `status`, `error_msg`, `time`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES (1568421802274435074, '用户登录', 4, NULL, 'POST', 'fxz', NULL, '127.0.0.1', 'fxz', 0, NULL, NULL, 'fxz', '2022-09-10 10:11:12', 'anonymousUser', '2022-09-10 10:11:12');
INSERT INTO `sys_oper_log` (`id`, `title`, `business_type`, `method`, `request_method`, `oper_name`, `oper_url`, `oper_ip`, `oper_param`, `status`, `error_msg`, `time`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES (1568422301035900930, '用户登录', 4, NULL, 'POST', 'fxz', NULL, '127.0.0.1', 'fxz', 0, NULL, NULL, 'fxz', '2022-09-10 10:13:11', 'anonymousUser', '2022-09-10 10:13:11');
INSERT INTO `sys_oper_log` (`id`, `title`, `business_type`, `method`, `request_method`, `oper_name`, `oper_url`, `oper_ip`, `oper_param`, `status`, `error_msg`, `time`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES (1568492319228551169, '用户登录', 4, NULL, 'POST', 'fxz', NULL, '127.0.0.1', 'fxz', 0, NULL, NULL, 'fxz', '2022-09-10 14:51:25', 'anonymousUser', '2022-09-10 14:51:24');
INSERT INTO `sys_oper_log` (`id`, `title`, `business_type`, `method`, `request_method`, `oper_name`, `oper_url`, `oper_ip`, `oper_param`, `status`, `error_msg`, `time`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES (1568494381983055874, '用户登录', 4, NULL, 'POST', 'fxz', NULL, '127.0.0.1', 'fxz', 0, NULL, NULL, 'fxz', '2022-09-10 14:59:37', 'anonymousUser', '2022-09-10 14:59:36');
INSERT INTO `sys_oper_log` (`id`, `title`, `business_type`, `method`, `request_method`, `oper_name`, `oper_url`, `oper_ip`, `oper_param`, `status`, `error_msg`, `time`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES (1568494596836278274, '用户登录', 4, NULL, 'POST', 'fxz', NULL, '127.0.0.1', 'fxz', 0, NULL, NULL, 'fxz', '2022-09-10 15:00:28', 'anonymousUser', '2022-09-10 15:00:27');
INSERT INTO `sys_oper_log` (`id`, `title`, `business_type`, `method`, `request_method`, `oper_name`, `oper_url`, `oper_ip`, `oper_param`, `status`, `error_msg`, `time`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES (1568497583474327554, '用户登录', 4, NULL, 'POST', 'fxz', NULL, '127.0.0.1', 'fxz', 0, NULL, NULL, 'fxz', '2022-09-10 15:12:20', 'anonymousUser', '2022-09-10 15:12:19');
INSERT INTO `sys_oper_log` (`id`, `title`, `business_type`, `method`, `request_method`, `oper_name`, `oper_url`, `oper_ip`, `oper_param`, `status`, `error_msg`, `time`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES (1568497663031885826, '用户登录', 4, NULL, 'POST', 'fxz', NULL, '127.0.0.1', 'fxz', 0, NULL, NULL, 'fxz', '2022-09-10 15:12:39', 'anonymousUser', '2022-09-10 15:12:38');
INSERT INTO `sys_oper_log` (`id`, `title`, `business_type`, `method`, `request_method`, `oper_name`, `oper_url`, `oper_ip`, `oper_param`, `status`, `error_msg`, `time`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES (1568505491956154370, '用户登录', 4, NULL, 'POST', 'fxz', NULL, '127.0.0.1', 'fxz', 0, NULL, NULL, 'fxz', '2022-09-10 15:43:45', 'anonymousUser', '2022-09-10 15:43:45');
INSERT INTO `sys_oper_log` (`id`, `title`, `business_type`, `method`, `request_method`, `oper_name`, `oper_url`, `oper_ip`, `oper_param`, `status`, `error_msg`, `time`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES (1568507061036892161, '用户登录', 4, NULL, 'POST', 'fxz', NULL, '127.0.0.1', 'fxz', 0, NULL, NULL, 'fxz', '2022-09-10 15:49:59', 'anonymousUser', '2022-09-10 15:49:59');
INSERT INTO `sys_oper_log` (`id`, `title`, `business_type`, `method`, `request_method`, `oper_name`, `oper_url`, `oper_ip`, `oper_param`, `status`, `error_msg`, `time`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES (1568507120713449474, '用户登录', 4, NULL, 'POST', 'fxz', NULL, '127.0.0.1', 'fxz', 0, NULL, NULL, 'fxz', '2022-09-10 15:50:14', 'anonymousUser', '2022-09-10 15:50:13');
INSERT INTO `sys_oper_log` (`id`, `title`, `business_type`, `method`, `request_method`, `oper_name`, `oper_url`, `oper_ip`, `oper_param`, `status`, `error_msg`, `time`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES (1568507260589293569, '用户登录', 4, NULL, 'POST', 'fxz', NULL, '127.0.0.1', 'fxz', 0, NULL, NULL, 'fxz', '2022-09-10 15:50:47', 'anonymousUser', '2022-09-10 15:50:47');
INSERT INTO `sys_oper_log` (`id`, `title`, `business_type`, `method`, `request_method`, `oper_name`, `oper_url`, `oper_ip`, `oper_param`, `status`, `error_msg`, `time`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES (1568508542435704833, '用户登录', 4, NULL, 'POST', 'fxz', NULL, '127.0.0.1', 'fxz', 0, NULL, NULL, 'fxz', '2022-09-10 15:55:53', 'anonymousUser', '2022-09-10 15:55:52');
INSERT INTO `sys_oper_log` (`id`, `title`, `business_type`, `method`, `request_method`, `oper_name`, `oper_url`, `oper_ip`, `oper_param`, `status`, `error_msg`, `time`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES (1568508634886553602, '用户登录', 4, NULL, 'POST', 'fxz', NULL, '127.0.0.1', 'fxz', 0, NULL, NULL, 'fxz', '2022-09-10 15:56:15', 'anonymousUser', '2022-09-10 15:56:14');
INSERT INTO `sys_oper_log` (`id`, `title`, `business_type`, `method`, `request_method`, `oper_name`, `oper_url`, `oper_ip`, `oper_param`, `status`, `error_msg`, `time`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES (1568573045656518658, '用户登录', 4, NULL, 'POST', 'fxz', NULL, '127.0.0.1', 'fxz', 0, NULL, NULL, 'fxz', '2022-09-10 20:12:11', 'anonymousUser', '2022-09-10 20:12:11');
INSERT INTO `sys_oper_log` (`id`, `title`, `business_type`, `method`, `request_method`, `oper_name`, `oper_url`, `oper_ip`, `oper_param`, `status`, `error_msg`, `time`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES (1568575006338449409, '文件管理', 1, 'com.fxz.system.controller.FileController.add()', 'POST', 'fxz', '/file/add', '127.0.0.1', '', NULL, NULL, 419, 'fxz', '2022-09-10 20:19:59', 'fxz', '2022-09-10 20:19:59');
INSERT INTO `sys_oper_log` (`id`, `title`, `business_type`, `method`, `request_method`, `oper_name`, `oper_url`, `oper_ip`, `oper_param`, `status`, `error_msg`, `time`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES (1568575694854422529, '文件管理', 1, 'com.fxz.system.controller.FileController.add()', 'POST', 'fxz', '/file/add', '127.0.0.1', '', NULL, NULL, 369, 'fxz', '2022-09-10 20:22:43', 'fxz', '2022-09-10 20:22:43');
INSERT INTO `sys_oper_log` (`id`, `title`, `business_type`, `method`, `request_method`, `oper_name`, `oper_url`, `oper_ip`, `oper_param`, `status`, `error_msg`, `time`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES (1568577689388580865, '字典管理', 1, 'com.fxz.system.controller.DictController.add()', 'POST', 'fxz', '/dict/add', '127.0.0.1', '', NULL, NULL, 183, 'fxz', '2022-09-10 20:30:39', 'fxz', '2022-09-10 20:30:39');
INSERT INTO `sys_oper_log` (`id`, `title`, `business_type`, `method`, `request_method`, `oper_name`, `oper_url`, `oper_ip`, `oper_param`, `status`, `error_msg`, `time`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES (1568790857826209794, '用户登录', 4, NULL, 'POST', 'fxz', NULL, '127.0.0.1', 'fxz', 0, NULL, NULL, 'fxz', '2022-09-11 10:37:42', 'anonymousUser', '2022-09-11 10:37:41');
INSERT INTO `sys_oper_log` (`id`, `title`, `business_type`, `method`, `request_method`, `oper_name`, `oper_url`, `oper_ip`, `oper_param`, `status`, `error_msg`, `time`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES (1568794378017476609, '用户登录', 4, NULL, 'POST', 'fxz', NULL, '127.0.0.1', 'fxz', 0, NULL, NULL, 'fxz', '2022-09-11 10:51:41', 'anonymousUser', '2022-09-11 10:51:41');
INSERT INTO `sys_oper_log` (`id`, `title`, `business_type`, `method`, `request_method`, `oper_name`, `oper_url`, `oper_ip`, `oper_param`, `status`, `error_msg`, `time`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES (1568800257311830018, '用户登录', 4, NULL, 'POST', 'fxz', NULL, '127.0.0.1', 'fxz', 0, NULL, NULL, 'fxz', '2022-09-11 11:15:03', 'anonymousUser', '2022-09-11 11:15:02');
INSERT INTO `sys_oper_log` (`id`, `title`, `business_type`, `method`, `request_method`, `oper_name`, `oper_url`, `oper_ip`, `oper_param`, `status`, `error_msg`, `time`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES (1568800434525368321, '用户登录', 4, NULL, 'POST', 'fxz', NULL, '127.0.0.1', 'fxz', 0, NULL, NULL, 'fxz', '2022-09-11 11:15:45', 'anonymousUser', '2022-09-11 11:15:45');
INSERT INTO `sys_oper_log` (`id`, `title`, `business_type`, `method`, `request_method`, `oper_name`, `oper_url`, `oper_ip`, `oper_param`, `status`, `error_msg`, `time`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES (1568800573038063618, '用户登录', 4, NULL, 'POST', 'fxz', NULL, '127.0.0.1', 'fxz', 0, NULL, NULL, 'fxz', '2022-09-11 11:16:18', 'anonymousUser', '2022-09-11 11:16:18');
INSERT INTO `sys_oper_log` (`id`, `title`, `business_type`, `method`, `request_method`, `oper_name`, `oper_url`, `oper_ip`, `oper_param`, `status`, `error_msg`, `time`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES (1568801536918503426, '用户登录', 4, NULL, 'POST', 'fxz', NULL, '127.0.0.1', 'fxz', 0, NULL, NULL, 'fxz', '2022-09-11 11:20:08', 'anonymousUser', '2022-09-11 11:20:07');
INSERT INTO `sys_oper_log` (`id`, `title`, `business_type`, `method`, `request_method`, `oper_name`, `oper_url`, `oper_ip`, `oper_param`, `status`, `error_msg`, `time`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES (1568802792915378178, '用户登录', 4, NULL, 'POST', 'fxz', NULL, '127.0.0.1', 'fxz', 0, NULL, NULL, 'fxz', '2022-09-11 11:25:07', 'anonymousUser', '2022-09-11 11:25:06');
COMMIT;

-- ----------------------------
-- Table structure for sys_post
-- ----------------------------
DROP TABLE IF EXISTS `sys_post`;
CREATE TABLE `sys_post` (
  `post_id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '岗位ID',
  `post_code` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '岗位编码',
  `post_name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '岗位名称',
  `post_sort` int(11) NOT NULL COMMENT '岗位排序',
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
  `role_id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '角色ID',
  `role_name` varchar(10) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '角色名称',
  `remark` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '角色描述',
  `create_time` datetime NOT NULL COMMENT '创建时间',
  `update_time` datetime DEFAULT NULL COMMENT '修改时间',
  `create_by` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '创建人',
  `update_by` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '更新人',
  `data_scope` tinyint(1) DEFAULT NULL COMMENT '数据权限范围',
  `data_scope_dept_ids` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '数据范围(指定部门数组)',
  PRIMARY KEY (`role_id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=9 DEFAULT CHARSET=utf8 COMMENT='角色表';

-- ----------------------------
-- Records of sys_role
-- ----------------------------
BEGIN;
INSERT INTO `sys_role` (`role_id`, `role_name`, `remark`, `create_time`, `update_time`, `create_by`, `update_by`, `data_scope`, `data_scope_dept_ids`) VALUES (1, 'admin', '管理员', '2022-02-28 16:23:11', '2022-08-29 18:22:39', NULL, 'fxz', 1, NULL);
INSERT INTO `sys_role` (`role_id`, `role_name`, `remark`, `create_time`, `update_time`, `create_by`, `update_by`, `data_scope`, `data_scope_dept_ids`) VALUES (8, '游客', '游客,没啥权限', '2022-02-28 19:45:16', '2022-04-05 18:03:00', NULL, 'fxz', 1, NULL);
COMMIT;

-- ----------------------------
-- Table structure for sys_role_menu
-- ----------------------------
DROP TABLE IF EXISTS `sys_role_menu`;
CREATE TABLE `sys_role_menu` (
  `ROLE_ID` bigint(20) NOT NULL,
  `MENU_ID` bigint(20) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='角色菜单关联表';

-- ----------------------------
-- Records of sys_role_menu
-- ----------------------------
BEGIN;
INSERT INTO `sys_role_menu` (`ROLE_ID`, `MENU_ID`) VALUES (8, 12);
INSERT INTO `sys_role_menu` (`ROLE_ID`, `MENU_ID`) VALUES (8, 4);
INSERT INTO `sys_role_menu` (`ROLE_ID`, `MENU_ID`) VALUES (8, 5);
INSERT INTO `sys_role_menu` (`ROLE_ID`, `MENU_ID`) VALUES (8, 6);
INSERT INTO `sys_role_menu` (`ROLE_ID`, `MENU_ID`) VALUES (8, 1);
INSERT INTO `sys_role_menu` (`ROLE_ID`, `MENU_ID`) VALUES (8, 2);
INSERT INTO `sys_role_menu` (`ROLE_ID`, `MENU_ID`) VALUES (8, 3);
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
COMMIT;

-- ----------------------------
-- Table structure for sys_route_conf
-- ----------------------------
DROP TABLE IF EXISTS `sys_route_conf`;
CREATE TABLE `sys_route_conf` (
  `id` bigint(20) NOT NULL COMMENT '主键',
  `name` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL,
  `route_id` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL,
  `predicates` json DEFAULT NULL COMMENT '断言',
  `filters` json DEFAULT NULL COMMENT '过滤器',
  `uri` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL,
  `sort_order` int(11) DEFAULT '0' COMMENT '排序',
  `metadata` json DEFAULT NULL COMMENT '路由元信息',
  `create_by` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT ' ' COMMENT '创建人',
  `update_by` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT ' ' COMMENT '修改人',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
  `del_flag` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT '0',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='路由配置表';

-- ----------------------------
-- Records of sys_route_conf
-- ----------------------------
BEGIN;
INSERT INTO `sys_route_conf` (`id`, `name`, `route_id`, `predicates`, `filters`, `uri`, `sort_order`, `metadata`, `create_by`, `update_by`, `create_time`, `update_time`, `del_flag`) VALUES (1568803192783544321, 'system模块', 'fxz-server-system', '[{\"args\": {\"_genkey_0\": \"/system/**\"}, \"name\": \"Path\"}]', '[{\"args\": {\"key-resolver\": \"#{@remoteAddrKeyResolver}\", \"redis-rate-limiter.burstCapacity\": \"100\", \"redis-rate-limiter.replenishRate\": \"100\"}, \"name\": \"RequestRateLimiter\"}]', 'lb://fxz-server-system', 0, NULL, 'fxz', 'fxz', '2022-09-11 11:26:43', '2022-09-11 11:26:43', '0');
INSERT INTO `sys_route_conf` (`id`, `name`, `route_id`, `predicates`, `filters`, `uri`, `sort_order`, `metadata`, `create_by`, `update_by`, `create_time`, `update_time`, `del_flag`) VALUES (1568803192796127233, 'auth模块', 'fxz-auth', '[{\"args\": {\"_genkey_0\": \"/auth/**\"}, \"name\": \"Path\"}]', NULL, 'lb://fxz-auth', 1, NULL, 'fxz', 'fxz', '2022-09-11 11:26:43', '2022-09-11 11:26:43', '0');
INSERT INTO `sys_route_conf` (`id`, `name`, `route_id`, `predicates`, `filters`, `uri`, `sort_order`, `metadata`, `create_by`, `update_by`, `create_time`, `update_time`, `del_flag`) VALUES (1568803192804515842, '代码生成器模块', 'fxz-generate', '[{\"args\": {\"_genkey_0\": \"/generate/**\"}, \"name\": \"Path\"}]', NULL, 'lb://fxz-generate', 2, NULL, 'fxz', 'fxz', '2022-09-11 11:26:43', '2022-09-11 11:26:43', '0');
INSERT INTO `sys_route_conf` (`id`, `name`, `route_id`, `predicates`, `filters`, `uri`, `sort_order`, `metadata`, `create_by`, `update_by`, `create_time`, `update_time`, `del_flag`) VALUES (1568803192808710146, '实验模块', 'fxz-z-demos', '[{\"args\": {\"_genkey_0\": \"/demos/**\"}, \"name\": \"Path\"}]', NULL, 'lb://fxz-demos', 3, NULL, 'fxz', 'fxz', '2022-09-11 11:26:43', '2022-09-11 11:26:43', '0');
INSERT INTO `sys_route_conf` (`id`, `name`, `route_id`, `predicates`, `filters`, `uri`, `sort_order`, `metadata`, `create_by`, `update_by`, `create_time`, `update_time`, `del_flag`) VALUES (1568803192808710147, '定时任务模块', 'fxz-job', '[{\"args\": {\"_genkey_0\": \"/schedule/**\"}, \"name\": \"Path\"}]', NULL, 'lb://fxz-job', 3, NULL, 'fxz', 'fxz', '2022-09-11 11:26:43', '2022-09-11 11:26:43', '0');
INSERT INTO `sys_route_conf` (`id`, `name`, `route_id`, `predicates`, `filters`, `uri`, `sort_order`, `metadata`, `create_by`, `update_by`, `create_time`, `update_time`, `del_flag`) VALUES (1568803192817098753, '会员模块', 'fxz-mall-user', '[{\"args\": {\"_genkey_0\": \"/user/**\"}, \"name\": \"Path\"}]', NULL, 'lb://fxz-mall-user', 100, NULL, 'fxz', 'fxz', '2022-09-11 11:26:43', '2022-09-11 11:26:43', '0');
INSERT INTO `sys_route_conf` (`id`, `name`, `route_id`, `predicates`, `filters`, `uri`, `sort_order`, `metadata`, `create_by`, `update_by`, `create_time`, `update_time`, `del_flag`) VALUES (1568803192821293057, '订单模块', 'fxz-mall-order', '[{\"args\": {\"_genkey_0\": \"/order/**\"}, \"name\": \"Path\"}]', NULL, 'lb://fxz-mall-order', 100, NULL, 'fxz', 'fxz', '2022-09-11 11:26:43', '2022-09-11 11:26:43', '0');
INSERT INTO `sys_route_conf` (`id`, `name`, `route_id`, `predicates`, `filters`, `uri`, `sort_order`, `metadata`, `create_by`, `update_by`, `create_time`, `update_time`, `del_flag`) VALUES (1568803192821293058, '促销模块', 'fxz-mall-promotion', '[{\"args\": {\"_genkey_0\": \"/promotion/**\"}, \"name\": \"Path\"}]', NULL, 'lb://fxz-mall-promotion', 100, NULL, 'fxz', 'fxz', '2022-09-11 11:26:43', '2022-09-11 11:26:43', '0');
INSERT INTO `sys_route_conf` (`id`, `name`, `route_id`, `predicates`, `filters`, `uri`, `sort_order`, `metadata`, `create_by`, `update_by`, `create_time`, `update_time`, `del_flag`) VALUES (1568803192825487362, '商品模块', 'fxz-mall-product', '[{\"args\": {\"_genkey_0\": \"/product/**\"}, \"name\": \"Path\"}]', NULL, 'lb://fxz-mall-product', 100, NULL, 'fxz', 'fxz', '2022-09-11 11:26:43', '2022-09-11 11:26:43', '0');
INSERT INTO `sys_route_conf` (`id`, `name`, `route_id`, `predicates`, `filters`, `uri`, `sort_order`, `metadata`, `create_by`, `update_by`, `create_time`, `update_time`, `del_flag`) VALUES (1568803192829681665, 'es检索模块', 'fxz-mall-search', '[{\"args\": {\"_genkey_0\": \"/search/**\"}, \"name\": \"Path\"}]', NULL, 'lb://fxz-mall-search', 100, NULL, 'fxz', 'fxz', '2022-09-11 11:26:43', '2022-09-11 11:26:43', '0');
COMMIT;

-- ----------------------------
-- Table structure for sys_user
-- ----------------------------
DROP TABLE IF EXISTS `sys_user`;
CREATE TABLE `sys_user` (
  `user_id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '用户ID',
  `username` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '用户名',
  `password` varchar(128) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '密码',
  `dept_id` bigint(20) DEFAULT NULL COMMENT '部门ID',
  `email` varchar(128) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '邮箱',
  `mobile` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '联系电话',
  `status` char(1) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT '1' COMMENT '状态 0锁定 1有效',
  `create_time` datetime NOT NULL COMMENT '创建时间',
  `update_time` datetime DEFAULT NULL COMMENT '修改时间',
  `last_login_time` datetime DEFAULT NULL COMMENT '最近访问时间',
  `ssex` char(1) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '性别 0男 1女 2保密',
  `avatar` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '头像',
  `description` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '描述',
  `create_by` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '创建人',
  `update_by` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '更新人',
  PRIMARY KEY (`user_id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8 COMMENT='用户表';

-- ----------------------------
-- Records of sys_user
-- ----------------------------
BEGIN;
INSERT INTO `sys_user` (`user_id`, `username`, `password`, `dept_id`, `email`, `mobile`, `status`, `create_time`, `update_time`, `last_login_time`, `ssex`, `avatar`, `description`, `create_by`, `update_by`) VALUES (1, 'fxz', '{bcrypt}$2a$10$yMqLTL9t9TeRGp5fT7vENuxZaruaN.a/VlOpRGF7jBnKPAim6Xey.', 0, '2235602974@qq.com', '19806082431', '1', '2022-04-05 20:39:22', '2022-08-11 13:51:51', '2022-04-05 15:57:00', '0', '/system/file/fxzcloud/8ae52e3fb92b4e8089c8aa03f65163ae.png', 'FxzCloud的作者', NULL, 'fxz');
COMMIT;

-- ----------------------------
-- Table structure for sys_user_post
-- ----------------------------
DROP TABLE IF EXISTS `sys_user_post`;
CREATE TABLE `sys_user_post` (
  `user_id` bigint(20) NOT NULL COMMENT '用户ID',
  `post_id` bigint(20) NOT NULL COMMENT '岗位ID',
  PRIMARY KEY (`user_id`,`post_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='用户与岗位关联表';

-- ----------------------------
-- Records of sys_user_post
-- ----------------------------
BEGIN;
INSERT INTO `sys_user_post` (`user_id`, `post_id`) VALUES (1, 1511326356242804737);
COMMIT;

-- ----------------------------
-- Table structure for sys_user_role
-- ----------------------------
DROP TABLE IF EXISTS `sys_user_role`;
CREATE TABLE `sys_user_role` (
  `USER_ID` bigint(20) NOT NULL COMMENT '用户ID',
  `ROLE_ID` bigint(20) NOT NULL COMMENT '角色ID'
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='用户角色关联表';

-- ----------------------------
-- Records of sys_user_role
-- ----------------------------
BEGIN;
INSERT INTO `sys_user_role` (`USER_ID`, `ROLE_ID`) VALUES (1, 1);
COMMIT;

-- ----------------------------
-- Table structure for undo_log
-- ----------------------------
DROP TABLE IF EXISTS `undo_log`;
CREATE TABLE `undo_log` (
  `branch_id` bigint(20) NOT NULL COMMENT 'branch transaction id',
  `xid` varchar(128) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT 'global transaction id',
  `context` varchar(128) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT 'undo_log context,such as serialization',
  `rollback_info` longblob NOT NULL COMMENT 'rollback info',
  `log_status` int(11) NOT NULL COMMENT '0:normal status,1:defense status',
  `log_created` datetime(6) NOT NULL COMMENT 'create datetime',
  `log_modified` datetime(6) NOT NULL COMMENT 'modify datetime',
  UNIQUE KEY `ux_undo_log` (`xid`,`branch_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='AT transaction mode undo table';

-- ----------------------------
-- Records of undo_log
-- ----------------------------
BEGIN;
COMMIT;

SET FOREIGN_KEY_CHECKS = 1;
