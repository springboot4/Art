/*
 Navicat Premium Data Transfer

 Source Server         : 61.153.186.23
 Source Server Type    : MySQL
 Source Server Version : 80031
 Source Host           : 61.153.186.23:3306
 Source Schema         : art_nacos

 Target Server Type    : MySQL
 Target Server Version : 80031
 File Encoding         : 65001

 Date: 06/05/2023 15:04:41
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for config_info
-- ----------------------------
DROP TABLE IF EXISTS `config_info`;
CREATE TABLE `config_info` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT 'id',
  `data_id` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_bin NOT NULL COMMENT 'data_id',
  `group_id` varchar(128) CHARACTER SET utf8mb3 COLLATE utf8mb3_bin DEFAULT NULL,
  `content` longtext CHARACTER SET utf8mb3 COLLATE utf8mb3_bin NOT NULL COMMENT 'content',
  `md5` varchar(32) CHARACTER SET utf8mb3 COLLATE utf8mb3_bin DEFAULT NULL COMMENT 'md5',
  `gmt_create` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `gmt_modified` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '修改时间',
  `src_user` text CHARACTER SET utf8mb3 COLLATE utf8mb3_bin COMMENT 'source user',
  `src_ip` varchar(50) CHARACTER SET utf8mb3 COLLATE utf8mb3_bin DEFAULT NULL COMMENT 'source ip',
  `app_name` varchar(128) CHARACTER SET utf8mb3 COLLATE utf8mb3_bin DEFAULT NULL,
  `tenant_id` varchar(128) CHARACTER SET utf8mb3 COLLATE utf8mb3_bin DEFAULT '' COMMENT '租户字段',
  `c_desc` varchar(256) CHARACTER SET utf8mb3 COLLATE utf8mb3_bin DEFAULT NULL,
  `c_use` varchar(64) CHARACTER SET utf8mb3 COLLATE utf8mb3_bin DEFAULT NULL,
  `effect` varchar(64) CHARACTER SET utf8mb3 COLLATE utf8mb3_bin DEFAULT NULL,
  `type` varchar(64) CHARACTER SET utf8mb3 COLLATE utf8mb3_bin DEFAULT NULL,
  `c_schema` text CHARACTER SET utf8mb3 COLLATE utf8mb3_bin,
  `encrypted_data_key` text CHARACTER SET utf8mb3 COLLATE utf8mb3_bin COMMENT '秘钥',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE KEY `uk_configinfo_datagrouptenant` (`data_id`,`group_id`,`tenant_id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=281 DEFAULT CHARSET=utf8mb3 COLLATE=utf8mb3_bin COMMENT='config_info';

-- ----------------------------
-- Records of config_info
-- ----------------------------
BEGIN;
INSERT INTO `config_info` (`id`, `data_id`, `group_id`, `content`, `md5`, `gmt_create`, `gmt_modified`, `src_user`, `src_ip`, `app_name`, `tenant_id`, `c_desc`, `c_use`, `effect`, `type`, `c_schema`, `encrypted_data_key`) VALUES (1, 'art-auth.yaml', 'DEFAULT_GROUP', 'server:\n  port: 3000\n\nwechat:\n  weapp:\n    appid: ENC(SxZ78YMbeLIHg1/IAmPxkVFjbxHUDPSJDsrVJMmc/ws=)\n    secret: ENC(OcF3VEu+7nyetyF7S5pTvidOfNRXT1KLsuo6h09aYUD74Hz0buJo8ONkg/Z2AiNj)\n\nspring:\n  datasource:\n    driver-class-name: com.mysql.cj.jdbc.Driver\n    username: root\n    password: art-mysql-password\n    url: jdbc:mysql://art-mysql/art_base?useUnicode=true&characterEncoding=UTF-8&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=GMT%2b8&rewriteBatchedStatements=true\n  \nmybatis-plus:\n  type-aliases-package: com.fxz.common.entity.system\n  mapper-locations: classpath:mapper/*.xml\n  configuration:\n    jdbc-type-for-null: null\n  global-config:\n    banner: false\n\ninfo:\n  app:\n    name: ${spring.application.name}\n    description: \"@project.description@\"\n    version: \"@project.version@\"\n\nfxz:\n  cloud:\n    security:\n      anonUris: /token/**,/**/swagger-ui/index.html', 'f434634255c545dd1fe826a0b4bbb6be', '2021-12-16 18:39:32', '2023-05-06 15:01:44', 'nacos', '222.64.106.138', '', '', 'null', 'null', 'null', 'yaml', 'null', '');
INSERT INTO `config_info` (`id`, `data_id`, `group_id`, `content`, `md5`, `gmt_create`, `gmt_modified`, `src_user`, `src_ip`, `app_name`, `tenant_id`, `c_desc`, `c_use`, `effect`, `type`, `c_schema`, `encrypted_data_key`) VALUES (4, 'art-gateway.yaml', 'DEFAULT_GROUP', 'server:\n  port: 9999\nspring:\n  cloud:\n    gateway:\n      routes:\n      default-filters:\n        - StripPrefix=1\n\naj:\n  captcha:\n    cache-type: redis\n ', '6110a8f649396d33a3eb3ddb2caa6fd9', '2021-12-16 18:52:35', '2023-04-12 11:29:52', 'nacos', '222.64.106.138', '', '', 'null', 'null', 'null', 'yaml', 'null', '');
INSERT INTO `config_info` (`id`, `data_id`, `group_id`, `content`, `md5`, `gmt_create`, `gmt_modified`, `src_user`, `src_ip`, `app_name`, `tenant_id`, `c_desc`, `c_use`, `effect`, `type`, `c_schema`, `encrypted_data_key`) VALUES (6, 'art-server-system.yaml', 'DEFAULT_GROUP', 'server:\n  port: 4000\n\nspring:\n  datasource:\n    driver-class-name: com.mysql.cj.jdbc.Driver\n    username: root\n    password: art-mysql-password\n    url: jdbc:mysql://art-mysql/art_base?useUnicode=true&characterEncoding=UTF-8&rewriteBatchedStatements=true&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=GMT%2b8\n    type: com.zaxxer.hikari.HikariDataSource\nmybatis-plus:\n  type-aliases-package: com.fxz.common.core.entity.system\n  mapper-locations: classpath:mapper/*/*.xml\n  configuration:\n    jdbc-type-for-null: null\n  global-config:\n    banner: false\n    \nfxz:\n  cloud:\n    security: \n      anonUris: /find,/csrf,/actuator/*,/user/findByName/*,/menu/findUserPermissions/*,/doc.html\n  common:\n    sequence:\n      type: redis\n\nredis:\n  cache:\n    multi:\n      enabled: true\n\n# 文件系统\nfile:\n  storage:\n    oss:\n      endpoint: http://art-oss:9000\n      access-key: admin\n      secret-key: admin123456\n      bucketName: art\n\naliyun:\n  sms:\n    accessKeyId: LTAI5tJymbcizqk5nnVvQUU6\n    accessKeySecret: 9iszXlrnjoXuLLWIXFiaFCzWg2ttW2\n    domain: dysmsapi.aliyuncs.com\n    regionId: cn-hangzhou\n    templateCode: SMS_154950909\n    signName: 阿里云短信测试\n', '8edac9142168df6de53f505d858a84d6', '2021-12-17 19:01:56', '2023-05-06 15:02:05', 'nacos', '222.64.106.138', '', '', 'null', 'null', 'null', 'yaml', 'null', '');
INSERT INTO `config_info` (`id`, `data_id`, `group_id`, `content`, `md5`, `gmt_create`, `gmt_modified`, `src_user`, `src_ip`, `app_name`, `tenant_id`, `c_desc`, `c_use`, `effect`, `type`, `c_schema`, `encrypted_data_key`) VALUES (70, 'art-generate.yaml', 'DEFAULT_GROUP', 'server:\n  port: 5001\n\nspring:\n  datasource:\n    driver-class-name: com.mysql.cj.jdbc.Driver\n    username: root\n    password: art-mysql-password\n    url: jdbc:mysql://art-mysql/art_codegen?useUnicode=true&characterEncoding=UTF-8&rewriteBatchedStatements=true&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=GMT%2b8\n\n\nmybatis-plus:\n  mapper-locations: classpath:mapper/*/*.xml\n  configuration:\n    jdbc-type-for-null: null\n  global-config:\n    banner: false', '3f8cfd3ade32f082811dc8c998f0cca3', '2022-03-31 11:02:50', '2023-04-13 08:49:53', 'nacos', '222.64.106.138', '', '', 'null', 'null', 'null', 'yaml', 'null', '');
INSERT INTO `config_info` (`id`, `data_id`, `group_id`, `content`, `md5`, `gmt_create`, `gmt_modified`, `src_user`, `src_ip`, `app_name`, `tenant_id`, `c_desc`, `c_use`, `effect`, `type`, `c_schema`, `encrypted_data_key`) VALUES (80, 'art-job.yaml', 'DEFAULT_GROUP', 'server:\n  port: 5002\n\n\nspring:\n  datasource:\n    driver-class-name: com.mysql.cj.jdbc.Driver\n    username: root\n    password: art-mysql-password\n    url: jdbc:mysql://art-mysql/art_job?useUnicode=true&characterEncoding=UTF-8&rewriteBatchedStatements=true&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=GMT%2b8\n  quartz:\n    auto-startup: true # quartz启动开关\n    scheduler-name: schedulerName # 默认的schedulerName\n    job-store-type: jdbc # jdbc存储 默认内存\n    overwrite-existing-jobs: true # 启动时更新已经存在的job\n    wait-for-jobs-to-complete-on-shutdown: true # 应用关闭时 是否等待定时任务执行完成 默认为 false\n    properties:\n      org:\n        quartz:\n          scheduler:\n            instanceName: schedulerName # 执行器名称\n            instanceId: AUTO # 自动生成id\n          jobStore:\n            class: org.springframework.scheduling.quartz.LocalDataSourceJobStore\n            isClustered: true # 集群模式\n            clusterCheckinInterval: 15000 # 集群检查频率 单位：毫秒\n            misfireThreshold: 60000 # misfire 阀值 单位：毫秒\n          threadPool:\n            threadCount: 25 # 线程池大小\n            threadPriority: 5 # 线程优先级\n            threadsInheritContextClassLoaderOfInitializingThread: true\n            class: org.quartz.simpl.SimpleThreadPool # 线程池类型\n    jdbc:\n      initialize-schema: never #  是否自动初始化Quartz表 我们手动创建表\n\nmybatis-plus:\n  mapper-locations: classpath:mapper/*/*.xml\n  configuration:\n    jdbc-type-for-null: null\n  global-config:\n    banner: false', '52d8722c8776c297321e757cf95eae9c', '2022-04-03 17:45:50', '2023-05-06 15:02:23', 'nacos', '222.64.106.138', '', '', 'null', 'null', 'null', 'yaml', 'null', '');
INSERT INTO `config_info` (`id`, `data_id`, `group_id`, `content`, `md5`, `gmt_create`, `gmt_modified`, `src_user`, `src_ip`, `app_name`, `tenant_id`, `c_desc`, `c_use`, `effect`, `type`, `c_schema`, `encrypted_data_key`) VALUES (135, 'art-common.yaml', 'DEFAULT_GROUP', 'art:\n  name: art\n  author: fxz\n  version: 0.0.1\n\nbiz:\n  oss:\n    resources-url: http://localhost:8301\n    \nmybatis-plus:\n  configuration:\n    log-impl: org.apache.ibatis.logging.stdout.StdOutImpl\n\nspring:\n  redis:\n    database: 0\n    host: art-redis\n    port: 6379\n    password: art-redis-password\n    lettuce:\n      pool:\n        # 连接池中的最小空闲连接\n        min-idle: 8\n        # 连接池中的最大空闲连接\n        max-idle: 500\n        # 连接池最大连接数（使用负值表示没有限制）\n        max-active: 2000\n        # 连接池最大阻塞等待时间（使用负值表示没有限制）\n        max-wait: -1\n    # 连接超时时长（毫秒）\n    timeout: 10000 \n  data:\n    redis:\n      database: 0\n      host: art-redis\n      port: 6379\n      password: art-redis-password\n      lettuce:\n        pool:\n          # 连接池中的最小空闲连接\n          min-idle: 8\n          # 连接池中的最大空闲连接\n          max-idle: 500\n          # 连接池最大连接数（使用负值表示没有限制）\n          max-active: 2000\n          # 连接池最大阻塞等待时间（使用负值表示没有限制）\n          max-wait: -1\n      # 连接超时时长（毫秒）\n      timeout: 10000 \n\njasypt:\n  encryptor:\n    password: fxzcloud #配置文件加密密钥\n\nfxz:\n  cloud:\n    security: \n      anonUris:  /v3/api-docs,/operLog/add,/token/myLogout,/doc.html\n  tenant:\n    ignore-urls:\n      - /gitee/**\n      - /client/findById\n      - /tenant/findIdByName    # 根据租户名称获取租户Id\n      - /user/findByName/*           # 登录时根据用户名查询用户 不做拦截\n      - /user/findByMobile/*         # 登录时根据手机号查询用户 不做拦截\n      - /menu/findUserPermissions/*   # 登录时根据查询角色权限   不做拦截\n      - /tenant/validTenant/*       # 校验租户信息  不做拦截\n      - /role/getDataPermission     # 查询数据权限信息 不做拦截\n      - /operLog/add                # 保存登录日志 不做拦截\n      - /sms/sendSms                # 短信登录 不做拦截\n      - /file/download/**           # 文件下载\n      - /token/*\n      - /swagger-ui/**\n      - /v3/api-docs/**\n      - /doc.html\n      - /webjars/**\n      - /app/**\n      - /demo/**\n    tables: \n      - sys_user\n      - sys_role\n  common:\n    doc:\n      author: \"fxz\"\n      version: \"0.0.1\"\n      title: \"Art开发文档\"\n      description: \"Art开发文档\"\n      services:\n        \"fxz-server-system\": system\n        \"fxz-mall-user\": user', 'ed6c550533d6108aaf1163c133ec7046', '2022-06-26 12:10:13', '2023-05-01 21:13:26', 'nacos', '222.64.106.138', '', '', '', '', '', 'yaml', '', '');
INSERT INTO `config_info` (`id`, `data_id`, `group_id`, `content`, `md5`, `gmt_create`, `gmt_modified`, `src_user`, `src_ip`, `app_name`, `tenant_id`, `c_desc`, `c_use`, `effect`, `type`, `c_schema`, `encrypted_data_key`) VALUES (189, 'art-demo.yaml', 'DEFAULT_GROUP', 'server:\n  port: 5003\n\nspring:\n  datasource:\n    driver-class-name: com.mysql.cj.jdbc.Driver\n    username: root\n    password: ENC(bM81d5utQm25BU/ZnKFOL5Otj1+/GyCaF9+rnC9YUxA=)\n    url: jdbc:mysql://art-mysql/art_base?useUnicode=true&characterEncoding=UTF-8&rewriteBatchedStatements=true&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=GMT%2b8\n  data:\n    mongodb:\n      authentication-database: admin\n      database: user\n      username: admin\n      password: \"123456\"\n      host: fxz-mysql\n      port: 27017\n  rabbitmq:\n    host: fxz-rabbitmq\n    port: 5672\n    username: fxz\n    password: 123456\n\n\nsecurity:\n  oauth2:\n    resource:\n      id: ${spring.application.name}\n      #token-info-uri:  http://${fxz-gateway}:8301/auth/oauth/check_token\n      user-info-uri: http://fxz-gateway:8301/auth/user\n\n\nmanagement:\n  endpoints:\n    web:\n      exposure:\n        include: \'*\'\n  endpoint:\n    health:\n      show-details: ALWAYS\n\njasypt:\n  encryptor:\n    password: fxzcloud\n\n# 文件系统\nfile:\n  storage:\n    oss:\n      endpoint: http://fxz-oss:9000\n      access-key: admin\n      secret-key: admin123456\n      bucketName: fxzcloud', 'd9db17307d733ed1cb0120a27b1ddac5', '2022-08-30 14:20:58', '2023-04-12 14:42:32', 'nacos', '222.64.106.138', '', '', '', '', '', 'yaml', '', '');
INSERT INTO `config_info` (`id`, `data_id`, `group_id`, `content`, `md5`, `gmt_create`, `gmt_modified`, `src_user`, `src_ip`, `app_name`, `tenant_id`, `c_desc`, `c_use`, `effect`, `type`, `c_schema`, `encrypted_data_key`) VALUES (230, 'art-edu.yaml', 'DEFAULT_GROUP', 'server:\n  port: 5000\n\nspring:\n  datasource:\n    driver-class-name: com.mysql.cj.jdbc.Driver\n    username: root\n    password: art-mysql-password\n    url: jdbc:mysql://art-mysql/art-edu?useUnicode=true&characterEncoding=UTF-8&rewriteBatchedStatements=true&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=GMT%2b8\n\nsecurity:\n  oauth2:\n    resource:\n      id: ${spring.application.name}\n      #token-info-uri:  http://${fxz-gateway}:9999/auth/oauth/check_token\n      user-info-uri: http://fxz-gateway:9999/auth/user\n\nmybatis-plus:\n  type-aliases-package: com.fxz.common.core.entity.system\n  mapper-locations: classpath:mapper/*/*.xml\n  configuration:\n    jdbc-type-for-null: null\n  global-config:\n    banner: false\n', '5e4264b3d074b0300e482291a452abd2', '2023-04-06 17:13:09', '2023-04-18 15:05:42', 'nacos', '222.64.106.138', '', 'edu', '', '', '', 'yaml', '', '');
INSERT INTO `config_info` (`id`, `data_id`, `group_id`, `content`, `md5`, `gmt_create`, `gmt_modified`, `src_user`, `src_ip`, `app_name`, `tenant_id`, `c_desc`, `c_use`, `effect`, `type`, `c_schema`, `encrypted_data_key`) VALUES (231, 'art-common.yaml', 'DEFAULT_GROUP', 'art:\n  name: art\n  author: fxz\n  version: 0.0.1\n\nbiz:\n  oss:\n    resources-url: http://localhost:8301\n    \nmybatis-plus:\n  configuration:\n    log-impl: org.apache.ibatis.logging.stdout.StdOutImpl\n\nspring:\n  redis:\n    database: 0\n    host: fxz-redis\n    port: 6379\n    password: art-redis-password\n    lettuce:\n      pool:\n        # 连接池中的最小空闲连接\n        min-idle: 8\n        # 连接池中的最大空闲连接\n        max-idle: 500\n        # 连接池最大连接数（使用负值表示没有限制）\n        max-active: 2000\n        # 连接池最大阻塞等待时间（使用负值表示没有限制）\n        max-wait: -1\n    # 连接超时时长（毫秒）\n    timeout: 10000 \n\njasypt:\n  encryptor:\n    password: fxzcloud #配置文件加密密钥\n\nfxz:\n  cloud:\n    security: \n      anonUris: /swagger-ui/**,/webjars/**,/swagger-resources/**,/csrf,/actuator/**,/swagger**/**,/**/api-docs/**,/doc.html\n  tenant:\n    ignore-urls:\n      - /user            # check-token获取用户信息 不做拦截\n      - /oauth/token     # 登录接口 不做拦截\n      - /tenant/findIdByName    # 根据租户名称获取租户Id\n      - /user/findByName/*           # 登录时根据用户名查询用户 不做拦截\n      - /user/findByMobile/*         # 登录时根据手机号查询用户 不做拦截\n      - /menu/findUserPermissions/*   # 登录时根据查询角色权限   不做拦截\n      - /tenant/validTenant/*       # 校验租户信息  不做拦截\n      - /role/getDataPermission     # 查询数据权限信息 不做拦截\n      - /operLog/add                # 保存登录日志 不做拦截\n      - /sms/sendSms                # 短信登录 不做拦截\n      - /token/*\n      - /swagger-ui/**\n      - /v3/api-docs/**\n      - /doc.html\n      - /webjars/**\n      - /app/**\n      - /demo/**\n    tables: \n      - sys_user\n      - sys_role\n  common:\n    doc:\n      author: \"fxz\"\n      version: \"0.0.1\"\n      title: \"Art开发文档\"\n      description: \"Art开发文档\"\n      services:\n        \"fxz-server-system\": system\n        \"fxz-mall-user\": user', '4319935d23b2748a0991d25271b45c08', '2023-04-06 17:13:34', '2023-04-18 15:05:20', 'nacos', '222.64.106.138', '', 'edu', '', '', '', 'yaml', '', '');
INSERT INTO `config_info` (`id`, `data_id`, `group_id`, `content`, `md5`, `gmt_create`, `gmt_modified`, `src_user`, `src_ip`, `app_name`, `tenant_id`, `c_desc`, `c_use`, `effect`, `type`, `c_schema`, `encrypted_data_key`) VALUES (232, 'art-auth.yaml', 'DEFAULT_GROUP', 'server:\n  port: 3000\n\nwechat:\n  weapp:\n    appid: ENC(SxZ78YMbeLIHg1/IAmPxkVFjbxHUDPSJDsrVJMmc/ws=)\n    secret: ENC(OcF3VEu+7nyetyF7S5pTvidOfNRXT1KLsuo6h09aYUD74Hz0buJo8ONkg/Z2AiNj)\n\nspring:\n  datasource:\n    driver-class-name: com.mysql.cj.jdbc.Driver\n    username: root\n    password: ENC(bM81d5utQm25BU/ZnKFOL5Otj1+/GyCaF9+rnC9YUxA=)\n    url: jdbc:mysql://art-mysql/art-edu?useUnicode=true&characterEncoding=UTF-8&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=GMT%2b8&rewriteBatchedStatements=true\n  \nmybatis-plus:\n  type-aliases-package: com.fxz.common.entity.system\n  mapper-locations: classpath:mapper/*.xml\n  configuration:\n    jdbc-type-for-null: null\n  global-config:\n    banner: false\n\ninfo:\n  app:\n    name: ${spring.application.name}\n    description: \"@project.description@\"\n    version: \"@project.version@\"\n\nmanagement:\n  endpoints:\n    web:\n      exposure:\n        include: \'*\'\n  endpoint:\n    health:\n      show-details: ALWAYS\n\nsecurity:\n  oauth2:\n    resource:\n      id: ${spring.application.name}\n      #token-info-uri:  http://${fxz-gateway}:8301/auth/oauth/check_token\n      user-info-uri: http://fxz-gateway:9999/auth/user\n\nfxz:\n  cloud:\n    security:\n      anonUris: /token/**,/**/swagger-ui/index.html', 'd824cc779e4e019f64f373f7aa04a9a4', '2023-04-06 17:14:32', '2023-04-18 15:05:00', 'nacos', '222.64.106.138', '', 'edu', '', '', '', 'yaml', '', '');
INSERT INTO `config_info` (`id`, `data_id`, `group_id`, `content`, `md5`, `gmt_create`, `gmt_modified`, `src_user`, `src_ip`, `app_name`, `tenant_id`, `c_desc`, `c_use`, `effect`, `type`, `c_schema`, `encrypted_data_key`) VALUES (234, 'art-serve-system.yaml', 'DEFAULT_GROUP', 'server:\n  port: 4000\n\nspring:\n  datasource:\n    driver-class-name: com.mysql.cj.jdbc.Driver\n    username: root\n    password: art-mysql-password\n    url: jdbc:mysql://art-mysql/art-edu?useUnicode=true&characterEncoding=UTF-8&rewriteBatchedStatements=true&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=GMT%2b8\n \n   # RabbitMQ 配置\n  rabbitmq:\n    host: fxz-rabbitmq\n    port: 5672\n    username: fxz\n    password: 123456\n    # 动态创建和绑定队列、交换机的配置\n    modules: \n      # 订单延时队列，到了过期的时间会被转发到订单取消队列\n      - routing-key: canal_key\n        queue:\n          name: canal_queue\n        exchange:\n          name: canal_exchange\n  autoconfigure:\n    exclude: org.springframework.cloud.sleuth.instrument.hystrix.SleuthHystrixAutoConfiguration\n\nsecurity:\n  oauth2:\n    resource:\n      id: ${spring.application.name}\n      #token-info-uri:  http://${fxz-gateway}:9999/auth/oauth/check_token\n      user-info-uri: http://fxz-gateway:9999/auth/user\n\nmybatis-plus:\n  type-aliases-package: com.fxz.common.core.entity.system\n  mapper-locations: classpath:mapper/*/*.xml\n  configuration:\n    jdbc-type-for-null: null\n  global-config:\n    banner: false\n\ninfo:\n  app:\n    name: ${spring.application.name}\n    description: \"@project.description@\"\n    version: \"@project.version@\"\n\nmanagement:\n  endpoints:\n    web:\n      exposure:\n        include: \'*\'\n  endpoint:\n    health:\n      show-details: ALWAYS\n\nfeign:\n  hystrix:\n    enabled: true\n\nhystrix:\n  shareSecurityContext: true\n\nfxz:\n  cloud:\n    security: \n      anonUris: /find,/swagger-ui/**,/webjars/**,/swagger-resources/**,/v2/api-docs/**,/,/csrf,/actuator/**,/user/findByName/*,/menu/findUserPermissions/**,/user/test,/kk-anti-reptile/**,/swagger**/**,/**/api-docs/**,/doc.html\n  common:\n    sequence:\n      type: redis\n      \n# 文件系统\nfile:\n  storage:\n    oss:\n      endpoint: http://fxz-oss:9000\n      access-key: admin\n      secret-key: admin123456\n      bucketName: fxzcloud\n\naliyun:\n  sms:\n    accessKeyId: LTAI5tJymbcizqk5nnVvQUU6\n    accessKeySecret: 9iszXlrnjoXuLLWIXFiaFCzWg2ttW2\n    domain: dysmsapi.aliyuncs.com\n    regionId: cn-hangzhou\n    templateCode: SMS_154950909\n    signName: 阿里云短信测试\n', 'fdbb248346f15f46e57a5cea5490e52b', '2023-04-06 17:15:47', '2023-04-18 15:04:44', 'nacos', '222.64.106.138', '', 'edu', '', '', '', 'yaml', '', '');
INSERT INTO `config_info` (`id`, `data_id`, `group_id`, `content`, `md5`, `gmt_create`, `gmt_modified`, `src_user`, `src_ip`, `app_name`, `tenant_id`, `c_desc`, `c_use`, `effect`, `type`, `c_schema`, `encrypted_data_key`) VALUES (257, 'art-gateway.yaml', 'DEFAULT_GROUP', 'server:\n  port: 9999\nspring:\n  cloud:\n    gateway:\n      routes:\n      default-filters:\n        - StripPrefix=1\n\naj:\n  captcha:\n    cache-type: redis\n ', '6110a8f649396d33a3eb3ddb2caa6fd9', '2023-04-18 15:03:25', '2023-04-18 15:03:25', NULL, '222.64.106.138', '', 'edu', NULL, NULL, NULL, 'yaml', NULL, '');
INSERT INTO `config_info` (`id`, `data_id`, `group_id`, `content`, `md5`, `gmt_create`, `gmt_modified`, `src_user`, `src_ip`, `app_name`, `tenant_id`, `c_desc`, `c_use`, `effect`, `type`, `c_schema`, `encrypted_data_key`) VALUES (262, 'art-edu.yaml', 'DEFAULT_GROUP', 'server:\n  port: 5000\n\nspring:\n  datasource:\n    driver-class-name: com.mysql.cj.jdbc.Driver\n    username: root\n    password: art-mysql-password\n    url: jdbc:mysql://art-mysql/art_base?useUnicode=true&characterEncoding=UTF-8&rewriteBatchedStatements=true&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=GMT%2b8\n\n\nmybatis-plus:\n  type-aliases-package: com.fxz.common.core.entity.system\n  mapper-locations: classpath:mapper/*/*.xml\n  configuration:\n    jdbc-type-for-null: null\n  global-config:\n    banner: false\n', 'ec2beaf0161aac73dffd0fd21299865c', '2023-04-18 15:15:49', '2023-04-18 15:34:04', 'nacos', '222.64.106.138', '', '', '', '', '', 'yaml', '', '');
COMMIT;

-- ----------------------------
-- Table structure for config_info_aggr
-- ----------------------------
DROP TABLE IF EXISTS `config_info_aggr`;
CREATE TABLE `config_info_aggr` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT 'id',
  `data_id` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_bin NOT NULL COMMENT 'data_id',
  `group_id` varchar(128) CHARACTER SET utf8mb3 COLLATE utf8mb3_bin NOT NULL COMMENT 'group_id',
  `datum_id` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_bin NOT NULL COMMENT 'datum_id',
  `content` longtext CHARACTER SET utf8mb3 COLLATE utf8mb3_bin NOT NULL COMMENT '内容',
  `gmt_modified` datetime NOT NULL COMMENT '修改时间',
  `app_name` varchar(128) CHARACTER SET utf8mb3 COLLATE utf8mb3_bin DEFAULT NULL,
  `tenant_id` varchar(128) CHARACTER SET utf8mb3 COLLATE utf8mb3_bin DEFAULT '' COMMENT '租户字段',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE KEY `uk_configinfoaggr_datagrouptenantdatum` (`data_id`,`group_id`,`tenant_id`,`datum_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3 COLLATE=utf8mb3_bin COMMENT='增加租户字段';

-- ----------------------------
-- Records of config_info_aggr
-- ----------------------------
BEGIN;
COMMIT;

-- ----------------------------
-- Table structure for config_info_beta
-- ----------------------------
DROP TABLE IF EXISTS `config_info_beta`;
CREATE TABLE `config_info_beta` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT 'id',
  `data_id` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_bin NOT NULL COMMENT 'data_id',
  `group_id` varchar(128) CHARACTER SET utf8mb3 COLLATE utf8mb3_bin NOT NULL COMMENT 'group_id',
  `app_name` varchar(128) CHARACTER SET utf8mb3 COLLATE utf8mb3_bin DEFAULT NULL COMMENT 'app_name',
  `content` longtext CHARACTER SET utf8mb3 COLLATE utf8mb3_bin NOT NULL COMMENT 'content',
  `beta_ips` varchar(1024) CHARACTER SET utf8mb3 COLLATE utf8mb3_bin DEFAULT NULL COMMENT 'betaIps',
  `md5` varchar(32) CHARACTER SET utf8mb3 COLLATE utf8mb3_bin DEFAULT NULL COMMENT 'md5',
  `gmt_create` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `gmt_modified` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '修改时间',
  `src_user` text CHARACTER SET utf8mb3 COLLATE utf8mb3_bin COMMENT 'source user',
  `src_ip` varchar(50) CHARACTER SET utf8mb3 COLLATE utf8mb3_bin DEFAULT NULL COMMENT 'source ip',
  `tenant_id` varchar(128) CHARACTER SET utf8mb3 COLLATE utf8mb3_bin DEFAULT '' COMMENT '租户字段',
  `encrypted_data_key` text CHARACTER SET utf8mb3 COLLATE utf8mb3_bin NOT NULL COMMENT '秘钥',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE KEY `uk_configinfobeta_datagrouptenant` (`data_id`,`group_id`,`tenant_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3 COLLATE=utf8mb3_bin COMMENT='config_info_beta';

-- ----------------------------
-- Records of config_info_beta
-- ----------------------------
BEGIN;
COMMIT;

-- ----------------------------
-- Table structure for config_info_tag
-- ----------------------------
DROP TABLE IF EXISTS `config_info_tag`;
CREATE TABLE `config_info_tag` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT 'id',
  `data_id` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_bin NOT NULL COMMENT 'data_id',
  `group_id` varchar(128) CHARACTER SET utf8mb3 COLLATE utf8mb3_bin NOT NULL COMMENT 'group_id',
  `tenant_id` varchar(128) CHARACTER SET utf8mb3 COLLATE utf8mb3_bin DEFAULT '' COMMENT 'tenant_id',
  `tag_id` varchar(128) CHARACTER SET utf8mb3 COLLATE utf8mb3_bin NOT NULL COMMENT 'tag_id',
  `app_name` varchar(128) CHARACTER SET utf8mb3 COLLATE utf8mb3_bin DEFAULT NULL COMMENT 'app_name',
  `content` longtext CHARACTER SET utf8mb3 COLLATE utf8mb3_bin NOT NULL COMMENT 'content',
  `md5` varchar(32) CHARACTER SET utf8mb3 COLLATE utf8mb3_bin DEFAULT NULL COMMENT 'md5',
  `gmt_create` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `gmt_modified` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '修改时间',
  `src_user` text CHARACTER SET utf8mb3 COLLATE utf8mb3_bin COMMENT 'source user',
  `src_ip` varchar(50) CHARACTER SET utf8mb3 COLLATE utf8mb3_bin DEFAULT NULL COMMENT 'source ip',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE KEY `uk_configinfotag_datagrouptenanttag` (`data_id`,`group_id`,`tenant_id`,`tag_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3 COLLATE=utf8mb3_bin COMMENT='config_info_tag';

-- ----------------------------
-- Records of config_info_tag
-- ----------------------------
BEGIN;
COMMIT;

-- ----------------------------
-- Table structure for config_tags_relation
-- ----------------------------
DROP TABLE IF EXISTS `config_tags_relation`;
CREATE TABLE `config_tags_relation` (
  `id` bigint NOT NULL COMMENT 'id',
  `tag_name` varchar(128) CHARACTER SET utf8mb3 COLLATE utf8mb3_bin NOT NULL COMMENT 'tag_name',
  `tag_type` varchar(64) CHARACTER SET utf8mb3 COLLATE utf8mb3_bin DEFAULT NULL COMMENT 'tag_type',
  `data_id` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_bin NOT NULL COMMENT 'data_id',
  `group_id` varchar(128) CHARACTER SET utf8mb3 COLLATE utf8mb3_bin NOT NULL COMMENT 'group_id',
  `tenant_id` varchar(128) CHARACTER SET utf8mb3 COLLATE utf8mb3_bin DEFAULT '' COMMENT 'tenant_id',
  `nid` bigint NOT NULL AUTO_INCREMENT,
  PRIMARY KEY (`nid`) USING BTREE,
  UNIQUE KEY `uk_configtagrelation_configidtag` (`id`,`tag_name`,`tag_type`) USING BTREE,
  KEY `idx_tenant_id` (`tenant_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3 COLLATE=utf8mb3_bin COMMENT='config_tag_relation';

-- ----------------------------
-- Records of config_tags_relation
-- ----------------------------
BEGIN;
COMMIT;

-- ----------------------------
-- Table structure for group_capacity
-- ----------------------------
DROP TABLE IF EXISTS `group_capacity`;
CREATE TABLE `group_capacity` (
  `id` bigint unsigned NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `group_id` varchar(128) CHARACTER SET utf8mb3 COLLATE utf8mb3_bin NOT NULL DEFAULT '' COMMENT 'Group ID，空字符表示整个集群',
  `quota` int unsigned NOT NULL DEFAULT '0' COMMENT '配额，0表示使用默认值',
  `usage` int unsigned NOT NULL DEFAULT '0' COMMENT '使用量',
  `max_size` int unsigned NOT NULL DEFAULT '0' COMMENT '单个配置大小上限，单位为字节，0表示使用默认值',
  `max_aggr_count` int unsigned NOT NULL DEFAULT '0' COMMENT '聚合子配置最大个数，，0表示使用默认值',
  `max_aggr_size` int unsigned NOT NULL DEFAULT '0' COMMENT '单个聚合数据的子配置大小上限，单位为字节，0表示使用默认值',
  `max_history_count` int unsigned NOT NULL DEFAULT '0' COMMENT '最大变更历史数量',
  `gmt_create` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `gmt_modified` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '修改时间',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE KEY `uk_group_id` (`group_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3 COLLATE=utf8mb3_bin COMMENT='集群、各Group容量信息表';

-- ----------------------------
-- Records of group_capacity
-- ----------------------------
BEGIN;
COMMIT;

-- ----------------------------
-- Table structure for his_config_info
-- ----------------------------
DROP TABLE IF EXISTS `his_config_info`;
CREATE TABLE `his_config_info` (
  `id` bigint unsigned NOT NULL,
  `nid` bigint unsigned NOT NULL AUTO_INCREMENT,
  `data_id` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_bin NOT NULL,
  `group_id` varchar(128) CHARACTER SET utf8mb3 COLLATE utf8mb3_bin NOT NULL,
  `app_name` varchar(128) CHARACTER SET utf8mb3 COLLATE utf8mb3_bin DEFAULT NULL COMMENT 'app_name',
  `content` longtext CHARACTER SET utf8mb3 COLLATE utf8mb3_bin NOT NULL,
  `md5` varchar(32) CHARACTER SET utf8mb3 COLLATE utf8mb3_bin DEFAULT NULL,
  `gmt_create` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `gmt_modified` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `src_user` text CHARACTER SET utf8mb3 COLLATE utf8mb3_bin,
  `src_ip` varchar(50) CHARACTER SET utf8mb3 COLLATE utf8mb3_bin DEFAULT NULL,
  `op_type` char(10) CHARACTER SET utf8mb3 COLLATE utf8mb3_bin DEFAULT NULL,
  `tenant_id` varchar(128) CHARACTER SET utf8mb3 COLLATE utf8mb3_bin DEFAULT '' COMMENT '租户字段',
  `encrypted_data_key` text CHARACTER SET utf8mb3 COLLATE utf8mb3_bin NOT NULL COMMENT '秘钥',
  PRIMARY KEY (`nid`) USING BTREE,
  KEY `idx_gmt_create` (`gmt_create`) USING BTREE,
  KEY `idx_gmt_modified` (`gmt_modified`) USING BTREE,
  KEY `idx_did` (`data_id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=108 DEFAULT CHARSET=utf8mb3 COLLATE=utf8mb3_bin COMMENT='多租户改造';

-- ----------------------------
-- Records of his_config_info
-- ----------------------------
BEGIN;
COMMIT;

-- ----------------------------
-- Table structure for permissions
-- ----------------------------
DROP TABLE IF EXISTS `permissions`;
CREATE TABLE `permissions` (
  `role` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `resource` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `action` varchar(8) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  UNIQUE KEY `uk_role_permission` (`role`,`resource`,`action`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- ----------------------------
-- Records of permissions
-- ----------------------------
BEGIN;
COMMIT;

-- ----------------------------
-- Table structure for roles
-- ----------------------------
DROP TABLE IF EXISTS `roles`;
CREATE TABLE `roles` (
  `username` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `role` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  UNIQUE KEY `idx_user_role` (`username`,`role`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- ----------------------------
-- Records of roles
-- ----------------------------
BEGIN;
INSERT INTO `roles` (`username`, `role`) VALUES ('nacos', 'ROLE_ADMIN');
COMMIT;

-- ----------------------------
-- Table structure for tenant_capacity
-- ----------------------------
DROP TABLE IF EXISTS `tenant_capacity`;
CREATE TABLE `tenant_capacity` (
  `id` bigint unsigned NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `tenant_id` varchar(128) CHARACTER SET utf8mb3 COLLATE utf8mb3_bin NOT NULL DEFAULT '' COMMENT 'Tenant ID',
  `quota` int unsigned NOT NULL DEFAULT '0' COMMENT '配额，0表示使用默认值',
  `usage` int unsigned NOT NULL DEFAULT '0' COMMENT '使用量',
  `max_size` int unsigned NOT NULL DEFAULT '0' COMMENT '单个配置大小上限，单位为字节，0表示使用默认值',
  `max_aggr_count` int unsigned NOT NULL DEFAULT '0' COMMENT '聚合子配置最大个数',
  `max_aggr_size` int unsigned NOT NULL DEFAULT '0' COMMENT '单个聚合数据的子配置大小上限，单位为字节，0表示使用默认值',
  `max_history_count` int unsigned NOT NULL DEFAULT '0' COMMENT '最大变更历史数量',
  `gmt_create` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `gmt_modified` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '修改时间',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE KEY `uk_tenant_id` (`tenant_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3 COLLATE=utf8mb3_bin COMMENT='租户容量信息表';

-- ----------------------------
-- Records of tenant_capacity
-- ----------------------------
BEGIN;
COMMIT;

-- ----------------------------
-- Table structure for tenant_info
-- ----------------------------
DROP TABLE IF EXISTS `tenant_info`;
CREATE TABLE `tenant_info` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT 'id',
  `kp` varchar(128) CHARACTER SET utf8mb3 COLLATE utf8mb3_bin NOT NULL COMMENT 'kp',
  `tenant_id` varchar(128) CHARACTER SET utf8mb3 COLLATE utf8mb3_bin DEFAULT '' COMMENT 'tenant_id',
  `tenant_name` varchar(128) CHARACTER SET utf8mb3 COLLATE utf8mb3_bin DEFAULT '' COMMENT 'tenant_name',
  `tenant_desc` varchar(256) CHARACTER SET utf8mb3 COLLATE utf8mb3_bin DEFAULT NULL COMMENT 'tenant_desc',
  `create_source` varchar(32) CHARACTER SET utf8mb3 COLLATE utf8mb3_bin DEFAULT NULL COMMENT 'create_source',
  `gmt_create` bigint NOT NULL COMMENT '创建时间',
  `gmt_modified` bigint NOT NULL COMMENT '修改时间',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE KEY `uk_tenant_info_kptenantid` (`kp`,`tenant_id`) USING BTREE,
  KEY `idx_tenant_id` (`tenant_id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb3 COLLATE=utf8mb3_bin COMMENT='tenant_info';

-- ----------------------------
-- Records of tenant_info
-- ----------------------------
BEGIN;
INSERT INTO `tenant_info` (`id`, `kp`, `tenant_id`, `tenant_name`, `tenant_desc`, `create_source`, `gmt_create`, `gmt_modified`) VALUES (1, '1', 'edu', 'edu', 'edu', 'nacos', 1680770339023, 1680770339023);
COMMIT;

-- ----------------------------
-- Table structure for users
-- ----------------------------
DROP TABLE IF EXISTS `users`;
CREATE TABLE `users` (
  `username` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `password` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `enabled` tinyint(1) NOT NULL,
  PRIMARY KEY (`username`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- ----------------------------
-- Records of users
-- ----------------------------
BEGIN;
INSERT INTO `users` (`username`, `password`, `enabled`) VALUES ('nacos', '$2a$10$bKc5DOFPqLjOS2nLqGCBAe0BbIgU2i3/umWj0W5tvtZla6/ndpVLa', 1);
COMMIT;

SET FOREIGN_KEY_CHECKS = 1;
