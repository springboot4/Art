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

package com.art.job.sdk.constants;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * 任务调度通用常量
 *
 * @author fxz
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class ScheduleConstants {

	public static final String PARAMETER = "PARAMETER";

	public static final String JOB_BEAN_NAME = "JOB_BEAN_NAME";

	public static final String TASK_CLASS_NAME = "TASK_CLASS_NAME";

	/**
	 * 默认
	 */
	public static final String MISFIRE_DEFAULT = "0";

	/**
	 * 立即触发执行
	 */
	public static final String MISFIRE_IGNORE_MISFIRES = "1";

	/**
	 * 触发一次执行
	 */
	public static final String MISFIRE_FIRE_AND_PROCEED = "2";

	/**
	 * 不触发立即执行
	 */
	public static final String MISFIRE_DO_NOTHING = "3";

	/**
	 * 定时任务状态
	 */
	@Getter
	public enum Status {

		/**
		 * 正常
		 */
		NORMAL("0"),

		/**
		 * 暂停
		 */
		PAUSE("1");

		private final String value;

		Status(String value) {
			this.value = value;
		}

	}

}
