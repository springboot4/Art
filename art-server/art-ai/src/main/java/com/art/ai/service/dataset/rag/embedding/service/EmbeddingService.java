package com.art.ai.service.dataset.rag.embedding.service;

import com.art.ai.service.dataset.rag.embedding.ingest.DefaultEmbeddingStoreIngestor;
import com.art.ai.service.dataset.rag.embedding.entity.EmbeddingIngestParam;
import com.art.ai.service.dataset.service.AiDocumentSegmentService;
import dev.langchain4j.store.embedding.EmbeddingStoreIngestor;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

/**
 * @author fxz
 * @since 2025/9/14 13:56
 */
@RequiredArgsConstructor
@Component
public class EmbeddingService {

	private final AiDocumentSegmentService aiDocumentSegmentService;

	public void ingest(EmbeddingIngestParam embeddingIngestParam) {
		EmbeddingStoreIngestor storeIngestor = new DefaultEmbeddingStoreIngestor(null,
				embeddingIngestParam.getDocumentSplitter(), null, embeddingIngestParam.getEmbeddingModel(),
				embeddingIngestParam.getEmbeddingStore(), aiDocumentSegmentService);

		storeIngestor.ingest(embeddingIngestParam.getDocument());
	}

}
