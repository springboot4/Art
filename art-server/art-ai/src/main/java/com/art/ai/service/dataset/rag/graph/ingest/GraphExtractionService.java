package com.art.ai.service.dataset.rag.graph.ingest;

import com.art.ai.core.constants.DocumentConstants;
import com.art.ai.core.dto.dataset.AiDocumentSegmentDTO;
import com.art.ai.service.dataset.rag.constant.KnowledgePrompts;
import com.art.ai.service.dataset.rag.graph.entity.GraphExtraction;
import com.art.ai.service.dataset.service.AiDocumentSegmentService;
import dev.langchain4j.data.document.Metadata;
import dev.langchain4j.data.message.UserMessage;
import dev.langchain4j.data.segment.TextSegment;
import dev.langchain4j.model.chat.ChatModel;
import dev.langchain4j.model.chat.response.ChatResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

import static com.art.ai.service.dataset.rag.constant.KnowledgeConstants.DATASET_KEY;
import static com.art.ai.service.dataset.rag.constant.KnowledgeConstants.DOCUMENT_KEY;

/**
 * @author fxz
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class GraphExtractionService {

	private final AiDocumentSegmentService aiDocumentSegmentService;

	public List<GraphExtraction> extract(ChatModel chatModel, List<TextSegment> segments) {
		List<GraphExtraction> extract = segments.stream().map(segment -> {
			AiDocumentSegmentDTO segmentDTO = createAndSaveGraphSegment(segment);

			String response = getChatModelResponseIfExists(chatModel, segment);

			return new GraphExtraction(segment, segmentDTO.getId(), response);
		}).collect(Collectors.toList());

		log.info("Extracted content for {} text segments", extract.size());
		return extract;
	}

	private String getChatModelResponseIfExists(ChatModel chatModel, TextSegment segment) {
		if (StringUtils.isBlank(segment.text())) {
			return "";
		}

		ChatResponse aiMessageResponse = chatModel.chat(
				UserMessage.from(KnowledgePrompts.GRAPH_EXTRACTION_PROMPT_CN.replace("{input_text}", segment.text())));
		return aiMessageResponse.aiMessage().text();
	}

	private AiDocumentSegmentDTO createAndSaveGraphSegment(TextSegment segment) {
		Metadata metadata = segment.metadata();
		Long documentId = metadata.getLong(DOCUMENT_KEY);
		Long datasetId = metadata.getLong(DATASET_KEY);

		AiDocumentSegmentDTO segmentDTO = new AiDocumentSegmentDTO();
		segmentDTO.setDatasetId(datasetId);
		segmentDTO.setDocumentId(documentId);
		segmentDTO.setSegment(segment.text());
		segmentDTO.setSegmentType(DocumentConstants.GRAPH_SEGMENT);
		return aiDocumentSegmentService.addAiDocumentSegment(segmentDTO);
	}

}
