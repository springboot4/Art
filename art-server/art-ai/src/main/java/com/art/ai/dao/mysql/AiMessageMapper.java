package com.art.ai.dao.mysql;

import com.art.ai.dao.dataobject.AiMessageDO;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * AI消息Mapper
 *
 * @author fxz
 * @date 2025-10-18
 */
@Mapper
public interface AiMessageMapper extends BaseMapper<AiMessageDO> {

	/**
	 * 获取会话的最近N条消息（按时间正序）
	 * @param conversationId 会话ID
	 * @param limit 数量限制
	 * @return 消息列表
	 */
	List<AiMessageDO> selectRecentMessages(@Param("conversationId") Long conversationId, @Param("limit") int limit);

}
