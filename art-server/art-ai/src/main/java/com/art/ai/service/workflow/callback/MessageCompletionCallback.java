package com.art.ai.service.workflow.callback;

import com.art.ai.core.enums.MessageRoleEnum;
import lombok.Builder;
import lombok.Data;

import java.util.Map;

/**
 * 消息生成完成回调接口
 *
 * @author fxz
 */
@FunctionalInterface
public interface MessageCompletionCallback {

	/**
	 * 消息生成完成时的回调
	 * @param event 完成事件
	 */
	void onComplete(MessageCompletionEvent event);

	/**
	 * 消息完成事件
	 */
	@Data
	@Builder
	class MessageCompletionEvent {

		/**
		 * 节点ID
		 */
		private String nodeId;

		/**
		 * 节点名称
		 */
		private String nodeLabel;

		/**
		 * 消息角色
		 */
		private MessageRoleEnum role;

		/**
		 * 生成的完整内容
		 */
		private String content;

		/**
		 * 会话ID
		 */
		private Long conversationId;

		/**
		 * 工作流运行实例ID
		 */
		private Long instanceId;

		/**
		 * 模型ID
		 */
		private String modelId;

		/**
		 * 模型提供商
		 */
		private String modelProvider;

		/**
		 * 输入Token数
		 */
		private Integer promptTokens;

		/**
		 * 输出Token数
		 */
		private Integer completionTokens;

		/**
		 * 总Token数
		 */
		private Integer totalTokens;

		/**
		 * 扩展元数据
		 */
		private Map<String, Object> metadata;

	}

}
