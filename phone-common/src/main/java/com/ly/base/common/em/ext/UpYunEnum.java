package com.ly.base.common.em.ext;

import com.ly.base.common.em.EnumSuper;
import com.ly.base.common.system.UpYunConfig;

/**
 * 
 * @author TangFangguo
 * @date 2016年7月7日
 * @Description 又拍云上传文件枚举类
 */
public enum UpYunEnum implements EnumSuper{
	
	carImg(UpYunConfig.CAR_IMG_FOLDER,FileTypeEnum.Img.getValue(),"新车图片的上传保存目录"),
	
	oldCarImg(UpYunConfig.CAR_OLD_IMG_FOLDER,FileTypeEnum.Img.getValue(),"新车图片的上传保存目录 "),
	
	partsImg(UpYunConfig.PARTS_IMG_FOLDER,FileTypeEnum.Img.getValue(),"精品配件图片的上传保存目录 "),
	
	img(UpYunConfig.ORTHER_IMG,FileTypeEnum.Img.getValue(),"其他图片上传保存目录 "),
	
	carHtml(UpYunConfig.CAR_DETAIL_HTML_FOLDER,FileTypeEnum.Html.getValue(),"新车配置详情保存目录"),
	
	partsHtml(UpYunConfig.PARTS_DETAIL_HTML_FOLDER,FileTypeEnum.Html.getValue(),"汽车配件详情保存目录"),
	
	actHtml(UpYunConfig.ACT_DETAIL_HTML_FOLDER,FileTypeEnum.Html.getValue(),"活动内容保存目录"),
	;
	
	private String folder;//目录
	private String invilidType;//允许类型
	private String discription;//描述

	UpYunEnum(String folder,String invilidType , String discription) {
		this.folder = folder;
		this.invilidType=invilidType;
		this.discription = discription;
	}

	public String getFolder() {
		return folder;
	}

	public String getInvilidType() {
		return invilidType;
	}

	public String getDiscription() {
		return discription;
	}

	@Override
	public String getValue() {
		return folder;
	}

	
}
