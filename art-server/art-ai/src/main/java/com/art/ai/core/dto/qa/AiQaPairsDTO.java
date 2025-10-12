package com.art.ai.core.dto.qa;

import com.art.mybatis.common.base.BaseEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serial;
import java.time.LocalDateTime;

/**
 * QA问答对DTO
 *
 * @author fxz
 * @since 2025/10/12
 */
@Schema(title = "QA问答对")
@Data
public class AiQaPairsDTO extends BaseEntity {

	@Serial
	private static final long serialVersionUID = -1L;

	@Schema(description = "主键")
	private Long id;

	@Schema(description = "所属数据集ID")
	private Long datasetId;

	@Schema(description = "问题")
	private String question;

	@Schema(description = "答案")
	private String answer;

	@Schema(description = "优先级(1-5)", example = "1")
	private Integer priority;

	@Schema(description = "是否启用", example = "true")
	private Boolean enabled;

	@Schema(description = "命中次数")
	private Integer hitCount;

	@Schema(description = "最后命中时间")
	private LocalDateTime lastHitTime;

	@Schema(description = "是否已向量化")
	private Boolean vectorIndexed;

	@Schema(description = "向量ID")
	private String vectorId;

	@Schema(description = "数据来源")
	private String sourceType;

	@Schema(description = "租户ID")
	private Long tenantId;

}
