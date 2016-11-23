package com.ly.base.common.em.ext;

import com.ly.base.common.em.EnumSuper;

/**
 * 支付方式枚举
 * @author LeiYong
 *
 */
public enum QiNiuEnum implements EnumSuper{
	Phone("phone","手机图片"),Combo("combo","其它文件路径");
	private String value;
	private String discription;
	
	QiNiuEnum(String value,String discription) {
		this.discription = discription;
		this.value = value;
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
