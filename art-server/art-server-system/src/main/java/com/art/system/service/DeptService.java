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

package com.art.system.service;

import com.art.common.core.exception.FxzException;
import com.art.system.api.dept.dto.DeptDTO;

import java.util.List;

/**
 * @author Fxz
 * @version 0.0.1
 * @date 2022-02-27 18:38
 */
public interface DeptService {

	/**
	 * 获取部门树
	 */
	DeptDTO getDeptTree();

	/**
	 * 根据id删除部门
	 */
	Boolean deleteById(Long id) throws FxzException;

	/**
	 * 保存部门信息
	 */
	Boolean addDept(DeptDTO deptDTO);

	/**
	 * 根据Pid查询下级部门
	 */
	List<DeptDTO> getDeptsByParentId(Long pId);

	/**
	 * 根据id查询dept
	 * @param id deptId
	 * @return dept
	 */
	DeptDTO getDeptById(Long id);

	/**
	 * 根据id更新部门
	 * @param deptDTO deptDto
	 * @return 是否更新成功
	 */
	Boolean updateById(DeptDTO deptDTO);

	/**
	 * 获取当前用户部门(包含父级)
	 * @return 用户所处部门
	 */
	String getDeptNameByUserId();

}
