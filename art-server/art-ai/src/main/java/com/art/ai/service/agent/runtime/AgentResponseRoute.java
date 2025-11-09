package com.art.ai.service.agent.runtime;

import com.art.ai.core.constants.ModelFeature;
import com.art.ai.dao.dataobject.AiModelDO;
import com.art.ai.service.workflow.domain.node.llm.strategy.StructuredOutputStrategy;
import com.art.ai.service.workflow.domain.node.llm.strategy.StructuredOutputStrategySelector;
import lombok.Getter;

/**
 * Agent 输出路由
 */
@Getter
public enum AgentResponseRoute {

	FUNCTION_CALL,

	STRUCTURED,

	JSON_MODE,

	PROMPT_JSON;

	public static AgentResponseRoute select(AiModelDO model) {
		if (model == null) {
			return PROMPT_JSON;
		}
		if (model.hasFeature(ModelFeature.FUNCTION_CALL)) {
			return FUNCTION_CALL;
		}
		StructuredOutputStrategy strategy = StructuredOutputStrategySelector.select(model);
		return switch (strategy) {
			case NATIVE -> STRUCTURED;
			case JSON_MODE -> JSON_MODE;
			case PROMPT -> PROMPT_JSON;
		};
	}

}
