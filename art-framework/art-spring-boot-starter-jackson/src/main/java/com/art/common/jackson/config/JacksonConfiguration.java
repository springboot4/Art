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

package com.art.common.jackson.config;

import cn.hutool.core.date.DatePattern;
import com.art.common.jackson.module.JavaLongTypeModule;
import com.art.common.jackson.module.JavaTimeModule;
import com.art.common.jackson.util.JacksonUtil;
import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.jackson.Jackson2ObjectMapperBuilderCustomizer;
import org.springframework.boot.autoconfigure.jackson.JacksonAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;

import java.time.ZoneId;
import java.util.Locale;
import java.util.TimeZone;

/**
 * @author fxz
 */
@AutoConfiguration
@ConditionalOnClass(ObjectMapper.class)
@AutoConfigureBefore(JacksonAutoConfiguration.class)
public class JacksonConfiguration {

	@Bean
	@ConditionalOnMissingBean
	public Jackson2ObjectMapperBuilderCustomizer customizer() {
		return builder -> {
			builder.locale(Locale.CHINA);
			builder.timeZone(TimeZone.getTimeZone(ZoneId.systemDefault()));
			// 时间格式
			builder.simpleDateFormat(DatePattern.NORM_DATETIME_PATTERN);
			// Long转String
			builder.serializerByType(Long.class, ToStringSerializer.instance);
			builder.modules(new JavaTimeModule());
		};
	}

	@Bean
	@Primary
	public ObjectMapper objectMapper() {
		ObjectMapper objectMapper = new ObjectMapper()
				// 指定要序列化的域
				.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.ANY)
				// 不将日期写为时间戳
				.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)
				// 忽略未知属性
				.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES)
				// 对象属性为空时可以序列化
				.disable(SerializationFeature.FAIL_ON_EMPTY_BEANS).registerModule(new JavaTimeModule())
				.registerModule(new Jdk8Module()).registerModule(new JavaLongTypeModule())
				.registerModule(new SimpleModule());
		JacksonUtil.setObjectMapper(objectMapper);
		return objectMapper;
	}

}