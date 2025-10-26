package com.art.ai.service.workflow.domain.node.variable;

import com.art.ai.service.workflow.domain.node.NodeConfig;
import com.art.ai.service.workflow.domain.node.NodeReferenceParameter;
import com.art.ai.service.workflow.variable.SystemVariableKey;
import com.art.ai.service.workflow.variable.VariableType;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.apache.commons.collections4.CollectionUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * 会话变量写入节点配置
 *
 * @author fxz
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class ConversationVariableNodeConfig extends NodeConfig {

	/** 多个会话变量赋值配置 */
	private List<ConversationVariableAssignment> assignments;

	@Override
	public List<NodeReferenceParameter> getReferenceParameters() {
		List<NodeReferenceParameter> referenceParameters = super.getReferenceParameters();
		referenceParameters = CollectionUtils.isEmpty(referenceParameters) ? new ArrayList<>() : referenceParameters;

		NodeReferenceParameter parameter = new NodeReferenceParameter(VariableType.SYSTEM.getType(),
				SystemVariableKey.CONVERSATION_ID.getKey(), VariableType.SYSTEM);
		referenceParameters.add(parameter);

		return referenceParameters;
	}

}
