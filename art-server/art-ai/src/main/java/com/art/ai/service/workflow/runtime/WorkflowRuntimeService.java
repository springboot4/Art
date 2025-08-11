package com.art.ai.service.workflow.runtime;

import com.art.ai.core.dto.AiWorkflowRuntimeDTO;
import com.art.ai.core.dto.AiWorkflowRuntimePageDTO;
import com.baomidou.mybatisplus.core.metadata.IPage;

import java.util.List;

/**
 * @author fxz
 * @date 2025-08-09
 */
public interface WorkflowRuntimeService {

	/**
	 * 添加
	 */
	AiWorkflowRuntimeDTO addAiWorkflowRuntime(AiWorkflowRuntimeDTO aiWorkflowRuntimeDTO);

	/**
	 * 修改
	 */
	Boolean updateAiWorkflowRuntime(AiWorkflowRuntimeDTO aiWorkflowRuntimeDTO);

	/**
	 * 分页
	 */
	IPage<AiWorkflowRuntimeDTO> pageAiWorkflowRuntime(AiWorkflowRuntimePageDTO aiWorkflowRuntimePageDTO);

	/**
	 * 获取单条
	 */
	AiWorkflowRuntimeDTO findById(Long id);

	/**
	 * 获取全部
	 */
	List<AiWorkflowRuntimeDTO> findAll();

	/**
	 * 删除
	 */
	Boolean deleteAiWorkflowRuntime(Long id);

}