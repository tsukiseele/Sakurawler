package com.tsukiseele.koracrawler.bean;

import com.google.gson.Gson;
import com.tsukiseele.koracrawler.utils.FileUtil;
import com.tsukiseele.koracrawler.utils.RegexUtil;
import com.tsukiseele.koracrawler.utils.TextUtil;

import java.io.*;
import java.util.Map;

/**
 * 该类描述一个站点规则的所有配置参数与抓取方式
 * 与JSON形成映射
 *
 */
public class Site implements Serializable {
	public static final String FLAG_STATE_OK = "flagStateOk";
	public static final String FLAG_STATE_INSTABLE = "flagStateInstable";
	public static final String FLAG_STATE_ABROAD = "flagStateAbroad";
	public static final String FLAG_LOAD_JS = "loadJs";
	public static final String FLAG_DEBUG = "debug";
	
	private String title;
	private int id;
	private int version;
	private String author;
	private String remarks;
	private String rating;
	private String flag;
	private String path;
	private String type;
	private String json;
	private Map<String, String> requestHeaders;
	private Section homeSection;
	private Section searchSection;
	private Map<String, Section> extraSections;

	public static Site fromJSON(String json) {
		Gson gson = new Gson();
		Site site = gson.fromJson(json, Site.class);
		site.json = json;
		site.reuseSection(site.homeSection);
		site.reuseSection(site.searchSection);
		if (site.extraSections != null)
			for (Map.Entry<String, Section> entry : site.extraSections.entrySet())
				site.reuseSection(entry.getValue());
		return site;
	}
	
	public static Site fromJSON(File jsonFile) throws IOException {
		return fromJSON(FileUtil.readText(jsonFile.getAbsolutePath()));
	}
	
	private void reuseSection(Section section) {
		if (section == null)
			return;
		String reuse = section.getReuse();
		if (TextUtil.isEmpty(reuse))
			return;
		if (reuse.contains("homeSection")) {
			section.reuse(homeSection);
		} else if (reuse.contains("searchSection")) {
			section.reuse(searchSection);
		} else if (reuse.contains("extraSections")) {
			String key = RegexUtil.matchesText(reuse, "(?<=\\().*?(?=\\))");
			section.reuse(extraSections.get(key));
		}
	}
	
	public boolean hasFlag(String flag) {
		if (TextUtil.isEmpty(this.flag) || TextUtil.isEmpty(flag))
			return false;

		return this.flag.contains(flag);
	}
	
	@Override
	public String toString() {
		return TextUtil.toString(this);
	}
	
	public String getType() {
		return type;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public String getPath() {
		return path;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getTitle() {
		return title;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getId() {
		return id;
	}
	
	public void setVersion(int version) {
		this.version = version;
	}

	public int getVersion() {
		return version;
	}

	public void setAuthor(String author) {
		this.author = author;
	}

	public String getAuthor() {
		return author;
	}

	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}

	public String getRemarks() {
		return remarks;
	}

	public void setRating(String rating) {
		this.rating = rating;
	}

	public String getRating() {
		return rating;
	}

	public void setFlag(String flag) {
		this.flag = flag;
	}

	public String getFlag() {
		return flag;
	}
	public String toJson() {
		return json;
	}
	
	public void setRequestHeaders(Map<String, String> requestHeaders) {
		this.requestHeaders = requestHeaders;
	}

	public Map<String, String> getRequestHeaders() {
		return requestHeaders;
	}
	public void setHomeSection(Section homeSection) {
		this.homeSection = homeSection;
	}

	public Section getHomeSection() {
		return homeSection;
	}

	public void setSearchSection(Section searchSection) {
		this.searchSection = searchSection;
	}

	public Section getSearchSection() {
		return searchSection;
	}

	public void setExtraSections(Map<String, Section> extraSections) {
		this.extraSections = extraSections;
	}

	public Map<String, Section> getExtraSections() {
		return extraSections;
	}
}
