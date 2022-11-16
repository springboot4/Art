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

package com.art.system.param;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.art.system.entity.Tenant;

import java.util.Objects;

/**
 * 租户查询参数
 *
 * @author Fxz
 * @version 0.0.1
 * @date 2022/10/3 12:41
 */
public class TenantParam extends Tenant {

	public LambdaQueryWrapper<Tenant> lambdaQuery() {
		return Wrappers.<Tenant>lambdaQuery().eq(Objects.nonNull(this.getId()), Tenant::getId, this.getId())
				.eq(Objects.nonNull(this.getStatus()), Tenant::getStatus, this.getStatus())
				.like(StrUtil.isNotBlank(this.getName()), Tenant::getName, this.getName());
	}

}
