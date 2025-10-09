package com.art.ai.service.model;

import com.art.ai.core.convert.AiModelPlatformConvert;
import com.art.ai.core.dto.AiModelPlatformDTO;
import com.art.ai.core.dto.AiModelPlatformPageDTO;
import com.art.ai.manager.AiModelPlatformManager;
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
public class AiModelPlatformServiceImpl implements AiModelPlatformService {

	private final AiModelPlatformManager aiModelPlatformManager;

	/**
	 * 添加
	 */
	@Override
	public Boolean addAiModelPlatform(AiModelPlatformDTO aiModelPlatformDTO) {
		return aiModelPlatformManager.addAiModelPlatform(aiModelPlatformDTO) > 0;
	}

	/**
	 * 修改
	 */
	@Override
	public Boolean updateAiModelPlatform(AiModelPlatformDTO aiModelPlatformDTO) {
		return aiModelPlatformManager.updateAiModelPlatformById(aiModelPlatformDTO) > 0;
	}

	/**
	 * 分页
	 */
	@Override
	public IPage<AiModelPlatformDTO> pageAiModelPlatform(AiModelPlatformPageDTO aiModelPlatformPageDTO) {
		return AiModelPlatformConvert.INSTANCE
			.convertPage(aiModelPlatformManager.pageAiModelPlatform(aiModelPlatformPageDTO));
	}

	/**
	 * 获取单条
	 */
	@Override
	public AiModelPlatformDTO findById(Long id) {
		return AiModelPlatformConvert.INSTANCE.convert(aiModelPlatformManager.findById(id));
	}

	/**
	 * 获取全部
	 */
	@Override
	public List<AiModelPlatformDTO> findAll() {
		return AiModelPlatformConvert.INSTANCE.convertList(aiModelPlatformManager.listAiModelPlatform());
	}

	/**
	 * 删除
	 */
	@Override
	public Boolean deleteAiModelPlatform(Long id) {
		return aiModelPlatformManager.deleteAiModelPlatformById(id) > 0;
	}

}