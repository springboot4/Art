package com.art.ai.service.workflow.domain.node.llm.strategy;

import com.art.ai.core.constants.ModelFeature;
import com.art.ai.dao.dataobject.AiModelDO;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;

/**
 * 结构化输出策略选择器
 * <p>
 * 根据模型特性自动选择最优策略
 *
 * @author fxz
 */
@Slf4j
@UtilityClass
public class StructuredOutputStrategySelector {

	/**
	 * 自动选择策略
	 * @param model 模型
	 * @return 选择的策略
	 */
	public StructuredOutputStrategy select(AiModelDO model) {
		if (model == null) {
			log.warn("模型为空，使用兜底策略 PROMPT");
			return StructuredOutputStrategy.PROMPT;
		}

		// 策略 1：检查是否支持原生结构化输出
		if (model.hasFeature(ModelFeature.STRUCTURED_OUTPUT_NATIVE)) {
			log.debug("模型 {} 支持原生结构化输出，使用策略 NATIVE", model.getName());
			return StructuredOutputStrategy.NATIVE;
		}

		// 策略 2：检查是否支持 JSON Mode
		if (model.hasFeature(ModelFeature.JSON_MODE)) {
			log.debug("模型 {} 支持 JSON Mode，使用策略 JSON_MODE", model.getName());
			return StructuredOutputStrategy.JSON_MODE;
		}

		// 策略 3：兜底策略
		log.debug("模型 {} 不支持特殊特性，使用兜底策略 PROMPT", model.getName());
		return StructuredOutputStrategy.PROMPT;
	}

}
