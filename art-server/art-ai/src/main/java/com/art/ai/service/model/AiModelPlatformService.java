package com.art.ai.service.model;

import com.art.ai.core.dto.AiModelPlatformDTO;
import com.art.ai.core.dto.AiModelPlatformPageDTO;
import com.baomidou.mybatisplus.core.metadata.IPage;

import java.util.List;

/**
 * @author fxz
 * @date 2025-10-09
 */
public interface AiModelPlatformService {

	/**
	 * 添加
	 */
	Boolean addAiModelPlatform(AiModelPlatformDTO aiModelPlatformDTO);

	/**
	 * 修改
	 */
	Boolean updateAiModelPlatform(AiModelPlatformDTO aiModelPlatformDTO);

	/**
	 * 分页
	 */
	IPage<AiModelPlatformDTO> pageAiModelPlatform(AiModelPlatformPageDTO aiModelPlatformPageDTO);

	/**
	 * 获取单条
	 */
	AiModelPlatformDTO findById(Long id);

	/**
	 * 获取全部
	 */
	List<AiModelPlatformDTO> findAll();

	/**
	 * 删除
	 */
	Boolean deleteAiModelPlatform(Long id);

}