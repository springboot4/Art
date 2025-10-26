package com.art.ai.service.conversation.variable;

import java.util.Map;

/**
 * 会话变量服务
 *
 * @author fxz
 */
public interface ConversationVariableService {

	/**
	 * 初始化会话变量，按照工作流声明与持久化数据进行合并。
	 * @param conversationId 会话ID
	 * @param appId 应用ID
	 * @param declaredDefaults 工作流声明的变量默认值
	 * @return 会话变量快照
	 */
	ConversationVariableSnapshot initialize(Long conversationId, Long appId, Map<String, Object> declaredDefaults);

	/**
	 * 持久化会话变量数据。
	 * @param conversationId 会话ID
	 * @param appId 应用ID
	 * @param conversationVars 会话变量数据
	 */
	void persist(Long conversationId, Long appId, Map<String, Object> conversationVars);

	/**
	 * 根据声明过滤变量，仅保留声明中的顶级键。
	 */
	Map<String, Object> filterByDeclaration(Map<String, Object> declaredDefaults, Map<String, Object> source);

	/**
	 * 校验变量结构是否满足声明要求。
	 */
	void validateByDeclaration(Map<String, Object> declaredDefaults, Map<String, Object> toValidate);

}
