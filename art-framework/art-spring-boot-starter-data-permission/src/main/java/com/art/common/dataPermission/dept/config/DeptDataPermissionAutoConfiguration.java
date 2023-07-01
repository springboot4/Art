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

package com.art.common.dataPermission.dept.config;

import com.art.common.dataPermission.dept.rule.DeptDataPermissionRule;
import com.art.common.dataPermission.dept.rule.DeptDataPermissionRuleCustomizer;
import com.art.common.dataPermission.dept.service.DeptDataPermissionService;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.context.annotation.Bean;

import java.util.List;

/**
 * 系统内置基于部门的数据权限的AutoConfiguration
 *
 * @author fxz
 */
@AutoConfiguration
public class DeptDataPermissionAutoConfiguration {

	/**
	 * 构建部门数据权限规则对象
	 * @param service 数据权限service对象
	 * @param customizers 容器中自定义的部门数据权限规则集合
	 * @return 数据权限对象
	 */
	@Bean
	public DeptDataPermissionRule deptDataPermissionRule(DeptDataPermissionService service,
			List<DeptDataPermissionRuleCustomizer> customizers) {
		// 创建数据权限规则对象
		DeptDataPermissionRule rule = new DeptDataPermissionRule(service);
		// 根据配置的自定义规则 补全部门数据权限的表信息
		customizers.forEach(customizer -> customizer.customize(rule));
		return rule;
	}

}
