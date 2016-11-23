package com.ly.base.common.cache.model;

public class CacheModel<K,V> {
	private K key;
	private V value;
	private long cacheTime;
	
	public CacheModel(K key, V value) {
		super();
		this.key = key;
		this.value = value;
		refreshCacheTime();
	}
	/**
	 * 刷新缓存时间
	 */
	public void refreshCacheTime(){
		cacheTime = System.currentTimeMillis();
	}
	public K getKey() {
		return key;
	}
	public void setKey(K key) {
		this.key = key;
	}
	public V getValue() {
		return value;
	}
	public void setValue(V value) {
		this.value = value;
	}
	public long getCacheTime() {
		return cacheTime;
	}
}
