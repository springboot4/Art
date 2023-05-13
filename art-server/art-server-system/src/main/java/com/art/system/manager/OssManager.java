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

import cn.hutool.core.thread.ThreadUtil;
import com.amazonaws.services.s3.model.CompleteMultipartUploadResult;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PartETag;
import com.amazonaws.services.s3.model.S3Object;
import com.art.common.core.exception.FxzException;
import com.art.common.core.util.AsyncUtil;
import com.art.common.file.core.client.oss.OssFileStorage;
import com.art.system.core.handler.PartUploaderHandler;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.stereotype.Component;

import java.io.File;
import java.util.concurrent.CopyOnWriteArrayList;
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

	private final OssFileStorage ossFileStorage;

	private final ThreadPoolExecutor executor = ThreadUtil.newExecutor(20, 20);

	@SuppressWarnings("all")
	@SneakyThrows
	public String partUpload(String bucketName, String fileName, File file, String contentType) {
		// 元数据信息
		ObjectMetadata objectMetadata = new ObjectMetadata();
		objectMetadata.setContentType(contentType);

		// 初始化分片
		String uploadId = ossFileStorage.initiateMultipartUpload(bucketName, fileName, objectMetadata);

		// 计算文件有多少个分片,默认分片大小5M。
		long partSize = 5 * 1024 * 1024L;
		// 文件大小
		long fileLength = file.length();
		// 分片数量
		int partCount = (int) Math.ceil(1.0 * fileLength / partSize);
		// 判断分片是否大于10000
		if (partCount > MAX_PART_COUNT) {
			throw new FxzException("Total parts count should not exceed 10000!");
		}

		// 分片标识
		CopyOnWriteArrayList<PartETag> eTagList = new CopyOnWriteArrayList<>();
		// 多线程并发控制
		Runnable[] runnables = new PartUploaderHandler[partCount];

		// 遍历分片上传
		for (int i = 0; i < partCount; i++) {
			long startPos = i * partSize;
			long curPartSize = (i + 1 == partCount) ? (fileLength - (i * partSize)) : partSize;
			runnables[i] = new PartUploaderHandler(ossFileStorage, bucketName, fileName, uploadId, i + 1, curPartSize,
					file, eTagList, startPos);
		}

		// 等待所有零件完成
		AsyncUtil.parallel(runnables);

		// 验证所有零件是否均已完成
		if (eTagList.size() != partCount) {
			ossFileStorage.abortMultipartUpload(uploadId, bucketName, fileName);
			throw new FxzException("部分分片上传未完成，分片上传失败！");
		}

		CompleteMultipartUploadResult result = ossFileStorage.completeMultipartUpload(uploadId, bucketName, fileName,
				eTagList);
		return result.getLocation();
	}

	public void removeObject(String bucketName, String fileName) {
		ossFileStorage.removeObject(bucketName, fileName);
	}

	public S3Object getObject(String bucket, String fileName) {
		return ossFileStorage.getObject(bucket, fileName);
	}

	/**
	 * 获取文件临时外链
	 */
	public String preSignUploadUrl(String bucket, String fileName) {
		return ossFileStorage.genPreSignUploadUrl(bucket, fileName);
	}

}
