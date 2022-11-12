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

package com.fxz.common.sequence.segment;

import lombok.Data;

/**
 * 序列号号段配置
 *
 * @author Fxz
 * @version 1.0
 * @date 2022/5/23 09:48
 */
@Data
public class SeqSegmentConfig {

	/**
	 * 序列生成器步长
	 */
	private Integer step = 1;

	/**
	 * 序列生成器号段步长
	 */
	private Integer rangeStep = 1000;

	/**
	 * 序列生成器号段起始位置
	 */
	private Long rangeStart = 0L;

}
