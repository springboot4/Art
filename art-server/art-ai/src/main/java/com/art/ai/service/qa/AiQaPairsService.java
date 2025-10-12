package com.art.ai.service.qa;

import com.art.ai.core.dto.qa.AiQaPairsDTO;
import com.art.ai.core.dto.qa.AiQaPairsPageDTO;
import com.art.ai.core.dto.qa.AiQaSimilarQuestionDTO;
import com.baomidou.mybatisplus.core.metadata.IPage;

import java.util.List;

/**
 * QA问答对服务
 *
 * @author fxz
 * @since 2025/10/12
 */
public interface AiQaPairsService {

	/**
	 * 添加QA对
	 */
	AiQaPairsDTO addQaPair(AiQaPairsDTO dto);

	/**
	 * 修改QA对
	 */
	Boolean updateQaPair(AiQaPairsDTO dto);

	/**
	 * 删除QA对
	 */
	Boolean deleteQaPair(Long id);

	/**
	 * 获取单条
	 */
	AiQaPairsDTO findById(Long id);

	/**
	 * 分页查询
	 */
	IPage<AiQaPairsDTO> page(AiQaPairsPageDTO pageDTO);

	/**
	 * 根据数据集ID查询所有QA对
	 */
	List<AiQaPairsDTO> listByDatasetId(Long datasetId);

	/**
	 * 启用/禁用QA对
	 */
	Boolean toggleEnabled(Long id, Boolean enabled);

	/**
	 * 触发QA对向量化
	 */
	Boolean triggerVectorization(Long id);

	/**
	 * 添加相似问题
	 */
	AiQaSimilarQuestionDTO addSimilarQuestion(AiQaSimilarQuestionDTO dto);

	/**
	 * 删除相似问题
	 */
	Boolean deleteSimilarQuestion(Long id);

	/**
	 * 查询QA对的所有相似问题
	 */
	List<AiQaSimilarQuestionDTO> listSimilarQuestions(Long qaPairId);

}
