package com.tsukiseele.koracrawler.bean;

import com.tsukiseele.koracrawler.core.Const;
import com.tsukiseele.koracrawler.utils.RegexUtil;
import com.tsukiseele.koracrawler.utils.TextUtil;

import java.io.Serializable;
import java.util.regex.Matcher;

/**
 * 网站的规则选择器
 *
 */
public class Selector implements Serializable {
	public String selector;
	public String fun;
	public String attr;
	public String regex;
	public String capture;
	public String replacement;

	public void init() {
		if (TextUtil.isEmptyAll(fun, regex)) {
			String selector = null;
			String fun = null;
			String attr = null;
			// 提取选择器
			selector = RegexUtil.matchesText(this.selector, Const.PATTERN_SELECTOR);
			// 提取选择方法
			Matcher matcher = Const.PATTERN_FUN.matcher(this.selector);
			if (matcher.find()) {
				switch (matcher.group().trim()) {
					case "attr" :
						fun = "attr";
						attr = RegexUtil.matchesText(this.selector, Const.PATTERN_ATTR);
						break;
					case "html":
						fun = "html";
						break;
					case "text" :
						fun = "text";
						break;
				}
			}
			this.selector = selector;
			this.fun = fun;
			this.attr = attr;
		}
	}
	
	@Override
	public String toString() {
		return TextUtil.toString(this);
	}
}
