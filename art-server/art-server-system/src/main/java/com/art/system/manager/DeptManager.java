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

package com.art.system.manager;

import com.art.system.api.dept.dto.DeptDTO;
import com.art.system.core.bo.DeptBO;
import com.art.system.core.convert.DeptConvert;
import com.art.system.dao.dataobject.DeptDO;
import com.art.system.dao.mysql.DeptMapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Objects;

/**
 * @author Fxz
 * @version 0.0.1
 * @date 2022/11/23 10:26
 */
@Component
@RequiredArgsConstructor
public class DeptManager {

	private final DeptMapper deptMapper;

	/**
	 * 当前部门是否存在下级部门
	 * @param id 部门id
	 * @return true表示存在下级部门 false表示不存在下级部门
	 */
	public Boolean existsSubordinate(Long id) {
		DeptDO deptDO = deptMapper
			.selectOne(Wrappers.<DeptDO>lambdaQuery().eq(DeptDO::getParentId, id).last("limit 1"));
		return Objects.nonNull(deptDO);
	}

	/**
	 * 获取部门树
	 * @return 部门树
	 */
	public DeptBO getDeptTree() {
		return deptMapper.getDeptTree();
	}

	/**
	 * 根据部门id删除部门
	 * @param id 部门id
	 * @return 影响条数
	 */
	public Integer deleteById(Long id) {
		return deptMapper.deleteById(id);
	}

	public Integer addDept(DeptDTO deptDTO) {
		return deptMapper.insert(DeptConvert.INSTANCE.convert(deptDTO));
	}

	public DeptDO getDeptById(Long id) {
		return deptMapper.selectById(id);
	}

	public Integer updateById(DeptDTO deptDTO) {
		return deptMapper.updateById(DeptConvert.INSTANCE.convert(deptDTO));
	}

	public DeptBO getDeptsByParentId(Long pId) {
		return deptMapper.getDeptsByParentId(pId);
	}

	public String getDeptNameByUserId(Long deptId) {
		return deptMapper.getDeptNameByUserId(deptId);
	}

}
