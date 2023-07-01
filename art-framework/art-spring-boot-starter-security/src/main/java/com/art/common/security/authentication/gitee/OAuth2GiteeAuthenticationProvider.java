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

package com.art.common.security.authentication.gitee;

import com.art.common.security.client.GiteeRegisteredClientRepository;
import com.art.common.security.core.model.ArtAuthUser;
import com.art.common.security.core.utils.ArtOAuth2ConfigurerUtils;
import com.art.common.security.core.utils.ArtOAuth2EndpointUtils;
import lombok.Setter;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.*;
import org.springframework.security.oauth2.core.endpoint.OAuth2ParameterNames;
import org.springframework.security.oauth2.server.authorization.OAuth2Authorization;
import org.springframework.security.oauth2.server.authorization.OAuth2AuthorizationService;
import org.springframework.security.oauth2.server.authorization.OAuth2TokenType;
import org.springframework.security.oauth2.server.authorization.authentication.OAuth2AccessTokenAuthenticationToken;
import org.springframework.security.oauth2.server.authorization.authentication.OAuth2ClientAuthenticationToken;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClient;
import org.springframework.security.oauth2.server.authorization.context.AuthorizationServerContextHolder;
import org.springframework.security.oauth2.server.authorization.token.DefaultOAuth2TokenContext;
import org.springframework.security.oauth2.server.authorization.token.OAuth2TokenContext;
import org.springframework.security.oauth2.server.authorization.token.OAuth2TokenGenerator;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

import java.security.Principal;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import static com.art.common.security.client.core.constants.GiteeEndpointEnums.ACCESS_TOKEN_URL;
import static com.art.common.security.client.core.constants.GiteeEndpointEnums.USERINFO_URL;

/**
 * @author fxz
 * @version 0.0.1
 * @date 2023/4/15 16:38
 */
public class OAuth2GiteeAuthenticationProvider implements AuthenticationProvider {

	private static final String AUTHORIZED_SCOPE_KEY = OAuth2Authorization.class.getName().concat(".AUTHORIZED_SCOPE");

	private final HttpSecurity httpSecurity;

	private final GiteeRegisteredClientRepository giteeRegisteredClientRepository;

	@Setter
	private OAuth2AuthorizationService authorizationService;

	@Setter
	private OAuth2TokenGenerator<? extends OAuth2Token> tokenGenerator;

	/**
	 * @param authentication the authentication request object.
	 * @return
	 * @throws AuthenticationException
	 */
	@Override
	public Authentication authenticate(Authentication authentication) throws AuthenticationException {
		OAuth2GiteeAuthenticationToken grantAuthenticationToken = (OAuth2GiteeAuthenticationToken) authentication;

		String appid = grantAuthenticationToken.getAppid();
		String code = grantAuthenticationToken.getCode();
		String state = grantAuthenticationToken.getState();
		String binding = grantAuthenticationToken.getBinding();

		Map<String, Object> additionalParameters = grantAuthenticationToken.getAdditionalParameters();
		Set<String> requestedScopes = StringUtils.commaDelimitedListToSet(grantAuthenticationToken.getScope());

		OAuth2ClientAuthenticationToken clientPrincipal = ArtOAuth2EndpointUtils
			.getAuthenticatedClientElseThrowInvalidClient(grantAuthenticationToken);
		RegisteredClient registeredClient = clientPrincipal.getRegisteredClient();
		if (registeredClient == null) {
			OAuth2Error error = new OAuth2Error(OAuth2ErrorCodes.SERVER_ERROR, "注册客户不能为空", null);
			throw new OAuth2AuthenticationException(error);
		}

		Set<String> allowedScopes = registeredClient.getScopes();

		if (requestedScopes.isEmpty()) {
			// 请求中的 scope 为空，允许全部
			requestedScopes = allowedScopes;
		}
		else if (!allowedScopes.containsAll(requestedScopes)) {
			OAuth2Error error = new OAuth2Error(OAuth2ErrorCodes.INVALID_SCOPE,
					"OAuth 2.0 参数: " + OAuth2ParameterNames.SCOPE, null);
			throw new OAuth2AuthenticationException(error);
		}

		// 根据授权码获取token
		GiteeTokenResponse giteeTokenResponse = giteeRegisteredClientRepository.getAccessTokenResponse(appid, code,
				ACCESS_TOKEN_URL);

		// 根据token获取用户信息
		GiteeUserInfoResponse giteeUserInfoResponse = giteeRegisteredClientRepository.getUserInfo(USERINFO_URL, appid,
				state, giteeTokenResponse);

		// 储存或者更新gitee用户信息
		giteeRegisteredClientRepository.storeGiteeUsers(giteeTokenResponse, giteeUserInfoResponse, appid);

		String userId = giteeRegisteredClientRepository.getTagUser(appid, state);
		if (StringUtils.hasText(userId)) {
			// 绑定用户
			giteeRegisteredClientRepository.storeBinding(giteeUserInfoResponse.getId(), appid, Long.valueOf(userId));
		}

		OAuth2Authorization.Builder builder = OAuth2Authorization.withRegisteredClient(registeredClient);
		builder.principalName(String.valueOf(giteeUserInfoResponse.getId()));
		builder.authorizationGrantType(OAuth2GiteeAuthenticationToken.GITEE);

		UserDetails user = giteeRegisteredClientRepository.getUser(appid, giteeUserInfoResponse.getId());

		HashMap<String, Object> map = new HashMap<>(additionalParameters);
		map.put("tenantId", ((ArtAuthUser) user).getTenantId());

		UsernamePasswordAuthenticationToken principal = UsernamePasswordAuthenticationToken.authenticated(user, null,
				user.getAuthorities());

		builder.attribute(Principal.class.getName(), principal);
		builder.attribute(AUTHORIZED_SCOPE_KEY, requestedScopes);

		OAuth2Authorization authorization = builder.build();

		// @formatter:off
        DefaultOAuth2TokenContext.Builder tokenContextBuilder = DefaultOAuth2TokenContext.builder()
                .registeredClient(registeredClient)
                .principal(authorization.getAttribute(Principal.class.getName()))
                .authorizationServerContext(AuthorizationServerContextHolder.getContext())
                .authorization(authorization)
                .authorizedScopes(authorization.getAttribute(AUTHORIZED_SCOPE_KEY))
                .authorizationGrantType(OAuth2GiteeAuthenticationToken.GITEE)
                .authorizationGrant(grantAuthenticationToken);
        // @formatter:on

		OAuth2Authorization.Builder authorizationBuilder = OAuth2Authorization.from(authorization);

		// ----- Access token -----
		OAuth2TokenContext tokenContext = tokenContextBuilder.tokenType(OAuth2TokenType.ACCESS_TOKEN).build();
		OAuth2Token generatedAccessToken = tokenGenerator.generate(tokenContext);
		if (generatedAccessToken == null) {
			OAuth2Error error = new OAuth2Error(OAuth2ErrorCodes.SERVER_ERROR,
					"The token generator failed to generate the access token.", null);
			throw new OAuth2AuthenticationException(error);
		}
		OAuth2AccessToken oauth2AccessToken = new OAuth2AccessToken(OAuth2AccessToken.TokenType.BEARER,
				generatedAccessToken.getTokenValue(), generatedAccessToken.getIssuedAt(),
				generatedAccessToken.getExpiresAt(), tokenContext.getAuthorizedScopes());
		if (generatedAccessToken instanceof ClaimAccessor) {
			authorizationBuilder.token(oauth2AccessToken,
					(metadata) -> metadata.put(OAuth2Authorization.Token.CLAIMS_METADATA_NAME,
							((ClaimAccessor) generatedAccessToken).getClaims()));
		}
		else {
			authorizationBuilder.accessToken(oauth2AccessToken);
		}

		// ----- Refresh token -----
		OAuth2RefreshToken oauth2RefreshToken = null;
		if (registeredClient.getAuthorizationGrantTypes().contains(AuthorizationGrantType.REFRESH_TOKEN) &&
		// Do not issue refresh token to public client
				!clientPrincipal.getClientAuthenticationMethod().equals(ClientAuthenticationMethod.NONE)) {

			tokenContext = tokenContextBuilder.tokenType(OAuth2TokenType.REFRESH_TOKEN).build();
			OAuth2Token generatedRefreshToken = tokenGenerator.generate(tokenContext);
			if (!(generatedRefreshToken instanceof OAuth2RefreshToken)) {
				OAuth2Error error = new OAuth2Error(OAuth2ErrorCodes.SERVER_ERROR, "令牌生成器无法生成刷新令牌。", null);
				throw new OAuth2AuthenticationException(error);
			}
			oauth2RefreshToken = (OAuth2RefreshToken) generatedRefreshToken;
			authorizationBuilder.refreshToken(oauth2RefreshToken);
		}

		authorization = authorizationBuilder.build();

		authorizationService.save(authorization);

		return new OAuth2AccessTokenAuthenticationToken(registeredClient, clientPrincipal, oauth2AccessToken,
				oauth2RefreshToken, map);
	}

	@Override
	public boolean supports(Class<?> authentication) {
		return OAuth2GiteeAuthenticationToken.class.isAssignableFrom(authentication);
	}

	public OAuth2GiteeAuthenticationProvider(HttpSecurity httpSecurity) {
		Assert.notNull(httpSecurity, "HttpSecurity 不能为空");
		this.httpSecurity = httpSecurity;
		this.authorizationService = httpSecurity.getSharedObject(OAuth2AuthorizationService.class);
		this.tokenGenerator = ArtOAuth2ConfigurerUtils.getTokenGenerator(httpSecurity);
		this.giteeRegisteredClientRepository = ArtOAuth2ConfigurerUtils.getBean(httpSecurity,
				GiteeRegisteredClientRepository.class);
	}

}
