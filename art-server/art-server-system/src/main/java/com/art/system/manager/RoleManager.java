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

package com.art.system.manager;

import com.art.system.api.role.dto.RoleDTO;
import com.art.system.api.role.dto.RolePageDTO;
import com.art.system.core.convert.RoleConvert;
import com.art.system.dao.dataobject.RoleDO;
import com.art.system.dao.mysql.RoleMapper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author Fxz
 * @version 0.0.1
 * @date 2022/11/23 19:40
 */
@Component
@RequiredArgsConstructor
public class RoleManager {

	private final RoleMapper roleMapper;

	public List<RoleDO> listRole() {
		return roleMapper.selectList(Wrappers.emptyWrapper());
	}

	public Page<RoleDO> pageRole(RolePageDTO pageDTO) {
		LambdaQueryWrapper<RoleDO> wrapper = Wrappers.<RoleDO>lambdaQuery()
				.like(StringUtils.isNotEmpty(pageDTO.getRoleName()), RoleDO::getRoleName, pageDTO.getRoleName());
		return roleMapper.selectPage(Page.of(pageDTO.getCurrent(), pageDTO.getSize()), wrapper);
	}

	/**
	 * 新增角色信息
	 * @param roleDTO 角色dto
	 * @return 角色idx
	 */
	public Long addRole(RoleDTO roleDTO) {
		RoleDO roleDO = RoleConvert.INSTANCE.convert(roleDTO);
		int count = roleMapper.insert(roleDO);
		return count != 1 ? null : roleDO.getRoleId();
	}

	public RoleDO getRoleById(Long id) {
		return roleMapper.selectById(id);
	}

	public void updateRoleById(RoleDTO roleDTO) {
		roleMapper.updateById(RoleConvert.INSTANCE.convert(roleDTO));
	}

	public Integer deleteRoleById(Long id) {
		return roleMapper.deleteById(id);
	}

	public List<RoleDO> listRoleByIds(List<String> roleIds) {
		return roleMapper.selectBatchIds(roleIds);
	}

}
