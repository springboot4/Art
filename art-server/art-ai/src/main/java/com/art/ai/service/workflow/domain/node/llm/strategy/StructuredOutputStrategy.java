package com.art.ai.service.workflow.domain.node.llm.strategy;

/**
 * 结构化输出策略
 *
 * @author fxz
 */
public enum StructuredOutputStrategy {

	/**
	 * 原生结构化输出（Structured Outputs） - 使用模型原生的 JSON Schema 支持
	 */
	NATIVE,

	/**
	 * JSON Mode + Prompt - 使用 JSON Mode 保证 JSON 合法性 - 通过 Prompt 引导符合 Schema
	 */
	JSON_MODE,

	/**
	 * 纯 Prompt Engineering - 仅通过 Prompt 引导输出 - 适用模型：所有模型（兜底方案）
	 */
	PROMPT;

}
