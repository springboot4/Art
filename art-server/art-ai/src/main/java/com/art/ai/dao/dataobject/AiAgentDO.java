package com.art.ai.dao.dataobject;

import com.art.mybatis.common.base.BaseEntity;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serial;

/**
 * Agent 实体
 *
 * @author fxz
 * @since 2025-11-01
 */
@Data
@TableName("ai_agent")
public class AiAgentDO extends BaseEntity {

	@Serial
	private static final long serialVersionUID = -1L;

	/** 应用 ID */
	private Long appId;

	/** Agent 名称 */
	private String name;

	/** Agent 状态：draft/published */
	private String status;

	/** Agent 规格 JSON */
	private String specJson;

	/** 租户 ID */
	private Long tenantId;

}
