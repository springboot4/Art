package com.fxz.common.tenant.context;

import feign.RequestInterceptor;
import feign.RequestTemplate;
import lombok.extern.slf4j.Slf4j;

/**
 * 传递feign租户请求头
 *
 * @author Fxz
 * @version 1.0
 * @date 2022/10/1 11:09
 */
@Slf4j
public class FeignTenantInterceptor implements RequestInterceptor {

	@Override
	public void apply(RequestTemplate requestTemplate) {
		requestTemplate.header("TENANT-ID", TenantContextHolder.getTenantId().toString());
	}

}