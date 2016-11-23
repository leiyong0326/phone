package com.ly.base.common.model;

import com.ly.base.common.util.MD5Util;
import com.ly.base.common.util.StringUtil;
/**
 * Token生成校验类
 * @author LeiYong
 *
 */
public class TokenEntity {
	private String[] tokens;
	public TokenEntity() {
		this(3);
	}
	
	public TokenEntity(int tokenCount) {
		this.tokens = new String[tokenCount];
	}

	/**
	 * 验证token
	 * @param token
	 * @return
	 */
	public boolean checkTooken(String token){
		token =  MD5Util.decryptHex(token);
		for (int i = 0; i < tokens.length; i++) {
			if (tokens[i]!=null&&tokens[i].equals(token)) {
				tokens[i] = null;
				return true;
			}
		}
		return false;
	}
	/**
	 * 新增一条token并将token返回
	 * @return
	 */
	public String putToken(){
		String token = StringUtil.getNonceStr(5);
		for (int i = 0; i < tokens.length; i++) {
			if (tokens[i]==null) {
				tokens[i] = token;
				return MD5Util.encryptHex(token);
			}
		}
		//如果token列表无空token,则将token追加到最后一位
		for (int j = 1; j < tokens.length; j++) {
			tokens[j-1]=tokens[j];
		}
		tokens[tokens.length-1] = token;
		return MD5Util.encryptHex(token);
	}
}
