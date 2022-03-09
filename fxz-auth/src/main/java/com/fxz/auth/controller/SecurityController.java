package com.fxz.auth.controller;

import com.fxz.auth.service.ValidateCodeService;
import com.fxz.common.core.constant.FxzConstant;
import com.fxz.common.core.exception.FxzAuthException;
import com.fxz.common.core.exception.ValidateCodeException;
import com.fxz.common.core.result.Result;
import com.fxz.common.core.result.ResultCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.security.oauth2.provider.token.ConsumerTokenServices;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.security.Principal;

/**
 * @author Fxz
 * @version 1.0
 * @date 2021-11-27 16:28
 */
@Slf4j
@RestController
@RequiredArgsConstructor
public class SecurityController {

	private final ConsumerTokenServices consumerTokenServices;

	private final ValidateCodeService validateCodeService;

	@GetMapping("/captcha")
	public void captcha(HttpServletRequest request, HttpServletResponse response)
			throws IOException, ValidateCodeException {
		validateCodeService.create(request, response);
	}

	@GetMapping("/oauth/test")
	public String testOauth() {
		return "oauth";
	}

	@GetMapping("/user/info")
	public Principal currentUser(Principal principal) {
		return principal;
	}

	@GetMapping("/user")
	public Principal user(Principal principal) {
		return principal;
	}

	@DeleteMapping("/myLogout")
	public Result<Void> logout(HttpServletRequest request) throws FxzAuthException {
		// 获取请求头信息
		String authorization = request.getHeader("Authorization");
		// 提取token
		String token = StringUtils.replace(authorization, FxzConstant.OAUTH2_TOKEN_TYPE, "");
		log.info("token:{}", token);
		// 此处调用了redis删除缓存的登录信息
		if (!consumerTokenServices.revokeToken(token)) {
			throw new FxzAuthException("退出登录失败");
		}
		return Result.success();
	}

}
