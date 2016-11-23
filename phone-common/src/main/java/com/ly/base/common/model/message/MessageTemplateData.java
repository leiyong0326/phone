package com.ly.base.common.model.message;

public abstract class MessageTemplateData {
	/**
	 * 模板消息id
	 */
	private String template_id;
	/**
	 * 用户openId
	 */
	private String touser;
	/**
	 * URL置空，则在发送后，点击模板消息会进入一个空白页面（ios），或无法点击（android）
	 */
	private String url;
	public String getTemplate_id() {
		return template_id;
	}
	public void setTemplate_id(String template_id) {
		this.template_id = template_id;
	}
	public String getTouser() {
		return touser;
	}
	public void setTouser(String touser) {
		this.touser = touser;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	
}
