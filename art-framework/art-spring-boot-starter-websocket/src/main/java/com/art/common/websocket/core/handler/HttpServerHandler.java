package com.art.common.websocket.core.handler;

import com.art.common.websocket.core.support.WebSocketEndpointConfig;
import com.art.common.websocket.core.support.WebSocketEndpointEventServer;
import com.art.common.websocket.core.support.WsPathMatcher;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;
import io.netty.buffer.Unpooled;
import io.netty.channel.*;
import io.netty.handler.codec.http.*;
import io.netty.handler.codec.http.cors.CorsHandler;
import io.netty.handler.codec.http.websocketx.CloseWebSocketFrame;
import io.netty.handler.codec.http.websocketx.WebSocketFrameAggregator;
import io.netty.handler.codec.http.websocketx.WebSocketServerHandshaker;
import io.netty.handler.codec.http.websocketx.WebSocketServerHandshakerFactory;
import io.netty.handler.codec.http.websocketx.extensions.compression.WebSocketServerCompressionHandler;
import io.netty.handler.timeout.IdleStateHandler;
import io.netty.util.AttributeKey;
import io.netty.util.CharsetUtil;
import io.netty.util.concurrent.EventExecutorGroup;
import io.netty.util.internal.logging.InternalLogger;
import io.netty.util.internal.logging.InternalLoggerFactory;
import org.springframework.beans.TypeMismatchException;
import org.springframework.util.ObjectUtils;

import java.io.InputStream;
import java.util.Objects;
import java.util.Set;

import static io.netty.handler.codec.http.HttpHeaderNames.*;
import static io.netty.handler.codec.http.HttpMethod.GET;
import static io.netty.handler.codec.http.HttpResponseStatus.*;
import static io.netty.handler.codec.http.HttpVersion.HTTP_1_1;

public class HttpServerHandler extends SimpleChannelInboundHandler<FullHttpRequest> {

	private static final InternalLogger log = InternalLoggerFactory.getInstance(HttpServerHandler.class);

	/**
	 * 端点事件处理
	 */
	private final WebSocketEndpointEventServer webSocketEndpointEventServer;

	/**
	 * WebSocket配置获取
	 */
	private final WebSocketEndpointConfig webSocketEndpointConfig;

	private final EventExecutorGroup eventExecutorGroup;

	private final boolean isCors;

	private static ByteBuf faviconByteBuf = null;

	private static ByteBuf notFoundByteBuf = null;

	private static ByteBuf badRequestByteBuf = null;

	private static ByteBuf forbiddenByteBuf = null;

	private static ByteBuf internalServerErrorByteBuf = null;

	static {
		faviconByteBuf = buildStaticRes("/favicon.ico");
		notFoundByteBuf = buildStaticRes("/public/error/404.html");
		badRequestByteBuf = buildStaticRes("/public/error/400.html");
		forbiddenByteBuf = buildStaticRes("/public/error/403.html");
		internalServerErrorByteBuf = buildStaticRes("/public/error/500.html");
		if (notFoundByteBuf == null) {
			notFoundByteBuf = buildStaticRes("/public/error/4xx.html");
		}
		if (badRequestByteBuf == null) {
			badRequestByteBuf = buildStaticRes("/public/error/4xx.html");
		}
		if (forbiddenByteBuf == null) {
			forbiddenByteBuf = buildStaticRes("/public/error/4xx.html");
		}
		if (internalServerErrorByteBuf == null) {
			internalServerErrorByteBuf = buildStaticRes("/public/error/5xx.html");
		}
	}

	/**
	 * 加载静态资源
	 * @param resPath 资料路径
	 * @return ByteBuf
	 */
	private static ByteBuf buildStaticRes(String resPath) {
		try {
			InputStream inputStream = HttpServerHandler.class.getResourceAsStream(resPath);
			if (ObjectUtils.isEmpty(inputStream)) {
				return null;
			}

			int available = inputStream.available();
			if (available != 0) {
				byte[] bytes = new byte[available];
				inputStream.read(bytes);
				return ByteBufAllocator.DEFAULT.buffer(bytes.length).writeBytes(bytes);
			}
		}
		catch (Exception ignored) {
		}
		return null;
	}

	public HttpServerHandler(WebSocketEndpointEventServer webSocketEndpointEventServer,
			WebSocketEndpointConfig webSocketEndpointConfig, EventExecutorGroup eventExecutorGroup, boolean isCors) {
		this.webSocketEndpointEventServer = webSocketEndpointEventServer;
		this.webSocketEndpointConfig = webSocketEndpointConfig;
		this.eventExecutorGroup = eventExecutorGroup;
		this.isCors = isCors;
	}

	/**
	 * 接收消息
	 * @param ctx ChannelHandlerContext
	 * @param msg FullHttpRequest
	 */
	@Override
	protected void channelRead0(ChannelHandlerContext ctx, FullHttpRequest msg) {
		try {
			// 处理消息
			handleHttpRequest(ctx, msg);
		}
		catch (TypeMismatchException e) {
			FullHttpResponse res = new DefaultFullHttpResponse(HTTP_1_1, BAD_REQUEST);
			sendHttpResponse(ctx, msg, res);
			log.error(e);
		}
		catch (Exception e) {
			FullHttpResponse res;
			if (internalServerErrorByteBuf != null) {
				res = new DefaultFullHttpResponse(HTTP_1_1, INTERNAL_SERVER_ERROR,
						internalServerErrorByteBuf.retainedDuplicate());
			}
			else {
				res = new DefaultFullHttpResponse(HTTP_1_1, INTERNAL_SERVER_ERROR);
			}
			sendHttpResponse(ctx, msg, res);
			log.error(e);
		}
	}

	/**
	 * Processing error
	 * @param ctx ChannelHandlerContext
	 * @param cause Throwable
	 */
	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
		webSocketEndpointEventServer.doOnError(ctx.channel(), cause);
	}

	/**
	 * Close inactive connections
	 * @param ctx ChannelHandlerContext
	 */
	@Override
	public void channelInactive(ChannelHandlerContext ctx) throws Exception {
		webSocketEndpointEventServer.doOnClose(ctx.channel());
		super.channelInactive(ctx);
	}

	/**
	 * 消息处理
	 * @param ctx ChannelHandlerContext
	 * @param req FullHttpRequest
	 */
	private void handleHttpRequest(ChannelHandlerContext ctx, FullHttpRequest req) {
		FullHttpResponse res;
		// 请求错误
		if (!req.decoderResult().isSuccess()) {
			if (badRequestByteBuf != null) {
				res = new DefaultFullHttpResponse(HTTP_1_1, BAD_REQUEST, badRequestByteBuf.retainedDuplicate());
			}
			else {
				res = new DefaultFullHttpResponse(HTTP_1_1, BAD_REQUEST);
			}
			sendHttpResponse(ctx, req, res);
			return;
		}

		// 仅允许get方法
		if (!Objects.equals(req.method(), GET)) {
			if (forbiddenByteBuf != null) {
				res = new DefaultFullHttpResponse(HTTP_1_1, FORBIDDEN, forbiddenByteBuf.retainedDuplicate());
			}
			else {
				res = new DefaultFullHttpResponse(HTTP_1_1, FORBIDDEN);
			}
			sendHttpResponse(ctx, req, res);
			return;
		}

		HttpHeaders headers = req.headers();
		String host = headers.get(HttpHeaderNames.HOST);
		if (ObjectUtils.isEmpty(host)) {
			if (forbiddenByteBuf != null) {
				res = new DefaultFullHttpResponse(HTTP_1_1, FORBIDDEN, forbiddenByteBuf.retainedDuplicate());
			}
			else {
				res = new DefaultFullHttpResponse(HTTP_1_1, FORBIDDEN);
			}
			sendHttpResponse(ctx, req, res);
			return;
		}

		if (!ObjectUtils.isEmpty(webSocketEndpointEventServer.getHost())
				&& !webSocketEndpointEventServer.getHost().equals("0.0.0.0")
				&& !webSocketEndpointEventServer.getHost().equals(host.split(":")[0])) {
			if (forbiddenByteBuf != null) {
				res = new DefaultFullHttpResponse(HTTP_1_1, FORBIDDEN, forbiddenByteBuf.retainedDuplicate());
			}
			else {
				res = new DefaultFullHttpResponse(HTTP_1_1, FORBIDDEN);
			}
			sendHttpResponse(ctx, req, res);
			return;
		}

		QueryStringDecoder decoder = new QueryStringDecoder(req.uri());
		String path = decoder.path();
		if ("/favicon.ico".equals(path)) {
			if (faviconByteBuf != null) {
				res = new DefaultFullHttpResponse(HTTP_1_1, OK, faviconByteBuf.retainedDuplicate());
			}
			else {
				res = new DefaultFullHttpResponse(HTTP_1_1, NOT_FOUND);
			}
			sendHttpResponse(ctx, req, res);
			return;
		}

		Channel channel = ctx.channel();

		// 路径匹配
		String pattern = null;
		Set<WsPathMatcher> pathMatcherSet = webSocketEndpointEventServer.getPathMatcherSet();
		for (WsPathMatcher pathMatcher : pathMatcherSet) {
			if (pathMatcher.matchAndExtract(decoder, channel)) {
				pattern = pathMatcher.getPattern();
				break;
			}
		}

		if (pattern == null) {
			if (notFoundByteBuf != null) {
				res = new DefaultFullHttpResponse(HTTP_1_1, NOT_FOUND, notFoundByteBuf.retainedDuplicate());
			}
			else {
				res = new DefaultFullHttpResponse(HTTP_1_1, NOT_FOUND);
			}
			sendHttpResponse(ctx, req, res);
			return;
		}

		if (!req.headers().contains(UPGRADE) || !req.headers().contains(SEC_WEBSOCKET_KEY)
				|| !req.headers().contains(SEC_WEBSOCKET_VERSION)) {
			if (forbiddenByteBuf != null) {
				res = new DefaultFullHttpResponse(HTTP_1_1, FORBIDDEN, forbiddenByteBuf.retainedDuplicate());
			}
			else {
				res = new DefaultFullHttpResponse(HTTP_1_1, FORBIDDEN);
			}
			sendHttpResponse(ctx, req, res);
			return;
		}

		String subprotocols = null;

		// 握手前回调
		if (webSocketEndpointEventServer.hasBeforeHandshake(channel, pattern)) {
			webSocketEndpointEventServer.doBeforeHandshake(channel, req, pattern);
			if (!channel.isActive()) {
				return;
			}

			AttributeKey<String> subprotocolsAttrKey = AttributeKey.valueOf("subprotocols");
			if (channel.hasAttr(subprotocolsAttrKey)) {
				subprotocols = ctx.channel().attr(subprotocolsAttrKey).get();
			}
		}

		// 握手
		WebSocketServerHandshakerFactory wsFactory = new WebSocketServerHandshakerFactory(getWebSocketLocation(req),
				subprotocols, true, webSocketEndpointConfig.getMaxFramePayloadLength());
		WebSocketServerHandshaker webSocketServerHandshaker = wsFactory.newHandshaker(req);
		if (Objects.isNull(webSocketServerHandshaker)) {
			WebSocketServerHandshakerFactory.sendUnsupportedVersionResponse(channel);
		}
		else {
			ChannelPipeline pipeline = ctx.pipeline();
			pipeline.remove(ctx.name());

			if (webSocketEndpointConfig.getReaderIdleTimeSeconds() != 0
					|| webSocketEndpointConfig.getWriterIdleTimeSeconds() != 0
					|| webSocketEndpointConfig.getAllIdleTimeSeconds() != 0) {
				// 添加空闲状态处理器
				pipeline.addLast(new IdleStateHandler(webSocketEndpointConfig.getReaderIdleTimeSeconds(),
						webSocketEndpointConfig.getWriterIdleTimeSeconds(),
						webSocketEndpointConfig.getAllIdleTimeSeconds()));
			}
			if (webSocketEndpointConfig.isUseCompressionHandler()) {
				// 添加WebSocketServerCompressionHandler
				pipeline.addLast(new WebSocketServerCompressionHandler());
			}
			// 添加WebSocketFrameAggregator
			pipeline.addLast(new WebSocketFrameAggregator(Integer.MAX_VALUE));
			if (webSocketEndpointConfig.isUseEventExecutorGroup()) {
				pipeline.addLast(eventExecutorGroup, new WebSocketServerHandler(webSocketEndpointEventServer));
			}
			else {
				pipeline.addLast(new WebSocketServerHandler(webSocketEndpointEventServer));
			}

			String finalPattern = pattern;
			webSocketServerHandshaker.handshake(channel, req).addListener(future -> {
				if (future.isSuccess()) {
					if (isCors) {
						pipeline.remove(CorsHandler.class);
					}
					webSocketEndpointEventServer.doOnOpen(channel, req, finalPattern);
				}
				else {
					webSocketServerHandshaker.close(channel, new CloseWebSocketFrame());
				}
			});
		}

	}

	private static void sendHttpResponse(ChannelHandlerContext ctx, FullHttpRequest req, FullHttpResponse res) {
		// 如果响应getStatus代码不正常（200） 则生成错误页面
		int statusCode = res.status().code();
		if (statusCode != OK.code() && res.content().readableBytes() == 0) {
			ByteBuf buf = Unpooled.copiedBuffer(res.status().toString(), CharsetUtil.UTF_8);
			res.content().writeBytes(buf);
			buf.release();
		}
		HttpUtil.setContentLength(res, res.content().readableBytes());

		// 发送响应并在必要时关闭连接
		ChannelFuture f = ctx.channel().writeAndFlush(res);
		if (!HttpUtil.isKeepAlive(req) || statusCode != OK.code()) {
			f.addListener(ChannelFutureListener.CLOSE);
		}
	}

	private static String getWebSocketLocation(FullHttpRequest req) {
		String location = req.headers().get(HttpHeaderNames.HOST) + req.uri();
		return "ws://" + location;
	}

}