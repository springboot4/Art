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

	@Primary
	@Bean
	public Jackson2ObjectMapperBuilderCustomizer customizer() {
		return builder -> {
			builder.locale(Locale.CHINA)
				.timeZone(TimeZone.getTimeZone(ZoneId.systemDefault()))
				.simpleDateFormat(DatePattern.NORM_DATETIME_PATTERN)
				.modules(new JavaTimeModule(), new Jdk8Module(), new JavaLongTypeModule(), new SimpleModule())
				// Long转String
				.serializerByType(Long.class, ToStringSerializer.instance)
				// 序列化和反序列化对象中的所有字段并忽略它们的可见性修饰符
				.visibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.ANY)
				// featuresToDisable
				.featuresToDisable(
						// 不将日期写为时间戳
						SerializationFeature.WRITE_DATES_AS_TIMESTAMPS,
						// 忽略未知属性
						DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES,
						// 对象属性为空时可以序列化
						SerializationFeature.FAIL_ON_EMPTY_BEANS);
		};
	}

}