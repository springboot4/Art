package com.art.ai.service.dataset.rag.retrieval.entity;

import com.fasterxml.jackson.annotation.JsonCreator;

import java.util.stream.Stream;

/**
 * 召回类型枚举
 *
 * @author fxz
 * @since 2025/10/05
 */
public enum RetrievalType {

	VECTOR("vector", "向量召回"),

	GRAPH("graph", "图谱召回"),

	HYBRID("hybrid", "混合召回");

	private final String code;

	private final String description;

	RetrievalType(String code, String description) {
		this.code = code;
		this.description = description;
	}

	public String getCode() {
		return code;
	}

	public String getDescription() {
		return description;
	}

	@JsonCreator
	public static RetrievalType fromValue(String value) {
		return Stream.of(RetrievalType.values())
			.filter(status -> status.getCode().equals(value))
			.findAny()
			.orElseThrow(() -> new IllegalArgumentException("Unknown value: " + value));
	}

}