
USE art_ai;

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for ai_app
-- ----------------------------
DROP TABLE IF EXISTS `ai_app`;
CREATE TABLE `ai_app` (
  `id` bigint NOT NULL,
  `name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL,
  `description` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL,
  `mode` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL,
  `icon` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL,
  `status` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL,
  `create_time` datetime DEFAULT NULL,
  `create_by` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL,
  `update_time` datetime DEFAULT NULL,
  `update_by` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL,
  `tenant_id` bigint DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- ----------------------------
-- Table structure for ai_workflow_runtime
-- ----------------------------
DROP TABLE IF EXISTS `ai_workflow_runtime`;
CREATE TABLE `ai_workflow_runtime` (
  `id` bigint DEFAULT NULL COMMENT '主键',
  `app_id` bigint DEFAULT NULL COMMENT '应用id',
  `workflow_id` bigint DEFAULT NULL COMMENT '工作流主键',
  `input` json DEFAULT NULL COMMENT '流程入参',
  `output` json DEFAULT NULL COMMENT '流程出参',
  `status` int DEFAULT NULL COMMENT '执行状态',
  `status_remark` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '状态描述',
  `create_by` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL,
  `create_time` datetime DEFAULT NULL,
  `update_time` datetime DEFAULT NULL,
  `update_by` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL,
  `tenant_id` bigint DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- ----------------------------
-- Table structure for ai_workflows
-- ----------------------------
DROP TABLE IF EXISTS `ai_workflows`;
CREATE TABLE `ai_workflows` (
  `id` bigint NOT NULL COMMENT '主键',
  `app_id` bigint NOT NULL COMMENT '应用id',
  `type` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '流程类型',
  `version` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '流程版本',
  `graph` json NOT NULL COMMENT '图信息',
  `features` json DEFAULT NULL COMMENT '流程配置',
  `environment_variables` longtext CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci COMMENT '环境变量',
  `conversation_variables` longtext CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci COMMENT '会话变量',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `create_by` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '创建人',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  `update_by` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '更新人',
  `tenant_id` bigint DEFAULT NULL COMMENT '租户id',
  PRIMARY KEY (`id`) USING BTREE,
  KEY `workflow_version_idx` (`app_id`,`version`,`tenant_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- ----------------------------
-- Table structure for ai_datasets
-- ----------------------------
DROP TABLE IF EXISTS `ai_datasets`;
CREATE TABLE `ai_datasets` (
                               `id` bigint NOT NULL,
                               `tenant_id` bigint DEFAULT NULL,
                               `name` varchar(255) COLLATE utf8mb4_general_ci DEFAULT NULL,
                               `description` varchar(255) COLLATE utf8mb4_general_ci DEFAULT NULL,
                               `permission` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '权限(公开或者非公开)',
                               `data_source_type` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '数据源类型(文件上传)',
                               `create_time` datetime DEFAULT NULL,
                               `create_by` varchar(255) COLLATE utf8mb4_general_ci DEFAULT NULL,
                               `update_time` datetime DEFAULT NULL,
                               `update_by` varchar(255) COLLATE utf8mb4_general_ci DEFAULT NULL,
                               `embedding_model` varchar(255) COLLATE utf8mb4_general_ci DEFAULT NULL,
                               `embedding_model_provider` varchar(255) COLLATE utf8mb4_general_ci DEFAULT NULL,
                               `collection_binding_id` bigint DEFAULT NULL COMMENT '关联的集合id',
                               `retrieval_model` json DEFAULT NULL COMMENT '检索配置(混合检索或向量检索)',
                               `graphic_model` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL,
                               `graphic_model_provider` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL,
                               PRIMARY KEY (`id`),
                               UNIQUE KEY `uni_name` (`name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- ----------------------------
-- Table structure for ai_documents
-- ----------------------------
DROP TABLE IF EXISTS `ai_documents`;
CREATE TABLE `ai_documents` (
                                `id` bigint DEFAULT NULL,
                                `dataset_id` varchar(255) COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '数据集主键',
                                `bucket_name` varchar(255) COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '文件桶名称',
                                `file_name` varchar(255) COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '文件名',
                                `title` varchar(255) COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '文档标题',
                                `brief` varchar(255) COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '文档摘要',
                                `content` longtext CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci COMMENT '文档内容',
                                `embedding_status` int DEFAULT NULL COMMENT '向量化状态',
                                `embedding_status_change_time` datetime DEFAULT NULL COMMENT '向量化状态改变时间',
                                `graphical_status` int DEFAULT NULL COMMENT '图谱化状态',
                                `graphical_status_change_time` datetime DEFAULT NULL COMMENT '图谱状态改变时间',
                                `update_by` varchar(255) COLLATE utf8mb4_general_ci DEFAULT NULL,
                                `update_time` datetime DEFAULT NULL,
                                `create_by` varchar(255) COLLATE utf8mb4_general_ci DEFAULT NULL,
                                `create_time` datetime DEFAULT NULL,
                                `original_filename` varchar(255) COLLATE utf8mb4_general_ci DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- ----------------------------
-- Table structure for ai_document_segment
-- ----------------------------
DROP TABLE IF EXISTS `ai_document_segment`;
CREATE TABLE `ai_document_segment` (
                                       `id` bigint NOT NULL,
                                       `segment` longtext CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci COMMENT '分段内容',
                                       `dataset_id` bigint DEFAULT NULL COMMENT '数据集id',
                                       `document_id` bigint DEFAULT NULL COMMENT '文档id',
                                       `tenant_id` bigint DEFAULT NULL,
                                       `update_by` varchar(255) COLLATE utf8mb4_general_ci DEFAULT NULL,
                                       `update_time` datetime DEFAULT NULL,
                                       `create_by` varchar(255) COLLATE utf8mb4_general_ci DEFAULT NULL,
                                       `create_time` datetime DEFAULT NULL,
                                       `segment_type` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL,
                                       PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- ----------------------------
-- Table structure for ai_model_platform
-- ----------------------------
DROP TABLE IF EXISTS `ai_model_platform`;
CREATE TABLE `ai_model_platform` (
                                     `id` bigint NOT NULL COMMENT '主键',
                                     `name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '厂商名称',
                                     `base_url` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT 'base_url',
                                     `api_key` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT 'api_key',
                                     `secret_key` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT 'secret_key',
                                     `proxy_enable` int DEFAULT NULL COMMENT '是否启用代理',
                                     `openai_api_compatible` int DEFAULT NULL COMMENT '是否兼容openapi协议',
                                     `create_time` datetime DEFAULT NULL,
                                     `create_by` varchar(255) COLLATE utf8mb4_general_ci DEFAULT NULL,
                                     `update_time` datetime DEFAULT NULL,
                                     `update_by` varchar(255) COLLATE utf8mb4_general_ci DEFAULT NULL,
                                     `tenant_id` bigint DEFAULT NULL,
                                     PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- ----------------------------
-- Table structure for ai_qa_pairs
-- ----------------------------
DROP TABLE IF EXISTS `ai_qa_pairs`;
CREATE TABLE `ai_qa_pairs` (
  `id` bigint NOT NULL COMMENT '主键',
  `dataset_id` bigint NOT NULL COMMENT '所属数据集ID',
  `question` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '问题',
  `question_hash` char(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '问题Hash(MD5)',
  `answer` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '答案',
  `priority` int DEFAULT 1 COMMENT '优先级(1-5)',
  `enabled` tinyint(1) DEFAULT 1 COMMENT '是否启用',
  `hit_count` int DEFAULT 0 COMMENT '命中次数',
  `last_hit_time` datetime DEFAULT NULL COMMENT '最后命中时间',
  `vector_indexed` tinyint(1) DEFAULT 0 COMMENT '是否已向量化',
  `vector_id` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '向量ID',
  `source_type` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT 'manual' COMMENT '来源类型(manual/llm/import)',
  `tenant_id` bigint DEFAULT NULL COMMENT '租户ID',
  `create_by` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '创建人',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '更新人',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`),
  KEY `idx_question_hash` (`question_hash`),
  KEY `idx_dataset_enabled` (`dataset_id`, `enabled`),
  KEY `idx_priority` (`priority` DESC)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='QA问答对主表';

-- ----------------------------
-- Table structure for ai_qa_similar_questions
-- ----------------------------
DROP TABLE IF EXISTS `ai_qa_similar_questions`;
CREATE TABLE `ai_qa_similar_questions` (
  `id` bigint NOT NULL COMMENT '主键',
  `qa_pair_id` bigint NOT NULL COMMENT '关联的QA对ID',
  `similar_question` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '相似问题',
  `similar_hash` char(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '相似问题Hash',
  `source_type` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT 'manual' COMMENT '来源类型(manual/auto)',
  `create_by` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '创建人',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  PRIMARY KEY (`id`),
  KEY `idx_similar_hash` (`similar_hash`),
  KEY `idx_qa_pair` (`qa_pair_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='QA相似问题表';

-- ----------------------------
-- Table structure for ai_qa_hit_logs
-- ----------------------------
DROP TABLE IF EXISTS `ai_qa_hit_logs`;
CREATE TABLE `ai_qa_hit_logs` (
  `id` bigint NOT NULL COMMENT '主键',
  `qa_pair_id` bigint NOT NULL COMMENT '命中的QA对ID',
  `user_query` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '用户查询',
  `match_type` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '匹配类型(exact/semantic/keyword)',
  `match_score` decimal(3,2) DEFAULT NULL COMMENT '匹配分数',
  `user_feedback` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '用户反馈(positive/negative)',
  `response_time_ms` int DEFAULT NULL COMMENT '响应时间(毫秒)',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  PRIMARY KEY (`id`),
  KEY `idx_qa_pair_time` (`qa_pair_id`, `create_time`),
  KEY `idx_match_type` (`match_type`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='QA命中日志表';

-- ----------------------------
-- Table structure for ai_model
-- ----------------------------
DROP TABLE IF EXISTS `ai_model`;
CREATE TABLE `ai_model` (
                            `id` bigint NOT NULL COMMENT '主键',
                            `type` varchar(255) COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '模型类型（推理、chat等）',
                            `name` varchar(255) COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '模型名称',
                            `platform` bigint DEFAULT NULL COMMENT '所属平台',
                            `enable` int DEFAULT NULL COMMENT '是否启用',
                            `max_input_tokens` bigint DEFAULT NULL COMMENT '最大输入长度',
                            `max_output_tokens` bigint DEFAULT NULL COMMENT '最大输出长度',
                            `config` json DEFAULT NULL COMMENT '模型配置',
                            `create_time` datetime DEFAULT NULL,
                            `create_by` varchar(255) COLLATE utf8mb4_general_ci DEFAULT NULL,
                            `update_time` datetime DEFAULT NULL,
                            `update_by` varchar(255) COLLATE utf8mb4_general_ci DEFAULT NULL,
                            `tenant_id` bigint DEFAULT NULL,
                            PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

DROP TABLE IF EXISTS `ai_conversations`;
CREATE TABLE `ai_conversations` (
                                    `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '主键ID',
                                    `conversation_uuid` VARCHAR(36) NOT NULL COMMENT '会话UUID',

                                    `tenant_id` BIGINT DEFAULT NULL COMMENT '租户ID（多租户隔离）',
                                    `app_id` BIGINT NOT NULL COMMENT '应用ID（关联ai_app表）',
                                    `end_user_id` BIGINT DEFAULT NULL COMMENT '终端用户ID',

                                    `name` VARCHAR(255) DEFAULT NULL COMMENT '会话标题（自动生成或用户修改）',
                                    `status` VARCHAR(20) DEFAULT 'active' COMMENT '会话状态：active-活跃, archived-已归档, deleted-已删除',

                                    `message_count` INT DEFAULT 0 COMMENT '消息总数（包括用户和AI消息）',
                                    `total_tokens` INT DEFAULT 0 COMMENT '总Token消耗（累计）',
                                    `total_cost` DECIMAL(10, 6) DEFAULT 0.000000 COMMENT '总成本',

                                    `first_message_at` DATETIME DEFAULT NULL COMMENT '首条消息时间（用于排序）',
                                    `last_message_at` DATETIME DEFAULT NULL COMMENT '最后消息时间（用于超时判断）',
                                    `archived_at` DATETIME DEFAULT NULL COMMENT '归档时间',
                                    `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
                                    `create_by` VARCHAR(64) DEFAULT NULL COMMENT '创建人',
                                    `update_time` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
                                    `update_by` VARCHAR(64) DEFAULT NULL COMMENT '更新人',

                                    PRIMARY KEY (`id`) USING BTREE,
                                    UNIQUE KEY `uk_conversation_uuid` (`conversation_uuid`) COMMENT '会话UUID唯一索引',
                                    KEY `idx_tenant_app` (`tenant_id`, `app_id`) COMMENT '租户+应用联合索引（常用查询）',
                                    KEY `idx_end_user` (`end_user_id`) COMMENT '用户索引（按用户查询会话列表）',
                                    KEY `idx_status` (`status`) COMMENT '状态索引（查询活跃/归档会话）',
                                    KEY `idx_last_message` (`last_message_at`) COMMENT '最后消息时间索引（超时清理）'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='AI会话表';

DROP TABLE IF EXISTS `ai_messages`;
CREATE TABLE `ai_messages` (
                               `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '主键ID',
                               `message_uuid` VARCHAR(36) NOT NULL COMMENT '消息UUID',

                               `conversation_id` BIGINT NOT NULL COMMENT '所属会话ID（外键关联ai_conversations）',
                               `instance_id` BIGINT DEFAULT NULL COMMENT '实例ID（工作流或Agent实例，预留字段，后续集成使用）',
                               `instance_type` VARCHAR(20) DEFAULT NULL COMMENT '实例类型：workflow-工作流, agent-智能体（预留字段，后续集成使用）',

                               `role` VARCHAR(20) NOT NULL COMMENT '角色：user-用户, assistant-助手, system-系统',
                               `message_type` VARCHAR(20) DEFAULT 'text' COMMENT '消息类型：text-文本, image-图片, file-文件, audio-音频, video-视频（当前仅支持text）',
                               `content` LONGTEXT COMMENT '消息内容（支持大文本）',

                               `model_provider` VARCHAR(50) DEFAULT NULL COMMENT '模型提供商（如openai、anthropic，仅用于计费审计）',
                               `model_id` VARCHAR(100) DEFAULT NULL COMMENT '模型ID（如gpt-4、claude-3.5-sonnet，仅用于计费审计）',
                               `prompt_tokens` INT DEFAULT 0 COMMENT '输入Token数（Prompt）',
                               `completion_tokens` INT DEFAULT 0 COMMENT '输出Token数（Completion）',
                               `total_tokens` INT DEFAULT 0 COMMENT '总Token数（prompt_tokens + completion_tokens）',
                               `total_cost` DECIMAL(10, 6) DEFAULT 0.000000 COMMENT '总成本',

                               `status` VARCHAR(20) DEFAULT 'completed' COMMENT '状态：pending-待处理, completed-已完成, failed-失败',
                               `error_message` TEXT DEFAULT NULL COMMENT '错误信息（status=failed时记录）',

                               `metadata` JSON DEFAULT NULL COMMENT '扩展元数据（JSON格式，预留扩展，如附件信息等）',

                               `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
                               `completed_at` DATETIME DEFAULT NULL COMMENT '完成时间',

                               PRIMARY KEY (`id`) USING BTREE,
                               UNIQUE KEY `uk_message_uuid` (`message_uuid`) COMMENT '消息UUID唯一索引',
                               KEY `idx_conversation_time` (`conversation_id`, `create_time`) COMMENT '会话+时间联合索引（查询历史消息，最常用）',
                               KEY `idx_instance` (`instance_id`, `instance_type`) COMMENT '实例索引（追溯执行上下文，预留）'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='AI消息表';

DROP TABLE IF EXISTS `ai_conversation_state`;
CREATE TABLE `ai_conversation_state` (
                               `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '主键ID',
                               `conversation_id` BIGINT NOT NULL COMMENT '会话ID（关联 ai_conversations.id）',
                               `app_id` BIGINT DEFAULT NULL COMMENT '应用ID（关联 ai_app.id）',
                               `vars_json` JSON DEFAULT NULL COMMENT '会话变量快照（JSON）',
                               `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
                               `create_by` VARCHAR(64) DEFAULT NULL COMMENT '创建人',
                               `update_time` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
                               `update_by` VARCHAR(64) DEFAULT NULL COMMENT '更新人',

                               PRIMARY KEY (`id`) USING BTREE,
                               UNIQUE KEY `uk_conversation` (`conversation_id`) COMMENT '会话唯一索引'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='会话变量状态表';

DROP TABLE IF EXISTS `ai_agent`;
CREATE TABLE `ai_agent` (
                            `id` bigint NOT NULL COMMENT '主键',
                            `app_id` bigint NOT NULL COMMENT '所属应用ID',
                            `name` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT 'Agent名称',
                            `status` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT 'draft' COMMENT '状态(draft/published)',
                            `spec_json` json NOT NULL COMMENT 'Agent 配置',
                            `tenant_id` bigint DEFAULT NULL COMMENT '租户ID',
                            `create_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL,
                            `create_time` datetime DEFAULT CURRENT_TIMESTAMP,
                            `update_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL,
                            `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
                            PRIMARY KEY (`id`) USING BTREE,
                            KEY `idx_app_status` (`app_id`,`status`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

SET FOREIGN_KEY_CHECKS = 1;
