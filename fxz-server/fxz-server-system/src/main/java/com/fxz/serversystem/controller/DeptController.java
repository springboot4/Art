package com.fxz.serversystem.controller;

import com.fxz.common.core.entity.FxzResponse;
import com.fxz.serversystem.service.IDeptService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Fxz
 * @version 1.0
 * @date 2022-02-27 18:33
 */
@RestController
@RequestMapping("/dept")
@RequiredArgsConstructor
public class DeptController {

	private final IDeptService deptService;

	/**
	 * 获取部门树
	 */
	@GetMapping("/getDeptTree")
	public FxzResponse getDeptTree() {
		return new FxzResponse().data(deptService.getDeptTree());
	}

}
