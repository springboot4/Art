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

package com.art.system.controller;

import com.art.core.common.model.Result;
import com.art.system.api.dept.dto.DeptDTO;
import com.art.system.dao.redis.dept.DeptRedisKeyConstants;
import com.art.system.service.DeptService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Fxz
 * @version 0.0.1
 * @date 2022-02-27 18:33
 */
@Tag(name = "部门管理")
@RestController
@RequestMapping("/dept")
@RequiredArgsConstructor
public class DeptController {

	private final DeptService deptService;

	/**
	 * 获取部门树
	 */
	@Operation(summary = "获取部门树")
	@Cacheable(value = DeptRedisKeyConstants.CACHE_NAMES, key = DeptRedisKeyConstants.DEPT_TREE_KEY,
			unless = "#result.data==null")
	@GetMapping("/getDeptTree")
	public Result<DeptDTO> getDeptTree() {
		return Result.success(deptService.getDeptTree());
	}

	/**
	 * 根据id删除部门
	 */
	@Operation(summary = "根据id删除部门")
	@CacheEvict(value = DeptRedisKeyConstants.CACHE_NAMES, allEntries = true)
	@SneakyThrows
	@DeleteMapping("/deleteById/{id}")
	public Result<Void> deleteById(@PathVariable("id") Long id) {
		return Result.judge(deptService.deleteById(id));
	}

	/**
	 * 保存部门信息
	 */
	@Operation(summary = "保存部门信息")
	@CacheEvict(value = DeptRedisKeyConstants.CACHE_NAMES, allEntries = true)
	@PostMapping("/addDept")
	public Result<Void> addDept(@RequestBody DeptDTO deptDTO) {
		return Result.judge(deptService.addDept(deptDTO));
	}

	/**
	 * 根据id获取部门数据
	 */
	@Operation(summary = "根据id获取部门数据")
	@Cacheable(value = DeptRedisKeyConstants.CACHE_NAMES, key = "#id", unless = "#result.data==null")
	@GetMapping("/getDeptById/{id}")
	public Result<DeptDTO> getDeptById(@PathVariable("id") Long id) {
		return Result.success(deptService.getDeptById(id));
	}

	/**
	 * 更新部门信息
	 */
	@Operation(summary = "更新部门信息")
	@CacheEvict(value = DeptRedisKeyConstants.CACHE_NAMES, allEntries = true)
	@PutMapping("/updateDept")
	public Result<Void> updateDept(@RequestBody DeptDTO deptDTO) {
		return Result.judge(deptService.updateById(deptDTO));
	}

	/**
	 * 获取当前用户部门(包含父级)
	 */
	@Operation(summary = "获取当前用户部门(包含父级)")
	@GetMapping("/getDeptNameByUserId")
	public Result<String> getDeptNameByUserId() {
		return Result.success(deptService.getDeptNameByUserId());
	}

}
