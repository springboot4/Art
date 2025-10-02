package com.art.ai.service.dataset.rag.graph.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
public class GraphEdge {

	private String id;

	private String label;

	private Double weight;

	private String description;

	@JsonProperty("text_segment_id")
	private String textSegmentId;

	private Map<String, Object> metadata;

	private String startId;

	private String sourceName;

	private Map<String, Object> sourceMetadata;

	private String endId;

	private String targetName;

	private Map<String, Object> targetMetadata;

}