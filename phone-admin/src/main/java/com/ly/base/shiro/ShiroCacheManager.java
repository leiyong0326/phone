package com.ly.base.shiro;

import org.apache.shiro.cache.MemoryConstrainedCacheManager;

/**
 * 缓存鉴权信息,直接使用shiro内存级缓存
 * @author LeiYong
 */
public class ShiroCacheManager extends MemoryConstrainedCacheManager {

//    private RedisClientSupport redisClientSupport;
//
//    /**
//     * 设置spring cache manager
//     *
//     * @param redisClientSupport
//     */
//    public void setRedisClientSupport(RedisClientSupport redisClientSupport) {
//        this.redisClientSupport = redisClientSupport;
//    }
//
//    @Override
//    public <K, V> Cache<K, V> getCache(String name) throws CacheException {
////        org.springframework.cache.Cache springCache = redisClientSupport.getCache(name);
//        return new ShiroCustomCache(redisClientSupport);
//    }
//
//    static class ShiroCustomCache implements Cache {
//        private RedisClientSupport redis;
//
//        ShiroCustomCache(RedisClientSupport springCache) {
//            this.redis = springCache;
//        }
//
//        @Override
//        public Object get(Object key) throws CacheException {
//			try {
//				return redis.getHash(RedisKeyConfig.getShiroCacheKey(), key.toString());
//			} catch (CacheAccessException e) {
//				e.printStackTrace();
//			}
//            return null;
//        }
//
//        @Override
//        public Object put(Object key, Object value) throws CacheException {
//        	try {
//				redis.putHash(RedisKeyConfig.getShiroCacheKey(), key.toString(), value);
//			} catch (CacheAccessException e) {
//				e.printStackTrace();
//			}
////            redis.put(key, value);
//            return value;
//        }
//
//        @Override
//        public Object remove(Object key) throws CacheException {
//            try {
//				redis.deleteHashAll(RedisKeyConfig.getShiroCacheKey(), key.toString());
//			} catch (CacheAccessException e) {
//				e.printStackTrace();
//			}
//            return null;
//        }
//
//        @Override
//        public void clear() throws CacheException {
//			try {
//				redis.delete(RedisKeyConfig.getShiroCacheKey());
//			} catch (CacheAccessException e) {
//				e.printStackTrace();
//			}
//        }
//
//        @Override
//        public int size() {
//        	try {
//				return redis.getHashs(RedisKeyConfig.getShiroCacheKey()).size();
//			} catch (CacheAccessException e) {
//				e.printStackTrace();
//			}
//        	return 0;
//        }
//
//        @Override
//        public Set<Object> keys() {
//        	return redis.opsForHash().keys(RedisKeyConfig.getShiroCacheKey());
//        }
//
//        @Override
//        public Collection values() {
//            throw new UnsupportedOperationException("invoke spring cache abstract values method not supported");
//        }
//
//    }
}
