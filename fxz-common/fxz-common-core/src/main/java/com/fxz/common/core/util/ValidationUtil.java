package com.fxz.common.core.util;

import cn.hutool.core.collection.CollUtil;
import lombok.experimental.UtilityClass;

import javax.validation.*;
import java.util.Set;

/**
 * 参数校验工具类
 *
 * @author Fxz
 * @version 1.0
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
	 * 验证参数对象，如果验证失败则返回所有失败信息
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

}
