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

package com.art.common.security.client.core.constants;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author fxz
 * @version 0.0.1
 * @date 2023/4/16 12:51
 */
@AllArgsConstructor
public enum GiteeScopeEnums {

	USER_INFO("user_info"),

	PROJECTS("projects"),

	PULL_REQUESTS("pull_requests"),

	ISSUES("issues"),

	NOTES("notes"),

	KEYS("keys"),

	HOOK("hook"),

	GROUPS("groups"),

	GISTS("gists"),

	ENTERPRISES("enterprises");

	@Getter
	private final String value;

	public static List<String> legalScope() {
		return Arrays.stream(values()).map(GiteeScopeEnums::getValue).collect(Collectors.toList());
	}

}
