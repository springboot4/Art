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

package com.art.system.service.impl;

import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONUtil;
import com.art.common.redis.core.mq.client.RedisMQTemplate;
import com.art.system.api.route.dto.FilterDefinitionDTO;
import com.art.system.api.route.dto.PredicateDefinitionDTO;
import com.art.system.api.route.dto.RouteConfDTO;
import com.art.system.api.route.mq.RouteMessage;
import com.art.system.core.convert.RouteConfConvert;
import com.art.system.dao.dataobject.RouteConfDO;
import com.art.system.manager.RouteConfManager;
import com.art.system.service.RouteConfService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.PropertyMapper;
import org.springframework.stereotype.Service;

import java.net.URI;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 路由配置表
 *
 * @author fxz
 * @date 2022-08-20
 */
@SuppressWarnings("all")
@Slf4j
@Service
@RequiredArgsConstructor
public class RouteConfServiceImpl implements RouteConfService {

	private final RedisMQTemplate redisMQTemplate;

	private final RouteConfManager routeConfManager;

	/**
	 * 添加路由信息
	 */
	@Override
	public Boolean addRouteConf(RouteConfDTO routeConfDTO) {
		// 保存路由信息
		routeConfManager.addRouteConf(routeConfDTO);

		// 通知mq
		this.sendMq();

		return Boolean.TRUE;
	}

	/**
	 * 修改路由信息
	 */
	@Override
	public Boolean updateRouteConf(JSONArray routeConf) {
		// 构建路由信息
		List<RouteConfDTO> list = routeConf.stream().map(this::convert).collect(Collectors.toList());

		// 删掉所有路由信息
		routeConfManager.deleteRouteConf();

		// 保存路由信息
		list.forEach(routeConfManager::addRouteConf);

		// 通知网关加载路由
		this.sendMq();

		return Boolean.TRUE;
	}

	/**
	 * 删除路由信息
	 */
	@Override
	public Boolean deleteRouteConf(Long id) {
		// 删除路由信息
		routeConfManager.deleteRouteConfById(id);

		// 发送消息到mq
		this.sendMq();

		return Boolean.TRUE;
	}

	/**
	 * 获取单条路由信息
	 */
	@Override
	public RouteConfDTO findById(Long id) {
		return RouteConfConvert.INSTANCE.convert(routeConfManager.getRouteCongById(id));
	}

	/**
	 * 查询所有路由信息
	 */
	@Override
	public List<RouteConfDTO> findAll() {
		List<RouteConfDO> dos = routeConfManager.listRouteConf();
		List<RouteConfDTO> dtos = RouteConfConvert.INSTANCE.convert(dos);
		return dtos;
	}

	private void sendMq() {
		redisMQTemplate.send(new RouteMessage(findAll()));
	}

	/**
	 * 构建路由信息
	 */
	private RouteConfDTO convert(Object value) {
		RouteConfDTO r = new RouteConfDTO();

		Map<String, Object> map = (Map) value;

		PropertyMapper mapper = PropertyMapper.get().alwaysApplyingWhenNonNull();

		mapper.from(map.get(RouteConfDO.Fields.name)).whenNonNull().as(String::valueOf).to(r::setName);

		mapper.from(map.get(RouteConfDO.Fields.routeId)).whenNonNull().as(String::valueOf).to(r::setRouteId);

		mapper.from(map.get(RouteConfDO.Fields.uri))
			.whenNonNull()
			.as(String::valueOf)
			.as(URI::create)
			.as(String::valueOf)
			.to(r::setUri);

		mapper.from(map.get(RouteConfDO.Fields.sortOrder))
			.whenNonNull()
			.as(String::valueOf)
			.as(Integer::parseInt)
			.to(r::setSortOrder);

		mapper.from(map.get(RouteConfDO.Fields.metadata))
			.whenNonNull()
			.as(String::valueOf)
			.as(v -> JSONUtil.toBean(v, Map.class))
			.as(JSONUtil::toJsonStr)
			.to(r::setMetadata);

		mapper.from(map.get(RouteConfDO.Fields.filters))
			.whenNonNull()
			.as(JSONArray::new)
			.as(v -> v.toList(FilterDefinitionDTO.class))
			.as(JSONUtil::toJsonStr)
			.to(r::setFilters);

		mapper.from(map.get(RouteConfDO.Fields.predicates))
			.whenNonNull()
			.as(JSONArray::new)
			.as(v -> v.toList(PredicateDefinitionDTO.class))
			.as(JSONUtil::toJsonStr)
			.to(r::setPredicates);

		return r;
	}

}