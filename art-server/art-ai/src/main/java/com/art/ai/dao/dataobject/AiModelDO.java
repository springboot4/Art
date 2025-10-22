package com.art.ai.dao.dataobject;

import com.art.ai.core.constants.AiModelCapability;
import com.art.ai.core.constants.ModelFeature;
import com.art.ai.service.model.support.AiModelConfigurationParser;
import com.art.ai.service.model.support.AiModelRuntimeConfig;
import com.art.mybatis.common.base.BaseEntity;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

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

	/**
	 * 获取模型支持的特性集合
	 * @return 特性集合
	 */
	public Set<ModelFeature> getFeatures() {
		Map<String, Object> configMap = configAsMap();
		Object featuresObj = configMap.get("features");

		if (featuresObj == null) {
			return Collections.emptySet();
		}

		if (featuresObj instanceof List<?> featuresList) {
			return featuresList.stream()
				.filter(item -> item instanceof String)
				.map(Object::toString)
				.map(this::parseFeature)
				.filter(Objects::nonNull)
				.collect(Collectors.toSet());
		}

		return Collections.emptySet();
	}

	/**
	 * 判断模型是否支持某个特性
	 * @param feature 特性
	 * @return 是否支持
	 */
	public boolean hasFeature(ModelFeature feature) {
		return getFeatures().contains(feature);
	}

	/**
	 * 解析特性字符串
	 */
	private ModelFeature parseFeature(String featureStr) {
		try {
			return ModelFeature.valueOf(featureStr);
		}
		catch (IllegalArgumentException e) {
			return null;
		}
	}

}
