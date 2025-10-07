package com.art.ai.service.dataset.rag.rerank;

/**
 * 重排序器类型
 *
 * @author fxz
 * @since 2025/10/05
 */
public enum RerankerType {

	/**
	 * Cross-Encoder重排序 - 使用交叉编码器对query-document对进行评分
	 */
	CROSS_ENCODER("cross_encoder", "交叉编码器重排序"),

	/**
	 * LLM重排序 - 使用大语言模型对相关性进行评分
	 */
	LLM_BASED("llm_based", "LLM重排序"),

	/**
	 * 多样性重排序 - 基于MMR(Maximal Marginal Relevance)算法
	 */
	DIVERSITY("diversity", "多样性重排序"),

	/**
	 * 时间衰减重排序 - 考虑文档的时间因素
	 */
	TEMPORAL("temporal", "时间衰减重排序"),

	/**
	 * 混合重排序 - 组合多种重排序策略
	 */
	HYBRID("hybrid", "混合重排序"),

	/**
	 * 基于规则的重排序
	 */
	RULE_BASED("rule_based", "基于规则重排序");

	private final String code;

	private final String description;

	RerankerType(String code, String description) {
		this.code = code;
		this.description = description;
	}

	public String getCode() {
		return code;
	}

	public String getDescription() {
		return description;
	}

}