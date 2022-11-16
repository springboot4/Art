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

package com.art.common.tenant.util;

import com.art.common.tenant.context.TenantContextHolder;

import java.util.concurrent.Callable;

/**
 * 多租户操作工具类
 *
 * @author Fxz
 * @version 0.0.1
 * @date 2022/10/1 12:11
 */
public class TenantUtils {

	/**
	 * 指定租户 执行对应的逻辑(有返回值)
	 * @param tenantId 租户编号
	 * @param callable 逻辑
	 */
	public static <T> T call(Long tenantId, Callable<T> callable) {
		// 原有租户信息
		Long oldTenantId = TenantContextHolder.getTenantId();
		Boolean oldIgnore = TenantContextHolder.isIgnore();

		try {
			TenantContextHolder.setTenantId(tenantId);
			TenantContextHolder.setIgnore(false);

			// 执行逻辑
			return callable.call();
		}
		catch (Exception e) {
			throw new RuntimeException(e);
		}
		finally {
			// 恢复现场
			TenantContextHolder.setTenantId(oldTenantId);
			TenantContextHolder.setIgnore(oldIgnore);
		}
	}

	/**
	 * 指定租户 执行对应的逻辑(无返回值)
	 * @param tenantId 租户编号
	 * @param runnable 逻辑
	 */
	public static void run(Long tenantId, Runnable runnable) {
		// 原有租户信息
		Long oldTenantId = TenantContextHolder.getTenantId();
		Boolean oldIgnore = TenantContextHolder.isIgnore();

		try {
			TenantContextHolder.setTenantId(tenantId);
			TenantContextHolder.setIgnore(false);

			// 执行逻辑
			runnable.run();
		}
		finally {
			// 恢复现场
			TenantContextHolder.setTenantId(oldTenantId);
			TenantContextHolder.setIgnore(oldIgnore);
		}
	}

}
