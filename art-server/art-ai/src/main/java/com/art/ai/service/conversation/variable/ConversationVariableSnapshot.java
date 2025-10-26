package com.art.ai.service.conversation.variable;

import java.util.Map;

/**
 * 会话变量快照
 */
public record ConversationVariableSnapshot(Map<String, Object> variables) {
}
