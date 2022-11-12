/*
 * COPYRIGHT (C) 2022 Art AUTHORS(fxzcloud@gmail.com). ALL RIGHTS RESERVED.
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

package com.fxz.auth.endpoint;

import cn.hutool.core.map.MapUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fxz.common.core.constant.FxzConstant;
import com.fxz.common.core.constant.SecurityConstants;
import com.fxz.common.core.exception.FxzAuthException;
import com.fxz.common.mp.result.PageResult;
import com.fxz.common.mp.result.Result;
import com.fxz.common.security.annotation.Ojbk;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.security.oauth2.common.DefaultOAuth2AccessToken;
import org.springframework.security.oauth2.provider.token.ConsumerTokenServices;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.security.Principal;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author Fxz
 * @version 1.0
 * @date 2021-11-27 16:28
 */
@SuppressWarnings("all")
@Slf4j
@RestController
@RequiredArgsConstructor
public class SecurityEndpoint {

	private final ConsumerTokenServices consumerTokenServices;

	private final RedisTemplate redisTemplate;

	@GetMapping("/user")
	public Principal user(Principal principal) {
		return principal;
	}

	/**
	 * 退出登录
	 */
	@Ojbk
	@DeleteMapping("/myLogout")
	public Result<Void> logout(HttpServletRequest request) {
		// 获取请求头信息
		String authorization = request.getHeader("Authorization");
		// 提取token
		String token = StringUtils.replace(authorization, FxzConstant.OAUTH2_TOKEN_TYPE, "");
		token = StringUtils.replace(token, FxzConstant.OAUTH2_TOKEN_TYPE_LOW, "");
		log.info("退出登录,token:{}", token);

		return removeToken(token);
	}

	/**
	 * 删除token
	 */
	@SneakyThrows
	@DeleteMapping("/token/{token}")
	public Result<Void> removeToken(@PathVariable("token") String token) {
		// 此处调用了redis删除缓存的登录信息
		if (!consumerTokenServices.revokeToken(token)) {
			throw new FxzAuthException("退出登录失败");
		}

		return Result.success();
	}

	/**
	 * 分页查询token
	 * @param param 分页参数
	 * @return
	 */
	@PostMapping("/token/page")
	public Result<PageResult> tokenList(@RequestBody Map<String, Object> param) {
		redisTemplate.setValueSerializer(RedisSerializer.java());

		String key = SecurityConstants.TOKEN_PREFIX.concat("access:*");
		Set<String> keys = redisTemplate.keys(key);

		Integer current = MapUtil.getInt(param, "current");
		Integer size = MapUtil.getInt(param, "size");

		List<String> pages = keys.stream().skip((current - 1) * size).limit(size).collect(Collectors.toList());
		List<DefaultOAuth2AccessToken> list = redisTemplate.opsForValue().multiGet(pages);

		Page page = new Page(current, size);
		page.setRecords(list);
		page.setTotal(keys.size());

		return Result.success(PageResult.success(page));
	}

}
