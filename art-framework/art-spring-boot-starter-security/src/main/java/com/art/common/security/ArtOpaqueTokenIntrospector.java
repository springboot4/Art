/*
 * COPYRIGHT (C) 2023 Art AUTHORS(fxzcloud@gmail.com). ALL RIGHTS RESERVED.
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

package com.art.common.security;

import com.art.common.security.entity.ArtAuthUser;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.oauth2.core.OAuth2AuthenticatedPrincipal;
import org.springframework.security.oauth2.server.resource.introspection.OpaqueTokenIntrospector;

import java.util.List;

/**
 * @author fxz
 */
@Slf4j
public class ArtOpaqueTokenIntrospector implements OpaqueTokenIntrospector {

	static String sb = "dept,order:list,product:brand,product:category,product:good,product:goods,promotion:coupon:activity,promotion:coupon:list,promotion:seckill,seckill:manage,sys,sys:app,sys:dict,sys:doc,sys:file,sys:menu,sys:menu:save,sys:oper,sys:oper:log,sys:oper:login,sys:post,sys:role,sys:route,sys:tenant,sys:tenant:index,sys:tenant:package,sys:token,sys:user,sys:user:add,sys:user:update,sys:user:view,sysMonitor,sysMonitor:job,sysMonitor:jobLog,system,sysTool,sysTool:datasourceConf,sysTool:formBuild,sysTool:genCode,user,user:member,welcome";

	protected final OAuth2AuthenticatedPrincipal retrieveUser() {
		String[] split = sb.split(",");
		List<GrantedAuthority> authorityList = AuthorityUtils.createAuthorityList(split);
		return new ArtAuthUser("fxz", "{noop}123456", authorityList).setTenantId(0L).setUserId(1L);
	}

	/**
	 * @param token the token to introspect
	 * @return
	 */
	@Override
	public OAuth2AuthenticatedPrincipal introspect(String token) {
		log.info("根据token查询缓存：{}", token);
		return retrieveUser();
	}

}
