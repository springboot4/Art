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

package com.art.common.file.config;

import com.amazonaws.ClientConfiguration;
import com.amazonaws.Protocol;
import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.client.builder.AwsClientBuilder;
import com.amazonaws.regions.Region;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.art.common.file.core.OssProperties;
import com.art.common.file.core.OssTemplate;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;

/**
 * @author fxz
 */
@AutoConfiguration
@EnableConfigurationProperties({ OssProperties.class })
public class OssAutoConfiguration {

	@Bean
	@ConditionalOnProperty(name = "oss.enable", havingValue = "true", matchIfMissing = true)
	public OssTemplate ossTemplate(AmazonS3 amazonS3) {
		return new OssTemplate(amazonS3);
	}

	@Bean
	public AmazonS3 amazonS3(OssProperties properties) {
		// 客户端配置
		ClientConfiguration clientConfiguration = new ClientConfiguration();
		clientConfiguration.setProtocol(Protocol.HTTP);

		// 端点配置
		AwsClientBuilder.EndpointConfiguration endpointConfiguration = new AwsClientBuilder.EndpointConfiguration(
				properties.getEndpoint(), Region.getRegion(Regions.CN_NORTH_1).getName());

		// 凭证配置
		AWSCredentials awsCredentials = new BasicAWSCredentials(properties.getAccessKey(), properties.getSecretKey());

		return AmazonS3Client.builder().withEndpointConfiguration(endpointConfiguration)
				.withClientConfiguration(clientConfiguration)
				.withCredentials(new AWSStaticCredentialsProvider(awsCredentials))
				.withPathStyleAccessEnabled(properties.getPathStyleAccess()).build();
	}

}
