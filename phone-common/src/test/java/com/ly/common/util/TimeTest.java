package com.ly.common.util;

import java.sql.Date;

import com.ly.base.common.util.DateUtil;

public class TimeTest {
	public static void main(String[] args) {
		System.out.println(DateUtil.parse("2016-10-11 12:11"));
		System.out.println(DateUtil.parse("2016-10-11 12:11:11"));
		System.out.println(DateUtil.parse("2016-10-11"));
		System.out.println(DateUtil.parse("2016/10/11"));
	}
}
