package com.tsukiseele.koracrawler.bean;

import com.tsukiseele.koracrawler.utils.TextUtil;

import java.io.Serializable;
import java.util.Map;

/**
 * 网站的规则选择器集合
 *
 */
public class Section implements Serializable {
	private String indexUrl;
	private String reuse;
	
	private Map<String, Selector> gallerySelectors;
	private Map<String, Selector> catalogSelectors;
	private Map<String, Selector> extraSelectors;

	public String getReuse() {
		return reuse;
	}

	public void reuse(Section section) {
		this.gallerySelectors = section.gallerySelectors;
		this.catalogSelectors = section.catalogSelectors;
		this.extraSelectors = section.extraSelectors;
	}

	@Override
	public String toString() {
		return TextUtil.toString(this);
	}
	
	public void setIndexUrl(String indexUrl) {
		this.indexUrl = indexUrl;
	}

	public String getIndexUrl() {
		return indexUrl;
	}
	
	public void setGallerySelectors(Map<String, Selector> gallerySelectors) {
		this.gallerySelectors = gallerySelectors;
	}

	public Map<String, Selector> getGallerySelectors() {
		return gallerySelectors;
	}
	
	public void setCatalogSelectors(Map<String, Selector> catalogSelectors) {
		this.catalogSelectors = catalogSelectors;
	}

	public Map<String, Selector> getCatalogSelectors() {
		return catalogSelectors;
	}
	
	public void setExtraSelectors(Map<String, Selector> extraSelectors) {
		this.extraSelectors = extraSelectors;
	}

	public Map<String, Selector> getExtraSelectors() {
		return extraSelectors;
	}
}
