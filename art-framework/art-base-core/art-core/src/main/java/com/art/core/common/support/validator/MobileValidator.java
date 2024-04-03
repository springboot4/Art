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

package com.art.core.common.support.validator;

import cn.hutool.core.util.ReUtil;
import com.art.core.common.annotation.CheckMobileValid;
import com.art.core.common.constant.RegexpConstants;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.apache.commons.lang3.StringUtils;

/**
 * @author fxz
 */
public class MobileValidator implements ConstraintValidator<CheckMobileValid, String> {

	@Override
	public void initialize(CheckMobileValid isMobile) {
	}

	@Override
	public boolean isValid(String s, ConstraintValidatorContext constraintValidatorContext) {
		return StringUtils.isBlank(s) || ReUtil.isMatch(RegexpConstants.MOBILE_REG, s);
	}

}