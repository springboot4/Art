package com.fxz.system.controller;

import com.fxz.common.core.entity.FxzResponse;
import com.fxz.system.entity.Dept;
import com.fxz.system.service.IDeptService;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.web.bind.annotation.*;

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

	/**
	 * 根据id删除部门
	 */
	@SneakyThrows
	@DeleteMapping("/deleteById/{id}")
	public FxzResponse deleteById(@PathVariable("id") Long id) {
		return new FxzResponse().data(deptService.delete(id));
	}

	/**
	 * 保存部门信息
	 */
	@PostMapping("/addDept")
	public FxzResponse addDept(@RequestBody Dept dept) {
		return new FxzResponse().data(deptService.addDept(dept));
	}

	/**
	 * 根据id获取部门数据
	 */
	@GetMapping("/getDeptById/{id}")
	public FxzResponse getDeptById(@PathVariable("id") Long id) {
		return new FxzResponse().data(deptService.getById(id));
	}

	/**
	 * 更新部门信息(父机构ID 不能修改)
	 */
	@PutMapping("/updateDept")
	public FxzResponse updateDept(@RequestBody Dept dept) {
		return new FxzResponse().data(deptService.updateById(dept));
	}

}
