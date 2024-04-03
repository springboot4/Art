/*
 *   COPYRIGHT (C) 2023 Art AUTHORS(fxzcloud@gmail.com). ALL RIGHTS RESERVED.
 *
 *   Licensed under the Apache License, Version 2.0 (the "License");
 *   you may not use this file except in compliance with the License.
 *   You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 *   Unless required by applicable law or agreed to in writing, software
 *   distributed under the License is distributed on an "AS IS" BASIS,
 *   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *   See the License for the specific language governing permissions and
 *   limitations under the License.
 */

package com.art.common.security.client.http;

import cn.hutool.core.text.StrPool;
import com.art.common.security.client.GiteeRegisteredClientRepository;
import com.art.common.security.client.core.constants.GiteeEndpointConstants;
import com.art.common.security.client.core.constants.GiteeScopeEnums;
import com.art.common.security.core.model.ArtAuthUser;
import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.oauth2.core.endpoint.OAuth2ParameterNames;
import org.springframework.security.oauth2.server.authorization.OAuth2AuthorizationService;
import org.springframework.security.oauth2.server.authorization.OAuth2TokenType;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.security.Principal;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;

/**
 * 拦截我们前端的gitee code请求
 *
 * @author fxz
 * @version 0.0.1
 * @date 2023/4/15 14:38
 */
@Slf4j
@RequiredArgsConstructor
public class GiteeAuthorizeHttpFilter implements Filter {

	private final GiteeRegisteredClientRepository giteeRegisteredClientRepository;

	private final OAuth2AuthorizationService authorizationService;

	public static final String PREFIX_URL = "/gitee/authorize";

	/**
	 * 拦截前端gitee授权请求
	 * @param servletRequest 请求
	 * @param servletResponse 响应
	 * @param filterChain 过滤器链
	 */
	@Override
	public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain)
			throws IOException, ServletException {
		HttpServletRequest request = (HttpServletRequest) servletRequest;
		HttpServletResponse response = (HttpServletResponse) servletResponse;
		String uri = request.getRequestURI();

		// 码云配置的appid
		String appid = uri.replace(PREFIX_URL + StrPool.SLASH, "");
		// 重定向地址
		String redirectUri = giteeRegisteredClientRepository.getRedirectUriByAppid(appid);
		// 授权的scope
		String scope = request.getParameter(OAuth2ParameterNames.SCOPE);
		// accessToken
		String accessToken = request.getParameter(OAuth2ParameterNames.ACCESS_TOKEN);

		String scopeRes;
		if (Objects.isNull(scope)) {
			scopeRes = GiteeScopeEnums.USER_INFO.getValue();
		}
		else {
			List<String> scopes = Arrays.asList(StringUtils.delimitedListToStringArray(scope, StrPool.COMMA));
			scopes.retainAll(GiteeScopeEnums.legalScope());
			scopeRes = String.join(StrPool.COMMA, new HashSet<>(scopes));
		}

		// 生成一个状态码
		String state = giteeRegisteredClientRepository.stateGenerate(appid);

		if (StringUtils.hasText(accessToken)) {
			ArtAuthUser user = (ArtAuthUser) ((UsernamePasswordAuthenticationToken) authorizationService
				.findByToken(accessToken, OAuth2TokenType.ACCESS_TOKEN)
				.getAttributes()
				.get(Principal.class.getName())).getPrincipal();
			if (Objects.nonNull(user)) {
				// 标记操作用户
				giteeRegisteredClientRepository.tagUsers(appid, state, user.getUserId());
			}
		}

		String url = String.format(GiteeEndpointConstants.AUTHORIZE_URL, appid, redirectUri, scopeRes, state);
		response.sendRedirect(url);
	}

}
