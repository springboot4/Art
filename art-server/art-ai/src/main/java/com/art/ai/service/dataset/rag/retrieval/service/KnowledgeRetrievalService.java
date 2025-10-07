package com.art.ai.service.dataset.rag.retrieval.service;

import com.art.ai.core.dto.dataset.AiDatasetsDTO;
import com.art.ai.service.dataset.rag.pipeline.impl.DefaultRetrievalPipeline;
import com.art.ai.service.dataset.rag.retrieval.entity.RetrievalRequest;
import com.art.ai.service.dataset.rag.retrieval.entity.RetrievalResponse;
import com.art.ai.service.dataset.service.AiDatasetsService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * 知识库召回服务
 *
 * @author fxz
 * @since 2025/10/05
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class KnowledgeRetrievalService {

	private final DefaultRetrievalPipeline retrievalPipeline;

	private final AiDatasetsService aiDatasetsService;

	/**
	 * 执行知识库召回测试 - 委托给Pipeline处理
	 */
	public RetrievalResponse retrieve(RetrievalRequest request) {
		validateRequest(request);

		AiDatasetsDTO dataset = aiDatasetsService.findById(request.getDatasetId());
		if (dataset == null) {
			throw new IllegalArgumentException("数据集不存在: " + request.getDatasetId());
		}

		return retrievalPipeline.execute(request);
	}

	/**
	 * 验证请求参数
	 */
	private void validateRequest(RetrievalRequest request) {
		if (request.getQuery() == null || request.getQuery().trim().isEmpty()) {
			throw new IllegalArgumentException("查询语句不能为空");
		}
		if (request.getDatasetId() == null) {
			throw new IllegalArgumentException("数据集ID不能为空");
		}
		if (request.getRetrievalTypes() == null || request.getRetrievalTypes().isEmpty()) {
			throw new IllegalArgumentException("召回类型不能为空");
		}
	}

}