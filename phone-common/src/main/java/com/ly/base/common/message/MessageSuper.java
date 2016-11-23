package com.ly.base.common.message;

import com.ly.base.common.model.Json;
import com.ly.base.common.model.message.MessageTemplateData;

/**
 * 消息基类
 * @author LeiYong
 *
 */
public abstract class MessageSuper {
	public abstract Json sendMessage(String tarNum,String msg);
	public abstract Json sendTemplateMessage(String accessToken,MessageTemplateData temp);
}
