package com.art.ai.service.dataset.rag.graph.entity;

import dev.langchain4j.store.embedding.filter.Filter;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
public class GraphVertexUpdateInfo {

	private GraphVertex newData;

	private String name;

	private Filter metadataFilter;

}
