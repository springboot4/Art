package com.art.ai.service.workflow.domain.node;

import lombok.Data;

import java.util.List;

/**
 * @author fxz
 * @since 2025/8/10 15:21
 */
@Data
public abstract class NodeConfig {

	private int timeout;

	private int retryCount;

	/**
	 * 引用的参数(开始节点没有) todo fxz 节点新增引用参数配置
	 */
	private List<NodeReferenceParameter> referenceParameters;

}
