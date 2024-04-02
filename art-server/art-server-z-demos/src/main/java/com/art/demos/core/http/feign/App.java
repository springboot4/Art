package com.art.demos.core.http.feign;

import com.art.demos.core.http.feign.java.ApacheHttp5Client;
import com.art.demos.core.http.feign.java.ApacheHttpClient;
import com.art.demos.core.http.feign.java.Client;
import com.art.demos.core.http.feign.java.GoogleHttpClient;
import com.art.demos.core.http.feign.java.OkHttpClient;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.Maps;
import feign.Request;
import feign.Response;
import lombok.SneakyThrows;

import java.io.BufferedReader;
import java.io.Reader;
import java.nio.charset.Charset;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * @author fxz
 */
public class App {

	private static final ObjectMapper mapper = new ObjectMapper();

	@SneakyThrows
	public static void main(String[] args) {
		Request request = Request.create(Request.HttpMethod.GET, "http://localhost:9999/auth/configuration",
				Maps.newHashMap(), null, Charset.defaultCharset(), null);
		Request.Options options = new Request.Options(10, TimeUnit.SECONDS, 60, TimeUnit.SECONDS, true);

		Response response = new Client.Default(null, null).execute(request, options);
		System.out.println(decode(response, Map.class));

		response = new OkHttpClient().execute(request, options);
		System.out.println(decode(response, Map.class));

		response = new ApacheHttpClient().execute(request, options);
		System.out.println(decode(response, Map.class));

		response = new ApacheHttp5Client().execute(request, options);
		System.out.println(decode(response, Map.class));

		// response = new Http2Client().execute(request, options);
		// System.out.println(decode(response, Map.class));

		response = new GoogleHttpClient().execute(request, options);
		System.out.println(decode(response, Map.class));
	}

	@SneakyThrows
	public static <T> Object decode(Response response, Class<T> type) {
		Reader reader = response.body().asReader(response.charset());
		if (!reader.markSupported()) {
			reader = new BufferedReader(reader, 1);
		}
		try {
			// Read the first byte to see if we have any data
			reader.mark(1);
			if (reader.read() == -1) {
				// Eagerly returning null avoids "No content to map due to end-of-input"
				return null;
			}
			reader.reset();
			return mapper.readValue(reader, mapper.constructType(type));
		}
		catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

}
