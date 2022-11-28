

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for config_info
-- ----------------------------
DROP TABLE IF EXISTS `config_info`;
CREATE TABLE `config_info` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT 'id',
  `data_id` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_bin NOT NULL COMMENT 'data_id',
  `group_id` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_bin DEFAULT NULL,
  `content` longtext CHARACTER SET utf8mb3 COLLATE utf8mb3_bin NOT NULL COMMENT 'content',
  `md5` varchar(32) CHARACTER SET utf8mb3 COLLATE utf8mb3_bin DEFAULT NULL COMMENT 'md5',
  `gmt_create` datetime NOT NULL DEFAULT '2010-05-05 00:00:00' COMMENT '创建时间',
  `gmt_modified` datetime NOT NULL DEFAULT '2010-05-05 00:00:00' COMMENT '修改时间',
  `src_user` text CHARACTER SET utf8mb3 COLLATE utf8mb3_bin COMMENT 'source user',
  `src_ip` varchar(20) CHARACTER SET utf8mb3 COLLATE utf8mb3_bin DEFAULT NULL COMMENT 'source ip',
  `app_name` varchar(128) CHARACTER SET utf8mb3 COLLATE utf8mb3_bin DEFAULT NULL,
  `tenant_id` varchar(128) CHARACTER SET utf8mb3 COLLATE utf8mb3_bin DEFAULT '' COMMENT '租户字段',
  `c_desc` varchar(256) CHARACTER SET utf8mb3 COLLATE utf8mb3_bin DEFAULT NULL,
  `c_use` varchar(64) CHARACTER SET utf8mb3 COLLATE utf8mb3_bin DEFAULT NULL,
  `effect` varchar(64) CHARACTER SET utf8mb3 COLLATE utf8mb3_bin DEFAULT NULL,
  `type` varchar(64) CHARACTER SET utf8mb3 COLLATE utf8mb3_bin DEFAULT NULL,
  `c_schema` text CHARACTER SET utf8mb3 COLLATE utf8mb3_bin,
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE KEY `uk_configinfo_datagrouptenant` (`data_id`,`group_id`,`tenant_id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=278 DEFAULT CHARSET=utf8mb3 COLLATE=utf8mb3_bin COMMENT='config_info';

-- ----------------------------
-- Records of config_info
-- ----------------------------
BEGIN;
INSERT INTO `config_info` (`id`, `data_id`, `group_id`, `content`, `md5`, `gmt_create`, `gmt_modified`, `src_user`, `src_ip`, `app_name`, `tenant_id`, `c_desc`, `c_use`, `effect`, `type`, `c_schema`) VALUES (1, 'fxz-auth.yaml', 'DEFAULT_GROUP', 'server:\n  port: 8101\n\nwechat:\n  weapp:\n    appid: ENC(SxZ78YMbeLIHg1/IAmPxkVFjbxHUDPSJDsrVJMmc/ws=)\n    secret: ENC(OcF3VEu+7nyetyF7S5pTvidOfNRXT1KLsuo6h09aYUD74Hz0buJo8ONkg/Z2AiNj)\n\nspring:\n  datasource:\n    driver-class-name: com.mysql.cj.jdbc.Driver\n    username: root\n    password: ENC(bM81d5utQm25BU/ZnKFOL5Otj1+/GyCaF9+rnC9YUxA=)\n    url: jdbc:mysql://fxz-mysql/fxz_cloud_base?useUnicode=true&characterEncoding=UTF-8&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=GMT%2b8&rewriteBatchedStatements=true\n  \nmybatis-plus:\n  type-aliases-package: com.fxz.common.entity.system\n  mapper-locations: classpath:mapper/*.xml\n  configuration:\n    jdbc-type-for-null: null\n  global-config:\n    banner: false\n\ninfo:\n  app:\n    name: ${spring.application.name}\n    description: \"@project.description@\"\n    version: \"@project.version@\"\n\nmanagement:\n  endpoints:\n    web:\n      exposure:\n        include: \'*\'\n  endpoint:\n    health:\n      show-details: ALWAYS\n\nsecurity:\n  oauth2:\n    resource:\n      id: ${spring.application.name}\n      #token-info-uri:  http://${fxz-gateway}:8301/auth/oauth/check_token\n      user-info-uri: http://fxz-gateway:8301/auth/user\n\nfxz:\n  cloud:\n    security:\n      anonUris: /token/**,/**/swagger-ui/index.html', 'f894ef910899dd438420dca053e5bbb6', '2021-12-16 18:39:32', '2022-10-21 14:29:18', NULL, '0:0:0:0:0:0:0:1', '', '', 'null', 'null', 'null', 'yaml', 'null');
INSERT INTO `config_info` (`id`, `data_id`, `group_id`, `content`, `md5`, `gmt_create`, `gmt_modified`, `src_user`, `src_ip`, `app_name`, `tenant_id`, `c_desc`, `c_use`, `effect`, `type`, `c_schema`) VALUES (4, 'fxz-gateway.yaml', 'DEFAULT_GROUP', 'server:\n  port: 8301\nspring:\n  cloud:\n    gateway:\n      routes:\n        # - id: fxz-auth\n        #   uri: lb://fxz-auth\n        #   predicates:\n        #     - Path=/auth/**\n        # - id: fxz-server-system\n        #   uri: lb://fxz-server-system\n        #   predicates:\n        #     - Path=/system/** \n        #   #  - FxzTimeBetween=上午7:00,下午1:00 #自定义的断言工厂，模拟业务只能上午7点到下午1点访问\n        # - id: fxz-generate\n        #   uri: lb://fxz-generate\n        #   predicates:\n        #     - Path=/generate/**\n        # - id: fxz-job\n        #   uri: lb://fxz-job\n        #   predicates:\n        #     - Path=/schedule/**\n        # - id: fxz-mall-product\n        #   uri: lb://fxz-mall-product\n        #   predicates:\n        #     - Path=/product/**\n        # - id: fxz-mall-order\n        #   uri: lb://fxz-mall-order\n        #   predicates:\n        #     - Path=/order/**\n        # - id: fxz-mall-user\n        #   uri: lb://fxz-mall-user\n        #   predicates:\n        #     - Path=/user/**\n        # - id: fxz-mall-search\n        #   uri: lb://fxz-mall-search\n        #   predicates:\n        #     - Path=/search/**\n        # - id: fxz-mall-promotion\n        #   uri: lb://fxz-mall-promotion\n        #   predicates:\n        #     - Path=/promotion/**\n      default-filters:\n        - StripPrefix=1\n\n\nmanagement:\n  endpoint:\n    health:\n      show-details: ALWAYS\n  endpoints:\n    web:\n      exposure:\n        include: health,info,gateway\n\naj:\n  captcha:\n    cache-type: redis\n ', 'cbbf671074f4d68191f94694de616747', '2021-12-16 18:52:35', '2022-10-22 16:06:20', NULL, '0:0:0:0:0:0:0:1', '', '', 'null', 'null', 'null', 'yaml', 'null');
INSERT INTO `config_info` (`id`, `data_id`, `group_id`, `content`, `md5`, `gmt_create`, `gmt_modified`, `src_user`, `src_ip`, `app_name`, `tenant_id`, `c_desc`, `c_use`, `effect`, `type`, `c_schema`) VALUES (6, 'fxz-server-system.yaml', 'DEFAULT_GROUP', 'server:\n  port: 8201\n\nspring:\n  datasource:\n    driver-class-name: com.mysql.cj.jdbc.Driver\n    username: root\n    password: ENC(bM81d5utQm25BU/ZnKFOL5Otj1+/GyCaF9+rnC9YUxA=)\n    url: jdbc:mysql://fxz-mysql/fxz_cloud_base?useUnicode=true&characterEncoding=UTF-8&rewriteBatchedStatements=true&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=GMT%2b8\n   # RabbitMQ 配置\n  rabbitmq:\n    host: fxz-rabbitmq\n    port: 5672\n    username: fxz\n    password: 123456\n    # 动态创建和绑定队列、交换机的配置\n    modules: \n      # 订单延时队列，到了过期的时间会被转发到订单取消队列\n      - routing-key: canal_key\n        queue:\n          name: canal_queue\n        exchange:\n          name: canal_exchange\n  autoconfigure:\n    exclude: org.springframework.cloud.sleuth.instrument.hystrix.SleuthHystrixAutoConfiguration\n\nsecurity:\n  oauth2:\n    resource:\n      id: ${spring.application.name}\n      #token-info-uri:  http://${fxz-gateway}:8301/auth/oauth/check_token\n      user-info-uri: http://fxz-gateway:8301/auth/user\n\nmybatis-plus:\n  type-aliases-package: com.fxz.common.core.entity.system\n  mapper-locations: classpath:mapper/*/*.xml\n  configuration:\n    jdbc-type-for-null: null\n  global-config:\n    banner: false\n\ninfo:\n  app:\n    name: ${spring.application.name}\n    description: \"@project.description@\"\n    version: \"@project.version@\"\n\nmanagement:\n  endpoints:\n    web:\n      exposure:\n        include: \'*\'\n  endpoint:\n    health:\n      show-details: ALWAYS\n\nfeign:\n  hystrix:\n    enabled: true\n\nhystrix:\n  shareSecurityContext: true\n\nfxz:\n  cloud:\n    security: \n      anonUris: /find,/swagger-ui/**,/webjars/**,/swagger-resources/**,/v2/api-docs/**,/,/csrf,/actuator/**,/user/findByName/*,/menu/findUserPermissions/**,/user/test,/kk-anti-reptile/**,/swagger**/**,/**/api-docs/**,/doc.html\n  common:\n    sequence:\n      type: redis\n      \n# 文件系统\noss:\n  endpoint: http://fxz-oss:9000\n  access-key: admin\n  secret-key: admin123456\n  bucketName: fxzcloud\n\naliyun:\n  sms:\n    accessKeyId: LTAI5tJymbcizqk5nnVvQUU6\n    accessKeySecret: 9iszXlrnjoXuLLWIXFiaFCzWg2ttW2\n    domain: dysmsapi.aliyuncs.com\n    regionId: cn-hangzhou\n    templateCode: SMS_154950909\n    signName: 阿里云短信测试\n', '07c8a0492b5ba704e7a4bc04bf1cb3b6', '2021-12-17 19:01:56', '2022-10-21 15:06:52', NULL, '0:0:0:0:0:0:0:1', '', '', 'null', 'null', 'null', 'yaml', 'null');
INSERT INTO `config_info` (`id`, `data_id`, `group_id`, `content`, `md5`, `gmt_create`, `gmt_modified`, `src_user`, `src_ip`, `app_name`, `tenant_id`, `c_desc`, `c_use`, `effect`, `type`, `c_schema`) VALUES (70, 'fxz-generate.yaml', 'DEFAULT_GROUP', 'server:\n  port: 8400\n\nspring:\n  datasource:\n    driver-class-name: com.mysql.cj.jdbc.Driver\n    username: root\n    password: fxzcloud-mysql-password\n    url: jdbc:mysql://fxz-mysql/fxz_cloud_codegen?useUnicode=true&characterEncoding=UTF-8&rewriteBatchedStatements=true&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=GMT%2b8\n\n  \nsecurity:\n  oauth2:\n    resource:\n      id: ${spring.application.name}\n      user-info-uri: http://fxz-gateway:8301/auth/user\n\nmybatis-plus:\n  mapper-locations: classpath:mapper/*/*.xml\n  configuration:\n    jdbc-type-for-null: null\n  global-config:\n    banner: false', 'd1a1750b9134b97c24af1156fc6d09f1', '2022-03-31 11:02:50', '2022-09-17 10:28:18', NULL, '0:0:0:0:0:0:0:1', '', '', 'null', 'null', 'null', 'yaml', 'null');
INSERT INTO `config_info` (`id`, `data_id`, `group_id`, `content`, `md5`, `gmt_create`, `gmt_modified`, `src_user`, `src_ip`, `app_name`, `tenant_id`, `c_desc`, `c_use`, `effect`, `type`, `c_schema`) VALUES (80, 'fxz-job.yaml', 'DEFAULT_GROUP', 'server:\n  port: 8500\n\n\nspring:\n  datasource:\n    driver-class-name: com.mysql.cj.jdbc.Driver\n    username: root\n    password: ENC(bM81d5utQm25BU/ZnKFOL5Otj1+/GyCaF9+rnC9YUxA=)\n    url: jdbc:mysql://fxz-mysql/fxz_cloud_job?useUnicode=true&characterEncoding=UTF-8&rewriteBatchedStatements=true&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=GMT%2b8\n  \n\nsecurity:\n  oauth2:\n    resource:\n      id: ${spring.application.name}\n      user-info-uri: http://fxz-gateway:8301/auth/user\n\n\nmybatis-plus:\n  mapper-locations: classpath:mapper/*/*.xml\n  configuration:\n    jdbc-type-for-null: null\n  global-config:\n    banner: false', '463a152e475c2894937d9a98f7f9e52a', '2022-04-03 17:45:50', '2022-09-17 10:28:28', NULL, '0:0:0:0:0:0:0:1', '', '', 'null', 'null', 'null', 'yaml', 'null');
INSERT INTO `config_info` (`id`, `data_id`, `group_id`, `content`, `md5`, `gmt_create`, `gmt_modified`, `src_user`, `src_ip`, `app_name`, `tenant_id`, `c_desc`, `c_use`, `effect`, `type`, `c_schema`) VALUES (102, 'fxz-mall-product.yaml', 'DEFAULT_GROUP', 'server:\n  port: 6001\n\nspring:\n  cache:\n    type: redis\n  datasource:\n    driver-class-name: com.mysql.cj.jdbc.Driver\n    username: root\n    password: ENC(bM81d5utQm25BU/ZnKFOL5Otj1+/GyCaF9+rnC9YUxA=)\n    url: jdbc:mysql://fxz-mysql/fxz_mall_product?useUnicode=true&characterEncoding=UTF-8&rewriteBatchedStatements=true&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=GMT%2b8\n  autoconfigure:\n    exclude: org.springframework.cloud.sleuth.instrument.hystrix.SleuthHystrixAutoConfiguration\n\nsecurity:\n  oauth2:\n    resource:\n      id: ${spring.application.name}\n      #token-info-uri:  http://${fxz-gateway}:8301/auth/oauth/check_token\n      user-info-uri: http://fxz-gateway:8301/auth/user\n\nmybatis-plus:\n  type-aliases-package: com.fxz.common.core.entity.system\n  mapper-locations: classpath:mapper/*/*.xml\n  configuration:\n    jdbc-type-for-null: null\n  global-config:\n    banner: false\n\ninfo:\n  app:\n    name: ${spring.application.name}\n    description: \"@project.description@\"\n    version: \"@project.version@\"\n\nmanagement:\n  endpoints:\n    web:\n      exposure:\n        include: \'*\'\n  endpoint:\n    health:\n      show-details: ALWAYS\n\nfeign:\n  hystrix:\n    enabled: true\n\nhystrix:\n  shareSecurityContext: true\n\nfxz:\n  cloud:\n    security: \n      anonUris: /find,/swagger-ui.html,/webjars/**,/swagger-resources/**,/v2/api-docs/**,/,/csrf,/actuator/**,/user/findByName/*,/menu/findUserPermissions/**,/user/test,/kk-anti-reptile/**\n\n# 文件系统\noss:\n  endpoint: http://fxz-oss:9000\n  access-key: admin\n  secret-key: admin123456\n  bucketName: fxzcloud', '193e06f8f1cb91a5d5b10f25be5e4300', '2022-05-05 12:12:57', '2022-09-17 10:28:36', NULL, '0:0:0:0:0:0:0:1', '', '', '', '', '', 'yaml', '');
INSERT INTO `config_info` (`id`, `data_id`, `group_id`, `content`, `md5`, `gmt_create`, `gmt_modified`, `src_user`, `src_ip`, `app_name`, `tenant_id`, `c_desc`, `c_use`, `effect`, `type`, `c_schema`) VALUES (108, 'fxz-mall-order.yaml', 'DEFAULT_GROUP', 'server:\n  port: 6101\n\nspring:\n  datasource:\n    driver-class-name: com.mysql.cj.jdbc.Driver\n    username: root\n    password: ENC(bM81d5utQm25BU/ZnKFOL5Otj1+/GyCaF9+rnC9YUxA=)\n    url: jdbc:mysql://fxz-mysql/fxz_mall_order?useUnicode=true&characterEncoding=UTF-8&rewriteBatchedStatements=true&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=GMT%2b8\n  # RabbitMQ 配置\n  rabbitmq:\n    host: fxz-rabbitmq\n    port: 5672\n    username: fxz\n    password: 123456\n    # 动态创建和绑定队列、交换机的配置\n    modules: \n      # 订单延时队列，到了过期的时间会被转发到订单取消队列\n      - routing-key: order.create.routing.key\n        queue:\n          name: order.delay.queue\n          dead-letter-exchange: order.exchange\n          dead-letter-routing-key: order.close.routing.key\n          arguments:\n            # 1分钟(测试)，单位毫秒,没有被消费则进入死信队列\n            x-message-ttl: 60000 \n        exchange:\n          name: order.exchange\n      # 订单取消队列\n      - routing-key: order.close.routing.key\n        queue:\n          name: order.close.queue\n        exchange:\n          name: order.exchange\n  redis:\n    database: 0\n    host: fxz-redis\n    port: 6379\n    password: fxzcloud-redis\n    lettuce:\n      pool:\n        min-idle: 8\n        max-idle: 500\n        max-active: 2000\n        max-wait: 10000\n    timeout: 5000  \n  autoconfigure:\n    exclude: org.springframework.cloud.sleuth.instrument.hystrix.SleuthHystrixAutoConfiguration\n\nsecurity:\n  oauth2:\n    resource:\n      id: ${spring.application.name}\n      user-info-uri: http://fxz-gateway:8301/auth/user\n\nmybatis-plus:\n  type-aliases-package: com.fxz.common.core.entity.system\n  mapper-locations: classpath:mapper/*/*.xml\n  configuration:\n    jdbc-type-for-null: null\n  global-config:\n    banner: false\n\ninfo:\n  app:\n    name: ${spring.application.name}\n    description: \"@project.description@\"\n    version: \"@project.version@\"\n\nmanagement:\n  endpoints:\n    web:\n      exposure:\n        include: \'*\'\n  endpoint:\n    health:\n      show-details: ALWAYS\n\nfeign:\n  hystrix:\n    enabled: true\n\nhystrix:\n  shareSecurityContext: true\n\nfxz:\n  cloud:\n    security: \n      anonUris: /find,/swagger-ui.html,/webjars/**,/swagger-resources/**,/v2/api-docs/**,/,/csrf,/actuator/**,/user/findByName/*,/menu/findUserPermissions/**,/user/test,/kk-anti-reptile/**\n\n# 文件系统\noss:\n  endpoint: http://fxz-oss:9000\n  access-key: admin\n  secret-key: admin123456\n  bucketName: fxzcloud', '405601c72792835bcfa8a56ba18c8ad6', '2022-05-13 08:42:53', '2022-09-17 10:28:45', NULL, '0:0:0:0:0:0:0:1', '', '', '', '', '', 'yaml', '');
INSERT INTO `config_info` (`id`, `data_id`, `group_id`, `content`, `md5`, `gmt_create`, `gmt_modified`, `src_user`, `src_ip`, `app_name`, `tenant_id`, `c_desc`, `c_use`, `effect`, `type`, `c_schema`) VALUES (110, 'fxz-mall-user.yaml', 'DEFAULT_GROUP', 'server:\n  port: 6201\n\nspring:\n  datasource:\n    driver-class-name: com.mysql.cj.jdbc.Driver\n    username: root\n    password: ENC(bM81d5utQm25BU/ZnKFOL5Otj1+/GyCaF9+rnC9YUxA=)\n    url: jdbc:mysql://fxz-mysql/fxz_mall_user?useUnicode=true&characterEncoding=UTF-8&rewriteBatchedStatements=true&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=GMT%2b8 \n  autoconfigure:\n    exclude: org.springframework.cloud.sleuth.instrument.hystrix.SleuthHystrixAutoConfiguration\n\nsecurity:\n  oauth2:\n    resource:\n      id: ${spring.application.name}\n      user-info-uri: http://fxz-gateway:8301/auth/user\n\nmybatis-plus:\n  type-aliases-package: com.fxz.common.core.entity.system\n  mapper-locations: classpath:mapper/*/*.xml\n  configuration:\n    jdbc-type-for-null: null\n  global-config:\n    banner: false\n\ninfo:\n  app:\n    name: ${spring.application.name}\n    description: \"@project.description@\"\n    version: \"@project.version@\"\n\nmanagement:\n  endpoints:\n    web:\n      exposure:\n        include: \'*\'\n  endpoint:\n    health:\n      show-details: ALWAYS\n\nfeign:\n  hystrix:\n    enabled: true\n\nhystrix:\n  shareSecurityContext: true\n\nfxz:\n  cloud:\n    security: \n      anonUris: /find,/swagger-ui.html,/webjars/**,/swagger-resources/**,/v2/api-docs/**,/,/csrf,/actuator/**,/user/findByName/*,/menu/findUserPermissions/**,/user/test,/kk-anti-reptile/**\n', '23c4b81765e0c8323a56a87f456a21d8', '2022-05-14 03:09:20', '2022-09-17 10:28:56', NULL, '0:0:0:0:0:0:0:1', '', '', '', '', '', 'yaml', '');
INSERT INTO `config_info` (`id`, `data_id`, `group_id`, `content`, `md5`, `gmt_create`, `gmt_modified`, `src_user`, `src_ip`, `app_name`, `tenant_id`, `c_desc`, `c_use`, `effect`, `type`, `c_schema`) VALUES (126, 'fxz-mall-search.yaml', 'DEFAULT_GROUP', 'server:\n  port: 6301\n\nspring:\n   # RabbitMQ 配置\n  rabbitmq:\n    host: fxz-rabbitmq\n    port: 5672\n    username: fxz\n    password: 123456\n    # 动态创建和绑定队列、交换机的配置\n    modules: \n      # 订单延时队列，到了过期的时间会被转发到订单取消队列\n      - routing-key: canal_key\n        queue:\n          name: canal_queue\n        exchange:\n          name: canal_exchange\n\nsecurity:\n  oauth2:\n    resource:\n      id: ${spring.application.name}\n      user-info-uri: http://fxz-gateway:8301/auth/user\n\nmybatis-plus:\n  type-aliases-package: com.fxz.common.core.entity.system\n  mapper-locations: classpath:mapper/*/*.xml\n  configuration:\n    jdbc-type-for-null: null\n  global-config:\n    banner: false\n\ninfo:\n  app:\n    name: ${spring.application.name}\n    description: \"@project.description@\"\n    version: \"@project.version@\"\n\nmanagement:\n  endpoints:\n    web:\n      exposure:\n        include: \'*\'\n  endpoint:\n    health:\n      show-details: ALWAYS\n\nfeign:\n  hystrix:\n    enabled: true\n\nhystrix:\n  shareSecurityContext: true\n\nfxz:\n  cloud:\n    security: \n      anonUris: /find,/swagger-ui.html,/webjars/**,/swagger-resources/**,/v2/api-docs/**,/,/csrf,/actuator/**,/user/findByName/*,/menu/findUserPermissions/**,/user/test,/kk-anti-reptile/**\n', 'a81bd937fc8f05567943ea4e8500f533', '2022-05-27 14:03:25', '2022-07-27 12:29:16', NULL, '0:0:0:0:0:0:0:1', '', '', '', '', '', 'yaml', '');
INSERT INTO `config_info` (`id`, `data_id`, `group_id`, `content`, `md5`, `gmt_create`, `gmt_modified`, `src_user`, `src_ip`, `app_name`, `tenant_id`, `c_desc`, `c_use`, `effect`, `type`, `c_schema`) VALUES (135, 'fxz-application-common.yaml', 'DEFAULT_GROUP', 'biz:\n  oss:\n    resources-url: http://localhost:8301\n    \nmybatis-plus:\n  configuration:\n    log-impl: org.apache.ibatis.logging.stdout.StdOutImpl\n\nspring:\n  redis:\n    database: 0\n    host: fxz-redis\n    port: 6379\n    password: ENC(xztexTI7xBO9qlCAXlVv+prennZO1c7q)\n    lettuce:\n      pool:\n        # 连接池中的最小空闲连接\n        min-idle: 8\n        # 连接池中的最大空闲连接\n        max-idle: 500\n        # 连接池最大连接数（使用负值表示没有限制）\n        max-active: 2000\n        # 连接池最大阻塞等待时间（使用负值表示没有限制）\n        max-wait: -1\n    # 连接超时时长（毫秒）\n    timeout: 10000 \n\n\njasypt:\n  encryptor:\n    password: fxzcloud #配置文件加密密钥\n\nfxz:\n  cloud:\n    security: \n      anonUris: /swagger-ui/**,/webjars/**,/swagger-resources/**,/csrf,/actuator/**,/swagger**/**,/**/api-docs/**,/doc.html\n  tenant:\n    ignore-urls:\n      - /user            # check-token获取用户信息 不做拦截\n      - /oauth/token     # 登录接口 不做拦截\n      - /tenant/findIdByName    # 根据租户名称获取租户Id\n      - /user/findByName/*           # 登录时根据用户名查询用户 不做拦截\n      - /user/findByMobile/*         # 登录时根据手机号查询用户 不做拦截\n      - /menu/findUserPermissions/*   # 登录时根据查询角色权限   不做拦截\n      - /tenant/validTenant/*       # 校验租户信息  不做拦截\n      - /role/getDataPermission     # 查询数据权限信息 不做拦截\n      - /operLog/add                # 保存登录日志 不做拦截\n      - /sms/sendSms                # 短信登录 不做拦截\n      - /token/*\n      - /swagger-ui/**\n      - /v3/api-docs/**\n      - /doc.html\n      - /webjars/**\n      - /app/**\n      - /demo/**\n    tables: \n      - sys_user\n      - sys_role\n  common:\n    doc:\n      author: \"fxz\"\n      version: \"0.0.1\"\n      title: \"Art开发文档\"\n      description: \"Art开发文档\"\n      services:\n        \"fxz-server-system\": system\n        \"fxz-mall-user\": user', 'cea562f55fee4d1c071d9d628ec440b7', '2022-06-26 12:10:13', '2022-11-27 21:15:53', 'nacos', '27.115.16.158', '', '', '', '', '', 'yaml', '');
INSERT INTO `config_info` (`id`, `data_id`, `group_id`, `content`, `md5`, `gmt_create`, `gmt_modified`, `src_user`, `src_ip`, `app_name`, `tenant_id`, `c_desc`, `c_use`, `effect`, `type`, `c_schema`) VALUES (152, 'fxz-server-system-flow.yaml', 'DEFAULT_GROUP', '[\n    {\n        \"resource\": \"/test\",\n        \"limitApp\": \"default\",\n        \"grade\": 1,\n        \"count\": 1,\n        \"strategy\": 0,\n        \"controlBehavior\": 0,\n        \"clusterMode\": false\n    }\n]\n', '60b3d58e978e05ac12df555f11fc2132', '2022-07-28 03:09:23', '2022-07-28 03:12:42', NULL, '0:0:0:0:0:0:0:1', '', '', '', '', '', 'json', '');
INSERT INTO `config_info` (`id`, `data_id`, `group_id`, `content`, `md5`, `gmt_create`, `gmt_modified`, `src_user`, `src_ip`, `app_name`, `tenant_id`, `c_desc`, `c_use`, `effect`, `type`, `c_schema`) VALUES (162, 'fxz-mall-promotion.yaml', 'DEFAULT_GROUP', 'server:\n  port: 6401\n\nspring:\n  datasource:\n    driver-class-name: com.mysql.cj.jdbc.Driver\n    username: root\n    password: ENC(bM81d5utQm25BU/ZnKFOL5Otj1+/GyCaF9+rnC9YUxA=)\n    url: jdbc:mysql://fxz-mysql/fxz_mall_promotion?useUnicode=true&characterEncoding=UTF-8&rewriteBatchedStatements=true&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=GMT%2b8\n  autoconfigure:\n    exclude: org.springframework.cloud.sleuth.instrument.hystrix.SleuthHystrixAutoConfiguration\n\nsecurity:\n  oauth2:\n    resource:\n      id: ${spring.application.name}\n      #token-info-uri:  http://${fxz-gateway}:8301/auth/oauth/check_token\n      user-info-uri: http://fxz-gateway:8301/auth/user\n\nmybatis-plus:\n  type-aliases-package: com.fxz.common.core.entity.system\n  mapper-locations: classpath:mapper/*/*.xml\n  configuration:\n    jdbc-type-for-null: null\n  global-config:\n    banner: false\n\ninfo:\n  app:\n    name: ${spring.application.name}\n    description: \"@project.description@\"\n    version: \"@project.version@\"\n\nmanagement:\n  endpoints:\n    web:\n      exposure:\n        include: \'*\'\n  endpoint:\n    health:\n      show-details: ALWAYS\n\nfeign:\n  hystrix:\n    enabled: true\n\nhystrix:\n  shareSecurityContext: true\n\nfxz:\n  cloud:\n    security: \n      anonUris: /find,/swagger-ui.html,/webjars/**,/swagger-resources/**,/v2/api-docs/**,/,/csrf,/actuator/**,/user/findByName/*,/menu/findUserPermissions/**,/user/test,/kk-anti-reptile/**\n\n# 文件系统\noss:\n  endpoint: http://fxz-oss:9000\n  access-key: admin\n  secret-key: admin123456\n  bucketName: fxzcloud', 'c44e84870c7d84a5c3b5519be53d042f', '2022-08-09 03:41:45', '2022-09-17 10:29:16', NULL, '0:0:0:0:0:0:0:1', '', '', '', '', '', 'yaml', '');
INSERT INTO `config_info` (`id`, `data_id`, `group_id`, `content`, `md5`, `gmt_create`, `gmt_modified`, `src_user`, `src_ip`, `app_name`, `tenant_id`, `c_desc`, `c_use`, `effect`, `type`, `c_schema`) VALUES (171, 'fxz-mall-xxl-job.yaml', 'DEFAULT_GROUP', 'server:\n  port: 6501\n   \nfxz:\n  common:\n    xxl-job:\n      appname: ${spring.application.name}\n      admin-addresses: http://127.0.0.1:9003/xxl-job-admin\n      port: 9900\n\nsecurity:\n  oauth2:\n    resource:\n      id: ${spring.application.name}\n      user-info-uri: http://fxz-gateway:8301/auth/user\n\nmybatis-plus:\n  type-aliases-package: com.fxz.common.core.entity.system\n  mapper-locations: classpath:mapper/*/*.xml\n  configuration:\n    jdbc-type-for-null: null\n  global-config:\n    banner: false\n\ninfo:\n  app:\n    name: ${spring.application.name}\n    description: \"@project.description@\"\n    version: \"@project.version@\"\n\nmanagement:\n  endpoints:\n    web:\n      exposure:\n        include: \'*\'\n  endpoint:\n    health:\n      show-details: ALWAYS\n\n\n', '29ab9e77efd5f2dc35382ee3afd67ab2', '2022-08-14 06:52:26', '2022-08-30 15:38:11', NULL, '127.0.0.1', '', '', '', '', '', 'yaml', '');
INSERT INTO `config_info` (`id`, `data_id`, `group_id`, `content`, `md5`, `gmt_create`, `gmt_modified`, `src_user`, `src_ip`, `app_name`, `tenant_id`, `c_desc`, `c_use`, `effect`, `type`, `c_schema`) VALUES (189, 'fxz-demos.yaml', 'DEFAULT_GROUP', 'server:\n  port: 8601\n\nspring:\n  data:\n    mongodb:\n      authentication-database: admin\n      database: user\n      username: admin\n      password: \"123456\"\n      host: fxz-mysql\n      port: 27017\n  rabbitmq:\n    host: fxz-rabbitmq\n    port: 5672\n    username: fxz\n    password: 123456\n\n\nsecurity:\n  oauth2:\n    resource:\n      id: ${spring.application.name}\n      #token-info-uri:  http://${fxz-gateway}:8301/auth/oauth/check_token\n      user-info-uri: http://fxz-gateway:8301/auth/user\n\n\nmanagement:\n  endpoints:\n    web:\n      exposure:\n        include: \'*\'\n  endpoint:\n    health:\n      show-details: ALWAYS\n\njasypt:\n  encryptor:\n    password: fxzcloud', '23b83cea1c5e9cc7b18ed61cf78fd248', '2022-08-30 14:20:58', '2022-11-24 03:23:48', 'nacos', '27.115.16.158', '', '', '', '', '', 'yaml', '');
COMMIT;

-- ----------------------------
-- Table structure for config_info_aggr
-- ----------------------------
DROP TABLE IF EXISTS `config_info_aggr`;
CREATE TABLE `config_info_aggr` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT 'id',
  `data_id` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_bin NOT NULL COMMENT 'data_id',
  `group_id` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_bin NOT NULL COMMENT 'group_id',
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
  `gmt_create` datetime NOT NULL DEFAULT '2010-05-05 00:00:00' COMMENT '创建时间',
  `gmt_modified` datetime NOT NULL DEFAULT '2010-05-05 00:00:00' COMMENT '修改时间',
  `src_user` text CHARACTER SET utf8mb3 COLLATE utf8mb3_bin COMMENT 'source user',
  `src_ip` varchar(20) CHARACTER SET utf8mb3 COLLATE utf8mb3_bin DEFAULT NULL COMMENT 'source ip',
  `tenant_id` varchar(128) CHARACTER SET utf8mb3 COLLATE utf8mb3_bin DEFAULT '' COMMENT '租户字段',
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
  `gmt_create` datetime NOT NULL DEFAULT '2010-05-05 00:00:00' COMMENT '创建时间',
  `gmt_modified` datetime NOT NULL DEFAULT '2010-05-05 00:00:00' COMMENT '修改时间',
  `src_user` text CHARACTER SET utf8mb3 COLLATE utf8mb3_bin COMMENT 'source user',
  `src_ip` varchar(20) CHARACTER SET utf8mb3 COLLATE utf8mb3_bin DEFAULT NULL COMMENT 'source ip',
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
  `gmt_create` datetime NOT NULL DEFAULT '2010-05-05 00:00:00' COMMENT '创建时间',
  `gmt_modified` datetime NOT NULL DEFAULT '2010-05-05 00:00:00' COMMENT '修改时间',
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
  `gmt_create` datetime NOT NULL DEFAULT '2010-05-05 00:00:00',
  `gmt_modified` datetime NOT NULL DEFAULT '2010-05-05 00:00:00',
  `src_user` text CHARACTER SET utf8mb3 COLLATE utf8mb3_bin,
  `src_ip` varchar(20) CHARACTER SET utf8mb3 COLLATE utf8mb3_bin DEFAULT NULL,
  `op_type` char(10) CHARACTER SET utf8mb3 COLLATE utf8mb3_bin DEFAULT NULL,
  `tenant_id` varchar(128) CHARACTER SET utf8mb3 COLLATE utf8mb3_bin DEFAULT '' COMMENT '租户字段',
  PRIMARY KEY (`nid`) USING BTREE,
  KEY `idx_gmt_create` (`gmt_create`) USING BTREE,
  KEY `idx_gmt_modified` (`gmt_modified`) USING BTREE,
  KEY `idx_did` (`data_id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=293 DEFAULT CHARSET=utf8mb3 COLLATE=utf8mb3_bin COMMENT='多租户改造';

-- ----------------------------
-- Records of his_config_info
-- ----------------------------
BEGIN;
INSERT INTO `his_config_info` (`id`, `nid`, `data_id`, `group_id`, `app_name`, `content`, `md5`, `gmt_create`, `gmt_modified`, `src_user`, `src_ip`, `op_type`, `tenant_id`) VALUES (135, 287, 'fxz-application-common.yaml', 'DEFAULT_GROUP', '', 'biz:\n  oss:\n    resources-url: http://localhost:8301\n    \nmybatis-plus:\n  configuration:\n    log-impl: org.apache.ibatis.logging.stdout.StdOutImpl\n\nspring:\n  redis:\n    database: 0\n    host: fxz-redis\n    port: 6379\n    password: ENC(xztexTI7xBO9qlCAXlVv+prennZO1c7q)\n    lettuce:\n      pool:\n        # 最大连接\n        min-idle: 8\n        # 最大空闲连接\n        max-idle: 500\n        max-active: 2000\n        max-wait: 10000\n    timeout: 5000 \n\n\njasypt:\n  encryptor:\n    password: fxzcloud #配置文件加密密钥\n\nfxz:\n  cloud:\n    security: \n      anonUris: /swagger-ui/**,/webjars/**,/swagger-resources/**,/csrf,/actuator/**,/swagger**/**,/**/api-docs/**,/doc.html\n  tenant:\n    ignore-urls:\n      - /user            # check-token获取用户信息 不做拦截\n      - /oauth/token     # 登录接口 不做拦截\n      - /tenant/findIdByName    # 根据租户名称获取租户Id\n      - /user/findByName/*           # 登录时根据用户名查询用户 不做拦截\n      - /user/findByMobile/*         # 登录时根据手机号查询用户 不做拦截\n      - /menu/findUserPermissions/*   # 登录时根据查询角色权限   不做拦截\n      - /tenant/validTenant/*       # 校验租户信息  不做拦截\n      - /role/getDataPermission     # 查询数据权限信息 不做拦截\n      - /operLog/add                # 保存登录日志 不做拦截\n      - /sms/sendSms                # 短信登录 不做拦截\n      - /token/*\n      - /swagger-ui/**\n      - /v3/api-docs/**\n      - /doc.html\n      - /webjars/**\n    tables: \n      - sys_user\n      - sys_role\n  common:\n    doc:\n      author: \"fxz\"\n      version: \"0.0.1\"\n      title: \"Art开发文档\"\n      description: \"Art开发文档\"\n      services:\n        \"fxz-server-system\": system\n        \"fxz-mall-user\": user', '45c1d3ad578d2bfe270aac99be09b3cd', '2010-05-05 00:00:00', '2022-10-29 10:16:29', NULL, '0:0:0:0:0:0:0:1', 'U', '');
INSERT INTO `his_config_info` (`id`, `nid`, `data_id`, `group_id`, `app_name`, `content`, `md5`, `gmt_create`, `gmt_modified`, `src_user`, `src_ip`, `op_type`, `tenant_id`) VALUES (135, 288, 'fxz-application-common.yaml', 'DEFAULT_GROUP', '', 'biz:\n  oss:\n    resources-url: http://localhost:8301\n    \nmybatis-plus:\n  configuration:\n    log-impl: org.apache.ibatis.logging.stdout.StdOutImpl\n\nspring:\n  redis:\n    database: 0\n    host: fxz-redis\n    port: 6379\n    password: ENC(xztexTI7xBO9qlCAXlVv+prennZO1c7q)\n    lettuce:\n      pool:\n        # 最大连接\n        min-idle: 8\n        # 最大空闲连接\n        max-idle: 500\n        max-active: 2000\n        max-wait: 10000\n    timeout: 5000 \n\n\njasypt:\n  encryptor:\n    password: fxzcloud #配置文件加密密钥\n\nfxz:\n  cloud:\n    security: \n      anonUris: /swagger-ui/**,/webjars/**,/swagger-resources/**,/csrf,/actuator/**,/swagger**/**,/**/api-docs/**,/doc.html\n  tenant:\n    ignore-urls:\n      - /user            # check-token获取用户信息 不做拦截\n      - /oauth/token     # 登录接口 不做拦截\n      - /tenant/findIdByName    # 根据租户名称获取租户Id\n      - /user/findByName/*           # 登录时根据用户名查询用户 不做拦截\n      - /user/findByMobile/*         # 登录时根据手机号查询用户 不做拦截\n      - /menu/findUserPermissions/*   # 登录时根据查询角色权限   不做拦截\n      - /tenant/validTenant/*       # 校验租户信息  不做拦截\n      - /role/getDataPermission     # 查询数据权限信息 不做拦截\n      - /operLog/add                # 保存登录日志 不做拦截\n      - /sms/sendSms                # 短信登录 不做拦截\n      - /token/*\n      - /swagger-ui/**\n      - /v3/api-docs/**\n      - /doc.html\n      - /webjars/**\n      - /app/**\n    tables: \n      - sys_user\n      - sys_role\n  common:\n    doc:\n      author: \"fxz\"\n      version: \"0.0.1\"\n      title: \"Art开发文档\"\n      description: \"Art开发文档\"\n      services:\n        \"fxz-server-system\": system\n        \"fxz-mall-user\": user', 'f92f38c4e786dfee2717d7d618f8b936', '2010-05-05 00:00:00', '2022-11-20 07:27:29', NULL, '0:0:0:0:0:0:0:1', 'U', '');
INSERT INTO `his_config_info` (`id`, `nid`, `data_id`, `group_id`, `app_name`, `content`, `md5`, `gmt_create`, `gmt_modified`, `src_user`, `src_ip`, `op_type`, `tenant_id`) VALUES (189, 289, 'fxz-demos.yaml', 'DEFAULT_GROUP', '', 'server:\n  port: 8601\n\nspring:\n  data:\n    mongodb:\n      authentication-database: admin\n      database: user\n      username: admin\n      password: \"123456\"\n      host: fxz-mysql\n      port: 27017\n\n\nsecurity:\n  oauth2:\n    resource:\n      id: ${spring.application.name}\n      #token-info-uri:  http://${fxz-gateway}:8301/auth/oauth/check_token\n      user-info-uri: http://fxz-gateway:8301/auth/user\n\n\nmanagement:\n  endpoints:\n    web:\n      exposure:\n        include: \'*\'\n  endpoint:\n    health:\n      show-details: ALWAYS\n\njasypt:\n  encryptor:\n    password: fxzcloud', '8793e9b8e8f73b2af5cedea16935f6de', '2010-05-05 00:00:00', '2022-11-24 06:44:10', NULL, '0:0:0:0:0:0:0:1', 'U', '');
INSERT INTO `his_config_info` (`id`, `nid`, `data_id`, `group_id`, `app_name`, `content`, `md5`, `gmt_create`, `gmt_modified`, `src_user`, `src_ip`, `op_type`, `tenant_id`) VALUES (189, 290, 'fxz-demos.yaml', 'DEFAULT_GROUP', '', 'server:\n  port: 8601\n\nspring:\n  data:\n    mongodb:\n      authentication-database: admin\n      database: user\n      username: admin\n      password: \"123456\"\n      host: fxz-mysql\n      port: 27017\n  rabbitmq:\n    host: fxz-rabbitmq\n    port: 5672\n    username: fxz\n    password: 123456\n\n\nsecurity:\n  oauth2:\n    resource:\n      id: ${spring.application.name}\n      #token-info-uri:  http://${fxz-gateway}:8301/auth/oauth/check_token\n      user-info-uri: http://fxz-gateway:8301/auth/user\n\n\nmanagement:\n  endpoints:\n    web:\n      exposure:\n        include: \'*\'\n  endpoint:\n    health:\n      show-details: ALWAYS\n\njasypt:\n  encryptor:\n    password: fxzcloud', '23b83cea1c5e9cc7b18ed61cf78fd248', '2010-05-05 00:00:00', '2022-11-24 07:49:04', NULL, '0:0:0:0:0:0:0:1', 'U', '');
INSERT INTO `his_config_info` (`id`, `nid`, `data_id`, `group_id`, `app_name`, `content`, `md5`, `gmt_create`, `gmt_modified`, `src_user`, `src_ip`, `op_type`, `tenant_id`) VALUES (189, 291, 'fxz-demos.yaml', 'DEFAULT_GROUP', '', 'server:\n  port: 8601\n\nspring:\n  data:\n    mongodb:\n      authentication-database: admin\n      database: user\n      username: admin\n      password: \"123456\"\n      host: fxz-mysql\n      port: 27017\n  rabbitmq:\n    host: fxz-rabbitmq\n    port: 5672\n    username: admin\n    password: 123456\n\n\nsecurity:\n  oauth2:\n    resource:\n      id: ${spring.application.name}\n      #token-info-uri:  http://${fxz-gateway}:8301/auth/oauth/check_token\n      user-info-uri: http://fxz-gateway:8301/auth/user\n\n\nmanagement:\n  endpoints:\n    web:\n      exposure:\n        include: \'*\'\n  endpoint:\n    health:\n      show-details: ALWAYS\n\njasypt:\n  encryptor:\n    password: fxzcloud', 'b1b537dc085a8704f9ed7ca3c9389023', '2010-05-05 00:00:00', '2022-11-24 03:23:48', 'nacos', '27.115.16.158', 'U', '');
INSERT INTO `his_config_info` (`id`, `nid`, `data_id`, `group_id`, `app_name`, `content`, `md5`, `gmt_create`, `gmt_modified`, `src_user`, `src_ip`, `op_type`, `tenant_id`) VALUES (135, 292, 'fxz-application-common.yaml', 'DEFAULT_GROUP', '', 'biz:\n  oss:\n    resources-url: http://localhost:8301\n    \nmybatis-plus:\n  configuration:\n    log-impl: org.apache.ibatis.logging.stdout.StdOutImpl\n\nspring:\n  redis:\n    database: 0\n    host: fxz-redis\n    port: 6379\n    password: ENC(xztexTI7xBO9qlCAXlVv+prennZO1c7q)\n    lettuce:\n      pool:\n        # 最大连接\n        min-idle: 8\n        # 最大空闲连接\n        max-idle: 500\n        max-active: 2000\n        max-wait: 10000\n    timeout: 5000 \n\n\njasypt:\n  encryptor:\n    password: fxzcloud #配置文件加密密钥\n\nfxz:\n  cloud:\n    security: \n      anonUris: /swagger-ui/**,/webjars/**,/swagger-resources/**,/csrf,/actuator/**,/swagger**/**,/**/api-docs/**,/doc.html\n  tenant:\n    ignore-urls:\n      - /user            # check-token获取用户信息 不做拦截\n      - /oauth/token     # 登录接口 不做拦截\n      - /tenant/findIdByName    # 根据租户名称获取租户Id\n      - /user/findByName/*           # 登录时根据用户名查询用户 不做拦截\n      - /user/findByMobile/*         # 登录时根据手机号查询用户 不做拦截\n      - /menu/findUserPermissions/*   # 登录时根据查询角色权限   不做拦截\n      - /tenant/validTenant/*       # 校验租户信息  不做拦截\n      - /role/getDataPermission     # 查询数据权限信息 不做拦截\n      - /operLog/add                # 保存登录日志 不做拦截\n      - /sms/sendSms                # 短信登录 不做拦截\n      - /token/*\n      - /swagger-ui/**\n      - /v3/api-docs/**\n      - /doc.html\n      - /webjars/**\n      - /app/**\n      - /demo/**\n    tables: \n      - sys_user\n      - sys_role\n  common:\n    doc:\n      author: \"fxz\"\n      version: \"0.0.1\"\n      title: \"Art开发文档\"\n      description: \"Art开发文档\"\n      services:\n        \"fxz-server-system\": system\n        \"fxz-mall-user\": user', '2ab059a4ed79212fbdaf3d59a5c10a70', '2010-05-05 00:00:00', '2022-11-27 21:15:53', 'nacos', '27.115.16.158', 'U', '');
COMMIT;

-- ----------------------------
-- Table structure for roles
-- ----------------------------
DROP TABLE IF EXISTS `roles`;
CREATE TABLE `roles` (
  `username` varchar(50) CHARACTER SET latin1 COLLATE latin1_swedish_ci NOT NULL,
  `role` varchar(50) CHARACTER SET latin1 COLLATE latin1_swedish_ci NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

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
  `gmt_create` datetime NOT NULL DEFAULT '2010-05-05 00:00:00' COMMENT '创建时间',
  `gmt_modified` datetime NOT NULL DEFAULT '2010-05-05 00:00:00' COMMENT '修改时间',
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
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3 COLLATE=utf8mb3_bin COMMENT='tenant_info';

-- ----------------------------
-- Records of tenant_info
-- ----------------------------
BEGIN;
COMMIT;

-- ----------------------------
-- Table structure for users
-- ----------------------------
DROP TABLE IF EXISTS `users`;
CREATE TABLE `users` (
  `username` varchar(50) CHARACTER SET latin1 COLLATE latin1_swedish_ci NOT NULL,
  `password` varchar(500) CHARACTER SET latin1 COLLATE latin1_swedish_ci NOT NULL,
  `enabled` tinyint(1) NOT NULL,
  PRIMARY KEY (`username`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- ----------------------------
-- Records of users
-- ----------------------------
BEGIN;
INSERT INTO `users` (`username`, `password`, `enabled`) VALUES ('nacos', '$2a$10$EuWPZHzz32dJN7jexM34MOeYirDdFAZm2kuWj7VEOJhhZkDrxfvUu', 1);
COMMIT;

SET FOREIGN_KEY_CHECKS = 1;
