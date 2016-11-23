package com.ly.base.core.key;

import com.ly.base.common.em.ext.LogEnum;
/**
 * 定义主键生成策略接口,以便修改扩展
 * @author LeiYong
 *
 */
public interface KeyGenerate {
	/**
	 * 生成String类型主键
	 * @param em
	 * @return
	 */
	public String generateStringKey(LogEnum em);
	/**
	 * 生成long类型主键
	 * @param em
	 * @return
	 */
	public Long generateLongKey(LogEnum em);
}
