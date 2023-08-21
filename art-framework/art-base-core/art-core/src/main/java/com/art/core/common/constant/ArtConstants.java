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

package com.art.core.common.constant;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

/**
 * @author Fxz
 * @version 0.0.1
 * @date 2021-11-28 13:10
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class ArtConstants {

	/**
	 * GATEWAY请求头TOKEN名称（不要有空格）
	 */
	public static final String GATEWAY_TOKEN_HEADER = "GatewayToken";

	/**
	 * GATEWAY请求头TOKEN值
	 */
	public static final String GATEWAY_TOKEN_VALUE = "fxz:gateway:$**$";

}
