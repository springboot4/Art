package com.art.ai.service.dataset.rag.ner;

/**
 * 实体类型枚举
 *
 * @author fxz
 * @since 2025/10/05
 */
public enum EntityType {

	PERSON("person", "人物"),

	ORGANIZATION("organization", "组织机构"),

	GEO("geo", "地理位置"),

	EVENT("event", "事件");

	private final String code;

	private final String description;

	EntityType(String code, String description) {
		this.code = code;
		this.description = description;
	}

	public String getCode() {
		return code;
	}

	public String getDescription() {
		return description;
	}

	/**
	 * 从字符串获取实体类型
	 */
	public static EntityType fromString(String typeStr) {
		if (typeStr == null) {
			throw new NullPointerException("typeStr is null");
		}

		String lowerStr = typeStr.toLowerCase().trim();
		for (EntityType type : values()) {
			if (type.getCode().equals(lowerStr)) {
				return type;
			}
		}

		// 兼容其他可能的表示方式
		return switch (lowerStr) {
			case "人物", "人名", "person" -> PERSON;
			case "组织", "机构", "组织机构", "org", "organization" -> ORGANIZATION;
			case "地点", "地名", "位置", "geo", "location" -> GEO;
			case "事件", "event" -> EVENT;
			default -> throw new IllegalStateException("Unexpected value: " + lowerStr);
		};
	}

}