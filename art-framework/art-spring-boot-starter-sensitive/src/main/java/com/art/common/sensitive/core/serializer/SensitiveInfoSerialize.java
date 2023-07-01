/*
 *   COPYRIGHT (C) 2023 Art AUTHORS(fxzcloud@gmail.com). ALL RIGHTS RESERVED.
 *
 *   Licensed under the Apache License, Version 2.0 (the "License");
 *   you may not use this file except in compliance with the License.
 *   You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 *   Unless required by applicable law or agreed to in writing, software
 *   distributed under the License is distributed on an "AS IS" BASIS,
 *   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *   See the License for the specific language governing permissions and
 *   limitations under the License.
 */

package com.art.common.sensitive.core.serializer;

import cn.hutool.core.annotation.AnnotationUtil;
import cn.hutool.core.lang.Singleton;
import cn.hutool.core.util.ArrayUtil;
import cn.hutool.core.util.ReflectUtil;
import cn.hutool.core.util.StrUtil;
import com.art.common.sensitive.core.annotation.base.SensitiveInfo;
import com.art.common.sensitive.core.handler.base.SensitiveHandler;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.BeanProperty;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.ContextualSerializer;
import lombok.Getter;
import lombok.Setter;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.Objects;

/**
 * 敏感信息脱敏序列化
 *
 * @author fxz
 * @date 2022/4/25
 */
@Getter
@Setter
@SuppressWarnings({ "rawtypes", "unchecked" })
public class SensitiveInfoSerialize extends JsonSerializer<String> implements ContextualSerializer {

	private SensitiveHandler sensitiveHandler;

	@Override
	public void serialize(String s, JsonGenerator jsonGenerator, SerializerProvider serializerProvider)
			throws IOException {
		if (StrUtil.isBlank(s)) {
			jsonGenerator.writeNull();
			return;
		}

		// 获取序列化字段
		String currentName = jsonGenerator.getOutputContext().getCurrentName();
		Object currentValue = jsonGenerator.getCurrentValue();
		Class<?> currentValueClass = currentValue.getClass();
		Field field = ReflectUtil.getField(currentValueClass, currentName);

		SensitiveInfo[] annotations = AnnotationUtil.getCombinationAnnotations(field, SensitiveInfo.class);
		if (ArrayUtil.isEmpty(annotations)) {
			jsonGenerator.writeString(s);
			return;
		}

		for (Annotation annotation : field.getAnnotations()) {
			if (AnnotationUtil.hasAnnotation(annotation.annotationType(), SensitiveInfo.class)) {
				s = this.sensitiveHandler.desensitize(s, annotation);
				jsonGenerator.writeString(s);
				return;
			}
		}

		jsonGenerator.writeString(s);
	}

	@Override
	public JsonSerializer<?> createContextual(SerializerProvider serializerProvider, BeanProperty beanProperty)
			throws JsonMappingException {
		// 为空直接跳过
		if (Objects.isNull(beanProperty)) {
			return serializerProvider.findNullValueSerializer(null);
		}

		// 非String跳过
		if (!Objects.equals(beanProperty.getType().getRawClass(), String.class)) {
			return serializerProvider.findValueSerializer(beanProperty.getType(), beanProperty);
		}

		SensitiveInfo sensitiveInfo = beanProperty.getAnnotation(SensitiveInfo.class);
		if (Objects.isNull(sensitiveInfo)) {
			sensitiveInfo = beanProperty.getContextAnnotation(SensitiveInfo.class);
		}
		if (Objects.isNull(sensitiveInfo)) {
			return serializerProvider.findValueSerializer(beanProperty.getType(), beanProperty);
		}

		SensitiveInfoSerialize sensitiveInfoSerialize = new SensitiveInfoSerialize();
		sensitiveInfoSerialize.setSensitiveHandler(Singleton.get(sensitiveInfo.handler()));
		return sensitiveInfoSerialize;
	}

}
