package com.ly.base.common.em.ext;

import com.ly.base.common.em.EnumSuper;
import com.ly.base.common.upload.ext.FileUploadExcel;
import com.ly.base.common.upload.ext.FileUploadImage;

/**
 * 文件上传类型枚举
 * @author LeiYong
 *
 */
public enum FileUploadEnum implements EnumSuper{
	Image("0",FileUploadImage.class.getName()),Excel("1",FileUploadExcel.class.getName());
	private String value;
	private String discription;
	
	FileUploadEnum(String value,String discription) {
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
