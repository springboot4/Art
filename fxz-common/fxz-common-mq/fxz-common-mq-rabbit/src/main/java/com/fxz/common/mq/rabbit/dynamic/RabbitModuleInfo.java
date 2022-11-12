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

package com.fxz.common.mq.rabbit.dynamic;

import lombok.Data;

import java.util.Map;

/**
 * RabbitMQ 队列和交换机机绑定关系实体对象
 *
 * @author fxz
 */
@Data
public class RabbitModuleInfo {

	/**
	 * 路由Key
	 */
	private String routingKey;

	/**
	 * 队列信息
	 */
	private Queue queue;

	/**
	 * 交换机信息
	 */
	private Exchange exchange;

	/**
	 * 交换机信息类
	 */
	@Data
	public static class Exchange {

		/**
		 * 交换机类型
		 */
		private RabbitExchangeTypeEnum type = RabbitExchangeTypeEnum.DIRECT; // 默认直连交换机

		/**
		 * 交换机名称
		 */
		private String name;

		/**
		 * 是否持久化
		 */
		private boolean durable = true; // 默认true持久化，重启消息不会丢失

		/**
		 * 当所有队绑定列均不在使用时，是否自动删除交换机
		 */
		private boolean autoDelete = false; // 默认false，不自动删除

		/**
		 * 交换机其他参数
		 */
		private Map<String, Object> arguments;

	}

	/**
	 * 队列信息类
	 */
	@Data
	public static class Queue {

		/**
		 * 队列名称
		 */
		private String name;

		/**
		 * 是否持久化
		 */
		private boolean durable = true; // 默认true持久化，重启消息不会丢失

		/**
		 * 是否具有排他性
		 */
		private boolean exclusive = false; // 默认false，可多个消费者消费同一个队列

		/**
		 * 当消费者均断开连接，是否自动删除队列
		 */
		private boolean autoDelete = false; // 默认false,不自动删除，避免消费者断开队列丢弃消息

		/**
		 * 绑定死信队列的交换机名称
		 */
		private String deadLetterExchange;

		/**
		 * 绑定死信队列的路由key
		 */
		private String deadLetterRoutingKey;

		private Map<String, Object> arguments;

	}

}
