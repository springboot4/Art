/*
 *   COPYRIGHT (C) 2023 Art AUTHORS(fxzcloud@gmail.com). ALL RIGHTS RESERVED.
 *
 *   Licensed under the Apache License, Version 2.0 (the "License");
 *   you may not use this file except in compliance with the License.
 *   You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 *   Unless required by applicable law or agreed to in writing, software
 *   distributed under the License is distributed on an "AS IS" BASIS,
 *   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *   See the License for the specific language governing permissions and
 *   limitations under the License.
 */

package com.art.common.websocket.core.support;

import com.art.common.websocket.core.handler.HttpServerHandler;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.codec.http.cors.CorsConfig;
import io.netty.handler.codec.http.cors.CorsConfigBuilder;
import io.netty.handler.codec.http.cors.CorsHandler;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import io.netty.handler.ssl.SslContext;
import io.netty.util.concurrent.DefaultEventExecutorGroup;
import io.netty.util.concurrent.EventExecutorGroup;
import io.netty.util.internal.logging.InternalLogger;
import io.netty.util.internal.logging.InternalLoggerFactory;
import org.springframework.util.ObjectUtils;

import javax.net.ssl.SSLException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.UnknownHostException;

public class WebsocketServer {

	private final WebSocketEndpointEventServer webSocketEndpointEventServer;

	private final WebSocketEndpointConfig webSocketEndpointConfig;

	private static final InternalLogger logger = InternalLoggerFactory.getInstance(WebsocketServer.class);

	public WebsocketServer(WebSocketEndpointEventServer webSocketServerHandler,
			WebSocketEndpointConfig webSocketEndpointConfig) {
		this.webSocketEndpointEventServer = webSocketServerHandler;
		this.webSocketEndpointConfig = webSocketEndpointConfig;
	}

	/**
	 * Start Netty
	 */
	public void init() throws InterruptedException {
		EventExecutorGroup eventExecutorGroup = null;
		final SslContext sslCtx;
		// SSL
		if (!ObjectUtils.isEmpty(webSocketEndpointConfig.getKeyStore())) {
			try {
				sslCtx = SslUtils.createSslContext(webSocketEndpointConfig.getKeyPassword(),
						webSocketEndpointConfig.getKeyStore(), webSocketEndpointConfig.getKeyStoreType(),
						webSocketEndpointConfig.getKeyStorePassword(), webSocketEndpointConfig.getTrustStore(),
						webSocketEndpointConfig.getTrustStoreType(), webSocketEndpointConfig.getTrustStorePassword());
			}
			catch (SSLException e) {
				throw new RuntimeException(e);
			}
		}
		else {
			sslCtx = null;
		}

		final CorsConfig corsConfig = createCorsConfig(webSocketEndpointConfig.getCorsOrigins(),
				webSocketEndpointConfig.getCorsAllowCredentials());

		if (webSocketEndpointConfig.isUseEventExecutorGroup()) {
			eventExecutorGroup = new DefaultEventExecutorGroup(
					webSocketEndpointConfig.getEventExecutorGroupThreads() == 0 ? 16
							: webSocketEndpointConfig.getEventExecutorGroupThreads());
		}

		EventLoopGroup bossGroup = new NioEventLoopGroup(webSocketEndpointConfig.getBossLoopGroupThreads());
		EventLoopGroup workerGroup = new NioEventLoopGroup(webSocketEndpointConfig.getWorkerLoopGroupThreads());

		ServerBootstrap bootstrap = new ServerBootstrap();
		EventExecutorGroup finalEventExecutorGroup = eventExecutorGroup;

		bootstrap.group(bossGroup, workerGroup)
			.channel(NioServerSocketChannel.class)
			// ConnectTimeoutMillis
			.option(ChannelOption.CONNECT_TIMEOUT_MILLIS, webSocketEndpointConfig.getConnectTimeoutMillis())
			// Waiting queue
			.option(ChannelOption.SO_BACKLOG, webSocketEndpointConfig.getSoBacklog())
			.childOption(ChannelOption.WRITE_SPIN_COUNT, webSocketEndpointConfig.getWriteSpinCount())
			.childOption(ChannelOption.WRITE_BUFFER_WATER_MARK,
					new WriteBufferWaterMark(webSocketEndpointConfig.getWriteBufferLowWaterMark(),
							webSocketEndpointConfig.getWriteBufferHighWaterMark()))
			.childOption(ChannelOption.TCP_NODELAY, webSocketEndpointConfig.isTcpNodelay())
			.childOption(ChannelOption.SO_KEEPALIVE, webSocketEndpointConfig.isSoKeepalive())
			.childOption(ChannelOption.SO_LINGER, webSocketEndpointConfig.getSoLinger())
			.childOption(ChannelOption.ALLOW_HALF_CLOSURE, webSocketEndpointConfig.isAllowHalfClosure())
			.handler(new LoggingHandler(LogLevel.DEBUG))
			.childHandler(new ChannelInitializer<NioSocketChannel>() {
				@Override
				protected void initChannel(NioSocketChannel ch) {
					ChannelPipeline pipeline = ch.pipeline();
					if (sslCtx != null) {
						pipeline.addFirst(sslCtx.newHandler(ch.alloc()));
					}

					// http解码器
					pipeline.addLast(new HttpServerCodec());
					// http聚合器
					pipeline.addLast(new HttpObjectAggregator(65536));

					if (corsConfig != null) {
						pipeline.addLast(new CorsHandler(corsConfig));
					}

					pipeline.addLast(new HttpServerHandler(webSocketEndpointEventServer, webSocketEndpointConfig,
							finalEventExecutorGroup, corsConfig != null));
				}
			});

		if (webSocketEndpointConfig.getSoRcvbuf() != -1) {
			bootstrap.childOption(ChannelOption.SO_RCVBUF, webSocketEndpointConfig.getSoRcvbuf());
		}

		if (webSocketEndpointConfig.getSoSndbuf() != -1) {
			bootstrap.childOption(ChannelOption.SO_SNDBUF, webSocketEndpointConfig.getSoSndbuf());
		}

		ChannelFuture channelFuture;
		if ("0.0.0.0".equals(webSocketEndpointConfig.getHost())) {
			channelFuture = bootstrap.bind(webSocketEndpointConfig.getPort());
		}
		else {
			try {
				channelFuture = bootstrap.bind(new InetSocketAddress(
						InetAddress.getByName(webSocketEndpointConfig.getHost()), webSocketEndpointConfig.getPort()));
			}
			catch (UnknownHostException e) {
				channelFuture = bootstrap.bind(webSocketEndpointConfig.getHost(), webSocketEndpointConfig.getPort());
				e.printStackTrace();
			}
		}

		channelFuture.addListener(future -> {
			if (!future.isSuccess()) {
				future.cause().printStackTrace();
			}
		});

		Runtime.getRuntime().addShutdownHook(new Thread(() -> {
			bossGroup.shutdownGracefully().syncUninterruptibly();
			workerGroup.shutdownGracefully().syncUninterruptibly();
		}));
	}

	/**
	 * Cross Configuration
	 * @param corsOrigins address
	 * @param corsAllowCredentials Whether credentials exist
	 * @return CorsConfig
	 */
	private CorsConfig createCorsConfig(String[] corsOrigins, Boolean corsAllowCredentials) {
		if (corsOrigins.length == 0) {
			return null;
		}
		CorsConfigBuilder corsConfigBuilder = null;

		for (String corsOrigin : corsOrigins) {
			if ("*".equals(corsOrigin)) {
				// any
				corsConfigBuilder = CorsConfigBuilder.forAnyOrigin();
				break;
			}
		}

		if (corsConfigBuilder == null) {
			corsConfigBuilder = CorsConfigBuilder.forOrigins(corsOrigins);
		}

		// Whether to send cookies
		if (corsAllowCredentials != null && corsAllowCredentials) {
			corsConfigBuilder.allowCredentials();
		}

		corsConfigBuilder.allowNullOrigin();
		return corsConfigBuilder.build();
	}

	/**
	 * Access to services
	 * @return WebSocketEndpointEventServer
	 */
	public WebSocketEndpointEventServer getEndpointServer() {
		return webSocketEndpointEventServer;
	}

}
