package com.art.ai.service;

import com.art.ai.core.dto.AiWorkflowsDTO;
import com.art.ai.core.dto.AiWorkflowsPageDTO;
import com.baomidou.mybatisplus.core.metadata.IPage;

import java.util.List;

/**
 * @author fxz
 * @date 2025-07-31
 */
public interface AiWorkflowsService {

	/**
	 * 添加
	 */
	Boolean addAiWorkflows(AiWorkflowsDTO aiWorkflowsDTO);

	/**
	 * 修改
	 */
	Boolean updateAiWorkflows(AiWorkflowsDTO aiWorkflowsDTO);

	/**
	 * 分页
	 */
	IPage<AiWorkflowsDTO> pageAiWorkflows(AiWorkflowsPageDTO aiWorkflowsPageDTO);

	/**
	 * 获取单条
	 */
	AiWorkflowsDTO findById(Long id);

	/**
	 * 根据应用ID获取工作流
	 */
	AiWorkflowsDTO findByAppId(Long appId);

	/**
	 * 获取全部
	 */
	List<AiWorkflowsDTO> findAll();

	/**
	 * 删除
	 */
	Boolean deleteAiWorkflows(Long id);

}