package com.art.ai.service.dataset.rag.graph.entity;

import dev.langchain4j.data.segment.TextSegment;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class GraphExtraction {

	private TextSegment textSegment;

	private Long segmentId;

	private String response;

}