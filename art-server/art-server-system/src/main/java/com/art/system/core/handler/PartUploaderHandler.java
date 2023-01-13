package com.art.system.core.handler;

import com.amazonaws.services.s3.model.PartETag;
import com.art.common.file.core.OssTemplate;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.SneakyThrows;

import java.io.InputStream;
import java.util.concurrent.CopyOnWriteArrayList;
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
	 * 分片大小
	 */
	private long partSize;

	/**
	 * InputStream
	 */
	private InputStream stream;

	/**
	 * 需要保证线程安全
	 */
	private final CopyOnWriteArrayList<PartETag> eTagList;

	/**
	 * 并发控制
	 */
	private final CountDownLatch latch;

	@SuppressWarnings("all")
	@SneakyThrows
	@Override
	public void run() {
		PartETag eTag = ossTemplate.uploadPart(bucketName, key, uploadId, partNumber, stream, partSize);
		this.eTagList.add(eTag);
		latch.countDown();
	}

}
