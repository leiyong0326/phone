package com.ly.base.common.util;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
/**
 * Http请求工具类
 * @author LeiYong
 *
 */
public class HttpUtil {
	/**
	 * 获取HttpURLConnection连接
	 * 
	 * @param serviceUrl
	 * @param requestMethod
	 * @return
	 */
	private static HttpURLConnection getHttpURLConnection(String serviceUrl, String requestMethod) {
		HttpURLConnection connection = null;
		try {
			URL url = new URL(serviceUrl);
			connection = (HttpURLConnection) url.openConnection();// 新建连接实例
			connection.setConnectTimeout(30000);// 设置连接超时时间，单位毫秒
			connection.setReadTimeout(30000);// 设置读取数据超时时间，单位毫秒
			connection.setDoOutput(true);// 是否打开输出流 true|false
			connection.setDoInput(true);// 是否打开输入流true|false
			connection.setRequestMethod(requestMethod);// 提交方法POST|GET
			connection.setUseCaches(false);// 是否缓存true|false
		} catch (IOException e) {
			e.printStackTrace();
		}
		return connection;

	}

	/**
	 * 向目标地址发送数据
	 * 
	 * @param serviceUrl
	 *            请求的地址
	 * @param requestMethod
	 *            请求方式,POST/GET
	 * @param encode
	 *            字符编码
	 * @param parameter
	 *            请求参数user=abc&pwd=abc
	 * @return
	 */
	public static String sendToConnection(String serviceUrl, String requestMethod, String encode, String parameter) {
		StringBuffer sb = new StringBuffer();
		HttpURLConnection connection = getHttpURLConnection(serviceUrl, requestMethod);
		if (connection != null) {
			try {
				connection.connect();// 打开连接端口
				DataOutputStream out = new DataOutputStream(connection.getOutputStream());// 打开输出流往对端服务器写数据
				if (StringUtils.isNotBlank(parameter)) {
					out.write(parameter.getBytes());// 写数据,也就是提交你的表单 name=xxx&pwd=xxx
				}
				out.flush();// 刷新
				out.close();// 关闭输出流
				String line = null;
				BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream(), encode));// 往对端写完数据对端服务器返回数据
				while ((line = reader.readLine()) != null) {
					sb.append(line);
				}
				reader.close();
				connection.disconnect();
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			} finally {
				connection.disconnect();
			}
		}
		return sb.toString();
	}

	/**
	 * 使用Get方式获取数据
	 * 
	 * @param url
	 *            URL包括参数，http://HOST/XX?XX=XX&XXX=XXX
	 * @param charset
	 * @return
	 */
	public static String sendGet(String serviceUrl, String charset) {
		return sendToConnection(serviceUrl, "GET", charset, null);
	}

	/**
	 * POST请求，Map形式数据
	 * 
	 * @param url
	 *            请求地址
	 * @param param
	 *            MAP请求数据
	 * @param charset
	 *            编码方式
	 */
	public static String sendPost(String url, Map<String, String> param, String charset) {
		String stringParam = UrlUtil.parseToUrlParam(param);
		return sendPost(url, stringParam, charset);
	}

	/**
	 * POST请求，字符串形式数据
	 * 
	 * @param url
	 *            请求地址
	 * @param param
	 *            字符串请求数据
	 * @param charset
	 *            编码方式
	 */
	public static String sendPost(String url, String param, String charset) {
		return sendToConnection(url, "POST", charset, param);
	}
}
