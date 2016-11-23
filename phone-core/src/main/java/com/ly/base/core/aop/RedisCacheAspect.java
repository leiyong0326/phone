//package com.ly.base.core.aop;
//
//import java.lang.reflect.Field;
//import java.lang.reflect.Method;
//import java.util.List;
//
//import org.apache.commons.lang3.StringUtils;
//import org.aspectj.lang.ProceedingJoinPoint;
//import org.aspectj.lang.annotation.Around;
//import org.aspectj.lang.annotation.Aspect;
//import org.aspectj.lang.reflect.MethodSignature;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.beans.factory.annotation.Qualifier;
//import org.springframework.stereotype.Component;
//import org.springframework.util.Assert;
//
//import com.alibaba.fastjson.JSON;
//import com.ly.base.common.annotation.RedisCache;
//import com.ly.base.common.annotation.RedisEvict;
//import com.ly.base.common.annotation.RedisInsert;
//import com.ly.base.common.annotation.RedisUpdate;
//import com.ly.base.common.redis.CacheAccessException;
//import com.ly.base.common.redis.RedisClientSupport;
//import com.ly.base.common.system.SystemConfig;
//import com.ly.base.common.util.ReflectionUtil;
//import com.ly.base.core.util.AopUtil;
//
//@Aspect
//@Component
//public class RedisCacheAspect {
//	/**
//	 * Logger for this class
//	 */
//	private static final Logger logger = LoggerFactory.getLogger(RedisCacheAspect.class);
//
//	@Autowired
//	@Qualifier("redisClientSupport")
//	private RedisClientSupport redisClientSupport;
//	/**
//	 * 方法调用前，先查询缓存。如果存在缓存，则返回缓存数据，阻止方法调用; 如果没有缓存，则调用业务方法，然后将结果放到缓存中
//	 * 
//	 * @param jp
//	 * @return
//	 * @throws Throwable
//	 */
//	@SuppressWarnings("unchecked")
//	@Around("@annotation(com.ly.base.common.annotation.RedisInsert)")
//	public Object redisInsert(ProceedingJoinPoint jp) throws Throwable {
//		Object result = null;
//		Boolean cacheEnable = SystemConfig.CACHE_ENABLED;
//		result = AopUtil.executeJoinPointMethod(jp, jp.getArgs());
//		//是否执行成功
//		if ((int)result>0) {
//			// 判断是否开启缓存
//			if (cacheEnable) {
//				Method method = AopUtil.getMethod(jp);
//				RedisInsert t = AopUtil.getMethodAnnotation(method, RedisInsert.class);
//				if (t!=null) {
//					String clazzName = t.clazz();
//					String[] paramNames = AopUtil.getMethodParamNames(method);
//					//获取cacheKey
//					String cacheKey = AopUtil.parseKeyByParam(t.cacheKey(), paramNames, jp.getArgs());
//					Assert.notNull(cacheKey);
//					//插入成功后再执行缓存
//					//新增通常只有一个参数
//					Object[] params = jp.getArgs();
//					if (params!=null&&params.length>0) {
//						Object param = params[0];
//						long expire = t.expireTime();
//						if (t.batch()) {
//							if (param instanceof List) {
//								//批量
//								String[] keys = cacheKey.split(",");
//								List<Object> list = (List<Object>) param;
//								for (int i = 0; i < keys.length; i++) {
//									//获取key对应的param中的对象,不确定获取到的值是否与参数顺序一致
//									//String cacheKey = AopUtil.parseKeyByParam(t.cacheKey(), paramNames, jp.getArgs());
//									singleInsert(clazzName, keys[i],  list.get(i), expire);
//								}
//							}else{
//								if (logger.isWarnEnabled()) {
//									logger.warn("redisInsert:"+clazzName+"->"+cacheKey+"->pram is not list");
//								}
//							}
//						}else{
//							//单笔
//							singleInsert(clazzName, cacheKey,  param, expire);
//						}
//					}
//				}
//			}
//		}
//		return result;
//	}
//	/**
//	 * 方法调用前，先查询缓存。如果存在缓存，则返回缓存数据，阻止方法调用; 如果没有缓存，则调用业务方法，然后将结果放到缓存中
//	 * 
//	 * @param jp
//	 * @return
//	 * @throws Throwable
//	 */
//	@Around("@annotation(com.ly.base.common.annotation.RedisCache)")
//	public Object redisCache(ProceedingJoinPoint jp) throws Throwable {
//		Object result = null;
//		Boolean cacheEnable = SystemConfig.CACHE_ENABLED;
//		// 判断是否开启缓存
//		if (cacheEnable) {
//			//考虑批量新增和单笔新增
//			Method method = AopUtil.getMethod(jp);
//			RedisCache t = AopUtil.getMethodAnnotation(method, RedisCache.class);
//			if (t!=null) {
//				String clazzName = t.clazz();
//				String[] paramNames = AopUtil.getMethodParamNames(method);
//				//获取cacheKey
//				String cacheKey = AopUtil.parseKeyByParam(
//						ReflectionUtil.getField(t.getClass().getField("cacheKey"), t).toString(), paramNames, jp.getArgs());
//				Assert.notNull(cacheKey);
//				// 使用redis 的hash进行存取，易于管理
//				boolean isNull = false;
//				long expire = t.expireTime();
//				result = redisClientSupport.getHash(clazzName, cacheKey);
//				isNull = result == null;
//				if (isNull) {
//					try {
//						//获取方法返回结果
//						result = AopUtil.executeJoinPointMethod(jp, null);// 如果返回list?如何读取list?读取部分时如何处理?
//						redisClientSupport.putHash(clazzName, cacheKey, result);
//						setExpire(clazzName, cacheKey, expire);
//						return result;
//					} catch (Throwable e) {
//						e.printStackTrace();
//					}
//				}else{
//					setExpire(clazzName, cacheKey, expire);
//					if (logger.isWarnEnabled()) {
//						logger.warn("cache:"+clazzName+"->"+jp.getArgs()+"->"+cacheKey);
//					}
//					return result;
//				}
//			}
//		}
//		result = AopUtil.executeJoinPointMethod(jp, null);
//		return result;
//	}
//	/**
//	 * 在方法调用前清除缓存，然后调用业务方法
//	 * 
//	 * @param jp
//	 * @return
//	 * @throws Throwable
//	 */
//	@Around("@annotation(com.ly.base.common.annotation.RedisUpdate)")
//	public Object redisUpdate(ProceedingJoinPoint jp) throws Throwable {
//		Boolean cacheEnable = SystemConfig.CACHE_ENABLED;
//		Object result = jp.proceed(jp.getArgs());
//		//更新执行成功才更新redis
//		if ((int)result>0) {
//			if (cacheEnable) {
//				// 得到被代理的方法
//				Method method = ((MethodSignature) jp.getSignature()).getMethod();
//				// 得到被代理的方法上的注解
//				RedisUpdate t = method.getAnnotation(RedisUpdate.class);
//				if (t == null) {
//					return jp.proceed(jp.getArgs());
//				}
//				//获取参数名列表
//				String[] paramNames = AopUtil.getMethodParamNames(method);
//				// 获取cacheKey通过spel
//				String cacheKey = AopUtil.parseKeyByParam(
//						ReflectionUtil.getField(t.getClass().getField("cacheKey"), t).toString(), paramNames, jp.getArgs());
//				Assert.notNull(cacheKey);
//				String clazz = t.clazz();
//				if (logger.isDebugEnabled()) {
//					logger.debug("更新缓存:{}", clazz);
//				}
//				//如果是批量更新
//				if (t.batch()) {
//					//获取每笔更新的数据主键
//					String[] keys = cacheKey.split(",");
//					//获取更新列
//					String[] updateKeys = t.updateKeys();
//					Assert.notNull(updateKeys);
//					// 获取需要更新的对象,并更新所有需要更新的列
//					for (String key : keys) {
//						singleUpdate(updateKeys, clazz, key, jp.getArgs(), paramNames, t.expireTime());
//					}
//				} else {
//					// 公共可抽离
//					String[] updateKeys = t.updateKeys();
//					singleUpdate(updateKeys, clazz, cacheKey, jp.getArgs(), paramNames, t.expireTime());
//				}
//
//			}
//		}
//		return result;
//	}
//
//	/**
//	 * 单笔插入
//	 * @param clazzName
//	 * @param cacheKey
//	 * @param value
//	 * @param expire
//	 * @throws CacheAccessException 
//	 */
//	public void singleInsert(String clazzName,String cacheKey,Object value,long expire) throws CacheAccessException{
//		if (StringUtils.isNotBlank(cacheKey)) {
//			redisClientSupport.putHash(clazzName, cacheKey, value);
//			setExpire(clazzName, cacheKey, expire);
//		}else{
//			if (logger.isWarnEnabled()) {
//				logger.warn("singleInsert:"+clazzName+"->"+cacheKey+"->"+value);
//			}
//		}
//	}
//	/**
//	 * 单笔操作更新
//	 * @param updateKeys
//	 * @param clazzName
//	 * @param cacheKey
//	 * @param args
//	 * @param paramNames
//	 * @param expireTime
//	 * @throws CacheAccessException 
//	 * @throws NoSuchFieldException
//	 * @throws SecurityException
//	 * @throws IllegalArgumentException
//	 * @throws IllegalAccessException
//	 */
//	private void singleUpdate(String[] updateKeys,String clazzName,String cacheKey,Object[] args,String[] paramNames,long expireTime) throws CacheAccessException {
//		Assert.notNull(updateKeys);
//		Object obj = redisClientSupport.getHash(clazzName, cacheKey);
//		if (obj!=null) {
//			for (String uk : updateKeys) {
//				String[] uks = uk.split(":");// 0为参数名,1为列明
//				// 更新对应缓存
//				try {
//					Field f = obj.getClass().getDeclaredField(uks[1]);
//					Object value = getMethodParamValue(paramNames, args, uks[0]);
//					if (value != null) {
//						f.set(obj, value);
//					} else {
//						if (logger.isWarnEnabled()) {
//							logger.warn("singleUpdate:" + paramNames + "->" + args + "->" + uks[0]);
//						}
//					}
//				} catch (NoSuchFieldException | SecurityException | IllegalArgumentException | IllegalAccessException e) {
//					if (logger.isWarnEnabled()) {
//						logger.warn("singleUpdate:"+clazzName+"->" + paramNames + "->" + args + "->" + uks[0]);
//					}
//				}
//			}
//			//obj为null无意义
//			redisClientSupport.putHash(clazzName, cacheKey, obj);
//			setExpire(clazzName, cacheKey, expireTime);
//		}
//		
//	}
//	/**
//	 * 在方法调用前清除缓存，然后调用业务方法
//	 * 
//	 * @param jp
//	 * @return
//	 * @throws Throwable
//	 */
//	@Around("@annotation(com.ly.base.common.annotation.RedisEvict)")
//	public Object redisEvict(ProceedingJoinPoint jp) throws Throwable {
//		Boolean cacheEnable = SystemConfig.CACHE_ENABLED;
//		if (cacheEnable) {
//			// 得到被代理的方法
//			Method method = ((MethodSignature) jp.getSignature()).getMethod();
//			// 得到被代理的方法上的注解
//			RedisEvict t = method.getAnnotation(RedisEvict.class);
//			if (t == null) {
//				return jp.proceed(jp.getArgs());
//			}
//			String[] paramNames = AopUtil.getMethodParamNames(method);
//			//获取cacheKey
//			String cacheKey = AopUtil.parseKeyByParam(t.cacheKey(), paramNames, jp.getArgs());
//			Assert.notNull(cacheKey);
//			String clazzName = t.clazz();
//			if (logger.isDebugEnabled()) {
//				logger.debug("清空缓存:{}", clazzName);
//			}
//			if (t.batch()) {
//				String[] keys = cacheKey.split(",");
//				// 清除对应缓存
//				for (String key : keys) {
//					redisClientSupport.deleteHashAll(clazzName, key);
//				}
//			}else{
//				// 清除对应缓存
//				redisClientSupport.deleteHashAll(clazzName, cacheKey);
//			}
//		}
//		return jp.proceed(jp.getArgs());
//	}
//	/**
//	 * 设置过期时间
//	 * @param clazzName
//	 * @param cacheKey
//	 * @param expire
//	 * @return
//	 * @throws CacheAccessException 
//	 */
//	public Boolean setExpire(String clazzName,String cacheKey,long expire) throws CacheAccessException{
//		if (expire>0) {
//			return redisClientSupport.expire(clazzName+":"+cacheKey, expire);
//		}
//		return true;
//	}
//	/**
//	 * 通过参数名获取参数值
//	 * @param paramNames
//	 * @param paramValues
//	 * @param param
//	 * @return
//	 */
//	private Object getMethodParamValue(String[] paramNames,Object[] paramValues,String param){
//		Assert.notNull(param);
//		for (int i = 0; i < paramNames.length; i++) {
//			int index = param.indexOf(".");
//			if (index >0) {
//				String p = param.substring(0,index);
//				return getMethodParamValueByObject(p, paramValues[i]);
//			}else{
//				if (param.equals(paramNames[i])) {
//					return paramValues[i];
//				}
//			}
//		}
//		if (logger.isWarnEnabled()) {
//			logger.warn("getMethodParamValue:"+paramNames+"->"+paramValues+"->"+param);
//		}
//		return null;
//	}
//	/**
//	 * 如果获取失败则返回null
//	 * @param param
//	 * @param obj
//	 * @return
//	 */
//	private Object getMethodParamValueByObject(String param,Object obj){
//		int index = param.indexOf(".");
//		try {
//			if (index >0) {
//				String p = param.substring(0,index);
//				Object value = obj.getClass().getDeclaredField(param).get(obj);
//				if (value==null||value.toString().equals("")) {
//					if (logger.isWarnEnabled()) {
//						logger.warn("getMethodParamValueByObject:"+obj.getClass().getName()+"->"+param);
//					}
//					return value;
//				}
//				return getMethodParamValueByObject(p, value);
//			}else{
//				return obj.getClass().getDeclaredField(param).get(obj);
//			}
//		} catch (IllegalArgumentException | IllegalAccessException | NoSuchFieldException | SecurityException e) {
//			if (logger.isWarnEnabled()) {
//				logger.warn("getMethodParamValueByObject:"+obj.getClass().getName()+"->"+param);
//			}
//			e.printStackTrace();
//		}
//		return null;
//	}
//
////	/**
////	 * 根据类名、方法名和参数生成key
////	 * 
////	 * @param clazzName
////	 * @param methodName
////	 * @param args
////	 *            方法参数
////	 * @return
////	 */
////	protected String genKey(String clazzName, String methodName, Object[] args) {
////		StringBuilder sb = new StringBuilder(clazzName);
////		sb.append(DELIMITER);
////		sb.append(methodName);
////		sb.append(DELIMITER);
////
////		for (Object obj : args) {
////			sb.append(obj.toString());
////			sb.append(DELIMITER);
////		}
////
////		return sb.toString();
////	}
//
//	protected String serialize(Object target) {
//		return JSON.toJSONString(target);
//	}
//
//	@SuppressWarnings({ "unchecked", "rawtypes" })
//	protected Object deserialize(String jsonString, Class clazz) {
//		// 序列化结果是普通对象
//		return JSON.parseObject(jsonString, clazz);
//	}
//
//	/**
//	 * 获取缓存的key key 定义在注解上，支持SPEL表达式
//	 * 
//	 * @param pjp
//	 * @return
//	 */
//	// private String parseKey(String key, Method method, Object[] args) {
//	// // SPEL上下文
//	// ExpressionParser parser = getParser();
//	// StandardEvaluationContext context = new StandardEvaluationContext();
//	// // 把方法参数放入SPEL上下文中
//	// for (int i = 0; i < params.length; i++) {
//	// context.setVariable(params[i], args[i]);
//	// }
//	// return parser.parseExpression(key).getValue(context, String.class);
//	// }
//
//}