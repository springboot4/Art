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

package com.art.system.api.user.redis.user;

import com.art.core.common.constant.CacheConstants;

/**
 * @author Fxz
 * @version 0.0.1
 * @date 2022/11/26 12:00
 */
public interface UserRedisConstants {

	String PREFIX = "user:";

	String CACHE_NAMES = CacheConstants.GLOBALLY + PREFIX;

	String USER_INFO = CACHE_NAMES + "info:";

	String USER_DETAILS = CACHE_NAMES + "user_details:";

}
