package com.fxz.system.controller;

import com.fxz.common.mp.result.Result;
import com.fxz.system.entity.Dept;
import com.fxz.system.service.IDeptService;
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
@RestController
@RequestMapping("/dept")
@RequiredArgsConstructor
public class DeptController {

    private final IDeptService deptService;

    /**
     * 获取部门树
     */
    @Cacheable(value = "fxz_cloud:dept", key = "'deptTree'", unless = "#result.data==null")
    @GetMapping("/getDeptTree")
    public Result<Dept> getDeptTree() {
        return Result.success(deptService.getDeptTree());
    }

    /**
     * 根据id删除部门
     */
    @CacheEvict(value = "fxz_cloud:dept", allEntries = true)
    @SneakyThrows
    @DeleteMapping("/deleteById/{id}")
    public Result<Void> deleteById(@PathVariable("id") Long id) {
        return Result.judge(deptService.delete(id));
    }

    /**
     * 保存部门信息
     */
    @CacheEvict(value = "fxz_cloud:dept", allEntries = true)
    @PostMapping("/addDept")
    public Result<Void> addDept(@RequestBody Dept dept) {
        return Result.judge(deptService.addDept(dept));
    }

    /**
     * 根据id获取部门数据
     */
    @Cacheable(value = "fxz_cloud:dept", key = "#id", unless = "#result.data==null")
    @GetMapping("/getDeptById/{id}")
    public Result<Dept> getDeptById(@PathVariable("id") Long id) {
        return Result.success(deptService.getById(id));
    }

    /**
     * 更新部门信息(父机构ID 不能修改)
     */
    @CacheEvict(value = "fxz_cloud:dept", allEntries = true)
    @PutMapping("/updateDept")
    public Result<Void> updateDept(@RequestBody Dept dept) {
        return Result.judge(deptService.updateById(dept));
    }

}
