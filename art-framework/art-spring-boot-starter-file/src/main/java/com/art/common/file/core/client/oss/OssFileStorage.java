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

package com.art.common.file.core.client.oss;

import com.amazonaws.HttpMethod;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.*;
import com.amazonaws.util.IOUtils;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.InputStream;
import java.net.URL;
import java.nio.file.Files;
import java.time.Duration;
import java.time.Instant;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * 针对兼容亚马逊S3协议的对象存储平台
 *
 * @author fxz
 */
@RequiredArgsConstructor
public class OssFileStorage {

	private final AmazonS3 amazonS3;

	/**
	 * 初始化分片上传请求
	 * @param bucketName 桶名称
	 * @param objectName 文件路径
	 * @param objectMetadata 对象元数据
	 * @return uploadId
	 */
	public String initiateMultipartUpload(String bucketName, String objectName, ObjectMetadata objectMetadata) {
		InitiateMultipartUploadResult initiateMultipartUploadResult = amazonS3.initiateMultipartUpload(
				new InitiateMultipartUploadRequest(bucketName, objectName).withObjectMetadata(objectMetadata));
		return initiateMultipartUploadResult.getUploadId();
	}

	/**
	 * 分片上传
	 * @param bucketName 桶名称
	 * @param objectName 文件路径
	 * @param uploadId 分片标识
	 * @param partNumber 分片号
	 * @param file 文件
	 * @param partSize 分片大小
	 * @return {@link PartETag}
	 */
	@SneakyThrows
	public PartETag uploadPart(String bucketName, String objectName, String uploadId, int partNumber, File file,
			long partSize, long startPos) {
		InputStream stream = Files.newInputStream(file.toPath());

		// @formatter:off
        UploadPartRequest uploadRequest = new UploadPartRequest()
                .withBucketName(bucketName)
                .withUploadId(uploadId)
                .withKey(objectName)
                .withPartNumber(partNumber)
                .withPartSize(partSize)
                .withFileOffset(startPos)
                .withInputStream(stream);
        // @formatter:on
		UploadPartResult result = amazonS3.uploadPart(uploadRequest);

		return result.getPartETag();
	}

	/**
	 * 查询已经上传的分片
	 * @param bucketName 桶名称
	 * @param objectName 文件路径
	 * @param uploadId 分片过程的标识
	 * @return 已经上传的分片
	 */
	public List<PartSummary> listParts(String bucketName, String objectName, String uploadId) {
		ListPartsRequest listPartsRequest = new ListPartsRequest(bucketName, objectName, uploadId);
		PartListing partListing = amazonS3.listParts(listPartsRequest);
		return partListing.getParts();
	}

	/**
	 * 合并分片请求
	 * @param uploadId 分片标识
	 * @param bucketName 桶名称
	 * @param objectName 文件路径
	 * @param parts 分片
	 * @return {@link CompleteMultipartUploadResult}
	 */
	public CompleteMultipartUploadResult completeMultipartUpload(String uploadId, String bucketName, String objectName,
			List<PartETag> parts) {
		CompleteMultipartUploadRequest completeMultipartUploadRequest = new CompleteMultipartUploadRequest()
			.withUploadId(uploadId)
			.withKey(objectName)
			.withBucketName(bucketName)
			.withPartETags(parts);
		return amazonS3.completeMultipartUpload(completeMultipartUploadRequest);
	}

	/**
	 * 停止正在进行的分段上传,此方法将删除任何已上传到 Amazon S3 的分段并释放资源
	 * @param uploadId 分片标识
	 * @param bucketName 桶名称
	 * @param objectName 文件路径
	 */
	public void abortMultipartUpload(String uploadId, String bucketName, String objectName) {
		amazonS3.abortMultipartUpload(new AbortMultipartUploadRequest(bucketName, objectName, uploadId));
	}

	/**
	 * 上传文件
	 * @param bucketName bucket名称
	 * @param objectName 文件名称
	 * @param stream 文件流
	 * @param contentType 文件类型
	 * @see <a href=
	 * "https://docs.aws.amazon.com/zh_cn/AmazonS3/latest/userguide/upload-objects.html"/>
	 */
	@SneakyThrows
	public void putObject(String bucketName, String objectName, InputStream stream, String contentType) {
		putObject(bucketName, objectName, stream, stream.available(), contentType);
	}

	/**
	 * 上传文件
	 * @param bucketName bucket名称
	 * @param objectName 文件名称
	 * @param stream 文件流
	 * @see <a href=
	 * "https://docs.aws.amazon.com/zh_cn/AmazonS3/latest/userguide/upload-objects.html"/>
	 */
	@SneakyThrows
	public void putObject(String bucketName, String objectName, InputStream stream) {
		putObject(bucketName, objectName, stream, stream.available(), "application/octet-stream");
	}

	/**
	 * 上传文件
	 * @param bucketName bucket名称
	 * @param objectName 文件名称
	 * @param stream 文件流
	 * @param size 大小
	 * @param contentType 类型
	 * @see <a href=
	 * "https://docs.aws.amazon.com/zh_cn/AmazonS3/latest/userguide/upload-objects.html"/>
	 */
	@SneakyThrows
	public PutObjectResult putObject(String bucketName, String objectName, InputStream stream, long size,
			String contentType) {
		byte[] bytes = IOUtils.toByteArray(stream);
		ObjectMetadata objectMetadata = new ObjectMetadata();
		objectMetadata.setContentLength(size);
		objectMetadata.setContentType(contentType);
		ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(bytes);

		return amazonS3.putObject(bucketName, objectName, byteArrayInputStream, objectMetadata);
	}

	/**
	 * 获取文件
	 * @param bucketName bucket名称
	 * @param objectName 文件名称
	 * @return 二进制流
	 * @see <a href=
	 * "https://docs.aws.amazon.com/zh_cn/AmazonS3/latest/userguide/download-objects.html"/>
	 */
	public S3Object getObject(String bucketName, String objectName) {
		return amazonS3.getObject(new GetObjectRequest(bucketName, objectName));
	}

	/**
	 * 删除文件
	 * @param bucketName bucket名称
	 * @param objectName 文件名称
	 * @see <a href="http://docs.aws.amazon.com/goto/WebAPI/s3-2006-03-01/DeleteObject"/>
	 */
	public void removeObject(String bucketName, String objectName) {
		amazonS3.deleteObject(new DeleteObjectRequest(bucketName, objectName));
	}

	/**
	 * 根据文件前置查询文件
	 * @param bucketName bucket名称
	 * @param prefix 前缀
	 * @return S3ObjectSummary 列表
	 * @see <a href=
	 * "https://docs.aws.amazon.com/zh_cn/AmazonS3/latest/userguide/ListingKeysUsingAPIs.html"/>
	 */
	public List<S3ObjectSummary> getAllObjectsByPrefix(String bucketName, String prefix) {
		ObjectListing res = amazonS3.listObjects(bucketName, prefix);
		return res.getObjectSummaries();
	}

	/**
	 * 获取文件外链 (GET请求)
	 * @param bucketName bucket名称
	 * @param objectName 文件名称
	 * @return url
	 * {@link AmazonS3#generatePresignedUrl(String bucketName, String key, Date expiration)}
	 * @see <a href=
	 * "https://docs.aws.amazon.com/zh_cn/AmazonS3/latest/userguide/ShareObjectPreSignedURL.html"/>
	 */
	public String genPreSignUploadUrl(String bucketName, String objectName) {
		return genPreSignUploadUrl(bucketName, objectName, 30);
	}

	/**
	 * 获取文件外链 (GET请求)
	 * @param bucketName bucket名称
	 * @param objectName 文件名称
	 * @param expires 过期时间 <=7天
	 * @return url
	 * {@link AmazonS3#generatePresignedUrl(String bucketName, String key, Date expiration)}
	 * @see <a href=
	 * "https://docs.aws.amazon.com/zh_cn/AmazonS3/latest/userguide/ShareObjectPreSignedURL.html"/>
	 */
	public String genPreSignUploadUrl(String bucketName, String objectName, Integer expires) {
		return genPreSignUploadUrl(bucketName, objectName, expires, HttpMethod.GET);
	}

	/**
	 * 获取文件外链 (GET、PUT请求)
	 * @param bucketName bucket名称
	 * @param objectName 文件名称
	 * @param expires 过期时间 <=7天
	 * @param method GET、PUT请求
	 * @return url
	 * {@link AmazonS3#generatePresignedUrl(String bucketName, String key, Date expiration)}
	 * @see <a href=
	 * "https://docs.aws.amazon.com/zh_cn/AmazonS3/latest/userguide/ShareObjectPreSignedURL.html"/>
	 */
	public String genPreSignUploadUrl(String bucketName, String objectName, Integer expires, HttpMethod method) {
		return genPreSignUploadUrl(bucketName, objectName, expires, null, method);
	}

	/**
	 * 获取文件外链 (GET、PUT请求)
	 * @param bucketName bucket名称
	 * @param objectName 文件名称
	 * @param expires 过期时间 <=7天
	 * @param params 请求参数
	 * @param method GET、PUT请求
	 * @return url
	 * {@link AmazonS3#generatePresignedUrl(String bucketName, String key, Date expiration)}
	 * @see <a href=
	 * "https://docs.aws.amazon.com/zh_cn/AmazonS3/latest/userguide/ShareObjectPreSignedURL.html"/>
	 */
	public String genPreSignUploadUrl(String bucketName, String objectName, Integer expires, Map<String, String> params,
			HttpMethod method) {
		Date expiration = Date.from(Instant.now().plus(Duration.ofMinutes(expires)));

		GeneratePresignedUrlRequest request = new GeneratePresignedUrlRequest(bucketName, objectName)
			.withExpiration(expiration)
			.withMethod(method);
		if (params != null) {
			params.forEach(request::addRequestParameter);
		}

		URL url = amazonS3.generatePresignedUrl(request);
		return url.toString();
	}

	/**
	 * 创建桶
	 * @param bucketName bucket名称
	 * @see <a href=
	 * "https://docs.aws.amazon.com/zh_cn/AmazonS3/latest/userguide/create-bucket-overview.html"/>
	 */
	public void createBucket(String bucketName) {
		if (!amazonS3.doesBucketExistV2(bucketName)) {
			amazonS3.createBucket(new CreateBucketRequest(bucketName));
		}
	}

	/**
	 * 列出桶
	 */
	public List<Bucket> getAllBuckets() {
		return amazonS3.listBuckets();
	}

	/**
	 * 查找桶
	 * @param bucketName bucket名称
	 */
	public Optional<Bucket> getBucket(String bucketName) {
		return amazonS3.listBuckets().stream().filter(b -> b.getName().equals(bucketName)).findFirst();
	}

	/**
	 * 删除桶
	 * @param bucketName bucket名称
	 */
	public void removeBucket(String bucketName) {
		amazonS3.deleteBucket(new DeleteBucketRequest(bucketName));
	}

}
