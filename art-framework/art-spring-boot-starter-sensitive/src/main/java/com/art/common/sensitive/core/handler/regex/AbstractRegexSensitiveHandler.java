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

package com.art.common.sensitive.core.handler.regex;

import com.art.common.sensitive.core.handler.base.SensitiveHandler;

import java.lang.annotation.Annotation;

/**
 * @author fxz
 */
public abstract class AbstractRegexSensitiveHandler<T extends Annotation> implements SensitiveHandler<T> {

	@Override
	public String desensitize(String origin, T annotation) {
		String regex = getRegex(annotation);
		String replacer = getReplacer(annotation);
		return origin.replaceAll(regex, replacer);
	}

	/**
	 * 获取注解上的 regex 参数
	 * @param annotation 注解信息
	 * @return 正则表达式
	 */
	abstract String getRegex(T annotation);

	/**
	 * 获取注解上的 replacer 参数
	 * @param annotation 注解信息
	 * @return 待替换的字符串
	 */
	abstract String getReplacer(T annotation);

}