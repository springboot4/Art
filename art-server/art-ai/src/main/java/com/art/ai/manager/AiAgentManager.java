package com.art.ai.manager;

import com.art.ai.core.convert.AiAgentConvert;
import com.art.ai.core.dto.AiAgentDTO;
import com.art.ai.core.dto.AiAgentPageDTO;
import com.art.ai.dao.dataobject.AiAgentDO;
import com.art.ai.dao.mysql.AiAgentMapper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Objects;

/**
 * Agent 管理器
 *
 * @author fxz
 * @since 2025-11-01
 */
@Component
@RequiredArgsConstructor
public class AiAgentManager {

	private final AiAgentMapper agentMapper;

	public Page<AiAgentDO> page(AiAgentPageDTO pageDTO) {
		LambdaQueryWrapper<AiAgentDO> wrapper = Wrappers.<AiAgentDO>lambdaQuery()
			.eq(Objects.nonNull(pageDTO.getAppId()), AiAgentDO::getAppId, pageDTO.getAppId())
			.eq(StringUtils.isNotBlank(pageDTO.getStatus()), AiAgentDO::getStatus, pageDTO.getStatus())
			.like(StringUtils.isNotBlank(pageDTO.getName()), AiAgentDO::getName, pageDTO.getName())
			.eq(Objects.nonNull(pageDTO.getTenantId()), AiAgentDO::getTenantId, pageDTO.getTenantId())
			.orderByDesc(AiAgentDO::getUpdateTime);
		return agentMapper.selectPage(Page.of(pageDTO.getCurrent(), pageDTO.getSize()), wrapper);
	}

	public List<AiAgentDO> listByAppId(Long appId) {
		return agentMapper
			.selectList(Wrappers.<AiAgentDO>lambdaQuery().eq(Objects.nonNull(appId), AiAgentDO::getAppId, appId));
	}

	public int insert(AiAgentDTO dto) {
		return agentMapper.insert(AiAgentConvert.INSTANCE.convert(dto));
	}

	public int updateById(AiAgentDTO dto) {
		return agentMapper.updateById(AiAgentConvert.INSTANCE.convert(dto));
	}

	public int deleteById(Long id) {
		return agentMapper.deleteById(id);
	}

	public AiAgentDO selectById(Long id) {
		return agentMapper.selectById(id);
	}

	public AiAgentDO findLatestByAppIdAndStatus(Long appId, String status) {
		return agentMapper.selectOne(Wrappers.<AiAgentDO>lambdaQuery()
			.eq(AiAgentDO::getAppId, appId)
			.eq(StringUtils.isNotBlank(status), AiAgentDO::getStatus, status)
			.orderByDesc(AiAgentDO::getUpdateTime)
			.last("limit 1"));
	}

	public int saveOrUpdate(AiAgentDTO dto) {
		AiAgentDO entity = AiAgentConvert.INSTANCE.convert(dto);
		if (entity.getId() != null) {
			return agentMapper.updateById(entity);
		}
		return agentMapper.insert(entity);
	}

}
