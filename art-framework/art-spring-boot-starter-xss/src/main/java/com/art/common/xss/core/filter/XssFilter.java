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

package com.art.common.xss.core.filter;

import com.art.common.xss.core.propertie.XssProperties;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.util.PathMatcher;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

/**
 * @author Fxz
 * @version 0.0.1
 * @date 2022/12/8 10:56
 */
@RequiredArgsConstructor
public class XssFilter extends OncePerRequestFilter {

	private final XssProperties xssProperties;

	private final PathMatcher pathMatcher;

	@Override
	protected boolean shouldNotFilter(HttpServletRequest request) {
		if (!xssProperties.isEnable()) {
			return true;
		}

		return xssProperties.getIgnoreUrls()
			.stream()
			.anyMatch(excludeUrl -> pathMatcher.match(excludeUrl, request.getRequestURI()));
	}

	/**
	 * @param request
	 * @param response
	 * @param filterChain
	 * @throws ServletException
	 * @throws IOException
	 */
	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		filterChain.doFilter(new XssRequestWrapper(request), response);
	}

}
