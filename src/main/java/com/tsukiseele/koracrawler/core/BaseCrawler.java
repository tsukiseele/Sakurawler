package com.tsukiseele.koracrawler.core;

import com.tsukiseele.koracrawler.bean.Section;
import com.tsukiseele.koracrawler.bean.Site;
import com.tsukiseele.koracrawler.utils.FileUtil;
import com.tsukiseele.koracrawler.utils.HttpUtil;
import com.tsukiseele.koracrawler.utils.TextUtil;

import java.io.*;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;

import static com.tsukiseele.koracrawler.core.Const.*;

/**
 * 抽象类，可以重写此类部分方法以自定义HTML加载方式与逻辑
 *
 *
 */
public abstract class BaseCrawler implements Serializable {
	public static final int TYPE_HOME = 0;
	public static final int TYPE_SEARCH = 1;
	public static final int TYPE_EXTRA = 2;
	private static boolean isAddDefaultHeaders = false;
	
	public static final String[] USER_AGENTS = new String[] {
		"Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/65.0.3325.109 Safari/537.36", 
		"Mozilla/5.0 (Macintosh; U; Intel Mac OS X 10_6_8; en-us) AppleWebKit/534.50 (KHTML, like Gecko) Version/5.1 Safari/534.50",
		"Mozilla/5.0 (compatible; MSIE 9.0; Windows NT 6.1; Trident/5.0;",
		"Mozilla/5.0 (Macintosh; Intel Mac OS X 10_7_0) AppleWebKit/535.11 (KHTML, like Gecko) Chrome/17.0.963.56 Safari/535.11",
		"Mozilla/5.0 (Windows NT 6.1; rv:2.0.1) Gecko/20100101 Firefox/4.0.1"
	};
	
	/**
	 * 设置是否添加默认请求头(默认UA)
	 *
	 * @param isAddDefaultHeaders
	 */
	public static void setIsAddDefaultHeaders(boolean isAddDefaultHeaders) {
		BaseCrawler.isAddDefaultHeaders = isAddDefaultHeaders;
	}
	
	public abstract Mode getMode();

	public abstract Site getSite();
	
	protected String onResponse(Section section, String html) {
		return html;
	}
	
	/**
	 * 推荐重写此方法来提升请求网页的性能，推荐使用OkHttp对此部分进行优化。
	 * 可以在此处对获取到的HTML进行优化处理，但要慎重考虑，这可能会造成对HTML文档整体结构的破坏。
	 * 默认使用的原生HttpURLConnection类进行网页请求。
	 *
	 * @param url 请求的URL
	 * @return 请求到的HTML文档
	 * @throws IOException
	 */
	protected String request(String url) throws IOException {
		return FileUtil.readText(HttpUtil.requestStream(url, getHeaders()));
	}
	
	/**
	 * 解析器在获取HTML文档时调用
	 *
	 * @return HTML文档
	 * @throws IOException
	 */
	protected final String execute() throws IOException {
		Section section = getSection();
		Mode mode = getMode();
		if (section == null)
			throw new NullPointerException("section not exists! key = " + mode.extraKey);
		// 处理URL，替换里面的占位符和转义字符
		String url = encodeURL(replaceUrlPlaceholder(section.getIndexUrl(), mode.pageCode, mode.keywords));
		return onResponse(section, request(url));
	}
	
	/**
	 * 获取当前Site的Section
	 *
	 * @return 当前Site的Section
	 */
	public final Section getSection() {
		return findCurrentRule(getSite(), getMode().type, getMode().extraKey);
	}
	
	/**
	 * 构造解析器
	 *
	 * @param type 元数据Class对象
	 * @param <T> 元数据类型
	 * @return 关于元数据的数据解析器
	 */
	public final <T extends Bean> DocumentParser<T> buildParser(Class<T> type) {
		return new DocumentParser<>(this, type);
	}
	
	/**
	 * 生成随机的默认浏览器UA
	 *
	 * @return 随机生成的浏览器UA
	 */
	public static final String getDefaultUserAgent() {
		return USER_AGENTS[(int) (Math.random() * USER_AGENTS.length)];
	}
	
	/**
	 * 获取当前Site的请求头
	 *
	 * @return 实际请求头
	 */
	public Map<String, String> getHeaders() {
		Map<String, String> requestHeaders = getSite().getRequestHeaders();
		if (requestHeaders == null)
			requestHeaders = new HashMap<>();
		if (isAddDefaultHeaders) {
			requestHeaders.put("User-Agent", getDefaultUserAgent());
		}
		return requestHeaders;
	}
	
	/**
	 * 替换所有占位符
	 *
	 * @param templateUrl 模板URL
	 * @param pageCode 目标页码
	 * @param keyword 关键字
	 * @return 目标URL
	 */
	private String replaceUrlPlaceholder(String templateUrl, int pageCode, String keyword) {
		return replacePageCode(replaceSearchKeyword(templateUrl, keyword), pageCode);
	}
	
	/**
	 * 占位符用法：
	 * {page:a} 表示页面从pageCode + a页开始加载
	 * {page:a, b} 表示页面从(pageCode + a) * b页开始加载
	 *
	 * a 补正码，对pageCode给予一定的补正
	 * b 步距，改变pageCode的间隔值
	 *
	 * @param templateUrl 模板url
	 * @param pageCode 目标页码
	 * @return 实际url
	 */
	public static String replacePageCode(String templateUrl, int pageCode) {
		// 补正码
		int correct = 0;
		// 步距，默认为1
		int pace = 1;
		
		// 替换页码
		Matcher pageMatcher = PATTERN_CONTENT_PAGE.matcher(templateUrl);
		if (pageMatcher.find()) {
			int groupCount = pageMatcher.groupCount();
			int[] ints = new int[] {0, 1};
			for (int i = 1; i <= groupCount; i++) {
				String group = pageMatcher.group(i);
				if (!TextUtil.isEmpty(group))
					ints[i - 1] = Integer.parseInt(group);
			}
			correct = ints[0];
			pace = ints[1];
		}
		// 页码值 (当前页码 + 起始页码) * 修正码
		return templateUrl.replaceAll(REGEX_PLACEHOLDER_PAGE, String.valueOf((pageCode + correct) * pace));
	}
	
	/**
	 * {keywords:} 表示该位置会替换为搜索标签
	 *
	 * @param templateUrl 模板URL
	 * @param keyword 目标关键字
	 * @return 实际URL
	 */
	public static String replaceSearchKeyword(String templateUrl, String keyword) {
		// 关键字为空，则使用默认关键字
		if (TextUtil.isEmpty(keyword)) {
			Matcher keywordMatcher = PATTERN_CONTENT_KEYWORD.matcher(templateUrl);
			if (keywordMatcher.find())
				keyword = keywordMatcher.group();
		}
		return templateUrl.replaceAll(REGEX_PLACEHOLDER_KEYWORD, keyword);
	}
	
	/**
	 * 编码URL
	 *
	 */
	public static String encodeURL(String url) {
		
		String[][] ESCAPES = new String[][] {
			{"&amp;", "&"},
			{" ", "%20"}
		};
		for (String[] escape : ESCAPES)
			url = url.replaceAll(escape[0], escape[1]);
		return url;
	}
	
	/**
	 * 通过Site找到当前Rule
	 *
	 */
	public static Section findCurrentRule(Site site, int mode, String extraKey) {
		Section section = null;
		switch (mode) {
			case BaseCrawler.TYPE_HOME :
				section = site.getHomeSection();
				break;
			case BaseCrawler.TYPE_SEARCH :
				section = site.getSearchSection();
				break;
			case BaseCrawler.TYPE_EXTRA :
				if (site.getExtraSections() != null && !site.getExtraSections().isEmpty())
					section = site.getExtraSections().get(extraKey);
				break;
		}
		return section;
	}
	
	/**
	 * 内部Bean类
	 * 决定Crawler抓取方式以及传入参数
	 * 
	 */
	public static class Mode implements Serializable {
		public int type;
		public int pageCode;
		public String keywords;
		public String extraKey;
		public String extraData;
		
		/**
		 * 决定爬取过程中传入的参数
		 *
		 * @param type Site的类型（图片，视频等等。。。）
		 * @param pageCode 当前页码
		 * @param keywords 当然关键字
		 * @param extraKey 扩展子板块键值
		 * @param extraData 扩展子板块数据
		 */
		public Mode(int type, int pageCode, String keywords, String extraKey, String extraData) {
			this.type = type;
			this.pageCode = pageCode;
			this.keywords = keywords;
			this.extraKey = extraKey;
			this.extraData = extraData;
		}
		
		public Mode() {}
	}
}
