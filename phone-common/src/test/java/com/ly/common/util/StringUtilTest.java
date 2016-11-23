package com.ly.common.util;

import static org.junit.Assert.*;

import org.junit.Test;

import com.ly.base.common.util.StringUtil;

public class StringUtilTest {

	@Test
	public void testIsInteger() {
		assertTrue(StringUtil.isInteger("1231231034"));
		assertFalse(StringUtil.isInteger("1231231034.123"));
		assertFalse(StringUtil.isInteger("1231231034a"));
		assertFalse(StringUtil.isInteger("a1231231034"));
	}

	@Test
	public void testIsDouble() {
		assertTrue(StringUtil.isDouble("1231231034"));
		assertTrue(StringUtil.isDouble("1231231034.123"));
		assertFalse(StringUtil.isDouble("1231231034a"));
		assertFalse(StringUtil.isDouble("a1231231034"));
	}

	@Test
	public void testIsSafe() {
		assertTrue(StringUtil.isSafe("adsm_132"));
		assertTrue(StringUtil.isSafe("ssa"));
		assertFalse(StringUtil.isSafe("123_1231034a"));
		assertFalse(StringUtil.isSafe("a1231231*034"));
	}

	@Test
	public void testIsEmail() {
		assertTrue(StringUtil.isEmail("adsm_132@123.com"));
		assertTrue(StringUtil.isEmail("asd.-+@163.com"));
		assertFalse(StringUtil.isEmail("123_1231034a@163."));
		assertFalse(StringUtil.isEmail("a1231231*034@.com"));
	}

	@Test
	public void testIsChinese() {
		assertTrue(StringUtil.isChinese("骉"));
		assertTrue(StringUtil.isChinese("戊二醛"));
		assertFalse(StringUtil.isChinese("fasd1231"));
		assertFalse(StringUtil.isChinese("as放松"));
	}

	@Test
	public void testIsPhoneNumber() {
		assertTrue(StringUtil.isPhoneNumber("17704027977"));
		assertTrue(StringUtil.isPhoneNumber("12345678901"));
		assertFalse(StringUtil.isPhoneNumber("22133123122"));
		assertFalse(StringUtil.isPhoneNumber("12312321"));
	}

	@Test
	public void testGetUUID() {
		System.out.println("UUID:"+StringUtil.getUUID());
	}

	@Test
	public void testGetNonceStr() {
		System.out.println("NonceStr(15):"+StringUtil.getNonceStr(15));
	}

	@Test
	public void testGetHexString() {
		System.out.println("HexString(255,4):"+StringUtil.getHexString(255, 4));
		System.out.println("HexString(255,3):"+StringUtil.getHexString(255, 3));
		System.out.println("HexString(255,2):"+StringUtil.getHexString(255, 2));
		System.out.println("HexString(255,1):"+StringUtil.getHexString(255, 1));
	}

	@Test
	public void testFullStringBefore() {
		System.out.println("fullStringBefore(\"abcdefg\", '0', 0):"+StringUtil.fullStringBefore("abcdefg", '0', 0));
		System.out.println("fullStringBefore(\"abcdefg\", '0', 3):"+StringUtil.fullStringBefore("abcdefg", '0', 3));
		System.out.println("fullStringBefore(\"abcdefg\", '0', 10):"+StringUtil.fullStringBefore("abcdefg", '0', 10));
	}

	@Test
	public void testGetNString() {
		System.out.println("getNString('c',9):"+StringUtil.getNString('c',9));
		System.out.println("getNString('c',0):"+StringUtil.getNString('c',0));
	}

	@Test
	public void testIndexOf() {
		String[] res = StringUtil.indexOf("abcdabcdabcd", "cd", 3);
		System.out.println(res[0]+":"+res[1]);
		assertArrayEquals(res, new String[]{"dab","abcd"});
		String[] res2 = StringUtil.indexOf("abcdabcdabcd", "ef", 3);
		assertNull(res2);
		String[] res3 = StringUtil.indexOf("abcdabcdabcd", "cd", 0);
		assertArrayEquals(res3, new String[]{"ab","abcdabcd"});
	}

}
