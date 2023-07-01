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

import org.springframework.util.ObjectUtils;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;

public class WebSocketEndpointConfig {

	/**
	 * 绑定地址
	 */
	private final String host;

	/**
	 * 端口
	 */
	private final int port;

	/**
	 * bossEventLoopGroup的线程数
	 */
	private final int bossLoopGroupThreads;

	/**
	 * workerEventLoopGroup的线程数
	 */
	private final int workerLoopGroupThreads;

	/**
	 * 是否添加WebSocketServerCompressionHandler到pipeline
	 */
	private final boolean useCompressionHandler;

	/**
	 * 连接超时毫秒数
	 */
	private final int connectTimeoutMillis;

	/**
	 * 服务端接受连接的队列长度，如果队列已满，客户端连接将被拒绝
	 */
	private final int soBacklog;

	/**
	 * 一个Loop写操作执行的最大次数，默认值为16。
	 * 也就是说，对于大数据量的写操作至多进行16次，如果16次仍没有全部写完数据，此时会提交一个新的写任务给EventLoop，任务将在下次调度继续执行。
	 * 这样，其他的写请求才能被响应不会因为单个大数据量写请求而耽误
	 */
	private final int writeSpinCount;

	/**
	 * 写高水位标记，默认值64KB。如果Netty的写缓冲区中的字节超过该值，Channel的isWritable()返回False
	 */
	private final int writeBufferHighWaterMark;

	/**
	 * 写低水位标记，默认值32KB。 当Netty的写缓冲区中的字节超过高水位之后若下降到低水位，则Channel的isWritable()返回True。
	 * 写高低水位标记使用户可以控制写入数据速度，从而实现流量控制。
	 * 推荐做法是：每次调用channel.write(msg)方法首先调用channel.isWritable()判断是否可写。
	 */
	private final int writeBufferLowWaterMark;

	/**
	 * TCP数据接收缓冲区大小。 该缓冲区即TCP接收滑动窗口，linux操作系统可使用命令：cat /proc/sys/net/ipv4/tcp_rmem查询其大小。
	 * 一般情况下，该值可由用户在任意时刻设置，但当设置值超过64KB时，需要在连接到远端之前设置。
	 */
	private final int soRcvbuf;

	/**
	 * TCP数据发送缓冲区大小。 该缓冲区即TCP发送滑动窗口，linux操作系统可使用命令：cat /proc/sys/net/ipv4/tcp_smem查询其大小
	 */
	private final int soSndbuf;

	/**
	 * 立即发送数据，默认值为Ture（Netty默认为True而操作系统默认为False）。
	 * 该值设置Nagle算法的启用，改算法将小的碎片数据连接成更大的报文来最小化所发送的报文的数量，如果需要发送一些较小的报文，则需要禁用该算法。
	 * Netty默认禁用该算法，从而最小化报文传输延时
	 */
	private final boolean tcpNodelay;

	/**
	 * 连接保活，默认值为False。 启用该功能时，TCP会主动探测空闲连接的有效性。
	 * 可以将此功能视为TCP的心跳机制，需要注意的是：默认的心跳间隔是7200s即2小时。Netty默认关闭该功能。
	 */
	private final boolean soKeepalive;

	/**
	 * 关闭Socket的延迟时间，默认值为-1，表示禁用该功能。 -1表示socket.close()方法立即返回，但OS底层会将发送缓冲区全部发送到对端。
	 * 0表示socket.close()方法立即返回，OS放弃发送缓冲区的数据直接向对端发送RST包，对端收到复位错误。
	 * 非0整数值表示调用socket.close()方法的线程被阻塞直到延迟时间到或发送缓冲区中的数据发送完毕，若超时，则对端会收到复位错误。
	 */
	private final int soLinger;

	/**
	 * 一个连接的远端关闭时本地端是否关闭，默认值为False。
	 * 值为False时，连接自动关闭；为True时，触发ChannelInboundHandler的userEventTriggered()方法，事件为ChannelInputShutdownEvent。
	 */
	private final boolean allowHalfClosure;

	// ------------------------- idleEvent -------------------------

	/**
	 * 与IdleStateHandler中的readerIdleTimeSeconds一致，并且当它不为0时，将在pipeline中添加IdleStateHandler
	 */
	private final int readerIdleTimeSeconds;

	/**
	 * 与IdleStateHandler中的writerIdleTimeSeconds一致，并且当它不为0时，将在pipeline中添加IdleStateHandler
	 */
	private final int writerIdleTimeSeconds;

	/**
	 * 与IdleStateHandler中的allIdleTimeSeconds一致，并且当它不为0时，将在pipeline中添加IdleStateHandler
	 */
	private final int allIdleTimeSeconds;

	/**
	 * 最大允许帧载荷长度
	 */
	private final int maxFramePayloadLength;

	/**
	 * 是否使用另一个线程池来执行耗时的同步业务逻辑
	 */
	private final boolean useEventExecutorGroup;

	/**
	 * eventExecutorGroup的线程数
	 */
	private final int eventExecutorGroupThreads;

	/***************************************************
	 * SSL
	 *******************************************************************/
	/**
	 * 与spring-boot的server.ssl.key-password一致
	 */
	private final String keyPassword;

	/**
	 * 与spring-boot的server.ssl.key-store一致
	 */
	private final String keyStore;

	/**
	 * 与spring-boot的server.ssl.key-store-password一致
	 */
	private final String keyStorePassword;

	/**
	 * 与spring-boot的server.ssl.key-store-type一致
	 */
	private final String keyStoreType;

	/**
	 * 与spring-boot的server.ssl.trust-store一致
	 */
	private final String trustStore;

	/**
	 * 与spring-boot的server.ssl.trust-store-password一致
	 */
	private final String trustStorePassword;

	/**
	 * 与spring-boot的server.ssl.trust-store-type一致
	 */
	private final String trustStoreType;

	/***************************************************
	 * 跨域
	 *******************************************************************/
	/**
	 * 与spring-boot的@CrossOrigin#origins一致
	 */
	private final String[] corsOrigins;

	/**
	 * 与spring-boot的@CrossOrigin#allowCredentials一致
	 */
	private final Boolean corsAllowCredentials;

	/**
	 * 随机端口
	 */
	private static Integer randomPort;

	public WebSocketEndpointConfig(String host, int port, int bossLoopGroupThreads, int workerLoopGroupThreads,
			boolean useCompressionHandler, int connectTimeoutMillis, int soBacklog, int writeSpinCount,
			int writeBufferHighWaterMark, int writeBufferLowWaterMark, int soRcvbuf, int soSndbuf, boolean tcpNodelay,
			boolean soKeepalive, int soLinger, boolean allowHalfClosure, int readerIdleTimeSeconds,
			int writerIdleTimeSeconds, int allIdleTimeSeconds, int maxFramePayloadLength, boolean useEventExecutorGroup,
			int eventExecutorGroupThreads, String keyPassword, String keyStore, String keyStorePassword,
			String keyStoreType, String trustStore, String trustStorePassword, String trustStoreType,
			String[] corsOrigins, Boolean corsAllowCredentials) {
		if (ObjectUtils.isEmpty(host) || "0.0.0.0".equals(host) || "0.0.0.0/0.0.0.0".equals(host)) {
			this.host = "0.0.0.0";
		}
		else {
			this.host = host;
		}
		this.port = getAvailablePort(port);
		this.bossLoopGroupThreads = bossLoopGroupThreads;
		this.workerLoopGroupThreads = workerLoopGroupThreads;
		this.useCompressionHandler = useCompressionHandler;
		this.connectTimeoutMillis = connectTimeoutMillis;
		this.soBacklog = soBacklog;
		this.writeSpinCount = writeSpinCount;
		this.writeBufferHighWaterMark = writeBufferHighWaterMark;
		this.writeBufferLowWaterMark = writeBufferLowWaterMark;
		this.soRcvbuf = soRcvbuf;
		this.soSndbuf = soSndbuf;
		this.tcpNodelay = tcpNodelay;
		this.soKeepalive = soKeepalive;
		this.soLinger = soLinger;
		this.allowHalfClosure = allowHalfClosure;
		this.readerIdleTimeSeconds = readerIdleTimeSeconds;
		this.writerIdleTimeSeconds = writerIdleTimeSeconds;
		this.allIdleTimeSeconds = allIdleTimeSeconds;
		this.maxFramePayloadLength = maxFramePayloadLength;
		this.useEventExecutorGroup = useEventExecutorGroup;
		this.eventExecutorGroupThreads = eventExecutorGroupThreads;

		this.keyPassword = keyPassword;
		this.keyStore = keyStore;
		this.keyStorePassword = keyStorePassword;
		this.keyStoreType = keyStoreType;
		this.trustStore = trustStore;
		this.trustStorePassword = trustStorePassword;
		this.trustStoreType = trustStoreType;

		this.corsOrigins = corsOrigins;
		this.corsAllowCredentials = corsAllowCredentials;
	}

	/**
	 * Get port or random port
	 * @param port port
	 * @return port
	 */
	private int getAvailablePort(int port) {
		if (port != 0) {
			return port;
		}
		if (randomPort != null && randomPort != 0) {
			return randomPort;
		}
		InetSocketAddress inetSocketAddress = new InetSocketAddress(0);
		Socket socket = new Socket();
		try {
			socket.bind(inetSocketAddress);
		}
		catch (IOException e) {
			e.printStackTrace();
		}
		int localPort = socket.getLocalPort();
		try {
			socket.close();
		}
		catch (IOException e) {
			e.printStackTrace();
		}
		randomPort = localPort;
		return localPort;
	}

	public String getHost() {
		return host;
	}

	public int getPort() {
		return port;
	}

	public int getBossLoopGroupThreads() {
		return bossLoopGroupThreads;
	}

	public int getWorkerLoopGroupThreads() {
		return workerLoopGroupThreads;
	}

	public boolean isUseCompressionHandler() {
		return useCompressionHandler;
	}

	public int getConnectTimeoutMillis() {
		return connectTimeoutMillis;
	}

	public int getSoBacklog() {
		return soBacklog;
	}

	public int getWriteSpinCount() {
		return writeSpinCount;
	}

	public int getWriteBufferHighWaterMark() {
		return writeBufferHighWaterMark;
	}

	public int getWriteBufferLowWaterMark() {
		return writeBufferLowWaterMark;
	}

	public int getSoRcvbuf() {
		return soRcvbuf;
	}

	public int getSoSndbuf() {
		return soSndbuf;
	}

	public boolean isTcpNodelay() {
		return tcpNodelay;
	}

	public boolean isSoKeepalive() {
		return soKeepalive;
	}

	public int getSoLinger() {
		return soLinger;
	}

	public boolean isAllowHalfClosure() {
		return allowHalfClosure;
	}

	public static Integer getRandomPort() {
		return randomPort;
	}

	public int getReaderIdleTimeSeconds() {
		return readerIdleTimeSeconds;
	}

	public int getWriterIdleTimeSeconds() {
		return writerIdleTimeSeconds;
	}

	public int getAllIdleTimeSeconds() {
		return allIdleTimeSeconds;
	}

	public int getMaxFramePayloadLength() {
		return maxFramePayloadLength;
	}

	public boolean isUseEventExecutorGroup() {
		return useEventExecutorGroup;
	}

	public int getEventExecutorGroupThreads() {
		return eventExecutorGroupThreads;
	}

	public String getKeyPassword() {
		return keyPassword;
	}

	public String getKeyStore() {
		return keyStore;
	}

	public String getKeyStorePassword() {
		return keyStorePassword;
	}

	public String getKeyStoreType() {
		return keyStoreType;
	}

	public String getTrustStore() {
		return trustStore;
	}

	public String getTrustStorePassword() {
		return trustStorePassword;
	}

	public String getTrustStoreType() {
		return trustStoreType;
	}

	public String[] getCorsOrigins() {
		return corsOrigins;
	}

	public Boolean getCorsAllowCredentials() {
		return corsAllowCredentials;
	}

}
