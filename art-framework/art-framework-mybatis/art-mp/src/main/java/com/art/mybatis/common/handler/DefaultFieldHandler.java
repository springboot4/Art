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

package com.art.mybatis.common.handler;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import org.apache.ibatis.reflection.MetaObject;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.ClassUtils;

import java.nio.charset.Charset;
import java.time.LocalDateTime;
import java.util.Optional;

/**
 * 通用参数填充实现类 如果没有显式的对通用参数进行赋值，这里会对通用参数进行填充、赋值
 *
 * @author fxz
 */
public class DefaultFieldHandler implements MetaObjectHandler {

	@Override
	public void insertFill(MetaObject metaObject) {
		LocalDateTime now = LocalDateTime.now();

		fillValIfNullByName("createTime", now, metaObject, false);
		fillValIfNullByName("updateTime", now, metaObject, false);
		fillValIfNullByName("createBy", getUserName(), metaObject, false);
		fillValIfNullByName("updateBy", getUserName(), metaObject, false);
	}

	@Override
	public void updateFill(MetaObject metaObject) {
		fillValIfNullByName("updateTime", LocalDateTime.now(), metaObject, true);
		fillValIfNullByName("updateBy", getUserName(), metaObject, true);
	}

	/**
	 * @param fieldName 属性名
	 * @param fieldVal 属性值
	 * @param metaObject MetaObject
	 * @param isCover 是否覆盖原有值
	 */
	private static void fillValIfNullByName(String fieldName, Object fieldVal, MetaObject metaObject, boolean isCover) {
		// 1. 没有 get 方法
		if (!metaObject.hasSetter(fieldName)) {
			return;
		}
		// 2. 如果用户有手动设置的值
		Object userSetValue = metaObject.getValue(fieldName);
		String setValueStr = StrUtil.str(userSetValue, Charset.defaultCharset());
		if (StrUtil.isNotBlank(setValueStr) && !isCover) {
			return;
		}
		// 3. field 类型相同时设置
		Class<?> getterType = metaObject.getGetterType(fieldName);
		if (ClassUtils.isAssignableValue(getterType, fieldVal)) {
			metaObject.setValue(fieldName, fieldVal);
		}
	}

	/**
	 * 获取 spring security 当前的用户名
	 * @return 当前用户名
	 */
	private String getUserName() {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		if (Optional.ofNullable(authentication).isPresent()) {
			return authentication.getName();
		}
		return null;
	}

}
