package com.art.demos.core.http.feign.java;

import feign.Request;
import feign.Response;
import feign.Util;
import org.apache.hc.client5.http.classic.HttpClient;
import org.apache.hc.client5.http.config.Configurable;
import org.apache.hc.client5.http.config.RequestConfig;
import org.apache.hc.client5.http.entity.GzipCompressingEntity;
import org.apache.hc.client5.http.impl.classic.HttpClientBuilder;
import org.apache.hc.client5.http.protocol.HttpClientContext;
import org.apache.hc.core5.http.ClassicHttpRequest;
import org.apache.hc.core5.http.ClassicHttpResponse;
import org.apache.hc.core5.http.ContentType;
import org.apache.hc.core5.http.Header;
import org.apache.hc.core5.http.HttpEntity;
import org.apache.hc.core5.http.HttpHost;
import org.apache.hc.core5.http.NameValuePair;
import org.apache.hc.core5.http.io.entity.ByteArrayEntity;
import org.apache.hc.core5.http.io.entity.EntityUtils;
import org.apache.hc.core5.http.io.entity.StringEntity;
import org.apache.hc.core5.http.io.support.ClassicRequestBuilder;
import org.apache.hc.core5.net.URIBuilder;
import org.apache.hc.core5.net.URLEncodedUtils;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static feign.Util.UTF_8;
import static feign.Util.enumForName;

/**
 * ApacheHttp5Client实现 This module directs Feign's http requests to Apache's
 * <a href="https://hc.apache.org/httpcomponents-client-5.0.x/index.html">HttpClient
 * 5</a>. Ex.
 *
 * <pre> GitHub github = Feign.builder().client(new
 * ApacheHttp5Client()).target(GitHub.class, "https://api.github.com");
 */
/*
 */
public final class ApacheHttp5Client implements Client {

	private static final String ACCEPT_HEADER_NAME = "Accept";

	private final HttpClient client;

	public ApacheHttp5Client() {
		this(HttpClientBuilder.create().build());
	}

	public ApacheHttp5Client(HttpClient client) {
		this.client = client;
	}

	@Override
	public Response execute(Request request, Request.Options options) throws IOException {
		ClassicHttpRequest httpUriRequest;
		try {
			// 转成apache的request
			httpUriRequest = toClassicHttpRequest(request, options);
		}
		catch (final URISyntaxException e) {
			throw new IOException("URL '" + request.url() + "' couldn't be parsed into a URI", e);
		}

		final HttpHost target = HttpHost.create(URI.create(request.url()));
		final HttpClientContext context = configureTimeoutsAndRedirection(options);

		final ClassicHttpResponse httpResponse = (ClassicHttpResponse) client.execute(target, httpUriRequest, context);
		// 转成feign的response
		return toFeignResponse(httpResponse, request);
	}

	protected HttpClientContext configureTimeoutsAndRedirection(Request.Options options) {
		final HttpClientContext context = new HttpClientContext();
		// per request timeouts
		final RequestConfig requestConfig = (client instanceof Configurable
				? RequestConfig.copy(((Configurable) client).getConfig()) : RequestConfig.custom())
			// 连接超时时间
			.setConnectTimeout(options.connectTimeout(), options.connectTimeoutUnit())
			// 读取超时时间
			.setResponseTimeout(options.readTimeout(), options.readTimeoutUnit())
			// 重定向配置
			.setRedirectsEnabled(options.isFollowRedirects())
			.build();
		context.setRequestConfig(requestConfig);
		return context;
	}

	ClassicHttpRequest toClassicHttpRequest(Request request, Request.Options options) throws URISyntaxException {
		// 创建apache的request 配置方法类型
		final ClassicRequestBuilder requestBuilder = ClassicRequestBuilder.create(request.httpMethod().name());

		// 请求的url
		final URI uri = new URIBuilder(request.url()).build();
		requestBuilder.setUri(uri.getScheme() + "://" + uri.getAuthority() + uri.getRawPath());

		// 请求查询参数
		final List<NameValuePair> queryParams = URLEncodedUtils.parse(uri, requestBuilder.getCharset());
		for (final NameValuePair queryParam : queryParams) {
			requestBuilder.addParameter(queryParam);
		}

		// 请求头
		boolean hasAcceptHeader = false;
		boolean isGzip = false;
		for (final Map.Entry<String, Collection<String>> headerEntry : request.headers().entrySet()) {
			final String headerName = headerEntry.getKey();
			if (headerName.equalsIgnoreCase(ACCEPT_HEADER_NAME)) {
				hasAcceptHeader = true;
			}

			if (headerName.equalsIgnoreCase(Util.CONTENT_LENGTH)) {
				// The 'Content-Length' header is always set by the Apache client and it
				// doesn't like us to set it as well.
				continue;
			}
			if (headerName.equalsIgnoreCase(Util.CONTENT_ENCODING)) {
				isGzip = headerEntry.getValue().stream().anyMatch(Util.ENCODING_GZIP::equalsIgnoreCase);
				boolean isDeflate = headerEntry.getValue().stream().anyMatch(Util.ENCODING_DEFLATE::equalsIgnoreCase);
				if (isDeflate) {
					// DeflateCompressingEntity not available in hc5 yet
					throw new IllegalArgumentException("Deflate Content-Encoding is not supported by feign-hc5");
				}
			}
			for (final String headerValue : headerEntry.getValue()) {
				requestBuilder.addHeader(headerName, headerValue);
			}
		}
		// some servers choke on the default accept string, so we'll set it to anything
		if (!hasAcceptHeader) {
			// 在HTTP请求中，"Accept" 头部用于指示客户端所能接受的响应内容类型。如果客户端不明确指定 "Accept"
			// 头部，服务器可能会根据默认规则来决定响应内容的类型。
			requestBuilder.addHeader(ACCEPT_HEADER_NAME, "*/*");
		}

		// 请求体
		byte[] data = request.body();
		if (data != null) {
			HttpEntity entity;
			if (request.isBinary()) {
				entity = new ByteArrayEntity(data, null);
			}
			else {
				// 发送给服务器或接收自服务器的实体正文的媒体类型。它告诉服务器实际发送的数据是什么类型的
				final ContentType contentType = getContentType(request);
				String content;
				if (request.charset() != null) {
					content = new String(data, request.charset());
				}
				else {
					content = new String(data);
				}
				entity = new StringEntity(content, contentType);
			}
			if (isGzip) {
				entity = new GzipCompressingEntity(entity);
			}
			requestBuilder.setEntity(entity);
		}
		else {
			requestBuilder.setEntity(new ByteArrayEntity(new byte[0], null));
		}

		return requestBuilder.build();
	}

	/**
	 * Content-Type
	 * 是一个HTTP标头（header），用于指示发送给服务器或接收自服务器的实体正文的媒体类型。它告诉服务器实际发送的数据是什么类型的，以便服务器能够正确地处理这些数据。
	 * <p>
	 * 在HTTP请求中，Content-Type 通常由客户端设置，用来描述请求正文的数据类型。比如，如果请求包含了JSON格式的数据，那么Content-Type
	 * 可能被设置为 application/json。
	 * <p>
	 * 在HTTP响应中，Content-Type 标头由服务器设置，用于指示所返回数据的媒体类型。例如，当服务器返回HTML内容时，Content-Type 可能被设置为
	 * text/html。
	 */
	private ContentType getContentType(Request request) {
		ContentType contentType = null;
		for (final Map.Entry<String, Collection<String>> entry : request.headers().entrySet()) {
			if (entry.getKey().equalsIgnoreCase("Content-Type")) {
				final Collection<String> values = entry.getValue();
				if (values != null && !values.isEmpty()) {
					contentType = ContentType.parse(values.iterator().next());
					if (contentType.getCharset() == null) {
						contentType = contentType.withCharset(request.charset());
					}
					break;
				}
			}
		}
		return contentType;
	}

	Response toFeignResponse(ClassicHttpResponse httpResponse, Request request) throws IOException {
		// 响应状态码
		final int statusCode = httpResponse.getCode();
		// 响应状态码描述
		final String reason = httpResponse.getReasonPhrase();

		// 响应头
		final Map<String, Collection<String>> headers = new HashMap<>();
		for (final Header header : httpResponse.getHeaders()) {
			final String name = header.getName();
			final String value = header.getValue();

			Collection<String> headerValues = headers.get(name);
			if (headerValues == null) {
				headerValues = new ArrayList<>();
				headers.put(name, headerValues);
			}
			headerValues.add(value);
		}

		return Response.builder()
			// http协议版本
			.protocolVersion(enumForName(Request.ProtocolVersion.class, httpResponse.getVersion().format()))
			// 状态码
			.status(statusCode)
			// 状态码描述
			.reason(reason)
			// 响应头
			.headers(headers)
			// 请求
			.request(request)
			// 响应体
			.body(toFeignBody(httpResponse))
			.build();
	}

	Response.Body toFeignBody(ClassicHttpResponse httpResponse) {
		final HttpEntity entity = httpResponse.getEntity();
		if (entity == null) {
			return null;
		}
		return new Response.Body() {

			@Override
			public Integer length() {
				return entity.getContentLength() >= 0 && entity.getContentLength() <= Integer.MAX_VALUE
						? (int) entity.getContentLength() : null;
			}

			@Override
			public boolean isRepeatable() {
				return entity.isRepeatable();
			}

			@Override
			public InputStream asInputStream() throws IOException {
				return entity.getContent();
			}

			@Override
			public Reader asReader() throws IOException {
				return new InputStreamReader(asInputStream(), UTF_8);
			}

			@Override
			public Reader asReader(Charset charset) throws IOException {
				Util.checkNotNull(charset, "charset should not be null");
				return new InputStreamReader(asInputStream(), charset);
			}

			@Override
			public void close() throws IOException {
				try {
					EntityUtils.consume(entity);
				}
				finally {
					httpResponse.close();
				}
			}
		};
	}

}
