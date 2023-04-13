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

package com.art.common.security.core.support;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.security.oauth2.core.OAuth2AccessToken;
import org.springframework.security.oauth2.core.OAuth2RefreshToken;
import org.springframework.security.oauth2.core.endpoint.OAuth2ParameterNames;
import org.springframework.security.oauth2.server.authorization.OAuth2Authorization;
import org.springframework.security.oauth2.server.authorization.OAuth2AuthorizationCode;
import org.springframework.security.oauth2.server.authorization.OAuth2AuthorizationService;
import org.springframework.security.oauth2.server.authorization.OAuth2TokenType;
import org.springframework.util.Assert;

import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

/**
 * 对于授权结果处理的redis实现
 *
 * @author fxz
 * @version 0.0.1
 * @date 2023/4/12 16:41
 */
@RequiredArgsConstructor
public class RedisOAuth2AuthorizationService implements OAuth2AuthorizationService {

	private final RedisTemplate<String, Object> redisTemplate;

	/**
	 * @param authorization the {@link OAuth2Authorization}
	 */
	@Override
	public void save(OAuth2Authorization authorization) {
		Assert.notNull(authorization, "authorization cannot be null");

		Optional.ofNullable(authorization.getAttribute(OAuth2ParameterNames.STATE)).ifPresent(token -> {
			redisTemplate.setValueSerializer(RedisSerializer.java());
			redisTemplate.opsForValue()
				.set(buildKey(OAuth2ParameterNames.STATE, token.toString()), authorization, 10, TimeUnit.MINUTES);
		});

		Optional.ofNullable(authorization.getToken(OAuth2AuthorizationCode.class)).ifPresent(authorizationCode -> {
			redisTemplate.setValueSerializer(RedisSerializer.java());
			OAuth2AuthorizationCode authorizationCodeToken = authorizationCode.getToken();
			long between = ChronoUnit.MINUTES.between(Objects.requireNonNull(authorizationCodeToken.getIssuedAt()),
					authorizationCodeToken.getExpiresAt());
			redisTemplate.opsForValue()
				.set(buildKey(OAuth2ParameterNames.CODE, authorizationCodeToken.getTokenValue()), authorization,
						between, TimeUnit.MINUTES);
		});

		Optional.ofNullable(authorization.getRefreshToken()).ifPresent(refreshTokenToken -> {
			redisTemplate.setValueSerializer(RedisSerializer.java());
			OAuth2RefreshToken refreshToken = refreshTokenToken.getToken();
			long between = ChronoUnit.SECONDS.between(Objects.requireNonNull(refreshToken.getIssuedAt()),
					refreshToken.getExpiresAt());
			redisTemplate.opsForValue()
				.set(buildKey(OAuth2ParameterNames.REFRESH_TOKEN, refreshToken.getTokenValue()), authorization, between,
						TimeUnit.SECONDS);
		});

		Optional.ofNullable(authorization.getAccessToken()).ifPresent(accessTokenToken -> {
			redisTemplate.setValueSerializer(RedisSerializer.java());
			OAuth2AccessToken accessToken = accessTokenToken.getToken();
			long between = ChronoUnit.SECONDS.between(Objects.requireNonNull(accessToken.getIssuedAt()),
					accessToken.getExpiresAt());
			redisTemplate.opsForValue()
				.set(buildKey(OAuth2ParameterNames.ACCESS_TOKEN, accessToken.getTokenValue()), authorization, between,
						TimeUnit.SECONDS);
		});
	}

	/**
	 * @param authorization the {@link OAuth2Authorization}
	 */
	@Override
	public void remove(OAuth2Authorization authorization) {
		Assert.notNull(authorization, "authorization cannot be null");

		List<String> keys = new ArrayList<>();
		Optional.ofNullable(authorization.getAttribute(OAuth2ParameterNames.STATE)).ifPresent(token -> {
			keys.add(buildKey(OAuth2ParameterNames.STATE, token.toString()));
		});

		Optional.ofNullable(authorization.getToken(OAuth2AuthorizationCode.class)).ifPresent(authorizationCode -> {
			OAuth2AuthorizationCode authorizationCodeToken = authorizationCode.getToken();
			keys.add(buildKey(OAuth2ParameterNames.CODE, authorizationCodeToken.getTokenValue()));
		});

		Optional.ofNullable(authorization.getRefreshToken()).ifPresent(refreshTokenToken -> {
			OAuth2RefreshToken refreshToken = authorization.getRefreshToken().getToken();
			keys.add(buildKey(OAuth2ParameterNames.REFRESH_TOKEN, refreshToken.getTokenValue()));
		});

		Optional.ofNullable(authorization.getAccessToken()).ifPresent(accessTokenToken -> {
			OAuth2AccessToken accessToken = authorization.getAccessToken().getToken();
			keys.add(buildKey(OAuth2ParameterNames.ACCESS_TOKEN, accessToken.getTokenValue()));
		});

		redisTemplate.delete(keys);
	}

	/**
	 * @param token the token credential
	 * @param tokenType the {@link OAuth2TokenType token type}
	 * @return
	 */
	@Override
	public OAuth2Authorization findByToken(String token, OAuth2TokenType tokenType) {
		Assert.hasText(token, "token cannot be empty");
		Assert.notNull(tokenType, "tokenType cannot be empty");
		OAuth2Authorization oAuth2Authorization = null;
		try {
			redisTemplate.setValueSerializer(RedisSerializer.java());
			oAuth2Authorization = (OAuth2Authorization) redisTemplate.opsForValue()
				.get(buildKey(tokenType.getValue(), token));
		}
		catch (Throwable throwable) {
			System.out.println(throwable);
		}
		return oAuth2Authorization;
	}

	private String buildKey(String type, String id) {
		return String.format("token::%s::%s", type, id);
	}

	/**
	 * @param id the authorization identifier
	 * @return
	 */
	@Override
	public OAuth2Authorization findById(String id) {
		throw new UnsupportedOperationException();
	}

}
