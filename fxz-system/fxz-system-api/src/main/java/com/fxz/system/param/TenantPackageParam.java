package com.fxz.system.param;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.fxz.system.entity.TenantPackage;

import java.util.Objects;

/**
 * 租户套餐查询参数
 *
 * @author Fxz
 * @version 1.0
 * @date 2022/10/3 12:49
 */
public class TenantPackageParam extends TenantPackage {

	public LambdaQueryWrapper<TenantPackage> lambdaQuery() {
		return Wrappers.<TenantPackage>lambdaQuery()
				.eq(Objects.nonNull(this.getId()), TenantPackage::getId, this.getId())
				.eq(Objects.nonNull(this.getStatus()), TenantPackage::getStatus, this.getStatus())
				.like(StrUtil.isNotBlank(this.getName()), TenantPackage::getName, this.getName());
	}

}
