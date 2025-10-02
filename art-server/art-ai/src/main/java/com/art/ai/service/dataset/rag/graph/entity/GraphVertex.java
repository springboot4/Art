package com.art.ai.service.dataset.rag.graph.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class GraphVertex {

	private String id;

	private String label;

	private String name;

	@JsonProperty("text_segment_id")
	private String textSegmentId;

	private String description;

	private Map<String, Object> metadata;

}
