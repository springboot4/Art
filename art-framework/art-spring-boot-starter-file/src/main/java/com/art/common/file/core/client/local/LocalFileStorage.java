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

package com.art.common.file.core.client.local;

import cn.hutool.core.io.FileUtil;
import com.art.common.file.core.propertie.FileStorageProperties;
import lombok.RequiredArgsConstructor;

/**
 * 本地文件存储
 *
 * @author Fxz
 * @version 1.0
 * @date 2023/1/16 22:39
 */
@RequiredArgsConstructor
public class LocalFileStorage {

	private final FileStorageProperties properties;

	public String putObject(byte[] content, String path) {
		String filePath = getFilePath(path);
		FileUtil.writeBytes(content, filePath);
		return filePath;
	}

	public void delete(String path) {
		String filePath = getFilePath(path);
		FileUtil.del(filePath);
	}

	public byte[] getContent(String path) {
		String filePath = getFilePath(path);
		return FileUtil.readBytes(filePath);
	}

	private String getFilePath(String path) {
		return properties.getLocal().getBasePath() + path;
	}

}
