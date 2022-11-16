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

package com.art.common.core.validator;

import com.art.common.core.annotation.CheckMobileValid;
import com.art.common.core.constant.RegexpConstant;
import com.art.common.core.util.FxzUtil;
import org.apache.commons.lang3.StringUtils;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

/**
 * @author fxz
 */
public class MobileValidator implements ConstraintValidator<CheckMobileValid, String> {

	@Override
	public void initialize(CheckMobileValid isMobile) {
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