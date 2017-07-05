package com.ly.base.common.system.redis;

import java.io.Serializable;

import org.apache.commons.lang3.StringUtils;

import com.ly.base.common.util.StringUtil;

public class RedisKeyConfig {
	private static final String SPLIT_CHARPTER = ":";
	/** 菜单 **/
	private static final String MENU_CACHE_KEY = "SYS_MENU";//单个菜单
	private static final String MENU_STRING_CACHE_KEY = "SYS_MENU_STRING";//上级菜单的子功能集
	private static final String MENU_MENU_CACHE_KEY = "SYS_MENU_MENU";//菜单
	private static final String MENU_FUNC_CACHE_KEY = "SYS_MENU_FUNC";//菜单功能
	private static final String MENU_FUNC_STRING_CACHE_KEY = "SYS_MENU_FUNC_STRING";//菜单功能
	private static final String SEQUENCE_KEY = "SEQ";
	/** shiro权限 **/
	public static final String SHIRO_CACHE_KEY = "SHIRO";//Shiro的缓存主键
	public static final String SHIRO_SESSION_CACHE_KEY = "SHIRO_SESSION";// Shiro的缓存主键
	public static final String REMEMBER_CACHE_KEY = "REMEMBER";//Shiro的缓存主键
	public static final String API_USER_CACHE_KEY="API_USER";//微信用户缓存主键
	public static final String API_CAPTCHA_CACHE_KEY="API_CAPTCHA";//API短信验证码主键
	public static final String API_SENDSMS_TIMES_CACHE_KEY="API_SENDSMS_TIMES";//发送短信次数缓存

	/** 基础key支持 */
	public static String getBaseCacheKey(String... keys) {
		return StringUtil.appendString(SPLIT_CHARPTER, StringUtils.EMPTY, keys);
	}
	public static String getMenuMenuCacheKey(Integer rolePk){
		return getBaseCacheKey(MENU_MENU_CACHE_KEY, String.valueOf(rolePk));
	}
	public static String getMenuFuncCacheKey(Integer rolePk) {
		return getBaseCacheKey(MENU_FUNC_CACHE_KEY, String.valueOf(rolePk));
	}
	public static String getMenuFuncStringCacheKey(Integer rolePk) {
		return getBaseCacheKey(MENU_FUNC_STRING_CACHE_KEY, String.valueOf(rolePk));
	}
	/**
	 * 单个菜单
	 * @return
	 */
	public static String getMenuCacheKey(String menuPk) {
		return getBaseCacheKey(MENU_CACHE_KEY,menuPk);
	}
	/**
	 * 菜单的功能按钮集
	 * @param menuUpPk
	 * @return
	 */
	public static String getMenuStringCacheKey(String menuUpPk) {
		return getBaseCacheKey(MENU_STRING_CACHE_KEY,menuUpPk);
	}
	public static String getShiroCacheKey() {
		return SHIRO_CACHE_KEY;
	}
	public static String getRememberCacheKey(String cookieKey) {
		return getBaseCacheKey(REMEMBER_CACHE_KEY,cookieKey);
	}
	public static String getApiUserCacheKey(String cookieKey) {
		return getBaseCacheKey(API_USER_CACHE_KEY,cookieKey);
	}
	public static String getApiCaptchaCacheKey(String orgPk,String phone){
		return getBaseCacheKey(API_CAPTCHA_CACHE_KEY,orgPk,phone);
	}
	public static String getApiSendSmsTimesCacheKey(String orgPk){
		return getBaseCacheKey(API_SENDSMS_TIMES_CACHE_KEY,orgPk);
	}
	public static String getSequenceKey() {
		return SEQUENCE_KEY;
	}
	public static String getShiroSessionCacheKey(Serializable sessionId) {
		return getBaseCacheKey(SHIRO_SESSION_CACHE_KEY, sessionId.toString());
	}
}
