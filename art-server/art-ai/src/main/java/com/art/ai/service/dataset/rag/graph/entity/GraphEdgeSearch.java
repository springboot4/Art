package com.art.ai.service.dataset.rag.graph.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class GraphEdgeSearch {

	private GraphSearchCondition source;

	private GraphSearchCondition target;

	private GraphSearchCondition edge;

	@Builder.Default
	private Integer limit = 10;

	@Builder.Default
	private Long maxId = Long.MAX_VALUE;

}
