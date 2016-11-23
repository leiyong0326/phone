package com.ly.common.util;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.util.List;

import org.junit.Test;

import com.ly.base.common.model.Model;
import com.ly.base.common.util.MyBatisUtil;

public class MyBatisUtilTest {

	@Test
	public void testParseBaseStringArray() {
		System.out.println(MyBatisUtil.parseList("abc", "in", "123,345,123").toString());
	}


	@Test
	public void testParseListStringStringString() {
		List<Model> list = MyBatisUtil.parseBase("");
		assertEquals(list, "abcin[123, 345, 123]");
	}


}
