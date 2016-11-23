package com.ly.base.common.upload.ext;

import com.ly.base.common.em.ext.FileUploadEnum;
import com.ly.base.common.em.ext.QiNiuEnum;
import com.ly.base.common.em.ext.UpYunEnum;
import com.ly.base.common.model.Json;
import com.ly.base.common.model.fileupload.FileUploadData;
import com.ly.base.common.system.SystemConfig;
import com.ly.base.common.upload.FileUploadSuper;
import com.ly.base.common.upload.UpYunToken;
import com.ly.base.common.upload.UpYunUploadUtil;
import com.ly.base.common.util.ReflectionUtil;
//import com.qiniu.util.StringMap;
import com.qiniu.util.StringMap;

public class FileUploadStrategy {
	private static final String QINIU_AUTH = "nR_4iiNEOk3fHuiPMiBQf4ubWLseJPr7WqNCQI1Z";
	// 简单上传，使用默认策略，只需要设置上传的空间名就可以了
	public synchronized static String getUpToken(QiNiuEnum qiNiuEnum) {
		return SystemConfig.QINIU_AUTH.uploadToken(qiNiuEnum.getValue(),null,300,new StringMap() .put("callbackUrl","http://your.domain.com/callback")
		          .put("callbackBody", "filename=$(fname)&filesize=$(fsize)"));
	}
	public synchronized static UpYunToken getUpYunUploadToken(String simpleCode,UpYunEnum upYunEnum,String extendName,long fileSize,String fileMD5){
		return UpYunUploadUtil.getInstance().getUpYunToken(simpleCode,upYunEnum.getValue(), extendName, fileSize, fileMD5);
	}
	
	/**
	 * 上传文件
	 * @param uploadEnum 上传类型
	 * @param srcFile 上传原文件
	 * @param tarFile 目标文件,需带后缀
	 * @param compress 是否压缩
	 * @param blurring 是否模糊化
	 * @return 成功则返回文件地址
	 */
	public static Json fileUpload(FileUploadEnum uploadEnum,FileUploadData fileUploadData){
		Class<?> clazz = ReflectionUtil.getClass(uploadEnum.getDiscription());
		if (clazz!=null) {
			FileUploadSuper fus = (FileUploadSuper) ReflectionUtil.getInstance(clazz);
			if (fus!=null) {
				return fus.uploadFile(fileUploadData);
			}
		}
		return null;
	}
}
