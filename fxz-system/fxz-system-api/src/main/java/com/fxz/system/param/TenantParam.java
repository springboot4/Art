package com.fxz.system.param;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.fxz.system.entity.Tenant;

import java.util.Objects;

/**
 * 租户查询参数
 *
 * @author Fxz
 * @version 1.0
 * @date 2022/10/3 12:41
 */
public class TenantParam extends Tenant {

	public LambdaQueryWrapper<Tenant> lambdaQuery() {
		return Wrappers.<Tenant>lambdaQuery().eq(Objects.nonNull(this.getId()), Tenant::getId, this.getId())
				.eq(Objects.nonNull(this.getStatus()), Tenant::getStatus, this.getStatus())
				.like(StrUtil.isNotBlank(this.getName()), Tenant::getName, this.getName());
	}

}
