package com.fxz.common.core.serializer;

import cn.hutool.core.util.StrUtil;
import cn.hutool.extra.spring.SpringUtil;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.BeanProperty;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.ContextualSerializer;
import com.fxz.common.core.constant.RegexpConstant;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.List;
import java.util.Objects;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * @author Fxz
 * @version 1.0
 * @date 2022/5/29 20:44
 */
@SuppressWarnings("all")
@Component
@AllArgsConstructor
@NoArgsConstructor
public class ImgUrlSerialize<T> extends JsonSerializer<T> implements ContextualSerializer {

	@Value("${biz.oss.resources-url}")
	private String imgDomain;

	@Override
	public void serialize(T value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
		if (Objects.isNull(value)) {
			gen.writeString(StrUtil.EMPTY);
		}
		else if (value instanceof List) {
			writeList((List) value, gen);
		}
		else {
			writeString(value.toString(), gen);
		}
	}

	private void writeList(List value, JsonGenerator gen) throws IOException {
		if (CollectionUtils.isEmpty(value)) {
			gen.writeArray(new String[] {}, 0, 0);
		}

		List<String> collect = (List<String>) value.stream().map(item -> buildString(item.toString()))
				.collect(Collectors.toList());
		gen.writeArray(collect.toArray(new String[0]), 0, collect.size());
	}

	private void writeString(String value, JsonGenerator gen) throws IOException {
		gen.writeString(buildString(value));
	}

	public String buildString(String value) {
		if (StrUtil.isBlank(value)) {
			return StrUtil.EMPTY;
		}

		StringBuilder sb = new StringBuilder();

		Pattern pattern = Pattern.compile(RegexpConstant.HTTP_PROTOCOL_REGEXP);
		boolean res = pattern.matcher(value).find();

		// 图片为http协议开头，直接返回
		if (res) {
			sb.append(value);
		}
		else {
			sb.append(imgDomain).append(value);
		}
		return sb.toString();
	}

	@Override
	public JsonSerializer<?> createContextual(SerializerProvider serializerProvider, BeanProperty beanProperty)
			throws JsonMappingException {
		if (Objects.nonNull(beanProperty)) {
			if (Objects.equals(beanProperty.getType().getRawClass(), String.class)
					|| Objects.equals(beanProperty.getType().getRawClass(), List.class)) {
				ImgUrl imgUrl = beanProperty.getAnnotation(ImgUrl.class);
				if (Objects.isNull(imgUrl)) {
					imgUrl = beanProperty.getContextAnnotation(ImgUrl.class);
				}
				if (Objects.nonNull(imgUrl)) {
					return SpringUtil.getBean(ImgUrlSerialize.class);
				}
			}
			return serializerProvider.findValueSerializer(beanProperty.getType(), beanProperty);
		}
		return serializerProvider.findNullValueSerializer(null);
	}

}
