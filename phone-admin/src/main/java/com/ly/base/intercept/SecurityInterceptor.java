package com.ly.base.intercept;

import java.io.IOException;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authz.AuthorizationException;
import org.apache.shiro.subject.Subject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import com.alibaba.fastjson.JSONObject;
import com.ly.base.common.em.ext.security.AdminSecurityEnum;
import com.ly.base.common.model.Json;
import com.ly.base.common.system.cookies.AdminCookieConfig;
import com.ly.base.common.system.page.AdminPageConfig;
import com.ly.base.common.util.ArrayUtil;
import com.ly.base.common.util.ReflectionUtil;
import com.ly.base.common.util.SpringBeanUtil;
import com.ly.base.common.util.StringUtil;
import com.ly.base.core.model.sys.SysMenu;
import com.ly.base.proxy.sys.SysMenuProxy;
import com.ly.base.util.AdminSecurityUtil;

/**
 * 权限拦截器,主要验证用户是否具有页面/功能访问权限 .html采用url验证 .do采用权限验证
 * 
 * @author LeiYong
 *
 */
public class SecurityInterceptor extends HandlerInterceptorAdapter {
	/**
	 * Logger for this class
	 */
	private static final Logger logger = LoggerFactory.getLogger(SecurityInterceptor.class);

	private String[] igoreRegexUrls;//不拦截的url
	private String[] childRegexUrls;//不拦截的子页面
	private String[] editFuncRegex;
	private String[] showFuncRegex;
	private String[] delFuncRegex;
	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {
		if (logger.isDebugEnabled()) {
			logger.debug("preHandle(HttpServletRequest, HttpServletResponse, Object) - start"); //$NON-NLS-1$
		}

		Subject subject = SecurityUtils.getSubject();
		String requestUrl = request.getRequestURI().replace(request.getContextPath(), "");
		if (logger.isDebugEnabled()) {
			logger.debug(StringUtil.appendStringNotNull("###", "请求的url",requestUrl)); //$NON-NLS-1$
		}
		boolean res = ArrayUtil.foreach(igoreRegexUrls, (v,i) -> {
			return !requestUrl.matches(v);// 如果匹配则不再循环
		});
		// 如果返回false,则表示匹配忽略的url
		if (!res) {
			if (logger.isDebugEnabled()) {
				logger.debug("preHandle(HttpServletRequest, HttpServletResponse, Object) - end"); //$NON-NLS-1$
			}
			return true;
		}
		System.out.println(requestUrl);
		boolean checkResult = false;
		// 如果已登录则直接跳转
		if (subject != null && subject.isAuthenticated()) {
			// 判断是否是访问url
			if (requestUrl.matches(".+\\.(html|jsp)")) {
				checkResult = processMenuSecurity(requestUrl,request);
			} else {
				checkResult = processFuncSecurity(requestUrl);
			}
			// 功能
		}
		if (!checkResult) {
			redirect(response);

			if (logger.isDebugEnabled()) {
				logger.debug("preHandle(HttpServletRequest, HttpServletResponse, Object) - end"); //$NON-NLS-1$
			}
			return false;
		}

		if (logger.isDebugEnabled()) {
			logger.debug("preHandle(HttpServletRequest, HttpServletResponse, Object) - end"); //$NON-NLS-1$
		}
		return true;
	}

	/**
	 * 检测是否具有菜单权限
	 * 
	 * @param url
	 * @param request 
	 * @return
	 * @throws AuthorizationException
	 */
	private boolean processMenuSecurity(String url, HttpServletRequest request) {
		if (logger.isDebugEnabled()) {
			logger.debug("processMenuSecurity(String, HttpServletRequest) - start"); //$NON-NLS-1$
		}

		//子页面不做权限判断
		boolean res = ArrayUtil.foreach(childRegexUrls, (v,i) -> {
			return !url.matches(v);// 如果匹配则不再循环
		});
		// 如果返回false,则表示匹配子页面的url
		if (!res) {
			if (logger.isDebugEnabled()) {
				logger.debug("processMenuSecurity(String, HttpServletRequest) - end"); //$NON-NLS-1$
			}
			return true;
		}
		Object obj = SecurityUtils.getSubject().getSession().getAttribute(AdminCookieConfig.SYS_MENU_KEY);
		if (obj==null) {
			//如果为空重新加载代理类执行查询
			SysMenuProxy proxy = SpringBeanUtil.getWebBean(request,SysMenuProxy.class);
			if (proxy!=null) {
				Json json = proxy.findAllMenuByRole(request);
				if (json.isSuccess()) {
					obj = SecurityUtils.getSubject().getSession().getAttribute(AdminCookieConfig.SYS_MENU_KEY);
				}
			}
		}
		if (obj!=null) {
			List<?> list = ReflectionUtil.convertObjectToBean(obj, List.class);
			if (list!=null) {
				final String requestUrl = url.startsWith("/")?url.substring(1):url;
				boolean returnboolean = !ArrayUtil.foreach(list, (m, i) -> {
					JSONObject jo = ReflectionUtil.convertObjectToBean(m, JSONObject.class);
					SysMenu sm = null;
					if (jo != null) {
						sm = JSONObject.toJavaObject(jo, SysMenu.class);
					} else {
						sm = ReflectionUtil.convertObjectToBean(m, SysMenu.class);
					}
					if (sm != null && requestUrl.equals(sm.getUrl())) {
						return false;
					}
					return true;
				});
				if (logger.isDebugEnabled()) {
					logger.debug("processMenuSecurity(String, HttpServletRequest) - end"); //$NON-NLS-1$
				}
				return returnboolean;
			}
		}

		if (logger.isDebugEnabled()) {
			logger.debug("processMenuSecurity(String, HttpServletRequest) - end"); //$NON-NLS-1$
		}
		return false;
	}

	/**
	 * 检测是否具有功能权限
	 * 
	 * @param url
	 * @return
	 */
	private boolean processFuncSecurity(String url) {
		if (logger.isDebugEnabled()) {
			logger.debug("processFuncSecurity(String) - start"); //$NON-NLS-1$
		}

		String securityKey = url.replaceAll("^/(\\w+?)/.*$", "$1");
		AdminSecurityEnum em = AdminSecurityUtil.getSecurityEnumByValue(securityKey);
		if (em == null) {
			if (logger.isDebugEnabled()) {
				logger.debug("processFuncSecurity(String) - end"); //$NON-NLS-1$
			}
			return true;
		}
		// 验证edit
		boolean editNotMatche = ArrayUtil.foreach(editFuncRegex, (v,i) -> {
			return !url.matches(v);// 如果匹配则不再循环
		});
		if (!editNotMatche) {
			boolean returnboolean = AdminSecurityUtil.isPermitted(em, AdminSecurityUtil.EDIT_FUNC);
			if (logger.isDebugEnabled()) {
				logger.debug("processFuncSecurity(String) - end"); //$NON-NLS-1$
			}
			return returnboolean;
		}
		// 验证del
		boolean delNotMatche = ArrayUtil.foreach(delFuncRegex, (v,i) -> {
			return !url.matches(v);// 如果匹配则不再循环
		});
		if (!delNotMatche) {
			boolean returnboolean = AdminSecurityUtil.isPermitted(em, AdminSecurityUtil.DEL_FUNC);
			if (logger.isDebugEnabled()) {
				logger.debug("processFuncSecurity(String) - end"); //$NON-NLS-1$
			}
			return returnboolean;
		}
		// 验证get
		boolean getNotMatche = ArrayUtil.foreach(showFuncRegex, (v,i) -> {
			return !url.matches(v);// 如果匹配则不再循环
		});
		if (!getNotMatche) {
			boolean returnboolean = AdminSecurityUtil.isPermitted(em, AdminSecurityUtil.SHOW_FUNC);
			if (logger.isDebugEnabled()) {
				logger.debug("processFuncSecurity(String) - end"); //$NON-NLS-1$
			}
			return returnboolean;
		}
		//全部不匹配则不在权限验证范围内

		if (logger.isDebugEnabled()) {
			logger.debug("processFuncSecurity(String) - end"); //$NON-NLS-1$
		}
		return true;
	}

	/**
	 * 跳转无权访问
	 * 
	 * @param response
	 * @throws IOException
	 */
	private void redirect(HttpServletResponse response) throws IOException {
		if (logger.isDebugEnabled()) {
			logger.debug("redirect(HttpServletResponse) - start"); //$NON-NLS-1$
		}

		response.sendRedirect(StringUtil.appendStringNotNull(null, AdminPageConfig.PROJECT_NAME,AdminPageConfig.NOPERMS_PAGE));

		if (logger.isDebugEnabled()) {
			logger.debug("redirect(HttpServletResponse) - end"); //$NON-NLS-1$
		}
	}

	public void setIgoreRegexUrls(String[] igoreRegexUrls) {
		this.igoreRegexUrls = igoreRegexUrls;
	}

	public void setChildRegexUrls(String[] childRegexUrls) {
		this.childRegexUrls = childRegexUrls;
	}

	public void setEditFuncRegex(String[] editFuncRegex) {
		this.editFuncRegex = editFuncRegex;
	}

	public void setShowFuncRegex(String[] showFuncRegex) {
		this.showFuncRegex = showFuncRegex;
	}

	public void setDelFuncRegex(String[] delFuncRegex) {
		this.delFuncRegex = delFuncRegex;
	}
	
}
