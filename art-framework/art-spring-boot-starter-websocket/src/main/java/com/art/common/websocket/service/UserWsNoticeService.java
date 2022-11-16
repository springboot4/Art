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

package com.art.common.websocket.service;

import com.art.common.websocket.handler.UserNoticeWebSocketHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author Fxz
 * @version 0.0.1
 * @date 2022/6/28 10:45
 */
@Service
@RequiredArgsConstructor
public class UserWsNoticeService {

	private final UserNoticeWebSocketHandler noticeWebSocketHandler;

	/**
	 * 服务端发送消息给客户端(指定用户)
	 */
	public <T> void sendMessageByUser(String message, Long userId) {
		noticeWebSocketHandler.sendMessageByUserId(message, userId);
	}

	/**
	 * 服务端发送消息给客户端(指定用户组)
	 */
	public <T> void sendMessageByUsers(String message, List<Long> userIds) {
		noticeWebSocketHandler.sendMessageByUsers(message, userIds);
	}

	/**
	 * 服务端发送消息给客户端(全体发送)
	 */
	public <T> void sendMessageByAll(String message) {
		noticeWebSocketHandler.sendMessageByAll(message);
	}

}
