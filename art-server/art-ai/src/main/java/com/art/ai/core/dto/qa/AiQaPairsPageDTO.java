package com.art.ai.core.dto.qa;

import com.art.core.common.model.PageParam;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serial;

/**
 * QA问答对分页查询DTO
 *
 * @author fxz
 * @since 2025/10/12
 */
@Schema(title = "QA问答对分页查询")
@Data
@EqualsAndHashCode(callSuper = true)
public class AiQaPairsPageDTO extends PageParam {

	@Serial
	private static final long serialVersionUID = -1L;

	@Schema(description = "数据集ID")
	private Long datasetId;

	@Schema(description = "关键词搜索(问题/答案)")
	private String question;

	@Schema(description = "是否启用")
	private Boolean enabled;

	@Schema(description = "优先级")
	private Integer priority;

	@Schema(description = "是否已向量化")
	private Boolean vectorIndexed;

}
