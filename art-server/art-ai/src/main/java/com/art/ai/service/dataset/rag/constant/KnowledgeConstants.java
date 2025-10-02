package com.art.ai.service.dataset.rag.constant;

import java.util.List;

/**
 * @author fxz
 * @since 2025/9/14 14:22
 */
public interface KnowledgeConstants {

	int MAX_SEGMENT_SIZE_IN_TOKENS = 1000;

	int MAX_OVERLAP_SIZE_IN_TOKENS = 50;

	String DATASET_KEY = "DATASET_KEY";

	String DOCUMENT_KEY = "DOCUMENT_KEY";

	String SEGMENT_KEY = "text_segment_id";

	String[] GRAPH_ENTITY_EXTRACTION_ENTITY_TYPES = { "organization", "person", "geo", "event" };

	String GRAPH_TUPLE_DELIMITER = "<|>";

	String GRAPH_RECORD_DELIMITER = "##";

	String GRAPH_COMPLETION_DELIMITER = "<|COMPLETE|>";

	List<String> GRAPH_STORE_MAIN_FIELDS = List.of("name", "label", SEGMENT_KEY, "description");

}
