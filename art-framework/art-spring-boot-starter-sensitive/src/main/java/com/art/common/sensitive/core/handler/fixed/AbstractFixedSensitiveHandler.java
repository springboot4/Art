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

package com.art.common.sensitive.core.handler.fixed;

import com.art.common.sensitive.core.handler.base.SensitiveHandler;

import java.lang.annotation.Annotation;

/**
 * @author fxz
 */
public abstract class AbstractFixedSensitiveHandler<T extends Annotation> implements SensitiveHandler<T> {

	@Override
	public String desensitize(String origin, T annotation) {
		int front = getFront(annotation);
		int end = getEnd(annotation);
		String replacer = getReplacer(annotation);
		int length = origin.length();

		if (front >= length || end >= length || (front + end) >= length) {
			return buildReplacerByLength(replacer, length);
		}

		int interval = length - front - end;
		return origin.substring(0, front) + buildReplacerByLength(replacer, interval)
				+ origin.substring(front + interval);
	}

	/**
	 * 根据长度循环构建替换符
	 * @param replacer 替换符
	 * @param length 长度
	 * @return 构建后的替换符
	 */
	private String buildReplacerByLength(String replacer, int length) {
		StringBuilder builder = new StringBuilder();
		for (int i = 0; i < length; i++) {
			builder.append(replacer);
		}
		return builder.toString();
	}

	/**
	 * 前缀保留长度
	 * @param annotation 注解信息
	 * @return 前缀保留长度
	 */
	abstract Integer getFront(T annotation);

	/**
	 * 后缀保留长度
	 * @param annotation 注解信息
	 * @return 后缀保留长度
	 */
	abstract Integer getEnd(T annotation);

	/**
	 * 替换符
	 * @param annotation 注解信息
	 * @return 替换符
	 */
	abstract String getReplacer(T annotation);

}
