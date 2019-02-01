package com.tsukiseele.koracrawler.utils;

import java.io.*;

public class FileUtil {
	private static final String DEFAULT_CHARSET = "UTF-8";
	
	public static String readText(String filePath, String charset) throws IOException {
		return readText(new FileInputStream(filePath), charset);
	}
	
	public static String readText(String filePath) throws IOException {
		return readText(filePath, DEFAULT_CHARSET);
	}
	
	public static String readText(InputStream is, String charset) throws IOException {
		BufferedReader reader = null;
		StringBuilder text = new StringBuilder();
		try {
			reader = new BufferedReader(new InputStreamReader(is, charset));
			String line;
			while ((line = reader.readLine()) != null)
				text.append(line).append('\n');
		} finally {
			if (reader != null)
				reader.close();
		}
		return text.toString();
	}
	
	public static String readText(InputStream is) throws IOException {
		return readText(is, DEFAULT_CHARSET);
	}
}