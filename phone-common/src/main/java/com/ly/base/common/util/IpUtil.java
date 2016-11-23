package com.ly.base.common.util;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.ly.base.common.model.AddressBean;
import com.ly.base.common.system.SystemConfig;

/**
 * 根据IP地址获取详细的地域信息
 * 
 * @author LeiYong
 */
public class IpUtil {
	private static String serviceUrl = "http://ip.taobao.com/service/getIpInfo.php";

	/**
	 * 通过ip地址获取用户的地区信息
	 * 
	 * @param encoding
	 *            服务器端请求编码。如GBK,UTF-8等
	 * @param parameter
	 *            请求的参数 格式为：name=xxx&pwd=xxx
	 * @return
	 */
	public static AddressBean getAddress(String encodingString, String ip) {
		AddressBean bean = null;
		String returnStr = HttpUtil.sendToConnection(serviceUrl, "POST", SystemConfig.CHARSET, "ip=" + ip);
		if (returnStr != null) {
			// 处理返回的省市区信息
			JSONObject jo = JSON.parseObject(returnStr);
			// code为0时成功
			if (jo!=null&&jo.getInteger("code") != null && jo.getInteger("code")!=null&&jo.getInteger("code").intValue() == 0) {
				JSONObject data = jo.getJSONObject("data");
				if (data != null) {
					bean = JSON.toJavaObject(data, AddressBean.class);
				}
			}
		}
		return bean;
	}

	/**
	 * 通过ip地址获取用户的地区信息
	 * 
	 * @param ip
	 * @return
	 */
	public static AddressBean getAddress(String ip) {
		return getAddress(SystemConfig.CHARSET, ip);
	}

	/**
	 * 获取ip地址
	 * @param request
	 * @return
	 */
	public static String getIp(HttpServletRequest request) {
		String ip = request.getHeader("x-forwarded-for");
	    if(ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)){
	        ip = request.getHeader("Proxy-Client-IP");
	    }
	    if(ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)){
	        ip = request.getHeader("WL-Proxy-Client-IP");
	    }
	    if(ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)){
	        ip = request.getRemoteAddr();
	    }
	    if (ip==null) {
			return "192.168.0.255";//未获取到ip,返回虚拟ip地址,避免执行淘宝库查询ip信息
		}
	    return ip.equals("0:0:0:0:0:0:0:1")?"192.168.0.0":ip;
	}
	/**
	 * 获取ip地址
	 * @param request
	 * @return ["os","browser"]
	 */
	public static String[] getOsAndBrowser(HttpServletRequest request) {
		String[] res = {null,null};
		String agent = request.getHeader("User-Agent");
		if (StringUtils.isNotBlank(agent)) {
			String[] result = agent.replaceAll("^.+?\\((.+?)\\).+\\(.+\\)\\s(.+)$", "$1#$2").split("#");
			if (result.length==2) {
				res = result;
			}
		}
	    return res;
	}
}
