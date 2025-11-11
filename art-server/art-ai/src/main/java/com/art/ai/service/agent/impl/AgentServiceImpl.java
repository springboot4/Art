package com.art.ai.service.agent.impl;

import com.art.ai.core.convert.AiAgentConvert;
import com.art.ai.core.dto.AiAgentDTO;
import com.art.ai.core.dto.AiAgentPageDTO;
import com.art.ai.dao.dataobject.AiAgentDO;
import com.art.ai.manager.AiAgentManager;
import com.art.ai.service.agent.AgentService;
import com.art.ai.service.agent.spec.AgentSpecJsonHelper;
import com.art.ai.service.agent.spec.AgentVersionGenerator;
import com.art.core.common.exception.ArtException;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Agent 定义实现
 *
 * @author fxz
 * @since 2025-11-01
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class AgentServiceImpl implements AgentService {

	private final AiAgentManager agentManager;

	private static final String STATUS_DRAFT = "draft";

	private static final String STATUS_PUBLISHED = "published";

	@Override
	public Boolean add(AiAgentDTO dto) {
		validateSpec(dto.getSpecJson());
		dto.setStatus(StringUtils.defaultIfBlank(dto.getStatus(), STATUS_DRAFT));
		return agentManager.insert(dto) > 0;
	}

	@Override
	public Boolean update(AiAgentDTO dto) {
		if (dto.getId() == null) {
			throw new ArtException("Agent ID 不能为空");
		}
		validateSpec(dto.getSpecJson());
		return agentManager.updateById(dto) > 0;
	}

	@Override
	public Boolean delete(Long id) {
		return agentManager.deleteById(id) > 0;
	}

	@Override
	public AiAgentDTO findById(Long id) {
		return AiAgentConvert.INSTANCE.convert(agentManager.selectById(id));
	}

	@Override
	public List<AiAgentDTO> listByAppId(Long appId) {
		return AiAgentConvert.INSTANCE.convertList(agentManager.listByAppId(appId));
	}

	@Override
	public IPage<AiAgentDTO> page(AiAgentPageDTO pageDTO) {
		Page<AiAgentDO> page = agentManager.page(pageDTO);
		return AiAgentConvert.INSTANCE.convertPage(page);
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public Boolean saveDraft(AiAgentDTO dto) {
		if (dto.getAppId() == null) {
			throw new ArtException("应用 ID 不能为空");
		}
		validateSpec(dto.getSpecJson());
		dto.setStatus(STATUS_DRAFT);
		AiAgentDO existed = agentManager.findLatestByAppIdAndStatus(dto.getAppId(), STATUS_DRAFT);
		if (existed != null) {
			dto.setId(existed.getId());
		}
		dto.setSpecJson(AgentSpecJsonHelper.ensureVersion(dto.getSpecJson(), STATUS_DRAFT));
		return agentManager.saveOrUpdate(dto) > 0;
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public Boolean publish(AiAgentDTO dto) {
		if (dto.getAppId() == null) {
			throw new ArtException("应用 ID 不能为空");
		}

		AiAgentDTO source = dto;
		if (StringUtils.isBlank(dto.getSpecJson())) {
			AiAgentDO draft = agentManager.findLatestByAppIdAndStatus(dto.getAppId(), STATUS_DRAFT);
			if (draft == null) {
				throw new ArtException("未找到可发布的草稿");
			}
			source = AiAgentConvert.INSTANCE.convert(draft);
		}

		validateSpec(source.getSpecJson());
		String version = AgentVersionGenerator.newVersion();
		source.setId(null);
		source.setStatus(STATUS_PUBLISHED);
		source.setSpecJson(AgentSpecJsonHelper.ensureVersion(source.getSpecJson(), version));

		return agentManager.insert(source) > 0;
	}

	@Override
	public AiAgentDTO findPublishedByAppId(Long appId) {
		return AiAgentConvert.INSTANCE.convert(agentManager.findLatestByAppIdAndStatus(appId, STATUS_PUBLISHED));
	}

	@Override
	public AiAgentDTO findDraftByAppId(Long appId) {
		return AiAgentConvert.INSTANCE.convert(agentManager.findLatestByAppIdAndStatus(appId, STATUS_DRAFT));
	}

	@Override
	public AiAgentDTO findLastAgent(Long appId) {
		return AiAgentConvert.INSTANCE.convert(agentManager.findLatestByAppIdAndStatus(appId, null));
	}

	private void validateSpec(String specJson) {
		if (StringUtils.isBlank(specJson)) {
			throw new ArtException("Agent 配置不能为空");
		}
		try {
			AgentSpecJsonHelper.parse(specJson);
		}
		catch (Exception ex) {
			log.error("Agent 规格解析失败", ex);
			throw new ArtException("Agent 规格无效: " + ex.getMessage(), ex);
		}
	}

}
