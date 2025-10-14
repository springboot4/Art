package com.art.ai.service.workflow.callback;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

/**
 * @author fxz
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@Builder
@Getter
@Setter
public class CallbackData {

	/**
	 * 执行状态:{@link com.art.ai.service.workflow.domain.node.NodeStatus}
	 */
	private String nodeStatus;

	/**
	 * 节点ID
	 */
	private String nodeId;

	/**
	 * 节点名称
	 */
	private String nodeName;

	/**
	 * 节点输出
	 */
	private String outputs;

	/**
	 * 分块内容
	 */
	private String chunk;

}