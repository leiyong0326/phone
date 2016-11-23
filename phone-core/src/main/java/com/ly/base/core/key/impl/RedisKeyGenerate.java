package com.ly.base.core.key.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import com.ly.base.common.em.ext.LogEnum;
import com.ly.base.common.redis.CacheAccessException;
import com.ly.base.common.redis.RedisClientSupport;
import com.ly.base.common.system.redis.RedisKeyConfig;
import com.ly.base.core.key.KeyGenerate;

/**
 * 采用Redis的方式产生自增长主键
 * @author LeiYong
 *
 */
public class RedisKeyGenerate implements KeyGenerate{
	/**
	 * Logger for this class
	 */
	private final Logger logger = LoggerFactory.getLogger(RedisKeyGenerate.class);

	@Autowired
	@Qualifier("redisClientSupport")
	private RedisClientSupport redisClientSupport;
	
	@Override
	public String generateStringKey(LogEnum em) {
		Long key = generateLongKey(em);
		if (key!=null) {
			return key.toString();
		}
		//为了防止redis挂了程序能够继续运行
		return new BaseKeyGenerate().generateStringKey(em);
	}

	@Override
	public Long generateLongKey(LogEnum em) {
		synchronized (em) {
			try {
				Long key = redisClientSupport.incrementHash(RedisKeyConfig.getSequenceKey(), em.name(), 1l);
				if (key != null) {
					return key;
				}
			} catch (CacheAccessException e) {
				logger.warn("generateStringKey(KeyGenerateEnum) - exception ignored", e); //$NON-NLS-1$
			}
		}
		//为了防止redis挂了程序能够继续运行
		return new BaseKeyGenerate().generateLongKey(em);
	}

}
