package com.art.ai.service.document.impl;

import com.art.ai.core.constants.DatasetsIndexTypeConstants;
import com.art.ai.core.constants.DocumentConstants;
import com.art.ai.core.convert.AiDatasetsConvert;
import com.art.ai.core.convert.AiDocumentsConvert;
import com.art.ai.core.dto.dataset.AiDatasetsDTO;
import com.art.ai.core.dto.document.AiDocumentsDTO;
import com.art.ai.core.dto.document.AiDocumentsPageDTO;
import com.art.ai.core.model.AiModelInvokeOptions;
import com.art.ai.manager.AiDatasetsManager;
import com.art.ai.manager.AiDocumentsManager;
import com.art.ai.service.dataset.rag.constant.EmbedStoreTypeConstants;
import com.art.ai.service.dataset.rag.constant.EmbeddingStatusEnum;
import com.art.ai.service.dataset.rag.constant.GraphStatusEnum;
import com.art.ai.service.dataset.rag.constant.KnowledgeConstants;
import com.art.ai.service.dataset.rag.embedding.entity.EmbeddingIngestParam;
import com.art.ai.service.dataset.rag.embedding.entity.EmbeddingStoreConfig;
import com.art.ai.service.dataset.rag.embedding.service.EmbeddingService;
import com.art.ai.service.dataset.rag.graph.GraphService;
import com.art.ai.service.dataset.rag.graph.cleanup.GraphDocumentCleanupService;
import com.art.ai.service.dataset.rag.graph.entity.GraphEdge;
import com.art.ai.service.dataset.rag.graph.entity.GraphEdgeSearch;
import com.art.ai.service.dataset.rag.graph.entity.GraphSearchCondition;
import com.art.ai.service.dataset.rag.graph.entity.GraphVertex;
import com.art.ai.service.dataset.rag.graph.entity.GraphVertexSearch;
import com.art.ai.service.dataset.rag.graph.store.GraphStore;
import com.art.ai.service.dataset.service.AiDocumentSegmentService;
import com.art.ai.service.document.AiDocumentsService;
import com.art.ai.service.model.runtime.AiModelRuntimeService;
import com.art.common.security.core.utils.SecurityUtil;
import com.art.common.tenant.context.TenantContextHolder;
import com.art.core.common.exception.ArtException;
import com.art.core.common.util.AsyncUtil;
import com.art.core.common.util.SpringUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import dev.langchain4j.data.document.DefaultDocument;
import dev.langchain4j.data.document.Document;
import dev.langchain4j.data.document.DocumentSplitter;
import dev.langchain4j.data.document.Metadata;
import dev.langchain4j.data.document.splitter.DocumentSplitters;
import dev.langchain4j.data.segment.TextSegment;
import dev.langchain4j.model.chat.ChatModel;
import dev.langchain4j.model.embedding.EmbeddingModel;
import dev.langchain4j.store.embedding.EmbeddingStore;
import dev.langchain4j.store.embedding.filter.Filter;
import dev.langchain4j.store.embedding.filter.comparison.ContainsString;
import dev.langchain4j.store.embedding.filter.comparison.IsEqualTo;
import dev.langchain4j.store.embedding.pgvector.PgVectorEmbeddingStore;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.tuple.Triple;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDateTime;
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

	private final AiDatasetsManager aiDatasetsManager;

	private final AiDocumentSegmentService aiDocumentSegmentService;

	private final GraphStore graphStore;

	private final GraphDocumentCleanupService graphDocumentCleanupService;

	private final EmbeddingService embeddingService;

	private final GraphService graphService;

	private final AiModelRuntimeService aiModelRuntimeService;

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
		getEmbeddingStore(null).removeAll(new IsEqualTo(DOCUMENT_KEY, id));

		// 智能删除图信息 - 使用新的清理服务
		graphDocumentCleanupService.cleanupDocumentGraphData(id);

		// 删除分段信息
		aiDocumentSegmentService.deleteByDocumentId(id, null);

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

	/**
	 * 重新索引
	 */
	@Override
	public Boolean reIndexDocument(Long documentId, String indexType) {
		Long tenantId = TenantContextHolder.getTenantId();
		Authentication authentication = SecurityUtil.getAuthentication();

		AsyncUtil.run(() -> {
			try {
				TenantContextHolder.setTenantId(tenantId);
				SecurityUtil.setAuthentication(authentication);

				AiDocumentsDTO documentsDTO = AiDocumentsConvert.INSTANCE
					.convert(aiDocumentsManager.findById(documentId));
				if (documentsDTO == null) {
					throw new ArtException("文档不存在");
				}

				AiDatasetsDTO datasetsDTO = AiDatasetsConvert.INSTANCE
					.convert(aiDatasetsManager.findById(documentsDTO.getDatasetId()));
				if (datasetsDTO == null) {
					throw new ArtException("数据集不存在");
				}

				removeIndex(documentsDTO, indexType);

				asyncIndex(datasetsDTO, documentsDTO, indexType);
			}
			finally {
				TenantContextHolder.clear();
				SecurityUtil.setAuthentication(null);
			}
		});

		return Boolean.TRUE;
	}

	private void removeIndex(AiDocumentsDTO documentsDTO, String indexType) {
		if (indexType.contains(DatasetsIndexTypeConstants.EMBEDDING)) {
			try {
				EmbeddingStore<TextSegment> embeddingStore = getEmbeddingStore(null);
				embeddingStore.removeAll(new IsEqualTo(KnowledgeConstants.DOCUMENT_KEY, documentsDTO.getId()));

				aiDocumentSegmentService.deleteByDocumentId(documentsDTO.getId(),
						DocumentConstants.VECTOR_STORE_SEGMENT);
			}
			catch (Exception e) {
				log.error("remove embedding index error", e);
			}
		}

		if (indexType.contains(DatasetsIndexTypeConstants.GRAPH)) {
			try {
				graphDocumentCleanupService.cleanupDocumentGraphData(documentsDTO.getId());

				aiDocumentSegmentService.deleteByDocumentId(documentsDTO.getId(), DocumentConstants.GRAPH_SEGMENT);
			}
			catch (Exception e) {
				log.error("remove graph index error", e);
			}
		}
	}

	public void asyncIndex(AiDatasetsDTO aiDatasetsDTO, AiDocumentsDTO documentsDTO, String indexTypes) {
		Long tenantId = TenantContextHolder.getTenantId();
		Authentication authentication = SecurityUtil.getAuthentication();

		Metadata metadata = new Metadata();
		metadata.put(KnowledgeConstants.DATASET_KEY, String.valueOf(documentsDTO.getDatasetId()));
		metadata.put(KnowledgeConstants.DOCUMENT_KEY, String.valueOf(documentsDTO.getId()));
		Document document = new DefaultDocument(documentsDTO.getContent(), metadata);

		if (indexTypes.contains(DatasetsIndexTypeConstants.EMBEDDING)) {
			AsyncUtil.run(() -> {
				try {
					TenantContextHolder.setTenantId(tenantId);
					SecurityUtil.setAuthentication(authentication);

					log.info("开始异步向量化索引，datasetsId={}, documentId={}", aiDatasetsDTO.getId(), documentsDTO.getId());
					indexEmbedding(aiDatasetsDTO, documentsDTO, document);
				}
				catch (Exception e) {
					log.error("asyncIndex error", e);
				}
				finally {
					TenantContextHolder.clear();
					SecurityUtil.setAuthentication(null);
				}
			});
		}

		if (indexTypes.contains(DatasetsIndexTypeConstants.GRAPH)) {
			AsyncUtil.run(() -> {
				try {
					TenantContextHolder.setTenantId(tenantId);
					SecurityUtil.setAuthentication(authentication);

					log.info("开始异步图谱索引，datasetsId={}, documentId={}", aiDatasetsDTO.getId(), documentsDTO.getId());
					indexGraph(aiDatasetsDTO, documentsDTO, document);
				}
				catch (Exception e) {
					log.error("asyncIndex error", e);
				}
				finally {
					TenantContextHolder.clear();
					SecurityUtil.setAuthentication(null);
				}
			});
		}
	}

	private void indexGraph(AiDatasetsDTO datasetsDTO, AiDocumentsDTO documentsDTO, Document document) {
		try {
			documentsDTO.setGraphicalStatusChangeTime(LocalDateTime.now());
			documentsDTO.setGraphicalStatus(GraphStatusEnum.DOING);
			aiDocumentsManager.updateAiDocumentsById(documentsDTO);

			// todo fxz 模型管理tmp:
			ChatModel chatModel = getChatModel(datasetsDTO.getGraphicModel());

			graphService.ingest(document, chatModel);

			documentsDTO.setGraphicalStatus(GraphStatusEnum.DONE);
		}
		catch (Exception e) {
			log.error("初始化图谱模型失败", e);
			documentsDTO.setGraphicalStatus(GraphStatusEnum.FAIL);
		}
		finally {
			aiDocumentsManager.updateAiDocumentsById(documentsDTO);
		}
	}

	private void indexEmbedding(AiDatasetsDTO datasetsDTO, AiDocumentsDTO documentsDTO, Document document) {
		try {
			documentsDTO.setEmbeddingStatusChangeTime(LocalDateTime.now());
			documentsDTO.setEmbeddingStatus(EmbeddingStatusEnum.DOING);
			aiDocumentsManager.updateAiDocumentsById(documentsDTO);

			EmbeddingIngestParam embeddingIngestParam = EmbeddingIngestParam.builder()
				.document(document)
				.embeddingModel(
						getEmbeddingModel(datasetsDTO.getEmbeddingModel(), datasetsDTO.getEmbeddingModelProvider()))
				.embeddingStore(getEmbeddingStore(datasetsDTO.getCollectionBindingId()))
				.documentSplitter(createDocumentSplitter())
				.build();
			embeddingService.ingest(embeddingIngestParam);

			documentsDTO.setEmbeddingStatus(EmbeddingStatusEnum.DONE);
		}
		catch (Exception e) {
			log.error("初始化向量化模型失败", e);
			documentsDTO.setEmbeddingStatus(EmbeddingStatusEnum.FAIL);
		}
		finally {
			aiDocumentsManager.updateAiDocumentsById(documentsDTO);
		}
	}

	private DocumentSplitter createDocumentSplitter() {
		return DocumentSplitters.recursive(KnowledgeConstants.MAX_SEGMENT_SIZE_IN_TOKENS,
				KnowledgeConstants.MAX_OVERLAP_SIZE_IN_TOKENS);
	}

	public EmbeddingModel getEmbeddingModel(String embeddingModel, String embeddingModelProvider) {
		return aiModelRuntimeService.acquireEmbeddingModel(null, Long.valueOf(embeddingModel));
	}

	public ChatModel getChatModel(String modelName) {
		AiModelInvokeOptions options = AiModelInvokeOptions.builder().timeout(Duration.ofSeconds(60)).build();
		return aiModelRuntimeService.acquireChatModel(null, Long.valueOf(modelName), options);
	}

	public EmbeddingStore<TextSegment> getEmbeddingStore(Long collectionBindingId) {
		EmbeddingStoreConfig embeddingStoreConfig = getEmbeddingStoreConfig(collectionBindingId);

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

	private EmbeddingStoreConfig getEmbeddingStoreConfig(Long collectionBindingId) {
		// todo fxz 根据集合id查询向量库存储配置 根据存储配置类型实例化不同的存储实现
		return EmbeddingStoreConfig.builder()
			.storeType(EmbedStoreTypeConstants.PGVECTOR)
			.collectionId(collectionBindingId)
			.build();
	}

}