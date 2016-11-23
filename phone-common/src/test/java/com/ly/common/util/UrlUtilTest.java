package com.ly.common.util;

import static org.junit.Assert.*;
import static com.ly.base.common.util.UrlUtil.*;

import org.junit.Test;

public class UrlUtilTest {

	@Test
	public void testGetSecondUrl() {
		assertEquals("workorder.console", getSecondUrl("workorder.console.aliyun.com"));
		assertEquals("workorder.console", getSecondUrl("workorder.console.aliyun.com"));
		assertEquals("www", getSecondUrl("www.blogjava.net"));
		assertEquals("user.qzone", getSecondUrl("user.qzone.qq.com"));
		assertEquals("mail", getSecondUrl("mail.qq.com"));
		assertEquals("mail.qq", getSecondUrl("mail.qq.con.com"));
		assertEquals("mail.qq", getSecondUrl("mail.qq.cn.com"));
		assertEquals("mail.cn.qq", getSecondUrl("mail.cn.qq.cn.com"));
		assertEquals("", getSecondUrl("mail.qq.ccom"));
		assertEquals("mail", getSecondUrl("mail.qq.com.cn"));
		System.out.println(getSecondUrl("cykj.server.com"));
	}

}
