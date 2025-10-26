package com.art.ai.dao.mysql;

import com.art.ai.dao.dataobject.AiConversationStateDO;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * 会话变量持久化访问层
 *
 * @author fxz
 */
@Mapper
public interface AiConversationStateMapper extends BaseMapper<AiConversationStateDO> {

}
