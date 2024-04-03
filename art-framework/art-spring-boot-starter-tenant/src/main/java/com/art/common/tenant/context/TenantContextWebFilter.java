/*
 *   COPYRIGHT (C) 2023 Art AUTHORS(fxzcloud@gmail.com). ALL RIGHTS RESERVED.
 *
 *   Licensed under the Apache License, Version 2.0 (the "License");
 *   you may not use this file except in compliance with the License.
 *   You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 *   Unless required by applicable law or agreed to in writing, software
 *   distributed under the License is distributed on an "AS IS" BASIS,
 *   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *   See the License for the specific language governing permissions and
 *   limitations under the License.
 */

package com.art.common.tenant.context;

import com.art.common.security.core.utils.SecurityUtil;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Objects;

/**
 * 多租户上下文过滤器
 *
 * @author Fxz
 * @version 0.0.1
 * @date 2022/10/1 11:09
 */
public class TenantContextWebFilter extends OncePerRequestFilter {

	/**
	 * 保存租户信息到上下文
	 */
	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
			throws ServletException, IOException {
		// 获取请求携带的租户id
		Long tenantId = SecurityUtil.getTenantId(request);

		if (Objects.nonNull(tenantId)) {
			// 保存租户id到上下文
			TenantContextHolder.setTenantId(tenantId);
		}

		try {
			chain.doFilter(request, response);
		}
		finally {
			// 清理
			TenantContextHolder.clear();
		}
	}

}