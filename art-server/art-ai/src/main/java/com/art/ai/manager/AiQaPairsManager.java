package com.art.ai.manager;

import cn.hutool.core.util.StrUtil;
import com.art.ai.core.dto.qa.AiQaPairsPageDTO;
import com.art.ai.dao.dataobject.AiQaPairsDO;
import com.art.ai.dao.dataobject.AiQaSimilarQuestionsDO;
import com.art.ai.dao.mysql.AiQaPairsMapper;
import com.art.ai.dao.mysql.AiQaSimilarQuestionsMapper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

/**
 * QA问答对Manager 统一管理所有数据库操作
 *
 * @author fxz
 * @since 2025/10/12
 */
@Component
@RequiredArgsConstructor
public class AiQaPairsManager {

	private final AiQaPairsMapper qaPairsMapper;

	private final AiQaSimilarQuestionsMapper similarQuestionsMapper;

	/**
	 * 根据ID查询QA对
	 */
	public AiQaPairsDO findById(Long id) {
		return qaPairsMapper.selectById(id);
	}

	/**
	 * 根据Hash精确匹配查询
	 */
	public AiQaPairsDO findByQuestionHash(String questionHash, Long datasetId) {
		LambdaQueryWrapper<AiQaPairsDO> wrapper = Wrappers.lambdaQuery(AiQaPairsDO.class)
			.eq(AiQaPairsDO::getQuestionHash, questionHash)
			.eq(AiQaPairsDO::getDatasetId, datasetId)
			.eq(AiQaPairsDO::getEnabled, true)
			.orderByDesc(AiQaPairsDO::getPriority, AiQaPairsDO::getHitCount)
			.last("LIMIT 1");

		return qaPairsMapper.selectOne(wrapper);
	}

	/**
	 * 根据相似问题Hash查询
	 */
	public List<AiQaSimilarQuestionsDO> findSimilarByHash(String similarHash) {
		return similarQuestionsMapper.findByHashWithEnabledQa(similarHash);
	}

	/**
	 * 根据数据集查询所有启用的QA对
	 */
	public List<AiQaPairsDO> listByDatasetId(Long datasetId) {
		LambdaQueryWrapper<AiQaPairsDO> wrapper = Wrappers.lambdaQuery(AiQaPairsDO.class)
			.eq(AiQaPairsDO::getDatasetId, datasetId)
			.eq(AiQaPairsDO::getEnabled, true)
			.orderByDesc(AiQaPairsDO::getPriority, AiQaPairsDO::getHitCount);

		return qaPairsMapper.selectList(wrapper);
	}

	/**
	 * 分页查询QA对
	 */
	public Page<AiQaPairsDO> page(AiQaPairsPageDTO pageDTO) {
		LambdaQueryWrapper<AiQaPairsDO> wrapper = Wrappers.lambdaQuery(AiQaPairsDO.class)
			.eq(pageDTO.getDatasetId() != null, AiQaPairsDO::getDatasetId, pageDTO.getDatasetId())
			.eq(pageDTO.getEnabled() != null, AiQaPairsDO::getEnabled, pageDTO.getEnabled())
			.eq(pageDTO.getPriority() != null, AiQaPairsDO::getPriority, pageDTO.getPriority())
			.eq(pageDTO.getVectorIndexed() != null, AiQaPairsDO::getVectorIndexed, pageDTO.getVectorIndexed())
			.and(StrUtil.isNotBlank(pageDTO.getQuestion()),
					w -> w.like(AiQaPairsDO::getQuestion, pageDTO.getQuestion())
						.or()
						.like(AiQaPairsDO::getAnswer, pageDTO.getQuestion()))
			.orderByDesc(AiQaPairsDO::getPriority)
			.orderByDesc(AiQaPairsDO::getHitCount)
			.orderByDesc(AiQaPairsDO::getUpdateTime);

		return qaPairsMapper.selectPage(Page.of(pageDTO.getCurrent(), pageDTO.getSize()), wrapper);
	}

	/**
	 * 新增QA对
	 */
	public int insert(AiQaPairsDO qaPair) {
		return qaPairsMapper.insert(qaPair);
	}

	/**
	 * 更新QA对
	 */
	public int updateById(AiQaPairsDO qaPair) {
		return qaPairsMapper.updateById(qaPair);
	}

	/**
	 * 删除QA对
	 */
	public int deleteById(Long id) {
		return qaPairsMapper.deleteById(id);
	}

	/**
	 * 更新命中次数
	 */
	public int updateHitCount(Long qaPairId) {
		LambdaUpdateWrapper<AiQaPairsDO> wrapper = Wrappers.lambdaUpdate(AiQaPairsDO.class)
			.eq(AiQaPairsDO::getId, qaPairId)
			.setSql("hit_count = hit_count + 1")
			.set(AiQaPairsDO::getLastHitTime, LocalDateTime.now());

		return qaPairsMapper.update(null, wrapper);
	}

	/**
	 * 批量更新向量化状态
	 */
	public int updateVectorIndexed(Long qaPairId, String vectorId) {
		LambdaUpdateWrapper<AiQaPairsDO> wrapper = Wrappers.lambdaUpdate(AiQaPairsDO.class)
			.eq(AiQaPairsDO::getId, qaPairId)
			.set(AiQaPairsDO::getVectorIndexed, true)
			.set(AiQaPairsDO::getVectorId, vectorId);

		return qaPairsMapper.update(null, wrapper);
	}

	/**
	 * 新增相似问题
	 */
	public int insertSimilarQuestion(AiQaSimilarQuestionsDO similarQuestion) {
		return similarQuestionsMapper.insert(similarQuestion);
	}

	/**
	 * 根据ID查询相似问题
	 */
	public AiQaSimilarQuestionsDO findSimilarQuestionById(Long id) {
		return similarQuestionsMapper.selectById(id);
	}

	/**
	 * 删除相似问题
	 */
	public int deleteSimilarQuestion(Long id) {
		return similarQuestionsMapper.deleteById(id);
	}

	/**
	 * 根据QA对ID查询所有相似问题
	 */
	public List<AiQaSimilarQuestionsDO> listSimilarByQaPairId(Long qaPairId) {
		LambdaQueryWrapper<AiQaSimilarQuestionsDO> wrapper = Wrappers.lambdaQuery(AiQaSimilarQuestionsDO.class)
			.eq(AiQaSimilarQuestionsDO::getQaPairId, qaPairId);

		return similarQuestionsMapper.selectList(wrapper);
	}

}
