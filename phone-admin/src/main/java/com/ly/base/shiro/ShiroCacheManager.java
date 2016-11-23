package com.ly.base.shiro;

import java.util.Collection;
import java.util.Set;

import org.apache.shiro.cache.Cache;
import org.apache.shiro.cache.CacheException;
import org.apache.shiro.cache.CacheManager;

import com.ly.base.common.redis.CacheAccessException;
import com.ly.base.common.redis.RedisClientSupport;
import com.ly.base.common.system.redis.RedisKeyConfig;

/**
 * 包装Spring cache抽象
 * <p>User: Zhang Kaitao
 * <p>Date: 13-3-23 上午8:26
 * <p>Version: 1.0
 */
public class ShiroCacheManager implements CacheManager {

    private RedisClientSupport redisClientSupport;

    /**
     * 设置spring cache manager
     *
     * @param redisClientSupport
     */
    public void setRedisClientSupport(RedisClientSupport redisClientSupport) {
        this.redisClientSupport = redisClientSupport;
    }

    @Override
    public <K, V> Cache<K, V> getCache(String name) throws CacheException {
//        org.springframework.cache.Cache springCache = redisClientSupport.getCache(name);
        return new ShiroCustomCache(redisClientSupport);
    }

    static class ShiroCustomCache implements Cache {
        private RedisClientSupport redis;

        ShiroCustomCache(RedisClientSupport springCache) {
            this.redis = springCache;
        }

        @Override
        public Object get(Object key) throws CacheException {
			try {
				return redis.getHash(RedisKeyConfig.getShiroCacheKey(), key.toString());
			} catch (CacheAccessException e) {
				e.printStackTrace();
			}
            return null;
        }

        @Override
        public Object put(Object key, Object value) throws CacheException {
        	try {
				redis.putHash(RedisKeyConfig.getShiroCacheKey(), key.toString(), value);
			} catch (CacheAccessException e) {
				e.printStackTrace();
			}
//            redis.put(key, value);
            return value;
        }

        @Override
        public Object remove(Object key) throws CacheException {
            try {
				redis.deleteHashAll(RedisKeyConfig.getShiroCacheKey(), key.toString());
			} catch (CacheAccessException e) {
				e.printStackTrace();
			}
            return null;
        }

        @Override
        public void clear() throws CacheException {
			try {
				redis.delete(RedisKeyConfig.getShiroCacheKey());
			} catch (CacheAccessException e) {
				e.printStackTrace();
			}
        }

        @Override
        public int size() {
        	try {
				return redis.getHashs(RedisKeyConfig.getShiroCacheKey()).size();
			} catch (CacheAccessException e) {
				e.printStackTrace();
			}
        	return 0;
        }

        @Override
        public Set<Object> keys() {
        	return redis.opsForHash().keys(RedisKeyConfig.getShiroCacheKey());
        }

        @Override
        public Collection values() {
            throw new UnsupportedOperationException("invoke spring cache abstract values method not supported");
        }

    }
}
