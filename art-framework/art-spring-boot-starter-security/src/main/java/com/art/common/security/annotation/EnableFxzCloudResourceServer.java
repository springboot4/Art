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

package com.art.common.security.annotation;

import com.art.common.security.config.FxzCloudResourceServerConfigure;
import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

/**
 * @author fxz 资源服务器注解，通过添加此注解实现配置ResourceServerConfigurerAdapter的效果
 * 资源服务器需要忽略鉴权在配置文件中添加fxz:cloud:security:anonUris 具体配置参考:
 * @see com.art.common.security.properties.FxzCloudSecurityProperties
 */
@Target({ ElementType.TYPE })
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Import(FxzCloudResourceServerConfigure.class)
public @interface EnableFxzCloudResourceServer {

}
