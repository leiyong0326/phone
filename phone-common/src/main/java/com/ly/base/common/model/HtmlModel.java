package com.ly.base.common.model;

import java.util.Date;

public class HtmlModel {

	// 活动ID
	public String actPk;
	// 标题
	public String title;
	// 内容
	public String content;
	// 报名费
	public String price;
	// 开始时间
	public Date startTime;
	// 结束时间
	public Date endTime;

	public String getActPk() {
		return actPk;
	}

	public void setActPk(String actPk) {
		this.actPk = actPk;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getPrice() {
		return price;
	}

	public void setPrice(String price) {
		this.price = price;
	}

	public Date getStartTime() {
		return startTime;
	}

	public void setStartTime(Date startTime) {
		this.startTime = startTime;
	}

	public Date getEndTime() {
		return endTime;
	}

	public void setEndTime(Date endTime) {
		this.endTime = endTime;
	}
}
