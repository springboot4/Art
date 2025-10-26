package com.art.ai.dao.dataobject;

import com.art.mybatis.common.base.BaseEntity;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serial;

/**
 * 会话变量持久化实体
 *
 * @author fxz
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("ai_conversation_state")
public class AiConversationStateDO extends BaseEntity {

	@Serial
	private static final long serialVersionUID = -1L;

	/** 关联会话主键 */
	private Long conversationId;

	/** 应用 ID */
	private Long appId;

	/** 会话变量 JSON 快照 */
	private String varsJson;

}
