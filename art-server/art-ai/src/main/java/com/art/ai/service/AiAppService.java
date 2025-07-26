package com.art.ai.service;

import com.art.ai.core.dto.AiAppDTO;
import com.art.ai.core.dto.AiAppPageDTO;
import com.baomidou.mybatisplus.core.metadata.IPage;

import java.util.List;

/**
 * @author fxz
 * @date 2025-07-25
 */
public interface AiAppService {

	/**
	 * 添加
	 */
	Boolean addAiApp(AiAppDTO aiAppDTO);

	/**
	 * 修改
	 */
	Boolean updateAiApp(AiAppDTO aiAppDTO);

	/**
	 * 分页
	 */
	IPage<AiAppDTO> pageAiApp(AiAppPageDTO aiAppPageDTO);

	/**
	 * 获取单条
	 */
	AiAppDTO findById(Long id);

	/**
	 * 获取全部
	 */
	List<AiAppDTO> findAll();

	/**
	 * 删除
	 */
	Boolean deleteAiApp(Long id);

}