package com.art.ai.service.dataset.rag.ner;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 识别的实体
 *
 * @author fxz
 * @since 2025/10/05
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RecognizedEntity {

	/**
	 * 实体名称
	 */
	private String name;

	/**
	 * 实体类型
	 */
	private EntityType type;

	/**
	 * 置信度分数 (0-1)
	 */
	private double confidence;

	/**
	 * 实体来源
	 */
	private EntitySource source;

}