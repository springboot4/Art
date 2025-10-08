package com.art.ai.service.workflow.domain.node.knowledge;

import com.art.ai.service.dataset.rag.retrieval.entity.RetrievalType;
import com.art.ai.service.workflow.domain.node.NodeConfig;
import lombok.Data;

import java.util.List;

/**
 * 知识检索节点配置
 *
 * @author fxz
 * @since 2025/8/10 16:50
 */
@Data
public class KnowledgeNodeConfig extends NodeConfig {

	/**
	 * 查询字符串
	 */
	private String query;

	/**
	 * 数据集ID列表
	 */
	private List<Long> datasetIds;

	/**
	 * 检索类型列表
	 */
	private List<RetrievalType> retrievalTypes;

}
