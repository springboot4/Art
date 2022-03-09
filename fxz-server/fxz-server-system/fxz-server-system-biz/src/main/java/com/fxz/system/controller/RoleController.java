package com.fxz.system.controller;

import com.fxz.common.core.param.PageParam;
import com.fxz.common.core.result.PageResult;
import com.fxz.common.core.result.Result;
import com.fxz.common.core.utils.FxzUtil;
import com.fxz.system.entity.Role;
import com.fxz.system.service.IRoleService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * @author Fxz
 * @version 1.0
 * @date 2022-02-27 17:47
 */
@Slf4j
@RestController
@RequestMapping("/role")
@RequiredArgsConstructor
public class RoleController {

	private final IRoleService roleService;

	/**
	 * 获取所有角色
	 */
	@GetMapping("/getAllRole")
	public Result<List<Role>> getAllRole() {
		return Result.success(roleService.list());
	}

	/**
	 * 分页查询角色信息
	 */
	@GetMapping("/page")
	public Result<PageResult<?>> pageRole(PageParam pageParam, String roleName) {
		return Result.success(PageResult.success(roleService.PageRole(pageParam, roleName)));
	}

	/**
	 * 添加角色
	 */
	@PostMapping("/addRole")
	public Result<Void> addRole(@RequestBody Role role) {
		return Result.judge(roleService.addRole(role));
	}

	/**
	 * 根据id获取角色信息
	 */
	@GetMapping("/getRoleById/{id}")
	public Result<Role> getRoleById(@PathVariable("id") Long id) {
		return Result.success(roleService.getRoleById(id));
	}

	/**
	 * 修改角色信息
	 */
	@PutMapping("/editRole")
	public Result<Void> editRole(@RequestBody Role role) {
		return Result.judge(roleService.editRole(role));
	}

	/**
	 * 删除角色信息
	 */
	@DeleteMapping("/deleteRoleById/{id}")
	public Result<Void> deleteRoleById(@PathVariable("id") Long id) {
		return Result.judge(roleService.deleteRoleById(id));
	}

}