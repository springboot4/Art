package com.art.ai.service.dataset.rag.embedding.entity;

import dev.langchain4j.data.document.Document;
import dev.langchain4j.data.document.DocumentSplitter;
import dev.langchain4j.data.segment.TextSegment;
import dev.langchain4j.model.embedding.EmbeddingModel;
import dev.langchain4j.store.embedding.EmbeddingStore;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

/**
 * @author fxz
 * @since 2025/9/15 13:12
 */
@AllArgsConstructor
@Getter
@Builder
public class EmbeddingIngestParam {

	private DocumentSplitter documentSplitter;

	private EmbeddingModel embeddingModel;

	private EmbeddingStore<TextSegment> embeddingStore;

	private Document document;

}
