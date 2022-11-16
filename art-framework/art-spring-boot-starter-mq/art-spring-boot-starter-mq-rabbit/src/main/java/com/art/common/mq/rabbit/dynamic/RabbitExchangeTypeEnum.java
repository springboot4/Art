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

package com.art.common.mq.rabbit.dynamic;

/**
 * RabbitMQ 交换机类型枚举
 *
 * @author fxz
 */
public enum RabbitExchangeTypeEnum {

	/**
	 * 直接交换机
	 * <p>
	 * 根据routing-key精准匹配队列(最常使用)
	 */
	DIRECT,

	/**
	 * 主题交换机
	 * <p>
	 * 根据routing-key模糊匹配队列，*匹配任意一个字符，#匹配0个或多个字符
	 */
	TOPIC,

	/**
	 * 扇形交换机
	 * <p>
	 * 直接分发给所有绑定的队列，忽略routing-key,用于广播消息
	 */
	FANOUT,

	/**
	 * 头交换机
	 * <p>
	 * 类似直连交换机，不同于直连交换机的路由规则建立在头属性上而不是routing-key(使用较少)
	 */
	HEADERS;

}
