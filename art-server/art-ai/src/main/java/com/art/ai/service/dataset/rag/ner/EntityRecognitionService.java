package com.art.ai.service.dataset.rag.ner;

import com.art.ai.service.dataset.rag.graph.entity.GraphEntityDesc;
import com.art.ai.service.dataset.rag.graph.entity.GraphExtractionInfo;
import com.art.ai.service.dataset.rag.graph.ingest.GraphExtractionParser;
import com.art.ai.service.dataset.rag.graph.ingest.GraphExtractionService;
import dev.langchain4j.model.chat.ChatModel;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 实体识别服务
 *
 * @author fxz
 * @since 2025/10/05
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class EntityRecognitionService {

	private final GraphExtractionService graphExtractionService;

	private final GraphExtractionParser graphExtractionParser;

	/**
	 * 从查询文本中提取实体
	 */
	public List<RecognizedEntity> extractEntities(String query, ChatModel chatModel) {
		try {
			String extractionResult = graphExtractionService.getChatModelResponseIfExists(chatModel, query);
			List<GraphExtractionInfo> extractionInfos = graphExtractionParser.parse(extractionResult);

			List<RecognizedEntity> entities = parseExtractedEntities(extractionInfos);

			return deduplicateAndSort(entities);
		}
		catch (Exception e) {
			log.error("实体识别失败: query={}", query, e);
			return new ArrayList<>();
		}
	}

	/**
	 * 解析LLM提取的实体
	 */
	private List<RecognizedEntity> parseExtractedEntities(List<GraphExtractionInfo> extractionInfos) {
		List<RecognizedEntity> entities = new ArrayList<>();

		for (GraphExtractionInfo extractionInfo : extractionInfos) {
			GraphEntityDesc entityDesc = extractionInfo.getEntityDesc();
			if (entityDesc == null) {
				continue;
			}

			entities.add(RecognizedEntity.builder()
				.name(entityDesc.getEntity().toUpperCase())
				.type(EntityType.fromString(entityDesc.getType()))
				.confidence(1)
				.source(EntitySource.LLM)
				.build());
		}

		return entities;
	}

	/**
	 * 去重和排序
	 */
	private List<RecognizedEntity> deduplicateAndSort(List<RecognizedEntity> entities) {
		// 按名称去重，保留置信度最高的
		Map<String, RecognizedEntity> entityMap = new HashMap<>();
		for (RecognizedEntity entity : entities) {
			String key = entity.getName().toUpperCase();
			if (!entityMap.containsKey(key) || entityMap.get(key).getConfidence() < entity.getConfidence()) {
				entityMap.put(key, entity);
			}
		}

		List<RecognizedEntity> result = new ArrayList<>(entityMap.values());
		result.sort((a, b) -> Double.compare(b.getConfidence(), a.getConfidence()));

		return result;
	}

	/**
	 * 获取高置信度的实体（用于图谱查询）
	 */
	public List<RecognizedEntity> getHighConfidenceEntities(String query, ChatModel chatModel, double threshold) {
		List<RecognizedEntity> allEntities = extractEntities(query, chatModel);
		return allEntities.stream().filter(entity -> entity.getConfidence() >= threshold).toList();
	}

}