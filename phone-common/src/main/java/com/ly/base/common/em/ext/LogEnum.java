package com.ly.base.common.em.ext;

import com.ly.base.common.em.EnumSuper;

public enum LogEnum implements EnumSuper{
	GoodsColor("商品","手机颜色"),GoodsCombo("商品","套餐"),GoodsComboLevel("商品","套餐级别"),GoodsPhone("商品","手机"),GoodsType("商品","分类"),
	LogCombo("日志","套餐销售日志"),LogDrawMoney("日志","提现日志"),LogLogin("日志","登录日志"),LogOperater("日志","操作日志"),LogPhone("日志","手机销售日志"),
	SysAccount("系统","会员级别"),SysMenu("系统","菜单"),SysRole("系统","角色"),SysUser("系统","用户");
	private String value;
	private String discription;
	
	LogEnum(String value, String discription) {
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
