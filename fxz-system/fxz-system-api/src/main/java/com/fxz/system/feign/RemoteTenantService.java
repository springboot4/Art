package com.fxz.system.feign;

import com.fxz.common.core.constant.FxzServerConstant;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

/**
 * @author Fxz
 * @version 1.0
 * @date 2022-04-07 14:50
 */
@FeignClient(contextId = "remoteTenantService", value = FxzServerConstant.FXZ_SERVER_SYSTEM)
public interface RemoteTenantService {

	/**
	 * 校验租户信息是否合法
	 * @param id 租户id
	 */
	@GetMapping(value = "/tenant/validTenant/{id}")
	void validTenant(@PathVariable("id") Long id);

}
