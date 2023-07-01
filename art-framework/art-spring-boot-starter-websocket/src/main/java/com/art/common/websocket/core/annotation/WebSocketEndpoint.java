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

package com.art.common.websocket.core.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author Fxz
 * @version 0.0.1
 * @date 2022/12/27 19:11
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface WebSocketEndpoint {

	/**
	 * WebSocket的path
	 * @return String
	 */
	String path() default "/";

	/**
	 * WebSocket的host,"0.0.0.0"即是所有本地地址
	 * @return String
	 */
	String host() default "0.0.0.0";

	/**
	 * 访问端口
	 * @return int
	 */
	int port() default 80;

	/**
	 * bossEventLoopGroup的线程数
	 * @return int
	 */
	int bossLoopGroupThreads() default 1;

	/**
	 * workerEventLoopGroup的线程数
	 * @return int
	 */
	int workerLoopGroupThreads() default 0;

	/**
	 * 是否添加WebSocketServerCompressionHandler到pipeline
	 * @return boolean
	 */
	boolean useCompressionHandler() default false;

	// ------------------------- option -------------------------

	/**
	 * 连接超时毫秒数
	 * @return int
	 */
	int optionConnectTimeoutMillis() default 30000;

	/**
	 * 服务端接受连接的队列长度，如果队列已满，客户端连接将被拒绝
	 * @return int
	 */
	int optionSoBacklog() default 128;

	// ------------------------- childOption -------------------------

	/**
	 * 一个Loop写操作执行的最大次数，默认值为16
	 * 也就是说，对于大数据量的写操作至多进行16次，如果16次仍没有全部写完数据，此时会提交一个新的写任务给EventLoop，任务将在下次调度继续执行
	 * 这样，其他的写请求才能被响应不会因为单个大数据量写请求而耽误
	 * @return int
	 */
	int childOptionWriteSpinCount() default 16;

	/**
	 * 写高水位标记，默认值64KB。如果Netty的写缓冲区中的字节超过该值，Channel的isWritable()返回False
	 * @return int
	 */
	int childOptionWriteBufferHighWaterMark() default 65536;

	/**
	 * 写低水位标记，默认值32KB。 当Netty的写缓冲区中的字节超过高水位之后若下降到低水位，则Channel的isWritable()返回True。
	 * 写高低水位标记使用户可以控制写入数据速度，从而实现流量控制。
	 * 推荐做法是：每次调用channel.write(msg)方法首先调用channel.isWritable()判断是否可写。
	 * @return int
	 */
	int childOptionWriteBufferLowWaterMark() default 32768;

	/**
	 * TCP数据接收缓冲区大小。 该缓冲区即TCP接收滑动窗口，linux操作系统可使用命令：cat /proc/sys/net/ipv4/tcp_rmem查询其大小。
	 * 一般情况下，该值可由用户在任意时刻设置，但当设置值超过64KB时，需要在连接到远端之前设置。
	 * @return int
	 */
	int childOptionSoRcvbuf() default -1;

	/**
	 * TCP数据发送缓冲区大小。 该缓冲区即TCP发送滑动窗口，linux操作系统可使用命令：cat /proc/sys/net/ipv4/tcp_smem查询其大小
	 * @return int
	 */
	int childOptionSoSndbuf() default -1;

	/**
	 * 立即发送数据，默认值为Ture（Netty默认为True而操作系统默认为False）。
	 * 该值设置Nagle算法的启用，该算法将小的碎片数据连接成更大的报文来最小化所发送的报文的数量，如果需要发送一些较小的报文，则需要禁用该算法。
	 * Netty默认禁用该算法，从而最小化报文传输延时
	 * @return boolean
	 */
	boolean childOptionTcpNodelay() default true;

	/**
	 * 连接保活，默认值为False。 启用该功能时，TCP会主动探测空闲连接的有效性。
	 * 可以将此功能视为TCP的心跳机制，需要注意的是：默认的心跳间隔是7200s即2小时。Netty默认关闭该功能。
	 * @return boolean
	 */
	boolean childOptionSoKeepalive() default false;

	/**
	 * 关闭Socket的延迟时间，默认值为-1，表示禁用该功能。 -1表示socket.close()方法立即返回，但OS底层会将发送缓冲区全部发送到对端。
	 * 0表示socket.close()方法立即返回，OS放弃发送缓冲区的数据直接向对端发送RST包，对端收到复位错误。
	 * 非0整数值表示调用socket.close()方法的线程被阻塞直到延迟时间到或发送缓冲区中的数据发送完毕，若超时，则对端会收到复位错误。
	 * @return int
	 */
	int childOptionSoLinger() default -1;

	/**
	 * 一个连接的远端关闭时本地端是否关闭，默认值为False。
	 * 值为False时，连接自动关闭；为True时，触发ChannelInboundHandler的userEventTriggered()方法，事件为ChannelInputShutdownEvent。
	 * @return boolean
	 */
	boolean childOptionAllowHalfClosure() default false;

	// ------------------------- idleEvent -------------------------

	/**
	 * 与IdleStateHandler中的readerIdleTimeSeconds一致，并且当它不为0时，将在pipeline中添加IdleStateHandler
	 * @return int
	 */
	int readerIdleTimeSeconds() default 0;

	/**
	 * 与IdleStateHandler中的writerIdleTimeSeconds一致，并且当它不为0时，将在pipeline中添加IdleStateHandler
	 * @return int
	 */
	int writerIdleTimeSeconds() default 0;

	/**
	 * 与IdleStateHandler中的allIdleTimeSeconds一致，并且当它不为0时，将在pipeline中添加IdleStateHandler
	 * @return int
	 */
	int allIdleTimeSeconds() default 0;

	// ------------------------- handshake -------------------------

	/**
	 * 最大允许帧载荷长度
	 * @return int
	 */
	int maxFramePayloadLength() default 65536;

	// ------------------------- eventExecutorGroup -------------------------

	/**
	 * 是否使用另一个线程池来执行耗时的同步业务逻辑
	 * @return boolean
	 */
	boolean useEventExecutorGroup() default true;

	/**
	 * eventExecutorGroup的线程数
	 * @return int
	 */
	int eventExecutorGroupThreads() default 16;

	// ------------------------- ssl (refer to spring Ssl) -------------------------

	/**
	 * {@link org.springframework.boot.web.server.Ssl}
	 */

	/**
	 * 与spring-boot的server.ssl.key-password一致
	 * @return String
	 */
	String sslKeyPassword() default "";

	/**
	 * 与spring-boot的server.ssl.key-store一致
	 * @return String
	 */
	String sslKeyStore() default "";

	/**
	 * 与spring-boot的server.ssl.key-store-password一致
	 * @return String
	 */
	String sslKeyStorePassword() default "";

	/**
	 * 与spring-boot的server.ssl.key-store-type一致
	 * @return String
	 */
	String sslKeyStoreType() default "";

	/**
	 * 与spring-boot的server.ssl.trust-store一致
	 * @return String
	 */
	String sslTrustStore() default "";

	/**
	 * 与spring-boot的server.ssl.trust-store-password一致
	 * @return String
	 */
	String sslTrustStorePassword() default "";

	/**
	 * 与spring-boot的server.ssl.trust-store-type一致
	 * @return String
	 */
	String sslTrustStoreType() default "";

	// ------------------------- cors (refer to spring CrossOrigin)
	// -------------------------

	/**
	 * {@link org.springframework.web.bind.annotation.CrossOrigin}
	 */

	/**
	 * 与spring boot的@CrossOrigin#origins一致
	 * @return String[]
	 */
	String[] corsOrigins() default {};

	/**
	 * 与spring boot的@CrossOrigin#allowCredentials一致
	 * @return String
	 */
	String corsAllowCredentials() default "";

}
