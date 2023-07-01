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

package com.art.common.sensitive.core.annotation.regex;

import com.art.common.sensitive.core.annotation.base.SensitiveInfo;
import com.art.common.sensitive.core.handler.regex.EmailSensitiveHandler;
import com.fasterxml.jackson.annotation.JacksonAnnotationsInside;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author fxz
 */
@Target({ ElementType.FIELD })
@Retention(RetentionPolicy.RUNTIME)
@JacksonAnnotationsInside
@SensitiveInfo(handler = EmailSensitiveHandler.class)
public @interface EmailSensitive {

	/**
	 * 匹配的正则
	 */
	String regex() default "(^.)[^@]*(@.*$)";

	/**
	 * 替换规则
	 */
	String replacer() default "$1****$2";

}