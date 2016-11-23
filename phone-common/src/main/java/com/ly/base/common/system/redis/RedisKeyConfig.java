package com.ly.base.common.system.redis;

import com.ly.base.common.system.SystemConfig;
import com.ly.base.common.util.StringUtil;

public class RedisKeyConfig {
	private static final String MENU_CACHE_KEY = "SYS_MENU";//单个菜单
	private static final String MENU_STRING_CACHE_KEY = "SYS_MENU_STRING";//上级菜单的子功能集
	private static final String MENU_MENU_CACHE_KEY = "SYS_MENU_MENU";//菜单
	private static final String MENU_FUNC_CACHE_KEY = "SYS_MENU_FUNC";//菜单功能
	private static final String MENU_FUNC_STRING_CACHE_KEY = "SYS_MENU_FUNC_STRING";//菜单功能
	private static final String SEQUENCE_KEY = "SEQ";
	
	public static final String SHIRO_CACHE_KEY = "SHIRO";//Shiro的缓存主键
	public static final String REMEMBER_CACHE_KEY = "REMEMBER";//Shiro的缓存主键
	public static final String API_USER_CACHE_KEY="API_USER";//微信用户缓存主键
	public static final String API_CAPTCHA_CACHE_KEY="API_CAPTCHA";//API短信验证码主键
	public static final String API_SENDSMS_TIMES_CACHE_KEY="API_SENDSMS_TIMES";//发送短信次数缓存
	
	public static final String API_SALER_ROLE_TYPE="API_SALER_TYPE";//
	
	public static String getMenuMenuCacheKey(Integer rolePk){
		return StringUtil.appendStringNotNull(SystemConfig.SPLIT_CHARPTER,
				MENU_MENU_CACHE_KEY, String.valueOf(rolePk));
	}
	public static String getMenuFuncCacheKey(Integer rolePk) {
		return StringUtil.appendStringNotNull(SystemConfig.SPLIT_CHARPTER,
				MENU_FUNC_CACHE_KEY, String.valueOf(rolePk));
	}
	public static String getMenuFuncStringCacheKey(Integer rolePk) {
		return StringUtil.appendStringNotNull(SystemConfig.SPLIT_CHARPTER,
				MENU_FUNC_STRING_CACHE_KEY, String.valueOf(rolePk));
	}
	/**
	 * 单个菜单
	 * @return
	 */
	public static String getMenuCacheKey(String menuPk) {
		return StringUtil.appendStringNotNull(":", MENU_CACHE_KEY,menuPk);
	}
	/**
	 * 菜单的功能按钮集
	 * @param menuUpPk
	 * @return
	 */
	public static String getMenuStringCacheKey(String menuUpPk) {
		return StringUtil.appendStringNotNull(":", MENU_STRING_CACHE_KEY,menuUpPk);
	}
	public static String getShiroCacheKey() {
		return SHIRO_CACHE_KEY;
	}
	public static String getRememberCacheKey(String cookieKey) {
		return StringUtil.appendStringNotNull(":",REMEMBER_CACHE_KEY,cookieKey);
	}
	public static String getApiUserCacheKey(String cookieKey) {
		return StringUtil.appendStringNotNull(":",API_USER_CACHE_KEY,cookieKey);
	}
	public static String getApiCaptchaCacheKey(String orgPk,String phone){
		return StringUtil.appendStringNotNull(":",API_CAPTCHA_CACHE_KEY,orgPk,phone);
	}
	public static String getApiSendSmsTimesCacheKey(String orgPk){
		return StringUtil.appendStringNotNull(":", API_SENDSMS_TIMES_CACHE_KEY,orgPk);
	}
	public static String getApiSalerTypeCacheKey(String orgPk,String type){
		return StringUtil.appendStringNotNull(":", API_SALER_ROLE_TYPE,orgPk,type);
	}
	public static String getSequenceKey() {
		return SEQUENCE_KEY;
	}
}
