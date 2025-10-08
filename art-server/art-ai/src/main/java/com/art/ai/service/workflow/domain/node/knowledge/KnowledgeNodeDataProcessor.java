package com.art.ai.service.workflow.domain.node.knowledge;

import com.art.ai.service.dataset.rag.retrieval.entity.RetrievalRequest;
import com.art.ai.service.dataset.rag.retrieval.entity.RetrievalResponse;
import com.art.ai.service.dataset.rag.retrieval.entity.RetrievalResult;
import com.art.ai.service.dataset.rag.retrieval.service.KnowledgeRetrievalService;
import com.art.ai.service.workflow.NodeState;
import com.art.ai.service.workflow.WorkFlowContext;
import com.art.ai.service.workflow.domain.node.NodeDataProcessor;
import com.art.ai.service.workflow.domain.node.NodeOutputVariable;
import com.art.ai.service.workflow.domain.node.NodeProcessResult;
import com.art.ai.service.workflow.variable.VariableDataType;
import com.art.ai.service.workflow.variable.VariableRenderUtils;
import com.art.core.common.util.SpringUtil;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 知识检索节点处理器
 *
 * @author fxz
 * @since 2025/8/10 16:50
 */
@Slf4j
@Data
public class KnowledgeNodeDataProcessor extends NodeDataProcessor<KnowledgeNodeConfig> {

	@Override
	public NodeProcessResult process(WorkFlowContext workFlowContext, NodeState nodeState) {
		log.info("Knowledge node processing started");

		try {
			// 1. 初始化节点输入变量
			Map<String, Object> inputs = initNodeInputsByReference(workFlowContext.getPool(), getConfig());

			// 2. 获取配置
			KnowledgeNodeConfig config = getConfig();
			validateConfig(config);

			// 3. 渲染查询字符串
			String renderedQuery = VariableRenderUtils.format(config.getQuery(), inputs);
			log.debug("Rendered query: {}", renderedQuery);

			// 4. 构建检索请求
			RetrievalRequest retrievalRequest = buildRetrievalRequest(config, renderedQuery);

			// 5. 执行知识库检索
			KnowledgeRetrievalService retrievalService = SpringUtil.getBean(KnowledgeRetrievalService.class);
			RetrievalResponse response = retrievalService.retrieve(retrievalRequest);

			// 6. 后处理检索结果
			List<RetrievalResult> finalResults = response.getResults();

			// 7. 构建节点输出变量
			List<NodeOutputVariable> outputs = buildOutputVariables(finalResults);

			log.info("Knowledge node processing completed,  results: {}", finalResults.size());

			return NodeProcessResult.builder().outputVariables(outputs).build();
		}
		catch (Exception e) {
			log.error("Knowledge node processing failed", e);
			throw new RuntimeException("知识检索节点执行失败: " + e.getMessage(), e);
		}
	}

	/**
	 * 验证配置
	 */
	private void validateConfig(KnowledgeNodeConfig config) {
		if (config.getQuery() == null || config.getQuery().trim().isEmpty()) {
			throw new IllegalArgumentException("查询字符串不能为空");
		}
		if (config.getDatasetIds() == null || config.getDatasetIds().isEmpty()) {
			throw new IllegalArgumentException("数据集ID列表不能为空");
		}
		if (config.getRetrievalTypes() == null || config.getRetrievalTypes().isEmpty()) {
			throw new IllegalArgumentException("检索类型列表不能为空");
		}
	}

	/**
	 * 构建检索请求
	 */
	private RetrievalRequest buildRetrievalRequest(KnowledgeNodeConfig config, String query) {
		return RetrievalRequest.builder()
			.query(query)
			.datasetIds(config.getDatasetIds())
			.retrievalTypes(config.getRetrievalTypes())
			.build();
	}

	/**
	 * 构建节点输出变量
	 */
	private List<NodeOutputVariable> buildOutputVariables(List<RetrievalResult> results) {
		List<NodeOutputVariable> outputs = new ArrayList<>();

		List<Map<String, Object>> resultList = results.stream().map(r -> {
			Map<String, Object> item = new java.util.HashMap<>();
			item.put("content", r.getContent());
			item.put("score", r.getScore());
			return item;
		}).collect(Collectors.toList());
		outputs.add(new NodeOutputVariable("results", VariableDataType.ARRAY, resultList));

		return outputs;
	}

}
