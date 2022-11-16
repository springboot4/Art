/*
 * COPYRIGHT (C) 2022 Art AUTHORS(fxzcloud@gmail.com). ALL RIGHTS RESERVED.
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

package com.art.common.es.config;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.json.jackson.JacksonJsonpMapper;
import co.elastic.clients.transport.ElasticsearchTransport;
import co.elastic.clients.transport.rest_client.RestClientTransport;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.http.HttpHost;
import org.elasticsearch.client.RestClient;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.context.annotation.Bean;

import javax.annotation.Resource;

/**
 * @author Fxz
 * @version 0.0.1
 * @date 2022/5/24 21:52
 */
@AutoConfiguration
public class ElasticsearchClientConfig {

	@Resource
	private ObjectMapper objectMapper;

	@Bean
	public RestClient restClient() {
		return RestClient.builder(new HttpHost("fxz-elasticsearch", 9200))
				.setRequestConfigCallback(
						b -> b.setConnectTimeout(10000).setConnectionRequestTimeout(10000).setSocketTimeout(60000))
				.build();
	}

	@Bean
	public JacksonJsonpMapper jacksonJsonpMapper() {
		return new JacksonJsonpMapper(objectMapper);
	}

	@Bean
	public ElasticsearchTransport elasticsearchTransport() {
		return new RestClientTransport(restClient(), jacksonJsonpMapper());
	}

	@Bean
	public ElasticsearchClient elasticsearchClient() {
		return new ElasticsearchClient(elasticsearchTransport());
	}

}
