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
		// 保存路由信息
		this.save(routeConf);

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
		List<RouteConf> list = routeConf.stream().map(this::buildRouteConf).collect(Collectors.toList());

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
		// 删除路由信息
		this.removeById(id);

		// 发送消息到mq
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

	/**
	 * 构建路由信息
	 */
	@SuppressWarnings("all")
	private RouteConf buildRouteConf(Object value) {
		RouteConf r = new RouteConf();
		Map<String, Object> map = (Map) value;
		PropertyMapper mapper = PropertyMapper.get().alwaysApplyingWhenNonNull();

		mapper.from(map.get(RouteConf.Fields.name)).whenNonNull().as(String::valueOf).to(r::setName);

		mapper.from(map.get(RouteConf.Fields.routeId)).whenNonNull().as(String::valueOf).to(r::setRouteId);

		mapper.from(map.get(RouteConf.Fields.uri)).whenNonNull().as(String::valueOf).as(URI::create).as(String::valueOf)
				.to(r::setUri);

		mapper.from(map.get(RouteConf.Fields.sortOrder)).whenNonNull().as(String::valueOf).as(Integer::parseInt)
				.to(r::setSortOrder);

		mapper.from(map.get(RouteConf.Fields.metadata)).whenNonNull().as(String::valueOf)
				.as(v -> JSONUtil.toBean(v, Map.class)).as(JSONUtil::toJsonStr).to(r::setMetadata);

		mapper.from(map.get(RouteConf.Fields.filters)).whenNonNull().as(JSONArray::new)
				.as(v -> v.toList(FilterDefinitionDto.class)).as(JSONUtil::toJsonStr).to(r::setFilters);

		mapper.from(map.get(RouteConf.Fields.predicates)).whenNonNull().as(JSONArray::new)
				.as(v -> v.toList(PredicateDefinitionDto.class)).as(JSONUtil::toJsonStr).to(r::setPredicates);

		return r;
	}

}