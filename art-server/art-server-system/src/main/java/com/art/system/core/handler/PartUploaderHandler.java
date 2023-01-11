package com.art.system.core.handler;

import com.amazonaws.services.s3.model.PartETag;
import com.art.common.file.core.OssTemplate;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.SneakyThrows;

import java.io.File;
import java.util.List;
import java.util.concurrent.CountDownLatch;

/**
 * @author fxz
 * @since 2022-01-10
 */
@Data
@AllArgsConstructor
public class PartUploaderHandler implements Runnable {

	/**
	 * AmazonS3 Oss操作客户端
	 */
	private OssTemplate ossTemplate;

	/**
	 * 桶名称
	 */
	private String bucketName;

	/**
	 * 文件路径
	 */
	private String key;

	/**
	 * 分片标识
	 */
	private String uploadId;

	/**
	 * 分片数量
	 */
	private int partNumber;

	/**
	 * 文件偏移量
	 */
	private long fileOffset;

	/**
	 * 分片大小
	 */
	private long partSize;

	private File file;

	private final List<PartETag> eTagList;

	private final CountDownLatch latch;

	@SuppressWarnings("all")
	@SneakyThrows
	@Override
	public void run() {
		PartETag eTag = ossTemplate.uploadPart(bucketName, key, uploadId, partNumber, fileOffset, file, partSize);
		synchronized (eTagList) {
			this.eTagList.add(eTag);
			latch.countDown();
		}
	}

}
