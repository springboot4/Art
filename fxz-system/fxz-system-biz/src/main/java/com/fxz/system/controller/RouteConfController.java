package com.fxz.system.controller;

import cn.hutool.json.JSONArray;
import com.fxz.common.mp.result.Result;
import com.fxz.system.entity.RouteConf;
import com.fxz.system.service.RouteConfService;
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
	public Result<Boolean> add(@RequestBody RouteConf routeConf) {
		return Result.success(routeConfService.addRouteConf(routeConf));
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
	public Result<RouteConf> findById(Long id) {
		return Result.success(routeConfService.findById(id));
	}

	/**
	 * 获取全部路由信息
	 */
	@Operation(summary = "获取全部路由信息")
	@GetMapping(value = "/findAll")
	public Result<List<RouteConf>> findAll() {
		return Result.success(routeConfService.findAll());
	}

}