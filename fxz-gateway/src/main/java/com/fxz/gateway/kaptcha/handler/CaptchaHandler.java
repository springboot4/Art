package com.fxz.gateway.kaptcha.handler;

import cn.hutool.core.codec.Base64;
import cn.hutool.core.util.IdUtil;
import com.fxz.common.core.constant.FxzConstant;
import com.fxz.common.mp.result.Result;
import com.fxz.common.redis.service.RedisService;
import com.google.code.kaptcha.Producer;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.util.FastByteArrayOutputStream;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.server.HandlerFunction;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.HashMap;

/**
 * 生成验证码缓存并返回前端
 *
 * @author fxz
 */
@SuppressWarnings("all")
@Component
@RequiredArgsConstructor
public class CaptchaHandler implements HandlerFunction<ServerResponse> {

	private final Producer producer;

	private final RedisService redisService;

	@Override
	public Mono<ServerResponse> handle(ServerRequest serverRequest) {
		// 生成验证码
		String capText = producer.createText();
		String capStr = capText.substring(0, capText.lastIndexOf("@"));
		String code = capText.substring(capText.lastIndexOf("@") + 1);
		BufferedImage image = producer.createImage(capStr);
		// 缓存验证码至Redis
		String uuid = IdUtil.simpleUUID();
		redisService.set(FxzConstant.CODE_PREFIX + uuid, code, 20L);
		// 转换流信息写出
		FastByteArrayOutputStream os = new FastByteArrayOutputStream();
		try {
			ImageIO.write(image, "jpg", os);
		}
		catch (IOException e) {
			return Mono.error(e);
		}

		java.util.Map resultMap = new HashMap<String, String>();
		resultMap.put("uuid", uuid);
		resultMap.put("img", Base64.encode(os.toByteArray()));

		return ServerResponse.status(HttpStatus.OK).body(BodyInserters.fromValue(Result.success(resultMap)));
	}

}
