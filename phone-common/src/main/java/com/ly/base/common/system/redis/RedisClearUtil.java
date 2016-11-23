package com.ly.base.common.system.redis;

import com.ly.base.common.redis.CacheAccessException;
import com.ly.base.common.redis.RedisClientSupport;
import com.ly.base.common.util.SpringBeanUtil;

public class RedisClearUtil {

	/**
	 * 清除redis菜单权限缓存key
	 * @param rolePk
	 */
	public static void clearRoleMenuCache(Integer... rolePks){
		for (int i = 0; i < rolePks.length; i++) {
			Integer rolePk = rolePks[i];
			String menuFuncStringKey = RedisKeyConfig.getMenuFuncStringCacheKey(rolePk);
			String menuMenuKey = RedisKeyConfig.getMenuMenuCacheKey(rolePk);
			try {
				RedisClientSupport redisClientSupport = SpringBeanUtil.getBean(RedisClientSupport.class);
				redisClientSupport.delete(menuFuncStringKey);
				redisClientSupport.delete(menuMenuKey);
			} catch (CacheAccessException e) {
			}
		}
	}
	/**
	 * 删除抽奖信息
	 */
	
}
