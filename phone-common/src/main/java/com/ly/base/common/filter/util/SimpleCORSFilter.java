package com.ly.base.common.filter.util;
import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.filter.OncePerRequestFilter;  

/**
 * 解决跨域问题使用的filter
 * 可通过修改origin修改跨域访问对象条件
	<filter>
		<filter-name>corsFilter</filter-name>
		<filter-class>com.ly.base.common.filter.util.SimpleCORSFilter</filter-class>
		<init-param>
			<param-name>origin</param-name>
			<param-value>*</param-value>
		</init-param>
	</filter>
	<filter-mapping>
		<filter-name>corsFilter</filter-name>
		<url-pattern>/*</url-pattern>
	</filter-mapping>
 * @author LeiYong
 *
 */
public class SimpleCORSFilter extends OncePerRequestFilter{  
	private String origin;
    @Override  
    public void destroy() {  
          
    }  
  
    @Override  
    public void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws IOException, ServletException {  
            HttpServletResponse res = (HttpServletResponse) response;  
            res.setHeader("Access-Control-Allow-Origin", origin);
            res.setHeader("Access-Control-Allow-Methods", "POST, GET, OPTIONS, DELETE");  
            res.setHeader("Access-Control-Max-Age", "3600");  
            res.setHeader("Access-Control-Allow-Headers", "x-requested-with");  
            filterChain.doFilter(request, res);  
          
    }  
  
	public void setOrigin(String origin) {
		this.origin = origin;
	}  
  
}  