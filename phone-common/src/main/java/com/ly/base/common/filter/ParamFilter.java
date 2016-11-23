package com.ly.base.common.filter;

import java.io.IOException;
import java.util.Enumeration;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.filter.OncePerRequestFilter;

import com.ly.base.common.system.XssHttpServletRequestWrapper;
import com.ly.base.common.util.StringUtil;
/**
 * 参数过滤器
 * 过滤xss脚本
 * @author LeiYong
 *
 */
public class ParamFilter extends OncePerRequestFilter {
	private boolean printHead = false;
	private boolean printParam = false;
	private boolean enable = false;
	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		HttpServletRequest xssRequest = enable ? new XssHttpServletRequestWrapper(request) : request;
		checkPrint(xssRequest);
		filterChain.doFilter(xssRequest, response);// 放行。让其走到下个链或目标资源中
	}

	private void checkPrint(HttpServletRequest request){
		if (printHead) {
			logger.debug("###print head start###");
			Enumeration<?> paramNames = request.getHeaderNames();
			for (Enumeration<?> e = paramNames; e.hasMoreElements();) {
				String name = e.nextElement().toString();
				String value = request.getHeader(name);
				logger.debug(StringUtil.appendString("---", "NULL", "请求头部",name,value));
			}
			logger.debug("###print head end###");
		}
		if (printParam) {
			logger.debug("###print param start###");
			Enumeration<?> paramNames = request.getParameterNames();
			for (Enumeration<?> e = paramNames; e.hasMoreElements();) {
				String name = e.nextElement().toString();
				String value = request.getParameter(name);
				logger.debug(StringUtil.appendString("---", "NULL", "请求参数",name,value));
			}
			logger.debug("###print param end###");
		}
	}
	public void setPrintHead(boolean printHead) {
		this.printHead = printHead;
	}

	public void setPrintParam(boolean printParam) {
		this.printParam = printParam;
	}

	public void setEnable(boolean enable) {
		this.enable = enable;
	}
}