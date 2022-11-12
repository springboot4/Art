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

package com.fxz.system.handler;

import com.fxz.common.mq.redis.core.RedisMQTemplate;
import com.fxz.common.redis.constant.CacheConstants;
import com.fxz.system.mq.RouteMessage;
import com.fxz.system.service.RouteConfService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.boot.web.context.WebServerInitializedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

/**
 * @author Fxz
 * @version 1.0
 * @date 2022/8/20 14:17
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class DynamicRouteLoadHandler implements InitializingBean {

	private final RouteConfService routeConfService;

	private final RedisMQTemplate redisMQTemplate;

	@Override
	public void afterPropertiesSet() {
		loadRouteToRedis();
	}

	/**
	 * 加载路由信息到redis
	 */
	@Order
	@EventListener({ WebServerInitializedEvent.class })
	public void loadRouteToRedis() {
		// 发送消息告诉网关加载路由信息
		redisMQTemplate.send(new RouteMessage(routeConfService.list()));
	}

}
