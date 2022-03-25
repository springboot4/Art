package com.fxz.auth.service;

import com.fxz.auth.properties.FxzAuthProperties;
import com.fxz.auth.properties.FxzValidateCodeProperties;
import com.fxz.common.core.constant.FxzConstant;
import com.fxz.common.core.exception.ValidateCodeException;
import com.fxz.common.redis.service.RedisService;
import com.wf.captcha.GifCaptcha;
import com.wf.captcha.SpecCaptcha;
import com.wf.captcha.base.Captcha;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 验证码服务类
 *
 * @author Fxz
 * @version 1.0
 * @date 2021-11-28 16:50
 */

@Service
@RequiredArgsConstructor
public class ValidateCodeService {

	private final RedisService redisService;

	private final FxzAuthProperties properties;

	/**
	 * 生成验证码
	 * @param request HttpServletRequest
	 * @param response HttpServletResponse
	 */
	public void create(HttpServletRequest request, HttpServletResponse response)
			throws IOException, ValidateCodeException {
		String key = request.getParameter("key");
		if (StringUtils.isBlank(key)) {
			throw new ValidateCodeException("验证码key不能为空");
		}
		FxzValidateCodeProperties code = properties.getCode();
		setHeader(response, code.getType());

		Captcha captcha = createCaptcha(code);
		redisService.set(FxzConstant.CODE_PREFIX + key, StringUtils.lowerCase(captcha.text()), code.getTime());
		captcha.out(response.getOutputStream());
	}

	/**
	 * 校验验证码
	 * @param key 前端上送 key
	 * @param value 前端上送待校验值
	 */
	public void check(String key, String value) throws ValidateCodeException {

		if (StringUtils.isBlank(value)) {
			throw new ValidateCodeException("请输入验证码");
		}

		Object codeInRedis = redisService.get(FxzConstant.CODE_PREFIX + key);

		if (codeInRedis == null) {
			throw new ValidateCodeException("验证码已过期");
		}
		if (!StringUtils.equalsIgnoreCase(value, String.valueOf(codeInRedis))) {
			throw new ValidateCodeException("验证码不正确");
		}

	}

	private Captcha createCaptcha(FxzValidateCodeProperties code) {
		Captcha captcha = null;
		if (StringUtils.equalsIgnoreCase(code.getType(), FxzConstant.GIF)) {
			captcha = new GifCaptcha(code.getWidth(), code.getHeight(), code.getLength());
		}
		else {
			captcha = new SpecCaptcha(code.getWidth(), code.getHeight(), code.getLength());
		}
		captcha.setCharType(code.getCharType());
		return captcha;
	}

	private void setHeader(HttpServletResponse response, String type) {
		if (StringUtils.equalsIgnoreCase(type, FxzConstant.GIF)) {
			response.setContentType(MediaType.IMAGE_GIF_VALUE);
		}
		else {
			response.setContentType(MediaType.IMAGE_PNG_VALUE);
		}
		response.setHeader(HttpHeaders.PRAGMA, "No-cache");
		response.setHeader(HttpHeaders.CACHE_CONTROL, "no-cache");
		response.setDateHeader(HttpHeaders.EXPIRES, 0L);
	}

}
