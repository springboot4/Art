package com.art.common.websocket.core.support;

import com.art.common.websocket.core.annotation.WebSocketEndpoint;
import org.springframework.beans.TypeConverter;
import org.springframework.beans.TypeMismatchException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.beans.factory.SmartInitializingSingleton;
import org.springframework.beans.factory.config.BeanExpressionContext;
import org.springframework.beans.factory.config.BeanExpressionResolver;
import org.springframework.beans.factory.support.AbstractBeanFactory;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ApplicationObjectSupport;
import org.springframework.core.annotation.AnnotatedElementUtils;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.core.io.ResourceLoader;
import org.springframework.util.ClassUtils;

import javax.net.ssl.SSLException;
import java.net.InetSocketAddress;
import java.util.HashMap;
import java.util.Map;
import java.util.StringJoiner;

public class WebSocketEndpointExporter extends ApplicationObjectSupport
		implements SmartInitializingSingleton, BeanFactoryAware {

	/**
	 * 资源路径
	 */
	private final ResourceLoader resourceLoader;

	/**
	 * Bean工厂
	 */
	private AbstractBeanFactory beanFactory;

	public WebSocketEndpointExporter(ResourceLoader resourceLoader) {
		this.resourceLoader = resourceLoader;
	}

	/**
	 * 服务Map
	 */
	private final Map<InetSocketAddress, WebsocketServer> addressWebsocketServerMap = new HashMap<>();

	@Override
	public void setBeanFactory(BeanFactory beanFactory) {
		if (!(beanFactory instanceof AbstractBeanFactory)) {
			throw new IllegalArgumentException(
					"AutowiredAnnotationBeanPostProcessor requires a AbstractBeanFactory: " + beanFactory);
		}
		this.beanFactory = (AbstractBeanFactory) beanFactory;
	}

	/**
	 * 在所有bean都被放入到spring容器之后，执行WebSocket注入
	 */
	@Override
	public void afterSingletonsInstantiated() {
		registerEndpoints();
	}

	/**
	 * 注册实例beans
	 */
	protected void registerEndpoints() {
		// 容器管理
		ApplicationContext applicationContext = getApplicationContext();
		assert applicationContext != null;
		// add scan endpoint
		scanPackage(applicationContext);

		String[] endpointBeanNames = applicationContext.getBeanNamesForAnnotation(WebSocketEndpoint.class);
		for (String beanName : endpointBeanNames) {
			Class<?> endpointClass = applicationContext.getType(beanName);
			assert endpointClass != null;
			String name = endpointClass.getName();
			// Whether dynamic proxy
			if (name.contains(ClassUtils.CGLIB_CLASS_SEPARATOR)) {
				registerEndpoint(endpointClass.getSuperclass());
			}
			else {
				registerEndpoint(endpointClass);
			}
		}

		init();
	}

	/**
	 * add scan endpoint
	 * @param applicationContext ApplicationContext
	 */
	private void scanPackage(ApplicationContext applicationContext) {
		String[] basePackages = null;

		// 使用 @SpringBootApplication 的扫描路径
		String[] springBootApplicationBeanName = applicationContext
			.getBeanNamesForAnnotation(SpringBootApplication.class);
		Object springBootApplicationBean = applicationContext.getBean(springBootApplicationBeanName[0]);
		SpringBootApplication springBootApplication = AnnotationUtils
			.findAnnotation(springBootApplicationBean.getClass(), SpringBootApplication.class);
		assert springBootApplication != null;
		if (springBootApplication.scanBasePackages().length != 0) {
			basePackages = springBootApplication.scanBasePackages();
		}
		else {
			// Scan current application path
			String packageName = ClassUtils.getPackageName(springBootApplicationBean.getClass().getName());
			basePackages = new String[1];
			basePackages[0] = packageName;
		}

		EndpointClassPathScanner scanHandle = new EndpointClassPathScanner((BeanDefinitionRegistry) applicationContext,
				false);
		if (resourceLoader != null) {
			scanHandle.setResourceLoader(resourceLoader);
		}

		// scan custom path
		for (String basePackage : basePackages) {
			scanHandle.doScan(basePackage);
		}
	}

	/**
	 * 执行初始化
	 */
	private void init() {
		for (Map.Entry<InetSocketAddress, WebsocketServer> entry : addressWebsocketServerMap.entrySet()) {
			WebsocketServer websocketServer = entry.getValue();
			try {
				// Start Netty
				websocketServer.init();
				WebSocketEndpointEventServer webSocketEndpointEventServer = websocketServer.getEndpointServer();
				StringJoiner stringJoiner = new StringJoiner(",");
				webSocketEndpointEventServer.getPathMatcherSet()
					.forEach(pathMatcher -> stringJoiner.add("'" + pathMatcher.getPattern() + "'"));
				logger
					.info(String.format("\033[34mNetty WebSocket started on port: %s with context path(s): %s .\033[0m",
							webSocketEndpointEventServer.getPort(), stringJoiner));
			}
			catch (InterruptedException e) {
				logger.error(String.format("websocket [%s] init fail", entry.getKey()), e);
			}
			catch (SSLException e) {
				logger.error(String.format("websocket [%s] ssl create fail", entry.getKey()), e);

			}
		}
	}

	/**
	 * 注册端点
	 * @param endpointClass Class<?>
	 */
	private void registerEndpoint(Class<?> endpointClass) {
		WebSocketEndpoint annotation = AnnotatedElementUtils.findMergedAnnotation(endpointClass,
				WebSocketEndpoint.class);
		if (annotation == null) {
			throw new IllegalStateException("missingAnnotation WebSocketEndpoint");
		}

		// Set websocket parameters
		WebSocketEndpointConfig webSocketEndpointConfig = buildWebSocketConfig(annotation);

		ApplicationContext context = getApplicationContext();
		WebSocketEndpointMethodMapping webSocketEndpointMethodMapping;
		try {
			webSocketEndpointMethodMapping = new WebSocketEndpointMethodMapping(endpointClass, context, beanFactory);
		}
		catch (Exception e) {
			throw new IllegalStateException("Failed to register WebSocketEndpointConfig: " + webSocketEndpointConfig,
					e);
		}

		InetSocketAddress inetSocketAddress = new InetSocketAddress(webSocketEndpointConfig.getHost(),
				webSocketEndpointConfig.getPort());

		String path = resolveAnnotationValue(annotation.path(), String.class, "path");

		WebsocketServer websocketServer = addressWebsocketServerMap.get(inetSocketAddress);
		if (websocketServer == null) {
			// Add Server
			WebSocketEndpointEventServer webSocketEndpointEventServer = new WebSocketEndpointEventServer(
					webSocketEndpointMethodMapping, webSocketEndpointConfig, path);
			websocketServer = new WebsocketServer(webSocketEndpointEventServer, webSocketEndpointConfig);
			addressWebsocketServerMap.put(inetSocketAddress, websocketServer);
		}
		else {
			// Add path to the same port
			websocketServer.getEndpointServer().addPathMethodMapping(path, webSocketEndpointMethodMapping);
		}
	}

	/**
	 * set websocket parameters
	 * @param annotation WebSocketEndpoint
	 * @return WebSocketEndpointConfig
	 */
	private WebSocketEndpointConfig buildWebSocketConfig(WebSocketEndpoint annotation) {

		String host = resolveAnnotationValue(annotation.host(), String.class, "host");
		int port = resolveAnnotationValue(annotation.port(), Integer.class, "port");
		int bossLoopGroupThreads = resolveAnnotationValue(annotation.bossLoopGroupThreads(), Integer.class,
				"bossLoopGroupThreads");
		int workerLoopGroupThreads = resolveAnnotationValue(annotation.workerLoopGroupThreads(), Integer.class,
				"workerLoopGroupThreads");
		boolean useCompressionHandler = resolveAnnotationValue(annotation.useCompressionHandler(), Boolean.class,
				"useCompressionHandler");

		int optionConnectTimeoutMillis = resolveAnnotationValue(annotation.optionConnectTimeoutMillis(), Integer.class,
				"optionConnectTimeoutMillis");
		int optionSoBacklog = resolveAnnotationValue(annotation.optionSoBacklog(), Integer.class, "optionSoBacklog");

		int childOptionWriteSpinCount = resolveAnnotationValue(annotation.childOptionWriteSpinCount(), Integer.class,
				"childOptionWriteSpinCount");
		int childOptionWriteBufferHighWaterMark = resolveAnnotationValue(
				annotation.childOptionWriteBufferHighWaterMark(), Integer.class, "childOptionWriteBufferHighWaterMark");
		int childOptionWriteBufferLowWaterMark = resolveAnnotationValue(annotation.childOptionWriteBufferLowWaterMark(),
				Integer.class, "childOptionWriteBufferLowWaterMark");
		int childOptionSoRcvbuf = resolveAnnotationValue(annotation.childOptionSoRcvbuf(), Integer.class,
				"childOptionSoRcvbuf");
		int childOptionSoSndbuf = resolveAnnotationValue(annotation.childOptionSoSndbuf(), Integer.class,
				"childOptionSoSndbuf");
		boolean childOptionTcpNodelay = resolveAnnotationValue(annotation.childOptionTcpNodelay(), Boolean.class,
				"childOptionTcpNodelay");
		boolean childOptionSoKeepalive = resolveAnnotationValue(annotation.childOptionSoKeepalive(), Boolean.class,
				"childOptionSoKeepalive");
		int childOptionSoLinger = resolveAnnotationValue(annotation.childOptionSoLinger(), Integer.class,
				"childOptionSoLinger");
		boolean childOptionAllowHalfClosure = resolveAnnotationValue(annotation.childOptionAllowHalfClosure(),
				Boolean.class, "childOptionAllowHalfClosure");

		int readerIdleTimeSeconds = resolveAnnotationValue(annotation.readerIdleTimeSeconds(), Integer.class,
				"readerIdleTimeSeconds");
		int writerIdleTimeSeconds = resolveAnnotationValue(annotation.writerIdleTimeSeconds(), Integer.class,
				"writerIdleTimeSeconds");
		int allIdleTimeSeconds = resolveAnnotationValue(annotation.allIdleTimeSeconds(), Integer.class,
				"allIdleTimeSeconds");

		int maxFramePayloadLength = resolveAnnotationValue(annotation.maxFramePayloadLength(), Integer.class,
				"maxFramePayloadLength");

		boolean useEventExecutorGroup = resolveAnnotationValue(annotation.useEventExecutorGroup(), Boolean.class,
				"useEventExecutorGroup");
		int eventExecutorGroupThreads = resolveAnnotationValue(annotation.eventExecutorGroupThreads(), Integer.class,
				"eventExecutorGroupThreads");

		String sslKeyPassword = resolveAnnotationValue(annotation.sslKeyPassword(), String.class, "sslKeyPassword");
		String sslKeyStore = resolveAnnotationValue(annotation.sslKeyStore(), String.class, "sslKeyStore");
		String sslKeyStorePassword = resolveAnnotationValue(annotation.sslKeyStorePassword(), String.class,
				"sslKeyStorePassword");
		String sslKeyStoreType = resolveAnnotationValue(annotation.sslKeyStoreType(), String.class, "sslKeyStoreType");
		String sslTrustStore = resolveAnnotationValue(annotation.sslTrustStore(), String.class, "sslTrustStore");
		String sslTrustStorePassword = resolveAnnotationValue(annotation.sslTrustStorePassword(), String.class,
				"sslTrustStorePassword");
		String sslTrustStoreType = resolveAnnotationValue(annotation.sslTrustStoreType(), String.class,
				"sslTrustStoreType");

		// corsOrigins
		Boolean corsAllowCredentials = resolveAnnotationValue(annotation.corsAllowCredentials(), Boolean.class,
				"corsAllowCredentials");
		String[] corsOrigins = annotation.corsOrigins();
		if (corsOrigins.length != 0) {
			for (int i = 0; i < corsOrigins.length; i++) {
				corsOrigins[i] = resolveAnnotationValue(corsOrigins[i], String.class, "corsOrigins");
			}
		}

		return new WebSocketEndpointConfig(host, port, bossLoopGroupThreads, workerLoopGroupThreads,
				useCompressionHandler, optionConnectTimeoutMillis, optionSoBacklog, childOptionWriteSpinCount,
				childOptionWriteBufferHighWaterMark, childOptionWriteBufferLowWaterMark, childOptionSoRcvbuf,
				childOptionSoSndbuf, childOptionTcpNodelay, childOptionSoKeepalive, childOptionSoLinger,
				childOptionAllowHalfClosure, readerIdleTimeSeconds, writerIdleTimeSeconds, allIdleTimeSeconds,
				maxFramePayloadLength, useEventExecutorGroup, eventExecutorGroupThreads, sslKeyPassword, sslKeyStore,
				sslKeyStorePassword, sslKeyStoreType, sslTrustStore, sslTrustStorePassword, sslTrustStoreType,
				corsOrigins, corsAllowCredentials);
	}

	/**
	 * Set parameters and support SpEL expressions
	 * @param value value
	 * @param requiredType Parameter type
	 * @param paramName paramName
	 * @param <T> T
	 * @return T
	 */
	private <T> T resolveAnnotationValue(Object value, Class<T> requiredType, String paramName) {

		if (value instanceof String) {
			// SpEL Get configuration
			String strVal = beanFactory.resolveEmbeddedValue((String) value);
			BeanExpressionResolver beanExpressionResolver = beanFactory.getBeanExpressionResolver();
			if (beanExpressionResolver != null) {
				// SpEL operation
				value = beanExpressionResolver.evaluate(strVal, new BeanExpressionContext(beanFactory, null));
			}
			else {
				value = strVal;
			}
		}

		// convert type
		try {
			TypeConverter typeConverter = beanFactory.getTypeConverter();
			return typeConverter.convertIfNecessary(value, requiredType);
		}
		catch (TypeMismatchException e) {
			throw new IllegalArgumentException("Failed to convert value of parameter '" + paramName
					+ "' to required type '" + requiredType.getName() + "'");
		}
	}

}
