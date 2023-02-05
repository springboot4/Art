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

package com.art.system.controller;

import cn.hutool.json.JSONArray;
import com.art.common.core.model.Result;
import com.art.system.api.route.dto.RouteConfDTO;
import com.art.system.service.RouteConfService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 路由配置表
 *
 * @author fxz
 * @date 2022-08-20
 */
@Tag(name = "路由配置管理")
@RestController
@RequestMapping("/routeConf")
@RequiredArgsConstructor
public class RouteConfController {

	private final RouteConfService routeConfService;

	/**
	 * 添加路由信息
	 */
	@Operation(summary = "添加路由信息")
	@PostMapping(value = "/add")
	public Result<Boolean> add(@RequestBody RouteConfDTO routeConfDTO) {
		return Result.success(routeConfService.addRouteConf(routeConfDTO));
	}

	/**
	 * 修改路由信息
	 */
	@Operation(summary = "修改路由信息")
	@PutMapping(value = "/update")
	public Result<Boolean> update(@RequestBody JSONArray routeConf) {
		return Result.success(routeConfService.updateRouteConf(routeConf));
	}

	/**
	 * 删除路由信息
	 */
	@Operation(summary = "删除路由信息")
	@DeleteMapping(value = "/delete")
	public Result<Boolean> delete(Long id) {
		return Result.judge(routeConfService.deleteRouteConf(id));
	}

	/**
	 * 获取单条路由信息
	 */
	@Operation(summary = "获取单条路由信息")
	@GetMapping(value = "/findById")
	public Result<RouteConfDTO> findById(Long id) {
		return Result.success(routeConfService.findById(id));
	}

	/**
	 * 获取全部路由信息
	 */
	@Operation(summary = "获取全部路由信息")
	@GetMapping(value = "/findAll")
	public Result<List<RouteConfDTO>> findAll() {
		return Result.success(routeConfService.findAll());
	}

}