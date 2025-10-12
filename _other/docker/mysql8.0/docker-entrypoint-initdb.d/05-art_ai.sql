
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

SET FOREIGN_KEY_CHECKS = 1;
