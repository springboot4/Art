package com.art.ai.service.dataset.rag.embedding.ingest;

import com.art.ai.core.constants.DocumentConstants;
import com.art.ai.core.dto.dataset.AiDocumentSegmentDTO;
import com.art.ai.service.dataset.service.AiDocumentSegmentService;
import dev.langchain4j.data.document.Document;
import dev.langchain4j.data.document.DocumentSplitter;
import dev.langchain4j.data.document.DocumentTransformer;
import dev.langchain4j.data.document.Metadata;
import dev.langchain4j.data.embedding.Embedding;
import dev.langchain4j.data.segment.TextSegment;
import dev.langchain4j.data.segment.TextSegmentTransformer;
import dev.langchain4j.model.embedding.EmbeddingModel;
import dev.langchain4j.model.output.Response;
import dev.langchain4j.store.embedding.EmbeddingStore;
import dev.langchain4j.store.embedding.EmbeddingStoreIngestor;
import dev.langchain4j.store.embedding.IngestionResult;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

import static com.art.ai.service.dataset.rag.constant.KnowledgeConstants.DATASET_KEY;
import static com.art.ai.service.dataset.rag.constant.KnowledgeConstants.DOCUMENT_KEY;
import static com.art.ai.service.dataset.rag.constant.KnowledgeConstants.SEGMENT_KEY;
import static java.util.stream.Collectors.toList;

/**
 * @author fxz
 * @since 2025/10/2 13:35
 */
@Slf4j
public class DefaultEmbeddingStoreIngestor extends EmbeddingStoreIngestor {

	private final DocumentTransformer documentTransformer;

	private final DocumentSplitter documentSplitter;

	private final TextSegmentTransformer textSegmentTransformer;

	private final EmbeddingModel embeddingModel;

	private final EmbeddingStore<TextSegment> embeddingStore;

	private final AiDocumentSegmentService aiDocumentSegmentService;

	/**
	 * Creates an instance of an {@code EmbeddingStoreIngestor}.
	 * @param documentTransformer The {@link DocumentTransformer} to use. Optional.
	 * @param documentSplitter The {@link DocumentSplitter} to use. Optional. If none is
	 * specified, it tries to load one through SPI (see {@link DocumentSplitterFactory}).
	 * @param textSegmentTransformer The {@link TextSegmentTransformer} to use. Optional.
	 * @param embeddingModel The {@link EmbeddingModel} to use. Mandatory. If none is
	 * specified, it tries to load one through SPI (see {@link EmbeddingModelFactory}).
	 * @param embeddingStore The {@link EmbeddingStore} to use. Mandatory.
	 */
	public DefaultEmbeddingStoreIngestor(DocumentTransformer documentTransformer, DocumentSplitter documentSplitter,
			TextSegmentTransformer textSegmentTransformer, EmbeddingModel embeddingModel,
			EmbeddingStore<TextSegment> embeddingStore, AiDocumentSegmentService aiDocumentSegmentService) {
		super(documentTransformer, documentSplitter, textSegmentTransformer, embeddingModel, embeddingStore);
		this.documentTransformer = documentTransformer;
		this.documentSplitter = documentSplitter;
		this.textSegmentTransformer = textSegmentTransformer;
		this.embeddingModel = embeddingModel;
		this.embeddingStore = embeddingStore;
		this.aiDocumentSegmentService = aiDocumentSegmentService;
	}

	public IngestionResult ingest(List<Document> documents) {
		log.debug("Starting to ingest {} documents", documents.size());

		if (documentTransformer != null) {
			documents = documentTransformer.transformAll(documents);
			log.debug("Documents were transformed into {} documents", documents.size());
		}
		List<TextSegment> segments;
		if (documentSplitter != null) {
			segments = documentSplitter.splitAll(documents);
			log.debug("Documents were split into {} text segments", segments.size());
		}
		else {
			segments = documents.stream().map(Document::toTextSegment).collect(toList());
		}
		for (TextSegment segment : segments) {
			saveEmbeddingSegment(segment);
		}

		if (textSegmentTransformer != null) {
			segments = textSegmentTransformer.transformAll(segments);
			log.debug("{} documents were transformed into {} text segments", documents.size(), segments.size());
		}

		log.debug("Starting to embed {} text segments", segments.size());
		Response<List<Embedding>> embeddingsResponse = embeddingModel.embedAll(segments);
		log.debug("Finished embedding {} text segments", segments.size());

		log.debug("Starting to store {} text segments into the embedding store", segments.size());
		embeddingStore.addAll(embeddingsResponse.content(), segments);
		log.debug("Finished storing {} text segments into the embedding store", segments.size());

		return new IngestionResult(embeddingsResponse.tokenUsage());
	}

	private void saveEmbeddingSegment(TextSegment segment) {
		Metadata metadata = segment.metadata();
		Long documentId = metadata.getLong(DOCUMENT_KEY);
		Long datasetId = metadata.getLong(DATASET_KEY);
		AiDocumentSegmentDTO aiDocumentSegmentDTO = new AiDocumentSegmentDTO();
		aiDocumentSegmentDTO.setSegmentType(DocumentConstants.VECTOR_STORE_SEGMENT);
		aiDocumentSegmentDTO.setSegment(segment.text());
		aiDocumentSegmentDTO.setDocumentId(documentId);
		aiDocumentSegmentDTO.setDatasetId(datasetId);
		aiDocumentSegmentDTO = aiDocumentSegmentService.addAiDocumentSegment(aiDocumentSegmentDTO);
		metadata.put(SEGMENT_KEY, aiDocumentSegmentDTO.getId());
	}

}
