package com.art.ai.dao.dataobject;

import com.art.mybatis.common.base.BaseEntity;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serial;

/**
 * @author fxz
 * @date 2025-09-07
 */
@Data
@TableName("ai_datasets")
public class AiDatasetsDO extends BaseEntity {

	@Serial
	private static final long serialVersionUID = -1L;

	private Long id;

	private Long tenantId;

	private String name;

	private String description;

	/** 权限(公开或者非公开) */
	private String permission;

	/** 数据源类型(文件上传) */
	private String dataSourceType;

	/** 索引策略(高质量或经济) */
	private String indexingTechnique;

	private String embeddingModel;

	private String embeddingModelProvider;

	/** 关联的集合id */
	private Long collectionBindingId;

	/** 检索配置(混合检索或向量检索) */
	private String retrievalModel;

}
