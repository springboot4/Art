package com.art.ai.service.dataset.rag.pipeline;

import com.art.ai.service.dataset.rag.retrieval.entity.RetrievalRequest;
import com.art.ai.service.dataset.rag.retrieval.entity.RetrievalResponse;

/**
 * 召回管道接口 定义了完整的召回流程：召回 -> 融合 -> 重排序 -> 后处理
 *
 * @author fxz
 * @since 2025/10/05
 */
public interface RetrievalPipeline {

	/**
	 * 执行完整的召回管道
	 */
	RetrievalResponse execute(RetrievalRequest request);

}