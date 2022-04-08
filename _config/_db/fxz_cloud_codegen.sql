/*
 Navicat Premium Data Transfer

 Source Server         : localhost3306
 Source Server Type    : MySQL
 Source Server Version : 80000
 Source Host           : localhost:3306
 Source Schema         : fxz_cloud_codegen

 Target Server Type    : MySQL
 Target Server Version : 80000
 File Encoding         : 65001

 Date: 05/04/2022 22:17:08
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for gen_datasource_conf
-- ----------------------------
DROP TABLE IF EXISTS `gen_datasource_conf`;
CREATE TABLE `gen_datasource_conf`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `name` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '数据源名称',
  `url` varchar(512) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT 'jdbc-url',
  `username` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '用户名',
  `password` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '密码',
  `del_flag` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '0' COMMENT '删除标记',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  `create_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '创建人',
  `update_time` datetime(0) NULL DEFAULT NULL COMMENT '修改时间',
  `update_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '更新人',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '数据源表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of gen_datasource_conf
-- ----------------------------
INSERT INTO `gen_datasource_conf` VALUES (1509435110175907841, 'fxzcloud_base', 'jdbc:mysql://127.0.0.1:3306/fxz_cloud_codegen?useUnicode=true&characterEncoding=UTF-8&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=GMT%2b8', 'root', 'rM9IOsQdK5IUMmCF4Y5d0A==', '1', '2022-03-31 15:39:09', 'fxz', '2022-03-31 15:39:17', 'fxz');
INSERT INTO `gen_datasource_conf` VALUES (1509439122488958977, 'fxzcloud_base', 'jdbc:mysql://127.0.0.1/fxz_cloud_base?useUnicode=true&characterEncoding=UTF-8&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=GMT%2b8', 'root', 'nQKqaW9qNnnEsWgmXM0Lag==', '0', '2022-03-31 15:55:05', 'fxz', '2022-04-02 16:04:44', 'fxz');
INSERT INTO `gen_datasource_conf` VALUES (1510166239929495553, 'fxzcloud_gen', 'jdbc:mysql://127.0.0.1/fxz_cloud_codegen?useUnicode=true&characterEncoding=UTF-8&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=GMT%2b8', 'root', 'E46KJNsvjWdoc0V5nt2jGg==', '1', '2022-04-02 16:04:24', 'fxz', '2022-04-02 16:04:24', 'fxz');
INSERT INTO `gen_datasource_conf` VALUES (1510497293475336193, 'pig_base', 'jdbc:mysql://127.0.0.1/pig?useUnicode=true&characterEncoding=UTF-8&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=GMT%2b8', 'root', '0+Mu9YWFYj0QlJHXVSzscQ==', '0', '2022-04-03 13:59:53', 'fxz', '2022-04-03 13:59:53', 'fxz');
INSERT INTO `gen_datasource_conf` VALUES (1510886293184806913, 'fxzcloud_job', 'jdbc:mysql://127.0.0.1/fxz_cloud_job?useUnicode=true&characterEncoding=UTF-8&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=GMT%2b8', 'root', 'KNIsOUmb6f9fQnThKdC3Sw==', '0', '2022-03-31 15:55:05', 'fxz', '2022-04-02 16:04:44', 'fxz');

SET FOREIGN_KEY_CHECKS = 1;