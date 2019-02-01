package com.tsukiseele.koracrawler.utils;

import java.util.ArrayList;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class HttpRequestPool {
	private static final ThreadPoolExecutor threadPool = new ThreadPoolExecutor(10, Integer.MAX_VALUE, 10, TimeUnit.SECONDS, new LinkedBlockingDeque<Runnable>());
	private static ArrayList<Runnable> tasks = new ArrayList<>();

	public static void execute(Runnable runnable) {
		threadPool.execute(runnable);
		tasks.add(runnable);
	}

	public static void cancelAll() {
		for (Runnable task : tasks)
			threadPool.remove(task);
	}
}
