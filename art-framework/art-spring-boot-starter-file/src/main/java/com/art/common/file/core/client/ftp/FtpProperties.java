/*
 * COPYRIGHT (C) 2023 Art AUTHORS(fxzcloud@gmail.com). ALL RIGHTS RESERVED.
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

package com.art.common.file.core.client.ftp;

import lombok.Data;

/**
 * @author Fxz
 * @version 0.0.1
 * @date 2023/2/20 15:48
 */
@Data
public class FtpProperties {

	/**
	 * 基础路径
	 */
	private String basePath = "/var/ftp/work01";

	/**
	 * 地址
	 */
	private String host = "61.153.186.23";

	/**
	 * 端口
	 */
	private Integer port = 21;

	/**
	 * 用户名
	 */
	private String username = "root";

	/**
	 * 密码
	 */
	private String password = "Fxz127255";

	/**
	 * 连接模式
	 * <p/>
	 * 使用 {@link cn.hutool.extra.ftp.FtpMode} 对应的字符串
	 */
	private String mode = "Passive";

}
