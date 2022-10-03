package com.fxz.system.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.text.StrPool;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fxz.common.core.enums.GlobalStatusEnum;
import com.fxz.common.core.enums.RoleAdminEnum;
import com.fxz.common.core.exception.FxzException;
import com.fxz.common.dataPermission.enums.DataScopeEnum;
import com.fxz.common.tenant.context.TenantContextHolder;
import com.fxz.common.tenant.util.TenantUtils;
import com.fxz.system.entity.Role;
import com.fxz.system.entity.SystemUser;
import com.fxz.system.entity.Tenant;
import com.fxz.system.entity.TenantPackage;
import com.fxz.system.mapper.TenantMapper;
import com.fxz.system.param.TenantParam;
import com.fxz.system.service.TenantPackageService;
import com.fxz.system.service.TenantService;
import com.fxz.system.vo.TenantVO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * 租户表
 *
 * @author fxz
 * @date 2022-10-01
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class TenantServiceImpl extends ServiceImpl<TenantMapper, Tenant> implements TenantService {

	@Resource
	private TenantPackageService tenantPackageService;

	@Resource
	private UserServiceImpl userService;

	private final RoleServiceImpl roleService;

	/**
	 * 校验租户信息是否合法
	 * @param id 租户id
	 */
	@Override
	public void validTenant(Long id) {
		Tenant tenant = findById(id);

		if (Objects.isNull(tenant)) {
			throw new FxzException("租户信息不存在!");
		}
		if (tenant.getStatus().equals(GlobalStatusEnum.DISABLE.getValue())) {
			throw new FxzException(String.format("租户未开启:%s", tenant.getName()));
		}
		if (LocalDateTime.now().isAfter(tenant.getExpireTime())) {
			throw new FxzException("租户已经过期！");
		}
	}

	/**
	 * 获取所有租户id集合
	 * @return 所有租户id集合
	 */
	@Override
	public List<Long> getTenantIds() {
		return this.list().stream().map(Tenant::getId).collect(Collectors.toList());
	}

	/**
	 * 保存租户信息
	 * @param tenant 租户视图信息
	 */
	@Transactional(rollbackFor = Exception.class)
	@Override
	public Boolean addSysTenant(TenantVO tenant) {
		// 检查套餐信息
		TenantPackage tenantPackage = tenantPackageService.validTenantPackage(tenant.getPackageId());

		// 保存租户信息
		this.save(tenant);

		TenantUtils.run(tenant.getId(), () -> {
			// 根据套餐信息为新租户新建一个角色
			Long roleId = createRole(tenantPackage);

			// 为新租户创建一个默认账号
			Long userId = createUser(roleId, tenant);

			// 更新租户管理员id
			this.update(Wrappers.<Tenant>lambdaUpdate().eq(Tenant::getId, tenant.getId()).set(Tenant::getTenantAdminId,
					userId));
		});

		return Boolean.TRUE;
	}

	/**
	 * 根据角色id与租户信息创建一个默认账号
	 * @param roleId 角色id
	 * @param tenant 租户信息
	 * @return userId
	 */
	private Long createUser(Long roleId, TenantVO tenant) {
		// 创建用户
		SystemUser systemUser = new SystemUser().setUsername(tenant.getUsername()).setPassword(tenant.getPassword())
				.setMobile(tenant.getTenantAdminMobile()).setUsername(tenant.getTenantAdminName())
				.setRoleId(String.valueOf(roleId));

		return userService.createUser(systemUser).getUserId();
	}

	/**
	 * 根据套餐生成一个角色信息
	 * @param tenantPackage 租户套餐
	 * @return 角色信息
	 */
	private Long createRole(TenantPackage tenantPackage) {
		// 生成租户管理员角色角色
		Role role = new Role().setRoleName(RoleAdminEnum.TENANT_ADMIN.getDescription())
				.setCode(RoleAdminEnum.TENANT_ADMIN.getType()).setRemark("系统生成租户管理员角色")
				.setMenuId(tenantPackage.getMenuIds()).setDataScope(DataScopeEnum.ALL.getScope());

		return roleService.addRole(role).getRoleId();
	}

	/**
	 * 根据id查询租户信息
	 * @param id 租户id
	 * @return 租户信息
	 */
	@Override
	public Tenant findById(Long id) {
		return this.getById(id);
	}

	/**
	 * 根据name查询租户信息
	 */
	@Override
	public Long findTenantIdById(String name) {
		Tenant tenant = this.getOne(Wrappers.<Tenant>lambdaQuery().eq(Tenant::getName, name).last("limit 1"));
		if (Objects.isNull(tenant)) {
			return null;
		}

		return tenant.getId();
	}

	/**
	 * 删除租户
	 */
	@Override
	public Boolean deleteSysTenant(Long id) {
		if (isSystemTenant(id)) {
			throw new FxzException("系统内置租户，不允许删除");
		}

		this.removeById(id);
		return Boolean.TRUE;
	}

	/**
	 * 获取指定套餐下的所有租户
	 * @param packageId 套餐id
	 * @return 指定套餐下的所有租户
	 */
	@Override
	public List<Tenant> getTenantListByPackageId(Long packageId) {
		return this.list(Wrappers.<Tenant>lambdaQuery().eq(Tenant::getPackageId, packageId));
	}

	/**
	 * 更新指定租户的角色菜单信息
	 * @param id 租户id
	 * @param menus 菜单信息
	 */
	@Override
	public void updateTenantRoleMenu(Long id, List<String> menus) {
		TenantUtils.run(id, () -> {
			// 本租户下的所有角色
			List<Role> roleList = roleService.list();

			roleList.forEach(r -> {
				if (Objects.equals(r.getCode(), RoleAdminEnum.TENANT_ADMIN.getType())) {
					// 租户管理员 则直接赋值新菜单
					r.setMenuId(String.join(StrPool.COMMA, menus));
				}
				else {
					// 非租户管理员 则原菜单和现菜单取交集
					r.setMenuId(String.join(StrPool.COMMA,
							CollUtil.intersectionDistinct(menus, Arrays.asList(r.getMenuId().split(StrPool.COMMA)))));
				}

				// 更新角色信息
				roleService.editRole(r);
			});
		});
	}

	/**
	 * 校验租户下账号数量
	 */
	@Override
	public void validCount() {
		long count = userService.count();
		Long tenantId = TenantContextHolder.getTenantId();
		Tenant tenant = this.getById(tenantId);

		if (Objects.isNull(tenant) || count > tenant.getAccountCount()) {
			throw new FxzException("租户账号数量超过额度！");
		}
	}

	/**
	 * 校验租户是否是系统租户
	 * @param id 租户id 我们任务租户id为0时为系统内置租户 不允许删除
	 * @return 是否是系统租户
	 */
	private boolean isSystemTenant(Long id) {
		return Objects.equals(id, Tenant.PACKAGE_ID_SYSTEM);
	}

	/**
	 * 修改租户信息
	 */
	@Override
	public Boolean updateSysTenant(Tenant tenant) {
		this.updateById(tenant);
		return Boolean.TRUE;
	}

	/**
	 * 分页查询租户信息
	 */
	@Override
	public IPage<Tenant> pageSysTenant(Page pageParam, TenantParam param) {
		return this.page(pageParam, param.lambdaQuery());
	}

	/**
	 * 获取全部
	 */
	@Override
	public List<Tenant> findAll() {
		return this.list(Wrappers.emptyWrapper());
	}

}
