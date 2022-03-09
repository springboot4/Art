package com.fxz.auth.filter;

import com.fxz.auth.service.ValidateCodeService;
import com.fxz.common.core.exception.ValidateCodeException;
import com.fxz.common.core.result.Result;
import com.fxz.common.core.utils.FxzUtil;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

/**
 * @author Fxz
 * @version 1.0
 * @Deprecated 我们不再使用添加过滤器在UsernamePasswordAuthenticationFilter前来达到校验验证码的效果
 * 而是拓展了Oauth的Granter来实现验证码授权模式
 * <p>
 * ValidateCodeFilter继承Spring Boot提供的OncePerRequestFilter，该过滤器实现了javax.servlet.filter接口，
 * 顾名思义，它可以确保我们的逻辑只被执行一次：
 * @date 2021-11-28 17:11
 */
@Slf4j
// @Component
@RequiredArgsConstructor
public class ValidateCodeFilter extends OncePerRequestFilter {

	private final ValidateCodeService validateCodeService;

	@SneakyThrows
	@Override
	protected void doFilterInternal(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse,
			FilterChain filterChain) {
		String header = httpServletRequest.getHeader("Authorization");
		String clientId = getClientId(header, httpServletRequest);

		RequestMatcher matcher = new AntPathRequestMatcher("/oauth/token", HttpMethod.POST.toString());
		if (matcher.matches(httpServletRequest)
				&& StringUtils.equalsIgnoreCase(httpServletRequest.getParameter("grant_type"), "password")
				&& !StringUtils.equalsAnyIgnoreCase(clientId, "swagger")) {
			try {
				validateCode(httpServletRequest);
				filterChain.doFilter(httpServletRequest, httpServletResponse);
			}
			catch (ValidateCodeException e) {
				FxzUtil.makeResponse(httpServletResponse, MediaType.APPLICATION_JSON_VALUE,
						HttpServletResponse.SC_INTERNAL_SERVER_ERROR, Result.failed(e.getMessage()));
				log.error(e.getMessage(), e);
			}
		}
		else {
			filterChain.doFilter(httpServletRequest, httpServletResponse);
		}
	}

	private void validateCode(HttpServletRequest httpServletRequest) throws ValidateCodeException {
		String code = httpServletRequest.getParameter("code");
		String key = httpServletRequest.getParameter("key");
		validateCodeService.check(key, code);
	}

	private String getClientId(String header, HttpServletRequest request) {
		String clientId = "";
		try {
			byte[] base64Token = header.substring(6).getBytes(StandardCharsets.UTF_8);
			byte[] decoded;
			decoded = Base64.getDecoder().decode(base64Token);

			String token = new String(decoded, StandardCharsets.UTF_8);
			int delim = token.indexOf(":");
			if (delim != -1) {
				clientId = new String[] { token.substring(0, delim), token.substring(delim + 1) }[0];
			}
		}
		catch (Exception ignore) {
		}
		return clientId;
	}

}
