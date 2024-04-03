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

package com.art.system.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.art.common.dataPermission.enums.DataScopeEnum;
import com.art.common.security.core.model.ArtAuthUser;
import com.art.core.common.constant.RoleEnum;
import com.art.core.common.exception.ArtException;
import com.art.core.common.model.DeptDataPermissionRespEntity;
import com.art.system.api.dept.dto.DeptDTO;
import com.art.system.api.role.dto.RoleDTO;
import com.art.system.api.role.dto.RolePageDTO;
import com.art.system.api.user.dto.SystemUserDTO;
import com.art.system.core.bo.RoleBO;
import com.art.system.core.convert.RoleConvert;
import com.art.system.dao.dataobject.RoleDO;
import com.art.system.dao.dataobject.RoleMenuDO;
import com.art.system.dao.redis.role.RoleRedisConstants;
import com.art.system.manager.RoleManager;
import com.art.system.manager.RoleMenuManager;
import com.art.system.manager.UserRoleManager;
import com.art.system.service.DeptService;
import com.art.system.service.RoleService;
import com.art.system.service.UserService;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.StringPool;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import jakarta.annotation.Resource;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * @author Fxz
 * @version 0.0.1
 * @date 2022-02-27 17:52
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class RoleServiceImpl implements RoleService {

	@Resource
	private UserService userService;

	private final RoleManager roleManager;

	private final RoleMenuManager roleMenuManager;

	private final UserRoleManager userRoleManager;

	private final DeptService deptService;

	private final RedisTemplate redisTemplate;

	/**
	 * 分页查询角色信息
	 */
	@Override
	public IPage<RoleDTO> pageRole(RolePageDTO pageDTO) {
		return RoleConvert.INSTANCE.convert(roleManager.pageRole(pageDTO));
	}

	/**
	 * 添加角色信息
	 */
	@Transactional(rollbackFor = Exception.class)
	@Override
	public RoleDTO addRole(RoleDTO roleDTO) {
		// 保存角色信息
		Long id = roleManager.addRole(roleDTO);
		if (Objects.isNull(id)) {
			throw new ArtException("保存角色信息失败！");
		}

		// 保存角色菜单
		saveRoleMenu(roleDTO.setRoleId(id));

		return roleDTO;
	}

	/**
	 * 根据id获取角色信息
	 */
	@Cacheable(value = RoleRedisConstants.CACHE_NAMES, key = "#id", unless = "#result==null")
	@Override
	public RoleDTO getRoleById(Long id) {
		return RoleConvert.INSTANCE.convert(roleManager.getRoleById(id));
	}

	/**
	 * 修改角色信息
	 */
	@CacheEvict(value = RoleRedisConstants.CACHE_NAMES, key = "#roleDTO.roleId")
	@Transactional(rollbackFor = Exception.class)
	@Override
	public Boolean editRole(RoleDTO roleDTO) {
		roleManager.updateRoleById(roleDTO);

		// 删除角色原有菜单
		deleteRoleMenuByRoleId(roleDTO.getRoleId());

		// 保存角色菜单
		saveRoleMenu(roleDTO);

		return Boolean.TRUE;
	}

	/**
	 * 删除角色信息
	 */
	@CacheEvict(value = RoleRedisConstants.CACHE_NAMES, key = "#id")
	@Override
	public Boolean deleteRoleById(Long id) {
		RoleBO roleBO = roleManager.getRoleById(id);
		if (RoleEnum.isSystem(roleBO.getCode())) {
			throw new ArtException("系统内置角色不可删除！");
		}

		// 删除角色菜单关联信息
		deleteRoleMenuByRoleId(id);
		// 删除角色用户关联信息
		userRoleManager.deleteUserRoleByRoleId(id);
		// 删除角色信息
		return roleManager.deleteRoleById(id) > 0;
	}

	/**
	 * 获取当前用户角色下的数据权限
	 */
	@Cacheable(value = RoleRedisConstants.DATA_SCOPE, key = "#user.userId", unless = "#result==null")
	@Override
	public DeptDataPermissionRespEntity getDataPermission(ArtAuthUser user) {
		DeptDataPermissionRespEntity result = new DeptDataPermissionRespEntity();
		SystemUserDTO loginUser = userService.findByName(user.getUsername());

		if (StringUtils.isNotBlank(loginUser.getRoleId())) {
			String[] roleIds = loginUser.getRoleId().split(StringPool.COMMA);
			if (roleIds.length > 0) {
				Arrays.stream(roleIds).forEach(roleId -> {
					String key = RoleRedisConstants.CACHE_NAMES + roleId;
					RoleDTO roleDTO = (RoleDTO) redisTemplate.opsForValue().get(key);
					if (ObjectUtil.isNull(roleDTO)) {
						roleDTO = getRoleById(Long.valueOf(roleId));
					}
					// 为空时，跳过
					if (ObjectUtil.isNull(roleDTO.getDataScope())) {
						return;
					}
					// 情况一，ALL
					if (Objects.equals(roleDTO.getDataScope(), DataScopeEnum.ALL.getScope())) {
						result.setAll(true);
						return;
					}
					// 情况二，DEPT_CUSTOM
					if (Objects.equals(roleDTO.getDataScope(), DataScopeEnum.DEPT_CUSTOM.getScope())) {
						CollUtil.addAll(result.getDeptIds(), roleDTO.getDataScopeDeptIds());
						// 自定义可见部门时，保证可以看到自己所在的部门。否则，一些场景下可能会有问题。
						// 例如说，登录时，基于 t_user 的 username 查询会可能被 dept_id 过滤掉
						CollUtil.addAll(result.getDeptIds(), loginUser.getDeptId());
						return;
					}
					// 情况三，DEPT_ONLY
					if (Objects.equals(roleDTO.getDataScope(), DataScopeEnum.DEPT_ONLY.getScope())) {
						result.getDeptIds().add(loginUser.getDeptId());
						return;
					}
					// 情况四，DEPT_DEPT_AND_CHILD
					if (Objects.equals(roleDTO.getDataScope(), DataScopeEnum.DEPT_AND_CHILD.getScope())) {
						List<DeptDTO> depts = deptService.getDeptsByParentId(loginUser.getDeptId());
						CollUtil.addAll(result.getDeptIds(),
								depts.stream().map(DeptDTO::getDeptId).collect(Collectors.toList()));
						// 添加本身部门id
						CollUtil.addAll(result.getDeptIds(), loginUser.getDeptId());
						return;
					}
					// 情况五，SELF
					if (Objects.equals(roleDTO.getDataScope(), DataScopeEnum.SELF.getScope())) {
						result.setSelf(true);
						return;
					}
					// 未知情况，error log 即可
					log.error("[getDeptDataPermission][LoginUser({}) roleDO({}) 无法处理]", loginUser.getUserId(), result);
				});
			}
		}
		return result;
	}

	/**
	 * 是否是超级管理员
	 * @param roleId 角色id
	 */
	@Cacheable(value = RoleRedisConstants.IS_SUPER_ADMIN, key = "#roleId")
	@Override
	public boolean isSuperAdmin(String roleId) {
		if (StrUtil.isBlank(roleId)) {
			return false;
		}

		String[] roleIds = roleId.split(StringPool.COMMA);
		List<RoleDO> roleDOList = this.roleManager.listRoleByIds(Arrays.asList(roleIds));
		return roleDOList.stream().anyMatch(r -> RoleEnum.SUPER_ADMIN.getType().equals(r.getCode()));
	}

	/**
	 * 获取所有角色
	 */
	@Cacheable(value = RoleRedisConstants.CACHE_NAMES)
	@Override
	public List<RoleDTO> getAllRole() {
		return RoleConvert.INSTANCE.convert(roleManager.listRole());
	}

	/**
	 * 根据角色id删除角色菜单信息
	 * @param id 角色id
	 */

	private void deleteRoleMenuByRoleId(Long id) {
		roleMenuManager.deleteRoleMenuByRoleId(id);
	}

	/**
	 * 保存角色菜单
	 */
	private void saveRoleMenu(RoleDTO roleDTO) {
		String menuId = roleDTO.getMenuId();
		if (StringUtils.isBlank(menuId)) {
			return;
		}

		List<RoleMenuDO> roleMenuDOList = Arrays.stream(menuId.split(StringPool.COMMA)).map(item -> {
			RoleMenuDO roleMenuDO = new RoleMenuDO();
			roleMenuDO.setRoleId(roleDTO.getRoleId());
			roleMenuDO.setMenuId(Long.valueOf(item));
			return roleMenuDO;
		}).collect(Collectors.toList());

		roleMenuManager.addRoleMenuList(roleMenuDOList);
	}

}
