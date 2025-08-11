package com.art.ai.service.workflow.domain.node.condition;

import com.art.ai.service.workflow.domain.node.NodeConfig;
import lombok.Data;

import java.util.List;

/**
 * @author fxz
 * @since 2025/8/10 15:43
 */
@Data
public class ConditionNodeConfig extends NodeConfig {

	private List<ConditionConfig> conditions;

	@Data
	public static class ConditionConfig {

		private String id;

		private String expression;

		private String label;

		private String description;

	}

}
