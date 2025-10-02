package com.art.ai.dao.dataobject;

import com.art.mybatis.common.base.BaseEntity;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serial;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * @author fxz
 * @date 2025-09-07
 */
@Data
@TableName("ai_datasets")
public class AiDatasetsDO extends BaseEntity {

	@Serial
	private static final long serialVersionUID = -1L;

	private Long tenantId;

	private String name;

	private String description;

	private String permission;

	private String dataSourceType;

	private String embeddingModel;

	private String embeddingModelProvider;

	private String graphicModel;

	private String graphicModelProvider;

	/** 关联的集合id */
	private Long collectionBindingId;

	// todo fxz 检索的topk 阈值 、 重排序的模型或者全文和语义检索的权重
	/** 检索配置(混合检索或向量检索) */
	private String retrievalModel;

	public static String newDefaultName() {
		return LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd-HH:mm:ss"));
	}

}
