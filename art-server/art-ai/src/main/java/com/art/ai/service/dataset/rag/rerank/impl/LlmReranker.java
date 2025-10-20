package com.art.ai.service.dataset.rag.rerank.impl;

import com.art.ai.service.dataset.rag.rerank.Reranker;
import com.art.ai.service.dataset.rag.rerank.RerankerConfig;
import com.art.ai.service.dataset.rag.rerank.RerankerType;
import com.art.ai.service.dataset.rag.retrieval.entity.RetrievalResult;
import com.art.core.common.util.AsyncUtil;
import dev.langchain4j.data.message.UserMessage;
import dev.langchain4j.model.chat.ChatModel;
import dev.langchain4j.model.chat.response.ChatResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * LLM重排序器 - 使用大语言模型对相关性进行评分
 *
 * @author fxz
 * @since 2025/10/05
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class LlmReranker implements Reranker {

	private ChatModel chatModel;

	@Override
	public List<RetrievalResult> rerank(String query, List<RetrievalResult> results, RerankerConfig config) {
		if (results.isEmpty()) {
			return results;
		}

		try {
			RerankerConfig.LlmRerankerConfig llmConfig = config.getLlmRerankerConfig();
			if (llmConfig == null) {
				llmConfig = getDefaultLlmConfig();
			}
			chatModel = llmConfig.getChatModel();

			int batchSize = 5;
			List<List<RetrievalResult>> batches = splitIntoBatches(results, batchSize);

			if (batches.isEmpty()) {
				return results;
			}

			// 单批次直接处理,多批次并发处理
			List<RetrievalResult> rerankedResults;
			if (batches.size() == 1) {
				rerankedResults = rerankBatch(query, batches.get(0), llmConfig);
			}
			else {
				rerankedResults = rerankBatchesConcurrently(query, batches, llmConfig);
			}

			// 按新的相关性分数排序
			rerankedResults.sort((a, b) -> b.getScore().compareTo(a.getScore()));

			// 应用topK限制
			int topK = config.getTopK() != null ? config.getTopK() : results.size();
			return rerankedResults.subList(0, Math.min(topK, rerankedResults.size()));
		}
		catch (Exception e) {
			log.error("LLM重排序失败", e);
			return results;
		}
	}

	/**
	 * 并发处理多个批次
	 */
	private List<RetrievalResult> rerankBatchesConcurrently(String query, List<List<RetrievalResult>> batches,
			RerankerConfig.LlmRerankerConfig llmConfig) {

		long startTime = System.currentTimeMillis();

		// 构建并发任务
		List<Supplier<List<RetrievalResult>>> suppliers = batches.stream()
			.map(batch -> (Supplier<List<RetrievalResult>>) () -> {
				try {
					return rerankBatch(query, batch, llmConfig);
				}
				catch (Exception e) {
					log.warn("批次重排序失败,保留原排序: {}", e.getMessage());
					return batch;
				}
			})
			.toList();

		// 并发执行并收集结果
		List<RetrievalResult> allResults = executeParallelAndCollect(suppliers);

		long duration = System.currentTimeMillis() - startTime;
		log.info("LLM批次并发重排序完成: batches={}, totalDocs={}, duration={}ms", batches.size(), allResults.size(), duration);

		return allResults;
	}

	/**
	 * 将结果列表分割成批次
	 */
	private List<List<RetrievalResult>> splitIntoBatches(List<RetrievalResult> results, int batchSize) {
		List<List<RetrievalResult>> batches = new ArrayList<>();
		for (int i = 0; i < results.size(); i += batchSize) {
			int endIndex = Math.min(i + batchSize, results.size());
			batches.add(new ArrayList<>(results.subList(i, endIndex)));
		}
		return batches;
	}

	/**
	 * 批量重排序
	 */
	private List<RetrievalResult> rerankBatch(String query, List<RetrievalResult> batch,
			RerankerConfig.LlmRerankerConfig config) {
		String prompt = buildRerankerPrompt(query, batch, config);

		try {
			ChatResponse response = chatModel.chat(UserMessage.from(prompt));
			String result = response.aiMessage().text();

			return parseRerankerResponse(result, batch);

		}
		catch (Exception e) {
			log.error("LLM批量重排序失败", e);
			return batch;
		}
	}

	/**
	 * 构建重排序提示词
	 */
	private String buildRerankerPrompt(String query, List<RetrievalResult> batch,
			RerankerConfig.LlmRerankerConfig config) {
		String template = config.getPromptTemplate();
		if (template == null) {
			template = getDefaultPromptTemplate();
		}

		StringBuilder documentsText = new StringBuilder();
		for (int i = 0; i < batch.size(); i++) {
			RetrievalResult result = batch.get(i);
			documentsText.append(String.format("文档 %d:\n%s\n\n", i + 1, result.getContent()));
		}

		return template.replace("{query}", query).replace("{documents}", documentsText.toString());
	}

	/**
	 * 解析重排序响应
	 */
	private List<RetrievalResult> parseRerankerResponse(String response, List<RetrievalResult> originalBatch) {
		List<RetrievalResult> rerankedBatch = new ArrayList<>();

		// 解析格式：文档1: 相关性分数
		Pattern pattern = Pattern.compile("文档\\s*(\\d+)\\s*[：:]\\s*([0-9.]+)");
		Matcher matcher = pattern.matcher(response);

		while (matcher.find()) {
			try {
				int docIndex = Integer.parseInt(matcher.group(1)) - 1;
				double score = Double.parseDouble(matcher.group(2));

				if (docIndex >= 0 && docIndex < originalBatch.size()) {
					RetrievalResult result = originalBatch.get(docIndex);

					// 更新分数并添加重排序信息
					result.getMetadata().put("llm_rerank_score", score);
					result.getMetadata().put("original_score", result.getScore());
					result.setScore(BigDecimal.valueOf(score));

					rerankedBatch.add(result);
				}
			}
			catch (NumberFormatException e) {
				log.warn("解析LLM重排序分数失败: {}", matcher.group(), e);
			}
		}

		// 如果解析失败，返回原始结果
		if (rerankedBatch.isEmpty()) {
			log.warn("LLM重排序响应解析失败，使用原始排序");
			return originalBatch;
		}

		return rerankedBatch;
	}

	/**
	 * 获取默认提示词模板
	 */
	private String getDefaultPromptTemplate() {
		return """
				你是一个专业的文档相关性评估专家。请根据用户查询对以下文档的相关性进行评分。

				用户查询：{query}

				文档列表：
				{documents}

				请为每个文档给出0-1之间的相关性分数，其中：
				- 1.0表示完全相关
				- 0.8-0.9表示高度相关
				- 0.6-0.7表示中等相关
				- 0.4-0.5表示低度相关
				- 0.0-0.3表示不相关

				请按照以下格式返回评分：
				文档1: 0.85
				文档2: 0.72
				文档3: 0.91
				...

				注意：
				1. 重点考虑文档内容与查询的语义相关性
				2. 考虑文档的完整性和信息质量
				3. 优先考虑直接回答查询问题的文档
				""";
	}

	/**
	 * 获取默认LLM配置
	 */
	private RerankerConfig.LlmRerankerConfig getDefaultLlmConfig() {
		return RerankerConfig.LlmRerankerConfig.builder().temperature(0.1).maxTokens(1024).build();
	}

	@Override
	public RerankerType getType() {
		return RerankerType.LLM_BASED;
	}

	@Override
	public boolean supports(RerankerConfig config) {
		return config.getType() == RerankerType.LLM_BASED && config.getLlmRerankerConfig() != null;
	}

	/**
	 * 并行执行并收集结果
	 */
	@SuppressWarnings("unchecked")
	private List<RetrievalResult> executeParallelAndCollect(List<Supplier<List<RetrievalResult>>> suppliers) {
		if (suppliers.isEmpty()) {
			return List.of();
		}

		Object[] results = AsyncUtil.parallel(suppliers.toArray(new Supplier[0]));

		return Stream.of(results)
			.filter(result -> result instanceof List<?>)
			.flatMap(result -> ((List<RetrievalResult>) result).stream())
			.collect(Collectors.toList());
	}

}