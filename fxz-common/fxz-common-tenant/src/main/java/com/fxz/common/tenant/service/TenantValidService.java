package com.fxz.common.tenant.service;

import com.fxz.common.core.exception.FxzException;
import com.fxz.system.feign.RemoteTenantService;
import com.github.benmanes.caffeine.cache.Caffeine;
import com.github.benmanes.caffeine.cache.LoadingCache;

import java.util.Objects;
import java.util.concurrent.TimeUnit;

/**
 * 租户信息合法性校验
 *
 * @author fxz
 */
public class TenantValidService {

	private final LoadingCache<Long, FxzException> validTenantCache;

	private static final FxzException SERVICE_EXCEPTION_NULL = new FxzException("无异常信息");

	public TenantValidService(RemoteTenantService remoteTenantService) {
		this.validTenantCache = Caffeine.newBuilder().expireAfterAccess(1, TimeUnit.MINUTES)
				.expireAfterWrite(1, TimeUnit.MINUTES).build(id -> {
					try {
						remoteTenantService.validTenant(id);
						return SERVICE_EXCEPTION_NULL;
					}
					catch (FxzException e) {
						return e;
					}
				});

	}

	public void validTenant(Long id) {
		FxzException serviceException = validTenantCache.get(id);

		if (!Objects.equals(serviceException, SERVICE_EXCEPTION_NULL) && Objects.nonNull(serviceException)) {
			throw serviceException;
		}
	}

}
