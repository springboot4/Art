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

package com.art.gateway.handler;

import cn.hutool.core.util.IdUtil;
import com.art.common.core.constant.FxzConstant;
import com.art.common.core.model.Result;
import com.art.gateway.component.EasyCaptchaProducer;
import com.art.common.core.constant.CodeTypeEnum;
import com.wf.captcha.base.Captcha;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.server.HandlerFunction;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * 验证码处理器
 *
 * @author fxz
 */
@Component
@RequiredArgsConstructor
public class EasyCaptchaHandler implements HandlerFunction<ServerResponse> {

	private final EasyCaptchaProducer easyCaptchaProducer;

	private final StringRedisTemplate stringRedisTemplate;

	@Override
	public Mono<ServerResponse> handle(ServerRequest request) {

		CodeTypeEnum codeTypeEnum = CodeTypeEnum.ARITHMETIC;
		Captcha captcha = easyCaptchaProducer.getCaptcha(codeTypeEnum);
		String captchaValue = captcha.text();

		if (captcha.getCharType() - 1 == CodeTypeEnum.ARITHMETIC.ordinal() && captchaValue.contains(".")) {
			captchaValue = captchaValue.split("\\.")[0];
		}

		// 缓存验证码至Redis
		String uuid = IdUtil.simpleUUID();
		stringRedisTemplate.opsForValue().set(FxzConstant.CODE_PREFIX + uuid, captchaValue, 60, TimeUnit.SECONDS);

		// 获取到验证码Base64编码字符串
		String captchaBase64 = captcha.toBase64();
		Map<String, String> result = new HashMap<>(2);
		result.put("uuid", uuid);
		result.put("img", captchaBase64);

		return ServerResponse.ok().body(BodyInserters.fromValue(Result.success(result)));
	}

}
