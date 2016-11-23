package com.ly.base.common.upload;

import java.io.IOException;
import java.util.Date;

import org.apache.commons.lang3.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import com.ly.base.common.em.ext.FileTypeEnum;
import com.ly.base.common.system.UpYunConfig;
import com.ly.base.common.util.MD5Util;
import com.ly.base.common.util.StringUtil;

public class UpYunUploadUtil {

	private static UpYunUploadUtil uploadUtil;

	public synchronized static UpYunUploadUtil getInstance() {
		if (uploadUtil == null) {
			uploadUtil = new UpYunUploadUtil();
		}
		return uploadUtil;
	}

	/**
	 * 获取upyun上传文件的token 返回给页面
	 * @param simpleCode 机构简码
	 * @param path 文件存放路径
	 * @param extendName 文件后缀名 
	 * @param fileSize 文件大小
	 * @param fileMD5 文件md5
	 * @return
	 */
	public UpYunToken getUpYunToken(String simpleCode,String path, String extendName, long fileSize, String fileMD5) {
		// token授权有效期限
		long expiration = (new Date().getTime() + UpYunConfig.DEFAULT_EXPIRATION) / 1000;
		//上传到upyun后的文件名称包括后缀名
		String fileName = expiration + StringUtil.getNonceStr(6) + "." + extendName;
		//文件存放路径=/机构简码/文件存放路径/
		path = UpYunConfig.UPYUN_DIR_SEPARATOR
				.concat(simpleCode)
				.concat(UpYunConfig.UPYUN_DIR_SEPARATOR)
				.concat(path)
				.concat(UpYunConfig.UPYUN_DIR_SEPARATOR);
		//创建目录
		UpYun.getInstance().mkDir(path, true);
		
		path=path.concat(fileName);
		StringBuffer  signature=new StringBuffer("expiration");
		signature.append(expiration)
			.append("file_blocks").append(UpYunConfig.DEFAULT_FILE_BLOCKS)
			.append("file_hash") .append(fileMD5)
			.append("file_size").append(fileSize)
			.append("path").append(path)
			.append(UpYunConfig.UPYUN_FORM_API_SECRET);
		
		String md5=MD5Util.md5(signature.toString());
		
		
		UpYunToken token=new UpYunToken();
		token.setBucket(UpYunConfig.UPYUN_PIC_BUCKET_NAME);
		token.setExpiration(expiration);
		token.setSignature(md5);
		token.setPath(path);
		token.setAccessPrefix(UpYunConfig.FILE_ACCESS_URL);
		return token;
	}

	public String uploadFileToUpyun(MultipartFile Img, String folderName,String extendName, String picRemarkCode) {
		String address = "";
		String errorMsg = "";

		String dirRoot = "/";
		// 创建文件夹
		// TO-do 图片名称在后面加标识符
		String newFileName = String.valueOf(System.currentTimeMillis()) + picRemarkCode + "." + extendName;
		// 传到upyun后的文件路径
		String filePath = dirRoot + folderName + dirRoot + newFileName;

		try {
			// upyun.setContentMD5(upyun.md5(picFile));
			// 文件密钥
			UpYun.getInstance().setFileSecret(UpYunConfig.FILE_SECRET);
			byte[] imgData = Img.getBytes();
			boolean result = UpYun.getInstance().writeFile(filePath, imgData, true);
			if (result = true) {
				// ！ 是upyun制定的文件识别分隔符，可以去upyun服务器去修改
				address = UpYunConfig.FILE_ACCESS_URL + filePath + "!" + UpYunConfig.FILE_SECRET;
				System.out.println(address + "--------" + result);
			}
		} catch (IOException e) {
			errorMsg = "上传图片到upyun失败";
			e.printStackTrace();
		}
		return (StringUtils.isBlank(errorMsg)) ? address : errorMsg;
	}

	public String verifyImg(MultipartFile communityImg) {
		// 校验后返回的结果，包含errorMsg以及后缀名extendName
		String errorMsg = "";
		String extendName = "";

		String fileName = communityImg.getOriginalFilename();
		// 获取拓展名,并转化为小写
		extendName = fileName.substring(fileName.lastIndexOf(".") + 1, fileName.length()).toLowerCase();

		if (communityImg.getSize() > UpYunConfig.UPLOAD_IMG_SIZE) {
			errorMsg = "图片大小超过2M，请重新上传";
			return errorMsg;
		} else if (!FileTypeEnum.Img.getValue().contains(extendName)) {
			errorMsg = "图片类型非jpg或jpeg或png";
		}

		return StringUtils.isBlank(errorMsg) ? extendName : errorMsg;
	}
	
	
	/**
	 * 
	 * @param htmlContent 页面内容
	 * @param url  原url地址
	 * @param htmlFloder 保存目录
	 * @return
	 */
	public String uploadHtml(String htmlContent,String url,String htmlFloder){
		String address = "";
		String dirRoot = "/";
		// 创建文件夹
		// TO-do 图片名称在后面加标识符
		// 传到upyun后的文件路径
		String filePath="";
		if (StringUtils.isBlank(url)) {
			filePath = dirRoot.concat(htmlFloder) .concat(dirRoot).concat(StringUtil.getNonceStr(6)+System.currentTimeMillis()).concat(".html");
		}else{
			filePath=url.replace(UpYunConfig.FILE_ACCESS_URL, "");
		}
		
		// upyun.setContentMD5(upyun.md5(picFile));
		
		byte[] imgData = htmlContent.getBytes();
		boolean result = UpYun.getInstance().writeFile(filePath, imgData, true);
		if (result = true) {
			// ！ 是upyun制定的文件识别分隔符，可以去upyun服务器去修改
			address = UpYunConfig.FILE_ACCESS_URL + filePath;
			System.out.println(address + "--------" + result);
		}
		
		return address;
	}
	
	

}
