package com.fxz.common.core.validator;

import com.fxz.common.core.annotation.IsMobile;
import com.fxz.common.core.entity.RegexpConstant;
import com.fxz.common.core.utils.FxzUtil;
import org.apache.commons.lang3.StringUtils;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class MobileValidator implements ConstraintValidator<IsMobile, String> {

	@Override
	public void initialize(IsMobile isMobile) {
	}

	@Override
	public boolean isValid(String s, ConstraintValidatorContext constraintValidatorContext) {
		try {
			if (StringUtils.isBlank(s)) {
				return true;
			}
			else {
				String regex = RegexpConstant.MOBILE_REG;
				return FxzUtil.match(regex, s);
			}
		}
		catch (Exception e) {
			return false;
		}
	}

}