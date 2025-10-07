package com.art.ai.service.dataset.rag.ner;

/**
 * 实体识别来源
 *
 * @author fxz
 * @since 2025/10/05
 */
public enum EntitySource {

	/**
	 * 基于大语言模型的实体识别
	 */
	LLM("llm", "大语言模型"),

	/**
	 * 基于规则的实体识别
	 */
	RULE("rule", "规则引擎"),

	/**
	 * 基于NER模型的实体识别
	 */
	NER_MODEL("ner_model", "NER模型"),

	/**
	 * 基于词典的实体识别
	 */
	DICTIONARY("dictionary", "词典匹配"),

	/**
	 * 混合方法
	 */
	HYBRID("hybrid", "混合方法");

	private final String code;

	private final String description;

	EntitySource(String code, String description) {
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