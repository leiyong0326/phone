package com.ly.base.common.upload.ext;

import org.apache.commons.lang3.StringUtils;

import com.ly.base.common.em.ext.FileTypeEnum;
import com.ly.base.common.model.Json;
import com.ly.base.common.model.fileupload.FileUploadData;
import com.ly.base.common.upload.FileUploadSuper;
import com.ly.base.common.util.FileUtil;

/**
 * 图片上传
 * @author LeiYong
 *
 */
public class FileUploadImage extends FileUploadSuper{
	private static FileUploadImage uploadImage;
	private static String uploadPath = "";//普通图位置
	private static String compressPath = "";//压缩图位置
	private static String blurringPath = "";//低品质图位置
	private FileUploadImage(){
	}

	public synchronized static FileUploadImage getInstance() {
		if (uploadImage == null) {
			uploadImage = new FileUploadImage();
		}
		return uploadImage;
	}
	public boolean compressImage(){
		return false;
	}
	@Override
	public Json uploadFile(FileUploadData fileUploadData) {
		Json json = new Json();
		if (fileUploadData.getSrcFile()==null||StringUtils.isEmpty(fileUploadData.getTarFileName())) {
			json.setSuccess(false);
			json.setMsg("网络异常或文件名为空");
			return json;
		}
		if (!FileUtil.checkFileType(fileUploadData.getTarFileName(), FileTypeEnum.Img)) {
			json.setSuccess(false);
			json.setMsg("文件类型错误");
			return json;
		}
//		// 原图存放位置
//		String filePath = realPath + "upload\\img\\temp\\";
//		// 压缩后保存目录
//		String compressPath = realPath + "upload\\img\\compress\\";
//		// 上传
//		if (!FileUtil.createFolder(filePath)) {
//			request.setAttribute("msg", "图片上传失败");
//		}else{
//			file.transferTo(targetFile);
//			ImageUtil.compressPic(filePath, compressPath, targetFile.getName(), targetFile.getName(), 200, 200, true);
//			//ImageUtil.resize(filePath+fileName, compressPath+fileName);
//			request.setAttribute("msg", "图片上传成功");
//		}
		return null;
	}
}
