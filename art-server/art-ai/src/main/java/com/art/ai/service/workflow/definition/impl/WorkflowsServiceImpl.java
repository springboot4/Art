package com.art.ai.service.workflow.definition.impl;

import com.art.ai.core.convert.AiWorkflowsConvert;
import com.art.ai.core.dto.AiWorkflowsDTO;
import com.art.ai.core.dto.AiWorkflowsPageDTO;
import com.art.ai.dao.dataobject.AiWorkflowsDO;
import com.art.ai.manager.AiWorkflowsManager;
import com.art.ai.service.workflow.definition.WorkflowsService;
import com.art.common.lock.core.constants.RedissonLockType;
import com.art.common.lock.core.entity.LockEntity;
import com.art.common.lock.core.factory.RedissonLockServiceFactory;
import com.art.common.lock.core.service.RedissonService;
import com.art.core.common.exception.ArtException;
import com.baomidou.mybatisplus.core.metadata.IPage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;

/**
 * @author fxz
 * @date 2025-07-31
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class WorkflowsServiceImpl implements WorkflowsService {

	private final AiWorkflowsManager aiWorkflowsManager;

	private final RedissonService lockService = RedissonLockServiceFactory.getLock(RedissonLockType.REENTRANT);

	private static final String WORKFLOW_DRAFT_VERSION = "draft";

	private static final String WORKFLOW_DRAFT_LOCK_PREFIX = "ai:workflow:draft:";

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

	/**
	 * 根据应用ID获取工作流
	 */
	@Override
	public AiWorkflowsDTO findByAppId(Long appId) {
		return AiWorkflowsConvert.INSTANCE
			.convert(aiWorkflowsManager.findByWrapper(new AiWorkflowsDTO().setAppId(appId)));
	}

	/**
	 * 发布工作流
	 */
	@Override
	public Boolean publish(AiWorkflowsDTO aiWorkflowsDTO) {
		if (Objects.isNull(aiWorkflowsDTO.getAppId())) {
			throw new ArtException("应用ID不能为空");
		}

		AiWorkflowsDTO workflowsDTO = findByAppId(aiWorkflowsDTO.getAppId());
		if (Objects.isNull(workflowsDTO)) {
			throw new ArtException("工作流不存在!");
		}

		workflowsDTO.setId(null);
		workflowsDTO.setCreateBy(null);
		workflowsDTO.setCreateTime(null);
		workflowsDTO.setUpdateBy(null);
		workflowsDTO.setUpdateTime(null);
		workflowsDTO.setVersion(AiWorkflowsDO.newVersion());
		return addAiWorkflows(workflowsDTO);
	}

	/**
	 * 保存草稿
	 */
	@Override
	@Transactional(rollbackFor = Exception.class)
	public Boolean draft(AiWorkflowsDTO aiWorkflowsDTO) {
		if (Objects.isNull(aiWorkflowsDTO.getAppId())) {
			throw new ArtException("应用ID不能为空");
		}

		LockEntity lockEntity = new LockEntity().setLockName(buildDraftLockName(aiWorkflowsDTO.getAppId()));
		boolean locked = lockService.lock(lockEntity);
		if (!locked) {
			throw new ArtException("保存草稿频繁，请稍后再试");
		}

		try {
			aiWorkflowsDTO.setVersion(WORKFLOW_DRAFT_VERSION);
			aiWorkflowsDTO.setId(null);

			AiWorkflowsDTO queryDTO = new AiWorkflowsDTO().setAppId(aiWorkflowsDTO.getAppId())
				.setTenantId(aiWorkflowsDTO.getTenantId())
				.setVersion(WORKFLOW_DRAFT_VERSION)
				.setType(aiWorkflowsDTO.getType());
			AiWorkflowsDO existed = aiWorkflowsManager.findByWrapper(queryDTO);
			if (Objects.nonNull(existed)) {
				aiWorkflowsDTO.setId(existed.getId());
			}

			return aiWorkflowsManager.saveOrUpdate(aiWorkflowsDTO) > 0;
		}
		finally {
			lockService.unlock(lockEntity);
			lockService.clearThreadLock();
		}
	}

	private String buildDraftLockName(Long appId) {
		return WORKFLOW_DRAFT_LOCK_PREFIX + appId;
	}

}