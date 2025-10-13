package com.art.ai.service.dataset.rag.fusion.impl;

import com.art.ai.service.dataset.rag.fusion.FusionService;
import com.art.ai.service.dataset.rag.pipeline.PipelineConfig;
import com.art.ai.service.dataset.rag.retrieval.entity.RetrievalResult;
import com.art.ai.service.dataset.rag.retrieval.entity.RetrievalType;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 默认融合服务实现 统一实现所有融合策略
 *
 * @author fxz
 * @since 2025/10/05
 */
@Slf4j
@Service
public class DefaultFusionService implements FusionService {

	@Override
	public List<RetrievalResult> fuse(List<RetrievalResult> results, PipelineConfig.FusionConfig config) {
		if (results.isEmpty()) {
			return results;
		}

		log.info("融合策略，使用默认的RRF融合");
		List<RetrievalResult> fusedResults = performRRFFusion(results, config);

		int maxResults = config.getMaxResults() != null ? config.getMaxResults() : fusedResults.size();
		return fusedResults.stream().limit(maxResults).collect(Collectors.toList());
	}

	@Override
	public List<String> getSupportedStrategies() {
		return List.of("rrf");
	}

	/**
	 * 多来源结果RRF融合
	 * <p/>
	 * 根据结果的排名计算分数，公式为 1 / (rank + k)，k 是一个配置参数
	 */
	private List<RetrievalResult> performRRFFusion(List<RetrievalResult> results, PipelineConfig.FusionConfig config) {
		int k = config.getRrfK() != null ? config.getRrfK() : 60;

		// 按召回类型分组并排序
		Map<RetrievalType, List<RetrievalResult>> typeGroups = results.stream()
			.collect(Collectors.groupingBy(RetrievalResult::getRetrievalType));

		Map<String, Double> rrfScores = new HashMap<>();
		Map<String, RetrievalResult> resultMap = new HashMap<>();

		for (Map.Entry<RetrievalType, List<RetrievalResult>> entry : typeGroups.entrySet()) {
			List<RetrievalResult> typeResults = entry.getValue();
			typeResults.sort((a, b) -> b.getScore().compareTo(a.getScore()));

			for (int i = 0; i < typeResults.size(); i++) {
				RetrievalResult result = typeResults.get(i);
				String key = generateResultKey(result);
				double rrfScore = 1.0 / (i + 1 + k);

				rrfScores.merge(key, rrfScore, Double::sum);
				resultMap.put(key, result);
			}
		}

		return rrfScores.entrySet().stream().map(entry -> {
			String key = entry.getKey();
			double score = entry.getValue();
			RetrievalResult result = resultMap.get(key);

			RetrievalResult fusedResult = cloneResult(result);
			fusedResult.setScore(BigDecimal.valueOf(score));
			fusedResult.setRetrievalType(result.getRetrievalType());
			fusedResult.getMetadata().put("fusion_method", "rrf");
			fusedResult.getMetadata().put("original_type", result.getRetrievalType().getCode());
			fusedResult.getMetadata().put("rrf_score", score);
			fusedResult.getMetadata().put("original_score", result.getScore().doubleValue());

			return fusedResult;
		}).sorted((a, b) -> b.getScore().compareTo(a.getScore())).collect(Collectors.toList());
	}

	/**
	 * 生成结果唯一键
	 */
	private String generateResultKey(RetrievalResult result) {
		return "content_" + result.getContent().hashCode();
	}

	/**
	 * 克隆结果对象
	 */
	private RetrievalResult cloneResult(RetrievalResult original) {
		return RetrievalResult.builder()
			.documentId(original.getDocumentId())
			.segmentId(original.getSegmentId())
			.content(original.getContent())
			.score(original.getScore())
			.retrievalType(original.getRetrievalType())
			.metadata(new HashMap<>(original.getMetadata()))
			.build();
	}

}