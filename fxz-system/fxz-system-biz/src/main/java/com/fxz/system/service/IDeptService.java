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

package com.fxz.system.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.fxz.common.core.exception.FxzException;
import com.fxz.system.entity.Dept;

import java.util.List;

/**
 * @author Fxz
 * @version 1.0
 * @date 2022-02-27 18:38
 */
public interface IDeptService extends IService<Dept> {

	/**
	 * 获取部门树
	 */
	Dept getDeptTree();

	/**
	 * 根据id删除部门
	 */
	Boolean delete(Long id) throws FxzException;

	/**
	 * 保存部门信息
	 */
	Boolean addDept(Dept dept);

	/**
	 * 根据Pid查询下级部门
	 */
	List<Dept> getDeptsByParentId(Long pId);

}
