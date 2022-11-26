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

package com.art.system.service;

import cn.hutool.json.JSONArray;
import com.art.system.api.route.dto.RouteConfDTO;

import java.util.List;

/**
 * 路由配置表
 *
 * @author fxz
 * @date 2022-08-20
 */
public interface RouteConfService {

	/**
	 * 添加路由信息
	 */
	Boolean addRouteConf(RouteConfDTO routeConfDTO);

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
	RouteConfDTO findById(Long id);

	/**
	 * 查询所有路由信息
	 */
	List<RouteConfDTO> findAll();

}