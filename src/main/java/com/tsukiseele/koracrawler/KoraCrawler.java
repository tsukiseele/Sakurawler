package com.tsukiseele.koracrawler;

import com.tsukiseele.koracrawler.bean.Site;
import com.tsukiseele.koracrawler.core.BaseCrawler;

import java.io.Serializable;

/**
 * BaseCrawler的简单实现类
 */
public class KoraCrawler extends BaseCrawler implements Serializable {
	private Site site;
	private Mode mode;
	
	public KoraCrawler(Site site) {
		this.site = site;
		this.mode = new Mode();
	}
	
	public void setPageCode(int pageCode) {
		mode.pageCode = pageCode;
	}
	
	public void setKeywords(String keywords) {
		mode.keywords = keywords;
	}
	
	public void setExtraKey(String extraKey) {
		mode.extraKey = extraKey;
	}
	
	public void setExtraData(String extraData) {
		mode.extraData = extraData;
	}
	
	public Mode setMode() {
		return mode;
	}
	
	@Override
	public BaseCrawler.Mode getMode() {
		return mode;
	}

	@Override
	public Site getSite() {
		return site;
	}
}