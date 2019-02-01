package com.tsukiseele.koracrawler.bean;

import com.tsukiseele.koracrawler.core.Bean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

// 保存目录数据和状态

/**
 * 子容器，存放数据条目
 * @param <T>
 */
public class Catalog<T extends Bean> extends ArrayList<T> implements Serializable {
	// 源数据
	public T metadata;
	// 源Section
	public Section section;
	// 目录当前页码
	public int pageCode = 0;

	public Catalog(List<T> datas, Section section, T metadata) {
		super(datas);
		this.section = section;
		this.metadata = metadata;
	}
	
	public Catalog(Section section, T metadata) {
		super();
		this.section = section;
		this.metadata = metadata;
	}
	
	public boolean isSingle() {
		return size() == 1;
	}
}
