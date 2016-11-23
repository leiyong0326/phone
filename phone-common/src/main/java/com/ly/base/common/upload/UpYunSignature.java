package com.ly.base.common.upload;

public class UpYunSignature {

	private String path;

	private long expiration;

	private int file_blocks;

	private String file_hash;

	private long file_size;

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public long getExpiration() {
		return expiration;
	}

	public void setExpiration(long expiration) {
		this.expiration = expiration;
	}

	public int getFile_blocks() {
		return file_blocks;
	}

	public void setFile_blocks(int file_blocks) {
		this.file_blocks = file_blocks;
	}

	public String getFile_hash() {
		return file_hash;
	}

	public void setFile_hash(String file_hash) {
		this.file_hash = file_hash;
	}

	public long getFile_size() {
		return file_size;
	}

	public void setFile_size(long file_size) {
		this.file_size = file_size;
	}

}
