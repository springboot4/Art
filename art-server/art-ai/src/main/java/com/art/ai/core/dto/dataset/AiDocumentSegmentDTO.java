package com.art.ai.core.dto.dataset;

import com.art.mybatis.common.base.BaseCreateEntity;

import io.swagger.v3.oas.annotations.media.Schema;

import lombok.Data;

import java.io.Serial;

/**
 * @author fxz
 * @date 2025-09-14
 */
@Schema(title = "")
@Data
public class AiDocumentSegmentDTO extends BaseCreateEntity {

	@Serial
	private static final long serialVersionUID = -1L;

	@Schema(description = "")
	private Long id;

	@Schema(description = "分段内容")
	private String segment;

	@Schema(description = "数据集id")
	private Long datasetId;

	@Schema(description = "文档id")
	private Long documentId;

	@Schema(description = "")
	private Long tenantId;

	private String segmentType;

}