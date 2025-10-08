package com.art.ai.core.dto.retrieval;

import com.art.ai.service.dataset.rag.retrieval.entity.RetrievalType;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * 知识库召回测试DTO
 *
 * @author fxz
 * @since 2025/10/05
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "知识库召回测试请求")
public class KnowledgeRetrievalDTO {

	@Schema(description = "查询语句")
	@NotBlank(message = "查询语句不能为空")
	private String query;

	@Schema(description = "数据集ID")
	private Long datasetId;

	@Schema(description = "数据集ID集合")
	private List<Long> datasetIds;

	@Schema(description = "召回类型列表")
	@NotNull(message = "召回类型不能为空")
	private List<RetrievalType> retrievalTypes;

}