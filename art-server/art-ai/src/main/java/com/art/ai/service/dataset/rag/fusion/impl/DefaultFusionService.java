package com.art.ai.service.dataset.rag.fusion.impl;

import com.art.ai.service.dataset.rag.fusion.FusionService;
import com.art.ai.service.dataset.rag.pipeline.PipelineConfig;
import com.art.ai.service.dataset.rag.retrieval.entity.RetrievalResult;
import com.art.ai.service.dataset.rag.retrieval.entity.RetrievalType;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
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

		String strategy = config.getStrategy();
		if (strategy == null) {
			strategy = "rrf";
		}

		List<RetrievalResult> fusedResults = switch (strategy.toLowerCase()) {
			case "rrf" -> performRRFFusion(results, config);
			case "weighted_sum" -> performWeightedSumFusion(results, config);
			case "comb_mnz" -> performCombMNZFusion(results, config);
			case "comb_sum" -> performCombSumFusion(results, config);
			case "simple_merge" -> performSimpleMerge(results);
			default -> {
				log.warn("不支持的融合策略: {}, 使用默认RRF策略", strategy);
				yield performRRFFusion(results, config);
			}
		};

		int maxResults = config.getMaxResults() != null ? config.getMaxResults() : fusedResults.size();
		return fusedResults.stream().limit(maxResults).collect(Collectors.toList());
	}

	@Override
	public List<String> getSupportedStrategies() {
		return Arrays.asList("rrf", "weighted_sum", "comb_mnz", "comb_sum", "simple_merge");
	}

	/**
	 * RRF融合
	 * <p/>
	 * 根据结果的排名计算分数，公式为 1 / (rank + k)，k 是一个配置参数，将不同召回类型的结果按排名分数加权融合，适合多来源结果的融合
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
			fusedResult.setRetrievalType(RetrievalType.HYBRID);
			fusedResult.getMetadata().put("fusion_method", "rrf");
			fusedResult.getMetadata().put("original_type", result.getRetrievalType().getCode());
			fusedResult.getMetadata().put("rrf_score", score);
			fusedResult.getMetadata().put("original_score", result.getScore().doubleValue());

			return fusedResult;
		}).sorted((a, b) -> b.getScore().compareTo(a.getScore())).collect(Collectors.toList());
	}

	/**
	 * 加权求和融合
	 * <p/>
	 * 根据每种召回类型的权重对结果分数进行加权求和。适合需要根据召回类型重要性调整分数的场景
	 */
	private List<RetrievalResult> performWeightedSumFusion(List<RetrievalResult> results,
			PipelineConfig.FusionConfig config) {
		Map<String, WeightedResult> mergedResults = new HashMap<>();

		for (RetrievalResult result : results) {
			String key = generateResultKey(result);
			double weight = getTypeWeight(result.getRetrievalType(), config);

			if (mergedResults.containsKey(key)) {
				WeightedResult existing = mergedResults.get(key);
				existing.addScore(result.getScore().doubleValue() * weight);
				existing.addType(result.getRetrievalType());
			}
			else {
				WeightedResult weightedResult = new WeightedResult(result);
				weightedResult.addScore(result.getScore().doubleValue() * weight);
				weightedResult.addType(result.getRetrievalType());
				mergedResults.put(key, weightedResult);
			}
		}

		return mergedResults.values().stream().map(wr -> {
			RetrievalResult result = cloneResult(wr.getResult());
			result.setScore(BigDecimal.valueOf(wr.getTotalScore()));
			result.setRetrievalType(RetrievalType.HYBRID);
			result.getMetadata().put("fusion_method", "weighted_sum");
			result.getMetadata().put("contributing_types", wr.getTypes());
			return result;
		}).sorted((a, b) -> b.getScore().compareTo(a.getScore())).collect(Collectors.toList());
	}

	/**
	 * CombMNZ融合
	 * <p/>
	 * 计算每个结果的分数总和，并乘以该结果出现的次数（即参与融合的来源数量）。适合强调结果多来源支持的场景
	 */
	private List<RetrievalResult> performCombMNZFusion(List<RetrievalResult> results,
			PipelineConfig.FusionConfig config) {
		Map<String, List<RetrievalResult>> groupedResults = results.stream()
			.collect(Collectors.groupingBy(this::generateResultKey));

		return groupedResults.values().stream().map(group -> {
			double sumScore = group.stream().mapToDouble(r -> r.getScore().doubleValue()).sum();
			double combMNZScore = sumScore * group.size();

			RetrievalResult result = cloneResult(group.get(0));
			result.setScore(BigDecimal.valueOf(combMNZScore));
			result.setRetrievalType(RetrievalType.HYBRID);
			result.getMetadata().put("fusion_method", "comb_mnz");
			result.getMetadata().put("occurrence_count", group.size());
			result.getMetadata().put("sum_score", sumScore);

			return result;
		}).sorted((a, b) -> b.getScore().compareTo(a.getScore())).collect(Collectors.toList());
	}

	/**
	 * CombSum融合
	 * <p/>
	 * 仅计算每个结果的分数总和，不考虑出现次数。适合简单场景，强调分数累加。
	 */
	private List<RetrievalResult> performCombSumFusion(List<RetrievalResult> results,
			PipelineConfig.FusionConfig config) {
		Map<String, List<RetrievalResult>> groupedResults = results.stream()
			.collect(Collectors.groupingBy(this::generateResultKey));

		return groupedResults.values().stream().map(group -> {
			double sumScore = group.stream().mapToDouble(r -> r.getScore().doubleValue()).sum();

			RetrievalResult result = cloneResult(group.get(0));
			result.setScore(BigDecimal.valueOf(sumScore));
			result.setRetrievalType(RetrievalType.HYBRID);
			result.getMetadata().put("fusion_method", "comb_sum");
			result.getMetadata().put("component_count", group.size());

			return result;
		}).sorted((a, b) -> b.getScore().compareTo(a.getScore())).collect(Collectors.toList());
	}

	/**
	 * 简单合并
	 * <p/>
	 * 去重保留最高分
	 */
	private List<RetrievalResult> performSimpleMerge(List<RetrievalResult> results) {
		Map<String, RetrievalResult> uniqueResults = new LinkedHashMap<>();

		for (RetrievalResult result : results) {
			String key = generateResultKey(result);
			if (!uniqueResults.containsKey(key) || uniqueResults.get(key).getScore().compareTo(result.getScore()) < 0) {

				RetrievalResult mergedResult = cloneResult(result);
				mergedResult.setRetrievalType(RetrievalType.HYBRID);
				mergedResult.getMetadata().put("fusion_method", "simple_merge");
				uniqueResults.put(key, mergedResult);
			}
		}

		return uniqueResults.values()
			.stream()
			.sorted((a, b) -> b.getScore().compareTo(a.getScore()))
			.collect(Collectors.toList());
	}

	/**
	 * 获取类型权重
	 */
	private double getTypeWeight(RetrievalType type, PipelineConfig.FusionConfig config) {
		return switch (type) {
			case VECTOR -> 0.6;
			case GRAPH -> 0.4;
			case HYBRID -> 0.8;
		};
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

	/**
	 * 加权结果辅助类
	 */
	private static class WeightedResult {

		private final RetrievalResult result;

		private double totalScore = 0.0;

		private final Set<RetrievalType> types = new HashSet<>();

		public WeightedResult(RetrievalResult result) {
			this.result = result;
		}

		public void addScore(double score) {
			this.totalScore += score;
		}

		public void addType(RetrievalType type) {
			this.types.add(type);
		}

		public RetrievalResult getResult() {
			return result;
		}

		public double getTotalScore() {
			return totalScore;
		}

		public Set<RetrievalType> getTypes() {
			return types;
		}

	}

}