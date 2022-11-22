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

package com.art.common.core.util;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.CollectionUtil;
import lombok.experimental.UtilityClass;

import javax.validation.*;
import java.util.List;
import java.util.Set;

/**
 * 参数校验工具类
 *
 * @author Fxz
 * @version 0.0.1
 * @date 2022/10/29 17:05
 */
@UtilityClass
public class ValidationUtil {

	private static final ValidatorFactory VALIDATOR_FACTORY = Validation.buildDefaultValidatorFactory();

	/**
	 * 验证参数对象 验证失败则抛出异常
	 */
	public void validateParam(Object object, Class<?>... groups) {
		Validator validator = VALIDATOR_FACTORY.getValidator();
		Set<ConstraintViolation<Object>> constraintViolations = validator.validate(object, groups);
		if (CollUtil.isNotEmpty(constraintViolations)) {
			throw new ConstraintViolationException(constraintViolations);
		}
	}

	/**
	 * 验证参数对象 如果验证失败则返回所有失败信息
	 */
	public String validate(Object object, Class<?>... groups) {
		Validator validator = VALIDATOR_FACTORY.getValidator();
		Set<ConstraintViolation<Object>> violations = validator.validate(object, groups);

		StringBuilder message = new StringBuilder();
		for (ConstraintViolation<Object> violation : violations) {
			message.append(violation.getMessage()).append(System.lineSeparator());
		}

		return message.toString();
	}

	/**
	 * 验证参数对象 验证失败则抛出异常
	 */
	public void validateListObject(List<?> list, Class<?>... groups) {
		if (CollectionUtil.isEmpty(list)) {
			return;
		}

		Validator validator = VALIDATOR_FACTORY.getValidator();
		list.forEach(object -> {
			Set<ConstraintViolation<Object>> constraintViolations = validator.validate(object, groups);
			if (CollUtil.isNotEmpty(constraintViolations)) {
				throw new ConstraintViolationException(constraintViolations);
			}
		});
	}

}
