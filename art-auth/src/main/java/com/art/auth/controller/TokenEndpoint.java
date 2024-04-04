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

package com.art.auth.controller;

import cn.hutool.core.date.DatePattern;
import cn.hutool.core.date.TemporalAccessorUtil;
import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.StrUtil;
import com.art.core.common.model.PageResult;
import com.art.core.common.model.Result;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpHeaders;
import org.springframework.security.oauth2.core.OAuth2AccessToken;
import org.springframework.security.oauth2.server.authorization.OAuth2Authorization;
import org.springframework.security.oauth2.server.authorization.OAuth2AuthorizationService;
import org.springframework.security.oauth2.server.authorization.OAuth2TokenType;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author Fxz
 * @version 0.0.1
 * @date 2021-11-27 16:28
 */
@SuppressWarnings("all")
@Slf4j
@RequestMapping("/token")
@RestController
@RequiredArgsConstructor
public class TokenEndpoint {

	private final OAuth2AuthorizationService oAuth2AuthorizationService;

	private final RedisTemplate<String, OAuth2Authorization> oauth2RedisTemplate;

	/**
	 * 退出登录
	 */
	@DeleteMapping("/myLogout")
	public Result<Void> logout(HttpServletRequest request,
			@RequestHeader(value = HttpHeaders.AUTHORIZATION) String authorization) {
		String token = authorization.replace(OAuth2AccessToken.TokenType.BEARER.getValue(), StrUtil.EMPTY).trim();
		log.info("退出登录,token:{}", token);

		return removeToken(token);
	}

	/**
	 * 删除token
	 */
	@SneakyThrows
	@DeleteMapping("/{token}")
	public Result<Void> removeToken(@PathVariable("token") String token) {
		OAuth2Authorization authorization = oAuth2AuthorizationService.findByToken(token, OAuth2TokenType.ACCESS_TOKEN);

		oAuth2AuthorizationService.remove(authorization);

		return Result.success();
	}

	/**
	 * 分页查询token
	 * @param param 分页参数
	 * @return
	 */
	@PostMapping("/page")
	public Result<PageResult> tokenList(@RequestBody Map<String, Object> param) {
		String key = "token::access_token::*";
		Set<String> keys = oauth2RedisTemplate.keys(key);

		Integer current = MapUtil.getInt(param, "current");
		Integer size = MapUtil.getInt(param, "size");

		List<String> pages = keys.stream().skip((current - 1) * size).limit(size).collect(Collectors.toList());

		List<Map> list = oauth2RedisTemplate.opsForValue().multiGet(pages).stream().map(o -> {
			OAuth2Authorization authorization = (OAuth2Authorization) o;
			HashMap<String, Object> map = new HashMap<>();

			// 客户端
			map.put("client_id", authorization.getRegisteredClientId());

			// 用户名
			map.put("username", authorization.getPrincipalName());

			// token值
			OAuth2Authorization.Token<OAuth2AccessToken> accessToken = authorization.getAccessToken();
			map.put("access_token", accessToken.getToken().getTokenValue());

			// 过期时间
			String expiresAt = TemporalAccessorUtil.format(accessToken.getToken().getExpiresAt(),
					DatePattern.NORM_DATETIME_PATTERN);
			map.put("expires_in", expiresAt);

			// token类型
			map.put("token_type", OAuth2TokenType.ACCESS_TOKEN.getValue());

			return map;
		}).collect(Collectors.toList());

		Page page = new Page(current, size);
		page.setRecords(list);
		page.setTotal(keys.size());

		return Result.success(PageResult.success(page));
	}

}
