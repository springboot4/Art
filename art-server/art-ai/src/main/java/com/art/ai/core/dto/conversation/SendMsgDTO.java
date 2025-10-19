package com.art.ai.core.dto.conversation;

import com.art.ai.core.dto.WorkflowRunDTO;
import com.art.ai.service.workflow.variable.SystemVariableKey;
import lombok.Data;

/**
 * @author fxz
 * @since 2025/10/18 23:05
 */
@Data
public class SendMsgDTO {

	private WorkflowRunDTO workflowRunInfo;

	private String conversationId;

	public String getUserQuery() {
		return (String) workflowRunInfo.getSystems().get(SystemVariableKey.QUERY.getKey());
	}

}
