package com.art.ai.core.dto.document;

import com.art.ai.service.dataset.rag.constant.EmbeddingStatusEnum;
import com.art.ai.service.dataset.rag.constant.GraphStatusEnum;
import com.art.mybatis.common.base.BaseCreateEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serial;
import java.time.LocalDateTime;

/**
 * @author fxz
 * @date 2025-09-09
 */
@Schema(title = "")
@Data
public class AiDocumentsDTO extends BaseCreateEntity {

	@Serial
	private static final long serialVersionUID = -1L;

	@Schema(description = "")
	private Long id;

	@Schema(description = "数据集主键")
	private Long datasetId;

	@Schema(description = "文件桶名称")
	private String bucketName;

	@Schema(description = "文件名")
	private String fileName;

	@Schema(description = "原始文件名")
	private String originalFilename;

	@Schema(description = "文档标题")
	private String title;

	@Schema(description = "文档摘要")
	private String brief;

	@Schema(description = "文档内容")
	private String content;

	@Schema(description = "向量化状态")
	private EmbeddingStatusEnum embeddingStatus;

	@Schema(description = "向量化状态改变时间")
	private LocalDateTime embeddingStatusChangeTime;

	@Schema(description = "图谱化状态")
	private GraphStatusEnum graphicalStatus;

	@Schema(description = "图谱状态改变时间")
	private LocalDateTime graphicalStatusChangeTime;

}