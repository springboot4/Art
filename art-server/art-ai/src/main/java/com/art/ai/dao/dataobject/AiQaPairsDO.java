package com.art.ai.dao.dataobject;

import com.art.mybatis.common.base.BaseEntity;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serial;
import java.time.LocalDateTime;

/**
 * QA问答对实体
 *
 * @author fxz
 * @since 2025/10/12
 */
@Data
@TableName("ai_qa_pairs")
public class AiQaPairsDO extends BaseEntity {

	@Serial
	private static final long serialVersionUID = -1L;

	/** 所属数据集ID */
	private Long datasetId;

	/** 问题 */
	private String question;

	/** 问题Hash(MD5) */
	private String questionHash;

	/** 答案 */
	private String answer;

	/** 优先级(1-5) */
	private Integer priority;

	/** 是否启用 */
	private Boolean enabled;

	/** 命中次数 */
	private Integer hitCount;

	/** 最后命中时间 */
	private LocalDateTime lastHitTime;

	/** 是否已向量化 */
	private Boolean vectorIndexed;

	/** 向量ID */
	private String vectorId;

	/** 来源类型(manual/llm/import) */
	private String sourceType;

	/** 租户ID */
	private Long tenantId;

}
