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

package com.art.mq.common.message;

import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * 分组消息，此消息对于同一个消费者组仅会被消费到一次
 *
 * @author fxz
 * @version 0.0.1
 * @date 2023/6/30 14:43
 */
public abstract class AbstractGroupMessage extends AbstractMessage {

	/**
	 * 获取消费者组标识
	 * @return 消费者组标识
	 */
	@JsonIgnore
	public abstract String getGroup();

}
