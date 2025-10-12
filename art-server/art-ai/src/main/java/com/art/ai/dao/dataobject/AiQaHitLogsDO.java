package com.art.ai.dao.dataobject;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * QA命中日志实体
 *
 * @author fxz
 * @since 2025/10/12
 */
@Data
@TableName("ai_qa_hit_logs")
public class AiQaHitLogsDO implements Serializable {

	@Serial
	private static final long serialVersionUID = -1L;

	/** 主键 */
	@TableId(type = IdType.ASSIGN_ID)
	private Long id;

	/** 命中的QA对ID */
	private Long qaPairId;

	/** 用户查询 */
	private String userQuery;

	/** 匹配类型(exact/semantic) */
	private String matchType;

	/** 匹配分数 */
	private BigDecimal matchScore;

	/** 用户反馈(positive/negative) */
	private String userFeedback;

	/** 响应时间(毫秒) */
	private Integer responseTimeMs;

	/** 创建时间 */
	@TableField(fill = FieldFill.INSERT)
	private LocalDateTime createTime;

}
