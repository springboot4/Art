package com.art.demos.demo;

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
