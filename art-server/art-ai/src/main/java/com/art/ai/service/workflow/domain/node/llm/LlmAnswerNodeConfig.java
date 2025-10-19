package com.art.ai.service.workflow.domain.node.llm;

import com.art.ai.service.workflow.domain.node.NodeReferenceParameter;
import com.art.ai.service.workflow.variable.SystemVariableKey;
import com.art.ai.service.workflow.variable.VariableType;
import lombok.Data;
import org.apache.commons.collections4.CollectionUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * @author fxz
 * @since 2025/8/10 15:39
 */
@Data
public class LlmAnswerNodeConfig extends LlmNodeConfig {

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
