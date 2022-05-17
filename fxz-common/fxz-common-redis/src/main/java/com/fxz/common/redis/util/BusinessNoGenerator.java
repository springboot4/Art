package com.fxz.common.redis.util;

import com.fxz.common.core.constant.RedisConstant;
import com.fxz.common.core.enums.BusinessTypeEnum;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;

/**
 * 业务序号生成
 *
 * @author fxz
 */
@RequiredArgsConstructor
@Component
@Slf4j
public class BusinessNoGenerator {

	private final RedisTemplate redisTemplate;

	/**
	 * @param businessType 业务类型枚举
	 * @param digit 业务序号位数
	 * @return 业务序号
	 */
	public String generate(BusinessTypeEnum businessType, Integer digit) {
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");
		String date = LocalDateTime.now(ZoneOffset.of("+8")).format(formatter);

		String key = RedisConstant.BUSINESS_PREFIX + businessType.getValue() + ":" + date;

		Long increment = redisTemplate.opsForValue().increment(key);

		return date + businessType.getValue() + String.format("%0" + digit + "d", increment);
	}

	public String generate(BusinessTypeEnum businessType) {
		Integer defaultDigit = 6;
		return generate(businessType, defaultDigit);
	}

}
