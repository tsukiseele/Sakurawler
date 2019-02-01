package com.tsukiseele.koracrawler.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RegexUtil {
	public static String matchesText(String text, Pattern pattern) {
		String matchText = null;
		Matcher matcher = pattern.matcher(text);
		if (matcher.find())
			matchText = matcher.group();
		return matchText;
	}
	public static String matchesText(String text, String regex) {
		return matchesText(text, Pattern.compile(regex));
	}
}
