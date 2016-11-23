package com.ly.base.common.message.ext;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.ly.base.common.message.MessageSuper;
import com.ly.base.common.model.Json;
import com.ly.base.common.model.message.MessageTemplateData;
import com.ly.base.common.system.SystemConfig;
import com.ly.base.common.util.HttpUtil;

/**
 * 微信消息推送
 * 
 * @author LeiYong
 *
 */
public class MessageWeChat extends MessageSuper {
	private static final Logger logger = LoggerFactory.getLogger(MessageWeChat.class);
	private static MessageWeChat messageWeChat;

	public synchronized static MessageWeChat getInstance() {
		if (messageWeChat == null) {
			messageWeChat = new MessageWeChat();
		}
		return messageWeChat;
	}

	/** 发送模板消息接口 */
	private final String TEMPLATE_URL = "https://api.weixin.qq.com/cgi-bin/message/template/send?access_token=";

	/**
	 * 发送微信模板消息
	 * 
	 * @param accessToken
	 * @param temp
	 * @return
	 */
	public Json sendTemplateMessage(String accessToken, MessageTemplateData temp) {
		Json json = new Json();
		String url = TEMPLATE_URL + accessToken;
		// WxTemplate temp = new WxTemplate();
		// temp.setUrl(detailsUrl);
		// temp.setTouser(toUser);
		// temp.setTopcolor("#000000");
		// temp.setTemplate_id(templateId);
		// temp.setData(data);
		String dataJson = JSON.toJSONString(temp);
		String ret = HttpUtil.sendPost(url, dataJson, SystemConfig.CHARSET);
		logger.debug("微信模板发送结果：" + ret);
		JSONObject jsonObject = JSONObject.parseObject(ret);
		if (null != jsonObject) {
			if (0 != jsonObject.getIntValue("errcode")) {
				json.setAll(false, "模板消息发送失败", null);
			} else {
				json.setAll(true, null, jsonObject);
			}
		}
		return json;
	}

	@Override
	public Json sendMessage(String tarNum, String msg) {
		// TODO Auto-generated method stub
		return null;
	}
}
