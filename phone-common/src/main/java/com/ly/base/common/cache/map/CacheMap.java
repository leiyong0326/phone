package com.ly.base.common.cache.map;

import java.util.ArrayList;
import java.util.List;

import com.ly.base.common.cache.model.CacheModel;

public abstract class CacheMap<K,V> {
	private List<CacheModel<K, V>> cacheModels;
	
	public CacheMap() {
		super();
		cacheModels = new ArrayList<>();
	}
	/**
	 * 缓存时间值(ms)
	 * @return
	 */
	abstract long cacheTime();
	/**
	 * get已存在的值是否刷新时间
	 * @return
	 */
	abstract boolean cacheRefreshGet();
	/**
	 * put已存在的值是否刷新时间
	 * @return
	 */
	abstract boolean cacheRefreshPut();
	/**
	 * 获取返回之后调用
	 * @param v
	 * @return
	 */
	abstract V getAfter(V v);
//	abstract void putAfter(K k,V v);
	public synchronized void put(K k,V v){
		CacheModel<K, V> model = findByKey(k);
		if (model!=null) {
			model.setValue(v);
			if(cacheRefreshPut()){
				model.refreshCacheTime();
			}
		}else{
			model = new CacheModel<>(k, v);
			cacheModels.add(model);
		}
//		putAfter(k, v);
	}
	/**
	 * 获取对象
	 * @param k
	 * @return
	 */
	public V get(K k){
		CacheModel<K, V> model = findByKey(k);
		if (model!=null&&checkTime(model)) {
			if (cacheRefreshGet()) {
				model.refreshCacheTime();
			}
			return getAfter(model.getValue());
		}else{
			cacheModels.remove(model);
			model = null;
		}
		return getAfter(null);
	}
	/**
	 * 验证时间
	 * @param model
	 * @return
	 */
	protected boolean checkTime(CacheModel<K,V> model){
		if (model!=null) {
			if (System.currentTimeMillis()-model.getCacheTime()<cacheTime()) {
				return true;
			}
		}
		return false;
	}
	/**
	 * 通过key查找缓存对象
	 * @param k
	 * @return
	 */
	protected CacheModel<K, V> findByKey(K k) {
		if (cacheModels!=null) {
			for (CacheModel<K, V> cacheModel : cacheModels) {
				if (cacheModel.getKey().hashCode()==k.hashCode()) {
					return cacheModel;
				}
			}
		}
		return null;
	}
}
