package com.fxz.serversystem.controller;

import com.common.entity.FxzResponse;
import com.common.entity.router.VueRouter;
import com.common.entity.system.Menu;
import com.fxz.serversystem.service.IMenuService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.constraints.NotBlank;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @author Fxz
 * @version 1.0
 * @date 2021-11-29 18:47
 */
@Slf4j
@Validated
@RestController
@RequestMapping("/menu")
@RequiredArgsConstructor
public class MenuController {

	private final IMenuService menuService;

	@GetMapping("/{username}")
	public FxzResponse getUserRouters(@NotBlank(message = "{required}") @PathVariable String username) {
		Map<String, Object> result = new HashMap<>();
		// 构建用户路由对象
		List<VueRouter<Menu>> userRouters = this.menuService.getUserRouters(username);
		// 获取用户权限信息
		Set<String> userPermissions = this.menuService.findUserPermissions(username);
		// 组装数据
		result.put("routes", userRouters);
		result.put("permissions", userPermissions);
		return new FxzResponse().data(result);
	}

}
