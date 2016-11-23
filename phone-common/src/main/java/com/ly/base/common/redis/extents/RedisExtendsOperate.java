package com.ly.base.common.redis.extents;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSON;
import com.ly.base.common.redis.CacheAccessException;
import com.ly.base.common.redis.RedisClientSupport;
import com.ly.base.common.util.ArrayUtil;
import com.ly.base.common.util.ReflectionUtil;
import com.ly.base.common.util.StringUtil;

public class RedisExtendsOperate<T> {
	/**
	 * Logger for this class
	 */
	private static final Logger logger = LoggerFactory.getLogger(RedisExtendsOperate.class);

	private RedisClientSupport redisClientSupport;
	
	public RedisExtendsOperate(RedisClientSupport redisClientSupport) {
		super();
		this.redisClientSupport = redisClientSupport;
	}


	/**
	 * 
	 * 缓存list类型通过索性
	 * @param datas 被缓存的数据
	 * @param mainKey 缓存主键
	 * @param clazz 缓存类
	 * @param pkField 缓存类pk
	 * @param keyOperate 读取缓存key操作
	 * @throws CacheAccessException
	 */
	public void cacheListAndIndex(List<T> datas,String mainKey,Class<T> clazz,String pkField,RedisCacheKeyOperate keyOperate) throws CacheAccessException {
		if (logger.isDebugEnabled()) {
			logger.debug("cacheListAndIndex(List<?>, String, Class<?>, String, RedisCacheKeyOperate) - start"); //$NON-NLS-1$
		}

		if (redisClientSupport!=null&&datas!=null&&StringUtil.checkNotEmpty(mainKey,pkField)) {
			// 遍历并缓存
			Field field = ReflectionUtil.findField(clazz, pkField);
			if (field!=null) {
				List<String> keyList = new ArrayList<>();
				Map<String, Object> dataMap = new LinkedHashMap<>();
				ArrayUtil.foreach(datas, (m, i) -> {
					Object value = ReflectionUtil.getFieldAndSetAccessible(field, m);
					String key = keyOperate.cacheKey(value);//主键
					keyList.add(key);
					try {
						dataMap.put(key, m);
//						redisClientSupport.putValue(key, m);
						return true;
					} catch (Exception e) {
						logger.error("redisClientSupport.putValue(key, m)",e);
						return false;
					}
				});
				redisClientSupport.putValues(dataMap);
				redisClientSupport.putValue(mainKey, JSON.toJSONString(keyList)); 
			}
		}

		if (logger.isDebugEnabled()) {
			logger.debug("cacheListAndIndex(List<?>, String, Class<?>, String, RedisCacheKeyOperate) - end"); //$NON-NLS-1$
		}
	}
	/**
	 * 读取list类型通过索引
	 * @param mainKey 索引key主键
	 * @return 如果没有数据或读取过程中报错,则返回null
	 * @throws CacheAccessException
	 */
	@SuppressWarnings("unchecked")
	public List<T> readListByIndex(String mainKey) throws CacheAccessException{
		if (logger.isDebugEnabled()) {
			logger.debug("readListByIndex(String) - start"); //$NON-NLS-1$
		}
		Object keyValue = redisClientSupport.getValue(mainKey);
		if (keyValue!=null&&StringUtils.isNotBlank(keyValue.toString())) {
			List<String> cacheKeys = JSON.parseArray(keyValue.toString(),String.class);
//			Object obj = cacheKeys.get(0);
//			JSONArray ja = ReflectionUtil.convertObjectToBean(cacheKeys, JSONArray.class);
			if (cacheKeys!=null) {
				//如果存在缓存则从缓存读取
				if (cacheKeys != null && cacheKeys.size() > 0) {
					List<T> list = new ArrayList<>();
					List<Object> redisValues = redisClientSupport.getValues(cacheKeys);
					boolean res = ArrayUtil.foreach(redisValues, (value,index) -> {
						try {
//							String key = value.toString();
//							Object menu = redisClientSupport.getValue(key);
							list.add((T) value);
							return true;
						} catch (Exception e) {
							e.printStackTrace();
							logger.error("redisClientSupport.getValue(key)",e);
							return false;
						}
					});
					//如果size不匹配则说明读取过程中报错
					if (!res||list.size()!=cacheKeys.size()) {
						if (logger.isDebugEnabled()) {
							logger.debug("readListByIndex(String) - end"); //$NON-NLS-1$
						}
						return null;
					}

					if (logger.isDebugEnabled()) {
						logger.debug("readListByIndex(String) - end"); //$NON-NLS-1$
					}
					return list;
				}
			}
		}

		if (logger.isDebugEnabled()) {
			logger.debug("readListByIndex(String) - end"); //$NON-NLS-1$
		}
		return null;
	}
}
