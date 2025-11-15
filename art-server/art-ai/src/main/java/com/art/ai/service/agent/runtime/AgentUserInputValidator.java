package com.art.ai.service.agent.runtime;

import com.art.ai.service.agent.spec.AgentSpec;
import com.art.ai.service.workflow.domain.node.NodeInputVariable;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Agent 用户输入验证器 负责验证运行时提供的用户变量是否符合 AgentSpec 定义
 *
 * @author fxz
 * @since 2025-11-15
 */
@Slf4j
@Component
public class AgentUserInputValidator {

	/**
	 * 验证用户输入
	 * @param spec Agent 规格定义
	 * @param providedInputs 用户提供的输入
	 * @throws AgentUserInputException 验证失败时抛出
	 */
	public void validate(AgentSpec spec, Map<String, Object> providedInputs) {
		if (spec == null) {
			throw new IllegalArgumentException("AgentSpec 不能为空");
		}

		if (spec.getUserInputs() == null || spec.getUserInputs().isEmpty()) {
			log.debug("Agent 未定义用户输入变量，跳过验证");
			return;
		}

		if (providedInputs == null) {
			throw new AgentUserInputException("用户输入不能为 null");
		}

		List<String> errors = new ArrayList<>();

		// 1. 检查必填字段
		validateRequiredFields(spec, providedInputs, errors);

		// 2. 检查数据类型
		validateDataTypes(spec, providedInputs, errors);

		// 3. 如果有错误，抛出异常
		if (!errors.isEmpty()) {
			String errorMessage = String.join("; ", errors);
			log.error("用户输入验证失败: {}", errorMessage);
			throw new AgentUserInputException("用户输入验证失败: " + errorMessage);
		}

		log.debug("用户输入验证通过，共 {} 个变量", providedInputs.size());
	}

	/**
	 * 验证必填字段
	 */
	private void validateRequiredFields(AgentSpec spec, Map<String, Object> providedInputs, List<String> errors) {
		for (NodeInputVariable input : spec.getUserInputs()) {
			if (input.required()) {
				if (!providedInputs.containsKey(input.name())) {
					errors.add(String.format("缺少必填变量: %s (%s)", input.name(), input.displayName()));
				}
				else if (providedInputs.get(input.name()) == null) {
					errors.add(String.format("必填变量不能为 null: %s (%s)", input.name(), input.displayName()));
				}
			}
		}
	}

	/**
	 * 验证数据类型
	 */
	private void validateDataTypes(AgentSpec spec, Map<String, Object> providedInputs, List<String> errors) {
		for (Map.Entry<String, Object> entry : providedInputs.entrySet()) {
			String name = entry.getKey();
			Object value = entry.getValue();
			if (value == null) {
				continue;
			}

			spec.getUserInputs().stream().filter(input -> input.name().equals(name)).findFirst().ifPresent(inputDef -> {
				if (!isTypeCompatible(value, inputDef.dataType())) {
					errors.add(String.format("变量 %s 类型不匹配: 期望 %s, 实际 %s", name, inputDef.dataType(),
							value.getClass().getSimpleName()));
				}
			});
		}
	}

	/**
	 * 检查值的类型是否与期望类型兼容
	 * @param value 实际值
	 * @param expectedType 期望的数据类型（string, number, boolean, array, object, file）
	 * @return 是否兼容
	 */
	private boolean isTypeCompatible(Object value, String expectedType) {
		if (value == null) {
			return true;
		}

		return switch (expectedType) {
			case "string" -> value instanceof String;
			case "number" -> value instanceof Number;
			case "boolean" -> value instanceof Boolean;
			case "array" -> value instanceof List;
			case "object" -> value instanceof Map;
			case "file" -> value instanceof File || value instanceof String; // 支持文件路径字符串
			default -> {
				log.warn("未知的数据类型: {}, 默认通过验证", expectedType);
				yield true;
			}
		};
	}

}
