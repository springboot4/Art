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

package com.art.common.file.core.propertie;

import com.art.common.file.core.client.ftp.FtpProperties;
import com.art.common.file.core.client.local.LocalProperties;
import com.art.common.file.core.client.oss.OssProperties;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.NestedConfigurationProperty;

/**
 * @author Fxz
 * @version 1.0
 * @date 2023/1/16 23:01
 */
@Data
@ConfigurationProperties(prefix = "file.storage")
public class FileStorageProperties {

	@NestedConfigurationProperty
	private OssProperties oss = new OssProperties();

	@NestedConfigurationProperty
	private LocalProperties local = new LocalProperties();

	@NestedConfigurationProperty
	private FtpProperties ftp = new FtpProperties();

}
