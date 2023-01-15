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

package com.art.common.redis.core.util;

import cn.hutool.core.date.DatePattern;
import com.art.common.core.constant.RedisConstant;
import com.art.common.core.enums.BusinessTypeEnum;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;

import java.time.LocalDateTime;
import java.time.ZoneOffset;

/**
 * 业务序号生成
 * <p/>
 * 已弃用 使用art-spring-boot-starter-sequence
 *
 * @author fxz
 */
@Deprecated
@SuppressWarnings("all")
@RequiredArgsConstructor
@Slf4j
public class BusinessNoGenerator {

	private final RedisTemplate redisTemplate;

	/**
	 * @param businessType 业务类型枚举
	 * @param digit 业务序号位数
	 * @return 业务序号
	 */
	public String generate(BusinessTypeEnum businessType, Integer digit) {
		String date = LocalDateTime.now(ZoneOffset.of("+8")).format(DatePattern.PURE_DATETIME_FORMATTER);

		String key = RedisConstant.BUSINESS_PREFIX + businessType.getValue() + ":" + date;

		Long increment = redisTemplate.opsForValue().increment(key);

		return date + businessType.getValue() + String.format("%0" + digit + "d", increment);
	}

	public String generate(BusinessTypeEnum businessType) {
		Integer defaultDigit = 6;
		return generate(businessType, defaultDigit);
	}

}
