package com.ly.base.common.system;

import com.qiniu.util.Auth;

/**
 * 系统配置项
 * @author LeiYong
 *
 */
public class SystemConfig {
	//系统开关
	public static final boolean CAPTCHA_ENABLE= false;//验证码
	public static final boolean SYSTEM_FLAG_SENCURITY = true;//安全验证
	public static final boolean SYSTEM_FLAG_SESSION_FILTER = true;//session过滤器
	public static final boolean SYSTEM_FLAG_SHIRO = true;//shiro开启
	public static final boolean CACHE_ENABLED = false;//AOP缓存开启
	public static final boolean LOG_ENABLE = true;//AOP日志开启
	public static final boolean LOG_IP_ENABLE= true;//记录ip所在地等信息
	//参数配置
	public static final String SPLIT_CHARPTER = "_";//分隔符
	public static final String CHARSET = "utf-8";//编码格式
	public static final int LOGIN_COUNT = 5;//登录错误次数限制
	public static final int LOGIN_COUNT_TIME = 5;//错误N次后冻结时间N分钟
	public static final int LOGIN_ERROR_TIME = 60;//登录失败记录保留N分钟
	public static final int LOG_CONTENT_LENGTH = 81;//多主键时保留日志主键位数
	//数值常量
	public static final String ENABLE = "1";//分隔符
	public static final String DISABLE = "0";//分隔符
	
	public static final String SHOP_CAR_DETIAL_HTML_DIR="/usr/java/car_param/";//新车配置详情html页面保存路径
	
	public static final String SHOP_CAR_PARTS_DETIAL_HTML_DIR="/usr/java/car_parts_param/";//精品参数详情html页面保存路径
	
	
	//cacheKey
	public static final Auth QINIU_AUTH = Auth.create(SystemConfig.QINIU_ACCESS_KEY, SystemConfig.QINIU_SECRET_KEY);
	
	private static final String QINIU_ACCESS_KEY = "nR_4iiNEOk3fHuiPMiBQf4ubWLseJPr7WqNCQI1Z";
	private static final String QINIU_SECRET_KEY = "cCg81XRaR17T2KCAZbkQ83lEsh1LUkkwGMEgXiLH";
}
