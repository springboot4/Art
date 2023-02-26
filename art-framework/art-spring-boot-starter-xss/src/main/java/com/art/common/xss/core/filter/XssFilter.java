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

package com.art.common.xss.core.filter;

import com.art.common.xss.core.propertie.XssProperties;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.util.PathMatcher;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.http.HttpServletRequest;

/**
 * @author Fxz
 * @version 0.0.1
 * @date 2022/12/8 10:56
 */
@RequiredArgsConstructor
public class XssFilter extends OncePerRequestFilter {

	private final XssProperties xssProperties;

	private final PathMatcher pathMatcher;

	@SneakyThrows
	@Override
	protected void doFilterInternal(javax.servlet.http.HttpServletRequest request,
			javax.servlet.http.HttpServletResponse response, javax.servlet.FilterChain filterChain) {
		filterChain.doFilter(new XssRequestWrapper(request), response);
	}

	@Override
	protected boolean shouldNotFilter(HttpServletRequest request) {
		if (!xssProperties.isEnable()) {
			return true;
		}

		return xssProperties.getIgnoreUrls()
			.stream()
			.anyMatch(excludeUrl -> pathMatcher.match(excludeUrl, request.getRequestURI()));
	}

}
