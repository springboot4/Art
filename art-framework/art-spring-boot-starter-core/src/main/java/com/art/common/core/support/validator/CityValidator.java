/*
 * COPYRIGHT (C) 2023 Art AUTHORS(fxzcloud@gmail.com). ALL RIGHTS RESERVED.
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

package com.art.common.core.support.validator;

import cn.hutool.core.io.IoUtil;
import cn.hutool.json.JSONUtil;
import com.art.common.core.annotation.CheckCityValid;
import com.art.common.core.model.CityEntity;
import com.art.common.core.constant.CityTypeEnum;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.util.StringUtils;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.io.InputStream;
import java.util.List;

/**
 * @author Fxz
 * @version 0.0.1
 * @date 2022/5/14 15:32
 */
public class CityValidator implements ConstraintValidator<CheckCityValid, String> {

	private CityTypeEnum type;

	@Autowired
	private StringRedisTemplate redisTemplate;

	@Override
	public void initialize(CheckCityValid annotation) {
		this.type = annotation.value();
	}

	@SneakyThrows
	@Override
	public boolean isValid(String inputValue, ConstraintValidatorContext context) {
		if (inputValue == null) {
			return false;
		}
		String jsonStr = redisTemplate.opsForValue().get("constant:city");
		if (!StringUtils.hasText(jsonStr)) {
			ClassPathResource resource = new ClassPathResource("city.json");
			InputStream inputStream = resource.getInputStream();
			String line = IoUtil.readUtf8(inputStream);
			redisTemplate.opsForValue().set("constant:city", line);
			jsonStr = line;
		}
		List<CityEntity> cityJson = JSONUtil.toList(jsonStr, CityEntity.class);
		CityEntity entity;
		switch (type) {
			case PROVINCE: {
				entity = cityJson.stream().filter(item -> inputValue.equals(item.getName()) && item.getParent() == null)
						.findFirst().orElse(null);
				break;
			}
			default:
			case CITY: {
				entity = cityJson.stream().filter(item -> {
					// 找出名字相符且有父节点的entity
					if (inputValue.equals(item.getName()) && item.getParent() != null) {
						// 找出没有父节点的父节点
						CityEntity parentEntity = cityJson.stream().filter(
								parent -> item.getParent().equals(parent.getValue()) && parent.getParent() == null)
								.findFirst().orElse(null);
						return parentEntity != null;
					}
					return false;
				}).findFirst().orElse(null);
				break;
			}
			case AREA: {
				entity = cityJson.stream().filter(item -> {
					// 找出名字相符且有父节点的entity
					if (inputValue.equals(item.getName()) && item.getParent() != null) {
						// 找出有父节点的父节点
						CityEntity parentEntity = cityJson.stream().filter(
								parent -> item.getParent().equals(parent.getValue()) && parent.getParent() != null)
								.findFirst().orElse(null);
						return parentEntity != null;
					}
					return false;
				}).findFirst().orElse(null);
				break;
			}
		}
		return entity != null;
	}

}
