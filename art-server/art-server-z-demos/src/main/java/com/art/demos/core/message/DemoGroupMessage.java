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

package com.art.demos.core.message;

import com.art.common.hazelcast.core.mq.AbstractGroupMessage;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.time.LocalDateTime;

/**
 * @author Fxz
 * @version 0.0.1
 * @date 2023/3/24 21:52
 */
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class DemoGroupMessage extends AbstractGroupMessage {

	private String msg;

	private LocalDateTime dateTime;

	/**
	 * 获取消费者组
	 * @return 消费者组名称
	 */
	@Override
	public String getGroup() {
		return "DemoGroup";
	}

}
