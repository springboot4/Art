

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

SET FOREIGN_KEY_CHECKS = 1;
