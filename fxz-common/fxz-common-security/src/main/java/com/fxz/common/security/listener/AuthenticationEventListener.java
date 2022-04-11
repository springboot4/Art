package com.fxz.common.security.listener;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.extra.servlet.ServletUtil;
import com.fxz.common.security.entity.FxzAuthUser;
import com.fxz.system.dto.OperLogDto;
import com.fxz.system.feign.RemoteLogService;
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
