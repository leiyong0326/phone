package com.ly.base.common.model.message.ext;

import java.util.Map;

import com.ly.base.common.model.message.MessageTemplateData;
import com.ly.base.common.model.message.TemplateData;

public class MessageTemplateDataWeChat extends MessageTemplateData{
	/**
	 * 标题颜色
	 */
	private String topcolor;
	/**
	 * 详细内容
	 */
	private Map<String, TemplateData> data;

	public String getTopcolor() {
		return topcolor;
	}

	public void setTopcolor(String topcolor) {
		this.topcolor = topcolor;
	}

	public Map<String, TemplateData> getData() {
		return data;
	}

	public void setData(Map<String, TemplateData> data) {
		this.data = data;
	}
}
