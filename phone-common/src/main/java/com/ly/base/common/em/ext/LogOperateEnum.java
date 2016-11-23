package com.ly.base.common.em.ext;

import com.ly.base.common.em.EnumSuper;

public enum LogOperateEnum implements EnumSuper{
	Select("0","查询"),Save("1","新增"),Update("2","修改"),Delete("3","删除");
	private String value;
	private String discription;
	
	LogOperateEnum(String value, String discription) {
		this.value = value;
		this.discription = discription;
	}

	@Override
	public String getValue() {
		return value;
	}

	@Override
	public String getDiscription() {
		return discription;
	}

	
}
