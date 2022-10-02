package com.fxz.common.tenant.security;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.extra.servlet.ServletUtil;
import com.fxz.common.jackson.util.JacksonUtil;
import com.fxz.common.mp.result.Result;
import com.fxz.common.security.entity.FxzAuthUser;
import com.fxz.common.security.util.SecurityUtil;
import com.fxz.common.tenant.config.FxzTenantProperties;
import com.fxz.common.tenant.context.TenantContextHolder;
import com.fxz.common.tenant.service.TenantValidService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Objects;

/**
 * 用户租户信息合法性校验拦截器
 *
 * @author Fxz
 * @version 1.0
 * @date 2022/10/1 12:11
 */
@Slf4j
public class TenantSecurityWebFilter extends OncePerRequestFilter {

	private final FxzTenantProperties tenantProperties;

	private final AntPathMatcher pathMatcher;

	private final TenantValidService tenantFrameworkService;

	public TenantSecurityWebFilter(FxzTenantProperties tenantProperties, TenantValidService tenantValidService) {
		this.pathMatcher = new AntPathMatcher();
		this.tenantProperties = tenantProperties;
		this.tenantFrameworkService = tenantValidService;
	}

	@SuppressWarnings("deprecation")
	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
			throws ServletException, IOException {
		// 线程上下文中存储的租户id
		Long tenantId = TenantContextHolder.getTenantId();

		if (!skipDispatch(request)) {
			// 当前url不放行且未带租户的编号 不允许访问
			if (Objects.isNull(tenantId)) {
				log.info("未传递租户编号");
				ServletUtil.write(response, JacksonUtil.toJsonString(Result.failed("租户信息未传递!")),
						MediaType.APPLICATION_JSON_UTF8_VALUE);
				return;
			}

			try {
				// 校验租户是否合法
				tenantFrameworkService.validTenant(tenantId);
			}
			catch (Throwable ex) {
				ServletUtil.write(response, JacksonUtil.toJsonString(ex.getLocalizedMessage()),
						MediaType.APPLICATION_JSON_UTF8_VALUE);
				return;
			}

			FxzAuthUser user = SecurityUtil.getUser();
			if (Objects.isNull(user.getTenantId()) || !Objects.equals(tenantId, user.getTenantId())) {
				ServletUtil.write(response, JacksonUtil.toJsonString(Result.failed("无权访问该租户!")),
						MediaType.APPLICATION_JSON_UTF8_VALUE);
				return;
			}
		}
		else {
			// 当前url放行且没携带租户id
			if (Objects.isNull(tenantId)) {
				TenantContextHolder.setIgnore(true);
			}
		}

		chain.doFilter(request, response);
	}

	/**
	 * 当前访问路径是否放行
	 * @param request 当前请求
	 * @return 是否放行
	 */
	private boolean skipDispatch(HttpServletRequest request) {
		if (CollUtil.contains(tenantProperties.getIgnoreUrls(), request.getRequestURI())) {
			return true;
		}

		String uri = request.getRequestURI();
		return tenantProperties.getIgnoreUrls().stream().anyMatch(isIgnoreUrl -> pathMatcher.match(isIgnoreUrl, uri));
	}

}
