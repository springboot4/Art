package com.art.ai.dao.mysql;

import com.art.ai.dao.dataobject.AiConversationDO;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.math.BigDecimal;

/**
 * AI会话Mapper
 *
 * @author fxz
 * @date 2025-10-18
 */
@Mapper
public interface AiConversationMapper extends BaseMapper<AiConversationDO> {

	/**
	 * 更新会话统计信息（消息数、Token、成本、最后消息时间）
	 * @param conversationId 会话ID
	 * @param messageDelta 消息数增量
	 * @param tokensDelta Token增量
	 * @param costDelta 成本增量
	 * @return 更新行数
	 */
	int updateStats(@Param("conversationId") Long conversationId, @Param("messageDelta") int messageDelta,
			@Param("tokensDelta") int tokensDelta, @Param("costDelta") BigDecimal costDelta);

}
