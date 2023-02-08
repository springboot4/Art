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

package com.art.system.dao.redis.role;

import com.art.common.core.constant.CacheConstants;

/**
 * @author Fxz
 * @version 0.0.1
 * @date 2022/11/26 12:00
 */
public interface RoleRedisConstants {

	String PREFIX = "role:";

	String CACHE_NAMES = CacheConstants.GLOBALLY + PREFIX;

	String IS_SUPER_ADMIN = CACHE_NAMES + "isSuperAdmin:";

	String DATA_SCOPE = CACHE_NAMES + "dataScope:";

}
