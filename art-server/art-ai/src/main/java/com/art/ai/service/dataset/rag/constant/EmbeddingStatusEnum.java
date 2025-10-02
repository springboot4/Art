package com.art.ai.service.dataset.rag.constant;

import com.baomidou.mybatisplus.annotation.IEnum;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.stream.Stream;

/**
 * @author fxz
 */
@Getter
@AllArgsConstructor
public enum EmbeddingStatusEnum implements IEnum<Integer> {

	NONE(1, "未向量化"),

	DOING(2, "向量化中"),

	DONE(3, "已向量化"),

	FAIL(4, "向量化失败");

	private final Integer value;

	@JsonValue
	private final String desc;

	@JsonCreator
	public static EmbeddingStatusEnum fromValue(Integer value) {
		return Stream.of(EmbeddingStatusEnum.values())
			.filter(status -> status.getValue().equals(value))
			.findAny()
			.orElseThrow(() -> new IllegalArgumentException("Unknown value: " + value));
	}

}
