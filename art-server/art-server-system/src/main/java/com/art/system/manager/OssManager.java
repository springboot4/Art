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

package com.art.system.manager;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.thread.ThreadUtil;
import com.amazonaws.services.s3.model.CompleteMultipartUploadResult;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PartETag;
import com.art.common.core.exception.FxzException;
import com.art.common.file.core.OssTemplate;
import com.art.system.core.handler.PartUploaderHandler;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.stereotype.Component;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * @author Fxz
 * @version 1.0
 * @date 2023/1/11 18:17
 */
@Component
@RequiredArgsConstructor
public class OssManager {

	private static final int MAX_PART_COUNT = 10000;

	private final OssTemplate ossTemplate;

	private final ThreadPoolExecutor executor = ThreadUtil.newExecutor(20, 20);

	@SuppressWarnings("all")
	@SneakyThrows
	public String partUpload(String bucketName, String fileName, byte[] bytes, String contentType) {
		// 元数据信息
		ObjectMetadata objectMetadata = new ObjectMetadata();
		objectMetadata.setContentType(contentType);

		// 初始化分片
		String uploadId = ossTemplate.initiateMultipartUpload(bucketName, fileName, objectMetadata);

		// 计算文件有多少个分片,默认分片大小5M。
		long partSize = 5 * 1024 * 1024L;
		// 文件大小
		long fileLength = bytes.length;
		// 分片数量
		int partCount = (int) Math.ceil(1.0 * fileLength / partSize);
		// 判断分片是否大于10000
		if (partCount > MAX_PART_COUNT) {
			throw new FxzException("Total parts count should not exceed 10000!");
		}

		File file = new File(fileName);
		file = FileUtil.writeBytes(bytes, file);

		CountDownLatch latch = new CountDownLatch(partCount);
		List<PartETag> eTagList = new ArrayList<>();
		// 遍历分片上传
		for (int i = 0; i < partCount; i++) {
			long startPos = i * partSize;
			long curPartSize = (i + 1 == partCount) ? (fileLength - startPos) : partSize;
			executor.execute(new PartUploaderHandler(ossTemplate, bucketName, fileName, uploadId, i + 1, startPos,
					curPartSize, file, eTagList, latch));
		}

		// 等待所有零件完成
		latch.await();
		// 删除临时文件
		file.delete();

		// 验证所有零件是否均已完成
		if (eTagList.size() != partCount) {
			ossTemplate.abortMultipartUpload(uploadId, bucketName, fileName);
			throw new FxzException("部分分片上传未完成，分片上传失败！");
		}

		CompleteMultipartUploadResult result = ossTemplate.completeMultipartUpload(uploadId, bucketName, fileName,
				eTagList);
		return result.getLocation();
	}

}
