package com.art.ai.service.workflow.callback;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * @author fxz
 */
@Builder
@Getter
@Setter
public class CallbackData {

	/**
	 * 执行状态:{@link com.art.ai.service.workflow.domain.node.NodeStatus}
	 */
	private String nodeStatus;

	/**
	 * 失败原因
	 */
	private String reason;

	/**
	 * 节点类型
	 */
	private String nodeType;

	/**
	 * 节点ID
	 */
	private String nodeId;

	/**
	 * 执行耗时 节点执行的时间（毫秒）
	 */
	private Long nodeDuration;

	/**
	 * 下一个节点ID
	 */
	private List<String> nextNodeIds;

	/**
	 * Token使用情况
	 */
	private Object tokens;

	/**
	 * 是否结束
	 */
	private boolean flowEnd;

	/**
	 * 输出内容
	 */
	private String content;

	/**
	 * 是否思考中
	 */
	private Boolean isThinking;

}