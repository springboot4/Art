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

package com.art.system.core.configure;

import com.art.common.dataPermission.dept.rule.DeptDataPermissionRuleCustomizer;
import com.art.system.dao.dataobject.DeptDO;
import com.art.system.dao.dataobject.OperLogDO;
import com.art.system.dao.dataobject.SystemUserDO;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * system 模块的数据权限 Configuration
 *
 * @author fxz
 */
@Configuration(proxyBeanMethods = false)
public class DataPermissionConfiguration {

	@Bean
	public DeptDataPermissionRuleCustomizer sysDeptDataPermissionRuleCustomizer() {
		return rule -> {
			// dept
			rule.addDeptColumn(DeptDO.class);
			rule.addDeptColumn(OperLogDO.class);
			// user
			rule.addUserColumn(SystemUserDO.class, "user_id");
		};
	}

}
