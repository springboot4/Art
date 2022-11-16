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

package com.art.common.dataPermission.factory;

import com.art.common.dataPermission.rule.DataPermissionRule;

import java.util.List;

/**
 * 数据权限规则工厂接口 管理容器中配置的数据权限规则
 *
 * @author fxz
 */
public interface DataPermissionRuleFactory {

	/**
	 * 获取生效的数据权限规则
	 * @return 生效的数据权限规则数组
	 */
	List<DataPermissionRule> getDataPermissionRule();

}
