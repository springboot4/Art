package com.art.ai.service.workflow.runtime;

import com.art.ai.core.convert.AiWorkflowRuntimeConvert;
import com.art.ai.core.dto.AiWorkflowRuntimeDTO;
import com.art.ai.core.dto.AiWorkflowRuntimePageDTO;
import com.art.ai.manager.AiWorkflowRuntimeManager;
import com.baomidou.mybatisplus.core.metadata.IPage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author fxz
 * @date 2025-08-09
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class WorkflowRuntimeServiceImpl implements WorkflowRuntimeService {

	private final AiWorkflowRuntimeManager aiWorkflowRuntimeManager;

	/**
	 * 添加
	 */
	@Override
	public AiWorkflowRuntimeDTO addAiWorkflowRuntime(AiWorkflowRuntimeDTO aiWorkflowRuntimeDTO) {
		return aiWorkflowRuntimeManager.addAiWorkflowRuntime(aiWorkflowRuntimeDTO);
	}

	/**
	 * 修改
	 */
	@Override
	public Boolean updateAiWorkflowRuntime(AiWorkflowRuntimeDTO aiWorkflowRuntimeDTO) {
		return aiWorkflowRuntimeManager.updateAiWorkflowRuntimeById(aiWorkflowRuntimeDTO) > 0;
	}

	/**
	 * 分页
	 */
	@Override
	public IPage<AiWorkflowRuntimeDTO> pageAiWorkflowRuntime(AiWorkflowRuntimePageDTO aiWorkflowRuntimePageDTO) {
		return AiWorkflowRuntimeConvert.INSTANCE
			.convertPage(aiWorkflowRuntimeManager.pageAiWorkflowRuntime(aiWorkflowRuntimePageDTO));
	}

	/**
	 * 获取单条
	 */
	@Override
	public AiWorkflowRuntimeDTO findById(Long id) {
		return AiWorkflowRuntimeConvert.INSTANCE.convert(aiWorkflowRuntimeManager.findById(id));
	}

	/**
	 * 获取全部
	 */
	@Override
	public List<AiWorkflowRuntimeDTO> findAll() {
		return AiWorkflowRuntimeConvert.INSTANCE.convertList(aiWorkflowRuntimeManager.listAiWorkflowRuntime());
	}

	/**
	 * 删除
	 */
	@Override
	public Boolean deleteAiWorkflowRuntime(Long id) {
		return aiWorkflowRuntimeManager.deleteAiWorkflowRuntimeById(id) > 0;
	}

}