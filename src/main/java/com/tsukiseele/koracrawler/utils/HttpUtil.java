package com.tsukiseele.koracrawler.utils;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.SocketException;
import java.net.URL;
import java.util.Map;

public class HttpUtil {
	public static InputStream requestStream(String url, Map<String, String> headers) throws IOException {

		HttpURLConnection connection = (HttpURLConnection) new URL(url).openConnection();
		InputStream is;
		connection.setReadTimeout(10000);
		connection.setConnectTimeout(10000);
		for (String key : headers.keySet())
			connection.addRequestProperty(key, headers.get(key));
		connection.connect();
		if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
			is = connection.getInputStream();
		} else {
			throw new SocketException("Connect failed! response code: " + connection.getResponseCode());
		}
		return is;
	}
}
