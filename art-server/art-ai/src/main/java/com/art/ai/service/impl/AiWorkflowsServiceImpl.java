package com.art.ai.service.impl;

import com.art.ai.core.convert.AiWorkflowsConvert;
import com.art.ai.core.dto.AiWorkflowsDTO;
import com.art.ai.core.dto.AiWorkflowsPageDTO;
import com.art.ai.manager.AiWorkflowsManager;
import com.art.ai.service.AiWorkflowsService;
import com.baomidou.mybatisplus.core.metadata.IPage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author fxz
 * @date 2025-07-31
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class AiWorkflowsServiceImpl implements AiWorkflowsService {

	private final AiWorkflowsManager aiWorkflowsManager;

	/**
	 * 添加
	 */
	@Override
	public Boolean addAiWorkflows(AiWorkflowsDTO aiWorkflowsDTO) {
		return aiWorkflowsManager.addAiWorkflows(aiWorkflowsDTO) > 0;
	}

	/**
	 * 修改
	 */
	@Override
	public Boolean updateAiWorkflows(AiWorkflowsDTO aiWorkflowsDTO) {
		return aiWorkflowsManager.updateAiWorkflowsById(aiWorkflowsDTO) > 0;
	}

	/**
	 * 分页
	 */
	@Override
	public IPage<AiWorkflowsDTO> pageAiWorkflows(AiWorkflowsPageDTO aiWorkflowsPageDTO) {
		return AiWorkflowsConvert.INSTANCE.convertPage(aiWorkflowsManager.pageAiWorkflows(aiWorkflowsPageDTO));
	}

	/**
	 * 获取单条
	 */
	@Override
	public AiWorkflowsDTO findById(Long id) {
		return AiWorkflowsConvert.INSTANCE.convert(aiWorkflowsManager.findById(id));
	}

	/**
	 * 根据应用ID获取工作流
	 */
	@Override
	public AiWorkflowsDTO findByAppId(Long appId) {
		return AiWorkflowsConvert.INSTANCE
			.convert(aiWorkflowsManager.findByWrapper(new AiWorkflowsDTO().setAppId(appId)));
	}

	/**
	 * 获取全部
	 */
	@Override
	public List<AiWorkflowsDTO> findAll() {
		return AiWorkflowsConvert.INSTANCE.convertList(aiWorkflowsManager.listAiWorkflows());
	}

	/**
	 * 删除
	 */
	@Override
	public Boolean deleteAiWorkflows(Long id) {
		return aiWorkflowsManager.deleteAiWorkflowsById(id) > 0;
	}

}