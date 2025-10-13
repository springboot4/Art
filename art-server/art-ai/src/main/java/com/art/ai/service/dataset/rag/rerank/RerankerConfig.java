package com.art.ai.service.dataset.rag.rerank;

import dev.langchain4j.model.chat.ChatModel;
import dev.langchain4j.model.embedding.EmbeddingModel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 重排序配置
 *
 * @author fxz
 * @since 2025/10/05
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RerankerConfig {

	/**
	 * 重排序器类型
	 */
	private RerankerType type;

	/**
	 * 返回的最大结果数
	 */
	private Integer topK;

	/**
	 * 是否启用
	 */
	private Boolean enabled;

	/**
	 * 是否对QA类型结果进行重排序
	 */
	private Boolean enableForQa;

	/**
	 * LLM重排序配置
	 */
	private LlmRerankerConfig llmRerankerConfig;

	/**
	 * 多样性重排序配置
	 */
	private DiversityRerankerConfig diversityRerankerConfig;

	@Data
	@Builder
	@NoArgsConstructor
	@AllArgsConstructor
	public static class LlmRerankerConfig {

		/**
		 * LLM模型名称
		 */
		private ChatModel chatModel;

		/**
		 * 提示词模板
		 */
		private String promptTemplate;

		/**
		 * 温度参数
		 */
		private Double temperature;

		/**
		 * 最大token数
		 */
		private Integer maxTokens;

	}

	@Data
	@Builder
	@NoArgsConstructor
	@AllArgsConstructor
	public static class DiversityRerankerConfig {

		/**
		 * MMR的lambda参数，控制相关性和多样性的平衡
		 */
		private Double lambda;

		/**
		 * 多样性窗口大小
		 */
		private Integer windowSize;

		/**
		 * 向量嵌入模型
		 */
		private EmbeddingModel embeddingModel;

	}

}