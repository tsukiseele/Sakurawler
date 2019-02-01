package com.tsukiseele.koracrawler.utils;

import java.lang.reflect.Field;

public class TextUtil {
	public static boolean isEmpty(String text) {
		return null == text || text.trim().isEmpty();
	}
	
	public static boolean nonEmpty(String text) {
		return null != text && !text.trim().isEmpty();
	}
	
	public static boolean isEmptyAll(String... texts) {
		for (String text : texts)
			if (!isEmpty(text))
				return false;
		return true;
	}
	// 利用反射生成一个包含对象所有字段信息的字符串
	public static String toString(Object obj) {
		StringBuilder sb = new StringBuilder();
		sb.append('[');
		try {
			for (Class<?> type = obj.getClass(); type != Object.class; type = type.getSuperclass()) {
				Field[] fields = type.getDeclaredFields();
				for(Field field : fields) {
					field.setAccessible(true);
					sb.append(String.format("%s = %s, ", field.getName(), field.get(obj)));
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return sb.substring(0, sb.length() - 2) + ']';
	}
} 
