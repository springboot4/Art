package com.art.ai.service.agent.runtime;

import com.art.ai.service.agent.tool.ToolArgumentBinding;
import com.art.ai.service.workflow.variable.VariablePool;
import com.art.ai.service.workflow.variable.VariableSelector;
import com.art.ai.service.workflow.variable.VariableValue;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Agent工具参数绑定器 负责将变量池中的值绑定到工具参数
 *
 * @author fxz
 */
@Component
@RequiredArgsConstructor
public class AgentToolArgumentBinder {

	private final ObjectMapper objectMapper;

	/**
	 * 应用参数绑定
	 */
	public void applyArgumentBindings(List<ToolArgumentBinding> bindings, VariablePool variablePool,
			ObjectNode argsNode) {
		if (bindings == null || bindings.isEmpty()) {
			return;
		}
		for (ToolArgumentBinding binding : bindings) {
			if (binding == null || StringUtils.isBlank(binding.getField())) {
				continue;
			}

			String field = binding.getField();
			JsonNode existing = argsNode.get(field);
			boolean hasExisting = existing != null && !existing.isNull();
			boolean shouldOverride = binding.isOverride() || !hasExisting;
			if (!shouldOverride) {
				continue;
			}

			JsonNode value = resolveBindingValue(binding, variablePool);
			if (value == null || value.isNull()) {
				continue;
			}

			argsNode.set(field, cloneNode(value));
		}
	}

	private JsonNode resolveBindingValue(ToolArgumentBinding binding, VariablePool variablePool) {
		if (binding.getSelector() != null) {
			return resolveSelector(binding.getSelector(), variablePool);
		}

		if (binding.getConstant() != null) {
			return objectMapper.valueToTree(binding.getConstant());
		}

		return null;
	}

	private JsonNode resolveSelector(VariableSelector selector, VariablePool variablePool) {
		if (selector == null || variablePool == null) {
			return null;
		}

		return variablePool.get(selector).map(VariableValue::getValue).map(this::toJsonNode).orElse(null);
	}

	private JsonNode cloneNode(JsonNode value) {
		if (value == null) {
			return null;
		}

		return value.isContainerNode() ? value.deepCopy() : value;
	}

	private JsonNode toJsonNode(Object value) {
		if (value == null) {
			return objectMapper.nullNode();
		}
		if (value instanceof JsonNode jsonNode) {
			return jsonNode.deepCopy();
		}
		return objectMapper.valueToTree(value);
	}

}
