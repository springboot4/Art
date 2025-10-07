package com.art.ai.service.dataset.rag.pipeline;

import com.art.ai.service.dataset.rag.rerank.RerankerConfig;
import com.art.ai.service.dataset.rag.retrieval.entity.RetrievalType;
import dev.langchain4j.model.embedding.EmbeddingModel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * 召回管道配置
 *
 * @author fxz
 * @since 2025/10/05
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PipelineConfig {

	/**
	 * 召回器配置
	 */
	private List<RetrieverConfig> retrieverConfigs;

	/**
	 * 融合策略配置
	 */
	private FusionConfig fusionConfig;

	/**
	 * 重排序器配置列表
	 */
	private List<RerankerConfig> rerankerConfigs;

	/**
	 * 后处理配置
	 */
	private PostProcessConfig postProcessConfig;

	/**
	 * 是否启用并行处理
	 */
	private Boolean enableParallel;

	@Data
	@Builder
	@NoArgsConstructor
	@AllArgsConstructor
	public static class RetrieverConfig {

		/**
		 * 召回器类型
		 */
		private RetrievalType type;

		/**
		 * 是否启用
		 */
		private Boolean enabled;

		/**
		 * 向量召回配置
		 */
		private VectorRetrievalConfig vectorConfig;

		/**
		 * 图谱召回配置
		 */
		private GraphRetrievalConfig graphConfig;

		/**
		 * 混合召回配置
		 */
		private HybridRetrievalConfig hybridConfig;

	}

	@Data
	@Builder
	@NoArgsConstructor
	@AllArgsConstructor
	public static class VectorRetrievalConfig {

		private Integer topK;

		private Double minScore;

		private EmbeddingModel embeddingModel;

	}

	@Data
	@Builder
	@NoArgsConstructor
	@AllArgsConstructor
	public static class GraphRetrievalConfig {

		private Integer maxDepth;

		private Integer maxNodes;

		private Boolean includeRelations;

		/**
		 * 实体识别置信度阈值
		 */
		private Double entityConfidenceThreshold;

	}

	@Data
	@Builder
	@NoArgsConstructor
	@AllArgsConstructor
	public static class HybridRetrievalConfig {

		private VectorRetrievalConfig vectorConfig;

		private GraphRetrievalConfig graphConfig;

	}

	@Data
	@Builder
	@NoArgsConstructor
	@AllArgsConstructor
	public static class FusionConfig {

		/**
		 * 融合策略：rrf, weighted_sum, comb_mnz, comb_sum
		 */
		private String strategy;

		/**
		 * RRF参数k
		 */
		private Integer rrfK;

		/**
		 * 融合后的最大结果数
		 */
		private Integer maxResults;

	}

	@Data
	@Builder
	@NoArgsConstructor
	@AllArgsConstructor
	public static class PostProcessConfig {

		/**
		 * 去重策略：content, segment_id
		 */
		private String deduplicationStrategy;

		/**
		 * 最终返回的结果数量
		 */
		private Integer finalTopK;

		/**
		 * 最小分数过滤
		 */
		private Double minScoreFilter;

	}

}