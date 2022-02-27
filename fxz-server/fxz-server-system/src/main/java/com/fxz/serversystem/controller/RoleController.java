package com.fxz.serversystem.controller;

import com.fxz.common.core.entity.FxzResponse;
import com.fxz.serversystem.service.IRoleService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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

}
