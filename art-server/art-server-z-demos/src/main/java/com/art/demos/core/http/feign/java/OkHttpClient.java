package com.art.demos.core.http.feign.java;

import feign.AsyncClient;
import feign.Request.HttpMethod;
import feign.Request.ProtocolVersion;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Headers;
import okhttp3.MediaType;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;

import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.nio.charset.Charset;
import java.util.Collection;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

import static feign.Util.enumForName;

/**
 * This module directs Feign's http requests to
 * <a href="http://square.github.io/okhttp/">OkHttp</a>, which enables SPDY and better
 * network control. Ex.
 *
 * <pre> GitHub github = Feign.builder().client(new OkHttpClient()).target(GitHub.class,
 * "https://api.github.com");
 */
public final class OkHttpClient implements Client, AsyncClient<Object> {

	private final okhttp3.OkHttpClient delegate;

	public OkHttpClient() {
		this(new okhttp3.OkHttpClient());
	}

	public OkHttpClient(okhttp3.OkHttpClient delegate) {
		this.delegate = delegate;
	}

	static Request toOkHttpRequest(feign.Request input) {
		// 创建一个OkHttp的Request.Builder
		Request.Builder requestBuilder = new Request.Builder();
		// 请求的URL
		requestBuilder.url(input.url());

		// 创建和解析特定的媒体类型，以便在HTTP通信中指定正确的数据格式
		MediaType mediaType = null;

		boolean hasAcceptHeader = false;
		for (String field : input.headers().keySet()) {
			if (field.equalsIgnoreCase("Accept")) {
				hasAcceptHeader = true;
			}

			for (String value : input.headers().get(field)) {
				requestBuilder.addHeader(field, value);
				if (field.equalsIgnoreCase("Content-Type")) {
					mediaType = MediaType.parse(value);
					if (input.charset() != null) {
						mediaType.charset(input.charset());
					}
				}
			}
		}
		if (!hasAcceptHeader) {
			// 在HTTP请求中，"Accept" 头部用于指示客户端所能接受的响应内容类型。如果客户端不明确指定 "Accept"
			// 头部，服务器可能会根据默认规则来决定响应内容的类型。
			requestBuilder.addHeader("Accept", "*/*");
		}

		byte[] inputBody = input.body();
		// 这几种方法都是有请求体的
		boolean isMethodWithBody = HttpMethod.POST == input.httpMethod() || HttpMethod.PUT == input.httpMethod()
				|| HttpMethod.PATCH == input.httpMethod();
		if (isMethodWithBody) {
			requestBuilder.removeHeader("Content-Type");
			if (inputBody == null) {
				// write an empty BODY to conform with okhttp 2.4.0+
				// http://johnfeng.github.io/blog/2015/06/30/okhttp-updates-post-wouldnt-be-allowed-to-have-null-body/
				inputBody = new byte[0];
			}
		}

		// 处理请求体
		RequestBody body = inputBody != null ? RequestBody.create(mediaType, inputBody) : null;
		requestBuilder.method(input.httpMethod().name(), body);

		// 构建OkHttp的Request
		return requestBuilder.build();
	}

	private static feign.Response toFeignResponse(Response response, feign.Request request) throws IOException {
		return feign.Response.builder()
			// HTTP协议版本
			.protocolVersion(enumForName(ProtocolVersion.class, response.protocol()))
			// HTTP状态码
			.status(response.code())
			// HTTP状态码的原因短语
			.reason(response.message())
			// HTTP请求信息
			.request(request)
			// HTTP响应头
			.headers(toMap(response.headers()))
			// HTTP响应体
			.body(toBody(response.body()))
			.build();
	}

	private static Map<String, Collection<String>> toMap(Headers headers) {
		return (Map) headers.toMultimap();
	}

	/**
	 * 将OkHttp的ResponseBody转换为Feign的Response.Body
	 */
	private static feign.Response.Body toBody(final ResponseBody input) throws IOException {
		if (input == null || input.contentLength() == 0) {
			if (input != null) {
				input.close();
			}
			return null;
		}
		final Integer length = input.contentLength() >= 0 && input.contentLength() <= Integer.MAX_VALUE
				? (int) input.contentLength() : null;

		return new feign.Response.Body() {

			@Override
			public void close() throws IOException {
				input.close();
			}

			@Override
			public Integer length() {
				return length;
			}

			@Override
			public boolean isRepeatable() {
				return false;
			}

			@Override
			public InputStream asInputStream() throws IOException {
				return input.byteStream();
			}

			@SuppressWarnings("deprecation")
			@Override
			public Reader asReader() throws IOException {
				return input.charStream();
			}

			@Override
			public Reader asReader(Charset charset) throws IOException {
				return asReader();
			}
		};
	}

	/**
	 * 根据配置获取OkHttpClient
	 */
	private okhttp3.OkHttpClient getClient(feign.Request.Options options) {
		// 表示本次请求使用的配置
		okhttp3.OkHttpClient requestScoped;
		// 连接超时时间、读取超时时间、是否跟随重定向和delegate的配置不一样 则应用这次的配置
		if (delegate.connectTimeoutMillis() != options.connectTimeoutMillis()
				|| delegate.readTimeoutMillis() != options.readTimeoutMillis()
				|| delegate.followRedirects() != options.isFollowRedirects()) {
			requestScoped = delegate.newBuilder()
				.connectTimeout(options.connectTimeoutMillis(), TimeUnit.MILLISECONDS)
				.readTimeout(options.readTimeoutMillis(), TimeUnit.MILLISECONDS)
				.followRedirects(options.isFollowRedirects())
				.build();
		}
		else {
			requestScoped = delegate;
		}

		return requestScoped;
	}

	@Override
	public feign.Response execute(feign.Request input, feign.Request.Options options) throws IOException {
		// 根据配置获取OkHttpClient
		okhttp3.OkHttpClient requestScoped = getClient(options);
		// 根据Feign的Request构建OkHttp的Request
		Request request = toOkHttpRequest(input);
		// 创建一个新的 HTTP 请求，并返回一个 Call 对象，这个对象代表了将要执行的 HTTP 请求。通过 Call
		// 对象，可以对请求进行配置、执行、取消等操作。
		Response response = requestScoped.newCall(request).execute();
		// 将OkHttp的Response转换为Feign的Response
		return toFeignResponse(response, input).toBuilder().request(input).build();
	}

	// 异步请求
	@Override
	public CompletableFuture<feign.Response> execute(feign.Request input, feign.Request.Options options,
			Optional<Object> requestContext) {
		okhttp3.OkHttpClient requestScoped = getClient(options);
		Request request = toOkHttpRequest(input);
		CompletableFuture<feign.Response> responseFuture = new CompletableFuture<>();
		requestScoped.newCall(request).enqueue(new Callback() {
			@Override
			public void onFailure(Call call, IOException e) {
				responseFuture.completeExceptionally(e);
			}

			@Override
			public void onResponse(Call call, okhttp3.Response response) throws IOException {
				final feign.Response r = toFeignResponse(response, input).toBuilder().request(input).build();
				responseFuture.complete(r);
			}
		});
		return responseFuture;
	}

}
