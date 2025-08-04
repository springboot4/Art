package com.art.ai.dao.dataobject;

import com.art.mybatis.common.base.BaseEntity;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * @author fxz
 * @date 2025-07-31
 */
@Data
@TableName("ai_workflows")
public class AiWorkflowsDO extends BaseEntity {

	private static final long serialVersionUID = -1L;

	/** 主键 */
	private Long id;

	/** 应用id */
	private Long appId;

	/** 流程类型 */
	private String type;

	/** 流程版本 */
	private String version;

	/** 图信息 */
	private String graph;

	/** 流程配置 */
	private String features;

	/** 环境变量 */
	private String environmentVariables;

	/** 会话变量 */
	private String conversationVariables;

	/** 租户id */
	private Long tenantId;

	/**
	 * 生成新的流程版本号
	 */
	public static String newVersion() {
		return LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSSSSS"));
	}

}
