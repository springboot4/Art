package com.fxz.common.tenant.context;

import com.fxz.common.security.util.SecurityUtil;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Objects;

/**
 * 多租户上下文过滤器
 *
 * @author Fxz
 * @version 1.0
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