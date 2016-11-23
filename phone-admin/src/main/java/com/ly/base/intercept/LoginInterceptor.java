package com.ly.base.intercept;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import com.alibaba.fastjson.JSON;
import com.ly.base.common.model.Json;
import com.ly.base.common.redis.RedisClientSupport;
import com.ly.base.common.system.cookies.AdminCookieConfig;
import com.ly.base.common.system.page.AdminPageConfig;
import com.ly.base.common.system.redis.RedisKeyConfig;
import com.ly.base.common.util.IpUtil;
import com.ly.base.common.util.MD5Util;
import com.ly.base.common.util.StringUtil;
import com.ly.base.core.util.WebUtils;
import com.ly.base.proxy.sys.SysUserProxy;

public class LoginInterceptor extends HandlerInterceptorAdapter{
	/**
	 * Logger for this class
	 */
	private static final Logger logger = LoggerFactory.getLogger(LoginInterceptor.class);

	@Autowired
	@Qualifier("redisClientSupport")
	private RedisClientSupport redisClientSupport;
	@Autowired
	private SysUserProxy proxy;
	
	@Override
	public boolean preHandle(HttpServletRequest request,HttpServletResponse response, Object handler) throws Exception {
		if (logger.isDebugEnabled()) {
			logger.debug("preHandle(HttpServletRequest, HttpServletResponse, Object) - start"); //$NON-NLS-1$
		}
		Subject subject = SecurityUtils.getSubject();
		//如果已登录则直接跳转
		if (subject != null && subject.isAuthenticated()) {
			redirect(request,response);

			if (logger.isDebugEnabled()) {
				logger.debug("preHandle(HttpServletRequest, HttpServletResponse, Object) - end"); //$NON-NLS-1$
			}
			return false;
		}else{
			String cacheKey = WebUtils.readCookie(request, AdminCookieConfig.REMEMBER_ME_KEY);
			if (StringUtils.isNotBlank(cacheKey)) {
				String redisKey = RedisKeyConfig.getRememberCacheKey(cacheKey);
				Object obj = redisClientSupport.getValue(redisKey);
				if (obj!=null) {
					String[] unPwd = obj.toString().split("#");
					String ip = IpUtil.getIp(request);
					if (WebUtils.checkCacheKey(unPwd[0], unPwd[1], ip, cacheKey)) {
						String un = MD5Util.decryptBlend(unPwd[0]);
						un = un==null?"":un.substring(un.indexOf("_")+1);
						String pwd = MD5Util.decryptBlend(unPwd[1]);
						request.setAttribute("autoLoginUser", "1");
						Json json = proxy.loginSystem(request, response, un, pwd, "1");
						if (json.isSuccess()) {
							//如果是自动登录,则将用户信息加入cookies
							WebUtils.writeCookie(response, "autoLoginUser", JSON.toJSONString(json.getObj()), 600);
//							logLoginConsumerService.logLogin(request, subject.getPrincipal());
							redirect(request,response);

							if (logger.isDebugEnabled()) {
								logger.debug("preHandle(HttpServletRequest, HttpServletResponse, Object) - end"); //$NON-NLS-1$
							}
							return false;
						}
					}else{
						WebUtils.deleteCookie(response, AdminCookieConfig.REMEMBER_ME_KEY);
					}
				}
			}
		}
		if (logger.isDebugEnabled()) {
			logger.debug("preHandle(HttpServletRequest, HttpServletResponse, Object) - end"); //$NON-NLS-1$
		}
		return true;
	}
	/**
	 * 跳转主页
	 * @param response
	 * @throws IOException
	 */
	private void redirect(HttpServletRequest request,HttpServletResponse response) throws IOException{
		if (logger.isDebugEnabled()) {
			logger.debug("redirect(HttpServletResponse) - start"); //$NON-NLS-1$
		}
		response.sendRedirect(StringUtil.appendStringNotNull(null,AdminPageConfig.MAIN_PAGE));

		if (logger.isDebugEnabled()) {
			logger.debug("redirect(HttpServletResponse) - end"); //$NON-NLS-1$
		}
	}
}
