package com.ly.base.common.upload;

import java.io.Serializable;

@SuppressWarnings("serial")
public class UpYunToken implements Serializable{
	
	private String bucket;
	
	private long expiration;
	
	private String signature;
	
	private String path;
	
	private String accessPrefix;

	public String getBucket() {
		return bucket;
	}

	public void setBucket(String bucket) {
		this.bucket = bucket;
	}

	public long getExpiration() {
		return expiration;
	}

	public void setExpiration(long expiration) {
		this.expiration = expiration;
	}

	public String getSignature() {
		return signature;
	}

	public void setSignature(String signature) {
		this.signature = signature;
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public String getAccessPrefix() {
		return accessPrefix;
	}

	public void setAccessPrefix(String accessPrefix) {
		this.accessPrefix = accessPrefix;
	}
	

}
