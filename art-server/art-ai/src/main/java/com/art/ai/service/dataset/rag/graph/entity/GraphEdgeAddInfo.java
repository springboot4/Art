package com.art.ai.service.dataset.rag.graph.entity;

import lombok.Data;

@Data
public class GraphEdgeAddInfo {

	private GraphEdge edge;

	private GraphSearchCondition sourceFilter;

	private GraphSearchCondition targetFilter;

}
