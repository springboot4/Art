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

package com.art.common.file.core.client.ftp;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.extra.ftp.Ftp;
import cn.hutool.extra.ftp.FtpException;
import lombok.RequiredArgsConstructor;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;

/**
 * @author Fxz
 * @version 0.0.1
 * @date 2023/2/20 15:42
 */
@RequiredArgsConstructor
public class FtpFileStorage {

	private final Ftp client;

	private final String basePath;

	public String putObject(byte[] content, String path) {
		String filePath = getFilePath(path);
		String fileName = FileUtil.getName(filePath);
		String dir = StrUtil.removeSuffix(filePath, fileName);

		client.reconnectIfTimeout();
		boolean success = client.upload(dir, fileName, new ByteArrayInputStream(content));
		if (!success) {
			throw new FtpException(StrUtil.format("上传文件到目标目录 ({}) 失败", filePath));
		}

		return path;
	}

	public void delete(String path) {
		String filePath = getFilePath(path);
		client.reconnectIfTimeout();
		client.delFile(filePath);
	}

	public byte[] getContent(String path) {
		String filePath = getFilePath(path);
		String fileName = FileUtil.getName(filePath);
		String dir = StrUtil.removeSuffix(filePath, fileName);

		ByteArrayOutputStream out = new ByteArrayOutputStream();
		client.reconnectIfTimeout();
		client.download(dir, fileName, out);

		return out.toByteArray();
	}

	private String getFilePath(String path) {
		return basePath + path;
	}

}
