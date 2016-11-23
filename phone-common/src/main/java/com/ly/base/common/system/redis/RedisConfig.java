package com.ly.base.common.system.redis;

public class RedisConfig {
	/**
	 * 缓存时间,秒
	 */
	public static final int REMEMBER_REDIS_CACHE_TIME = 2592000;
	
	/**
	 * 缓存key混淆长度
	 */
	public static final int REMEMBER_REDIS_CACHE_RANDOM = 6;
	
	/**
	 * 短信验证有效时间,秒
	 */
	public static final int REMEMBER_REDIS_CACHE_CAPTCHA_TIME=600;
	
	/**
	 * 用户最多可以校验验证码次数
	 */
	public static final int MAX_CHECK_REDIS_CACHE_CAPTCHA_TIMES=5;
	
	/**
	 * 缓存字符串时值与值之间的分隔符,例如缓存短信验证码是 验证码#已校验次数
	 */
	public static final String REDIS_CACHE_STRING_SPLIT="#";
	
	/**
	 * 8*60*60 每个用户发送短信的限定时间 八小时
	 */
	public static final int REMEMER_REDIS_CACHE_SENDSMS_TIMES_TIME=28800;
	
	/**
	 * 每个用户在限定时间内发送的短信次数
	 */
	public static final int MAX_SENDSMS_TIMES=5;
	
	/**
	 *  刷新所有顾问缓存频率(秒) 3*60 * 60 * 1000
	 */
	public static final long CACHE_ALL_SALER_FIXED_RATE = 3*60 * 60 * 1000;
	
	/**
	 * 每日定时刷新所有顾问时间 "0 20 04 ? * *" 每天上午04:20触发 
	 */
	public static final String CACHE_ALL_SALER_CRON="0 20 04 ? * *";
}
