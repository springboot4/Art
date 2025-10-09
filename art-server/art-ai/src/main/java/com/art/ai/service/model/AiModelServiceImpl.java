package com.art.ai.service.model;

import com.art.ai.core.convert.AiModelConvert;
import com.art.ai.core.dto.AiModelDTO;
import com.art.ai.core.dto.AiModelPageDTO;
import com.art.ai.manager.AiModelManager;
import com.baomidou.mybatisplus.core.metadata.IPage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author fxz
 * @date 2025-10-09
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class AiModelServiceImpl implements AiModelService {

	private final AiModelManager aiModelManager;

	/**
	 * 添加
	 */
	@Override
	public Boolean addAiModel(AiModelDTO aiModelDTO) {
		return aiModelManager.addAiModel(aiModelDTO) > 0;
	}

	/**
	 * 修改
	 */
	@Override
	public Boolean updateAiModel(AiModelDTO aiModelDTO) {
		return aiModelManager.updateAiModelById(aiModelDTO) > 0;
	}

	/**
	 * 分页
	 */
	@Override
	public IPage<AiModelDTO> pageAiModel(AiModelPageDTO aiModelPageDTO) {
		return AiModelConvert.INSTANCE.convertPage(aiModelManager.pageAiModel(aiModelPageDTO));
	}

	/**
	 * 获取单条
	 */
	@Override
	public AiModelDTO findById(Long id) {
		return AiModelConvert.INSTANCE.convert(aiModelManager.findById(id));
	}

	/**
	 * 获取全部
	 */
	@Override
	public List<AiModelDTO> findAll() {
		return AiModelConvert.INSTANCE.convertList(aiModelManager.listAiModel());
	}

	/**
	 * 删除
	 */
	@Override
	public Boolean deleteAiModel(Long id) {
		return aiModelManager.deleteAiModelById(id) > 0;
	}

}