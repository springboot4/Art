package com.art.ai.service.dataset;

import com.art.ai.core.dto.AiDatasetsDTO;
import com.art.ai.core.dto.AiDatasetsPageDTO;
import com.baomidou.mybatisplus.core.metadata.IPage;

import java.util.List;

/**
 * @author fxz
 * @date 2025-09-07
 */
public interface AiDatasetsService {

	/**
	 * 添加
	 */
	Boolean addAiDatasets(AiDatasetsDTO aiDatasetsDTO);

	/**
	 * 修改
	 */
	Boolean updateAiDatasets(AiDatasetsDTO aiDatasetsDTO);

	/**
	 * 分页
	 */
	IPage<AiDatasetsDTO> pageAiDatasets(AiDatasetsPageDTO aiDatasetsPageDTO);

	/**
	 * 获取单条
	 */
	AiDatasetsDTO findById(Long id);

	/**
	 * 获取全部
	 */
	List<AiDatasetsDTO> findAll();

	/**
	 * 删除
	 */
	Boolean deleteAiDatasets(Long id);

}