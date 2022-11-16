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

package com.art.common.dataPermission.dept.rule;

/**
 * 数据权限规则的自定义配置接口 用于对数据权限的规则列进行配置
 *
 * @author fxz
 */
@FunctionalInterface
public interface DeptDataPermissionRuleCustomizer {

	/**
	 * 自定义该权限规则
	 * <p/>
	 * 1. 调用 {@link DeptDataPermissionRule#addDeptColumn(Class, String)} 方法 配置基于部门列的过滤规则
	 * <p/>
	 * 2. 调用 {@link DeptDataPermissionRule#addUserColumn(Class, String)} 方法 配置基于用户列的过滤规则
	 * @param rule 权限规则
	 */
	void customize(DeptDataPermissionRule rule);

}
