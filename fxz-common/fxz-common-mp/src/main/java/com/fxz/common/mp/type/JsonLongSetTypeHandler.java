package com.fxz.common.mp.type;

import com.baomidou.mybatisplus.extension.handlers.AbstractJsonTypeHandler;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fxz.common.jackson.util.JacksonUtil;

import java.util.Set;

/**
 * @author fxz
 */
public class JsonLongSetTypeHandler extends AbstractJsonTypeHandler<Object> {

	private static final TypeReference<Set<Long>> TYPE_REFERENCE = new TypeReference<Set<Long>>() {
	};

	@Override
	protected Object parse(String json) {
		return JacksonUtil.parseObject(json, TYPE_REFERENCE);
	}

	@Override
	protected String toJson(Object obj) {
		return JacksonUtil.toJsonString(obj);
	}

}