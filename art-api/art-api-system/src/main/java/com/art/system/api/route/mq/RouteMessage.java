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

package com.art.system.api.route.mq;

import com.art.common.mq.redis.pubsub.AbstractPubSubMessage;
import com.art.system.api.route.dto.RouteConfDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

/**
 * 网关路由更新消息
 *
 * @author fxz
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class RouteMessage extends AbstractPubSubMessage implements Serializable {

	private static final long serialVersionUID = -1L;

	List<RouteConfDTO> routeConfDOList;

	@Override
	public String getTopic() {
		return "route.redis.gateway.topic";
	}

}