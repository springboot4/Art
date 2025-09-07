package com.art.ai.service.dataset.impl;

import com.art.ai.core.convert.AiDatasetsConvert;
import com.art.ai.core.dto.AiDatasetsDTO;
import com.art.ai.core.dto.AiDatasetsPageDTO;
import com.art.ai.manager.AiDatasetsManager;
import com.art.ai.service.dataset.AiDatasetsService;
import com.baomidou.mybatisplus.core.metadata.IPage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

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

	/**
	 * 添加
	 */
	@Override
	public Boolean addAiDatasets(AiDatasetsDTO aiDatasetsDTO) {
		return aiDatasetsManager.addAiDatasets(aiDatasetsDTO) > 0;
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

}