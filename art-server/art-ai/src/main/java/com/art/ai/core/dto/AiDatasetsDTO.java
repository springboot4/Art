package com.art.ai.core.dto;

import com.art.mybatis.common.base.BaseCreateEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serial;

/**
 * @author fxz
 * @date 2025-09-07
 */
@Schema(title = "")
@Data
public class AiDatasetsDTO extends BaseCreateEntity {

	@Serial
	private static final long serialVersionUID = -1L;

	@Schema(description = "")
	private Long id;

	@Schema(description = "")
	private Long tenantId;

	@Schema(description = "")
	private String name;

	@Schema(description = "")
	private String description;

	@Schema(description = "权限(公开或者非公开)")
	private String permission;

	@Schema(description = "数据源类型(文件上传)")
	private String dataSourceType;

	@Schema(description = "索引策略(高质量或经济)")
	private String indexingTechnique;

	@Schema(description = "")
	private String embeddingModel;

	@Schema(description = "")
	private String embeddingModelProvider;

	@Schema(description = "关联的集合id")
	private Long collectionBindingId;

	@Schema(description = "检索配置(混合检索或向量检索)")
	private String retrievalModel;

}