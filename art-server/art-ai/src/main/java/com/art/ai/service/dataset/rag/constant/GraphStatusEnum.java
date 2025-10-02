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
public enum GraphStatusEnum implements IEnum<Integer> {

	NONE(1, "未图谱化"), DOING(2, "图谱化中"), DONE(3, "已图谱化"), FAIL(4, "图谱化失败");

	private final Integer value;

	@JsonValue
	private final String desc;

	@JsonCreator
	public static GraphStatusEnum fromValue(Integer value) {
		return Stream.of(GraphStatusEnum.values())
			.filter(status -> status.getValue().equals(value))
			.findAny()
			.orElseThrow(() -> new IllegalArgumentException("Unknown value: " + value));
	}

}
