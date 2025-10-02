package com.art.ai.core.dto.document;

import com.art.core.common.model.BasePageEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author fxz
 * @date 2025-10-02
 */
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Schema(title = "")
@Data
public class AiDocumentSegmentPageDTO extends BasePageEntity {

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

	@Schema(description = "")
	private String segmentType;

}