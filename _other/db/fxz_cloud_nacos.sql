
SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for config_info
-- ----------------------------
DROP TABLE IF EXISTS `config_info`;
CREATE TABLE `config_info` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT 'id',
  `data_id` varchar(255) COLLATE utf8mb3_bin NOT NULL COMMENT 'data_id',
  `group_id` varchar(128) COLLATE utf8mb3_bin DEFAULT NULL,
  `content` longtext COLLATE utf8mb3_bin NOT NULL COMMENT 'content',
  `md5` varchar(32) COLLATE utf8mb3_bin DEFAULT NULL COMMENT 'md5',
  `gmt_create` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `gmt_modified` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '修改时间',
  `src_user` text COLLATE utf8mb3_bin COMMENT 'source user',
  `src_ip` varchar(50) COLLATE utf8mb3_bin DEFAULT NULL COMMENT 'source ip',
  `app_name` varchar(128) COLLATE utf8mb3_bin DEFAULT NULL,
  `tenant_id` varchar(128) COLLATE utf8mb3_bin DEFAULT '' COMMENT '租户字段',
  `c_desc` varchar(256) COLLATE utf8mb3_bin DEFAULT NULL,
  `c_use` varchar(64) COLLATE utf8mb3_bin DEFAULT NULL,
  `effect` varchar(64) COLLATE utf8mb3_bin DEFAULT NULL,
  `type` varchar(64) COLLATE utf8mb3_bin DEFAULT NULL,
  `c_schema` text COLLATE utf8mb3_bin,
  `encrypted_data_key` text CHARACTER SET utf8mb3 COLLATE utf8mb3_bin COMMENT '秘钥',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_configinfo_datagrouptenant` (`data_id`,`group_id`,`tenant_id`)
) ENGINE=InnoDB AUTO_INCREMENT=205 DEFAULT CHARSET=utf8mb3 COLLATE=utf8mb3_bin COMMENT='config_info';

-- ----------------------------
-- Records of config_info
-- ----------------------------
BEGIN;
INSERT INTO `config_info` (`id`, `data_id`, `group_id`, `content`, `md5`, `gmt_create`, `gmt_modified`, `src_user`, `src_ip`, `app_name`, `tenant_id`, `c_desc`, `c_use`, `effect`, `type`, `c_schema`, `encrypted_data_key`) VALUES (1, 'fxz-auth.yaml', 'DEFAULT_GROUP', 'server:\n  port: 3000\n\nwechat:\n  weapp:\n    appid: ENC(SxZ78YMbeLIHg1/IAmPxkVFjbxHUDPSJDsrVJMmc/ws=)\n    secret: ENC(OcF3VEu+7nyetyF7S5pTvidOfNRXT1KLsuo6h09aYUD74Hz0buJo8ONkg/Z2AiNj)\n\nspring:\n  datasource:\n    driver-class-name: com.mysql.cj.jdbc.Driver\n    username: root\n    password: ENC(bM81d5utQm25BU/ZnKFOL5Otj1+/GyCaF9+rnC9YUxA=)\n    url: jdbc:mysql://fxz-mysql/fxz_cloud_base?useUnicode=true&characterEncoding=UTF-8&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=GMT%2b8&rewriteBatchedStatements=true\n  \nmybatis-plus:\n  type-aliases-package: com.fxz.common.entity.system\n  mapper-locations: classpath:mapper/*.xml\n  configuration:\n    jdbc-type-for-null: null\n  global-config:\n    banner: false\n\ninfo:\n  app:\n    name: ${spring.application.name}\n    description: \"@project.description@\"\n    version: \"@project.version@\"\n\nmanagement:\n  endpoints:\n    web:\n      exposure:\n        include: \'*\'\n  endpoint:\n    health:\n      show-details: ALWAYS\n\nsecurity:\n  oauth2:\n    resource:\n      id: ${spring.application.name}\n      #token-info-uri:  http://${fxz-gateway}:8301/auth/oauth/check_token\n      user-info-uri: http://fxz-gateway:9999/auth/user\n\nfxz:\n  cloud:\n    security:\n      anonUris: /token/**,/**/swagger-ui/index.html', 'f2994345f2af3fd063089d2057debfde', '2021-12-16 18:39:32', '2023-02-22 13:52:40', 'nacos', '27.115.16.158', '', '', 'null', 'null', 'null', 'yaml', 'null', '');
INSERT INTO `config_info` (`id`, `data_id`, `group_id`, `content`, `md5`, `gmt_create`, `gmt_modified`, `src_user`, `src_ip`, `app_name`, `tenant_id`, `c_desc`, `c_use`, `effect`, `type`, `c_schema`, `encrypted_data_key`) VALUES (4, 'fxz-gateway.yaml', 'DEFAULT_GROUP', 'server:\n  port: 9999\nspring:\n  cloud:\n    gateway:\n      routes:\n        # - id: fxz-auth\n        #   uri: lb://fxz-auth\n        #   predicates:\n        #     - Path=/auth/**\n        # - id: fxz-server-system\n        #   uri: lb://fxz-server-system\n        #   predicates:\n        #     - Path=/system/** \n        #   #  - FxzTimeBetween=上午7:00,下午1:00 #自定义的断言工厂，模拟业务只能上午7点到下午1点访问\n        # - id: fxz-generate\n        #   uri: lb://fxz-generate\n        #   predicates:\n        #     - Path=/generate/**\n        # - id: fxz-job\n        #   uri: lb://fxz-job\n        #   predicates:\n        #     - Path=/schedule/**\n        # - id: fxz-mall-product\n        #   uri: lb://fxz-mall-product\n        #   predicates:\n        #     - Path=/product/**\n        # - id: fxz-mall-order\n        #   uri: lb://fxz-mall-order\n        #   predicates:\n        #     - Path=/order/**\n        # - id: fxz-mall-user\n        #   uri: lb://fxz-mall-user\n        #   predicates:\n        #     - Path=/user/**\n        # - id: fxz-mall-search\n        #   uri: lb://fxz-mall-search\n        #   predicates:\n        #     - Path=/search/**\n        # - id: fxz-mall-promotion\n        #   uri: lb://fxz-mall-promotion\n        #   predicates:\n        #     - Path=/promotion/**\n      default-filters:\n        - StripPrefix=1\n\n\nmanagement:\n  endpoint:\n    health:\n      show-details: ALWAYS\n  endpoints:\n    web:\n      exposure:\n        include: health,info,gateway\n\naj:\n  captcha:\n    cache-type: redis\n ', 'cbbf671074f4d68191f94694de616747', '2021-12-16 18:52:35', '2022-10-22 16:06:20', NULL, '0:0:0:0:0:0:0:1', '', '', 'null', 'null', 'null', 'yaml', 'null', NULL);
INSERT INTO `config_info` (`id`, `data_id`, `group_id`, `content`, `md5`, `gmt_create`, `gmt_modified`, `src_user`, `src_ip`, `app_name`, `tenant_id`, `c_desc`, `c_use`, `effect`, `type`, `c_schema`, `encrypted_data_key`) VALUES (6, 'fxz-server-system.yaml', 'DEFAULT_GROUP', 'server:\n  port: 4000\n\nspring:\n  datasource:\n    driver-class-name: com.mysql.cj.jdbc.Driver\n    username: root\n    password: ENC(bM81d5utQm25BU/ZnKFOL5Otj1+/GyCaF9+rnC9YUxA=)\n    url: jdbc:mysql://fxz-mysql/fxz_cloud_base?useUnicode=true&characterEncoding=UTF-8&rewriteBatchedStatements=true&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=GMT%2b8\n \n   # RabbitMQ 配置\n  rabbitmq:\n    host: fxz-rabbitmq\n    port: 5672\n    username: fxz\n    password: 123456\n    # 动态创建和绑定队列、交换机的配置\n    modules: \n      # 订单延时队列，到了过期的时间会被转发到订单取消队列\n      - routing-key: canal_key\n        queue:\n          name: canal_queue\n        exchange:\n          name: canal_exchange\n  autoconfigure:\n    exclude: org.springframework.cloud.sleuth.instrument.hystrix.SleuthHystrixAutoConfiguration\n\nsecurity:\n  oauth2:\n    resource:\n      id: ${spring.application.name}\n      #token-info-uri:  http://${fxz-gateway}:9999/auth/oauth/check_token\n      user-info-uri: http://fxz-gateway:9999/auth/user\n\nmybatis-plus:\n  type-aliases-package: com.fxz.common.core.entity.system\n  mapper-locations: classpath:mapper/*/*.xml\n  configuration:\n    jdbc-type-for-null: null\n  global-config:\n    banner: false\n\ninfo:\n  app:\n    name: ${spring.application.name}\n    description: \"@project.description@\"\n    version: \"@project.version@\"\n\nmanagement:\n  endpoints:\n    web:\n      exposure:\n        include: \'*\'\n  endpoint:\n    health:\n      show-details: ALWAYS\n\nfeign:\n  hystrix:\n    enabled: true\n\nhystrix:\n  shareSecurityContext: true\n\nfxz:\n  cloud:\n    security: \n      anonUris: /find,/swagger-ui/**,/webjars/**,/swagger-resources/**,/v2/api-docs/**,/,/csrf,/actuator/**,/user/findByName/*,/menu/findUserPermissions/**,/user/test,/kk-anti-reptile/**,/swagger**/**,/**/api-docs/**,/doc.html\n  common:\n    sequence:\n      type: redis\n      \n# 文件系统\nfile:\n  storage:\n    oss:\n      endpoint: http://fxz-oss:9000\n      access-key: admin\n      secret-key: admin123456\n      bucketName: fxzcloud\n\naliyun:\n  sms:\n    accessKeyId: LTAI5tJymbcizqk5nnVvQUU6\n    accessKeySecret: 9iszXlrnjoXuLLWIXFiaFCzWg2ttW2\n    domain: dysmsapi.aliyuncs.com\n    regionId: cn-hangzhou\n    templateCode: SMS_154950909\n    signName: 阿里云短信测试\n', '532655b251c715d2a582fb96618f7d34', '2021-12-17 19:01:56', '2023-02-22 13:51:16', 'nacos', '27.115.16.158', '', '', 'null', 'null', 'null', 'yaml', 'null', '');
INSERT INTO `config_info` (`id`, `data_id`, `group_id`, `content`, `md5`, `gmt_create`, `gmt_modified`, `src_user`, `src_ip`, `app_name`, `tenant_id`, `c_desc`, `c_use`, `effect`, `type`, `c_schema`, `encrypted_data_key`) VALUES (70, 'fxz-generate.yaml', 'DEFAULT_GROUP', 'server:\n  port: 5001\n\nspring:\n  datasource:\n    driver-class-name: com.mysql.cj.jdbc.Driver\n    username: root\n    password: fxzcloud-mysql-password\n    url: jdbc:mysql://fxz-mysql/fxz_cloud_codegen?useUnicode=true&characterEncoding=UTF-8&rewriteBatchedStatements=true&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=GMT%2b8\n\n  \nsecurity:\n  oauth2:\n    resource:\n      id: ${spring.application.name}\n      user-info-uri: http://fxz-gateway:9999/auth/user\n\nmybatis-plus:\n  mapper-locations: classpath:mapper/*/*.xml\n  configuration:\n    jdbc-type-for-null: null\n  global-config:\n    banner: false', 'd7a472297537b463f05c83b550f24fcc', '2022-03-31 11:02:50', '2023-02-22 13:51:03', 'nacos', '27.115.16.158', '', '', 'null', 'null', 'null', 'yaml', 'null', '');
INSERT INTO `config_info` (`id`, `data_id`, `group_id`, `content`, `md5`, `gmt_create`, `gmt_modified`, `src_user`, `src_ip`, `app_name`, `tenant_id`, `c_desc`, `c_use`, `effect`, `type`, `c_schema`, `encrypted_data_key`) VALUES (80, 'fxz-job.yaml', 'DEFAULT_GROUP', 'server:\n  port: 5002\n\n\nspring:\n  datasource:\n    driver-class-name: com.mysql.cj.jdbc.Driver\n    username: root\n    password: ENC(bM81d5utQm25BU/ZnKFOL5Otj1+/GyCaF9+rnC9YUxA=)\n    url: jdbc:mysql://fxz-mysql/fxz_cloud_job?useUnicode=true&characterEncoding=UTF-8&rewriteBatchedStatements=true&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=GMT%2b8\n  quartz:\n    auto-startup: true # quartz启动开关\n    scheduler-name: schedulerName # 默认的schedulerName\n    job-store-type: jdbc # jdbc存储 默认内存\n    overwrite-existing-jobs: true # 启动时更新已经存在的job\n    wait-for-jobs-to-complete-on-shutdown: true # 应用关闭时 是否等待定时任务执行完成 默认为 false\n    properties:\n      org:\n        quartz:\n          scheduler:\n            instanceName: schedulerName # 执行器名称\n            instanceId: AUTO # 自动生成id\n          jobStore:\n            class: org.springframework.scheduling.quartz.LocalDataSourceJobStore\n            isClustered: true # 集群模式\n            clusterCheckinInterval: 15000 # 集群检查频率 单位：毫秒\n            misfireThreshold: 60000 # misfire 阀值 单位：毫秒\n          threadPool:\n            threadCount: 25 # 线程池大小\n            threadPriority: 5 # 线程优先级\n            threadsInheritContextClassLoaderOfInitializingThread: true\n            class: org.quartz.simpl.SimpleThreadPool # 线程池类型\n    jdbc:\n      initialize-schema: never #  是否自动初始化Quartz表 我们手动创建表\n\nsecurity:\n  oauth2:\n    resource:\n      id: ${spring.application.name}\n      user-info-uri: http://fxz-gateway:9999/auth/user\n\n\nmybatis-plus:\n  mapper-locations: classpath:mapper/*/*.xml\n  configuration:\n    jdbc-type-for-null: null\n  global-config:\n    banner: false', '1052650821447c226100616cf1f7ba66', '2022-04-03 17:45:50', '2023-02-22 13:51:54', 'nacos', '27.115.16.158', '', '', 'null', 'null', 'null', 'yaml', 'null', '');
INSERT INTO `config_info` (`id`, `data_id`, `group_id`, `content`, `md5`, `gmt_create`, `gmt_modified`, `src_user`, `src_ip`, `app_name`, `tenant_id`, `c_desc`, `c_use`, `effect`, `type`, `c_schema`, `encrypted_data_key`) VALUES (102, 'fxz-mall-product.yaml', 'DEFAULT_GROUP', 'server:\n  port: 6001\n\nspring:\n  cache:\n    type: redis\n  datasource:\n    driver-class-name: com.mysql.cj.jdbc.Driver\n    username: root\n    password: ENC(bM81d5utQm25BU/ZnKFOL5Otj1+/GyCaF9+rnC9YUxA=)\n    url: jdbc:mysql://fxz-mysql/fxz_mall_product?useUnicode=true&characterEncoding=UTF-8&rewriteBatchedStatements=true&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=GMT%2b8\n  autoconfigure:\n    exclude: org.springframework.cloud.sleuth.instrument.hystrix.SleuthHystrixAutoConfiguration\n\nsecurity:\n  oauth2:\n    resource:\n      id: ${spring.application.name}\n      user-info-uri: http://fxz-gateway:9999/auth/user\n\nmybatis-plus:\n  type-aliases-package: com.fxz.common.core.entity.system\n  mapper-locations: classpath:mapper/*/*.xml\n  configuration:\n    jdbc-type-for-null: null\n  global-config:\n    banner: false\n\ninfo:\n  app:\n    name: ${spring.application.name}\n    description: \"@project.description@\"\n    version: \"@project.version@\"\n\nmanagement:\n  endpoints:\n    web:\n      exposure:\n        include: \'*\'\n  endpoint:\n    health:\n      show-details: ALWAYS\n\nfeign:\n  hystrix:\n    enabled: true\n\nhystrix:\n  shareSecurityContext: true\n\nfxz:\n  cloud:\n    security: \n      anonUris: /find,/swagger-ui.html,/webjars/**,/swagger-resources/**,/v2/api-docs/**,/,/csrf,/actuator/**,/user/findByName/*,/menu/findUserPermissions/**,/user/test,/kk-anti-reptile/**\n\n# 文件系统\noss:\n  endpoint: http://fxz-oss:9000\n  access-key: admin\n  secret-key: admin123456\n  bucketName: fxzcloud', '15f40b9bdbc4c1af7e0332d70c313703', '2022-05-05 12:12:57', '2023-02-22 13:53:24', 'nacos', '27.115.16.158', '', '', '', '', '', 'yaml', '', '');
INSERT INTO `config_info` (`id`, `data_id`, `group_id`, `content`, `md5`, `gmt_create`, `gmt_modified`, `src_user`, `src_ip`, `app_name`, `tenant_id`, `c_desc`, `c_use`, `effect`, `type`, `c_schema`, `encrypted_data_key`) VALUES (108, 'fxz-mall-order.yaml', 'DEFAULT_GROUP', 'server:\n  port: 6101\n\nspring:\n  datasource:\n    driver-class-name: com.mysql.cj.jdbc.Driver\n    username: root\n    password: ENC(bM81d5utQm25BU/ZnKFOL5Otj1+/GyCaF9+rnC9YUxA=)\n    url: jdbc:mysql://fxz-mysql/fxz_mall_order?useUnicode=true&characterEncoding=UTF-8&rewriteBatchedStatements=true&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=GMT%2b8\n  # RabbitMQ 配置\n  rabbitmq:\n    host: fxz-rabbitmq\n    port: 5672\n    username: fxz\n    password: 123456\n    # 动态创建和绑定队列、交换机的配置\n    modules: \n      # 订单延时队列，到了过期的时间会被转发到订单取消队列\n      - routing-key: order.create.routing.key\n        queue:\n          name: order.delay.queue\n          dead-letter-exchange: order.exchange\n          dead-letter-routing-key: order.close.routing.key\n          arguments:\n            # 1分钟(测试)，单位毫秒,没有被消费则进入死信队列\n            x-message-ttl: 60000 \n        exchange:\n          name: order.exchange\n      # 订单取消队列\n      - routing-key: order.close.routing.key\n        queue:\n          name: order.close.queue\n        exchange:\n          name: order.exchange\n  redis:\n    database: 0\n    host: fxz-redis\n    port: 6379\n    password: fxzcloud-redis\n    lettuce:\n      pool:\n        min-idle: 8\n        max-idle: 500\n        max-active: 2000\n        max-wait: 10000\n    timeout: 5000  \n  autoconfigure:\n    exclude: org.springframework.cloud.sleuth.instrument.hystrix.SleuthHystrixAutoConfiguration\n\nsecurity:\n  oauth2:\n    resource:\n      id: ${spring.application.name}\n      user-info-uri: http://fxz-gateway:9999/auth/user\n\nmybatis-plus:\n  type-aliases-package: com.fxz.common.core.entity.system\n  mapper-locations: classpath:mapper/*/*.xml\n  configuration:\n    jdbc-type-for-null: null\n  global-config:\n    banner: false\n\ninfo:\n  app:\n    name: ${spring.application.name}\n    description: \"@project.description@\"\n    version: \"@project.version@\"\n\nmanagement:\n  endpoints:\n    web:\n      exposure:\n        include: \'*\'\n  endpoint:\n    health:\n      show-details: ALWAYS\n\nfeign:\n  hystrix:\n    enabled: true\n\nhystrix:\n  shareSecurityContext: true\n\nfxz:\n  cloud:\n    security: \n      anonUris: /find,/swagger-ui.html,/webjars/**,/swagger-resources/**,/v2/api-docs/**,/,/csrf,/actuator/**,/user/findByName/*,/menu/findUserPermissions/**,/user/test,/kk-anti-reptile/**\n\n# 文件系统\noss:\n  endpoint: http://fxz-oss:9000\n  access-key: admin\n  secret-key: admin123456\n  bucketName: fxzcloud', '70dbe83aa8877aadd4717283f15cdec2', '2022-05-13 08:42:53', '2023-02-22 13:53:37', 'nacos', '27.115.16.158', '', '', '', '', '', 'yaml', '', '');
INSERT INTO `config_info` (`id`, `data_id`, `group_id`, `content`, `md5`, `gmt_create`, `gmt_modified`, `src_user`, `src_ip`, `app_name`, `tenant_id`, `c_desc`, `c_use`, `effect`, `type`, `c_schema`, `encrypted_data_key`) VALUES (110, 'fxz-mall-user.yaml', 'DEFAULT_GROUP', 'server:\n  port: 6201\n\nspring:\n  datasource:\n    driver-class-name: com.mysql.cj.jdbc.Driver\n    username: root\n    password: ENC(bM81d5utQm25BU/ZnKFOL5Otj1+/GyCaF9+rnC9YUxA=)\n    url: jdbc:mysql://fxz-mysql/fxz_mall_user?useUnicode=true&characterEncoding=UTF-8&rewriteBatchedStatements=true&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=GMT%2b8 \n  autoconfigure:\n    exclude: org.springframework.cloud.sleuth.instrument.hystrix.SleuthHystrixAutoConfiguration\n\nsecurity:\n  oauth2:\n    resource:\n      id: ${spring.application.name}\n      user-info-uri: http://fxz-gateway:9999/auth/user\n\nmybatis-plus:\n  type-aliases-package: com.fxz.common.core.entity.system\n  mapper-locations: classpath:mapper/*/*.xml\n  configuration:\n    jdbc-type-for-null: null\n  global-config:\n    banner: false\n\ninfo:\n  app:\n    name: ${spring.application.name}\n    description: \"@project.description@\"\n    version: \"@project.version@\"\n\nmanagement:\n  endpoints:\n    web:\n      exposure:\n        include: \'*\'\n  endpoint:\n    health:\n      show-details: ALWAYS\n\nfeign:\n  hystrix:\n    enabled: true\n\nhystrix:\n  shareSecurityContext: true\n\nfxz:\n  cloud:\n    security: \n      anonUris: /find,/swagger-ui.html,/webjars/**,/swagger-resources/**,/v2/api-docs/**,/,/csrf,/actuator/**,/user/findByName/*,/menu/findUserPermissions/**,/user/test,/kk-anti-reptile/**\n', '41d9e9bfb5f0ca12ce319b2fdd3d8334', '2022-05-14 03:09:20', '2023-02-22 13:53:47', 'nacos', '27.115.16.158', '', '', '', '', '', 'yaml', '', '');
INSERT INTO `config_info` (`id`, `data_id`, `group_id`, `content`, `md5`, `gmt_create`, `gmt_modified`, `src_user`, `src_ip`, `app_name`, `tenant_id`, `c_desc`, `c_use`, `effect`, `type`, `c_schema`, `encrypted_data_key`) VALUES (126, 'fxz-mall-search.yaml', 'DEFAULT_GROUP', 'server:\n  port: 6301\n\nspring:\n   # RabbitMQ 配置\n  rabbitmq:\n    host: fxz-rabbitmq\n    port: 5672\n    username: fxz\n    password: 123456\n    # 动态创建和绑定队列、交换机的配置\n    modules: \n      # 订单延时队列，到了过期的时间会被转发到订单取消队列\n      - routing-key: canal_key\n        queue:\n          name: canal_queue\n        exchange:\n          name: canal_exchange\n\nsecurity:\n  oauth2:\n    resource:\n      id: ${spring.application.name}\n      user-info-uri: http://fxz-gateway:9999/auth/user\n\nmybatis-plus:\n  type-aliases-package: com.fxz.common.core.entity.system\n  mapper-locations: classpath:mapper/*/*.xml\n  configuration:\n    jdbc-type-for-null: null\n  global-config:\n    banner: false\n\ninfo:\n  app:\n    name: ${spring.application.name}\n    description: \"@project.description@\"\n    version: \"@project.version@\"\n\nmanagement:\n  endpoints:\n    web:\n      exposure:\n        include: \'*\'\n  endpoint:\n    health:\n      show-details: ALWAYS\n\nfeign:\n  hystrix:\n    enabled: true\n\nhystrix:\n  shareSecurityContext: true\n\nfxz:\n  cloud:\n    security: \n      anonUris: /find,/swagger-ui.html,/webjars/**,/swagger-resources/**,/v2/api-docs/**,/,/csrf,/actuator/**,/user/findByName/*,/menu/findUserPermissions/**,/user/test,/kk-anti-reptile/**\n', '52d910a156d712e0593e426e1fd2a389', '2022-05-27 14:03:25', '2023-02-22 13:53:57', 'nacos', '27.115.16.158', '', '', '', '', '', 'yaml', '', '');
INSERT INTO `config_info` (`id`, `data_id`, `group_id`, `content`, `md5`, `gmt_create`, `gmt_modified`, `src_user`, `src_ip`, `app_name`, `tenant_id`, `c_desc`, `c_use`, `effect`, `type`, `c_schema`, `encrypted_data_key`) VALUES (135, 'fxz-application-common.yaml', 'DEFAULT_GROUP', 'art:\n  name: art\n  author: fxz\n  version: 0.0.1\n\nbiz:\n  oss:\n    resources-url: http://localhost:8301\n    \nmybatis-plus:\n  configuration:\n    log-impl: org.apache.ibatis.logging.stdout.StdOutImpl\n\nspring:\n  redis:\n    database: 0\n    host: fxz-redis\n    port: 6379\n    password: ENC(xztexTI7xBO9qlCAXlVv+prennZO1c7q)\n    lettuce:\n      pool:\n        # 连接池中的最小空闲连接\n        min-idle: 8\n        # 连接池中的最大空闲连接\n        max-idle: 500\n        # 连接池最大连接数（使用负值表示没有限制）\n        max-active: 2000\n        # 连接池最大阻塞等待时间（使用负值表示没有限制）\n        max-wait: -1\n    # 连接超时时长（毫秒）\n    timeout: 10000 \n\njasypt:\n  encryptor:\n    password: fxzcloud #配置文件加密密钥\n\nfxz:\n  cloud:\n    security: \n      anonUris: /swagger-ui/**,/webjars/**,/swagger-resources/**,/csrf,/actuator/**,/swagger**/**,/**/api-docs/**,/doc.html\n  tenant:\n    ignore-urls:\n      - /user            # check-token获取用户信息 不做拦截\n      - /oauth/token     # 登录接口 不做拦截\n      - /tenant/findIdByName    # 根据租户名称获取租户Id\n      - /user/findByName/*           # 登录时根据用户名查询用户 不做拦截\n      - /user/findByMobile/*         # 登录时根据手机号查询用户 不做拦截\n      - /menu/findUserPermissions/*   # 登录时根据查询角色权限   不做拦截\n      - /tenant/validTenant/*       # 校验租户信息  不做拦截\n      - /role/getDataPermission     # 查询数据权限信息 不做拦截\n      - /operLog/add                # 保存登录日志 不做拦截\n      - /sms/sendSms                # 短信登录 不做拦截\n      - /token/*\n      - /swagger-ui/**\n      - /v3/api-docs/**\n      - /doc.html\n      - /webjars/**\n      - /app/**\n      - /demo/**\n    tables: \n      - sys_user\n      - sys_role\n  common:\n    doc:\n      author: \"fxz\"\n      version: \"0.0.1\"\n      title: \"Art开发文档\"\n      description: \"Art开发文档\"\n      services:\n        \"fxz-server-system\": system\n        \"fxz-mall-user\": user', '563d724c45b1d9c1648203648249b67d', '2022-06-26 12:10:13', '2022-12-05 07:54:32', 'nacos', '27.115.16.158', '', '', '', '', '', 'yaml', '', NULL);
INSERT INTO `config_info` (`id`, `data_id`, `group_id`, `content`, `md5`, `gmt_create`, `gmt_modified`, `src_user`, `src_ip`, `app_name`, `tenant_id`, `c_desc`, `c_use`, `effect`, `type`, `c_schema`, `encrypted_data_key`) VALUES (152, 'fxz-server-system-flow.yaml', 'DEFAULT_GROUP', '[\n    {\n        \"resource\": \"/test\",\n        \"limitApp\": \"default\",\n        \"grade\": 1,\n        \"count\": 1,\n        \"strategy\": 0,\n        \"controlBehavior\": 0,\n        \"clusterMode\": false\n    }\n]\n', '60b3d58e978e05ac12df555f11fc2132', '2022-07-28 03:09:23', '2022-07-28 03:12:42', NULL, '0:0:0:0:0:0:0:1', '', '', '', '', '', 'json', '', NULL);
INSERT INTO `config_info` (`id`, `data_id`, `group_id`, `content`, `md5`, `gmt_create`, `gmt_modified`, `src_user`, `src_ip`, `app_name`, `tenant_id`, `c_desc`, `c_use`, `effect`, `type`, `c_schema`, `encrypted_data_key`) VALUES (162, 'fxz-mall-promotion.yaml', 'DEFAULT_GROUP', 'server:\n  port: 6401\n\nspring:\n  datasource:\n    driver-class-name: com.mysql.cj.jdbc.Driver\n    username: root\n    password: ENC(bM81d5utQm25BU/ZnKFOL5Otj1+/GyCaF9+rnC9YUxA=)\n    url: jdbc:mysql://fxz-mysql/fxz_mall_promotion?useUnicode=true&characterEncoding=UTF-8&rewriteBatchedStatements=true&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=GMT%2b8\n  autoconfigure:\n    exclude: org.springframework.cloud.sleuth.instrument.hystrix.SleuthHystrixAutoConfiguration\n\nsecurity:\n  oauth2:\n    resource:\n      id: ${spring.application.name}\n      #token-info-uri:  http://${fxz-gateway}:8301/auth/oauth/check_token\n      user-info-uri: http://fxz-gateway:9999/auth/user\n\nmybatis-plus:\n  type-aliases-package: com.fxz.common.core.entity.system\n  mapper-locations: classpath:mapper/*/*.xml\n  configuration:\n    jdbc-type-for-null: null\n  global-config:\n    banner: false\n\ninfo:\n  app:\n    name: ${spring.application.name}\n    description: \"@project.description@\"\n    version: \"@project.version@\"\n\nmanagement:\n  endpoints:\n    web:\n      exposure:\n        include: \'*\'\n  endpoint:\n    health:\n      show-details: ALWAYS\n\nfeign:\n  hystrix:\n    enabled: true\n\nhystrix:\n  shareSecurityContext: true\n\nfxz:\n  cloud:\n    security: \n      anonUris: /find,/swagger-ui.html,/webjars/**,/swagger-resources/**,/v2/api-docs/**,/,/csrf,/actuator/**,/user/findByName/*,/menu/findUserPermissions/**,/user/test,/kk-anti-reptile/**\n\n# 文件系统\noss:\n  endpoint: http://fxz-oss:9000\n  access-key: admin\n  secret-key: admin123456\n  bucketName: fxzcloud', 'deff2fc119a200e963663c589680386f', '2022-08-09 03:41:45', '2023-02-22 13:54:12', 'nacos', '27.115.16.158', '', '', '', '', '', 'yaml', '', '');
INSERT INTO `config_info` (`id`, `data_id`, `group_id`, `content`, `md5`, `gmt_create`, `gmt_modified`, `src_user`, `src_ip`, `app_name`, `tenant_id`, `c_desc`, `c_use`, `effect`, `type`, `c_schema`, `encrypted_data_key`) VALUES (171, 'fxz-mall-xxl-job.yaml', 'DEFAULT_GROUP', 'server:\n  port: 6501\n   \nfxz:\n  common:\n    xxl-job:\n      appname: ${spring.application.name}\n      admin-addresses: http://127.0.0.1:9003/xxl-job-admin\n      port: 9900\n\nsecurity:\n  oauth2:\n    resource:\n      id: ${spring.application.name}\n      user-info-uri: http://fxz-gateway:9999/auth/user\n\nmybatis-plus:\n  type-aliases-package: com.fxz.common.core.entity.system\n  mapper-locations: classpath:mapper/*/*.xml\n  configuration:\n    jdbc-type-for-null: null\n  global-config:\n    banner: false\n\ninfo:\n  app:\n    name: ${spring.application.name}\n    description: \"@project.description@\"\n    version: \"@project.version@\"\n\nmanagement:\n  endpoints:\n    web:\n      exposure:\n        include: \'*\'\n  endpoint:\n    health:\n      show-details: ALWAYS\n\n\n', '96accb5f1295526db42cc062cce8da51', '2022-08-14 06:52:26', '2023-02-22 13:51:36', 'nacos', '27.115.16.158', '', '', '', '', '', 'yaml', '', '');
INSERT INTO `config_info` (`id`, `data_id`, `group_id`, `content`, `md5`, `gmt_create`, `gmt_modified`, `src_user`, `src_ip`, `app_name`, `tenant_id`, `c_desc`, `c_use`, `effect`, `type`, `c_schema`, `encrypted_data_key`) VALUES (189, 'fxz-demos.yaml', 'DEFAULT_GROUP', 'server:\n  port: 5003\n\nspring:\n  datasource:\n    driver-class-name: com.mysql.cj.jdbc.Driver\n    username: root\n    password: ENC(bM81d5utQm25BU/ZnKFOL5Otj1+/GyCaF9+rnC9YUxA=)\n    url: jdbc:mysql://fxz-mysql/fxz_cloud_base?useUnicode=true&characterEncoding=UTF-8&rewriteBatchedStatements=true&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=GMT%2b8\n  data:\n    mongodb:\n      authentication-database: admin\n      database: user\n      username: admin\n      password: \"123456\"\n      host: fxz-mysql\n      port: 27017\n  rabbitmq:\n    host: fxz-rabbitmq\n    port: 5672\n    username: fxz\n    password: 123456\n\n\nsecurity:\n  oauth2:\n    resource:\n      id: ${spring.application.name}\n      #token-info-uri:  http://${fxz-gateway}:8301/auth/oauth/check_token\n      user-info-uri: http://fxz-gateway:8301/auth/user\n\n\nmanagement:\n  endpoints:\n    web:\n      exposure:\n        include: \'*\'\n  endpoint:\n    health:\n      show-details: ALWAYS\n\njasypt:\n  encryptor:\n    password: fxzcloud\n\n# 文件系统\nfile:\n  storage:\n    oss:\n      endpoint: http://fxz-oss:9000\n      access-key: admin\n      secret-key: admin123456\n      bucketName: fxzcloud', '891f2237dab719117658383752609bdb', '2022-08-30 14:20:58', '2023-01-17 00:05:38', '', '112.38.48.135', '', '', '', '', '', 'yaml', '', '');
COMMIT;

-- ----------------------------
-- Table structure for config_info_aggr
-- ----------------------------
DROP TABLE IF EXISTS `config_info_aggr`;
CREATE TABLE `config_info_aggr` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT 'id',
  `data_id` varchar(255) COLLATE utf8mb3_bin NOT NULL COMMENT 'data_id',
  `group_id` varchar(128) COLLATE utf8mb3_bin NOT NULL COMMENT 'group_id',
  `datum_id` varchar(255) COLLATE utf8mb3_bin NOT NULL COMMENT 'datum_id',
  `content` longtext COLLATE utf8mb3_bin NOT NULL COMMENT '内容',
  `gmt_modified` datetime NOT NULL COMMENT '修改时间',
  `app_name` varchar(128) COLLATE utf8mb3_bin DEFAULT NULL,
  `tenant_id` varchar(128) COLLATE utf8mb3_bin DEFAULT '' COMMENT '租户字段',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_configinfoaggr_datagrouptenantdatum` (`data_id`,`group_id`,`tenant_id`,`datum_id`)
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
  `data_id` varchar(255) COLLATE utf8mb3_bin NOT NULL COMMENT 'data_id',
  `group_id` varchar(128) COLLATE utf8mb3_bin NOT NULL COMMENT 'group_id',
  `app_name` varchar(128) COLLATE utf8mb3_bin DEFAULT NULL COMMENT 'app_name',
  `content` longtext COLLATE utf8mb3_bin NOT NULL COMMENT 'content',
  `beta_ips` varchar(1024) COLLATE utf8mb3_bin DEFAULT NULL COMMENT 'betaIps',
  `md5` varchar(32) COLLATE utf8mb3_bin DEFAULT NULL COMMENT 'md5',
  `gmt_create` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `gmt_modified` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '修改时间',
  `src_user` text COLLATE utf8mb3_bin COMMENT 'source user',
  `src_ip` varchar(50) COLLATE utf8mb3_bin DEFAULT NULL COMMENT 'source ip',
  `tenant_id` varchar(128) COLLATE utf8mb3_bin DEFAULT '' COMMENT '租户字段',
  `encrypted_data_key` text COLLATE utf8mb3_bin NOT NULL COMMENT '秘钥',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_configinfobeta_datagrouptenant` (`data_id`,`group_id`,`tenant_id`)
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
  `data_id` varchar(255) COLLATE utf8mb3_bin NOT NULL COMMENT 'data_id',
  `group_id` varchar(128) COLLATE utf8mb3_bin NOT NULL COMMENT 'group_id',
  `tenant_id` varchar(128) COLLATE utf8mb3_bin DEFAULT '' COMMENT 'tenant_id',
  `tag_id` varchar(128) COLLATE utf8mb3_bin NOT NULL COMMENT 'tag_id',
  `app_name` varchar(128) COLLATE utf8mb3_bin DEFAULT NULL COMMENT 'app_name',
  `content` longtext COLLATE utf8mb3_bin NOT NULL COMMENT 'content',
  `md5` varchar(32) COLLATE utf8mb3_bin DEFAULT NULL COMMENT 'md5',
  `gmt_create` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `gmt_modified` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '修改时间',
  `src_user` text COLLATE utf8mb3_bin COMMENT 'source user',
  `src_ip` varchar(50) COLLATE utf8mb3_bin DEFAULT NULL COMMENT 'source ip',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_configinfotag_datagrouptenanttag` (`data_id`,`group_id`,`tenant_id`,`tag_id`)
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
  `tag_name` varchar(128) COLLATE utf8mb3_bin NOT NULL COMMENT 'tag_name',
  `tag_type` varchar(64) COLLATE utf8mb3_bin DEFAULT NULL COMMENT 'tag_type',
  `data_id` varchar(255) COLLATE utf8mb3_bin NOT NULL COMMENT 'data_id',
  `group_id` varchar(128) COLLATE utf8mb3_bin NOT NULL COMMENT 'group_id',
  `tenant_id` varchar(128) COLLATE utf8mb3_bin DEFAULT '' COMMENT 'tenant_id',
  `nid` bigint NOT NULL AUTO_INCREMENT,
  PRIMARY KEY (`nid`),
  UNIQUE KEY `uk_configtagrelation_configidtag` (`id`,`tag_name`,`tag_type`),
  KEY `idx_tenant_id` (`tenant_id`)
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
  `group_id` varchar(128) COLLATE utf8mb3_bin NOT NULL DEFAULT '' COMMENT 'Group ID，空字符表示整个集群',
  `quota` int unsigned NOT NULL DEFAULT '0' COMMENT '配额，0表示使用默认值',
  `usage` int unsigned NOT NULL DEFAULT '0' COMMENT '使用量',
  `max_size` int unsigned NOT NULL DEFAULT '0' COMMENT '单个配置大小上限，单位为字节，0表示使用默认值',
  `max_aggr_count` int unsigned NOT NULL DEFAULT '0' COMMENT '聚合子配置最大个数，，0表示使用默认值',
  `max_aggr_size` int unsigned NOT NULL DEFAULT '0' COMMENT '单个聚合数据的子配置大小上限，单位为字节，0表示使用默认值',
  `max_history_count` int unsigned NOT NULL DEFAULT '0' COMMENT '最大变更历史数量',
  `gmt_create` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `gmt_modified` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '修改时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_group_id` (`group_id`)
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
  `data_id` varchar(255) COLLATE utf8mb3_bin NOT NULL,
  `group_id` varchar(128) COLLATE utf8mb3_bin NOT NULL,
  `app_name` varchar(128) COLLATE utf8mb3_bin DEFAULT NULL COMMENT 'app_name',
  `content` longtext COLLATE utf8mb3_bin NOT NULL,
  `md5` varchar(32) COLLATE utf8mb3_bin DEFAULT NULL,
  `gmt_create` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `gmt_modified` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `src_user` text COLLATE utf8mb3_bin,
  `src_ip` varchar(50) COLLATE utf8mb3_bin DEFAULT NULL,
  `op_type` char(10) COLLATE utf8mb3_bin DEFAULT NULL,
  `tenant_id` varchar(128) COLLATE utf8mb3_bin DEFAULT '' COMMENT '租户字段',
  `encrypted_data_key` text COLLATE utf8mb3_bin NOT NULL COMMENT '秘钥',
  PRIMARY KEY (`nid`),
  KEY `idx_gmt_create` (`gmt_create`),
  KEY `idx_gmt_modified` (`gmt_modified`),
  KEY `idx_did` (`data_id`)
) ENGINE=InnoDB AUTO_INCREMENT=16 DEFAULT CHARSET=utf8mb3 COLLATE=utf8mb3_bin COMMENT='多租户改造';

-- ----------------------------
-- Records of his_config_info
-- ----------------------------
BEGIN;
INSERT INTO `his_config_info` (`id`, `nid`, `data_id`, `group_id`, `app_name`, `content`, `md5`, `gmt_create`, `gmt_modified`, `src_user`, `src_ip`, `op_type`, `tenant_id`, `encrypted_data_key`) VALUES (6, 4, 'fxz-server-system.yaml', 'DEFAULT_GROUP', '', 'server:\n  port: 3000\n\nspring:\n  datasource:\n    driver-class-name: com.mysql.cj.jdbc.Driver\n    username: root\n    password: ENC(bM81d5utQm25BU/ZnKFOL5Otj1+/GyCaF9+rnC9YUxA=)\n    url: jdbc:mysql://fxz-mysql/fxz_cloud_base?useUnicode=true&characterEncoding=UTF-8&rewriteBatchedStatements=true&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=GMT%2b8\n \n   # RabbitMQ 配置\n  rabbitmq:\n    host: fxz-rabbitmq\n    port: 5672\n    username: fxz\n    password: 123456\n    # 动态创建和绑定队列、交换机的配置\n    modules: \n      # 订单延时队列，到了过期的时间会被转发到订单取消队列\n      - routing-key: canal_key\n        queue:\n          name: canal_queue\n        exchange:\n          name: canal_exchange\n  autoconfigure:\n    exclude: org.springframework.cloud.sleuth.instrument.hystrix.SleuthHystrixAutoConfiguration\n\nsecurity:\n  oauth2:\n    resource:\n      id: ${spring.application.name}\n      #token-info-uri:  http://${fxz-gateway}:8301/auth/oauth/check_token\n      user-info-uri: http://fxz-gateway:8301/auth/user\n\nmybatis-plus:\n  type-aliases-package: com.fxz.common.core.entity.system\n  mapper-locations: classpath:mapper/*/*.xml\n  configuration:\n    jdbc-type-for-null: null\n  global-config:\n    banner: false\n\ninfo:\n  app:\n    name: ${spring.application.name}\n    description: \"@project.description@\"\n    version: \"@project.version@\"\n\nmanagement:\n  endpoints:\n    web:\n      exposure:\n        include: \'*\'\n  endpoint:\n    health:\n      show-details: ALWAYS\n\nfeign:\n  hystrix:\n    enabled: true\n\nhystrix:\n  shareSecurityContext: true\n\nfxz:\n  cloud:\n    security: \n      anonUris: /find,/swagger-ui/**,/webjars/**,/swagger-resources/**,/v2/api-docs/**,/,/csrf,/actuator/**,/user/findByName/*,/menu/findUserPermissions/**,/user/test,/kk-anti-reptile/**,/swagger**/**,/**/api-docs/**,/doc.html\n  common:\n    sequence:\n      type: redis\n      \n# 文件系统\nfile:\n  storage:\n    oss:\n      endpoint: http://fxz-oss:9000\n      access-key: admin\n      secret-key: admin123456\n      bucketName: fxzcloud\n\naliyun:\n  sms:\n    accessKeyId: LTAI5tJymbcizqk5nnVvQUU6\n    accessKeySecret: 9iszXlrnjoXuLLWIXFiaFCzWg2ttW2\n    domain: dysmsapi.aliyuncs.com\n    regionId: cn-hangzhou\n    templateCode: SMS_154950909\n    signName: 阿里云短信测试\n', 'fe38fe0886e636d93e215ea4a58f211d', '2023-02-18 21:54:19', '2023-02-18 21:54:19', 'nacos', '101.87.132.119', 'U', '', '');
INSERT INTO `his_config_info` (`id`, `nid`, `data_id`, `group_id`, `app_name`, `content`, `md5`, `gmt_create`, `gmt_modified`, `src_user`, `src_ip`, `op_type`, `tenant_id`, `encrypted_data_key`) VALUES (6, 5, 'fxz-server-system.yaml', 'DEFAULT_GROUP', '', 'server:\n  port: 4000\n\nspring:\n  datasource:\n    driver-class-name: com.mysql.cj.jdbc.Driver\n    username: root\n    password: ENC(bM81d5utQm25BU/ZnKFOL5Otj1+/GyCaF9+rnC9YUxA=)\n    url: jdbc:mysql://fxz-mysql/fxz_cloud_base?useUnicode=true&characterEncoding=UTF-8&rewriteBatchedStatements=true&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=GMT%2b8\n \n   # RabbitMQ 配置\n  rabbitmq:\n    host: fxz-rabbitmq\n    port: 5672\n    username: fxz\n    password: 123456\n    # 动态创建和绑定队列、交换机的配置\n    modules: \n      # 订单延时队列，到了过期的时间会被转发到订单取消队列\n      - routing-key: canal_key\n        queue:\n          name: canal_queue\n        exchange:\n          name: canal_exchange\n  autoconfigure:\n    exclude: org.springframework.cloud.sleuth.instrument.hystrix.SleuthHystrixAutoConfiguration\n\nsecurity:\n  oauth2:\n    resource:\n      id: ${spring.application.name}\n      #token-info-uri:  http://${fxz-gateway}:8301/auth/oauth/check_token\n      user-info-uri: http://fxz-gateway:8301/auth/user\n\nmybatis-plus:\n  type-aliases-package: com.fxz.common.core.entity.system\n  mapper-locations: classpath:mapper/*/*.xml\n  configuration:\n    jdbc-type-for-null: null\n  global-config:\n    banner: false\n\ninfo:\n  app:\n    name: ${spring.application.name}\n    description: \"@project.description@\"\n    version: \"@project.version@\"\n\nmanagement:\n  endpoints:\n    web:\n      exposure:\n        include: \'*\'\n  endpoint:\n    health:\n      show-details: ALWAYS\n\nfeign:\n  hystrix:\n    enabled: true\n\nhystrix:\n  shareSecurityContext: true\n\nfxz:\n  cloud:\n    security: \n      anonUris: /find,/swagger-ui/**,/webjars/**,/swagger-resources/**,/v2/api-docs/**,/,/csrf,/actuator/**,/user/findByName/*,/menu/findUserPermissions/**,/user/test,/kk-anti-reptile/**,/swagger**/**,/**/api-docs/**,/doc.html\n  common:\n    sequence:\n      type: redis\n      \n# 文件系统\nfile:\n  storage:\n    oss:\n      endpoint: http://fxz-oss:9000\n      access-key: admin\n      secret-key: admin123456\n      bucketName: fxzcloud\n\naliyun:\n  sms:\n    accessKeyId: LTAI5tJymbcizqk5nnVvQUU6\n    accessKeySecret: 9iszXlrnjoXuLLWIXFiaFCzWg2ttW2\n    domain: dysmsapi.aliyuncs.com\n    regionId: cn-hangzhou\n    templateCode: SMS_154950909\n    signName: 阿里云短信测试\n', '74aaf879d79608ebf01cf1d4a411d417', '2023-02-22 13:50:00', '2023-02-22 13:50:00', 'nacos', '27.115.16.158', 'U', '', '');
INSERT INTO `his_config_info` (`id`, `nid`, `data_id`, `group_id`, `app_name`, `content`, `md5`, `gmt_create`, `gmt_modified`, `src_user`, `src_ip`, `op_type`, `tenant_id`, `encrypted_data_key`) VALUES (70, 6, 'fxz-generate.yaml', 'DEFAULT_GROUP', '', 'server:\n  port: 5001\n\nspring:\n  datasource:\n    driver-class-name: com.mysql.cj.jdbc.Driver\n    username: root\n    password: fxzcloud-mysql-password\n    url: jdbc:mysql://fxz-mysql/fxz_cloud_codegen?useUnicode=true&characterEncoding=UTF-8&rewriteBatchedStatements=true&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=GMT%2b8\n\n  \nsecurity:\n  oauth2:\n    resource:\n      id: ${spring.application.name}\n      user-info-uri: http://fxz-gateway:8301/auth/user\n\nmybatis-plus:\n  mapper-locations: classpath:mapper/*/*.xml\n  configuration:\n    jdbc-type-for-null: null\n  global-config:\n    banner: false', '3098c66c081a3241e8ada3b7d43ff379', '2023-02-22 13:51:02', '2023-02-22 13:51:03', 'nacos', '27.115.16.158', 'U', '', '');
INSERT INTO `his_config_info` (`id`, `nid`, `data_id`, `group_id`, `app_name`, `content`, `md5`, `gmt_create`, `gmt_modified`, `src_user`, `src_ip`, `op_type`, `tenant_id`, `encrypted_data_key`) VALUES (6, 7, 'fxz-server-system.yaml', 'DEFAULT_GROUP', '', 'server:\n  port: 4000\n\nspring:\n  datasource:\n    driver-class-name: com.mysql.cj.jdbc.Driver\n    username: root\n    password: ENC(bM81d5utQm25BU/ZnKFOL5Otj1+/GyCaF9+rnC9YUxA=)\n    url: jdbc:mysql://fxz-mysql/fxz_cloud_base?useUnicode=true&characterEncoding=UTF-8&rewriteBatchedStatements=true&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=GMT%2b8\n \n   # RabbitMQ 配置\n  rabbitmq:\n    host: fxz-rabbitmq\n    port: 5672\n    username: fxz\n    password: 123456\n    # 动态创建和绑定队列、交换机的配置\n    modules: \n      # 订单延时队列，到了过期的时间会被转发到订单取消队列\n      - routing-key: canal_key\n        queue:\n          name: canal_queue\n        exchange:\n          name: canal_exchange\n  autoconfigure:\n    exclude: org.springframework.cloud.sleuth.instrument.hystrix.SleuthHystrixAutoConfiguration\n\nsecurity:\n  oauth2:\n    resource:\n      id: ${spring.application.name}\n      #token-info-uri:  http://${fxz-gateway}:8301/auth/oauth/check_token\n      user-info-uri: http://fxz-gateway:9999/auth/user\n\nmybatis-plus:\n  type-aliases-package: com.fxz.common.core.entity.system\n  mapper-locations: classpath:mapper/*/*.xml\n  configuration:\n    jdbc-type-for-null: null\n  global-config:\n    banner: false\n\ninfo:\n  app:\n    name: ${spring.application.name}\n    description: \"@project.description@\"\n    version: \"@project.version@\"\n\nmanagement:\n  endpoints:\n    web:\n      exposure:\n        include: \'*\'\n  endpoint:\n    health:\n      show-details: ALWAYS\n\nfeign:\n  hystrix:\n    enabled: true\n\nhystrix:\n  shareSecurityContext: true\n\nfxz:\n  cloud:\n    security: \n      anonUris: /find,/swagger-ui/**,/webjars/**,/swagger-resources/**,/v2/api-docs/**,/,/csrf,/actuator/**,/user/findByName/*,/menu/findUserPermissions/**,/user/test,/kk-anti-reptile/**,/swagger**/**,/**/api-docs/**,/doc.html\n  common:\n    sequence:\n      type: redis\n      \n# 文件系统\nfile:\n  storage:\n    oss:\n      endpoint: http://fxz-oss:9000\n      access-key: admin\n      secret-key: admin123456\n      bucketName: fxzcloud\n\naliyun:\n  sms:\n    accessKeyId: LTAI5tJymbcizqk5nnVvQUU6\n    accessKeySecret: 9iszXlrnjoXuLLWIXFiaFCzWg2ttW2\n    domain: dysmsapi.aliyuncs.com\n    regionId: cn-hangzhou\n    templateCode: SMS_154950909\n    signName: 阿里云短信测试\n', '42300672c9974f56a646a2083f297a64', '2023-02-22 13:51:15', '2023-02-22 13:51:16', 'nacos', '27.115.16.158', 'U', '', '');
INSERT INTO `his_config_info` (`id`, `nid`, `data_id`, `group_id`, `app_name`, `content`, `md5`, `gmt_create`, `gmt_modified`, `src_user`, `src_ip`, `op_type`, `tenant_id`, `encrypted_data_key`) VALUES (171, 8, 'fxz-mall-xxl-job.yaml', 'DEFAULT_GROUP', '', 'server:\n  port: 6501\n   \nfxz:\n  common:\n    xxl-job:\n      appname: ${spring.application.name}\n      admin-addresses: http://127.0.0.1:9003/xxl-job-admin\n      port: 9900\n\nsecurity:\n  oauth2:\n    resource:\n      id: ${spring.application.name}\n      user-info-uri: http://fxz-gateway:8301/auth/user\n\nmybatis-plus:\n  type-aliases-package: com.fxz.common.core.entity.system\n  mapper-locations: classpath:mapper/*/*.xml\n  configuration:\n    jdbc-type-for-null: null\n  global-config:\n    banner: false\n\ninfo:\n  app:\n    name: ${spring.application.name}\n    description: \"@project.description@\"\n    version: \"@project.version@\"\n\nmanagement:\n  endpoints:\n    web:\n      exposure:\n        include: \'*\'\n  endpoint:\n    health:\n      show-details: ALWAYS\n\n\n', '29ab9e77efd5f2dc35382ee3afd67ab2', '2023-02-22 13:51:36', '2023-02-22 13:51:36', 'nacos', '27.115.16.158', 'U', '', '');
INSERT INTO `his_config_info` (`id`, `nid`, `data_id`, `group_id`, `app_name`, `content`, `md5`, `gmt_create`, `gmt_modified`, `src_user`, `src_ip`, `op_type`, `tenant_id`, `encrypted_data_key`) VALUES (80, 9, 'fxz-job.yaml', 'DEFAULT_GROUP', '', 'server:\n  port: 5002\n\n\nspring:\n  datasource:\n    driver-class-name: com.mysql.cj.jdbc.Driver\n    username: root\n    password: ENC(bM81d5utQm25BU/ZnKFOL5Otj1+/GyCaF9+rnC9YUxA=)\n    url: jdbc:mysql://fxz-mysql/fxz_cloud_job?useUnicode=true&characterEncoding=UTF-8&rewriteBatchedStatements=true&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=GMT%2b8\n  quartz:\n    auto-startup: true # quartz启动开关\n    scheduler-name: schedulerName # 默认的schedulerName\n    job-store-type: jdbc # jdbc存储 默认内存\n    overwrite-existing-jobs: true # 启动时更新已经存在的job\n    wait-for-jobs-to-complete-on-shutdown: true # 应用关闭时 是否等待定时任务执行完成 默认为 false\n    properties:\n      org:\n        quartz:\n          scheduler:\n            instanceName: schedulerName # 执行器名称\n            instanceId: AUTO # 自动生成id\n          jobStore:\n            class: org.springframework.scheduling.quartz.LocalDataSourceJobStore\n            isClustered: true # 集群模式\n            clusterCheckinInterval: 15000 # 集群检查频率 单位：毫秒\n            misfireThreshold: 60000 # misfire 阀值 单位：毫秒\n          threadPool:\n            threadCount: 25 # 线程池大小\n            threadPriority: 5 # 线程优先级\n            threadsInheritContextClassLoaderOfInitializingThread: true\n            class: org.quartz.simpl.SimpleThreadPool # 线程池类型\n    jdbc:\n      initialize-schema: never #  是否自动初始化Quartz表 我们手动创建表\n\nsecurity:\n  oauth2:\n    resource:\n      id: ${spring.application.name}\n      user-info-uri: http://fxz-gateway:8301/auth/user\n\n\nmybatis-plus:\n  mapper-locations: classpath:mapper/*/*.xml\n  configuration:\n    jdbc-type-for-null: null\n  global-config:\n    banner: false', '78a8636a9141fd608036b3aa01ac0ef2', '2023-02-22 13:51:54', '2023-02-22 13:51:54', 'nacos', '27.115.16.158', 'U', '', '');
INSERT INTO `his_config_info` (`id`, `nid`, `data_id`, `group_id`, `app_name`, `content`, `md5`, `gmt_create`, `gmt_modified`, `src_user`, `src_ip`, `op_type`, `tenant_id`, `encrypted_data_key`) VALUES (1, 10, 'fxz-auth.yaml', 'DEFAULT_GROUP', '', 'server:\n  port: 3000\n\nwechat:\n  weapp:\n    appid: ENC(SxZ78YMbeLIHg1/IAmPxkVFjbxHUDPSJDsrVJMmc/ws=)\n    secret: ENC(OcF3VEu+7nyetyF7S5pTvidOfNRXT1KLsuo6h09aYUD74Hz0buJo8ONkg/Z2AiNj)\n\nspring:\n  datasource:\n    driver-class-name: com.mysql.cj.jdbc.Driver\n    username: root\n    password: ENC(bM81d5utQm25BU/ZnKFOL5Otj1+/GyCaF9+rnC9YUxA=)\n    url: jdbc:mysql://fxz-mysql/fxz_cloud_base?useUnicode=true&characterEncoding=UTF-8&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=GMT%2b8&rewriteBatchedStatements=true\n  \nmybatis-plus:\n  type-aliases-package: com.fxz.common.entity.system\n  mapper-locations: classpath:mapper/*.xml\n  configuration:\n    jdbc-type-for-null: null\n  global-config:\n    banner: false\n\ninfo:\n  app:\n    name: ${spring.application.name}\n    description: \"@project.description@\"\n    version: \"@project.version@\"\n\nmanagement:\n  endpoints:\n    web:\n      exposure:\n        include: \'*\'\n  endpoint:\n    health:\n      show-details: ALWAYS\n\nsecurity:\n  oauth2:\n    resource:\n      id: ${spring.application.name}\n      #token-info-uri:  http://${fxz-gateway}:8301/auth/oauth/check_token\n      user-info-uri: http://fxz-gateway:8301/auth/user\n\nfxz:\n  cloud:\n    security:\n      anonUris: /token/**,/**/swagger-ui/index.html', 'fc90fe3fa344af86c2569e6404dde142', '2023-02-22 13:52:39', '2023-02-22 13:52:40', 'nacos', '27.115.16.158', 'U', '', '');
INSERT INTO `his_config_info` (`id`, `nid`, `data_id`, `group_id`, `app_name`, `content`, `md5`, `gmt_create`, `gmt_modified`, `src_user`, `src_ip`, `op_type`, `tenant_id`, `encrypted_data_key`) VALUES (102, 11, 'fxz-mall-product.yaml', 'DEFAULT_GROUP', '', 'server:\n  port: 6001\n\nspring:\n  cache:\n    type: redis\n  datasource:\n    driver-class-name: com.mysql.cj.jdbc.Driver\n    username: root\n    password: ENC(bM81d5utQm25BU/ZnKFOL5Otj1+/GyCaF9+rnC9YUxA=)\n    url: jdbc:mysql://fxz-mysql/fxz_mall_product?useUnicode=true&characterEncoding=UTF-8&rewriteBatchedStatements=true&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=GMT%2b8\n  autoconfigure:\n    exclude: org.springframework.cloud.sleuth.instrument.hystrix.SleuthHystrixAutoConfiguration\n\nsecurity:\n  oauth2:\n    resource:\n      id: ${spring.application.name}\n      #token-info-uri:  http://${fxz-gateway}:8301/auth/oauth/check_token\n      user-info-uri: http://fxz-gateway:8301/auth/user\n\nmybatis-plus:\n  type-aliases-package: com.fxz.common.core.entity.system\n  mapper-locations: classpath:mapper/*/*.xml\n  configuration:\n    jdbc-type-for-null: null\n  global-config:\n    banner: false\n\ninfo:\n  app:\n    name: ${spring.application.name}\n    description: \"@project.description@\"\n    version: \"@project.version@\"\n\nmanagement:\n  endpoints:\n    web:\n      exposure:\n        include: \'*\'\n  endpoint:\n    health:\n      show-details: ALWAYS\n\nfeign:\n  hystrix:\n    enabled: true\n\nhystrix:\n  shareSecurityContext: true\n\nfxz:\n  cloud:\n    security: \n      anonUris: /find,/swagger-ui.html,/webjars/**,/swagger-resources/**,/v2/api-docs/**,/,/csrf,/actuator/**,/user/findByName/*,/menu/findUserPermissions/**,/user/test,/kk-anti-reptile/**\n\n# 文件系统\noss:\n  endpoint: http://fxz-oss:9000\n  access-key: admin\n  secret-key: admin123456\n  bucketName: fxzcloud', '193e06f8f1cb91a5d5b10f25be5e4300', '2023-02-22 13:53:23', '2023-02-22 13:53:24', 'nacos', '27.115.16.158', 'U', '', '');
INSERT INTO `his_config_info` (`id`, `nid`, `data_id`, `group_id`, `app_name`, `content`, `md5`, `gmt_create`, `gmt_modified`, `src_user`, `src_ip`, `op_type`, `tenant_id`, `encrypted_data_key`) VALUES (108, 12, 'fxz-mall-order.yaml', 'DEFAULT_GROUP', '', 'server:\n  port: 6101\n\nspring:\n  datasource:\n    driver-class-name: com.mysql.cj.jdbc.Driver\n    username: root\n    password: ENC(bM81d5utQm25BU/ZnKFOL5Otj1+/GyCaF9+rnC9YUxA=)\n    url: jdbc:mysql://fxz-mysql/fxz_mall_order?useUnicode=true&characterEncoding=UTF-8&rewriteBatchedStatements=true&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=GMT%2b8\n  # RabbitMQ 配置\n  rabbitmq:\n    host: fxz-rabbitmq\n    port: 5672\n    username: fxz\n    password: 123456\n    # 动态创建和绑定队列、交换机的配置\n    modules: \n      # 订单延时队列，到了过期的时间会被转发到订单取消队列\n      - routing-key: order.create.routing.key\n        queue:\n          name: order.delay.queue\n          dead-letter-exchange: order.exchange\n          dead-letter-routing-key: order.close.routing.key\n          arguments:\n            # 1分钟(测试)，单位毫秒,没有被消费则进入死信队列\n            x-message-ttl: 60000 \n        exchange:\n          name: order.exchange\n      # 订单取消队列\n      - routing-key: order.close.routing.key\n        queue:\n          name: order.close.queue\n        exchange:\n          name: order.exchange\n  redis:\n    database: 0\n    host: fxz-redis\n    port: 6379\n    password: fxzcloud-redis\n    lettuce:\n      pool:\n        min-idle: 8\n        max-idle: 500\n        max-active: 2000\n        max-wait: 10000\n    timeout: 5000  \n  autoconfigure:\n    exclude: org.springframework.cloud.sleuth.instrument.hystrix.SleuthHystrixAutoConfiguration\n\nsecurity:\n  oauth2:\n    resource:\n      id: ${spring.application.name}\n      user-info-uri: http://fxz-gateway:8301/auth/user\n\nmybatis-plus:\n  type-aliases-package: com.fxz.common.core.entity.system\n  mapper-locations: classpath:mapper/*/*.xml\n  configuration:\n    jdbc-type-for-null: null\n  global-config:\n    banner: false\n\ninfo:\n  app:\n    name: ${spring.application.name}\n    description: \"@project.description@\"\n    version: \"@project.version@\"\n\nmanagement:\n  endpoints:\n    web:\n      exposure:\n        include: \'*\'\n  endpoint:\n    health:\n      show-details: ALWAYS\n\nfeign:\n  hystrix:\n    enabled: true\n\nhystrix:\n  shareSecurityContext: true\n\nfxz:\n  cloud:\n    security: \n      anonUris: /find,/swagger-ui.html,/webjars/**,/swagger-resources/**,/v2/api-docs/**,/,/csrf,/actuator/**,/user/findByName/*,/menu/findUserPermissions/**,/user/test,/kk-anti-reptile/**\n\n# 文件系统\noss:\n  endpoint: http://fxz-oss:9000\n  access-key: admin\n  secret-key: admin123456\n  bucketName: fxzcloud', '405601c72792835bcfa8a56ba18c8ad6', '2023-02-22 13:53:36', '2023-02-22 13:53:37', 'nacos', '27.115.16.158', 'U', '', '');
INSERT INTO `his_config_info` (`id`, `nid`, `data_id`, `group_id`, `app_name`, `content`, `md5`, `gmt_create`, `gmt_modified`, `src_user`, `src_ip`, `op_type`, `tenant_id`, `encrypted_data_key`) VALUES (110, 13, 'fxz-mall-user.yaml', 'DEFAULT_GROUP', '', 'server:\n  port: 6201\n\nspring:\n  datasource:\n    driver-class-name: com.mysql.cj.jdbc.Driver\n    username: root\n    password: ENC(bM81d5utQm25BU/ZnKFOL5Otj1+/GyCaF9+rnC9YUxA=)\n    url: jdbc:mysql://fxz-mysql/fxz_mall_user?useUnicode=true&characterEncoding=UTF-8&rewriteBatchedStatements=true&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=GMT%2b8 \n  autoconfigure:\n    exclude: org.springframework.cloud.sleuth.instrument.hystrix.SleuthHystrixAutoConfiguration\n\nsecurity:\n  oauth2:\n    resource:\n      id: ${spring.application.name}\n      user-info-uri: http://fxz-gateway:8301/auth/user\n\nmybatis-plus:\n  type-aliases-package: com.fxz.common.core.entity.system\n  mapper-locations: classpath:mapper/*/*.xml\n  configuration:\n    jdbc-type-for-null: null\n  global-config:\n    banner: false\n\ninfo:\n  app:\n    name: ${spring.application.name}\n    description: \"@project.description@\"\n    version: \"@project.version@\"\n\nmanagement:\n  endpoints:\n    web:\n      exposure:\n        include: \'*\'\n  endpoint:\n    health:\n      show-details: ALWAYS\n\nfeign:\n  hystrix:\n    enabled: true\n\nhystrix:\n  shareSecurityContext: true\n\nfxz:\n  cloud:\n    security: \n      anonUris: /find,/swagger-ui.html,/webjars/**,/swagger-resources/**,/v2/api-docs/**,/,/csrf,/actuator/**,/user/findByName/*,/menu/findUserPermissions/**,/user/test,/kk-anti-reptile/**\n', '23c4b81765e0c8323a56a87f456a21d8', '2023-02-22 13:53:46', '2023-02-22 13:53:47', 'nacos', '27.115.16.158', 'U', '', '');
INSERT INTO `his_config_info` (`id`, `nid`, `data_id`, `group_id`, `app_name`, `content`, `md5`, `gmt_create`, `gmt_modified`, `src_user`, `src_ip`, `op_type`, `tenant_id`, `encrypted_data_key`) VALUES (126, 14, 'fxz-mall-search.yaml', 'DEFAULT_GROUP', '', 'server:\n  port: 6301\n\nspring:\n   # RabbitMQ 配置\n  rabbitmq:\n    host: fxz-rabbitmq\n    port: 5672\n    username: fxz\n    password: 123456\n    # 动态创建和绑定队列、交换机的配置\n    modules: \n      # 订单延时队列，到了过期的时间会被转发到订单取消队列\n      - routing-key: canal_key\n        queue:\n          name: canal_queue\n        exchange:\n          name: canal_exchange\n\nsecurity:\n  oauth2:\n    resource:\n      id: ${spring.application.name}\n      user-info-uri: http://fxz-gateway:8301/auth/user\n\nmybatis-plus:\n  type-aliases-package: com.fxz.common.core.entity.system\n  mapper-locations: classpath:mapper/*/*.xml\n  configuration:\n    jdbc-type-for-null: null\n  global-config:\n    banner: false\n\ninfo:\n  app:\n    name: ${spring.application.name}\n    description: \"@project.description@\"\n    version: \"@project.version@\"\n\nmanagement:\n  endpoints:\n    web:\n      exposure:\n        include: \'*\'\n  endpoint:\n    health:\n      show-details: ALWAYS\n\nfeign:\n  hystrix:\n    enabled: true\n\nhystrix:\n  shareSecurityContext: true\n\nfxz:\n  cloud:\n    security: \n      anonUris: /find,/swagger-ui.html,/webjars/**,/swagger-resources/**,/v2/api-docs/**,/,/csrf,/actuator/**,/user/findByName/*,/menu/findUserPermissions/**,/user/test,/kk-anti-reptile/**\n', 'a81bd937fc8f05567943ea4e8500f533', '2023-02-22 13:53:56', '2023-02-22 13:53:57', 'nacos', '27.115.16.158', 'U', '', '');
INSERT INTO `his_config_info` (`id`, `nid`, `data_id`, `group_id`, `app_name`, `content`, `md5`, `gmt_create`, `gmt_modified`, `src_user`, `src_ip`, `op_type`, `tenant_id`, `encrypted_data_key`) VALUES (162, 15, 'fxz-mall-promotion.yaml', 'DEFAULT_GROUP', '', 'server:\n  port: 6401\n\nspring:\n  datasource:\n    driver-class-name: com.mysql.cj.jdbc.Driver\n    username: root\n    password: ENC(bM81d5utQm25BU/ZnKFOL5Otj1+/GyCaF9+rnC9YUxA=)\n    url: jdbc:mysql://fxz-mysql/fxz_mall_promotion?useUnicode=true&characterEncoding=UTF-8&rewriteBatchedStatements=true&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=GMT%2b8\n  autoconfigure:\n    exclude: org.springframework.cloud.sleuth.instrument.hystrix.SleuthHystrixAutoConfiguration\n\nsecurity:\n  oauth2:\n    resource:\n      id: ${spring.application.name}\n      #token-info-uri:  http://${fxz-gateway}:8301/auth/oauth/check_token\n      user-info-uri: http://fxz-gateway:8301/auth/user\n\nmybatis-plus:\n  type-aliases-package: com.fxz.common.core.entity.system\n  mapper-locations: classpath:mapper/*/*.xml\n  configuration:\n    jdbc-type-for-null: null\n  global-config:\n    banner: false\n\ninfo:\n  app:\n    name: ${spring.application.name}\n    description: \"@project.description@\"\n    version: \"@project.version@\"\n\nmanagement:\n  endpoints:\n    web:\n      exposure:\n        include: \'*\'\n  endpoint:\n    health:\n      show-details: ALWAYS\n\nfeign:\n  hystrix:\n    enabled: true\n\nhystrix:\n  shareSecurityContext: true\n\nfxz:\n  cloud:\n    security: \n      anonUris: /find,/swagger-ui.html,/webjars/**,/swagger-resources/**,/v2/api-docs/**,/,/csrf,/actuator/**,/user/findByName/*,/menu/findUserPermissions/**,/user/test,/kk-anti-reptile/**\n\n# 文件系统\noss:\n  endpoint: http://fxz-oss:9000\n  access-key: admin\n  secret-key: admin123456\n  bucketName: fxzcloud', 'c44e84870c7d84a5c3b5519be53d042f', '2023-02-22 13:54:12', '2023-02-22 13:54:12', 'nacos', '27.115.16.158', 'U', '', '');
COMMIT;

-- ----------------------------
-- Table structure for permissions
-- ----------------------------
DROP TABLE IF EXISTS `permissions`;
CREATE TABLE `permissions` (
  `role` varchar(50) COLLATE utf8mb4_general_ci NOT NULL,
  `resource` varchar(255) COLLATE utf8mb4_general_ci NOT NULL,
  `action` varchar(8) COLLATE utf8mb4_general_ci NOT NULL,
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
  `username` varchar(50) COLLATE utf8mb4_general_ci NOT NULL,
  `role` varchar(50) COLLATE utf8mb4_general_ci NOT NULL,
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
  `tenant_id` varchar(128) COLLATE utf8mb3_bin NOT NULL DEFAULT '' COMMENT 'Tenant ID',
  `quota` int unsigned NOT NULL DEFAULT '0' COMMENT '配额，0表示使用默认值',
  `usage` int unsigned NOT NULL DEFAULT '0' COMMENT '使用量',
  `max_size` int unsigned NOT NULL DEFAULT '0' COMMENT '单个配置大小上限，单位为字节，0表示使用默认值',
  `max_aggr_count` int unsigned NOT NULL DEFAULT '0' COMMENT '聚合子配置最大个数',
  `max_aggr_size` int unsigned NOT NULL DEFAULT '0' COMMENT '单个聚合数据的子配置大小上限，单位为字节，0表示使用默认值',
  `max_history_count` int unsigned NOT NULL DEFAULT '0' COMMENT '最大变更历史数量',
  `gmt_create` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `gmt_modified` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '修改时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_tenant_id` (`tenant_id`)
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
  `kp` varchar(128) COLLATE utf8mb3_bin NOT NULL COMMENT 'kp',
  `tenant_id` varchar(128) COLLATE utf8mb3_bin DEFAULT '' COMMENT 'tenant_id',
  `tenant_name` varchar(128) COLLATE utf8mb3_bin DEFAULT '' COMMENT 'tenant_name',
  `tenant_desc` varchar(256) COLLATE utf8mb3_bin DEFAULT NULL COMMENT 'tenant_desc',
  `create_source` varchar(32) COLLATE utf8mb3_bin DEFAULT NULL COMMENT 'create_source',
  `gmt_create` bigint NOT NULL COMMENT '创建时间',
  `gmt_modified` bigint NOT NULL COMMENT '修改时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_tenant_info_kptenantid` (`kp`,`tenant_id`),
  KEY `idx_tenant_id` (`tenant_id`)
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
  `username` varchar(50) COLLATE utf8mb4_general_ci NOT NULL,
  `password` varchar(500) COLLATE utf8mb4_general_ci NOT NULL,
  `enabled` tinyint(1) NOT NULL,
  PRIMARY KEY (`username`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- ----------------------------
-- Records of users
-- ----------------------------
BEGIN;
INSERT INTO `users` (`username`, `password`, `enabled`) VALUES ('nacos', '$2a$10$EuWPZHzz32dJN7jexM34MOeYirDdFAZm2kuWj7VEOJhhZkDrxfvUu', 1);
COMMIT;

SET FOREIGN_KEY_CHECKS = 1;
