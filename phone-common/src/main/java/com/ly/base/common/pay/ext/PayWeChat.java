package com.ly.base.common.pay.ext;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Date;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.SortedMap;
import java.util.TreeMap;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.methods.StringRequestEntity;
import org.jdom.JDOMException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ly.base.common.model.Json;
import com.ly.base.common.model.WechatPayModel;
import com.ly.base.common.pay.PaySuper;
import com.ly.base.common.system.SystemConfig;
import com.ly.base.common.util.MD5Util;
import com.ly.base.common.util.StringUtil;
import com.ly.base.common.util.XMLUtil;

/**
 * 微信预支付统一下单 返回预付订单到页面调起JS支付
 * 
 * @author TianLin
 *
 */
public class PayWeChat extends PaySuper {
	private static final Logger logger = LoggerFactory.getLogger(PayWeChat.class);

	private static PayWeChat payWeChat;

	public synchronized static PayWeChat getInstance() {
		if (payWeChat == null) {
			payWeChat = new PayWeChat();
		}
		return payWeChat;
	}

	/** 获取预支付单号prepay_id */
	private static final String UNI_URL = "https://api.mch.weixin.qq.com/pay/unifiedorder";

	public Json wechatPay(WechatPayModel payModel) {
		Json json = new Json();

		SortedMap<Object, Object> parameters = new TreeMap<Object, Object>();
		/** 公众号APPID */
		parameters.put("appid", payModel.getAppId());
		/** 商户号 */
		parameters.put("mch_id", payModel.getMchId());
		/** 随机字符串 */
		parameters.put("nonce_str", StringUtil.getNonceStr(5));
		/** 商品名称 */
		parameters.put("body", payModel.getCommodityName());

		/** 订单号 */
		parameters.put("out_trade_no", payModel.getOrderNum());

		/** 订单金额以分为单位，只能为整数 */
		int total = (int) (payModel.getMoney() * 100);
		parameters.put("total_fee", total);

		/** 支付回调地址 */
		parameters.put("notify_url", payModel.getNotifyUrl());
		/** 支付方式为JSAPI支付 */
		parameters.put("trade_type", "JSAPI");
		/** 用户微信的openid，当trade_type为JSAPI的时候，该属性字段必须设置 */
		parameters.put("openid", payModel.getOpenId());

		/** MD5进行签名，必须为UTF-8编码，注意上面几个参数名称的大小写 */
		String sign = createSign(SystemConfig.CHARSET, parameters, payModel.getAppKey());
		parameters.put("sign", sign);

		/** 生成xml结构的数据，用于统一下单接口的请求 */
		String requestXML = getRequestXml(parameters);

		/** 开始请求统一下单接口，获取预支付prepay_id */
		HttpClient client = new HttpClient();
		PostMethod myPost = new PostMethod(UNI_URL);
		client.getParams().setSoTimeout(300 * 1000);
		String result = null;
		try {
			myPost.setRequestEntity(new StringRequestEntity(requestXML, "text/xml", SystemConfig.CHARSET));
			int statusCode = client.executeMethod(myPost);
			if (statusCode == HttpStatus.SC_OK) {
				BufferedInputStream bis = new BufferedInputStream(myPost.getResponseBodyAsStream());
				byte[] bytes = new byte[1024];
				ByteArrayOutputStream bos = new ByteArrayOutputStream();
				int count = 0;
				while ((count = bis.read(bytes)) != -1) {
					bos.write(bytes, 0, count);
				}
				byte[] strByte = bos.toByteArray();
				result = new String(strByte, 0, strByte.length, SystemConfig.CHARSET);
				bos.close();
				bis.close();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		/** 需要释放掉、关闭连接 */
		myPost.releaseConnection();
		client.getHttpConnectionManager().closeIdleConnections(0);

		try {
			/** 解析微信返回的信息，以Map形式存储便于取值 */
			@SuppressWarnings("unchecked")
			Map<String, String> map = XMLUtil.doXMLParse(result);

			SortedMap<Object, Object> params = new TreeMap<Object, Object>();
			params.put("appId", payModel.getAppId());
			params.put("timeStamp", "\"" + new Date().getTime() + "\"");
			params.put("nonceStr", StringUtil.getNonceStr(5));
			/**
			 * 获取预支付单号prepay_id后，需要将它参与签名。
			 * 微信支付最新接口中，要求package的值的固定格式为prepay_id=...
			 */
			params.put("package", "prepay_id=" + map.get("prepay_id"));

			/** 微信支付新版本签名算法使用MD5，不是SHA1 */
			params.put("signType", "MD5");
			/**
			 * 获取预支付prepay_id之后，需要再次进行签名，参与签名的参数有：appId、timeStamp、nonceStr、
			 * package、signType. 主意上面参数名称的大小写.
			 * 该签名用于前端js中WeixinJSBridge.invoke中的paySign的参数值
			 */
			String paySign = createSign(SystemConfig.CHARSET, params, payModel.getAppKey());
			params.put("paySign", paySign);

			/** 预支付单号，前端ajax回调获取。由于js中package为关键字，所以，这里使用packageValue作为key。 */
			params.put("packageValue", "prepay_id=" + map.get("prepay_id"));

			/** 获取用户的微信客户端版本号，用于前端支付之前进行版本判断，微信版本低于5.0无法使用微信支付 */
			String userAgent = payModel.getUserAgent();
			char agent = userAgent.charAt(userAgent.indexOf("MicroMessenger") + 15);
			params.put("agent", new String(new char[] { agent }));

			// String retJson = JSON.toJSONString(params);
			json.setAll(true, null, params);
			return json;

		} catch (JDOMException e) {
			logger.error("调用微信支付发起预支付失败：" + e);
			json.setAll(false, "支付失败", null);
			return json;
		} catch (IOException e) {
			logger.error("调用微信支付发起预支付失败：" + e);
			json.setAll(false, "支付失败", null);
			return json;
		}
	}

	/**
	 * sign签名
	 * 
	 * @param parameters
	 * @return
	 */
	public static String createSign(String characterEncoding, SortedMap<Object, Object> parameters, String appKey) {
		StringBuffer sb = new StringBuffer();
		Set<Entry<Object, Object>> es = parameters.entrySet();
		Iterator<Entry<Object, Object>> it = es.iterator();
		while (it.hasNext()) {
			Map.Entry<Object, Object> entry = (Map.Entry<Object, Object>) it.next();
			String k = (String) entry.getKey();
			Object v = entry.getValue();
			/** 如果参数为key或者sign，则不参与加密签名 */
			if (null != v && !"".equals(v) && !"sign".equals(k) && !"key".equals(k)) {
				sb.append(k + "=" + v + "&");
			}
		}
		/** 支付密钥必须参与加密，放在字符串最后面 */
		sb.append("key=" + appKey);
		/** 记得最后一定要转换为大写 */
		String sign = MD5Util.MD5Encode(sb.toString(), characterEncoding).toUpperCase();
		return sign;
	}

	/**
	 * 将请求参数转换为xml格式的string
	 * 
	 * @param parameters
	 * @return
	 */
	public static String getRequestXml(SortedMap<Object, Object> parameters) {
		StringBuffer sb = new StringBuffer();
		sb.append("<xml>");
		Set<Entry<Object, Object>> es = parameters.entrySet();
		Iterator<Entry<Object, Object>> it = es.iterator();
		while (it.hasNext()) {
			Map.Entry<Object, Object> entry = (Map.Entry<Object, Object>) it.next();
			String k = (String) entry.getKey();
			String v = entry.getValue() + "";
			if ("attach".equalsIgnoreCase(k) || "body".equalsIgnoreCase(k) || "sign".equalsIgnoreCase(k)) {
				sb.append("<" + k + ">" + "<![CDATA[" + v + "]]></" + k + ">");
			} else {
				sb.append("<" + k + ">" + v + "</" + k + ">");
			}
		}
		sb.append("</xml>");
		return sb.toString();
	}
}
