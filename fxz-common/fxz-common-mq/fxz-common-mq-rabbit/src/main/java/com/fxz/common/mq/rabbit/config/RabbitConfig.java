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

package com.fxz.common.mq.rabbit.config;

import com.fxz.common.mq.rabbit.dynamic.RabbitModuleInitializer;
import com.fxz.common.mq.rabbit.dynamic.RabbitModuleProperties;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.AmqpAdmin;
import org.springframework.amqp.rabbit.config.SimpleRabbitListenerContainerFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.listener.RabbitListenerContainerFactory;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;

/**
 * @author fxz
 */
@EnableConfigurationProperties(RabbitModuleProperties.class)
@AutoConfiguration
@Slf4j
public class RabbitConfig {

	/**
	 * 消息序列化配置
	 */
	@Bean
	public RabbitListenerContainerFactory<?> rabbitListenerContainerFactory(ConnectionFactory connectionFactory) {
		SimpleRabbitListenerContainerFactory factory = new SimpleRabbitListenerContainerFactory();
		factory.setConnectionFactory(connectionFactory);
		factory.setMessageConverter(new Jackson2JsonMessageConverter());
		return factory;
	}

	/**
	 * 动态创建队列、交换机初始化器
	 */
	@Bean
	@ConditionalOnMissingBean
	public RabbitModuleInitializer rabbitModuleInitializer(AmqpAdmin amqpAdmin,
			RabbitModuleProperties rabbitModuleProperties) {
		return new RabbitModuleInitializer(amqpAdmin, rabbitModuleProperties);
	}

}
