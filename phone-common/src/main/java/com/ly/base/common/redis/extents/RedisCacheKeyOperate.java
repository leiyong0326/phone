package com.ly.base.common.redis.extents;
@FunctionalInterface
public interface RedisCacheKeyOperate {
	/**
	 * 获取单个对象cacheKey
	 * @param value 对象对应的field值 
	 * @return
	 */
	public String cacheKey(Object value);
}
