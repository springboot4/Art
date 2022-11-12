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

package com.fxz.common.security.extension.mobile;

import cn.hutool.core.util.StrUtil;
import com.fxz.common.core.constant.SecurityConstants;
import com.fxz.common.core.exception.FxzException;
import com.fxz.common.security.service.FxzUserDetailsService;
import com.fxz.common.security.util.SecurityUtil;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.HashSet;
import java.util.Map;
import java.util.Optional;

/**
 * 短信验证码认证授权提供者
 *
 * @author fxz
 */
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class FxzSmsCodeAuthenticationProvider implements AuthenticationProvider {

	private StringRedisTemplate redisTemplate;

	private Map<String, FxzUserDetailsService> userDetailsServiceMap;

	@Override
	public Authentication authenticate(Authentication authentication) throws AuthenticationException {
		FxzSmsCodeAuthenticationToken authenticationToken = (FxzSmsCodeAuthenticationToken) authentication;
		String mobile = (String) authenticationToken.getPrincipal();
		String code = (String) authenticationToken.getCredentials();

		String codeKey = SecurityConstants.SMS_CODE_PREFIX + mobile;
		String correctCode = redisTemplate.opsForValue().get(codeKey);
		// 验证码比对
		if (StrUtil.isBlank(correctCode) || !code.equals(correctCode)) {
			throw new FxzException("验证码不正确");
		}
		// 比对成功删除缓存的验证码
		redisTemplate.delete(codeKey);

		String clientId = SecurityUtil.getOAuth2ClientId();

		Optional<FxzUserDetailsService> optional = userDetailsServiceMap.values().stream()
				.filter(s -> s.support(clientId, null)).findFirst();

		if (!optional.isPresent()) {
			throw new FxzException("请检查客户端配置!");
		}

		UserDetails userDetails = optional.get().loadUserByMobile(mobile);

		authenticationToken = new FxzSmsCodeAuthenticationToken(userDetails, authentication.getCredentials(),
				new HashSet<>());
		authenticationToken.setDetails(authentication.getDetails());

		return authenticationToken;
	}

	/**
	 * 是否匹配此Provider
	 */
	@Override
	public boolean supports(Class<?> authentication) {
		return FxzSmsCodeAuthenticationToken.class.isAssignableFrom(authentication);
	}

}
