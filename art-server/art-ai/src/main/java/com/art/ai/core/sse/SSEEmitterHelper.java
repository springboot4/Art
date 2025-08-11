package com.art.ai.core.sse;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.util.Objects;

/**
 * @author fxz
 */
@RequiredArgsConstructor
@Slf4j
@Service
public class SSEEmitterHelper {

	private final StringRedisTemplate stringRedisTemplate;

	public void startSse(SseEmitter sseEmitter, String data) {
	}

	public void sendComplete(SseEmitter sseEmitter, String msg) {
		try {
			sseEmitter.send(SseEmitter.event().name(SSEEventNameConstants.DONE).data(msg));
		}
		catch (IOException e) {
			throw new RuntimeException(e);
		}
		sseEmitter.complete();
	}

	public void sendComplete(SseEmitter sseEmitter) {
		try {
			sseEmitter.send(SseEmitter.event().name(SSEEventNameConstants.DONE));
			sseEmitter.complete();
		}
		catch (Exception e) {
			log.warn("sendComplete error", e);
		}
	}

	public void sendStartAndComplete(SseEmitter sseEmitter, String msg) {
		try {
			sseEmitter.send(SseEmitter.event().name(SSEEventNameConstants.START));
			sseEmitter.send(SseEmitter.event().name(SSEEventNameConstants.DONE).data(msg));
		}
		catch (IOException e) {
			throw new RuntimeException(e);
		}
		sseEmitter.complete();
	}

	public void sendErrorAndComplete(SseEmitter sseEmitter, String errorMsg) {
		try {
			sseEmitter.send(SseEmitter.event().name(SSEEventNameConstants.ERROR).data(Objects.toString(errorMsg, "")));
		}
		catch (IOException e) {
			throw new RuntimeException(e);
		}
		sseEmitter.complete();
	}

	public static void parseAndSendPartialMsg(SseEmitter sseEmitter, String content) {
		parseAndSendPartialMsg(sseEmitter, "", content);
	}

	public static void sendThinking(SseEmitter sseEmitter, String content) {
		try {
			sseEmitter.send(SseEmitter.event().name(SSEEventNameConstants.THINKING).data(content));
		}
		catch (IOException e) {
			log.error("stream onNext error", e);
			throw new RuntimeException(e);
		}
	}

	public static void parseAndSendPartialMsg(SseEmitter sseEmitter, String name, String content) {
		try {
			String[] lines = content.split("[\\r\\n]", -1);
			if (lines.length > 1) {
				sendPartial(sseEmitter, name, " " + lines[0]);
				for (int i = 1; i < lines.length; i++) {
					sendPartial(sseEmitter, name, "-_wrap_-");
					sendPartial(sseEmitter, name, " " + lines[i]);
				}
			}
			else {
				sendPartial(sseEmitter, name, " " + content);
			}
		}
		catch (IOException e) {
			log.error("stream onNext error", e);
		}
	}

	private static void sendPartial(SseEmitter sseEmitter, String name, String msg) throws IOException {
		if (StringUtils.isNotBlank(name)) {
			sseEmitter.send(SseEmitter.event().name(name).data(msg));
		}
		else {
			sseEmitter.send(msg);
		}
	}

}
