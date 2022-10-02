package com.fxz.system.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fxz.common.core.enums.GlobalStatusEnum;
import com.fxz.common.core.exception.FxzException;
import com.fxz.system.entity.TenantPackage;
import com.fxz.system.mapper.TenantPackageMapper;
import com.fxz.system.service.TenantPackageService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

/**
 * 租户套餐表
 *
 * @author fxz
 * @date 2022-10-01
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class TenantPackageServiceImpl extends ServiceImpl<TenantPackageMapper, TenantPackage>
		implements TenantPackageService {

	private final TenantPackageMapper tenantPackageMapper;

	/**
	 * 添加
	 */
	@Override
	public Boolean addTenantPackage(TenantPackage tenantPackage) {
		tenantPackageMapper.insert(tenantPackage);
		return Boolean.TRUE;
	}

	/**
	 * 修改
	 */
	@Override
	public Boolean updateTenantPackage(TenantPackage tenantPackage) {
		tenantPackageMapper.updateById(tenantPackage);
		return Boolean.TRUE;
	}

	/**
	 * 分页
	 */
	@Override
	public IPage<TenantPackage> pageTenantPackage(Page<TenantPackage> pageParam, TenantPackage tenantPackage) {
		return tenantPackageMapper.selectPage(pageParam, Wrappers.emptyWrapper());
	}

	/**
	 * 获取单条
	 */
	@Override
	public TenantPackage findById(Long id) {
		return tenantPackageMapper.selectById(id);
	}

	/**
	 * 获取全部
	 */
	@Override
	public List<TenantPackage> findAll() {
		return tenantPackageMapper.selectList(Wrappers.emptyWrapper());
	}

	/**
	 * 删除
	 */
	@Override
	public Boolean deleteTenantPackage(Long id) {
		tenantPackageMapper.deleteById(id);
		return Boolean.TRUE;
	}

	/**
	 * 校验套餐信息
	 * @param packageId 套餐id
	 * @return 套餐信息
	 */
	@Override
	public TenantPackage validTenantPackage(Long packageId) {
		TenantPackage tenantPackage = this.getById(packageId);

		if (Objects.isNull(tenantPackage)) {
			throw new FxzException("租户套餐不存在！");
		}
		else if (GlobalStatusEnum.DISABLE.getValue().equals(tenantPackage.getStatus())) {
			throw new FxzException("套餐未开启！");
		}

		return tenantPackage;
	}

}