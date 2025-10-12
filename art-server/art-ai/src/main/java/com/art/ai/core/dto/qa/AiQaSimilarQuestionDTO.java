package com.art.ai.core.dto.qa;

import com.art.mybatis.common.base.BaseCreateEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serial;

/**
 * QA相似问题DTO
 *
 * @author fxz
 * @since 2025/10/12
 */
@Schema(title = "QA相似问题")
@Data
public class AiQaSimilarQuestionDTO extends BaseCreateEntity {

	@Serial
	private static final long serialVersionUID = -1L;

	@Schema(description = "主键")
	private Long id;

	@Schema(description = "关联的QA对ID")
	private Long qaPairId;

	@Schema(description = "相似问题")
	private String similarQuestion;

	@Schema(description = "租户ID")
	private Long tenantId;

}
