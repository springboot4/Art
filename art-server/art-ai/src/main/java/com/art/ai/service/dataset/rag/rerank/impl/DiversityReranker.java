package com.art.ai.service.dataset.rag.rerank.impl;

import com.art.ai.service.dataset.rag.rerank.Reranker;
import com.art.ai.service.dataset.rag.rerank.RerankerConfig;
import com.art.ai.service.dataset.rag.rerank.RerankerType;
import com.art.ai.service.dataset.rag.retrieval.entity.RetrievalResult;
import dev.langchain4j.data.embedding.Embedding;
import dev.langchain4j.model.embedding.EmbeddingModel;
import dev.langchain4j.store.embedding.CosineSimilarity;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 基于MMR的多样性重排序器<br/>
 * MMR (Maximal Marginal Relevance) 算法平衡相关性和多样性<br/>
 * MMR 分数 = `λ` * (相关性) - (1 - `λ`) * (最大相似度)<br/>
 *
 * @author fxz
 * @since 2025/10/05
 */
@Slf4j
@Component
public class DiversityReranker implements Reranker {

	/**
	 * Embedding缓存,避免重复计算 Key: 文本内容, Value: Embedding向量
	 */
	private Map<String, Embedding> embeddingCache = new ConcurrentHashMap<>();

	/**
	 * 是否使用Embedding相似度(自动检测)
	 */
	private Boolean useEmbeddingSimilarity;

	/**
	 * 用于计算文本Embedding的模型(可选)
	 */
	private EmbeddingModel embeddingModel;

	@Override
	public List<RetrievalResult> rerank(String query, List<RetrievalResult> results, RerankerConfig config) {
		if (results.isEmpty()) {
			return results;
		}

		try {
			RerankerConfig.DiversityRerankerConfig diversityConfig = config.getDiversityRerankerConfig();
			if (diversityConfig == null) {
				diversityConfig = getDefaultDiversityConfig();
			}
			embeddingModel = config.getDiversityRerankerConfig().getEmbeddingModel();
			embeddingCache = new HashMap<>();

			return performMMRReranking(query, results, diversityConfig, config);
		}
		catch (Exception e) {
			log.error("多样性重排序失败", e);
			return results;
		}
	}

	/**
	 * 执行MMR重排序
	 */
	private List<RetrievalResult> performMMRReranking(String query, List<RetrievalResult> results,
			RerankerConfig.DiversityRerankerConfig config, RerankerConfig globalConfig) {
		double lambda = config.getLambda() != null ? config.getLambda() : 0.7;
		int windowSize = config.getWindowSize() != null ? config.getWindowSize() : results.size();
		int topK = globalConfig.getTopK() != null ? globalConfig.getTopK() : results.size();

		List<RetrievalResult> selected = new ArrayList<>();
		List<RetrievalResult> candidates = new ArrayList<>(results);

		// 按原始相关性分数排序
		candidates.sort((a, b) -> b.getScore().compareTo(a.getScore()));

		// 选择第一个（最相关的）文档
		if (!candidates.isEmpty()) {
			RetrievalResult first = candidates.remove(0);
			first.getMetadata().put("mmr_position", 1);
			first.getMetadata().put("mmr_score", first.getScore().doubleValue());
			selected.add(first);
		}

		// MMR迭代选择
		while (selected.size() < topK && !candidates.isEmpty()) {
			RetrievalResult bestCandidate = null;
			double bestScore = Double.NEGATIVE_INFINITY;
			int bestIndex = -1;

			for (int i = 0; i < Math.min(candidates.size(), windowSize); i++) {
				RetrievalResult candidate = candidates.get(i);

				// 计算与查询的相关性分数
				double relevanceScore = candidate.getScore().doubleValue();

				// 计算与已选文档的最大相似度
				double maxSimilarity = calculateMaxSimilarity(candidate, selected);

				// MMR分数：λ * 相关性 - (1-λ) * 最大相似度
				double mmrScore = lambda * relevanceScore - (1 - lambda) * maxSimilarity;

				if (mmrScore > bestScore) {
					bestScore = mmrScore;
					bestCandidate = candidate;
					bestIndex = i;
				}
			}

			if (bestCandidate != null) {
				candidates.remove(bestIndex);
				bestCandidate.setScore(BigDecimal.valueOf(bestScore));
				bestCandidate.getMetadata().put("mmr_position", selected.size() + 1);
				bestCandidate.getMetadata().put("mmr_score", bestScore);
				bestCandidate.getMetadata().put("original_relevance", bestCandidate.getScore().doubleValue());
				selected.add(bestCandidate);
			}
		}

		return selected;
	}

	/**
	 * 计算候选文档与已选文档集合的最大相似度
	 */
	private double calculateMaxSimilarity(RetrievalResult candidate, List<RetrievalResult> selected) {
		if (selected.isEmpty()) {
			return 0.0;
		}

		double maxSim = 0.0;
		String candidateContent = candidate.getContent();

		for (RetrievalResult selectedDoc : selected) {
			double similarity = calculateContentSimilarity(candidateContent, selectedDoc.getContent());
			maxSim = Math.max(maxSim, similarity);
		}

		return maxSim;
	}

	/**
	 * 计算内容相似度 (智能选择策略)
	 * <p>
	 * 优先使用Embedding余弦相似度(如果EmbeddingModel可用),否则降级为Jaccard相似度
	 * </p>
	 */
	private double calculateContentSimilarity(String content1, String content2) {
		if (content1 == null || content2 == null) {
			return 0.0;
		}

		// 初始化相似度计算策略(懒加载)
		if (useEmbeddingSimilarity == null) {
			useEmbeddingSimilarity = (embeddingModel != null);
			if (useEmbeddingSimilarity) {
				log.info("MMR多样性重排序: 使用Embedding余弦相似度(深度语义理解)");
			}
			else {
				log.info("MMR多样性重排序: 使用Jaccard相似度(快速词汇匹配)");
			}
		}

		// 根据策略计算相似度
		if (useEmbeddingSimilarity) {
			return calculateEmbeddingSimilarity(content1, content2);
		}
		else {
			return calculateJaccardSimilarity(content1, content2);
		}
	}

	/**
	 * 计算Embedding余弦相似度 (推荐方法)
	 * <p>
	 * 使用CosineSimilarity计算两个文本的语义相似度<br/>
	 * 优点: 深度语义理解,支持同义词、近义词、跨语言<br/>
	 */
	private double calculateEmbeddingSimilarity(String content1, String content2) {
		try {
			Embedding embedding1 = getOrComputeEmbedding(content1);
			Embedding embedding2 = getOrComputeEmbedding(content2);

			double similarity = CosineSimilarity.between(embedding1, embedding2);

			// Cosine相似度范围[-1, 1],归一化到[0, 1]
			return (similarity + 1.0) / 2.0;
		}
		catch (Exception e) {
			log.warn("Embedding相似度计算失败,降级为Jaccard相似度: {}", e.getMessage());
			return calculateJaccardSimilarity(content1, content2);
		}
	}

	/**
	 * 获取或计算Embedding (带缓存)
	 */
	private Embedding getOrComputeEmbedding(String text) {
		return embeddingCache.computeIfAbsent(text, t -> {
			try {
				return embeddingModel.embed(t).content();
			}
			catch (Exception e) {
				throw new RuntimeException("Embedding计算失败: " + e.getMessage(), e);
			}
		});
	}

	/**
	 * 计算Jaccard相似度 (备选方法)
	 * <p>
	 * 基于词袋模型的Jaccard相似度,计算词汇重叠度<br/>
	 * 优点: 计算速度快,无外部依赖<br/>
	 * 缺点: 无法理解语义,"汽车"和"car"被视为不同词
	 * </p>
	 */
	private double calculateJaccardSimilarity(String content1, String content2) {
		Set<String> words1 = tokenize(content1);
		Set<String> words2 = tokenize(content2);

		if (words1.isEmpty() && words2.isEmpty()) {
			return 1.0;
		}

		Set<String> intersection = new HashSet<>(words1);
		intersection.retainAll(words2);

		Set<String> union = new HashSet<>(words1);
		union.addAll(words2);

		return union.isEmpty() ? 0.0 : (double) intersection.size() / union.size();
	}

	/**
	 * 简单分词
	 */
	private Set<String> tokenize(String text) {
		if (text == null || text.trim().isEmpty()) {
			return Collections.emptySet();
		}

		// 简单的中英文分词
		Set<String> tokens = new HashSet<>();

		// 英文分词
		String[] englishWords = text.toLowerCase().replaceAll("[^a-zA-Z\\u4e00-\\u9fa5\\s]", " ").split("\\s+");

		for (String word : englishWords) {
			if (word.length() > 1) {
				tokens.add(word.trim());
			}
		}

		// 中文字符级别的分词
		for (char c : text.toCharArray()) {
			// 中文字符范围
			if (c >= 0x4e00 && c <= 0x9fa5) {
				tokens.add(String.valueOf(c));
			}
		}

		return tokens;
	}

	/**
	 * 获取默认多样性配置
	 */
	private RerankerConfig.DiversityRerankerConfig getDefaultDiversityConfig() {
		return RerankerConfig.DiversityRerankerConfig.builder()
			// 70%相关性，30%多样性
			.lambda(0.7)
			// 在前50个候选中选择
			.windowSize(50)
			.build();
	}

	@Override
	public RerankerType getType() {
		return RerankerType.DIVERSITY;
	}

	@Override
	public boolean supports(RerankerConfig config) {
		return config.getType() == getType();
	}

}