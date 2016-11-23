package com.ly.base.common.util;

import java.net.URLEncoder;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;

import com.ly.base.common.system.SystemConfig;


/**
 * Url处理工具类
 * @author LeiYong
 *
 */
public class UrlUtil {
	/**
	 * 获取二级域名
	 * www.baidu.com返回www
	 * abc.def.ggg.com.cn返回abc.def
	 * abc.def.ggg.com返回abc.def
	 * @param url
	 * @return
	 */
	public static String getSecondUrl(String url){
		if (StringUtils.isNotBlank(url)) {
			if (url.contains("tl18178.oicp.net")) {
				return "bm";
			}
			if (url.contains("1551247bt3.iask.in")) {
				return "bm";
			}
			String nurl = url.replaceAll("^(.+?)(\\.[\\w-]+?\\.)(net|com|cn|org|com.cn)$", "$1");
			return nurl.equals(url)?"":nurl;
		}
		return "";
	}
	/**
	 * 解析Map为参数
	 * @param param
	 * @return
	 */
	public static String parseToUrlParam(Map<String, String> param){
		StringBuffer buffer = new StringBuffer();
		if (param != null && !param.isEmpty()) {
			ArrayUtil.foreach(param, (key,value)->{
				try {
					buffer.append(key).append("=").append(URLEncoder.encode(value,SystemConfig.CHARSET)).append("&");
					return true;
				} catch (Exception e) {
					return false;
				}
			});
			buffer.deleteCharAt(buffer.length() - 1);
		}
		return buffer.toString();
	}
}
