package com.ly.base.core.util;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;

import com.alibaba.fastjson.JSON;
import com.ly.base.common.model.Json;
import com.ly.base.common.system.SystemConfig;
import com.ly.base.common.util.MD5Util;
import com.ly.base.common.util.ReflectionUtil;
import com.ly.base.common.util.StringUtil;
import com.ly.base.core.model.sys.SysUser;

public final class WebUtils {
	
	/**
	 * 登录的cookie key.
	 */
	public static final String USER_COOKIE_KEY="loginUser";
	
	/**
	 * 登录跳转
	 */
	public static final String REDIRECT = "redirectURL";
	
	/**
	 * 字符编码
	 */
	public static final String CHARSET=SystemConfig.CHARSET;
	
	/**
	 * 写入Cookie
	 * 
	 * @param response response对象
	 * @param key 读取cookie的键
	 * @param value 写入cookie的值
	 */
	public static void writeCookie(HttpServletResponse response, String key, Object value, int cookieAge) {
		try {
			String b = null;
			if (value instanceof String) {
				b = value.toString();
			} else {
				b = JSON.toJSONString(value);
			}
			Cookie cookie = new Cookie(key, URLEncoder.encode(b, SystemConfig.CHARSET));
			cookie.setPath("/");
			if (cookieAge > 0) {
				cookie.setMaxAge(cookieAge);
			}
			response.addCookie(cookie);
		} catch (UnsupportedEncodingException e) {

		}
	}

	/**
	 * 删除Cookie
	 * 
	 * @param response response对象
	 * @param key 读取cookie的键
	 */
	public static void deleteCookie(HttpServletResponse response, String key) {
		Cookie cookie = new Cookie(key, null);
		cookie.setMaxAge(0);
		response.addCookie(cookie);
	}

	/**
	 * 根据键读取cookie的值
	 * 
	 * @param request 请求对象
	 * @param key 读取cookie的键
	 * @return cookie的值
	 */
	public static String readCookie(HttpServletRequest request, String key) {
		Cookie[] cookies = request.getCookies();
		if (cookies != null&&key!=null) {
			for (Cookie cookie : cookies) {
				if (cookie != null && key.equalsIgnoreCase(cookie.getName())) {
					try {
						String value = URLDecoder.decode(cookie.getValue(), SystemConfig.CHARSET);
						return value;
					} catch (UnsupportedEncodingException e) {
					}
				}
			}
		}
		return null;
	}

	/**
	 * 获取当前登录用户
	 * 
	 * @param request 请求对象
	 * @return 用户信息
	 */
	public static SysUser getLoginUser(HttpServletRequest request) {
		Subject subject = SecurityUtils.getSubject();
		if (subject!=null) {
			SysUser sysUser = ReflectionUtil.convertObjectToBean(subject.getPrincipal(), SysUser.class);
			return sysUser;
		}else if(request!=null){
			String jsonText = readCookie(request, USER_COOKIE_KEY);
			SysUser sysUser =JSON.parseObject(jsonText, SysUser.class);
			if (sysUser == null) {
				HttpSession session = request.getSession();
				sysUser=(SysUser) session.getAttribute("loginUser");
			}
			return sysUser;
		}
		return null;
	}
	
	/**
	 * 获取登录用户的组织主键
	 * 
	 * @param request 请求对象
	 * @return 用户名
	 */
//	public static String getOrganizationPk(HttpServletRequest request) {
//		SysUser sysUser = getLoginUser(request);
//		if (sysUser != null) {
//			return sysUser.getOrgPk();
//		}
//		return null;
//	}
	
	public static  Json checkKaptcha(HttpServletRequest request){
		Json json=new Json();
		//从session中取出servlet生成的验证码text值  
		String kaptchaExpected = (String)SecurityUtils.getSubject().getSession().getAttribute(com.google.code.kaptcha.Constants.KAPTCHA_SESSION_KEY); 
		SecurityUtils.getSubject().getSession().setAttribute(com.google.code.kaptcha.Constants.KAPTCHA_SESSION_KEY,"");
		//获取用户页面输入的验证码  
		String kaptchaReceived = request.getParameter("captcha");  
		//校验验证码是否正确  
		if (kaptchaReceived == null || !kaptchaReceived.equalsIgnoreCase(kaptchaExpected)){  
			json.setSuccess(false).setMsg("验证码不正确!");
			return json;
		} 
		json.setSuccess(true);
		return json;
	}

	/**
	 * 获取缓存key
	 * @param un
	 * @param pwd
	 * @param ip
	 * @return
	 */
	public static String getCacheKey(String un, String pwd, String ip) {
		if (StringUtil.checkNotEmpty(un,pwd,ip)) {
			return MD5Util.md5s(un,pwd,ip);
		}
		return null;
	}
	/**
	 * 验证缓存key
	 * @param un
	 * @param pwd
	 * @param ip
	 * @return
	 */
	public static boolean checkCacheKey(String un, String pwd, String ip,String cacheKey) {
		if (StringUtils.isNotBlank(un)&&StringUtils.isNotBlank(pwd)&&StringUtils.isNotBlank(ip)&&StringUtils.isNotBlank(cacheKey)) {
			return MD5Util.md5s(un,pwd,ip).equals(cacheKey);
		}
		return false;
	}
	
}