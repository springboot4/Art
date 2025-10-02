package com.art.ai.core.dto.document;

import com.art.core.common.model.BasePageEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.time.LocalDateTime;

/**
 * @author fxz
 * @date 2025-09-09
 */
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Schema(title = "")
@Data
public class AiDocumentsPageDTO extends BasePageEntity {

	@Serial
	private static final long serialVersionUID = -1L;

	@Schema(description = "")
	private String id;

	@Schema(description = "数据集主键")
	private String datasetId;

	@Schema(description = "文件桶名称")
	private String bucketName;

	@Schema(description = "文件名")
	private String fileName;

	@Schema(description = "文档标题")
	private String title;

	@Schema(description = "文档摘要")
	private String brief;

	@Schema(description = "文档内容")
	private String content;

	@Schema(description = "向量化状态")
	private Integer embeddingStatus;

	@Schema(description = "向量化状态改变时间")
	private LocalDateTime embeddingStatusChangeTime;

	@Schema(description = "图谱化状态")
	private Integer graphicalStatus;

	@Schema(description = "图谱状态改变时间")
	private LocalDateTime graphicalStatusChangeTime;

}