package com.art.ai.service.qa;

import com.art.ai.core.convert.AiQaPairsConvert;
import com.art.ai.core.convert.AiQaSimilarQuestionConvert;
import com.art.ai.core.dto.qa.AiQaPairsDTO;
import com.art.ai.core.dto.qa.AiQaPairsPageDTO;
import com.art.ai.core.dto.qa.AiQaSimilarQuestionDTO;
import com.art.ai.dao.dataobject.AiQaPairsDO;
import com.art.ai.dao.dataobject.AiQaSimilarQuestionsDO;
import com.art.ai.manager.AiDatasetsManager;
import com.art.ai.manager.AiQaPairsManager;
import com.art.ai.service.dataset.rag.qa.util.QaHashUtil;
import com.art.ai.service.model.runtime.AiModelRuntimeService;
import com.art.common.security.core.utils.SecurityUtil;
import com.art.common.tenant.context.TenantContextHolder;
import com.art.core.common.exception.ArtException;
import com.art.core.common.util.AsyncUtil;
import com.art.core.common.util.SpringUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import dev.langchain4j.data.segment.TextSegment;
import dev.langchain4j.model.embedding.EmbeddingModel;
import dev.langchain4j.store.embedding.EmbeddingStore;
import dev.langchain4j.store.embedding.pgvector.PgVectorEmbeddingStore;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

import static com.art.ai.core.constants.AiQASourceTypeConstants.MANUAL;

/**
 * QA问答对服务实现
 *
 * @author fxz
 * @since 2025/10/12
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class AiQaPairsServiceImpl implements AiQaPairsService {

	private final AiQaPairsManager qaPairsManager;

	private final AiDatasetsManager datasetsManager;

	private final QaVectorizationService qaVectorizationService;

	private final AiModelRuntimeService aiModelRuntimeService;

	/**
	 * 添加QA对
	 */
	@Override
	@Transactional(rollbackFor = Exception.class)
	public AiQaPairsDTO addQaPair(AiQaPairsDTO dto) {
		if (dto.getDatasetId() == null) {
			throw new ArtException("数据集ID不能为空");
		}
		if (dto.getQuestion() == null || dto.getQuestion().trim().isEmpty()) {
			throw new ArtException("问题不能为空");
		}
		if (dto.getAnswer() == null || dto.getAnswer().trim().isEmpty()) {
			throw new ArtException("答案不能为空");
		}

		AiQaPairsDO qaPair = AiQaPairsConvert.INSTANCE.convert(dto);
		qaPair.setQuestionHash(QaHashUtil.calculateQuestionHash(dto.getQuestion()));

		if (qaPair.getPriority() == null) {
			qaPair.setPriority(1);
		}
		if (qaPair.getEnabled() == null) {
			qaPair.setEnabled(true);
		}
		if (qaPair.getHitCount() == null) {
			qaPair.setHitCount(0);
		}
		if (qaPair.getVectorIndexed() == null) {
			qaPair.setVectorIndexed(false);
		}
		if (StringUtils.isBlank(qaPair.getSourceType())) {
			qaPair.setSourceType(MANUAL);
		}

		int result = qaPairsManager.insert(qaPair);
		if (result <= 0) {
			throw new ArtException("添加QA对失败");
		}

		triggerVectorization(qaPair.getId());

		return AiQaPairsConvert.INSTANCE.convert(qaPair);
	}

	/**
	 * 修改QA对
	 */
	@Override
	@Transactional(rollbackFor = Exception.class)
	public Boolean updateQaPair(AiQaPairsDTO dto) {
		if (dto.getId() == null) {
			throw new ArtException("ID不能为空");
		}

		AiQaPairsDO existing = qaPairsManager.findById(dto.getId());
		if (existing == null) {
			throw new ArtException("QA对不存在");
		}

		AiQaPairsDO qaPair = AiQaPairsConvert.INSTANCE.convert(dto);

		if (dto.getQuestion() != null && !dto.getQuestion().equals(existing.getQuestion())) {
			qaPair.setQuestionHash(QaHashUtil.calculateQuestionHash(dto.getQuestion()));
			qaPair.setVectorIndexed(false);
			qaPair.setVectorId(null);

			triggerVectorization(qaPair.getId());
		}

		return qaPairsManager.updateById(qaPair) > 0;
	}

	/**
	 * 删除QA对
	 */
	@Override
	@Transactional(rollbackFor = Exception.class)
	public Boolean deleteQaPair(Long id) {
		if (id == null) {
			throw new ArtException("ID不能为空");
		}

		// 先删除所有相似问题
		List<AiQaSimilarQuestionsDO> similarQuestions = qaPairsManager.listSimilarByQaPairId(id);
		for (AiQaSimilarQuestionsDO similar : similarQuestions) {
			qaPairsManager.deleteSimilarQuestion(similar.getId());
		}

		// 删除QA对的向量
		try {
			EmbeddingStore<TextSegment> embeddingStore = getEmbeddingStore(null);
			qaVectorizationService.removeQaVector(id, embeddingStore);
		}
		catch (Exception e) {
			log.warn("删除QA向量失败: id={}", id, e);
		}

		// 删除QA对
		return qaPairsManager.deleteById(id) > 0;
	}

	/**
	 * 获取单条
	 */
	@Override
	public AiQaPairsDTO findById(Long id) {
		if (id == null) {
			throw new ArtException("ID不能为空");
		}

		AiQaPairsDO qaPair = qaPairsManager.findById(id);
		return AiQaPairsConvert.INSTANCE.convert(qaPair);
	}

	/**
	 * 分页查询
	 */
	@Override
	public IPage<AiQaPairsDTO> page(AiQaPairsPageDTO pageDTO) {
		return AiQaPairsConvert.INSTANCE.convertPage(qaPairsManager.page(pageDTO));
	}

	/**
	 * 根据数据集ID查询所有QA对
	 */
	@Override
	public List<AiQaPairsDTO> listByDatasetId(Long datasetId) {
		if (datasetId == null) {
			throw new ArtException("数据集ID不能为空");
		}

		List<AiQaPairsDO> list = qaPairsManager.listByDatasetId(datasetId);
		return AiQaPairsConvert.INSTANCE.convertList(list);
	}

	/**
	 * 启用/禁用QA对
	 */
	@Override
	public Boolean toggleEnabled(Long id, Boolean enabled) {
		if (id == null) {
			throw new ArtException("ID不能为空");
		}
		if (enabled == null) {
			throw new ArtException("启用状态不能为空");
		}

		AiQaPairsDO qaPair = new AiQaPairsDO();
		qaPair.setId(id);
		qaPair.setEnabled(enabled);
		qaPair.setUpdateTime(LocalDateTime.now());

		if (!enabled) {
			AiQaPairsDO existing = qaPairsManager.findById(id);
			if (existing != null && existing.getVectorIndexed() && existing.getVectorId() != null) {
				EmbeddingStore<TextSegment> embeddingStore = getEmbeddingStore(null);
				qaVectorizationService.removeQaVector(id, embeddingStore);
			}
		}
		else {
			triggerVectorization(id);
		}

		return qaPairsManager.updateById(qaPair) > 0;
	}

	/**
	 * 触发QA对向量化
	 */
	@Override
	public Boolean triggerVectorization(Long id) {
		if (id == null) {
			throw new ArtException("ID不能为空");
		}

		Long tenantId = TenantContextHolder.getTenantId();
		Authentication authentication = SecurityUtil.getAuthentication();

		AsyncUtil.run(() -> {
			try {
				TenantContextHolder.setTenantId(tenantId);
				SecurityUtil.setAuthentication(authentication);

				log.info("开始QA对向量化: id={}", id);

				AiQaPairsDO qaPair = qaPairsManager.findById(id);
				if (qaPair == null) {
					throw new ArtException("QA对不存在");
				}

				// 获取数据集配置
				var dataset = datasetsManager.findById(qaPair.getDatasetId());
				if (dataset == null) {
					throw new ArtException("数据集不存在");
				}

				// 获取向量化模型和存储
				EmbeddingModel embeddingModel = getEmbeddingModel(dataset.getEmbeddingModel(),
						dataset.getEmbeddingModelProvider());
				EmbeddingStore<TextSegment> embeddingStore = getEmbeddingStore(dataset.getCollectionBindingId());

				// 执行向量化
				qaVectorizationService.vectorizeQaPair(id, embeddingModel, embeddingStore);

				log.info("QA对向量化完成: id={}", id);
			}
			catch (Exception e) {
				log.error("QA对向量化失败: id={}", id, e);
				throw new ArtException("QA对向量化失败: " + e.getMessage());
			}
			finally {
				TenantContextHolder.clear();
				SecurityUtil.setAuthentication(null);
			}
		});

		return true;
	}

	/**
	 * 添加相似问题
	 */
	@Override
	@Transactional(rollbackFor = Exception.class)
	public AiQaSimilarQuestionDTO addSimilarQuestion(AiQaSimilarQuestionDTO dto) {
		if (dto.getQaPairId() == null) {
			throw new ArtException("QA对ID不能为空");
		}
		if (dto.getSimilarQuestion() == null || dto.getSimilarQuestion().trim().isEmpty()) {
			throw new ArtException("相似问题不能为空");
		}

		// 检查QA对是否存在
		AiQaPairsDO qaPair = qaPairsManager.findById(dto.getQaPairId());
		if (qaPair == null) {
			throw new ArtException("QA对不存在");
		}

		// 转换并计算Hash
		AiQaSimilarQuestionsDO similarQuestion = AiQaSimilarQuestionConvert.INSTANCE.convert(dto);
		similarQuestion.setSimilarHash(QaHashUtil.calculateQuestionHash(dto.getSimilarQuestion()));

		int result = qaPairsManager.insertSimilarQuestion(similarQuestion);
		if (result <= 0) {
			throw new ArtException("添加相似问题失败");
		}

		triggerVectorization(dto.getQaPairId());

		return AiQaSimilarQuestionConvert.INSTANCE.convert(similarQuestion);
	}

	/**
	 * 删除相似问题
	 */
	@Override
	@Transactional(rollbackFor = Exception.class)
	public Boolean deleteSimilarQuestion(Long id) {
		if (id == null) {
			throw new ArtException("ID不能为空");
		}

		// 先查询相似问题，获取关联的QA对ID
		AiQaSimilarQuestionsDO similarQuestion = qaPairsManager.findSimilarQuestionById(id);
		Long qaPairId = similarQuestion != null ? similarQuestion.getQaPairId() : null;

		boolean deleted = qaPairsManager.deleteSimilarQuestion(id) > 0;

		if (deleted && qaPairId != null) {
			triggerVectorization(qaPairId);
		}

		return deleted;
	}

	/**
	 * 查询QA对的所有相似问题
	 */
	@Override
	public List<AiQaSimilarQuestionDTO> listSimilarQuestions(Long qaPairId) {
		if (qaPairId == null) {
			throw new ArtException("QA对ID不能为空");
		}

		List<AiQaSimilarQuestionsDO> list = qaPairsManager.listSimilarByQaPairId(qaPairId);
		return AiQaSimilarQuestionConvert.INSTANCE.convertList(list);
	}

	/**
	 * 获取向量化模型
	 */
	private EmbeddingModel getEmbeddingModel(String embeddingModel, String embeddingModelProvider) {
		return aiModelRuntimeService.acquireEmbeddingModel(null, Long.valueOf(embeddingModel));
	}

	/**
	 * 获取向量存储
	 */
	private EmbeddingStore<TextSegment> getEmbeddingStore(Long collectionBindingId) {
		// TODO: 向量库管理，根据配置动态选择向量存储
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
