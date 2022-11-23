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

package com.art.system.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.art.common.core.entity.DeptDataPermissionRespDTO;
import com.art.common.core.enums.RoleAdminEnum;
import com.art.common.core.exception.FxzException;
import com.art.common.core.param.PageParam;
import com.art.common.dataPermission.enums.DataScopeEnum;
import com.art.common.redis.constant.CacheConstants;
import com.art.common.security.entity.FxzAuthUser;
import com.art.system.dao.dataobject.*;
import com.art.system.dao.mysql.RoleMapper;
import com.art.system.service.*;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.StringPool;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
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
public class RoleServiceImpl extends ServiceImpl<RoleMapper, RoleDO> implements RoleService {

	@Resource
	private UserService userService;

	private final RoleMenuService roleMenuService;

	private final UserRoleService userRoleService;

	private final DeptService deptService;

	private final RedisTemplate redisTemplate;

	/**
	 * 分页查询角色信息
	 */
	@Override
	public IPage<?> PageRole(PageParam pageParam, String roleName) {
		Page<RoleDO> rolePage = new Page<>(pageParam.getCurrent(), pageParam.getSize());
		return this.getBaseMapper().selectPage(rolePage,
				Wrappers.<RoleDO>lambdaQuery().like(StringUtils.isNotEmpty(roleName), RoleDO::getRoleName, roleName));
	}

	/**
	 * 添加角色信息
	 */
	@Transactional(rollbackFor = Exception.class)
	@Override
	public RoleDO addRole(RoleDO roleDO) {
		// 保存角色信息
		this.getBaseMapper().insert(roleDO);

		// 保存角色菜单
		saveRoleMenu(roleDO);

		return roleDO;
	}

	/**
	 * 根据id获取角色信息
	 */
	@Cacheable(value = CacheConstants.GLOBALLY + "role", key = "#id", unless = "#result==null")
	@Override
	public RoleDO getRoleById(Long id) {
		return this.getBaseMapper().getRoleById(id);
	}

	/**
	 * 修改角色信息
	 */
	@CacheEvict(value = CacheConstants.GLOBALLY + "role", key = "#roleDO.roleId")
	@Transactional(rollbackFor = Exception.class)
	@Override
	public Boolean editRole(RoleDO roleDO) {
		this.getBaseMapper().updateById(roleDO);

		// 删除角色原有菜单
		roleMenuService.remove(Wrappers.<RoleMenuDO>lambdaQuery().eq(RoleMenuDO::getRoleId, roleDO.getRoleId()));

		// 保存角色菜单
		saveRoleMenu(roleDO);

		return Boolean.TRUE;
	}

	/**
	 * 删除角色信息
	 */
	@CacheEvict(value = CacheConstants.GLOBALLY + "role", key = "#id")
	@Override
	public Boolean deleteRoleById(Long id) {
		RoleDO roleDO = this.getById(id);
		if (RoleAdminEnum.isAdmin(roleDO.getCode())) {
			throw new FxzException("管理员角色不可删除！");
		}

		// 删除角色菜单关联信息
		roleMenuService.remove(Wrappers.<RoleMenuDO>lambdaQuery().eq(RoleMenuDO::getRoleId, id));
		// 删除角色用户关联信息
		userRoleService.remove(Wrappers.<UserRoleDO>lambdaQuery().eq(UserRoleDO::getRoleId, id));
		// 删除角色信息
		return this.getBaseMapper().deleteById(id) > 0;
	}

	/**
	 * 获取当前用户角色下的数据权限
	 */
	@Cacheable(value = CacheConstants.GLOBALLY + "user", key = "#user.userId+':dataScope'", unless = "#result==null")
	@Override
	public DeptDataPermissionRespDTO getDataPermission(FxzAuthUser user) {
		SystemUserDO loginUser = userService.findByName(user.getUsername());

		// 创建 DeptDataPermissionRespDTO 对象
		DeptDataPermissionRespDTO result = new DeptDataPermissionRespDTO();
		if (StringUtils.isNotBlank(loginUser.getRoleId())) {
			String[] roleIds = loginUser.getRoleId().split(StringPool.COMMA);
			if (roleIds.length > 0) {
				Arrays.stream(roleIds).forEach(roleId -> {
					String key = CacheConstants.GLOBALLY + "roleDO:" + roleId;
					RoleDO roleDO = (RoleDO) redisTemplate.opsForValue().get(key);
					if (ObjectUtil.isNull(roleDO)) {
						roleDO = getRoleById(Long.valueOf(roleId));
					}
					// 为空时，跳过
					if (ObjectUtil.isNull(roleDO.getDataScope())) {
						return;
					}
					// 情况一，ALL
					if (Objects.equals(roleDO.getDataScope(), DataScopeEnum.ALL.getScope())) {
						result.setAll(true);
						return;
					}
					// 情况二，DEPT_CUSTOM
					if (Objects.equals(roleDO.getDataScope(), DataScopeEnum.DEPT_CUSTOM.getScope())) {
						CollUtil.addAll(result.getDeptIds(), roleDO.getDataScopeDeptIds());
						// 自定义可见部门时，保证可以看到自己所在的部门。否则，一些场景下可能会有问题。
						// 例如说，登录时，基于 t_user 的 username 查询会可能被 dept_id 过滤掉
						CollUtil.addAll(result.getDeptIds(), loginUser.getDeptId());
						return;
					}
					// 情况三，DEPT_ONLY
					if (Objects.equals(roleDO.getDataScope(), DataScopeEnum.DEPT_ONLY.getScope())) {
						result.getDeptIds().add(loginUser.getDeptId());
						return;
					}
					// 情况四，DEPT_DEPT_AND_CHILD
					if (Objects.equals(roleDO.getDataScope(), DataScopeEnum.DEPT_AND_CHILD.getScope())) {
						List<DeptDO> deptDOS = deptService.getDeptsByParentId(loginUser.getDeptId());
						CollUtil.addAll(result.getDeptIds(),
								deptDOS.stream().map(DeptDO::getDeptId).collect(Collectors.toList()));
						// 添加本身部门id
						CollUtil.addAll(result.getDeptIds(), loginUser.getDeptId());
						return;
					}
					// 情况五，SELF
					if (Objects.equals(roleDO.getDataScope(), DataScopeEnum.SELF.getScope())) {
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
	@Cacheable(value = CacheConstants.GLOBALLY + "role:isSuperAdmin", key = "#roleId")
	@Override
	public boolean isSuperAdmin(String roleId) {
		if (StrUtil.isBlank(roleId)) {
			return false;
		}

		String[] roleIds = roleId.split(StringPool.COMMA);
		List<RoleDO> roleDOList = this.listByIds(Arrays.asList(roleIds));
		return roleDOList.stream().anyMatch(r -> RoleAdminEnum.SUPER_ADMIN.getType().equals(r.getCode()));
	}

	/**
	 * 获取所有角色
	 */
	@CacheEvict(value = CacheConstants.GLOBALLY + "role")
	@Override
	public List<RoleDO> getAllRole() {
		return this.list();
	}

	/**
	 * 保存角色菜单
	 */
	private void saveRoleMenu(RoleDO roleDO) {
		String menuId = roleDO.getMenuId();

		if (StringUtils.isBlank(menuId)) {
			return;
		}

		List<RoleMenuDO> roleMenuDOList = Arrays.stream(menuId.split(StringPool.COMMA)).map(item -> {
			RoleMenuDO roleMenuDO = new RoleMenuDO();
			roleMenuDO.setRoleId(roleDO.getRoleId());
			roleMenuDO.setMenuId(Long.valueOf(item));
			return roleMenuDO;
		}).collect(Collectors.toList());

		roleMenuService.saveBatch(roleMenuDOList);
	}

}
