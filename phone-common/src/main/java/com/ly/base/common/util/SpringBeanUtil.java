package com.ly.base.common.util;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.web.context.support.WebApplicationContextUtils;

import com.ly.base.common.redis.RedisClientSupport;

/**
 * 获取spring信息的工具类
 * 
 * @author Administrator
 *
 */
public final class SpringBeanUtil implements ApplicationContextAware {
	private static ApplicationContext applicationContext = null;

	public static ApplicationContext getAppContext() {
		return applicationContext;
	}

	public static Object getBean(String name) {
		return applicationContext.getBean(name);
	}
	public static<T> T getBean(String name,Class<T> clazz) {
		return applicationContext.getBean(name,clazz);
	}
	/**
	 * 若无法获取返回null,有多个实体默认返回第一个
	 * @param clazz
	 * @return
	 */
	public static<T> T getBean(Class<T> clazz) {
		String[] beanNames = getBeanNamesForType(clazz);
		if (beanNames.length >= 1) {
			return getBean(beanNames[0], clazz);
		}else{
			return null;
		}
	}
	public static String[] getBeanNamesForType(Class<?> clazz){
		return applicationContext.getBeanNamesForType(clazz);
	}
	
	public static Object getWebBean(HttpServletRequest request,String beanName){
		ApplicationContext ac = WebApplicationContextUtils.getWebApplicationContext(request.getSession().getServletContext());
		if(ac!=null){
			return ac.getBean(beanName);
		}
		return null;
	}

	/**
	 * 若无法获取返回null,有多个实体默认返回第一个
	 * @param clazz
	 * @return
	 */
	public static<T> T getWebBean(HttpServletRequest request,Class<T> clazz){
		String[] beanNames = getWebBeanNamesForType(request,clazz);
		if (beanNames.length >= 1) {
			return getWebBean(request,beanNames[0], clazz);
		}else{
			return null;
		}
	}
	public static<T> T getWebBean(HttpServletRequest request,String name,Class<T> clazz){
		ApplicationContext ac = WebApplicationContextUtils.getWebApplicationContext(request.getSession().getServletContext());
		if(ac!=null){
			return ac.getBean(name,clazz);
		}
		return null;
	}
	public static String[] getWebBeanNamesForType(HttpServletRequest request,Class<?> clazz){
		ApplicationContext ac = WebApplicationContextUtils.getWebApplicationContext(request.getSession().getServletContext());
		if(ac!=null){
			return ac.getBeanNamesForType(clazz);
		}
		return null;
	}
	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		if (SpringBeanUtil.applicationContext == null) {
			SpringBeanUtil.applicationContext = applicationContext;
		}
	}
	
	public static RedisClientSupport getRedisClientSupport(){
		RedisClientSupport redisClientSupport = (RedisClientSupport) SpringBeanUtil.getBean("redisClientSupport");
		return redisClientSupport;
	}

}