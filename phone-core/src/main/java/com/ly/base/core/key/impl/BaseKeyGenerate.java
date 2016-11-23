package com.ly.base.core.key.impl;

import com.ly.base.common.em.ext.LogEnum;
import com.ly.base.common.util.NumberUtil;
import com.ly.base.common.util.StringUtil;
import com.ly.base.core.key.KeyGenerate;

/**
 * 基础主键生成策略,采用随机数或UUID+随机数
 * @author LeiYong
 *
 */
public class BaseKeyGenerate implements KeyGenerate{
	@Override
	public String generateStringKey(LogEnum em) {
		return StringUtil.getUUID()+StringUtil.getNonceStr(6);
	}
	@Override
	public Long generateLongKey(LogEnum em) {
		return System.currentTimeMillis()*1000000+NumberUtil.random(6);
	}
}
