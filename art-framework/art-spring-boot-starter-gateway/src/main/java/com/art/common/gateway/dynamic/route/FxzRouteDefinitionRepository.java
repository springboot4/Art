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

package com.art.common.gateway.dynamic.route;

import com.art.common.redis.core.constant.CacheConstants;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.cloud.gateway.event.RefreshRoutesEvent;
import org.springframework.cloud.gateway.route.RouteDefinition;
import org.springframework.cloud.gateway.route.RouteDefinitionRepository;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.ApplicationEventPublisherAware;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

/**
 * @author Fxz
 * @version 0.0.1
 * @date 2022/8/20 12:01
 */
@SuppressWarnings("all")
@Slf4j
@Component
@RequiredArgsConstructor
public class FxzRouteDefinitionRepository implements RouteDefinitionRepository, ApplicationEventPublisherAware {

	private final RedisTemplate redisTemplate;

	private ApplicationEventPublisher applicationEventPublisher;

	/**
	 * 加载路由信息
	 */
	@Override
	public Flux<RouteDefinition> getRouteDefinitions() {
		List<FxzRouteDefinition> values = redisTemplate.opsForHash().values(CacheConstants.ROUTE_KEY);
		log.info("加载redis中路由: {} 条， {}", values.size(), values);
		return Flux.fromIterable(values);
	}

	/**
	 * 保存路由信息
	 */
	@Override
	public Mono<Void> save(Mono<RouteDefinition> route) {
		route.subscribe(this::saveRouteToRedis);
		return Mono.empty();
	}

	/**
	 * 删除路由信息
	 */
	@Override
	public Mono<Void> delete(Mono<String> routeId) {
		routeId.subscribe(this::deleteRouteToRedis);
		return Mono.empty();
	}

	/**
	 * 保存路由信息到redis
	 */
	private void saveRouteToRedis(RouteDefinition routeDefinition) {
		FxzRouteDefinition fxzRouteDefinition = new FxzRouteDefinition();
		BeanUtils.copyProperties(routeDefinition, fxzRouteDefinition);
		log.info("保存路由信息到redis:{}", fxzRouteDefinition);
		redisTemplate.opsForHash().put(CacheConstants.ROUTE_KEY, fxzRouteDefinition.getId(), fxzRouteDefinition);
	}

	/**
	 * 删除redis中的路由信息
	 */
	private void deleteRouteToRedis(String id) {
		log.info("删除redis中的路由信息{}", id);
		redisTemplate.opsForHash().delete(CacheConstants.ROUTE_KEY, id);
	}

	/**
	 * 通知网关重新加载路由
	 */
	public void publishEvent() {
		applicationEventPublisher.publishEvent(new RefreshRoutesEvent(this));
	}

	@Override
	public void setApplicationEventPublisher(ApplicationEventPublisher applicationEventPublisher) {
		this.applicationEventPublisher = applicationEventPublisher;
	}

}
