package com.tsukiseele.koracrawler.core;

import java.util.Map;

/**
 * 抽象容器类，继承于Bean
 * 映射类，该类内部使用Map结构存放数据而非POJO
 */
public abstract class MetaData extends Bean {
	abstract public Map<String, String> getMetaData();
}
