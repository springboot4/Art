package com.art.ai.service.agent;

import com.art.ai.core.dto.AiAgentDTO;
import com.art.ai.core.dto.AiAgentPageDTO;
import com.baomidou.mybatisplus.core.metadata.IPage;

import java.util.List;

/**
 * Agent 定义服务
 *
 * @author fxz
 * @since 2025-11-01
 */
public interface AgentService {

	Boolean add(AiAgentDTO dto);

	Boolean update(AiAgentDTO dto);

	Boolean delete(Long id);

	AiAgentDTO findById(Long id);

	List<AiAgentDTO> listByAppId(Long appId);

	IPage<AiAgentDTO> page(AiAgentPageDTO pageDTO);

	Boolean saveDraft(AiAgentDTO dto);

	Boolean publish(AiAgentDTO dto);

	AiAgentDTO findPublishedByAppId(Long appId);

	AiAgentDTO findDraftByAppId(Long appId);

	AiAgentDTO findLastAgent(Long appId);

}
