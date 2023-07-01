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

package com.art.common.doc.properties;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author Fxz
 * @version 0.0.1
 * @date 2022/10/21 21:05
 */
@Getter
@Setter
@ConfigurationProperties(prefix = "fxz.common.doc")
public class DocProperties {

	/**
	 * 是否开启
	 */
	private boolean enabled = true;

	/**
	 * 标题
	 */
	private String title = "Art开发文档";

	/**
	 * 作者
	 */
	private String author = "fxz";

	/**
	 * 描述
	 */
	private String description = "描述";

	/**
	 * 版本
	 */
	private String version = "0.0.1";

	/**
	 * 网关地址
	 */
	private String url = "http://art-gateway:9999";

}
