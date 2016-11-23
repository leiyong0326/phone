package com.ly.base.common.model.fileupload.ext;

import com.ly.base.common.model.fileupload.FileUploadData;

public class FileUploadDataImage extends FileUploadData {
	private boolean compress=false,blurring=false,gp=true;//是否压缩,是否模糊化,是否等比压缩
	private int tarWidth=1024,tarHeight=768;

	public boolean isCompress() {
		return compress;
	}

	public void setCompress(boolean compress) {
		this.compress = compress;
	}

	public boolean isBlurring() {
		return blurring;
	}

	public void setBlurring(boolean blurring) {
		this.blurring = blurring;
	}

	public boolean isGp() {
		return gp;
	}

	public void setGp(boolean gp) {
		this.gp = gp;
	}

	public int getTarWidth() {
		return tarWidth;
	}

	public void setTarWidth(int tarWidth) {
		this.tarWidth = tarWidth;
	}

	public int getTarHeight() {
		return tarHeight;
	}

	public void setTarHeight(int tarHeight) {
		this.tarHeight = tarHeight;
	}
	
}
