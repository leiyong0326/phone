package com.ly.base.common.em.ext.security;

import com.ly.base.common.em.EnumSuper;

public enum AdminSecurityEnum implements EnumSuper {
	//商品
	GoodsColor("goodsColor","手机颜色"),
	GoodsCombo("goodsCombo","0402"),
	GoodsComboLevel("goodsComboLevel","套餐级别"),
	GoodsPhone("goodsPhone","0401"),
	GoodsType("goodsType","0403"),
	//日志
	LogCombo("logCombo","0204"),
	LogDrawMoney("logDrawMoney","0205"),
	LogLogin("logLogin","0201"),
	LogOperater("logOperater","0202"),
	LogPhone("logPhone","0203"),
	//系统
	SysAccount("sysAccount","0104"),
	SysMenu("sysMenu","0101"),
	SysRole("sysRole","0103"),
	SysUser("sysUser","0102");

	private String value;
	private String discription;

	AdminSecurityEnum(String value, String discription) {
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
