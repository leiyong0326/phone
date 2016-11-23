package com.ly.common.util;

import org.junit.Test;

import com.ly.base.common.util.NumberUtil;

public class NumberUtilTest {
	/**
	 * 获取一个随机整数
	 * @param length
	 * @return
	 */
	@Test
	public void random(){
		System.out.println(NumberUtil.random(10));;
		System.out.println(NumberUtil.random(100));;
		System.out.println(NumberUtil.random(1000));;
		System.out.println(NumberUtil.random(100000000));;
	}
}
