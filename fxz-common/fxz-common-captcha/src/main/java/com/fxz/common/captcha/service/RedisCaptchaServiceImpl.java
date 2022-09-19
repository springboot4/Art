package com.fxz.common.captcha.service;

import com.anji.captcha.service.CaptchaCacheService;
import com.fxz.common.captcha.constant.CaptchaConstants;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;

import javax.annotation.Resource;
import java.util.concurrent.TimeUnit;

/**
 * 对于分布式部署的应用，我们建议应用自己实现CaptchaCacheService 如果应用是单点的，也没有使用redis，那默认使用内存。
 * 内存缓存只适合单节点部署的应用，否则验证码生产与验证在节点之间信息不同步，导致失败。
 *
 * @author Fxz
 * @version 1.0
 * @date 2022/9/19 16:12
 */
@NoArgsConstructor
@AllArgsConstructor
public class RedisCaptchaServiceImpl implements CaptchaCacheService {

	@Resource
	private StringRedisTemplate stringRedisTemplate;

	@Override
	public String type() {
		return CaptchaConstants.REDIS;
	}

	@Override
	public void set(String key, String value, long expiresInSeconds) {
		stringRedisTemplate.opsForValue().set(key, value, expiresInSeconds, TimeUnit.SECONDS);
	}

	@Override
	public boolean exists(String key) {
		return Boolean.TRUE.equals(stringRedisTemplate.hasKey(key));
	}

	@Override
	public void delete(String key) {
		stringRedisTemplate.delete(key);
	}

	@Override
	public String get(String key) {
		return stringRedisTemplate.opsForValue().get(key);
	}

	@Override
	public Long increment(String key, long val) {
		return stringRedisTemplate.opsForValue().increment(key, val);
	}

}
