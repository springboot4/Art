package com.art.ai.service.app.impl;

import com.art.ai.core.convert.AiAppConvert;
import com.art.ai.core.dto.AiAppDTO;
import com.art.ai.core.dto.AiAppPageDTO;
import com.art.ai.manager.AiAppManager;
import com.art.ai.service.app.AiAppService;
import com.baomidou.mybatisplus.core.metadata.IPage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author fxz
 * @date 2025-07-25
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class AiAppServiceImpl implements AiAppService {

	private final AiAppManager aiAppManager;

	/**
	 * 添加
	 */
	@Override
	public Boolean addAiApp(AiAppDTO aiAppDTO) {
		return aiAppManager.addAiApp(aiAppDTO) > 0;
	}

	/**
	 * 修改
	 */
	@Override
	public Boolean updateAiApp(AiAppDTO aiAppDTO) {
		return aiAppManager.updateAiAppById(aiAppDTO) > 0;
	}

	/**
	 * 分页
	 */
	@Override
	public IPage<AiAppDTO> pageAiApp(AiAppPageDTO aiAppPageDTO) {
		return AiAppConvert.INSTANCE.convertPage(aiAppManager.pageAiApp(aiAppPageDTO));
	}

	/**
	 * 获取单条
	 */
	@Override
	public AiAppDTO findById(Long id) {
		return AiAppConvert.INSTANCE.convert(aiAppManager.findById(id));
	}

	/**
	 * 获取全部
	 */
	@Override
	public List<AiAppDTO> findAll() {
		return AiAppConvert.INSTANCE.convertList(aiAppManager.listAiApp());
	}

	/**
	 * 删除
	 */
	@Override
	public Boolean deleteAiApp(Long id) {
		return aiAppManager.deleteAiAppById(id) > 0;
	}

}