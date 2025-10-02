package com.art.ai.service.document.impl;

import com.art.ai.core.convert.AiDocumentsConvert;
import com.art.ai.core.dto.document.AiDocumentsDTO;
import com.art.ai.core.dto.document.AiDocumentsPageDTO;
import com.art.ai.manager.AiDocumentsManager;
import com.art.ai.service.dataset.rag.graph.entity.GraphEdge;
import com.art.ai.service.dataset.rag.graph.entity.GraphEdgeSearch;
import com.art.ai.service.dataset.rag.graph.entity.GraphSearchCondition;
import com.art.ai.service.dataset.rag.graph.entity.GraphVertex;
import com.art.ai.service.dataset.rag.graph.entity.GraphVertexSearch;
import com.art.ai.service.dataset.rag.graph.store.GraphStore;
import com.art.ai.service.dataset.rag.graph.cleanup.GraphDocumentCleanupService;
import com.art.ai.service.dataset.service.AiDocumentSegmentService;
import com.art.ai.service.document.AiDocumentsService;
import com.art.core.common.util.SpringUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import dev.langchain4j.data.segment.TextSegment;
import dev.langchain4j.store.embedding.EmbeddingStore;
import dev.langchain4j.store.embedding.filter.Filter;
import dev.langchain4j.store.embedding.filter.comparison.ContainsString;
import dev.langchain4j.store.embedding.filter.comparison.IsEqualTo;
import dev.langchain4j.store.embedding.pgvector.PgVectorEmbeddingStore;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.tuple.Triple;
import org.springframework.stereotype.Service;

import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import static com.art.ai.service.dataset.rag.constant.KnowledgeConstants.DOCUMENT_KEY;

/**
 * @author fxz
 * @date 2025-09-09
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class AiDocumentsServiceImpl implements AiDocumentsService {

	private final AiDocumentsManager aiDocumentsManager;

	private final AiDocumentSegmentService aiDocumentSegmentService;

	private final GraphStore graphStore;

	private final GraphDocumentCleanupService graphDocumentCleanupService;

	/**
	 * 添加
	 */
	@Override
	public Boolean addAiDocuments(AiDocumentsDTO aiDocumentsDTO) {
		return aiDocumentsManager.addAiDocuments(aiDocumentsDTO) != null;
	}

	/**
	 * 修改
	 */
	@Override
	public Boolean updateAiDocuments(AiDocumentsDTO aiDocumentsDTO) {
		return aiDocumentsManager.updateAiDocumentsById(aiDocumentsDTO) > 0;
	}

	/**
	 * 分页
	 */
	@Override
	public IPage<AiDocumentsDTO> pageAiDocuments(AiDocumentsPageDTO aiDocumentsPageDTO) {
		return AiDocumentsConvert.INSTANCE.convertPage(aiDocumentsManager.pageAiDocuments(aiDocumentsPageDTO));
	}

	/**
	 * 获取单条
	 */
	@Override
	public AiDocumentsDTO findById(Long id) {
		return AiDocumentsConvert.INSTANCE.convert(aiDocumentsManager.findById(id));
	}

	/**
	 * 获取全部
	 */
	@Override
	public List<AiDocumentsDTO> findAll() {
		return AiDocumentsConvert.INSTANCE.convertList(aiDocumentsManager.listAiDocuments());
	}

	/**
	 * 删除
	 */
	@Override
	public Boolean deleteAiDocuments(Long id) {
		// 删除向量
		getEmbeddingStore().removeAll(new IsEqualTo(DOCUMENT_KEY, id));

		// 智能删除图信息 - 使用新的清理服务
		graphDocumentCleanupService.cleanupDocumentGraphData(id);

		// 删除分段信息
		aiDocumentSegmentService.deleteByDocumentId(id);

		return aiDocumentsManager.deleteAiDocumentsById(id) > 0;
	}

	/**
	 * 查询文档的图谱信息
	 */
	@Override
	public Map<String, Object> queryDocumentGraphInfo(Long documentId) {
		Filter filter = new ContainsString(DOCUMENT_KEY, String.valueOf(documentId));
		GraphSearchCondition condition = GraphSearchCondition.builder().metadataFilter(filter).build();
		GraphEdgeSearch edgeSearch = GraphEdgeSearch.builder().edge(condition).limit(3000).build();
		List<Triple<GraphVertex, GraphEdge, GraphVertex>> triples = graphStore.searchEdges(edgeSearch);

		List<GraphVertex> graphVertexList = triples.stream()
			.map(t -> List.of(t.getLeft(), t.getRight()))
			.flatMap(List::stream)
			.toList();
		Set<GraphEdge> graphEdgeSet = triples.stream().map(Triple::getMiddle).collect(Collectors.toSet());

		GraphVertexSearch vertexSearch = GraphVertexSearch.builder().metadataFilter(filter).limit(3000).build();
		List<GraphVertex> graphVertices = graphStore.searchVertices(vertexSearch);

		Set<GraphVertex> graphVerticesRes = new LinkedHashSet<>();
		graphVerticesRes.addAll(graphVertices);
		graphVerticesRes.addAll(graphVertexList);

		return Map.of("node", graphVerticesRes, "edge", graphEdgeSet);
	}

	private EmbeddingStore<TextSegment> getEmbeddingStore() {
		// todo fxz 向量库管理
		return PgVectorEmbeddingStore.builder()
			.host(SpringUtil.getProperty("tmp.embedding.host"))
			.port(Integer.valueOf(SpringUtil.getProperty("tmp.embedding.port")))
			.database(SpringUtil.getProperty("tmp.embedding.database"))
			.user(SpringUtil.getProperty("tmp.embedding.user"))
			.password(SpringUtil.getProperty("tmp.embedding.password"))
			.dimension(Integer.valueOf(SpringUtil.getProperty("tmp.embedding.dimension")))
			.table(SpringUtil.getProperty("tmp.embedding.table"))
			.createTable(true)
			.dropTableFirst(false)
			.build();
	}

}