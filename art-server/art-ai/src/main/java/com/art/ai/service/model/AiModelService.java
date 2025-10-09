package com.art.ai.service.model;

import com.art.ai.core.dto.AiModelDTO;
import com.art.ai.core.dto.AiModelPageDTO;
import com.baomidou.mybatisplus.core.metadata.IPage;

import java.util.List;

/**
 * @author fxz
 * @date 2025-10-09
 */
public interface AiModelService {

	/**
	 * 添加
	 */
	Boolean addAiModel(AiModelDTO aiModelDTO);

	/**
	 * 修改
	 */
	Boolean updateAiModel(AiModelDTO aiModelDTO);

	/**
	 * 分页
	 */
	IPage<AiModelDTO> pageAiModel(AiModelPageDTO aiModelPageDTO);

	/**
	 * 获取单条
	 */
	AiModelDTO findById(Long id);

	/**
	 * 获取全部
	 */
	List<AiModelDTO> findAll();

	/**
	 * 删除
	 */
	Boolean deleteAiModel(Long id);

}