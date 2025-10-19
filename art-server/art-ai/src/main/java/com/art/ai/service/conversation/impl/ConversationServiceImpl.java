package com.art.ai.service.conversation.impl;

import cn.hutool.core.util.StrUtil;
import com.art.ai.core.convert.AiConversationConvert;
import com.art.ai.core.dto.conversation.AiConversationDTO;
import com.art.ai.core.dto.conversation.ConversationCreateDTO;
import com.art.ai.core.dto.conversation.ConversationQueryDTO;
import com.art.ai.core.enums.ConversationStatusEnum;
import com.art.ai.dao.dataobject.AiConversationDO;
import com.art.ai.manager.AiConversationManager;
import com.art.ai.service.conversation.ConversationService;
import com.art.common.security.core.utils.SecurityUtil;
import com.art.core.common.exception.ArtException;
import com.art.core.common.model.PageResult;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

/**
 * AI会话服务实现
 *
 * @author fxz
 * @date 2025-10-18
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class ConversationServiceImpl implements ConversationService {

	private final AiConversationManager conversationManager;

	@Override
	@Transactional(rollbackFor = Exception.class)
	public AiConversationDTO create(ConversationCreateDTO vo) {
		Long userId = SecurityUtil.getUser().getUserId();
		log.info("创建会话，appId={}, endUserId={}", vo.getAppId(), userId);

		AiConversationDO conversationDO = new AiConversationDO();
		conversationDO.setConversationUuid(UUID.randomUUID().toString());
		conversationDO.setAppId(vo.getAppId());
		conversationDO.setEndUserId(userId);
		conversationDO.setName(StrUtil.isNotBlank(vo.getName()) ? vo.getName() : "新会话");
		conversationDO.setStatus(ConversationStatusEnum.ACTIVE.getCode());
		conversationDO.setMessageCount(0);
		conversationDO.setTotalTokens(0);
		conversationDO.setTotalCost(BigDecimal.ZERO);
		conversationDO.setLastMessageAt(LocalDateTime.now());
		conversationManager.insert(conversationDO);
		log.info("创建会话成功，conversationId={}, uuid={}", conversationDO.getId(), conversationDO.getConversationUuid());

		return AiConversationConvert.INSTANCE.convert(conversationDO);
	}

	@Override
	public PageResult<AiConversationDTO> page(ConversationQueryDTO queryVO) {
		Page<AiConversationDO> page = conversationManager.page(queryVO.getCurrent(), queryVO.getSize(),
				queryVO.getAppId(), queryVO.getEndUserId(), queryVO.getStatus(), queryVO.getName());

		return PageResult.success(AiConversationConvert.INSTANCE.convertPage(page));
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public void updateName(Long conversationId, String name) {
		log.info("更新会话标题，conversationId={}, name={}", conversationId, name);

		AiConversationDO conversationDO = conversationManager.findById(conversationId);
		if (conversationDO == null) {
			throw new ArtException("会话不存在");
		}

		conversationDO.setName(name);
		conversationManager.updateById(conversationDO);
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public void updateStats(Long conversationId, int messageDelta, int tokensDelta, BigDecimal costDelta) {
		log.debug("更新会话统计，conversationId={}, messageDelta={}, tokensDelta={}, costDelta={}", conversationId,
				messageDelta, tokensDelta, costDelta);

		int rows = conversationManager.updateStats(conversationId, messageDelta, tokensDelta, costDelta);
		if (rows == 0) {
			log.warn("更新会话统计失败，会话可能不存在，conversationId={}", conversationId);
			throw new ArtException("更新会话统计失败");
		}

		if (messageDelta > 0) {
			AiConversationDO conversationDO = conversationManager.findById(conversationId);
			if (conversationDO != null && conversationDO.getFirstMessageAt() == null) {
				conversationDO.setFirstMessageAt(LocalDateTime.now());
				conversationManager.updateById(conversationDO);
			}
		}
	}

}
