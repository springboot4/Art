package com.art.ai.service.qa;

import com.art.ai.dao.dataobject.AiQaPairsDO;
import com.art.ai.dao.dataobject.AiQaSimilarQuestionsDO;
import com.art.ai.manager.AiQaPairsManager;
import dev.langchain4j.data.document.Metadata;
import dev.langchain4j.data.embedding.Embedding;
import dev.langchain4j.data.segment.TextSegment;
import dev.langchain4j.model.embedding.EmbeddingModel;
import dev.langchain4j.store.embedding.EmbeddingStore;
import dev.langchain4j.store.embedding.filter.comparison.IsEqualTo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * QA向量化服务
 *
 * @author fxz
 * @since 2025/10/12
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class QaVectorizationService {

	private final AiQaPairsManager qaPairsManager;

	public static final String QA_TYPE_KEY = "qa_type";

	public static final String QA_ID_KEY = "qa_id";

	public static final String DATASET_ID_KEY = "dataset_id";

	/**
	 * 向量化单个QA对（包括标准问和相似问题）
	 */
	public void vectorizeQaPair(Long qaPairId, EmbeddingModel embeddingModel,
			EmbeddingStore<TextSegment> embeddingStore) {
		try {
			AiQaPairsDO qaPair = qaPairsManager.findById(qaPairId);
			if (qaPair == null || !qaPair.getEnabled()) {
				log.warn("QA对不存在或已禁用: qaPairId={}", qaPairId);
				return;
			}

			removeQaVector(qaPairId, embeddingStore);

			List<TextSegment> segments = new ArrayList<>();
			Metadata metadata = buildMetadata(qaPair);

			segments.add(TextSegment.from(qaPair.getQuestion(), metadata));

			List<AiQaSimilarQuestionsDO> similarQuestions = qaPairsManager.listSimilarByQaPairId(qaPairId);
			for (AiQaSimilarQuestionsDO similar : similarQuestions) {
				segments.add(TextSegment.from(similar.getSimilarQuestion(), metadata));
			}

			List<Embedding> embeddings = embeddingModel.embedAll(segments).content();

			List<String> vectorIds = embeddingStore.addAll(embeddings, segments);

			qaPairsManager.updateVectorIndexed(qaPairId, vectorIds.get(0));

			log.info("QA对向量化成功: qaPairId={}, 标准问+相似问共{}个向量", qaPairId, vectorIds.size());
		}
		catch (Exception e) {
			log.error("QA对向量化失败: qaPairId={}", qaPairId, e);
			throw new RuntimeException("QA对向量化失败", e);
		}
	}

	/**
	 * 删除QA对的所有向量（包括标准问和相似问题的向量）
	 */
	public void removeQaVector(Long qaPairId, EmbeddingStore<TextSegment> embeddingStore) {
		try {
			embeddingStore.removeAll(new IsEqualTo(QA_ID_KEY, qaPairId.toString()));
		}
		catch (Exception e) {
			log.error("删除QA对向量失败: qaPairId={}", qaPairId, e);
		}
	}

	/**
	 * 构建元数据
	 */
	private Metadata buildMetadata(AiQaPairsDO qaPair) {
		Map<String, Object> metadataMap = new HashMap<>();
		metadataMap.put(QA_TYPE_KEY, "qa");
		metadataMap.put(QA_ID_KEY, qaPair.getId().toString());
		metadataMap.put(DATASET_ID_KEY, qaPair.getDatasetId().toString());
		metadataMap.put("priority", qaPair.getPriority());
		return Metadata.from(metadataMap);
	}

}
