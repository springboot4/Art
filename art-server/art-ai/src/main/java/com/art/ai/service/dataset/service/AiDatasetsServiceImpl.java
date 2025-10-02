package com.art.ai.service.dataset.service;

import cn.hutool.core.io.FileUtil;
import com.art.ai.core.constants.DatasetsIndexTypeConstants;
import com.art.ai.core.convert.AiDatasetsConvert;
import com.art.ai.core.convert.AiDocumentsConvert;
import com.art.ai.core.dto.dataset.AiDatasetsDTO;
import com.art.ai.core.dto.dataset.AiDatasetsPageDTO;
import com.art.ai.core.dto.dataset.AiDatasetsUploadDocDTO;
import com.art.ai.core.dto.document.AiDocumentsDTO;
import com.art.ai.dao.dataobject.AiDatasetsDO;
import com.art.ai.manager.AiDatasetsManager;
import com.art.ai.manager.AiDocumentsManager;
import com.art.ai.service.dataset.rag.constant.EmbedStoreTypeConstants;
import com.art.ai.service.dataset.rag.constant.EmbeddingStatusEnum;
import com.art.ai.service.dataset.rag.constant.GraphStatusEnum;
import com.art.ai.service.dataset.rag.constant.KnowledgeConstants;
import com.art.ai.service.dataset.rag.embedding.entity.EmbeddingIngestParam;
import com.art.ai.service.dataset.rag.embedding.entity.EmbeddingModelConfig;
import com.art.ai.service.dataset.rag.embedding.entity.EmbeddingStoreConfig;
import com.art.ai.service.dataset.rag.embedding.service.EmbeddingService;
import com.art.ai.service.dataset.rag.file.DocumentLoaderFactory;
import com.art.ai.service.dataset.rag.graph.GraphService;
import com.art.common.security.core.utils.SecurityUtil;
import com.art.common.tenant.context.TenantContextHolder;
import com.art.core.common.exception.ArtException;
import com.art.core.common.model.ResultOpt;
import com.art.core.common.util.AsyncUtil;
import com.art.core.common.util.SpringUtil;
import com.art.system.api.file.FileServiceApi;
import com.art.system.api.file.dto.FileDownloadDTO;
import com.baomidou.mybatisplus.core.metadata.IPage;
import dev.langchain4j.data.document.DefaultDocument;
import dev.langchain4j.data.document.Document;
import dev.langchain4j.data.document.DocumentSplitter;
import dev.langchain4j.data.document.Metadata;
import dev.langchain4j.data.document.splitter.DocumentSplitters;
import dev.langchain4j.data.segment.TextSegment;
import dev.langchain4j.model.chat.ChatModel;
import dev.langchain4j.model.embedding.EmbeddingModel;
import dev.langchain4j.model.openai.OpenAiChatModel;
import dev.langchain4j.model.openai.OpenAiEmbeddingModel;
import dev.langchain4j.store.embedding.EmbeddingStore;
import dev.langchain4j.store.embedding.pgvector.PgVectorEmbeddingStore;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

/**
 * @author fxz
 * @date 2025-09-07
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class AiDatasetsServiceImpl implements AiDatasetsService {

	private final AiDatasetsManager aiDatasetsManager;

	private final AiDocumentsManager aiDocumentsManager;

	private final FileServiceApi fileServiceApi;

	private final EmbeddingService embeddingService;

	private final GraphService graphService;

	/**
	 * 创建数据集
	 */
	@Override
	public AiDatasetsDTO addAiDatasets(AiDatasetsDTO aiDatasetsDTO) {
		return aiDatasetsManager.addAiDatasets(aiDatasetsDTO);
	}

	/**
	 * 修改
	 */
	@Override
	public Boolean updateAiDatasets(AiDatasetsDTO aiDatasetsDTO) {
		return aiDatasetsManager.updateAiDatasetsById(aiDatasetsDTO) > 0;
	}

	/**
	 * 分页
	 */
	@Override
	public IPage<AiDatasetsDTO> pageAiDatasets(AiDatasetsPageDTO aiDatasetsPageDTO) {
		return AiDatasetsConvert.INSTANCE.convertPage(aiDatasetsManager.pageAiDatasets(aiDatasetsPageDTO));
	}

	/**
	 * 获取单条
	 */
	@Override
	public AiDatasetsDTO findById(Long id) {
		return AiDatasetsConvert.INSTANCE.convert(aiDatasetsManager.findById(id));
	}

	/**
	 * 获取全部
	 */
	@Override
	public List<AiDatasetsDTO> findAll() {
		return AiDatasetsConvert.INSTANCE.convertList(aiDatasetsManager.listAiDatasets());
	}

	/**
	 * 删除
	 */
	@Override
	public Boolean deleteAiDatasets(Long id) {
		return aiDatasetsManager.deleteAiDatasetsById(id) > 0;
	}

	/**
	 * 数据集文档上传
	 */
	@Override
	public Boolean uploadDoc(AiDatasetsUploadDocDTO uploadDoc) {
		try {
			Long datasetsId = uploadDoc.getDatasetsId();

			// todo fxz 数据集权限校验
			AiDatasetsDO datasetsDO = aiDatasetsManager.findById(datasetsId);
			AiDatasetsDTO datasetsDTO = AiDatasetsConvert.INSTANCE.convert(datasetsDO);

			// 加载文件信息
			String fileUrl = ResultOpt.of(fileServiceApi.preSignUploadUrl(
					new FileDownloadDTO().setFileName(uploadDoc.getFileName()).setBucket(uploadDoc.getBucketName())))
				.getData()
				.orElseThrow(() -> new ArtException("文件不存在"));

			// todo fxz 文件类型
			Document document = DocumentLoaderFactory.loadDocument(fileUrl, FileUtil.extName(uploadDoc.getFileName()))
				.orElseThrow(() -> new ArtException("不支持的文件类型"));

			AiDocumentsDTO documentsDTO = new AiDocumentsDTO();
			documentsDTO.setDatasetId(datasetsId);
			documentsDTO.setContent(document.text());
			documentsDTO.setFileName(uploadDoc.getFileName());
			documentsDTO.setTitle(uploadDoc.getFileName());
			documentsDTO.setBucketName(uploadDoc.getBucketName());
			documentsDTO.setGraphicalStatus(GraphStatusEnum.NONE);
			documentsDTO.setEmbeddingStatus(EmbeddingStatusEnum.NONE);
			documentsDTO = AiDocumentsConvert.INSTANCE.convert(aiDocumentsManager.addAiDocuments(documentsDTO));

			asyncIndex(datasetsDTO, documentsDTO, uploadDoc.getIndexTypes());

			return Boolean.TRUE;
		}
		catch (Exception e) {
			log.error("uploadDoc error", e);
			return Boolean.FALSE;
		}
	}

	private void asyncIndex(AiDatasetsDTO aiDatasetsDTO, AiDocumentsDTO documentsDTO, String indexTypes) {
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
			ChatModel chatModel = OpenAiChatModel.builder()
				.baseUrl(SpringUtil.getProperty("tmp.open-ai.base-url"))
				.apiKey(SpringUtil.getProperty("tmp.open-ai.api-key"))
				.modelName(datasetsDTO.getGraphicModel())
				.logRequests(true)
				.timeout(Duration.ofSeconds(60))
				.maxRetries(1)
				.build();

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

	private EmbeddingModel getEmbeddingModel(String embeddingModel, String embeddingModelProvider) {
		EmbeddingModelConfig embeddingModelConfig = getEmbeddingModelConfig(embeddingModel, embeddingModelProvider);

		return OpenAiEmbeddingModel.builder()
			.apiKey(embeddingModelConfig.getApiKey())
			.baseUrl(embeddingModelConfig.getBaseUrl())
			.maxRetries(embeddingModelConfig.getMaxRetries())
			.timeout(embeddingModelConfig.getTimeout())
			.dimensions(embeddingModelConfig.getDimensions())
			.modelName(embeddingModelConfig.getModelName())
			.logRequests(true)
			.build();
	}

	private EmbeddingStore<TextSegment> getEmbeddingStore(Long collectionBindingId) {
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

	private EmbeddingModelConfig getEmbeddingModelConfig(String embeddingModel, String embeddingModelProvider) {
		// todo fxz 模型管理
		return EmbeddingModelConfig.builder()
			.baseUrl(SpringUtil.getProperty("tmp.embedding.base-url"))
			.apiKey(SpringUtil.getProperty("tmp.open-ai.api-key"))
			.timeout(Duration.ofSeconds(30))
			.maxRetries(0)
			.dimensions(1024)
			.modelName(embeddingModel)
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