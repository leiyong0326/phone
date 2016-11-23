package com.ly.base.common.em.ext;

import com.ly.base.common.em.EnumSuper;

public enum FileTypeEnum implements EnumSuper{
	Img("jpg,jepg,png,gif,bmp","图片"),Excel("xls,xlsx","Excel"),
	Word("doc,docx","Word"),Xml("xml","Excel"),Properties("properties","Properties"),
	Jar("jar","Jar"),
	Html("html","html");
	private String value;
	private String discription;
	
	FileTypeEnum(String value,String discription) {
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
