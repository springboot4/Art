package com.art.ai.service.dataset.rag.fusion;

import com.art.ai.service.dataset.rag.pipeline.PipelineConfig;
import com.art.ai.service.dataset.rag.retrieval.entity.RetrievalResult;

import java.util.List;

/**
 * 融合服务接口 统一处理多种召回结果的融合逻辑
 *
 * @author fxz
 * @since 2025/10/05
 */
public interface FusionService {

	/**
	 * 融合结果列表（已混合类型）
	 * @param results 混合的结果列表
	 * @param config 融合配置
	 * @return 融合后的结果列表
	 */
	List<RetrievalResult> fuse(List<RetrievalResult> results, PipelineConfig.FusionConfig config);

}