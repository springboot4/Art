/*
 * COPYRIGHT (C) 2022 Art AUTHORS(fxzcloud@gmail.com). ALL RIGHTS RESERVED.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.art.common.jackson.util;

import cn.hutool.core.util.ArrayUtil;
import cn.hutool.core.util.StrUtil;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Fxz
 * @version 0.0.1
 * @date 2022/6/30 17:44
 */
public class JacksonUtil {

	private static ObjectMapper objectMapper;

	public static void setObjectMapper(ObjectMapper objectMapper) {
		JacksonUtil.objectMapper = objectMapper;
	}

	@SneakyThrows
	public static String toJsonString(Object object) {
		return objectMapper.writeValueAsString(object);
	}

	@SneakyThrows
	public static <T> T parseObject(byte[] bytes, Class<T> clazz) {
		if (ArrayUtil.isEmpty(bytes)) {
			return null;
		}
		return objectMapper.readValue(bytes, clazz);
	}

	@SneakyThrows
	public static <T> T parseObject(String text, Class<T> clazz) {
		if (StrUtil.isEmpty(text)) {
			return null;
		}
		return objectMapper.readValue(text, clazz);
	}

	@SneakyThrows
	public static <T> T parseObject(String text, TypeReference<T> typeReference) {
		return objectMapper.readValue(text, typeReference);
	}

	@SneakyThrows
	public static <T> List<T> parseArray(String text, Class<T> clazz) {
		if (StrUtil.isEmpty(text)) {
			return new ArrayList<>();
		}

		return objectMapper.readValue(text, objectMapper.getTypeFactory().constructCollectionType(List.class, clazz));
	}

	@SneakyThrows
	public static JsonNode parseTree(String text) {
		return objectMapper.readTree(text);
	}

}
