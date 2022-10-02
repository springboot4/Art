package com.fxz.common.tenant.util;

import com.fxz.common.tenant.context.TenantContextHolder;

import java.util.concurrent.Callable;

/**
 * 多租户操作工具类
 *
 * @author Fxz
 * @version 1.0
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
