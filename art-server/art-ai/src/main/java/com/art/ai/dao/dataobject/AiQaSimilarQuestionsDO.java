package com.art.ai.dao.dataobject;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * QA相似问题实体
 *
 * @author fxz
 * @since 2025/10/12
 */
@Data
@TableName("ai_qa_similar_questions")
public class AiQaSimilarQuestionsDO implements Serializable {

	@Serial
	private static final long serialVersionUID = -1L;

	/** 关联的QA对ID */
	private Long qaPairId;

	/** 相似问题 */
	private String similarQuestion;

	/** 相似问题Hash */
	private String similarHash;

	/** 来源类型(manual/auto) */
	private String sourceType;

	/**
	 * 主键
	 */
	@TableId(type = IdType.ASSIGN_ID)
	private Long id;

	/**
	 * 创建者
	 */
	@TableField(fill = FieldFill.INSERT)
	private String createBy;

	/**
	 * 创建时间
	 */
	@TableField(fill = FieldFill.INSERT)
	private LocalDateTime createTime;

}
