package com.ly.base.common.cache.map;

/**
 * String类型缓存器
 * @author LeiYong
 *
 */
public class StringTokenCacheMap extends CacheMap<String, String>{
	private static StringTokenCacheMap instance;
	
	private boolean cacheRefreshGet = false;
	private boolean cacheRefreshPut = false;
	private long cacheTime = 7100_000;
	
	private StringTokenCacheMap() {
		super();
	}
	public synchronized static StringTokenCacheMap getInstance(){
		if (instance==null) {
			instance = new StringTokenCacheMap();
		}
		return instance;
	}

	@Override
	String getAfter(String v) {
		System.out.println("获取的token为:"+(v==null?"null":v));
		return v;
	}
	@Override
	long cacheTime() {
		return cacheTime;
	}
	@Override
	boolean cacheRefreshGet() {
		return cacheRefreshGet;
	}
	@Override
	boolean cacheRefreshPut() {
		return cacheRefreshPut;
	}
	
}
