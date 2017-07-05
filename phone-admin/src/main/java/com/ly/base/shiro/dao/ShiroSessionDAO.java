package com.ly.base.shiro.dao;

import java.io.Serializable;
import java.util.Collection;
import java.util.Collections;
import java.util.concurrent.TimeUnit;

import org.apache.shiro.session.InvalidSessionException;
import org.apache.shiro.session.Session;
import org.apache.shiro.session.mgt.ValidatingSession;
import org.apache.shiro.session.mgt.eis.MemorySessionDAO;
import org.apache.shiro.util.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ly.base.common.redis.CacheAccessException;
import com.ly.base.common.redis.RedisClientSupport;
import com.ly.base.common.system.redis.RedisKeyConfig;
import com.ly.base.common.util.SerializableUtils;
import com.ly.base.common.util.SpringBeanUtil;

/**
 * 缓存到redis主要为了集群分享session
 * @author LeiYong
 *
 */
public class ShiroSessionDAO extends MemorySessionDAO {
	/**
	 * Logger for this class
	 */
	private static final Logger logger = LoggerFactory.getLogger(ShiroSessionDAO.class);
	
	@Override
	protected Serializable doCreate(Session session) {
		logger.trace("shiro create session start");
		super.doCreate(session);
		//先本地缓存,再存redis
		RedisClientSupport jedis = SpringBeanUtil.getRedisClientSupport();
		if (jedis != null) {
			Serializable sessionId = generateSessionId(session);
			logger.trace("cache by jedis,and sessionId is {}",sessionId);
			assignSessionId(session, sessionId);
			String key = RedisKeyConfig.getShiroSessionCacheKey(sessionId);
			String value = SerializableUtils.serialize(session);
			try {
				jedis.putValue(key, value,session.getTimeout()/1000,TimeUnit.SECONDS);
			} catch (InvalidSessionException | CacheAccessException e) {
			}
		}
		return session.getId();
	}

	@Override
	public void update(Session session) {
		if (session instanceof ValidatingSession && !((ValidatingSession) session).isValid()) {
			return; // 如果会话过期/停止 没必要再更新了
		}
		super.update(session);
		RedisClientSupport jedis = SpringBeanUtil.getRedisClientSupport();
		if (jedis != null) {
			String key = RedisKeyConfig.getShiroSessionCacheKey(session.getId());
//			String value = JSON.toJSONString(session);
			String value = SerializableUtils.serialize(session);
			try {
				jedis.putValue(key, value,session.getTimeout()/1000,TimeUnit.SECONDS);
			} catch (InvalidSessionException | CacheAccessException e) {
			}
		}
	}

	@Override
	public void delete(Session session) {
		super.delete(session);
		//同时删除redis中的session
		RedisClientSupport jedis = SpringBeanUtil.getRedisClientSupport();
		if (jedis != null) {
			String key = RedisKeyConfig.getShiroSessionCacheKey(session.getId());
			try {
				jedis.delete(key);
			} catch (CacheAccessException e) {
			}
		}
	}

	@Override
	protected Session doReadSession(Serializable sessionId) {
		Session session = super.doReadSession(sessionId);
		if (session!=null) {
			return session;
		}
		RedisClientSupport jedis = SpringBeanUtil.getRedisClientSupport();
		if (jedis != null) {
			String key = RedisKeyConfig.getShiroSessionCacheKey(sessionId);
			try {
				Object value = jedis.getValue(key);
				//如果redis中读取不到,尝试从内存中获取
				if (value!=null) {
					Session redisSession =  SerializableUtils.deserialize(value.toString(),Session.class);
					if (redisSession!=null) {
						//取出来后存入内存缓存
						super.update(redisSession);
						return redisSession;
					}
				}
			} catch (CacheAccessException e) {
			}
		}
		return null;
	}

    public Collection<Session> getActiveSessions() {
        Collection<Session> values = super.getActiveSessions();
//		RedisClientSupport jedis = SpringBeanUtil.getRedisClientSupport();
//		if (jedis != null) {
//			String key = RedisKeyConfig.getShiroSessionCacheKey("*");
//			try {
//				Set<String> valueSet = jedis.keys(key);
//				if (valueSet!=null&&valueSet.size()>0) {
//					for (String v : valueSet) {
////					Session session = JSON.parseObject(v, Session.class);
//						Session session = SerializableUtils.deserialize(v,Session.class);
//						values.add(session);
//					}
//				}
//			} catch (CacheAccessException e) {
//			}
//		}
        if (CollectionUtils.isEmpty(values)) {
            return Collections.emptySet();
        } else {
            return Collections.unmodifiableCollection(values);
        }
    }
}