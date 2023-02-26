package com.art.system.core.handler;

import com.amazonaws.services.s3.model.PartETag;
import com.art.common.file.core.client.oss.OssFileStorage;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.SneakyThrows;

import java.io.File;
import java.util.concurrent.CopyOnWriteArrayList;

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
	private OssFileStorage ossFileStorage;

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
	private File file;

	/**
	 * 需要保证线程安全
	 */
	private final CopyOnWriteArrayList<PartETag> eTagList;

	/**
	 * 偏移量
	 */
	private long startPos;

	@SuppressWarnings("all")
	@SneakyThrows
	@Override
	public void run() {
		PartETag eTag = ossFileStorage.uploadPart(bucketName, key, uploadId, partNumber, file, partSize, startPos);
		this.eTagList.add(eTag);
	}

}
