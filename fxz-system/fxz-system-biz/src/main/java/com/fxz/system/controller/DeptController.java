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

package com.fxz.system.controller;

import com.fxz.common.mp.result.Result;
import com.fxz.system.entity.Dept;
import com.fxz.system.service.IDeptService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.web.bind.annotation.*;

/**
 * @author Fxz
 * @version 1.0
 * @date 2022-02-27 18:33
 */
@Tag(name = "部门管理")
@RestController
@RequestMapping("/dept")
@RequiredArgsConstructor
public class DeptController {

	private final IDeptService deptService;

	/**
	 * 获取部门树
	 */
	@Operation(summary = "获取部门树")
	@Cacheable(value = "fxz_cloud:dept", key = "'deptTree'", unless = "#result.data==null")
	@GetMapping("/getDeptTree")
	public Result<Dept> getDeptTree() {
		return Result.success(deptService.getDeptTree());
	}

	/**
	 * 根据id删除部门
	 */
	@Operation(summary = "根据id删除部门")
	@CacheEvict(value = "fxz_cloud:dept", allEntries = true)
	@SneakyThrows
	@DeleteMapping("/deleteById/{id}")
	public Result<Void> deleteById(@PathVariable("id") Long id) {
		return Result.judge(deptService.delete(id));
	}

	/**
	 * 保存部门信息
	 */
	@Operation(summary = "保存部门信息")
	@CacheEvict(value = "fxz_cloud:dept", allEntries = true)
	@PostMapping("/addDept")
	public Result<Void> addDept(@RequestBody Dept dept) {
		return Result.judge(deptService.addDept(dept));
	}

	/**
	 * 根据id获取部门数据
	 */
	@Operation(summary = "根据id获取部门数据")
	@Cacheable(value = "fxz_cloud:dept", key = "#id", unless = "#result.data==null")
	@GetMapping("/getDeptById/{id}")
	public Result<Dept> getDeptById(@PathVariable("id") Long id) {
		return Result.success(deptService.getById(id));
	}

	/**
	 * 更新部门信息(父机构ID 不能修改)
	 */
	@Operation(summary = "更新部门信息(父机构ID 不能修改)")
	@CacheEvict(value = "fxz_cloud:dept", allEntries = true)
	@PutMapping("/updateDept")
	public Result<Void> updateDept(@RequestBody Dept dept) {
		return Result.judge(deptService.updateById(dept));
	}

}
