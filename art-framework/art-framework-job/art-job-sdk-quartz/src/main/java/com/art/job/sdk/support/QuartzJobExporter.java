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

package com.art.job.sdk.support;

import org.springframework.beans.factory.SmartInitializingSingleton;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ApplicationObjectSupport;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.util.ClassUtils;

import java.util.Arrays;
import java.util.Map;

/**
 * @author Fxz
 * @version 0.0.1
 * @date 2022/12/12 17:19
 */
public class QuartzJobExporter extends ApplicationObjectSupport implements SmartInitializingSingleton {

	@Override
	public void afterSingletonsInstantiated() {
		ApplicationContext applicationContext = getApplicationContext();
		assert applicationContext != null;

		scanPackage(applicationContext);
	}

	private void scanPackage(ApplicationContext applicationContext) {
		Map<String, Object> springBootApplicationBeans = applicationContext
			.getBeansWithAnnotation(SpringBootApplication.class);

		JobClassPathScanner scanner = new JobClassPathScanner((BeanDefinitionRegistry) applicationContext, false);

		String[] array = springBootApplicationBeans.values()
			.stream()
			.map(this::getScanBasePackages)
			.flatMap(Arrays::stream)
			.toArray(String[]::new);

		scanner.doScan(array);
	}

	private String[] getScanBasePackages(Object bean) {
		SpringBootApplication annotation = AnnotationUtils.findAnnotation(bean.getClass(), SpringBootApplication.class);
		assert annotation != null;

		return (annotation.scanBasePackages().length != 0) ? annotation.scanBasePackages()
				: new String[] { ClassUtils.getPackageName(bean.getClass().getName()) };
	}

}
