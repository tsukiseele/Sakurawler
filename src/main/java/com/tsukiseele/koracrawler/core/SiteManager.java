package com.tsukiseele.koracrawler.core;

import com.tsukiseele.koracrawler.bean.Site;
import com.tsukiseele.koracrawler.utils.FileUtil;
import com.tsukiseele.koracrawler.utils.TextUtil;

import java.io.File;
import java.io.FileFilter;
import java.util.*;

/**
 * 规则管理器，用于管理加载的Site对象
 */
public class SiteManager {
	private static SiteManager siteRuleHolder;
	public interface OnErrorListener {
		void onError(Map<String, Exception> errorMessage);
	}
	// 站点列表组
	private Map<String, List<Site>> siteMap = null;
	// 保存路径映射
	private Map<Integer, File> sitePaths = new HashMap<>();
	// 保存解析失败的错误规则的异常信息
	private Map<String, Exception> errorMessage = new HashMap<>();

	private OnErrorListener onErrorListener = null;

	private SiteManager() {	}

	public Map<String, List<Site>> getSiteMap() {
		return siteMap;
	}

	// 单例模式
	public static SiteManager instance() {
		if (siteRuleHolder == null)
			siteRuleHolder = new SiteManager();
		return siteRuleHolder;
	}

	// 载入规则
	public void loadSites(File dir) {
		loadSites(dir, null);
	}

	public void loadSites(File dir, OnErrorListener onErrorListener) {
		this.onErrorListener = onErrorListener;
		addSitesToMap(siteMap, loadSiteFromFile(dir));
	}
	/*
	 * 用于将规则分组
	 *
	 *
	 */
	private void addSitesToMap(Map<String, List<Site>> siteMap, List<Site> siteList) {
		// 生成映射
		for (Site site : siteList)
			addSiteToMap(siteMap, site);

		// 按标题字典排序
		for (List<Site> sites : siteMap.values()) {
			Collections.sort(sites, new Comparator<Site>() {
					@Override
					public int compare(Site a, Site b) {
						return a.getTitle().compareTo(b.getTitle());
					}
				});
		}
	}

	// 读取外部并将规则转化为对象
	private List<Site> loadSiteFromFile(File dir) {
		// 获取规则文件
		File[] files = dir.listFiles(new FileFilter() {
				@Override
				public boolean accept(File file) {
					if (file == null)
						return false;
					if (file.exists() && file.isFile()) {
						String fileName = file.getName().toLowerCase();
						if (fileName.endsWith(".json"))
							return true;
					}
					return false;
				}
			});
		// 读取规则
		List<Site> sites = new ArrayList<>();
		for (File file : files) {
			// 解析规则
			String data = null;
			Site site = null;
			try {
				data = FileUtil.readText(file.getAbsolutePath());
				// 将JSON转换为对象并添加到列表
				site = Site.fromJSON(data);
			} catch (Exception e) {
				errorMessage.put(file.getName(), e);
			}
			if (site != null) {
				// 写入规则路径
				sitePaths.put(site.getId(), file);
				// 加入规则组
				sites.add(site);
			}
		}

		if (errorMessage.size() > 0 && onErrorListener != null)
			onErrorListener.onError(errorMessage);
		return sites;
	}

	public void addSiteToMap(Map<String, List<Site>> sites, Site site) {
		String type = site.getType();
		if (TextUtil.isEmpty(type))
			type = "unknown";
		List<Site> list = null;
		// 如果组不存在，则新建
		if (sites.containsKey(type)) {
			list = sites.get(type);
		} else {
			list = new ArrayList<>();
			sites.put(type, list);
		}
		list.add(site);
	}
	
	public List<Site> getSiteList(String key) {
		if (siteMap.containsKey(key))
			return siteMap.get(key);
		return null;
	}

	public void setOnErrorListener(OnErrorListener onErrorListener) {
		this.onErrorListener = onErrorListener;
	}

	public OnErrorListener getOnErrorListener() {
		return onErrorListener;
	}
}
