package com.ly.base.common.em.ext;

import com.ly.base.common.em.EnumSuper;

/**
 * 消费类型枚举
 * 
 * @author Tianlin
 *
 */
public enum ConsumeTypeEnum implements EnumSuper {
	weizhang("违章代办", "违章代办"), shangcheng("配件商城", "配件商城");
	private String value;
	private String discription;

	ConsumeTypeEnum(String value, String discription) {
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
