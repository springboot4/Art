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

package com.art.gateway.listener;

import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONUtil;
import com.art.common.gateway.dynamic.route.FxzRouteDefinition;
import com.art.common.gateway.dynamic.route.FxzRouteDefinitionRepository;
import com.art.common.mq.redis.stream.AbstractStreamMessageListener;
import com.art.common.redis.constant.CacheConstants;
import com.art.system.api.route.dto.RouteConfDTO;
import com.art.system.api.route.mq.RouteMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.FilterDefinition;
import org.springframework.cloud.gateway.handler.predicate.PredicateDefinition;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.net.URI;
import java.util.List;
import java.util.Map;

/**
 * 监听路由刷新事件
 *
 * @author fxz
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class RouteMessageConsumer extends AbstractStreamMessageListener<RouteMessage> {

	private final RedisTemplate redisTemplate;

	private final FxzRouteDefinitionRepository fxzRouteDefinitionRepository;

	@Override
	public void onMessage(RouteMessage message) {
		// 清空缓存中的路由信息
		redisTemplate.delete(CacheConstants.ROUTE_KEY);

		log.info("接收到redis stream消息，缓存路由信息到redis {}", message.getRouteConfDOList());
		List<RouteConfDTO> routeConfList = message.getRouteConfDOList();

		routeConfList.stream().forEach(sys -> {
			FxzRouteDefinition fxzRouteDefinition = new FxzRouteDefinition();

			fxzRouteDefinition.setName(sys.getName());
			fxzRouteDefinition.setId(sys.getRouteId());
			fxzRouteDefinition.setUri(URI.create(sys.getUri()));
			fxzRouteDefinition.setOrder(sys.getSortOrder());
			JSONArray filterObj = JSONUtil.parseArray(sys.getFilters());
			fxzRouteDefinition.setFilters(filterObj.toList(FilterDefinition.class));
			JSONArray predicateObj = JSONUtil.parseArray(sys.getPredicates());
			fxzRouteDefinition.setPredicates(predicateObj.toList(PredicateDefinition.class));
			fxzRouteDefinition.setMetadata(JSONUtil.toBean(sys.getMetadata(), Map.class));

			redisTemplate.opsForHash().put(CacheConstants.ROUTE_KEY, fxzRouteDefinition.getId(), fxzRouteDefinition);
		});

		fxzRouteDefinitionRepository.publishEvent();
	}

}