package com.fxz.system.service.impl;

import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fxz.common.mq.redis.core.RedisMQTemplate;
import com.fxz.system.dto.FilterDefinitionDto;
import com.fxz.system.dto.PredicateDefinitionDto;
import com.fxz.system.entity.RouteConf;
import com.fxz.system.mapper.RouteConfMapper;
import com.fxz.system.mq.RouteMessage;
import com.fxz.system.service.RouteConfService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.stereotype.Service;

import java.net.URI;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * 路由配置表
 *
 * @author fxz
 * @date 2022-08-20
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class RouteConfServiceImpl extends ServiceImpl<RouteConfMapper, RouteConf> implements RouteConfService {

	private final RedisMQTemplate redisMQTemplate;

	private final RouteConfMapper routeConfMapper;

	/**
	 * 添加路由信息
	 */
	@Override
	public Boolean addRouteConf(RouteConf routeConf) {
		this.save(routeConf);
		this.sendMq();
		return Boolean.TRUE;
	}

	/**
	 * 修改路由信息
	 */
	@Override
	public Boolean updateRouteConf(JSONArray routeConf) {
		List<RouteConf> list = routeConf.stream().map(value -> {
			RouteConf r = new RouteConf();
			Map<String, Object> map = (Map) value;

			Object routeId = map.get(RouteConf.Fields.routeId);
			if (Objects.nonNull(routeId)) {
				r.setRouteId(String.valueOf(routeId));
			}

			Object name = map.get(RouteConf.Fields.name);
			if (Objects.nonNull(name)) {
				r.setName(String.valueOf(name));
			}

			Object predicates = map.get(RouteConf.Fields.predicates);
			if (Objects.nonNull(predicates)) {
				JSONArray predicatesArray = (JSONArray) predicates;
				List<PredicateDefinitionDto> predicateDefinitionList = predicatesArray
						.toList(PredicateDefinitionDto.class);
				r.setPredicates(JSONUtil.toJsonStr(predicateDefinitionList));
			}

			Object filters = map.get(RouteConf.Fields.filters);
			if (filters != null) {
				JSONArray filtersArray = (JSONArray) filters;
				List<FilterDefinitionDto> filterDefinitionList = filtersArray.toList(FilterDefinitionDto.class);
				r.setFilters(JSONUtil.toJsonStr(filterDefinitionList));
			}

			Object uri = map.get(RouteConf.Fields.uri);
			if (uri != null) {
				r.setUri(URI.create(String.valueOf(uri)).toString());
			}

			Object order = map.get(RouteConf.Fields.sortOrder);
			if (order != null) {
				r.setSortOrder(Integer.parseInt(String.valueOf(order)));
			}

			Object metadata = map.get(RouteConf.Fields.metadata);
			if (metadata != null) {
				Map<String, Object> metadataMap = JSONUtil.toBean(String.valueOf(metadata), Map.class);
				r.setMetadata(JSONUtil.toJsonStr(metadataMap));
			}
			return r;
		}).collect(Collectors.toList());

		// 删掉所有路由信息
		this.remove(Wrappers.emptyWrapper());

		// 保存路由信息
		this.saveBatch(list);

		// 通知网关加载路由
		this.sendMq();

		return Boolean.TRUE;
	}

	/**
	 * 删除路由信息
	 */
	@Override
	public Boolean deleteRouteConf(Long id) {
		this.removeById(id);
		this.sendMq();
		return Boolean.TRUE;
	}

	/**
	 * 获取单条路由信息
	 */
	@Override
	public RouteConf findById(Long id) {
		return this.getById(id);
	}

	/**
	 * 查询所有路由信息
	 */
	@Override
	public List<RouteConf> findAll() {
		return routeConfMapper.findAll();
	}

	private void sendMq() {
		redisMQTemplate.send(new RouteMessage(this.list()));
	}

}