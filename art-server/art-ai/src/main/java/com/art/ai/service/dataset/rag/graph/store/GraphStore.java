package com.art.ai.service.dataset.rag.graph.store;

import com.art.ai.service.dataset.rag.graph.entity.GraphEdge;
import com.art.ai.service.dataset.rag.graph.entity.GraphEdgeAddInfo;
import com.art.ai.service.dataset.rag.graph.entity.GraphEdgeEditInfo;
import com.art.ai.service.dataset.rag.graph.entity.GraphEdgeSearch;
import com.art.ai.service.dataset.rag.graph.entity.GraphSearchCondition;
import com.art.ai.service.dataset.rag.graph.entity.GraphVertex;
import com.art.ai.service.dataset.rag.graph.entity.GraphVertexSearch;
import com.art.ai.service.dataset.rag.graph.entity.GraphVertexUpdateInfo;
import org.apache.commons.lang3.tuple.Triple;

import java.util.List;

/**
 * @author fxz
 */
public interface GraphStore {

	Triple<GraphVertex, GraphEdge, GraphVertex> addEdge(GraphEdgeAddInfo addInfo);

	Triple<GraphVertex, GraphEdge, GraphVertex> updateEdge(GraphEdgeEditInfo edgeEditInfo);

	Triple<GraphVertex, GraphEdge, GraphVertex> getEdge(GraphEdgeSearch search);

	List<Triple<GraphVertex, GraphEdge, GraphVertex>> searchEdges(GraphEdgeSearch search);

	GraphVertex getVertex(GraphVertexSearch search);

	boolean addVertex(GraphVertex vertex);

	GraphVertex updateVertex(GraphVertexUpdateInfo updateInfo);

	List<GraphVertex> searchVertices(GraphVertexSearch search);

	boolean addVertexes(List<GraphVertex> vertexes);

	/**
	 * 删除节点和边
	 */
	void deleteVertices(GraphSearchCondition filter);

}
