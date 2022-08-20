package com.fxz.system.service;

import cn.hutool.json.JSONArray;
import com.baomidou.mybatisplus.extension.service.IService;
import com.fxz.system.entity.RouteConf;

import java.util.List;

/**
 * 路由配置表
 *
 * @author fxz
 * @date 2022-08-20
 */
public interface RouteConfService extends IService<RouteConf> {

	/**
	 * 添加路由信息
	 */
	Boolean addRouteConf(RouteConf routeConf);

	/**
	 * 修改路由信息
	 */
	Boolean updateRouteConf(JSONArray routeConf);

	/**
	 * 删除路由信息
	 */
	Boolean deleteRouteConf(Long id);

	/**
	 * 获取单条路由信息
	 */
	RouteConf findById(Long id);

	/**
	 * 查询所有路由信息
	 */
	List<RouteConf> findAll();

}