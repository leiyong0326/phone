package com.ly.base.common.util;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

public class JuheBrandUtil {

	private static final Logger log = LoggerFactory.getLogger(JuheBrandUtil.class);

	public static final String DEF_CHATSET = "UTF-8";
	public static final int DEF_CONN_TIMEOUT = 30000;
	public static final int DEF_READ_TIMEOUT = 30000;
	public static String userAgent = "Mozilla/5.0 (Windows NT 6.1) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/29.0.1547.66 Safari/537.36";

	public static String[] key = { 
			"d55a990a2bdf5cd8ed0f8f327186171b", 
			"876aa23ca8c12ec9f88cfae76b1a27f9",
			"c163f6286177f36377c90c709ea99455" };

	private static String JUHE_CAR_BRAND_URL = "http://op.juhe.cn/onebox/car/brand";

	
	public static void main(String[] args) {
//		System.out.println(getCarClassByBrandName("宝马", ""));
	}
	
	@SuppressWarnings("unchecked")
	public static String getCarClassByBrandName(String brandName,String appKey) {
		String result = "";
		Map params = new HashMap();// 请求参数
		params.put("key", appKey);// 应用APPKEY(应用详细页查询)
		params.put("brand", brandName);// 车系名称，如:奥迪
		try {
			String resultStr = net(JUHE_CAR_BRAND_URL, params, "GET");
			JSONObject jsonObject = JSONObject.parseObject(resultStr);
			if (jsonObject.getIntValue("error_code") == 0) {
				result = jsonObject.getString("result");
				System.out.println(result);
				return result;
			} else {
				System.out.println("_____________error_code="+jsonObject.getString("error_code"));
				return "error_code="+jsonObject.getString("error_code");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		//result="[{\"car\": \"华晨宝马\",         \"type\": \"宝马3系\"     },     {         \"car\": \"华晨宝马\",         \"type\": \"宝马5系\"     },     {         \"car\": \"华晨宝马\",         \"type\": \"宝马X1\"     },     {         \"car\": \"宝马(进口)\",         \"type\": \"宝马i3\"     },     {         \"car\": \"宝马(进口)\",         \"type\": \"宝马1系\"     },     {         \"car\": \"宝马(进口)\",         \"type\": \"宝马2系Active Tourer\"     },     {         \"car\": \"宝马(进口)\",         \"type\": \"宝马3系(进口)\"     },     {         \"car\": \"宝马(进口)\",         \"type\": \"宝马3系GT\"     },     {         \"car\": \"宝马(进口)\",         \"type\": \"宝马4系\"     },     {         \"car\": \"宝马(进口)\",         \"type\": \"宝马5系(进口)\"     },     {         \"car\": \"宝马(进口)\",         \"type\": \"宝马5系GT\"     },     {         \"car\": \"宝马(进口)\",         \"type\": \"宝马6系\"     },     {         \"car\": \"宝马(进口)\",         \"type\": \"宝马7系\"     },     {         \"car\": \"宝马(进口)\",         \"type\": \"宝马X3\"     },     {         \"car\": \"宝马(进口)\",         \"type\": \"宝马X4\"     },     {         \"car\": \"宝马(进口)\",         \"type\": \"宝马X5\"     },     {         \"car\": \"宝马(进口)\",         \"type\": \"宝马X6\"     },     {         \"car\": \"宝马(进口)\",         \"type\": \"宝马2系\"     },     {         \"car\": \"宝马(进口)\",         \"type\": \"宝马i8\"     },     {         \"car\": \"宝马(进口)\",         \"type\": \"宝马Z4\"     },     {         \"car\": \"宝马(进口)\",         \"type\": \"Active Tourer\"     },     {         \"car\": \"宝马(进口)\",         \"type\": \"Vision Future Luxury\"     },     {         \"car\": \"宝马(进口)\",         \"type\": \"宝马X1(进口)\"     },     {         \"car\": \"宝马(进口)\",         \"type\": \"Gran Lusso\"     },     {         \"car\": \"宝马(进口)\",         \"type\": \"Zagato Coupe\"     },     {         \"car\": \"宝马(进口)\",         \"type\": \"Isetta\"     },     {         \"car\": \"宝马(进口)\",         \"type\": \"ConnectedDrive\"     },     {         \"car\": \"宝马(进口)\",         \"type\": \"EfficientDynamics\"     },     {         \"car\": \"宝马(进口)\",         \"type\": \"宝马8系\"     },     {         \"car\": \"宝马(进口)\",         \"type\": \"宝马Z8\"     },     {         \"car\": \"宝马M\",         \"type\": \"宝马M3\"     },     {         \"car\": \"宝马M\",         \"type\": \"宝马M4\"     },     {         \"car\": \"宝马M\",         \"type\": \"宝马M5\"     },     {         \"car\": \"宝马M\",         \"type\": \"宝马M6\"     },     {         \"car\": \"宝马M\",         \"type\": \"宝马X5 M\"     },     {         \"car\": \"宝马M\",         \"type\": \"宝马X6 M\"     },     {         \"car\": \"宝马M\",         \"type\": \"宝马1系M\"     },     {         \"car\": \"宝马M\",         \"type\": \"宝马M1\"     } ]";
		
		return result;
	}

	/**
	 *
	 * @param strUrl
	 *            请求地址
	 * @param params
	 *            请求参数
	 * @param method
	 *            请求方法
	 * @return 网络请求字符串
	 * @throws Exception
	 */
	public static String net(String strUrl, Map params, String method) throws Exception {
		HttpURLConnection conn = null;
		BufferedReader reader = null;
		String rs = null;
		try {
			StringBuffer sb = new StringBuffer();
			if (method == null || method.equals("GET")) {
				strUrl = strUrl + "?" + urlencode(params);
			}
			URL url = new URL(strUrl);
			conn = (HttpURLConnection) url.openConnection();
			if (method == null || method.equals("GET")) {
				conn.setRequestMethod("GET");
			} else {
				conn.setRequestMethod("POST");
				conn.setDoOutput(true);
			}
			conn.setRequestProperty("User-agent", userAgent);
			conn.setUseCaches(false);
			conn.setConnectTimeout(DEF_CONN_TIMEOUT);
			conn.setReadTimeout(DEF_READ_TIMEOUT);
			conn.setInstanceFollowRedirects(false);
			conn.connect();
			if (params != null && method.equals("POST")) {
				try {
					DataOutputStream out = new DataOutputStream(conn.getOutputStream());
					out.writeBytes(urlencode(params));
				} catch (Exception e) {
					// TODO: handle exception
				}
			}
			InputStream is = conn.getInputStream();
			reader = new BufferedReader(new InputStreamReader(is, DEF_CHATSET));
			String strRead = null;
			while ((strRead = reader.readLine()) != null) {
				sb.append(strRead);
			}
			rs = sb.toString();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (reader != null) {
				reader.close();
			}
			if (conn != null) {
				conn.disconnect();
			}
		}
		return rs;
	}

	// 将map型转为请求参数型
	public static String urlencode(Map<String, Object> data) {
		StringBuilder sb = new StringBuilder();
		for (Map.Entry i : data.entrySet()) {
			try {
				sb.append(i.getKey()).append("=").append(URLEncoder.encode(i.getValue() + "", "UTF-8")).append("&");
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
		}
		return sb.toString();
	}
	
}