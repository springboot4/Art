package com.art.ai.service.model.support;

import com.art.ai.dao.dataobject.AiModelDO;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.json.JsonMapper;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StringUtils;

import java.util.Collections;
import java.util.Map;

/**
 * 模型配置解析器
 *
 * @author fxz
 */
@UtilityClass
@Slf4j
public final class AiModelConfigurationParser {

	private static final ObjectMapper MAPPER = JsonMapper.builder()
		.findAndAddModules()
		.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES)
		.build();

	public static AiModelRuntimeConfig toRuntimeConfig(AiModelDO modelDO) {
		Map<String, Object> rawConfig = readConfig(modelDO.getConfig());

		String modelIdentifier = modelDO.getName();

		Long maxInput = firstNonNull(modelDO.getMaxInputTokens(), rawConfig.get("maxInputTokens"));
		Long maxOutput = firstNonNull(modelDO.getMaxOutputTokens(), rawConfig.get("maxOutputTokens"));
		Long contextWindow = firstNonNull(rawConfig.get("contextWindow"), maxInput);

		return AiModelRuntimeConfig.builder()
			.modelIdentifier(modelIdentifier)
			.maxInputTokens(maxInput)
			.maxOutputTokens(maxOutput)
			.contextWindowTokens(contextWindow)
			.parameters(Collections.unmodifiableMap(rawConfig))
			.build();
	}

	public static Map<String, Object> readConfig(String configJson) {
		if (!StringUtils.hasText(configJson)) {
			return Collections.emptyMap();
		}
		try {
			return MAPPER.readValue(configJson, new TypeReference<>() {
			});
		}
		catch (JsonProcessingException e) {
			log.warn("Failed to parse model config json", e);
			return Collections.emptyMap();
		}
	}

	private static Long firstNonNull(Object primary, Object secondary) {
		Long first = toLong(primary);
		if (first != null) {
			return first;
		}
		Long second = toLong(secondary);
		if (second != null) {
			return second;
		}
		return null;
	}

	private static Long toLong(Object value) {
		if (value == null) {
			return null;
		}
		if (value instanceof Number number) {
			return number.longValue();
		}
		if (value instanceof String text && StringUtils.hasText(text)) {
			try {
				return Long.parseLong(text);
			}
			catch (NumberFormatException ex) {
				log.debug("Unable to parse long from value: {}", text, ex);
			}
		}
		return null;
	}

}
