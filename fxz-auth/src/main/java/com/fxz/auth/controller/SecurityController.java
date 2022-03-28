package com.fxz.auth.controller;

import com.fxz.common.core.constant.FxzConstant;
import com.fxz.common.core.exception.FxzAuthException;
import com.fxz.common.mp.result.Result;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.security.oauth2.provider.token.ConsumerTokenServices;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
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
