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

package com.art.common.xss.config;

import com.art.common.xss.core.filter.XssFilter;
import com.art.common.xss.core.propertie.XssProperties;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.util.PathMatcher;

/**
 * @author Fxz
 * @version 0.0.1
 * @date 2022/12/8 10:53
 */
@AutoConfiguration
public class XssAutoConfig {

	@Bean
	public XssProperties xssProperties() {
		return new XssProperties();
	}

	@Bean
	public FilterRegistrationBean<XssFilter> xssFilter(XssProperties properties, PathMatcher pathMatcher) {
		FilterRegistrationBean<XssFilter> registrationBean = new FilterRegistrationBean<>();
		registrationBean.setFilter(new XssFilter(properties, pathMatcher));
		registrationBean.setOrder(-105);
		return registrationBean;
	}

}
