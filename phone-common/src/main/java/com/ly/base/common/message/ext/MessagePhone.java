package com.ly.base.common.message.ext;

import com.ly.base.common.message.MessageSuper;
import com.ly.base.common.model.Json;
import com.ly.base.common.model.message.MessageTemplateData;

/**
 * 手机消息推送
 * 
 * @author LeiYong
 *
 */
public class MessagePhone extends MessageSuper {
	private static MessagePhone messagePhone;

	public synchronized static MessagePhone getInstance(){
		if (messagePhone==null) {
			messagePhone = new MessagePhone();
		}
		return messagePhone;
	}
	public Json sendMessage(String tarNum,String msg){
		Json json = new Json();
		json.setSuccess(true);
		return json;
	}
	/**
	 * 发送微信模板消息
	 * @param accessToken
	 * @param temp
	 * @return
	 */
	public Json sendTemplateMessage(String accessToken,MessageTemplateData temp) {
		Json json = new Json();
//		String url = TEMPLATE_URL + accessToken;
//		WxTemplate temp = new WxTemplate();
//		temp.setUrl(detailsUrl);
//		temp.setTouser(toUser);
//		temp.setTopcolor("#000000");
//		temp.setTemplate_id(templateId);
//		temp.setData(data);
//		String dataJson = JSON.toJSONString(temp);
//		String ret = HttpUtil.sendPost(url, "POST", dataJson, SystemConfig.CHARSET);
//		JSONObject jsonObject = JSONObject.parseObject(ret);
//		if (null != jsonObject) {
//			if (0 != jsonObject.getIntValue("errcode")) {
//				json.setAll(false, "模板消息发送失败", null);
//			} else {
//				json.setAll(true, null, jsonObject);
//			}
//		}
		return json;
	}
}
