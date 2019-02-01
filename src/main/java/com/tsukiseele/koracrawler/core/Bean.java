package com.tsukiseele.koracrawler.core;

import com.tsukiseele.koracrawler.utils.TextUtil;

import java.lang.reflect.Field;
import java.util.List;

/**
 * 抽象容器类
 * 所有用于存放抓取数据的Bean都必须继承该抽象类
 */
public abstract class Bean {
	public abstract String getCatalogUrl();
	public abstract String getExtraUrl();
	
	public boolean hasCatalog() {
		return !TextUtil.isEmpty(getCatalogUrl());
	}
	
	public boolean hasExtra() {
		return !TextUtil.isEmpty(getExtraUrl());
	}

	public void fillTo(Bean data) {
		if (data == null)
			return;
		Field[] fields = data.getClass().getDeclaredFields();
		for (Field field : fields) {
			field.setAccessible(true);
			try {
				if (field.get(data) == null) {
					Object obj = field.get(this);
					if (obj != null) 
						field.set(data, obj);
				}
			} catch (Exception e) {

			}
		}
	}
	
	public void fillToAll(List<? extends Bean> datas) {
		if (datas != null)
			for (Bean data : datas)
				this.fillTo(data);
	}

	public void coverTo(Bean data) {
		if (data == null)
			return;
		Field[] fields = data.getClass().getDeclaredFields();
		for (Field field : fields) {
			field.setAccessible(true);
			try {
				Object obj = field.get(this);
				if (obj != null)
					field.set(data, obj);
			} catch (Exception e) {

			}
		}
	}
	public void coverToAll(List<? extends Bean> datas) {
		if (datas != null)
			for (Bean data : datas)
				this.coverTo(data);
	}
}
