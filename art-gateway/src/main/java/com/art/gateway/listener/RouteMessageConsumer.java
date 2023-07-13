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

package com.art.gateway.listener;

import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONUtil;
import com.art.common.gateway.dynamic.route.ArtRouteDefinition;
import com.art.common.gateway.dynamic.route.ArtRouteDefinitionRepository;
import com.art.core.common.constant.CacheConstants;
import com.art.mq.sdk.support.broadcast.AbstractRedisBroadcastMessageListener;
import com.art.system.api.route.dto.RouteConfDTO;
import com.art.system.api.route.mq.RouteMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.PropertyMapper;
import org.springframework.cloud.gateway.filter.FilterDefinition;
import org.springframework.cloud.gateway.handler.predicate.PredicateDefinition;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.net.URI;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * 监听路由刷新事件
 *
 * @author fxz
 */
@SuppressWarnings("unchecked")
@Slf4j
@Component
@RequiredArgsConstructor
public class RouteMessageConsumer extends AbstractRedisBroadcastMessageListener<RouteMessage> {

	private final RedisTemplate redisTemplate;

	private final ArtRouteDefinitionRepository artRouteDefinitionRepository;

	@Override
	public void onMessage(RouteMessage message) {
		// 清空缓存中的路由信息
		redisTemplate.delete(CacheConstants.ROUTE_KEY);

		if (log.isDebugEnabled()) {
			log.debug("接收到redis topic消息，缓存路由信息到redis {}", message.getRouteConfDOList());
		}
		List<RouteConfDTO> routeConfList = message.getRouteConfDOList();

		Map<String, ArtRouteDefinition> map = routeConfList.stream()
			.map(this::convert)
			.collect(Collectors.toMap(ArtRouteDefinition::getId, Function.identity()));

		redisTemplate.opsForHash().putAll(CacheConstants.ROUTE_KEY, map);
		artRouteDefinitionRepository.publishEvent();
	}

	public ArtRouteDefinition convert(RouteConfDTO dto) {
		ArtRouteDefinition routeDefinition = new ArtRouteDefinition();
		PropertyMapper mapper = PropertyMapper.get().alwaysApplyingWhenNonNull();

		mapper.from(dto.getName()).whenNonNull().to(routeDefinition::setName);
		mapper.from(dto.getRouteId()).whenNonNull().to(routeDefinition::setId);
		mapper.from(dto.getUri()).whenNonNull().as(URI::create).to(routeDefinition::setUri);
		mapper.from(dto.getSortOrder()).whenNonNull().as(Integer::valueOf).to(routeDefinition::setOrder);
		mapper.from(dto.getMetadata())
			.whenNonNull()
			.as(v -> JSONUtil.toBean(v, Map.class))
			.to(routeDefinition::setMetadata);
		mapper.from(dto.getFilters())
			.whenNonNull()
			.as(JSONArray::new)
			.as(v -> v.toList(FilterDefinition.class))
			.to(routeDefinition::setFilters);
		mapper.from(dto.getPredicates())
			.whenNonNull()
			.as(JSONArray::new)
			.as(v -> v.toList(PredicateDefinition.class))
			.to(routeDefinition::setPredicates);

		return routeDefinition;
	}

}