package com.ly.base.common.model.fileupload;
/**
 * 上传文件基本信息
 * @author LeiYong
 *
 */
public class FileUploadData {
	private byte[] srcFile;
	private String tarFileName;
	public byte[] getSrcFile() {
		return srcFile;
	}
	public void setSrcFile(byte[] srcFile) {
		this.srcFile = srcFile;
	}
	public String getTarFileName() {
		return tarFileName;
	}
	public void setTarFileName(String tarFileName) {
		this.tarFileName = tarFileName;
	}
	
}
