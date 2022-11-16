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

package com.art.scheduled.utils;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.extra.spring.SpringUtil;
import com.art.scheduled.entity.SysJob;
import org.apache.commons.lang3.StringUtils;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.LinkedList;
import java.util.List;

/**
 * 任务执行工具
 *
 * @author fxz
 */
public class JobInvokeUtil {

	/**
	 * 执行方法
	 * @param sysJob 系统任务
	 */
	public static void invokeMethod(SysJob sysJob) throws Exception {
		String invokeTarget = sysJob.getInvokeTarget();
		String beanName = getBeanName(invokeTarget);
		String methodName = getMethodName(invokeTarget);
		List<Object[]> methodParams = getMethodParams(invokeTarget);

		if (!isValidClassName(beanName)) {
			Object bean = SpringUtil.getBean(beanName);
			invokeMethod(bean, methodName, methodParams);
		}
		else {
			Object bean = Class.forName(beanName).newInstance();
			invokeMethod(bean, methodName, methodParams);
		}
	}

	/**
	 * 调用任务方法
	 * @param bean 目标对象
	 * @param methodName 方法名称
	 * @param methodParams 方法参数
	 */
	private static void invokeMethod(Object bean, String methodName, List<Object[]> methodParams)
			throws NoSuchMethodException, SecurityException, IllegalAccessException, IllegalArgumentException,
			InvocationTargetException {
		if (ObjectUtil.isNotNull(methodParams) && methodParams.size() > 0) {
			Method method = bean.getClass().getDeclaredMethod(methodName, getMethodParamsType(methodParams));
			method.invoke(bean, getMethodParamsValue(methodParams));
		}
		else {
			Method method = bean.getClass().getDeclaredMethod(methodName);
			method.invoke(bean);
		}
	}

	/**
	 * 校验是否为为class包名
	 * @param invokeTarget 名称
	 * @return true是 false否
	 */
	public static boolean isValidClassName(String invokeTarget) {
		if (StringUtils.isNotBlank(invokeTarget))
			return invokeTarget.contains(".");
		return false;
	}

	/**
	 * 获取bean名称
	 * @param invokeTarget 目标字符串
	 * @return bean名称
	 */
	public static String getBeanName(String invokeTarget) {
		String beanName = StringUtils.substringBefore(invokeTarget, "(");
		return StringUtils.substringBeforeLast(beanName, ".");
	}

	/**
	 * 获取bean方法
	 * @param invokeTarget 目标字符串
	 * @return method方法
	 */
	public static String getMethodName(String invokeTarget) {
		String methodName = StringUtils.substringBefore(invokeTarget, "(");
		return StringUtils.substringAfterLast(methodName, ".");
	}

	/**
	 * 获取method方法参数相关列表
	 * @param invokeTarget 目标字符串
	 * @return method方法相关参数列表
	 */
	public static List<Object[]> getMethodParams(String invokeTarget) {
		String methodStr = StringUtils.substringBetween(invokeTarget, "(", ")");
		if (StringUtils.isEmpty(methodStr)) {
			return null;
		}
		String[] methodParams = methodStr.split(",(?=([^\"']*[\"'][^\"']*[\"'])*[^\"']*$)");
		List<Object[]> classs = new LinkedList<>();
		for (int i = 0; i < methodParams.length; i++) {
			String str = StringUtils.trimToEmpty(methodParams[i]);
			// String字符串类型，以'或"开头
			if (StringUtils.startsWithAny(str, "'", "\"")) {
				classs.add(new Object[] { StringUtils.substring(str, 1, str.length() - 1), String.class });
			}
			// boolean布尔类型，等于true或者false
			else if ("true".equalsIgnoreCase(str) || "false".equalsIgnoreCase(str)) {
				classs.add(new Object[] { Boolean.valueOf(str), Boolean.class });
			}
			// long长整形，以L结尾
			else if (StringUtils.endsWith(str, "L")) {
				classs.add(new Object[] { Long.valueOf(StringUtils.substring(str, 0, str.length() - 1)), Long.class });
			}
			// double浮点类型，以D结尾
			else if (StringUtils.endsWith(str, "D")) {
				classs.add(
						new Object[] { Double.valueOf(StringUtils.substring(str, 0, str.length() - 1)), Double.class });
			}
			// 其他类型归类为整形
			else {
				classs.add(new Object[] { Integer.valueOf(str), Integer.class });
			}
		}
		return classs;
	}

	/**
	 * 获取参数类型
	 * @param methodParams 参数相关列表
	 * @return 参数类型列表
	 */
	public static Class<?>[] getMethodParamsType(List<Object[]> methodParams) {
		Class<?>[] classs = new Class<?>[methodParams.size()];
		int index = 0;
		for (Object[] os : methodParams) {
			classs[index] = (Class<?>) os[1];
			index++;
		}
		return classs;
	}

	/**
	 * 获取参数值
	 * @param methodParams 参数相关列表
	 * @return 参数值列表
	 */
	public static Object[] getMethodParamsValue(List<Object[]> methodParams) {
		Object[] classs = new Object[methodParams.size()];
		int index = 0;
		for (Object[] os : methodParams) {
			classs[index] = (Object) os[0];
			index++;
		}
		return classs;
	}

}
