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

package com.art.demos.core.websocket;

import com.art.common.websocket.core.annotation.*;
import com.art.common.websocket.core.support.NettySession;
import io.netty.handler.codec.http.HttpHeaders;
import io.netty.handler.timeout.IdleStateEvent;
import org.springframework.util.MultiValueMap;

import java.io.IOException;
import java.util.Map;

/**
 * @author Fxz
 * @version 0.0.1
 * @date 2022/12/18 13:39
 */
@WebSocketEndpoint(path = "/ws/{arg}")
public class MyWebSocket {

	@BeforeHandshake
	public void handshake(NettySession nettySession, HttpHeaders headers, @RequestParam String req,
			@RequestParam MultiValueMap reqMap, @PathVariable String arg, @PathVariable Map pathMap) {
		nettySession.setSubprotocols("stomp");
		if (!"ok".equals(req)) {
			System.out.println("Authentication failed!");
			// nettySession.close();
		}
	}

	@OnOpen
	public void onOpen(NettySession nettySession, HttpHeaders headers, @RequestParam String req,
			@RequestParam MultiValueMap reqMap, @PathVariable String arg, @PathVariable Map pathMap) {
		System.out.println("new connection");
		System.out.println(req);
	}

	@OnClose
	public void onClose(NettySession nettySession) throws IOException {
		System.out.println("one connection closed");
	}

	@OnError
	public void onError(NettySession nettySession, Throwable throwable) {
		throwable.printStackTrace();
	}

	@OnMessage
	public void onMessage(NettySession nettySession, String message) {
		System.out.println(message);
		nettySession.sendText("Hello Netty!");
	}

	@OnBinary
	public void onBinary(NettySession nettySession, byte[] bytes) {
		for (byte b : bytes) {
			System.out.println(b);
		}
		nettySession.sendBinary(bytes);
	}

	@OnEvent
	public void onEvent(NettySession nettySession, Object evt) {
		if (evt instanceof IdleStateEvent) {
			IdleStateEvent idleStateEvent = (IdleStateEvent) evt;
			switch (idleStateEvent.state()) {
			case READER_IDLE:
				System.out.println("read idle");
				break;
			case WRITER_IDLE:
				System.out.println("write idle");
				break;
			case ALL_IDLE:
				System.out.println("all idle");
				break;
			default:
				break;
			}
		}
	}

}
