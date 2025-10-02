package com.art.ai.dao.dataobject;

import com.art.mybatis.common.base.BaseEntity;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serial;

/**
 * @author fxz
 * @date 2025-09-14
 */
@Data
@TableName("ai_document_segment")
public class AiDocumentSegmentDO extends BaseEntity {

	@Serial
	private static final long serialVersionUID = -1L;

	/** 分段内容 */
	private String segment;

	/** 数据集id */
	private Long datasetId;

	/** 文档id */
	private Long documentId;

	/** 租户id */
	private Long tenantId;

	/** 分段类型 */
	private String segmentType;

}
