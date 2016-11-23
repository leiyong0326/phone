package com.ly.base.common.upload;

import com.ly.base.common.model.Json;
import com.ly.base.common.model.fileupload.FileUploadData;

/**
 * 文件上传基类
 * 
 * @author LeiYong
 *
 */
public abstract class FileUploadSuper {
	/**
	 * 上传文件
	 */
	public abstract Json uploadFile(FileUploadData fileUploadData);
}
