package com.art.ai.service.dataset.rag.rerank;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 重排序器工厂
 *
 * @author fxz
 * @since 2025/10/05
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class RerankerFactory {

	private final List<Reranker> rerankers;

	/**
	 * 获取支持指定配置的重排序器
	 */
	public Reranker getReranker(RerankerConfig config) {
		return rerankers.stream()
			.filter(reranker -> reranker.supports(config))
			.findFirst()
			.orElseThrow(() -> new IllegalArgumentException("没有重排序器支持配置: " + config.getType()));
	}

}