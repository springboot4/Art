package com.art.ai.manager;

import com.art.ai.dao.dataobject.AiConversationStateDO;
import com.art.ai.dao.mysql.AiConversationStateMapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

/**
 * 会话变量 Manager
 *
 * @author fxz
 */
@Component
@RequiredArgsConstructor
public class AiConversationStateManager {

	private final AiConversationStateMapper conversationStateMapper;

	/**
	 * 根据会话ID查询
	 */
	public AiConversationStateDO findByConversationId(Long conversationId) {
		return conversationStateMapper.selectOne(Wrappers.<AiConversationStateDO>lambdaQuery()
			.eq(AiConversationStateDO::getConversationId, conversationId)
			.last("limit 1"));
	}

	/**
	 * 新增
	 */
	public int insert(AiConversationStateDO stateDO) {
		return conversationStateMapper.insert(stateDO);
	}

	/**
	 * 根据ID更新
	 */
	public int updateById(AiConversationStateDO stateDO) {
		return conversationStateMapper.updateById(stateDO);
	}

}
