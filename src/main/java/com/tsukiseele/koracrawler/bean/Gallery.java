package com.tsukiseele.koracrawler.bean;

import com.tsukiseele.koracrawler.core.BaseCrawler;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * 存放数据条目与子容器的根容器
 * @param <T>
 */
public class Gallery<T> extends ArrayList<T> implements Serializable {
	// 源Section
	public Section section;
	// 当前页码
	public int pageCode;
	// 当前Tags
	public String keywords;

	public Gallery(List<T> datas, Section section, int pageCode, String keywords) {
		super(datas);
		this.section = section;
		this.pageCode = pageCode;
		this.keywords = keywords;
	}

	public Gallery(List<T> datas, BaseCrawler baseCrawler) {
		this(datas, baseCrawler.getSection(), baseCrawler.getMode().pageCode, baseCrawler.getMode().keywords);
	}
}
