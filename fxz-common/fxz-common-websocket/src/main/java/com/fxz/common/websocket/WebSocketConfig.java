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

package com.fxz.common.websocket;

import com.fxz.common.websocket.handler.UserNoticeWebSocketHandler;
import com.fxz.common.websocket.interceptor.UserNoticeWebSocketInterceptor;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;
import org.springframework.web.socket.server.standard.ServerEndpointExporter;

/**
 * @author Fxz
 * @version 0.0.1
 * @date 2022/6/28 09:23
 */
@AutoConfiguration
@ComponentScan
@EnableWebSocket
@RequiredArgsConstructor
public class WebSocketConfig implements WebSocketConfigurer {

	private final UserNoticeWebSocketHandler userNoticeWebSocketHandler;

	private final UserNoticeWebSocketInterceptor userNoticeWebSocketInterceptor;

	/**
	 * 注入ServerEndpointExporter,该Bean会自动注册使用@ServerEndpoint注解声明的websocket endpoint
	 */
	@Bean
	@ConditionalOnMissingBean
	public ServerEndpointExporter serverEndpointExporter() {
		return new ServerEndpointExporter();
	}

	@Override
	public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
		registry
				// WebSocket 连接处理器
				.addHandler(userNoticeWebSocketHandler, "/websocket/user")
				// WebSocket 拦截器
				.addInterceptors(userNoticeWebSocketInterceptor)
				// 允许跨域
				.setAllowedOrigins("*");
	}

}
