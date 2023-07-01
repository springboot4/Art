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

import com.art.system.dao.dataobject.RoleMenuDO;
import com.art.system.dao.mysql.RoleMenuMapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author Fxz
 * @version 0.0.1
 * @date 2022/11/26 13:01
 */
@Component
@RequiredArgsConstructor
public class RoleMenuManager {

	private final RoleMenuMapper roleMenuMapper;

	public void addRoleMenuList(List<RoleMenuDO> roleMenuDOList) {
		roleMenuDOList.forEach(roleMenuMapper::insert);
	}

	public void deleteRoleMenuByRoleId(Long id) {
		roleMenuMapper.delete(Wrappers.<RoleMenuDO>lambdaQuery().eq(RoleMenuDO::getRoleId, id));
	}

	public List<RoleMenuDO> getRoleMenuByRoleIds(List<Long> roleIds) {
		return roleMenuMapper.selectList(Wrappers.<RoleMenuDO>lambdaQuery().in(RoleMenuDO::getRoleId, roleIds));
	}

}
