package com.art.ai.service.dataset.rag.graph.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@EqualsAndHashCode(callSuper = true)
@SuperBuilder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class GraphVertexSearch extends GraphSearchCondition {

	private String label;

	@JsonProperty("text_segment_id")
	private String textSegmentId;

	@Builder.Default
	private Integer limit = 10;

	@Builder.Default
	private Long maxId = Long.MAX_VALUE;

}
