package com.art.ai.service.workflow.domain.node.variable;

import com.art.ai.service.workflow.variable.ConversationVariableWriteMode;
import lombok.Data;

/**
 * 会话变量赋值配置
 *
 * @author fxz
 */
@Data
public class ConversationVariableAssignment {

	/** 目标会话变量顶级 key */
	private String targetKey;

	/** 写入模式，默认 SET */
	private ConversationVariableWriteMode writeMode = ConversationVariableWriteMode.SET;

	/** 值来源 */
	private AssignmentSource source;

}
