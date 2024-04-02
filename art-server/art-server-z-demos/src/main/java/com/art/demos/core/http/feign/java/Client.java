package com.art.demos.core.http.feign.java;

import feign.Request;
import feign.Request.Options;
import feign.Response;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLSocketFactory;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.Proxy;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.zip.DeflaterOutputStream;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;
import java.util.zip.InflaterInputStream;

import static feign.Util.CONTENT_ENCODING;
import static feign.Util.CONTENT_LENGTH;
import static feign.Util.ENCODING_DEFLATE;
import static feign.Util.ENCODING_GZIP;
import static feign.Util.checkArgument;
import static feign.Util.checkNotNull;
import static feign.Util.isNotBlank;
import static java.lang.String.CASE_INSENSITIVE_ORDER;
import static java.lang.String.format;

/**
 * Submits HTTP {@link Request requests}. Implementations are expected to be thread-safe.
 */
public interface Client {

	/**
	 * Executes a request against its {@link Request#url() url} and returns a response.
	 * @param request safe to replay.
	 * @param options options to apply to this request.
	 * @return connected response, {@link Response.Body} is absent or unread.
	 * @throws IOException on a network error connecting to {@link Request#url()}.
	 */
	Response execute(Request request, Options options) throws IOException;

	class Default implements Client {

		private final SSLSocketFactory sslContextFactory;

		private final HostnameVerifier hostnameVerifier;

		/**
		 * Disable the request body internal buffering for {@code HttpURLConnection}.
		 *
		 * @see HttpURLConnection#setFixedLengthStreamingMode(int)
		 * @see HttpURLConnection#setFixedLengthStreamingMode(long)
		 * @see HttpURLConnection#setChunkedStreamingMode(int)
		 */
		private final boolean disableRequestBuffering;

		/**
		 * Create a new client, which disable request buffering by default.
		 * @param sslContextFactory SSLSocketFactory for secure https URL connections.
		 * @param hostnameVerifier the host name verifier.
		 */
		public Default(SSLSocketFactory sslContextFactory, HostnameVerifier hostnameVerifier) {
			this.sslContextFactory = sslContextFactory;
			this.hostnameVerifier = hostnameVerifier;
			this.disableRequestBuffering = true;
		}

		/**
		 * Create a new client.
		 * @param sslContextFactory SSLSocketFactory for secure https URL connections.
		 * @param hostnameVerifier the host name verifier.
		 * @param disableRequestBuffering Disable the request body internal buffering for
		 * {@code HttpURLConnection}.
		 */
		public Default(SSLSocketFactory sslContextFactory, HostnameVerifier hostnameVerifier,
				boolean disableRequestBuffering) {
			super();
			this.sslContextFactory = sslContextFactory;
			this.hostnameVerifier = hostnameVerifier;
			this.disableRequestBuffering = disableRequestBuffering;
		}

		@Override
		public Response execute(Request request, Options options) throws IOException {
			// 转换请求并发送
			HttpURLConnection connection = convertAndSend(request, options);
			// 转换响应
			return convertResponse(connection, request);
		}

		Response convertResponse(HttpURLConnection connection, Request request) throws IOException {
			// 获取与连接关联的 HTTP 响应码 注意这里设置了链接的标识位为true 也就是没有建立连接的话会建立连接
			int status = connection.getResponseCode();
			// 获取与连接关联的 HTTP 响应消息
			String reason = connection.getResponseMessage();

			if (status < 0) {
				throw new IOException(format("Invalid status(%s) executing %s %s", status,
						connection.getRequestMethod(), connection.getURL()));
			}

			Map<String, Collection<String>> headers = new TreeMap<>(CASE_INSENSITIVE_ORDER);
			for (Map.Entry<String, List<String>> field : connection.getHeaderFields().entrySet()) {
				// 保存响应头信息
				if (field.getKey() != null) {
					headers.put(field.getKey(), field.getValue());
				}
			}

			// 查询响应正文的长度
			Integer length = connection.getContentLength();
			if (length == -1) {
				length = null;
			}
			InputStream stream;
			if (status >= 400) {
				stream = connection.getErrorStream();
			}
			else {
				if (this.isGzip(headers.get(CONTENT_ENCODING))) {
					stream = new GZIPInputStream(connection.getInputStream());
				}
				else if (this.isDeflate(headers.get(CONTENT_ENCODING))) {
					stream = new InflaterInputStream(connection.getInputStream());
				}
				else {
					stream = connection.getInputStream();
				}
			}

			return Response.builder()
				.status(status)
				.reason(reason)
				.headers(headers)
				.request(request)
				.body(stream, length)
				.build();
		}

		public HttpURLConnection getConnection(final URL url) throws IOException {
			return (HttpURLConnection) url.openConnection();
		}

		HttpURLConnection convertAndSend(Request request, Options options) throws IOException {
			// 请求的url
			final URL url = new URL(request.url());
			// 获取连接 应该注意的是，URLConnection 实例在创建时不会建立实际的网络连接。这只会在调用 URLConnection.connect（）
			// 时发生。
			final HttpURLConnection connection = this.getConnection(url);

			// 处理https
			if (connection instanceof HttpsURLConnection) {
				HttpsURLConnection sslCon = (HttpsURLConnection) connection;
				if (sslContextFactory != null) {
					sslCon.setSSLSocketFactory(sslContextFactory);
				}
				if (hostnameVerifier != null) {
					sslCon.setHostnameVerifier(hostnameVerifier);
				}
			}

			// 连接超时时间
			connection.setConnectTimeout(options.connectTimeoutMillis());
			// 读取超时时间
			connection.setReadTimeout(options.readTimeoutMillis());
			// 是否允许连接与用户进行交互，比如弹出登录对话框或者其他需要用户响应的请求。如果你将其设置为
			// false，则所有需要用户交互的操作都会被禁止，包括身份验证等。
			connection.setAllowUserInteraction(false);
			// 设置为 true 将使得 HTTP 连接遵循服务器返回的重定向指令，即当服务器返回重定向响应时，连接会自动跟随重定向并请求新的URL地址。而设置为
			// false 则会禁止连接自动遵循重定向，从而需要手动处理重定向逻辑。
			connection.setInstanceFollowRedirects(options.isFollowRedirects());
			// 设置请求方法
			connection.setRequestMethod(request.httpMethod().name());

			/**
			 * 消息体传输过程中使用的编码方式。它告知客户端消息体内容的压缩或加密方式，以便客户端能够正确解析和处理接收到的数据。
			 *
			 * 常见的 Content-Encoding 值包括： gzip：表示消息体使用了 Gzip 压缩编码。 deflate：表示消息体使用了 Deflate
			 * 压缩编码。 br：表示消息体使用了 Brotli 压缩编码。 identity：表示消息体未经过压缩或编码。 当客户端接收到包含
			 * Content-Encoding 标头的响应时，会根据该标头指定的编码方式对消息体进行解码，从而还原出原始的消息内容。
			 */
			Collection<String> contentEncodingValues = request.headers().get(CONTENT_ENCODING);
			boolean gzipEncodedRequest = this.isGzip(contentEncodingValues);
			boolean deflateEncodedRequest = this.isDeflate(contentEncodingValues);

			boolean hasAcceptHeader = false;
			Integer contentLength = null;
			for (String field : request.headers().keySet()) {
				if (field.equalsIgnoreCase("Accept")) {
					hasAcceptHeader = true;
				}

				for (String value : request.headers().get(field)) {
					if (field.equals(CONTENT_LENGTH)) {
						if (!gzipEncodedRequest && !deflateEncodedRequest) {
							contentLength = Integer.valueOf(value);
							connection.addRequestProperty(field, value);
						}
					}
					else {
						connection.addRequestProperty(field, value);
					}
				}
			}
			// 某些服务器会阻塞默认的接受字符串。
			if (!hasAcceptHeader) {
				// 在HTTP请求中，"Accept" 头部用于指示客户端所能接受的响应内容类型。如果客户端不明确指定 "Accept"
				// 头部，服务器可能会根据默认规则来决定响应内容的类型。
				connection.addRequestProperty("Accept", "*/*");
			}

			if (request.body() != null) {
				if (disableRequestBuffering) {
					if (contentLength != null) {
						// 连接使用固定长度的流式传输模式，并且使用 contentLength 指定的长度
						connection.setFixedLengthStreamingMode(contentLength);
					}
					else {
						// 连接使用分块编码的流式传输模式，并且指定每个分块的大小为 8196 字节
						connection.setChunkedStreamingMode(8196);
					}
				}

				// 设置为 true， 是用于指示连接可以向服务器输出内容的方法调用。它告诉连接可以将数据发送到服务器，通常用于执行 POST
				// 请求或其他需要向服务器传递数据的情况。如果尚未建立连接，则会尝试建立连接。这是因为设置了输出标志后，Java 会自动尝试打开与指定 URL
				// 的连接。
				connection.setDoOutput(true);

				// 获取与服务器的输出流。这个输出流可以用于向服务器发送数据。
				OutputStream out = connection.getOutputStream();
				if (gzipEncodedRequest) {
					out = new GZIPOutputStream(out);
				}
				else if (deflateEncodedRequest) {
					out = new DeflaterOutputStream(out);
				}
				try {
					out.write(request.body());
				}
				finally {
					try {
						out.close();
					}
					catch (IOException suppressed) {
						// NOPMD
					}
				}
			}

			return connection;
		}

		private boolean isGzip(Collection<String> contentEncodingValues) {
			return contentEncodingValues != null && !contentEncodingValues.isEmpty()
					&& contentEncodingValues.contains(ENCODING_GZIP);
		}

		private boolean isDeflate(Collection<String> contentEncodingValues) {
			return contentEncodingValues != null && !contentEncodingValues.isEmpty()
					&& contentEncodingValues.contains(ENCODING_DEFLATE);
		}

	}

	/**
	 * Client that supports a {@link java.net.Proxy}.
	 */
	class Proxied extends Default {

		public static final String PROXY_AUTHORIZATION = "Proxy-Authorization";

		private final Proxy proxy;

		private String credentials;

		public Proxied(SSLSocketFactory sslContextFactory, HostnameVerifier hostnameVerifier, Proxy proxy) {
			super(sslContextFactory, hostnameVerifier);
			checkNotNull(proxy, "a proxy is required.");
			this.proxy = proxy;
		}

		public Proxied(SSLSocketFactory sslContextFactory, HostnameVerifier hostnameVerifier, Proxy proxy,
				String proxyUser, String proxyPassword) {
			this(sslContextFactory, hostnameVerifier, proxy);
			checkArgument(isNotBlank(proxyUser), "proxy user is required.");
			checkArgument(isNotBlank(proxyPassword), "proxy password is required.");
			this.credentials = basic(proxyUser, proxyPassword);
		}

		@Override
		public HttpURLConnection getConnection(URL url) throws IOException {
			HttpURLConnection connection = (HttpURLConnection) url.openConnection(this.proxy);
			if (isNotBlank(this.credentials)) {
				connection.addRequestProperty(PROXY_AUTHORIZATION, this.credentials);
			}
			return connection;
		}

		public String getCredentials() {
			return this.credentials;
		}

		private String basic(String username, String password) {
			String token = username + ":" + password;
			byte[] bytes = token.getBytes(StandardCharsets.ISO_8859_1);
			String encoded = Base64.getEncoder().encodeToString(bytes);
			return "Basic " + encoded;
		}

	}

}
