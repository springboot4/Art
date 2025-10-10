package com.art.ai.dao.dataobject;

import com.art.ai.core.constants.AiModelCapability;
import com.art.ai.service.model.support.AiModelConfigurationParser;
import com.art.ai.service.model.support.AiModelRuntimeConfig;
import com.art.mybatis.common.base.BaseEntity;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.util.Map;

/**
 * @author fxz
 * @date 2025-10-09
 */
@Data
@TableName("ai_model")
public class AiModelDO extends BaseEntity {

	private static final long serialVersionUID = -1L;

	/** 主键 */
	private Long id;

	/** 模型类型（推理、chat等） */
	private String type;

	/** 模型名称 */
	private String name;

	/** 所属平台 */
	private Long platform;

	/** 是否启用 */
	private Integer enable;

	/** 最大输入长度 */
	private Long maxInputTokens;

	/** 最大输出长度 */
	private Long maxOutputTokens;

	/** 模型配置 */
	private String config;

	/** 租户id */
	private Long tenantId;

	public boolean isEnabled() {
		return Integer.valueOf(1).equals(enable);
	}

	public AiModelCapability capability() {
		return AiModelCapability.fromCode(type);
	}

	public AiModelRuntimeConfig runtimeConfig() {
		return AiModelConfigurationParser.toRuntimeConfig(this);
	}

	public Map<String, Object> configAsMap() {
		return AiModelConfigurationParser.readConfig(config);
	}

}
