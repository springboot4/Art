package com.art.ai.service.workflow.domain.node.variable;

import cn.hutool.core.util.StrUtil;
import com.art.ai.service.conversation.variable.ConversationVariableService;
import com.art.ai.service.workflow.NodeState;
import com.art.ai.service.workflow.WorkFlowContext;
import com.art.ai.service.workflow.domain.node.NodeDataProcessor;
import com.art.ai.service.workflow.domain.node.NodeOutputVariable;
import com.art.ai.service.workflow.domain.node.NodeProcessResult;
import com.art.ai.service.workflow.variable.ConversationVariableWriteMode;
import com.art.ai.service.workflow.variable.VariableDataType;
import com.art.ai.service.workflow.variable.VariablePool;
import com.art.ai.service.workflow.variable.VariablePoolManager;
import com.art.core.common.exception.ArtException;
import com.art.core.common.util.SpringUtil;
import com.art.json.sdk.util.JacksonUtil;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * 会话变量写入节点
 *
 * @author fxz
 */
@Slf4j
public class ConversationVariableNodeDataProcessor extends NodeDataProcessor<ConversationVariableNodeConfig> {

	@Override
	public NodeProcessResult process(WorkFlowContext workFlowContext, NodeState nodeState) {
		ConversationVariableNodeConfig config = getConfig();
		if (config == null) {
			throw new ArtException("会话变量节点配置不能为空");
		}

		VariablePool variablePool = workFlowContext.getPool();
		Map<String, Object> inputs = initNodeInputsByReference(variablePool, config);

		List<ConversationVariableAssignment> assignments = config.getAssignments();
		if (assignments == null || assignments.isEmpty()) {
			throw new ArtException("会话变量节点未配置赋值项");
		}

		Map<String, Object> declaration = workFlowContext.getConversationVariableDeclaration();
		List<NodeOutputVariable> outputs = new ArrayList<>();
		for (ConversationVariableAssignment assignment : assignments) {
			String targetKey = assignment.getTargetKey();
			if (StrUtil.isBlank(targetKey)) {
				throw new ArtException("会话变量节点存在未配置目标变量的赋值项");
			}

			AssignmentSource source = assignment.getSource();
			if (source == null || source.getType() == null) {
				throw new ArtException("会话变量赋值项未配置值来源: " + targetKey);
			}

			if (declaration == null || !declaration.containsKey(targetKey)) {
				throw new ArtException("尝试写入未声明的会话变量: " + targetKey);
			}

			Object value = resolveValue(source, inputs);
			Map<String, Object> validatePayload = new HashMap<>();
			validatePayload.put(targetKey, JacksonUtil.toJsonString(value));
			SpringUtil.getBean(ConversationVariableService.class).validateByDeclaration(declaration, validatePayload);

			ConversationVariableWriteMode writeMode = Objects.requireNonNullElse(assignment.getWriteMode(),
					ConversationVariableWriteMode.SET);
			if (writeMode == ConversationVariableWriteMode.MERGE && !(value instanceof Map<?, ?>)) {
				throw new ArtException("MERGE 模式要求写入对象类型的值: " + targetKey);
			}

			try {
				VariablePoolManager.updateConversationVariable(targetKey, value, writeMode, variablePool);
			}
			catch (IllegalStateException ex) {
				throw new ArtException(ex.getMessage());
			}

			outputs.add(new NodeOutputVariable(targetKey, deduceType(value), value));
		}

		return NodeProcessResult.builder().outputVariables(outputs).build();
	}

	private Object resolveValue(AssignmentSource source, Map<String, Object> inputs) {
		return switch (source.getType()) {
			case CONSTANT -> source.getConstant();
			case REFERENCE -> {
				String referenceKey = source.getReferenceKey();
				if (StrUtil.isBlank(referenceKey)) {
					throw new ArtException("引用类型的值来源必须提供 referenceKey");
				}
				yield inputs.getOrDefault(referenceKey, null);
			}
		};
	}

	private String deduceType(Object value) {
		if (value == null) {
			return VariableDataType.OBJECT;
		}
		if (value instanceof Number) {
			return VariableDataType.NUMBER;
		}
		if (value instanceof Boolean) {
			return VariableDataType.BOOLEAN;
		}
		if (value instanceof List<?>) {
			return VariableDataType.ARRAY;
		}
		if (value instanceof Map<?, ?>) {
			return VariableDataType.OBJECT;
		}
		return VariableDataType.STRING;
	}

}
