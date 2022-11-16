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

package com.art.common.security.listener;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.extra.servlet.ServletUtil;
import com.art.common.security.entity.FxzAuthUser;
import com.art.system.dto.OperLogDto;
import com.art.system.feign.RemoteLogService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.security.authentication.event.AuthenticationSuccessEvent;
import org.springframework.security.core.Authentication;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;

/**
 * @author fxz
 */
@RequiredArgsConstructor
@Slf4j
public class AuthenticationEventListener {

	private final RemoteLogService remoteLogService;

	@EventListener(AuthenticationSuccessEvent.class)
	public void onAuthenticationSuccessEvent(AuthenticationSuccessEvent event) {
		if (event.getSource().getClass().getName()
				.equals("org.springframework.security.authentication.UsernamePasswordAuthenticationToken")) {
			Authentication authentication = (Authentication) event.getSource();
			if (isUserAuthentication(authentication)) {
				ServletRequestAttributes requestAttributes = (ServletRequestAttributes) RequestContextHolder
						.getRequestAttributes();
				HttpServletRequest request = requestAttributes.getRequest();

				OperLogDto logDto = new OperLogDto();
				logDto.setTitle("用户登录");
				logDto.setUpdateTime(LocalDateTime.now());
				logDto.setStatus(0);
				logDto.setOperName(authentication.getName());
				logDto.setCreateBy(authentication.getName());
				logDto.setOperIp(ServletUtil.getClientIP(request));
				logDto.setOperParam(authentication.getName());
				logDto.setBusinessType(4);
				logDto.setRequestMethod(request.getMethod());
				remoteLogService.add(logDto);
				log.info("登陆成功:{}", authentication.getName());
			}
		}
	}

	private boolean isUserAuthentication(Authentication authentication) {
		return authentication.getPrincipal() instanceof FxzAuthUser
				|| CollUtil.isNotEmpty(authentication.getAuthorities());
	}

}
