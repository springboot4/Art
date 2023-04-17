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

package com.art.common.security.client;

import cn.hutool.core.text.StrPool;
import com.art.common.core.constant.ResultCode;
import com.art.common.core.exception.FxzException;
import com.art.common.core.model.ResultOpt;
import com.art.common.security.authentication.gitee.GiteeTokenResponse;
import com.art.common.security.authentication.gitee.GiteeUserInfoResponse;
import com.art.common.security.client.exception.AppidGiteeException;
import com.art.common.security.client.exception.RedirectUriGiteeException;
import com.art.common.security.client.properties.GiteeProperties;
import com.art.common.security.core.model.ArtAuthUser;
import com.art.common.security.core.utils.ArtOAuth2EndpointUtils;
import com.art.system.api.gitee.UserGiteeServiceApi;
import com.art.system.api.gitee.dto.UsersGiteeDTO;
import com.art.system.api.user.dto.SystemUserDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.OAuth2Error;
import org.springframework.security.oauth2.core.OAuth2ErrorCodes;
import org.springframework.security.oauth2.core.endpoint.OAuth2AccessTokenResponse;
import org.springframework.security.oauth2.core.endpoint.OAuth2ParameterNames;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.TimeUnit;

import static com.art.common.security.client.core.constants.GiteeEndpointEnums.USERINFO_URL;

/**
 * @author fxz
 * @version 0.0.1
 * @date 2023/4/16 11:44
 */
@Slf4j
@RequiredArgsConstructor
public class DefaultGiteeRegisteredClientRepository implements GiteeRegisteredClientRepository {

	private final GiteeProperties giteeProperties;

	private final StringRedisTemplate redisTemplate;

	private final UserGiteeServiceApi userGiteeServiceApi;

	private final static String GITEE_STATE_PREFIX = "gitee_state_prefix";

	private final static String GITEE_BINDING_PREFIX = "gitee_binding_prefix";

	private final static String GITEE_USERS_PREFIX = "gitee_users_prefix";

	/**
	 * @param appid 码云配置的客户端Id
	 * @return 重定向地址
	 */
	@Override
	public String getRedirectUriByAppid(String appid) {
		Assert.hasText(appid, "appid不能为空！");
		GiteeProperties.Gitee gitee = getGiteeByAppid(appid);
		String redirectUriPrefix = gitee.getRedirectUriPrefix();

		if (StringUtils.hasText(redirectUriPrefix)) {
			return UriUtils.encode(redirectUriPrefix + "/" + appid, StandardCharsets.UTF_8);
		}
		else {
			OAuth2Error error = new OAuth2Error(ResultCode.SYSTEM_EXECUTION_ERROR.getCode(), "重定向地址前缀不能为空", null);
			throw new RedirectUriGiteeException(error);
		}
	}

	/**
	 * 生成状态码
	 * @param appid 开放平台 网站应用 ID
	 * @return 状态码
	 */
	@Override
	public String stateGenerate(String appid) {
		String state = UUID.randomUUID().toString();
		redisTemplate.opsForValue()
			.set(GITEE_STATE_PREFIX + StrPool.COLON + appid + StrPool.COLON + state, state, 30, TimeUnit.MINUTES);
		return state;
	}

	/**
	 * @param appid 开放平台 网站应用 ID
	 * @param id 码云用户唯一标识
	 * @param userId 系统用户id
	 */
	@Override
	public void storeBinding(Integer id, String appid, Long userId) {
		UsersGiteeDTO usersGiteeDTO = new UsersGiteeDTO();
		usersGiteeDTO.setAppid(appid);
		usersGiteeDTO.setId(id);
		usersGiteeDTO.setUsersId(userId);
		userGiteeServiceApi.binding(usersGiteeDTO);
	}

	/**
	 * 储存gitee用户
	 * @param giteeTokenResponse gitee token响应
	 * @param userInfo gitee 用户信息响应
	 * @param appid 码云网站应用ID
	 */
	@Override
	public void storeGiteeUsers(GiteeTokenResponse giteeTokenResponse, GiteeUserInfoResponse userInfo, String appid) {
		String accessToken = giteeTokenResponse.getAccessToken();
		String refreshToken = giteeTokenResponse.getRefreshToken();
		String scope = giteeTokenResponse.getScope();
		Integer expiresIn = giteeTokenResponse.getExpiresIn();
		LocalDateTime expires = LocalDateTime.now().plusSeconds(expiresIn);
		Integer id = userInfo.getId();
		String avatarUrl = userInfo.getAvatarUrl();

		UsersGiteeDTO usersGitee = ResultOpt.ofNullable(userGiteeServiceApi.getByAppidAndId(appid, id))
			.assertSuccess(r -> new FxzException("查询接口失败"))
			.peek(d -> log.info("usersGitee:{}", d))
			.getData();

		if (Objects.isNull(usersGitee)) {
			UsersGiteeDTO gitee = new UsersGiteeDTO();
			BeanUtils.copyProperties(userInfo, gitee);
			gitee.setAppid(appid);
			gitee.setAccessToken(accessToken);
			gitee.setRefreshToken(refreshToken);
			gitee.setExpires(expires);
			gitee.setScope(scope);
			userGiteeServiceApi.add(gitee);
		}
		else {
			usersGitee.setAccessToken(accessToken);
			usersGitee.setRefreshToken(refreshToken);
			usersGitee.setExpires(expires);
			usersGitee.setScope(scope);
			usersGitee.setAvatarUrl(avatarUrl);
			userGiteeServiceApi.update(usersGitee);
		}
	}

	/**
	 * 检验状态码
	 * @param appid 码云应用 ID
	 * @param code 授权码
	 * @param state 状态码
	 * @return true or false
	 */
	@Override
	public boolean stateValid(String appid, String code, String state) {
		Assert.hasText(state, "状态码不能为空！");
		Assert.hasText(code, "授权码不能为空！");

		String cacheState = redisTemplate.opsForValue()
			.get(GITEE_STATE_PREFIX + StrPool.COLON + appid + StrPool.COLON + state);
		Assert.hasText(cacheState, "状态码校验失效！");

		assert cacheState != null;
		return cacheState.equals(state);
	}

	/**
	 * @param request 请求
	 * @param response 响应
	 * @param appid 开放平台 网站应用 ID
	 * @param code 授权码
	 * @param state 状态码
	 * @return
	 */
	@Override
	public String getBinding(HttpServletRequest request, HttpServletResponse response, String appid, String code,
			String state) {
		return null;
	}

	/**
	 * @param appid 码云配置的客户端id
	 * @return GiteeProperties.Gitee
	 */
	@Override
	public GiteeProperties.Gitee getGiteeByAppid(String appid) throws OAuth2AuthenticationException {
		Assert.hasText(appid, "appid不能为空！");

		List<GiteeProperties.Gitee> clients = giteeProperties.getList();
		if (CollectionUtils.isEmpty(clients)) {
			return null;
		}

		Optional<GiteeProperties.Gitee> optional = clients.stream().filter(c -> appid.equals(c.getAppid())).findFirst();
		if (optional.isPresent()) {
			return optional.get();
		}
		else {
			OAuth2Error error = new OAuth2Error(ResultCode.SYSTEM_EXECUTION_ERROR.getCode(), "为配置的appid", null);
			throw new AppidGiteeException(error);
		}
	}

	/**
	 * @param clientPrincipal 经过身份验证的客户端主体
	 * @param additionalParameters 附加参数
	 * @param details 登录信息
	 * @param appid AppID(码云Gitee client_id)
	 * @param code 授权码，<a href="https://gitee.com/api/v5/oauth_doc">OAuth文档</a>
	 * @param id 用户唯一标识，<a href="https://gitee.com/api/v5/oauth_doc">OAuth文档</a>
	 * @param credentials 证书
	 * @param login 多账户用户唯一标识，<a href="https://gitee.com/api/v5/oauth_doc">OAuth文档</a>
	 * @param accessToken 授权凭证，<a href="https://gitee.com/api/v5/oauth_doc">OAuth文档</a>
	 * @param refreshToken 刷新凭证，<a href="https://gitee.com/api/v5/oauth_doc">OAuth文档</a>
	 * @param expiresIn 过期时间，<a href="https://gitee.com/api/v5/oauth_doc">OAuth文档</a>
	 * @param scope {@link OAuth2ParameterNames#SCOPE}，授权范围，<a href=
	 * "https://gitee.com/api/v5/oauth_doc">OAuth文档</a>
	 * @return
	 * @throws OAuth2AuthenticationException
	 */
	@Override
	public AbstractAuthenticationToken authenticationToken(Authentication clientPrincipal,
			Map<String, Object> additionalParameters, Object details, String appid, String code, Integer id,
			Object credentials, String login, String accessToken, String refreshToken, Integer expiresIn, String scope)
			throws OAuth2AuthenticationException {
		return null;
	}

	/**
	 * @param request 请求
	 * @param response 响应
	 * @param tokenUrlPrefix 获取 Token URL 前缀
	 * @param tokenUrl Token URL
	 * @param uriVariables 参数
	 * @return
	 * @throws OAuth2AuthenticationException
	 */
	@Override
	public OAuth2AccessTokenResponse getOAuth2AccessTokenResponse(HttpServletRequest request,
			HttpServletResponse response, String tokenUrlPrefix, String tokenUrl, Map<String, String> uriVariables)
			throws OAuth2AuthenticationException {
		return null;
	}

	/**
	 * @param appid AppID(码云Gitee
	 * client_id)，<a href="https://gitee.com/api/v5/oauth_doc">OAuth文档</a>
	 * @param code 授权码，<a href="https://gitee.com/api/v5/oauth_doc">OAuth文档</a>
	 * @param accessTokenUrl <a href="https://gitee.com/api/v5/oauth_doc">OAuth文档</a>
	 * @return
	 * @throws OAuth2AuthenticationException
	 */
	@Override
	public GiteeTokenResponse getAccessTokenResponse(String appid, String code, String accessTokenUrl)
			throws OAuth2AuthenticationException {
		GiteeProperties.Gitee gitee = getGiteeByAppid(appid);
		Map<String, String> uriVariables = new HashMap<>(8);

		uriVariables.put(OAuth2ParameterNames.CLIENT_ID, appid);
		uriVariables.put(OAuth2ParameterNames.CODE, code);
		uriVariables.put(OAuth2ParameterNames.CLIENT_SECRET, gitee.getSecret());
		uriVariables.put(OAuth2ParameterNames.REDIRECT_URI, gitee.getRedirectUriPrefix() + "/" + appid);

		RestTemplate restTemplate = new RestTemplate();
		HttpHeaders httpHeaders = new HttpHeaders();
		httpHeaders.setContentType(MediaType.APPLICATION_JSON);

		// 将请求体放入HttpEntity中
		HttpEntity<Map<String, Object>> httpEntity = new HttpEntity<>(httpHeaders);

		GiteeTokenResponse giteeTokenResponse = null;
		try {
			giteeTokenResponse = restTemplate.postForObject(accessTokenUrl, httpEntity, GiteeTokenResponse.class,
					uriVariables);
		}
		catch (Exception e) {
			ArtOAuth2EndpointUtils.throwError(OAuth2ErrorCodes.INVALID_REQUEST, OAuth2ErrorCodes.INVALID_CLIENT,
					ArtOAuth2EndpointUtils.ACCESS_TOKEN_REQUEST_ERROR_URI);
		}
		if (Objects.isNull(giteeTokenResponse)) {
			ArtOAuth2EndpointUtils.throwError(OAuth2ErrorCodes.INVALID_REQUEST, OAuth2ErrorCodes.INVALID_CLIENT,
					ArtOAuth2EndpointUtils.ACCESS_TOKEN_REQUEST_ERROR_URI);
		}

		if (giteeTokenResponse.getAccessToken() == null) {
			ArtOAuth2EndpointUtils.throwError(OAuth2ErrorCodes.INVALID_REQUEST, OAuth2ErrorCodes.INVALID_CLIENT,
					ArtOAuth2EndpointUtils.ACCESS_TOKEN_REQUEST_ERROR_URI);
		}

		return giteeTokenResponse;
	}

	/**
	 * @param userinfoUrl 用户信息接口
	 * @param appid AppID(码云Gitee client_id)
	 * @param state 状态码
	 * @param giteeTokenResponse 码云 Token
	 */
	@Override
	public GiteeUserInfoResponse getUserInfo(String userinfoUrl, String appid, String state,
			GiteeTokenResponse giteeTokenResponse) {
		RestTemplate restTemplate = new RestTemplate();
		String accessToken = giteeTokenResponse.getAccessToken();

		GiteeUserInfoResponse giteeUserInfoResponse = null;
		try {
			giteeUserInfoResponse = restTemplate.getForObject(userinfoUrl + "?access_token=" + accessToken,
					GiteeUserInfoResponse.class);
		}
		catch (Exception e) {
			ArtOAuth2EndpointUtils.throwError(OAuth2ErrorCodes.INVALID_REQUEST, "", USERINFO_URL);
		}

		if (giteeUserInfoResponse == null) {
			ArtOAuth2EndpointUtils.throwError(OAuth2ErrorCodes.INVALID_REQUEST, "", USERINFO_URL);
		}

		Integer id = giteeUserInfoResponse.getId();
		if (id == null) {
			ArtOAuth2EndpointUtils.throwError(OAuth2ErrorCodes.INVALID_REQUEST, "", USERINFO_URL);
		}

		return giteeUserInfoResponse;
	}

	/**
	 * @param request 请求
	 * @param response 响应
	 * @param uriVariables 参数
	 * @param oauth2AccessTokenResponse OAuth2.1 授权 Token
	 * @param gitee 码云Gitee配置
	 * @throws OAuth2AuthenticationException
	 */
	@Override
	public void sendRedirect(HttpServletRequest request, HttpServletResponse response, Map<String, String> uriVariables,
			OAuth2AccessTokenResponse oauth2AccessTokenResponse, GiteeProperties.Gitee gitee)
			throws OAuth2AuthenticationException {

	}

	/**
	 * 获取登录系统的用户信息
	 * @param appid 码云应用id
	 * @param id 码云用户唯一标识
	 * @return 系统用户信息
	 */
	@Override
	public UserDetails getUser(String appid, Integer id) {
		SystemUserDTO userDTO = ResultOpt.ofNullable(userGiteeServiceApi.getUser(appid, id))
			.assertSuccess(r -> new FxzException("查询接口失败"))
			.peek(u -> log.info("user:{}", u))
			.getData();

		if (Objects.isNull(userDTO)) {
			// todo 未绑定用户，将以游客角色登录
			OAuth2Error error = new OAuth2Error(ResultCode.SYSTEM_EXECUTION_ERROR.getCode(), "用户未绑定，请先绑定用户！", null);
			throw new OAuth2AuthenticationException(error);
		}

		boolean notLocked = org.apache.commons.lang3.StringUtils.equals(SystemUserDTO.STATUS_VALID,
				userDTO.getStatus());

		ArtAuthUser authUser = new ArtAuthUser(userDTO.getUsername(), userDTO.getPassword(), true, true, true,
				notLocked, AuthorityUtils.commaSeparatedStringToAuthorityList(userDTO.getPerms()));
		BeanUtils.copyProperties(userDTO, authUser);

		return authUser;
	}

	public void tagUsers(String appid, String state, Long userId) {
		redisTemplate.opsForValue()
			.set(GITEE_USERS_PREFIX + StrPool.COLON + appid + StrPool.COLON + state, String.valueOf(userId), 30,
					TimeUnit.MINUTES);
	}

	public String getTagUser(String appid, String state) {
		return redisTemplate.opsForValue().get(GITEE_USERS_PREFIX + StrPool.COLON + appid + StrPool.COLON + state);
	}

}
