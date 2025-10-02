package com.art.ai.service.dataset.rag.graph;

import com.art.ai.service.dataset.rag.constant.KnowledgeConstants;
import com.art.ai.service.dataset.rag.graph.ingest.GraphStoreIngestor;
import dev.langchain4j.data.document.Document;
import dev.langchain4j.data.document.DocumentSplitter;
import dev.langchain4j.data.document.splitter.DocumentSplitters;
import dev.langchain4j.model.chat.ChatModel;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Collections;

import static com.art.ai.service.dataset.rag.constant.KnowledgeConstants.MAX_OVERLAP_SIZE_IN_TOKENS;

/**
 * @author fxz
 * @since 2025/9/14 15:36
 */
@RequiredArgsConstructor
@Service
public class GraphService {

	private final GraphStoreIngestor ingestor;

	public void ingest(Document document, ChatModel chatModel) {
		DocumentSplitter documentSplitter = DocumentSplitters.recursive(KnowledgeConstants.MAX_SEGMENT_SIZE_IN_TOKENS,
				MAX_OVERLAP_SIZE_IN_TOKENS);

		ingestor.ingest(Collections.singletonList(document), documentSplitter, null, null, chatModel);
	}

}
