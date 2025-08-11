package com.art.ai.service.workflow.domain.node;

/**
 * @author fxz
 * @since 2025/8/10 14:55
 */
public interface NodeConstants {

	/**
	 * 开始节点
	 */
	String START_NODE = "start";

	/**
	 * 模版渲染节点
	 */
	String TEMPLATE_RENDER_NODE = "template";

	/**
	 * LLM 节点
	 */
	String LLM_NODE = "llm";

	/**
	 * 代码执行节点
	 */
	String CODE_NODE = "code";

	/**
	 * 条件判断节点
	 */
	String CONDITION_NODE = "condition";

	/**
	 * 结束节点
	 */
	String OUTPUT_NODE = "output";

	/**
	 * HTTP 请求节点
	 */
	String HTTP_NODE = "http";

	/**
	 * 知识库检索节点
	 */
	String KNOWLEDGE_RETRIEVAL_NODE = "knowledge";

	/**
	 * 变量替换
	 */
	String VARIABLE_REPLACE_NODE = "variable";

}
