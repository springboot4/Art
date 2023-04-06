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

package com.art.system.api.dept;

import com.art.common.core.constant.FxzServerConstant;
import com.art.common.core.model.Result;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * @author Fxz
 * @version 0.0.1
 * @date 2022-04-07 14:50
 */
@FeignClient(contextId = "deptServiceApi", value = FxzServerConstant.ART_SERVER_SYSTEM)
public interface DeptServiceApi {

	/**
	 * 获取当前用户的部门名称
	 */
	@GetMapping(value = "/dept/getDeptNameByUserId")
	Result<String> getDeptAllByUserId();

}
