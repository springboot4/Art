package com.art.ai.service.dataset.rag.rerank;

import com.art.ai.service.dataset.rag.retrieval.entity.RetrievalResult;

import java.util.List;

/**
 * 重排序器接口
 *
 * @author fxz
 * @since 2025/10/05
 */
public interface Reranker {

	/**
	 * 对召回结果进行重排序
	 * @param query 用户查询
	 * @param results 初始召回结果
	 * @param config 重排序配置
	 * @return 重排序后的结果
	 */
	List<RetrievalResult> rerank(String query, List<RetrievalResult> results, RerankerConfig config);

	/**
	 * 获取重排序器类型
	 */
	RerankerType getType();

	/**
	 * 是否支持该配置
	 */
	boolean supports(RerankerConfig config);

}