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

package com.art.common.security.core.client;

import cn.hutool.core.util.BooleanUtil;
import cn.hutool.core.util.StrUtil;
import com.art.common.core.exception.FxzException;
import com.art.common.core.model.ResultOpt;
import com.art.system.api.client.ClientServiceApi;
import com.art.system.api.client.dto.ClientDetailsDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.oauth2.core.ClientAuthenticationMethod;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClient;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClientRepository;
import org.springframework.security.oauth2.server.authorization.settings.ClientSettings;
import org.springframework.security.oauth2.server.authorization.settings.OAuth2TokenFormat;
import org.springframework.security.oauth2.server.authorization.settings.TokenSettings;
import org.springframework.util.StringUtils;

import java.time.Duration;
import java.util.Arrays;
import java.util.Optional;

/**
 * @author fxz
 * @version 0.0.1
 * @date 2023/4/13 10:29
 */
@RequiredArgsConstructor
public class ArtRegisteredClientRepository implements RegisteredClientRepository {

	private final ClientServiceApi clientServiceApi;

	/**
	 * 刷新令牌有效期默认 30 天
	 */
	private final static int REFRESH_TOKEN_VALIDITY_SECONDS = 60 * 60 * 24 * 30;

	/**
	 * 请求令牌有效期默认 6 小时
	 */
	private final static int ACCESS_TOKEN_VALIDITY_SECONDS = 60 * 60 * 6;

	/**
	 * @param clientId the client identifier
	 * @return
	 */
	@Override
	public RegisteredClient findByClientId(String clientId) {
		// @formatter:off
        ClientDetailsDTO clientDetails = ResultOpt.ofNullable(clientServiceApi.findById(clientId))
                .assertSuccess(r -> new FxzException(String.format("客户端不存在:%s,%s", clientId, r.getMsg())))
                .peek().getData();
        // @formatter:on

		RegisteredClient.Builder builder = RegisteredClient
			// 注册标识符
			.withId(clientDetails.getClientId())
			// 客户端id
			.clientId(clientDetails.getClientId())
			// 客户端密钥
			.clientSecret("{noop}" + clientDetails.getClientSecret())
			// 身份验证方法
			.clientAuthenticationMethod(ClientAuthenticationMethod.CLIENT_SECRET_POST)
			.clientAuthenticationMethods(clientAuthenticationMethods -> {
				clientAuthenticationMethods.add(ClientAuthenticationMethod.CLIENT_SECRET_BASIC);
				clientAuthenticationMethods.add(ClientAuthenticationMethod.CLIENT_SECRET_POST);
			});

		// 授权模式
		Optional.ofNullable(clientDetails.getAuthorizedGrantTypes())
			.ifPresent(grants -> StringUtils.commaDelimitedListToSet(grants)
				.forEach(s -> builder.authorizationGrantType(new AuthorizationGrantType(s))));

		// 回调地址
		Optional.ofNullable(clientDetails.getWebServerRedirectUri())
			.ifPresent(redirectUri -> Arrays.stream(redirectUri.split(StrUtil.COMMA))
				.filter(StrUtil::isNotBlank)
				.forEach(builder::redirectUri));

		// scope
		Optional.ofNullable(clientDetails.getScope())
			.ifPresent(scope -> Arrays.stream(scope.split(StrUtil.COMMA))
				.filter(StrUtil::isNotBlank)
				.forEach(builder::scope));

		return builder
			.tokenSettings(TokenSettings.builder()
				.accessTokenFormat(OAuth2TokenFormat.REFERENCE)
				.accessTokenTimeToLive(Duration.ofSeconds(Optional.ofNullable(clientDetails.getAccessTokenValidity())
					.orElse(ACCESS_TOKEN_VALIDITY_SECONDS)))
				.refreshTokenTimeToLive(Duration.ofSeconds(Optional.ofNullable(clientDetails.getRefreshTokenValidity())
					.orElse(REFRESH_TOKEN_VALIDITY_SECONDS)))
				.build())
			.clientSettings(ClientSettings.builder()
				.requireAuthorizationConsent(!BooleanUtil.toBoolean(clientDetails.getAutoapprove()))
				.build())
			.build();
	}

	/**
	 * @param registeredClient the {@link RegisteredClient}
	 */
	@Override
	public void save(RegisteredClient registeredClient) {
		throw new UnsupportedOperationException();
	}

	/**
	 * @param id the registration identifier
	 * @return
	 */
	@Override
	public RegisteredClient findById(String id) {
		throw new UnsupportedOperationException();
	}

}
