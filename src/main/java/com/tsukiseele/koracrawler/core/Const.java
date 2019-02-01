package com.tsukiseele.koracrawler.core;

import java.util.regex.Pattern;

public class Const {
	public static final Pattern PATTERN_SELECTOR = Pattern.compile("(?<=\\$\\().*(?=\\)\\.)");
	public static final Pattern PATTERN_FUN = Pattern.compile("(?<=\\.)(text|html|attr)");
	public static final Pattern PATTERN_ATTR = Pattern.compile("(?<=attr\\().*?(?=\\))");
	public static final Pattern PATTERN_CONTENT_PAGE = Pattern.compile("(?<=\\{page:)(-?\\d*)?,?(-?\\d*)?(?=\\})");
	public static final Pattern PATTERN_CONTENT_KEYWORD = Pattern.compile("(?<=\\{keyword:).*(?=\\})");
	public static final String REGEX_PLACEHOLDER_PAGE = "\\{page:.*?\\}";
	public static final String REGEX_PLACEHOLDER_KEYWORD = "\\{keywords:.*?\\}";
}
