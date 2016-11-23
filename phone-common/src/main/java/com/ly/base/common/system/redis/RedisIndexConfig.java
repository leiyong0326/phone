package com.ly.base.common.system.redis;

public class RedisIndexConfig {
	public static final int REDIS_INDEX_DB = 1;// 索引库(索引,记住我)
	public static final int REDIS_CACHE_DB = 0;// 缓存库(AOP缓存)
	public static final int REDIS_WX_TOKEN_DB = 10;// 微信token库(微信Token)
}
