package com.art.ai.dao.dataobject;

import com.art.ai.service.dataset.rag.constant.EmbeddingStatusEnum;
import com.art.ai.service.dataset.rag.constant.GraphStatusEnum;
import com.art.mybatis.common.base.BaseEntity;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serial;
import java.time.LocalDateTime;

/**
 * @author fxz
 * @date 2025-09-09
 */
@Data
@TableName("ai_documents")
public class AiDocumentsDO extends BaseEntity {

	@Serial
	private static final long serialVersionUID = -1L;

	/** 数据集主键 */
	private Long datasetId;

	/** 文件桶名称 */
	private String bucketName;

	/** 文件名 */
	private String fileName;

	/** 文档标题 */
	private String title;

	/** 文档摘要 */
	private String brief;

	/** 文档内容 */
	private String content;

	/** 向量化状态 */
	private EmbeddingStatusEnum embeddingStatus;

	/** 向量化状态改变时间 */
	private LocalDateTime embeddingStatusChangeTime;

	/** 图谱化状态 */
	private GraphStatusEnum graphicalStatus;

	/** 图谱状态改变时间 */
	private LocalDateTime graphicalStatusChangeTime;

}
