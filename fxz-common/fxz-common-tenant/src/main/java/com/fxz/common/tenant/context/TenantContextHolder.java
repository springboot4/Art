package com.fxz.common.tenant.context;

import com.alibaba.ttl.TransmittableThreadLocal;

/**
 * 多租户上下文
 *
 * @author Fxz
 * @version 1.0
 * @date 2022/10/1 11:09
 */
public class TenantContextHolder {

	/**
	 * 租户id
	 */
	private static final ThreadLocal<Long> TENANT_THREAD_LOCAL = new TransmittableThreadLocal<>();

	/**
	 * 全局是否忽略多租户
	 */
	private static final ThreadLocal<Boolean> THREAD_IGNORE = new TransmittableThreadLocal<>();

	/**
	 * 获得租户id
	 * @return 租户id
	 */
	public static Long getTenantId() {
		return TENANT_THREAD_LOCAL.get();
	}

	/**
	 * 保存租户id
	 * @param tenantId 租户id
	 */
	public static void setTenantId(Long tenantId) {
		TENANT_THREAD_LOCAL.set(tenantId);
	}

	/**
	 * 设置是否忽略多租户
	 * @param ignore 是否忽略多租户
	 */
	public static void setIgnore(Boolean ignore) {
		THREAD_IGNORE.set(ignore);
	}

	/**
	 * 当前是否忽略租户
	 * @return true or false
	 */
	public static boolean isIgnore() {
		return Boolean.TRUE.equals(THREAD_IGNORE.get());
	}

	/**
	 * 清空上下文多租户信息
	 */
	public static void clear() {
		THREAD_IGNORE.remove();
		TENANT_THREAD_LOCAL.remove();
	}

}
