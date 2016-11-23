package com.ly.base.common.system;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;

import org.apache.commons.lang3.StringUtils;

import com.ly.base.common.filter.util.HTMLFilterStrategy;
import com.ly.base.common.util.ArrayUtil;

/**
 * Xss过滤Request
 * 
 * @author LeiYong
 */
public class XssHttpServletRequestWrapper extends HttpServletRequestWrapper {

	public XssHttpServletRequestWrapper(HttpServletRequest request) {
		super(request);
	}

	/**
	 * 覆盖getParameter方法，将参数名和参数值都做xss过滤。<br/>
	 * 如果需要获得原始的值，则通过super.getParameterValues(name)来获取<br/>
	 * getParameterNames,getParameterValues和getParameterMap也可能需要覆盖
	 */
	@Override
	public String getParameter(String name) {
		String value = super.getParameter(name);
		if (value != null) {
			value = HTMLFilterStrategy.getBaesHtmlFilter().filter(value);
			if (name.equals("url")) {
				value = value.replaceAll("((\"||'||&apos;||&quot;)(\\s||&nbsp;)+[oO][nN].*=)(\\s||&nbsp;)*(\"||'||&apos;||&quot;)", "");
			}
		}
		return value;
	}
	@Override
	public String[] getParameterValues(String name) {
		String[] values = super.getParameterValues(name);
		if (values != null && values.length > 0) {
			ArrayUtil.foreach(values, (v, i) -> {
				if (StringUtils.isNotBlank(v)) {
					values[i] = HTMLFilterStrategy.getBaesHtmlFilter().filter(v);
				}
				return true;
			});
		}
		return values;
	}

	/**
	 * 覆盖getHeader方法，将参数名和参数值都做xss过滤。<br/>
	 * 如果需要获得原始的值，则通过super.getHeaders(name)来获取<br/>
	 * getHeaderNames 也可能需要覆盖
	 */
	@Override
	public String getHeader(String name) {
		String value = super.getHeader(name);
		if (value != null) {
			value = HTMLFilterStrategy.getBaesHtmlFilter().filter(value);
			;
		}
		return value;
	}

}