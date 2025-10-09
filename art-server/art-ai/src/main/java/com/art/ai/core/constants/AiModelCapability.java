package com.art.ai.core.constants;

import java.util.Arrays;

/**
 * 模型类型
 *
 * @author fxz
 */
public enum AiModelCapability {

	/**
	 * 聊天或文本生成模型
	 */
	CHAT,

	/**
	 * 向量嵌入模型
	 */
	EMBEDDING,

	/**
	 * 重排序模型
	 */
	RERANK,

	/**
	 * 图像生成模型
	 */
	IMAGE,

	/**
	 * 音频模型
	 */
	AUDIO;

	public static AiModelCapability fromCode(String code) {
		if (code == null) {
			return CHAT;
		}

		final String normalized = code.trim().toLowerCase();
		return Arrays.stream(values())
			.filter(item -> item.name().equalsIgnoreCase(normalized) || item.alias().equals(normalized))
			.findFirst()
			.orElse(CHAT);
	}

	private String alias() {
		return switch (this) {
			case CHAT -> "llm";
			case EMBEDDING -> "vector";
			case RERANK -> "rerank";
			case IMAGE -> "image";
			case AUDIO -> "audio";
		};
	}

}
