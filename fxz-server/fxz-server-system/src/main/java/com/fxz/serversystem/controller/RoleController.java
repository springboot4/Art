package com.fxz.serversystem.controller;

import com.fxz.common.core.entity.FxzResponse;
import com.fxz.common.core.entity.PageParam;
import com.fxz.common.core.entity.system.Role;
import com.fxz.common.core.utils.FxzUtil;
import com.fxz.serversystem.service.IRoleService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

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
	public FxzResponse getAllRole() {
		return new FxzResponse().data(roleService.list());
	}

	/**
	 * 分页查询角色信息
	 */
	@GetMapping("/page")
	public FxzResponse PageRole(PageParam pageParam, String roleName) {
		Map<String, Object> dataTable = FxzUtil.getDataTable(roleService.PageRole(pageParam, roleName));
		return new FxzResponse().data(dataTable);
	}

	/**
	 * 添加角色
	 */
	@PostMapping("/addRole")
	public FxzResponse addRole(@RequestBody Role role) {
		return new FxzResponse().data(roleService.addRole(role));
	}

	/**
	 * 根据id获取角色信息
	 */
	@GetMapping("/getRoleById/{id}")
	public FxzResponse getRoleById(@PathVariable("id") Long id) {
		return new FxzResponse().data(roleService.getRoleById(id));
	}

	/**
	 * 修改角色信息
	 */
	@PutMapping("/editRole")
	public FxzResponse editRole(@RequestBody Role role) {
		return new FxzResponse().data(roleService.editRole(role));
	}

	/**
	 * 删除角色信息
	 */
	@DeleteMapping("/deleteRoleById/{id}")
	public FxzResponse deleteRoleById(@PathVariable("id") Long id) {
		return new FxzResponse().data(roleService.deleteRoleById(id));
	}

}
