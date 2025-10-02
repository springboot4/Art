package com.art.ai.service.dataset.service;

import com.art.ai.core.dto.dataset.AiDatasetsDTO;
import com.art.ai.core.dto.dataset.AiDatasetsPageDTO;
import com.art.ai.core.dto.dataset.AiDatasetsUploadDocDTO;
import com.baomidou.mybatisplus.core.metadata.IPage;

import java.util.List;

/**
 * @author fxz
 * @date 2025-09-07
 */
public interface AiDatasetsService {

	/**
	 * 创建数据集
	 */
	AiDatasetsDTO addAiDatasets(AiDatasetsDTO aiDatasetsDTO);

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

	/**
	 * 数据集文档上传
	 */
	Boolean uploadDoc(AiDatasetsUploadDocDTO uploadDoc);

}